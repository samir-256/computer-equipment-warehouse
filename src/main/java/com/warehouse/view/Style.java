package com.warehouse.view;

final class Style {

    private static final String RESET = "\u001B[0m";
    private static final String BOLD = "\u001B[1m";
    private static final String BOLD_CYAN = "\u001B[1;36m";
    private static final String BOLD_YELLOW = "\u001B[1;33m";
    private static final String GREY = "\u001B[90m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";

    private final boolean enabled;

    Style(boolean enabled) {
        this.enabled = enabled;
    }

    String header(String text) {
        return paint(BOLD_CYAN, text);
    }

    String title(String text) {
        return paint(BOLD_YELLOW, text);
    }

    String strong(String text) {
        return paint(BOLD, text);
    }

    String border(String text) {
        return paint(GREY, text);
    }

    String danger(String text) {
        return paint(RED, text);
    }

    String success(String text) {
        return paint(GREEN, text);
    }

    String prompt(String text) {
        return paint(GREEN, text);
    }

    private String paint(String code, String text) {
        return enabled ? code + text + RESET : text;
    }
}
