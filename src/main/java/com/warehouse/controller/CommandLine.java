package com.warehouse.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

record CommandLine(String name, List<String> positionals, Map<String, String> options) {

    CommandLine {
        positionals = List.copyOf(positionals);
        options = Map.copyOf(options);
    }

    static CommandLine parse(String text) {
        List<String> tokens = tokenize(text);
        if (tokens.isEmpty() || tokens.get(0).isBlank()) {
            throw new IllegalArgumentException("Empty command. Type 'help' to see the commands.");
        }
        List<String> positionals = new ArrayList<>();
        Map<String, String> options = new LinkedHashMap<>();
        for (String token : tokens.subList(1, tokens.size())) {
            int separator = token.indexOf('=');
            if (separator < 0) {
                positionals.add(token);
            } else {
                addOption(options, token, separator);
            }
        }
        return new CommandLine(tokens.get(0).toLowerCase(Locale.ROOT), positionals, options);
    }

    void requireNoArguments() {
        if (!positionals.isEmpty() || !options.isEmpty()) {
            throw new IllegalArgumentException("Command '" + name + "' takes no arguments");
        }
    }

    void requireOptionsIn(Set<String> allowed) {
        for (String key : new TreeSet<>(options.keySet())) {
            if (!allowed.contains(key)) {
                throw new IllegalArgumentException("Unknown parameter '" + key + "'. Allowed: "
                        + String.join(", ", new TreeSet<>(allowed)));
            }
        }
    }

    String singlePositional(String usage) {
        if (positionals.size() != 1) {
            throw new IllegalArgumentException("Expected exactly one category. Usage: " + usage);
        }
        return positionals.get(0);
    }

    private static void addOption(Map<String, String> options, String token, int separator) {
        String key = token.substring(0, separator).toLowerCase(Locale.ROOT);
        String value = token.substring(separator + 1);
        if (key.isEmpty()) {
            throw new IllegalArgumentException("Parameter '" + token + "' has no name");
        }
        if (value.isBlank()) {
            throw new IllegalArgumentException("Parameter '" + key + "' has no value");
        }
        if (options.put(key, value) != null) {
            throw new IllegalArgumentException("Parameter '" + key + "' is given more than once");
        }
    }

    private static List<String> tokenize(String text) {
        List<String> tokens = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean quoted = false;
        boolean started = false;
        for (char symbol : text.toCharArray()) {
            if (symbol == '"') {
                quoted = !quoted;
                started = true;
            } else if (Character.isWhitespace(symbol) && !quoted) {
                if (started) {
                    tokens.add(current.toString());
                    current.setLength(0);
                    started = false;
                }
            } else {
                current.append(symbol);
                started = true;
            }
        }
        if (quoted) {
            throw new IllegalArgumentException("Unclosed double quote in the command");
        }
        if (started) {
            tokens.add(current.toString());
        }
        return tokens;
    }
}
