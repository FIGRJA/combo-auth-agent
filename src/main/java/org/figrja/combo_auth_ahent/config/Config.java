package org.figrja.combo_auth_ahent.config;

import java.util.HashMap;
import java.util.List;

public class Config {
    private List<String> AuthList;
    private HashMap<String, AuthSchemaList> AuthSchema;
    private String debug;

    public Config() {
    }

    public HashMap<String, AuthSchemaList> getAuthSchema() {
        return this.AuthSchema;
    }

    public String getGebugStatus() {
        return this.debug;
    }

    public List<String> getAuthList() {
        return this.AuthList;
    }
}
