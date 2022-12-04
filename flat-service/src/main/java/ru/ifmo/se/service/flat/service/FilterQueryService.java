package ru.ifmo.se.service.flat.service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.ifmo.se.common.entity.Flat;
import ru.ifmo.se.common.model.Furnish;
import ru.ifmo.se.common.model.Transport;
import ru.ifmo.se.common.model.View;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class FilterQueryService {
    private final static Pattern FILTER_LEFT_VALUE_PATTERN =
            Pattern.compile("filter\\[(?<field>" + String.join("|", FlatService.getFlatFields()) +")]");
    private final static String FILTER_LEFT_VALUE_FIELD_GROUP = "field";
    private final static Pattern FILTER_RIGHT_VALUE_PATTERN =
            Pattern.compile("<(?<operation>" + String.join("|", OPERATION.asStrings()) + ")>(?<value>.+)");
    private final static String FILTER_RIGHT_VALUE_OPERATION_GROUP = "operation";
    private final static String FILTER_RIGHT_VALUE_VALUE_GROUP = "value";

    private final CriteriaBuilder criteriaBuilder;
    private final EntityManager entityManager;

    public FilterQueryService(EntityManager entityManager) {
        this.entityManager = entityManager;
        criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    public Query generateQuery(Map<String, String> requestParams) {
        return entityManager.createQuery(generateCriteria(generateFilterClauses(requestParams)));
    }

    public Specification<Flat> generateSpecification(Map<String, String> requestParams) {
        return (root, criteriaQuery, criteriaBuilder) ->
                generateHighLevelPredicate(generateFilterClauses(requestParams), root, criteriaBuilder);
    }

    private CriteriaQuery<Flat> generateCriteria(List<FilterClause> filterClauses) {
        CriteriaQuery<Flat> criteriaQuery = criteriaBuilder.createQuery(Flat.class);
        Root<Flat> root = criteriaQuery.from(Flat.class);

        Predicate predicate = generateHighLevelPredicate(filterClauses, root, criteriaBuilder);
        criteriaQuery.select(root).where(predicate);
        return criteriaQuery;
    }

    private List<FilterClause> generateFilterClauses(Map<String, String> requestParams) {
        List<FilterClause> filterClauses = new LinkedList<>();

        for (var entry : requestParams.entrySet()) {
            String filterLeftValue = entry.getKey();
            Matcher filterLeftValueMatcher = FILTER_LEFT_VALUE_PATTERN.matcher(filterLeftValue);

            String filterRightValue = entry.getValue();
            Matcher filterRightValueMatcher = FILTER_RIGHT_VALUE_PATTERN.matcher(filterRightValue);

            if (filterLeftValueMatcher.matches() && filterRightValueMatcher.matches()) {
                String field = filterLeftValueMatcher.group(FILTER_LEFT_VALUE_FIELD_GROUP);
                String rightValue = filterRightValueMatcher.group(FILTER_RIGHT_VALUE_VALUE_GROUP);

                String operationString = filterRightValueMatcher.group(FILTER_RIGHT_VALUE_OPERATION_GROUP);
                OPERATION operation = OPERATION.fromString(operationString).orElseThrow(IllegalArgumentException::new);

                FilterClause filterClause = new FilterClause(field, operation, rightValue);
                filterClauses.add(filterClause);
            }
        }

        return filterClauses;
    }

    private Predicate generateHighLevelPredicate(List<FilterClause> filterClauses, Root<Flat> root,
                                                 CriteriaBuilder criteriaBuilder) {
        Predicate[] predicates = filterClauses.stream()
                .map(filterClause -> generatePredicate(root, filterClause))
                .toArray(Predicate[]::new);

        return criteriaBuilder.and(predicates);
    }

    private Predicate generatePredicate(Root<Flat> root, FilterClause filterClause) {
        Predicate predicate = null;
        String fieldString = filterClause.getField();
        String rightValueString = filterClause.getRightValue();
        Path field = root.get(fieldString);
        Object typedRightValue = getTypedRightValue(rightValueString, fieldString);

        switch (filterClause.operation) {
            case EQUAL: {
                predicate = criteriaBuilder.equal(field, typedRightValue);
                break;
            }
            case NOT_EQUAL: {
                predicate = criteriaBuilder.notEqual(field, typedRightValue);
                break;
            }
            case GREATER_THAN: {
                predicate = criteriaBuilder.greaterThan(field, rightValueString);
                break;
            }
            case LESS_THAN: {
                predicate = criteriaBuilder.lessThan(field, rightValueString);
                break;
            }
            case SUBSTR: {
                predicate = criteriaBuilder.like(field, "%" + rightValueString + "%");
                break;
            }
        }

        return predicate;
    }

    private Object getTypedRightValue(String rightValue,  String path) {
        Object typed = rightValue;

        switch (path) {
            case "furnish": {
                typed = Furnish.valueOf(rightValue);
                break;
            }
            case "view": {
                typed = View.valueOf(rightValue);
                break;
            }
            case "transport": {
                typed = Transport.valueOf(rightValue);
                break;
            }
            case "hasBalcony": {
                typed = Boolean.valueOf(rightValue);
                break;
            }
        }

        return typed;
    }

    @Data
    @AllArgsConstructor
    private static class FilterClause {
        private String field;
        private OPERATION operation;
        private String rightValue;
    }

    private enum OPERATION {
        EQUAL("eq"),
        NOT_EQUAL("neq"),
        GREATER_THAN("gt"),
        LESS_THAN("lt"),
        SUBSTR("substr");

        private final String string;

        OPERATION(String string) {
            this.string = string;
        }

        public static Optional<OPERATION> fromString(String string) {
            for (OPERATION operation : OPERATION.values()) {
                if (operation.getString().equals(string)) {
                    return Optional.of(operation);
                }
            }

            return Optional.empty();
        }

        public static List<String> asStrings() {
            return Arrays.stream(OPERATION.values())
                    .map(OPERATION::getString)
                    .collect(Collectors.toList());
        }

        public String getString() {
            return string;
        }
    }
}
