package org.figrja.combo_auth.config.debuglogger;

public class DebugAll extends LoggerMain {
    private String name;

    public DebugAll() {
        super();
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
