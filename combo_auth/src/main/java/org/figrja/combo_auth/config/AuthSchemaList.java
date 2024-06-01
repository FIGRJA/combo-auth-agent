package org.figrja.combo_auth.config;

import com.google.gson.annotations.SerializedName;

public class AuthSchemaList {
    private String url_check;
    private String url_property;
    private pro[] AddProperty;

    public AuthSchemaList() {
    }

    @SerializedName("AddProperty")
    public pro[] getAddProperty() {
        return this.AddProperty;
    }

    @SerializedName("url_check")
    public String getUrlCheck() {
        return this.url_check;
    }

    @SerializedName("url_property")
    public String getUrlProperty() {
        return this.url_property;
    }
}
