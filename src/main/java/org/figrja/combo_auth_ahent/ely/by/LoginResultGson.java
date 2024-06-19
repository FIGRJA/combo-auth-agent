package org.figrja.combo_auth_ahent.ely.by;


public class LoginResultGson {

    public LoginResultGson(String id, String name, propery[] properties){
        this.name = name;
        this.id = id;
        this.properties = properties;
    }

    private String id;
    private String name;
    private propery[] properties;

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public propery[] getProperties() {
        return this.properties;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProperties(propery[] properties) {
        this.properties = properties;
    }

}
