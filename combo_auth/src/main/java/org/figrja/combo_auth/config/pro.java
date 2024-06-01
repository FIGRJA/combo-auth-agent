package org.figrja.combo_auth.config;

import com.google.gson.annotations.SerializedName;

public class pro {
    private String name;
    private String value;
    private String signature;

    public pro(String name, String value) {
        this(name, value, (String)null);
    }

    public pro(String name, String value, String signature) {
        this.name = name;
        this.value = value;
        this.signature = signature;
    }

    @SerializedName("name")
    public String name() {
        return this.name;
    }

    @SerializedName("value")
    public String value() {
        return this.value;
    }

    @SerializedName("signature")
    public String signature() {
        return this.signature;
    }
}
