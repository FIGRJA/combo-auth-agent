package org.figrja.combo_auth.config.debuglogger;

public class DebugAll extends LoggerMain{

    private final String name;

    public DebugAll(String name) {
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
        System.out.println("["+name+"/debug] "+mes);
    }
}
