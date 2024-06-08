package org.figrja.combo_auth_ahent.config;

import java.util.HashMap;
import java.util.List;

public class Config {
    private final List<String> AuthList;
    private final HashMap<String, SchemaList> AuthSchema;
    private final String debug;

    public Config(List<String> AuthList, HashMap<String, SchemaList> AuthSchema, String debug) {
        this.AuthList = AuthList;
        this.AuthSchema = AuthSchema;
        this.debug = debug;
    }

    public HashMap<String, SchemaList> getAuthSchema() {
        return this.AuthSchema;
    }

    public String getGebugStatus() {
        return this.debug;
    }

    public List<String> getAuthList() {
        return this.AuthList;
    }
}
