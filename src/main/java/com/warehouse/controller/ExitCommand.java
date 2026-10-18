package com.warehouse.controller;

final class ExitCommand implements Command {

    @Override
    public Response execute(CommandLine line) {
        line.requireNoArguments();
        return ResponseImpl.exit("Goodbye!");
    }
}
