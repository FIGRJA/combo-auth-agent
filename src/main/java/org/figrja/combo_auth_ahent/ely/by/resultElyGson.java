package org.figrja.combo_auth_ahent.ely.by;


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
        String comUUID = id.length() == 32 ? id.substring(0, 8) +
                "-" + id.substring(8, 12) +
                "-" + id.substring(12, 16) +
                "-" + id.substring(16, 20) +
                "-" + id.substring(20) : id;
        return UUID.fromString(comUUID);
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
