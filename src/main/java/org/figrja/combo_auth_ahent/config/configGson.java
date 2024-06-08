package org.figrja.combo_auth_ahent.config;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;

public class configGson{
    private List<String> AuthList;
    private HashMap<String, AuthSchemaList> AuthSchema;
    private String debug;

    public configGson() {
    }

    @SerializedName("AuthSchema")
    public HashMap<String, AuthSchemaList> getAuthSchema() {
        return this.AuthSchema;
    }

    @SerializedName("debug")
    public String getGebugStatus() {
        return this.debug;
    }

    @SerializedName("AuthList")
    public List<String> getAuthList() {
        return this.AuthList;
    }
}
