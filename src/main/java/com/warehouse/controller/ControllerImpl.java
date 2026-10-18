package com.warehouse.controller;

import com.warehouse.service.ServiceException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ControllerImpl implements Controller {

    private static final Logger LOGGER = Logger.getLogger(ControllerImpl.class.getName());

    private final CommandProvider provider;

    public ControllerImpl(CommandProvider provider) {
        this.provider = Objects.requireNonNull(provider, "Command provider must not be null");
    }

    @Override
    public Response handle(Request request) {
        Objects.requireNonNull(request, "Request must not be null");
        try {
            CommandLine line = CommandLine.parse(request.requestString());
            return provider.get(line.name()).execute(line);
        } catch (IllegalArgumentException e) {
            return ResponseImpl.error(e.getMessage());
        } catch (ServiceException e) {
            LOGGER.log(Level.SEVERE, "Service failure", e);
            return ResponseImpl.fatal("Fatal error: " + e.getMessage());
        }
    }
}
