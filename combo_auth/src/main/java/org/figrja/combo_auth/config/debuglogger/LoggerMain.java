package org.figrja.combo_auth.config.debuglogger;

public abstract class LoggerMain {
    private String name = "combo-auth";
    public LoggerMain() {
    }

    public abstract void info(String var1);

    public abstract void debug(String var1);

    public abstract void debugRes(String var1);
}
