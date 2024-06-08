package org.figrja.combo_auth_ahent.config;

import org.figrja.combo_auth_ahent.ely.by.propery;

public class SchemaList {
    private String url_check;
    private String url_property;
    private propery[] AddProperty;

    public SchemaList() {
    }

    public propery[] getAddProperty() {
        return this.AddProperty;
    }

    public String getUrlCheck() {
        return this.url_check;
    }

    public String getUrlProperty() {
        return this.url_property;
    }
}
