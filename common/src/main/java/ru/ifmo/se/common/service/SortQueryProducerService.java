package ru.ifmo.se.common.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SortQueryProducerService {
    private static final Pattern SORT_PARAM_NAME_PATTERN =
            Pattern.compile("sort\\[(" + String.join("|", FlatService.getFlatFields()) + ")\\]");
    private static final Integer PATTERN_FIELD_NAME_GROUP = 1;
    private static final String ASC = "asc";
    private static final String DESC = "desc";

    public Sort parseSortQuery(Map<String, String> requestParams) {
        List<Sort.Order> orders = new LinkedList<>();

        for (var entry : requestParams.entrySet()) {
            String paramName = entry.getKey();
            Matcher matcher = SORT_PARAM_NAME_PATTERN.matcher(paramName);
            if (!matcher.matches()) {
                continue;
            }

            String field = matcher.group(PATTERN_FIELD_NAME_GROUP);
            String sortType = entry.getValue();
            parseSortOrder(field, sortType).ifPresent(orders::add);
        }

        return Sort.by(orders);
    }

    private Optional<Sort.Order> parseSortOrder(String field, String sortType) {
        if (sortType.equals(ASC)) {
            return Optional.of(Sort.Order.asc(field));
        } else if (sortType.equals(DESC)) {
            return Optional.of(Sort.Order.desc(field));
        } else {
            return Optional.empty();
        }
    }
}
