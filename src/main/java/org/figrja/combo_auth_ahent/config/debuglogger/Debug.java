package org.figrja.combo_auth_ahent.config.debuglogger;

public class Debug extends LoggerMain {
    private final String name;

    public Debug(String name) {
        this.name = name;
    }

    public void info(String mes) {
        System.out.println("[" + this.name + "/info] " + mes);
    }

    public void debug(String mes) {
        System.out.println("[" + this.name + "/debug] " + mes);
    }

    public void debugRes(String mes) {
    }
}
