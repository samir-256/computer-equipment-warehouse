package com.warehouse.controller;

import com.warehouse.service.EquipmentService;
import java.util.Map;
import java.util.Objects;

public final class CommandProvider {

    private final Map<String, Command> commands;
    private final Command wrongCommand = new WrongCommand();

    public CommandProvider(EquipmentService service) {
        Objects.requireNonNull(service, "Service must not be null");
        this.commands = Map.of(
                "find", new FindCommand(service),
                "list", new ListCommand(service),
                "help", new HelpCommand(),
                "exit", new ExitCommand());
    }

    Command get(String name) {
        return commands.getOrDefault(name, wrongCommand);
    }
}
