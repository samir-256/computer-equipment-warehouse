package com.warehouse.controller;

import com.warehouse.service.ServiceException;

interface Command {

    Response execute(CommandLine line) throws ServiceException;
}
