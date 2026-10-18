package com.warehouse.controller;

import com.warehouse.controller.Table.Alignment;
import com.warehouse.controller.Table.Cell;
import com.warehouse.controller.Table.Column;
import com.warehouse.entity.Equipment;
import com.warehouse.service.Category;
import java.util.List;
import java.util.Locale;

final class ProductTableBuilder {

    private static final List<Column> COLUMNS = List.of(
            new Column("ID", Alignment.RIGHT),
            new Column("Category", Alignment.LEFT),
            new Column("Name", Alignment.LEFT),
            new Column("Brand", Alignment.LEFT),
            new Column("Price", Alignment.RIGHT),
            new Column("Qty", Alignment.RIGHT),
            new Column("Total", Alignment.RIGHT),
            new Column("Details", Alignment.LEFT));

    private ProductTableBuilder() {
    }

    static Table build(Category category, List<Equipment> items) {
        List<List<Cell>> rows = items.stream().map(ProductTableBuilder::row).toList();
        return new Table(title(category), COLUMNS, rows, footer(items));
    }

    static String title(Category category) {
        if (category == Category.ALL) {
            return "All products";
        }
        String name = category.name().toLowerCase(Locale.ROOT);
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }

    private static List<Cell> row(Equipment item) {
        String quantity = String.valueOf(item.getQuantity());
        return List.of(
                Cell.of(String.valueOf(item.getId())),
                Cell.of(item.getClass().getSimpleName()),
                Cell.of(item.getName()),
                Cell.of(item.getBrand()),
                Cell.of(money(item.getPrice())),
                item.getQuantity() == 0 ? Cell.warning(quantity) : Cell.of(quantity),
                Cell.of(money(item.totalCost())),
                Cell.of(item.details()));
    }

    private static List<Cell> footer(List<Equipment> items) {
        int quantity = items.stream().mapToInt(Equipment::getQuantity).sum();
        double total = items.stream().mapToDouble(Equipment::totalCost).sum();
        return List.of(
                Cell.of(""),
                Cell.of(""),
                Cell.of("Total"),
                Cell.of(""),
                Cell.of(""),
                Cell.of(String.valueOf(quantity)),
                Cell.of(money(total)),
                Cell.of(""));
    }

    private static String money(double value) {
        return String.format(Locale.US, "%.2f", value);
    }
}
