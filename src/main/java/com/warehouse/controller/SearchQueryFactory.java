package com.warehouse.controller;

import com.warehouse.entity.ConnectionType;
import com.warehouse.service.Category;
import com.warehouse.service.SearchQuery;
import com.warehouse.service.SortField;
import com.warehouse.service.SortOrder;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

final class SearchQueryFactory {

    static final String FIND_USAGE = "find <category> [filters] [sort=<field>[:asc|:desc]]";
    static final String LIST_USAGE = "list <category> [sort=<field>[:asc|:desc]]";

    private static final Set<String> LIST_KEYS = Set.of("sort");
    private static final Set<String> FIND_KEYS = Set.of("id", "name", "brand", "minprice", "maxprice",
            "minqty", "maxqty", "minscreen", "maxscreen", "connection", "sort");

    private static final Map<String, Category> CATEGORIES = byName(Category.values());
    private static final Map<String, ConnectionType> CONNECTIONS = byName(ConnectionType.values());
    private static final Map<String, SortField> SORT_FIELDS = Map.of(
            "id", SortField.ID,
            "name", SortField.NAME,
            "brand", SortField.BRAND,
            "price", SortField.PRICE,
            "quantity", SortField.QUANTITY,
            "total", SortField.TOTAL_COST);
    private static final Map<String, Boolean> DIRECTIONS = Map.of("asc", false, "desc", true);

    private SearchQueryFactory() {
    }

    static SearchQuery forFind(CommandLine line) {
        line.requireOptionsIn(FIND_KEYS);
        Map<String, String> options = line.options();
        SearchQuery.Builder builder = baseBuilder(line, FIND_USAGE);
        if (options.containsKey("id")) {
            builder.id(ArgumentParser.integer("id", options.get("id")));
        }
        if (options.containsKey("connection")) {
            builder.connectionType(ArgumentParser.choice("connection", options.get("connection"), CONNECTIONS));
        }
        return builder
                .name(options.get("name"))
                .brand(options.get("brand"))
                .price(ArgumentParser.decimalRange(options, "minprice", "maxprice"))
                .quantity(ArgumentParser.integerRange(options, "minqty", "maxqty"))
                .screenSize(ArgumentParser.decimalRange(options, "minscreen", "maxscreen"))
                .build();
    }

    static SearchQuery forList(CommandLine line) {
        line.requireOptionsIn(LIST_KEYS);
        return baseBuilder(line, LIST_USAGE).build();
    }

    private static SearchQuery.Builder baseBuilder(CommandLine line, String usage) {
        Category category = ArgumentParser.choice("category", line.singlePositional(usage), CATEGORIES);
        SearchQuery.Builder builder = SearchQuery.builder().category(category);
        if (line.options().containsKey("sort")) {
            builder.sortOrder(sortOrder(line.options().get("sort")));
        }
        return builder;
    }

    private static SortOrder sortOrder(String text) {
        String[] parts = text.split(":", -1);
        if (parts.length > 2) {
            throw new IllegalArgumentException("Parameter 'sort' must look like field or field:desc, but was '"
                    + text + "'");
        }
        SortField field = ArgumentParser.choice("sort", parts[0], SORT_FIELDS);
        boolean descending = parts.length == 2 && ArgumentParser.choice("sort direction", parts[1], DIRECTIONS);
        return new SortOrder(field, descending);
    }

    private static <E extends Enum<E>> Map<String, E> byName(E[] values) {
        return Arrays.stream(values)
                .collect(Collectors.toUnmodifiableMap(value -> value.name().toLowerCase(), Function.identity()));
    }
}
