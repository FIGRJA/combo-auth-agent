package org.figrja.combo_auth.config.debuglogger;

public class Logger extends LoggerMain {
    private final String name;

    public Logger(String name) {
        this.name = name;
    }

    public void info(String mes) {
        System.out.println("[" + this.name + "/info] " + mes);
    }

    public void debug(String mes) {
    }

    public void debugRes(String mes) {
    }
}
