package org.figrja.combo_auth_ahent;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import org.figrja.combo_auth_ahent.config.AuthSchemaList;
import org.figrja.combo_auth_ahent.config.configGson;
import org.figrja.combo_auth_ahent.config.debuglogger.LoggerMain;
import org.figrja.combo_auth_ahent.ely.by.httpHelper;
import org.figrja.combo_auth_ahent.ely.by.propery;
import org.figrja.combo_auth_ahent.ely.by.resultElyGson;

import java.net.URL;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class checkauth {
    LoggerMain LOGGER;
    configGson CONFIG;

    public checkauth() {
        this.LOGGER = auth.Logger;
        this.CONFIG = auth.getConfig();
    }

    public GameProfile AuthListCheck(String profileName, String serverId) throws Exception {
        Map<String, Object> arguments = new HashMap();
        arguments.put("username", profileName);
        arguments.put("serverId", serverId);
        Exception var6 = null;
        Iterator var7 = this.CONFIG.getAuthList().iterator();
        GameProfile result ;
        while(var7.hasNext()) {
            String name = (String)var7.next();
            this.LOGGER.debug("try " + name);
            AuthSchemaList authSchema = (AuthSchemaList)this.CONFIG.getAuthSchema().get(name);
            this.LOGGER.debugRes("in " + authSchema.getUrlCheck());
            URL url = httpHelper.concatenateURL(httpHelper.constantURL(authSchema.getUrlCheck()), httpHelper.buildQuery(arguments));

            try {
                resultElyGson response = httpHelper.makeRequest(url);
                if (response != null && response.getId() != null) {
                    this.LOGGER.debug("response not null");
                    result = new GameProfile(response.getId(), response.getName());
                    if (response.getProperties() != null) {
                        new PropertyMap();
                        this.LOGGER.debug("properties not null");
                        PropertyMap properties;
                        if (authSchema.getUrlProperty() != null) {
                            this.LOGGER.debug("custom property");
                            this.LOGGER.debugRes("in " + authSchema.getUrlProperty());
                            String PROPERTY_URL = authSchema.getUrlProperty();
                            URL p_url = httpHelper.concatenateURL(httpHelper.constantURL(MessageFormat.format(PROPERTY_URL, profileName, response.getId())), httpHelper.buildQuery((Map)null));
                            resultElyGson pr = httpHelper.makeRequest(p_url);
                            if (pr != null) {
                                properties = pr.getProperties();
                            } else {
                                this.LOGGER.debug("custom property is null");
                                properties = response.getProperties();
                            }
                        } else {
                            properties = response.getProperties();
                        }

                        result.getProperties().putAll(properties);
                    }

                    if (authSchema.getAddProperty() != null) {
                        this.LOGGER.debug("add custom property");
                        result.getProperties().putAll(this.getProperty(authSchema.getAddProperty()));
                    }

                    this.LOGGER.info("logging from " + name);
                    return result;
                }
            } catch (Exception var17) {
                var6 = var17;
            }
        }

        if (var6!=null) {
            throw var6;
        }
        return null;

    }

    public PropertyMap getProperty(propery[] AddProperty) {
        PropertyMap map = new PropertyMap();
        propery[] var3 = AddProperty;
        int var4 = AddProperty.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            propery p = var3[var5];
            if (p.signature() != null) {
                map.put(p.name(), new Property(p.name(), p.value(), p.signature()));
            } else {
                map.put(p.name(), new Property(p.name(), p.value()));
            }
        }

        return map;
    }
}