package ru.ifmo.se.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;
import ru.ifmo.se.common.service.FlatService;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.*;
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

        return entityManager.createQuery(generateCriteria(filterClauses));
    }

    private CriteriaQuery<Flat> generateCriteria(List<FilterClause> filterClauses) {
        CriteriaQuery<Flat> criteriaQuery = criteriaBuilder.createQuery(Flat.class);
        Root<Flat> root = criteriaQuery.from(Flat.class);

        Predicate[] predicates = filterClauses.stream()
                .map(filterClause -> generatePredicate(root, filterClause))
                .toArray(Predicate[]::new);

        criteriaQuery.select(root).where(predicates);

        return criteriaQuery;
    }

    private Predicate generatePredicate(Root<Flat> root, FilterClause filterClause) {
        Predicate predicate = null;
        Path field = root.get(filterClause.getField());
        String rightValue = filterClause.getRightValue();

        switch (filterClause.operation) {
            case EQUAL: {
                predicate = criteriaBuilder.equal(field, rightValue);
                break;
            }
            case NOT_EQUAL: {
                predicate = criteriaBuilder.notEqual(field, rightValue);
                break;
            }
            case GREATER_THAN: {
                predicate = criteriaBuilder.greaterThan(field, rightValue);
                break;
            }
            case LESS_THAN: {
                predicate = criteriaBuilder.lessThan(field, rightValue);
                break;
            }
            case SUBSTR: {
                predicate = criteriaBuilder.like(field, "%" + rightValue + "%");
                break;
            }
        }

        return predicate;
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
