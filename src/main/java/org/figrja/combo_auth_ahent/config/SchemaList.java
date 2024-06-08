package org.figrja.combo_auth_ahent.config;

import org.figrja.combo_auth_ahent.ely.by.propery;

public class SchemaList {
    private final String url_check;
    private final String url_property;
    private final propery[] AddProperty;

    public SchemaList(String url_check , String url_property , propery[] AddProperty) {
        this.url_check = url_check;
        this.url_property = url_property;
        this.AddProperty = AddProperty;
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
