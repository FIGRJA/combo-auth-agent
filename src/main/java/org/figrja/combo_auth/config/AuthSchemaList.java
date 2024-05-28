package org.figrja.combo_auth.config;

import com.google.gson.annotations.SerializedName;

public class AuthSchemaList {
    private String url_check;

    private String url_property;

    private pro[] AddProperty;

    @SerializedName("AddProperty")
    public pro[] getAddProperty() {
        return AddProperty;
    }



    @SerializedName("url_check")
    public String getUrlCheck() {
        return url_check;
    }

    @SerializedName("url_property")
    public String getUrlProperty() {
        return url_property;
    }
}
