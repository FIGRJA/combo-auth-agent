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

    public PropertyMap getProperties() {
        PropertyMap pm = new PropertyMap();
        propery[] var2 = this.properties;
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            propery p = var2[var4];
            pm.put(p.name(), new Property(p.name(), p.value(), p.signature()));
        }

        return pm;
    }

    public String getError() {
        return this.error;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }
}
