package com.overops.plugins.service.impl;

import jetbrains.buildServer.agent.BuildProgressLogger;
import org.jetbrains.annotations.NotNull;

import java.io.*;

public class TeamCityPrintWriter extends PrintStream {

    private BuildProgressLogger logger;

    public TeamCityPrintWriter(@NotNull OutputStream out, BuildProgressLogger logger) {
        super(out);
        this.logger = logger;
    }

    public void print(String s) {
        super.print(s);
        logger.message(s);
    }
}
