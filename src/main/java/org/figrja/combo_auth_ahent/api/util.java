package org.figrja.combo_auth_ahent.api;

import java.util.HashMap;

public class util {

    private static final HashMap<String,base> PlayerBase = new HashMap<>();

    static public HashMap<String, base> getPlayerBase() {
        return PlayerBase;
    }
}
