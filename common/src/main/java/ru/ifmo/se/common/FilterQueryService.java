package ru.ifmo.se.common;

import lombok.Data;
import org.springframework.stereotype.Service;
import ru.ifmo.se.common.service.FlatService;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class FilterQueryService {
    private final static Pattern FILTER_LEFT_VALUE_PATTERN =
            Pattern.compile("filter\\[(" + String.join("|", FlatService.getFlatFields()) +")]");
    private final static int FILTER_LEFT_VALUE_FIELD_GROUP = 1;
    private final static Pattern FILTER_RIGHT_VALUE_PATTERN =
            Pattern.compile("(?:" + String.join("|", OPERATION.asStrings()) + ")");

    private final CriteriaBuilder criteriaBuilder;
    private final EntityManager entityManager;

    public FilterQueryService(EntityManager entityManager) {
        this.entityManager = entityManager;
        criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    public Query generateQuery(Map<String, String> requestParams) {
        List<FilterClause> clauses = new LinkedList<>();

        for (var entry : requestParams.entrySet()) {
            String leftValue = entry.getKey();
            Matcher leftValueMatcher = FILTER_LEFT_VALUE_PATTERN.matcher(leftValue);

            String rightValue = entry.getValue();
            Matcher rightValueMatcher = FILTER_RIGHT_VALUE_PATTERN.matcher(rightValue);

            if (leftValueMatcher.matches() && rightValueMatcher.matches()) {
                String field = leftValueMatcher.group(FILTER_LEFT_VALUE_FIELD_GROUP);
            }
        }
    }

    private CriteriaQuery<Flat> generateCriteria(List<FilterClause> clauses) {

    }

    @Data
    private static class FilterClause {
        private String field;
        private OPERATION operation;
        private String rightValue;
    }

    private enum OPERATION {
        EQ("eq"),
        NEQ("neq"),
        GT("gt"),
        LT("lt"),
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
