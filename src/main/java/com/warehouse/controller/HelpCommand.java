package com.warehouse.controller;

final class HelpCommand implements Command {

    static final String TEXT = """
            Commands:
              find <category> [filters] [sort=<field>[:asc|:desc]]   search products
              list <category> [sort=<field>[:asc|:desc]]             show all products of a category
              help                                                   show this menu
              exit                                                   quit the application
            Categories: laptops, monitors, mice, keyboards, all
            Filters:
              id=<number>                  name=<text> (part of the name)   brand=<text>
              minprice=<n> maxprice=<n>    minqty=<n> maxqty=<n>
              minscreen=<n> maxscreen=<n>  (laptops, monitors, all)
              connection=wired|wireless    (mice, keyboards, all)
            Sort fields: id, name, brand, price, quantity, total
            Values with spaces go in double quotes, for example name="XPS 13"
            Examples:
              find laptops brand=Dell maxprice=1000 sort=price:desc
              find all name=pro minqty=1
              list monitors sort=name""";

    @Override
    public Response execute(CommandLine line) {
        line.requireNoArguments();
        return ResponseImpl.ok(TEXT);
    }
}
