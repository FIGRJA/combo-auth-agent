package org.figrja.combo_auth_ahent.ely.by;

import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;

import java.util.UUID;

public class resultElyGson {
    private String error;
    private String errorMessage;
    String id;
    private UUID Id;
    private String name;
    private propery[] properties;

    public resultElyGson() {
    }

    public UUID getId() {
        return this.Id;
    }

    public void setId(UUID id) {
        this.Id = id;
    }

    public String getName() {
        return this.name;
    }

    public propery[] getProperties() {
        return properties;
    }

    public String getError() {
        return this.error;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }
}
