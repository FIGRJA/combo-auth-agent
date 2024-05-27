package org.figrja.combo_auth.config.debuglogger;

public class Debug extends LoggerMain{
    private final String name;

    public Debug(String name) {
        this.name = name;
    }
    @Override
    public void info(String mes) {
        System.out.println("["+name+"/info] "+mes);
    }

    @Override
    public void debug(String mes) {
        System.out.println("["+name+"/debug] "+mes);
    }
    @Override
    public void debugRes(String mes) {
        return;
    }
}
