package org.figrja.combo_auth.config.debuglogger;

public class Logger extends LoggerMain{
    private final String name;

    public Logger(String name) {
        this.name = name;
    }
    @Override
    public void info(String mes) {
        System.out.println("["+name+"/info] "+mes);
    }

    @Override
    public void debug(String mes) {
        return;
    }

    @Override
    public void debugRes(String mes) {
        return;
    }
}
