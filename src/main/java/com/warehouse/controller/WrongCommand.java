package com.warehouse.controller;

final class WrongCommand implements Command {

    @Override
    public Response execute(CommandLine line) {
        return ResponseImpl.error("Unknown command '" + line.name() + "'. Type 'help' to see the commands.");
    }
}
