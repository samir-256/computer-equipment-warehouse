package com.warehouse.view;

public enum BorderStyle {
    UNICODE("┌", "┬", "┐", "─", "│", "├", "┼", "┤",
            "└", "┴", "┘"),
    ASCII("+", "+", "+", "-", "|", "+", "+", "+", "+", "+", "+");

    private final String topLeft;
    private final String topJoin;
    private final String topRight;
    private final String horizontal;
    private final String vertical;
    private final String middleLeft;
    private final String cross;
    private final String middleRight;
    private final String bottomLeft;
    private final String bottomJoin;
    private final String bottomRight;

    BorderStyle(String topLeft, String topJoin, String topRight, String horizontal, String vertical,
                String middleLeft, String cross, String middleRight,
                String bottomLeft, String bottomJoin, String bottomRight) {
        this.topLeft = topLeft;
        this.topJoin = topJoin;
        this.topRight = topRight;
        this.horizontal = horizontal;
        this.vertical = vertical;
        this.middleLeft = middleLeft;
        this.cross = cross;
        this.middleRight = middleRight;
        this.bottomLeft = bottomLeft;
        this.bottomJoin = bottomJoin;
        this.bottomRight = bottomRight;
    }

    String topLeft() {
        return topLeft;
    }

    String topJoin() {
        return topJoin;
    }

    String topRight() {
        return topRight;
    }

    String horizontal() {
        return horizontal;
    }

    String vertical() {
        return vertical;
    }

    String middleLeft() {
        return middleLeft;
    }

    String cross() {
        return cross;
    }

    String middleRight() {
        return middleRight;
    }

    String bottomLeft() {
        return bottomLeft;
    }

    String bottomJoin() {
        return bottomJoin;
    }

    String bottomRight() {
        return bottomRight;
    }
}
