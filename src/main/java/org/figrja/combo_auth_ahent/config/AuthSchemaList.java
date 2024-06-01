package org.figrja.combo_auth_ahent.config;

import com.google.gson.annotations.SerializedName;
import org.figrja.combo_auth_ahent.ely.by.propery;

public class AuthSchemaList {
    private String url_check;
    private String url_property;
    private propery[] AddProperty;

    public AuthSchemaList() {
    }

    @SerializedName("AddProperty")
    public propery[] getAddProperty() {
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
