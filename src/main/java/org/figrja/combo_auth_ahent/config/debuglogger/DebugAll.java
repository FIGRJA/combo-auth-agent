package org.figrja.combo_auth_ahent.config.debuglogger;

public class DebugAll extends LoggerMain {
    private final String name;

    public DebugAll(String name) {
        this.name = name;
    }

    public void info(String mes) {
        System.out.println("[" + this.name + "/info] " + mes);
    }

    public void debug(String mes) {
        System.out.println("[" + this.name + "/debug] " + mes);
    }


    public void debugRes(String mes) {
        System.out.println("[" + this.name + "/deBug] " + mes);
    }
}
