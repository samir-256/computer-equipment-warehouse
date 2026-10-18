# Computer Equipment Warehouse

Console application for searching products in a computer equipment warehouse.
Java 21, Maven, JUnit 5. No frameworks.

## Build and run

```
mvn test
mvn package
java -jar target/computer-equipment-warehouse-1.0.jar
```

Options:

```
java -jar target/computer-equipment-warehouse-1.0.jar [--config <properties-file>] [--batch <commands-file>]
```

- `--config` uses another properties file from the classpath (default `app.properties`).
- `--batch` reads the commands from a text file, one per line, instead of the keyboard.

## Commands

| Command | Meaning |
|---|---|
| `find <category> [filters] [sort=<field>[:asc\|:desc]]` | search products |
| `list <category> [sort=<field>[:asc\|:desc]]` | show all products of a category |
| `help` | show the menu |
| `exit` | quit |

- Categories: `laptops`, `monitors`, `mice`, `keyboards`, `all`
- Filters: `id`, `name`, `brand`, `minprice`, `maxprice`, `minqty`, `maxqty`, `minscreen`, `maxscreen`, `connection=wired|wireless`
- Sort fields: `id`, `name`, `brand`, `price`, `quantity`, `total`
- Values with spaces go in double quotes: `name="XPS 13"`

Examples:

```
find laptops brand=Dell maxprice=1000 sort=price:desc
find all name=pro minqty=1
find mice connection=wireless
list monitors sort=name
```

## Data

Inventory is read from `;`-separated CSV files in `src/main/resources`
(`laptops.csv`, `monitors.csv`, `mice.csv`, `keyboards.csv`). The first line is a header.
File names are set in `app.properties`.

## Architecture

Each layer talks only to the layer next to it.

| Package | Role |
|---|---|
| `entity` | `Equipment` and its children `Laptop`, `Monitor`, `Mouse`, `Keyboard` |
| `dao` | reads CSV files, finds products by search criteria |
| `service` | validates the query, sorts the result, wraps DAO errors |
| `controller` | parses commands, runs them (command pattern), builds the answer text |
| `view` | reads input, shows answers |
| `startup` | reads `app.properties`, creates the objects, starts the view |
| `common` | small shared helpers (`Range`, `Holder`) |

## Errors

- Wrong command or wrong value: a clear message, the application continues.
- Broken data file: a message with the file name and line number, the application stops.
- Unexpected error: logged, a short message is shown, the application stops.
