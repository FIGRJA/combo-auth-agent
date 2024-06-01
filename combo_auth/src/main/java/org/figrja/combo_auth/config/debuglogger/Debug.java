package org.figrja.combo_auth.config.debuglogger;

public class Debug extends LoggerMain {

    private String name;
    public Debug(){super();}


    public void info(String mes) {
        System.out.println("[" + this.name + "/info] " + mes);
    }

    public void debug(String mes) {
        System.out.println("[" + this.name + "/debug] " + mes);
    }

    public void debugRes(String mes) {
    }
}
