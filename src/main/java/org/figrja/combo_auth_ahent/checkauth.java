package org.figrja.combo_auth_ahent;

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
import java.util.Map;

public class checkauth {

    public HashMap<String,Object> AuthListCheck(String profileName, String serverId) throws Exception {

        LoggerMain LOGGER = auth.Logger;
        configGson CONFIG = auth.getConfig();
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("username", profileName);
        arguments.put("serverId", serverId);
        Exception var6 = null;
        HashMap<String,Object> result = new HashMap<>();
        for (String name : CONFIG.getAuthList()) {
            LOGGER.debug("try " + name);
            AuthSchemaList authSchema = CONFIG.getAuthSchema().get(name);
            LOGGER.debugRes("in " + authSchema.getUrlCheck());
            URL url = httpHelper.concatenateURL(httpHelper.constantURL(authSchema.getUrlCheck()), httpHelper.buildQuery(arguments));

            try {
                resultElyGson response = httpHelper.makeRequest(url);
                if (response != null && response.getId() != null) {
                    LOGGER.debug("response not null");
                    result.put("name",response.getName());
                    result.put("id",response.getId());

                    PropertyMap properties = null;
                    if (response.getProperties() != null) {
                        LOGGER.debug("properties not null");
                        if (authSchema.getUrlProperty() != null) {
                            LOGGER.debug("custom property");
                            LOGGER.debugRes("in " + authSchema.getUrlProperty());
                            String PROPERTY_URL = authSchema.getUrlProperty();
                            URL p_url = httpHelper.constantURL(MessageFormat.format(PROPERTY_URL, profileName, response.getId()));
                            resultElyGson pr = httpHelper.makeRequest(p_url);
                            if (pr != null) {
                                properties = pr.getProperties();
                            } else {
                                LOGGER.debug("custom property is null");
                                properties = response.getProperties();
                            }
                        } else {
                            properties = response.getProperties();
                        }

                        result.put("properties",properties);
                    }

                    if (authSchema.getAddProperty() != null) {
                        LOGGER.debug("add custom property");
                        PropertyMap map = new PropertyMap();
                        propery[] AP = authSchema.getAddProperty();
                        if (properties != null) {
                            map.putAll(properties);
                        }
                        for (propery p : AP) {
                            if (p.signature() != null) {
                                map.put(p.name(), new Property(p.name(), p.value(), p.signature()));
                            } else {
                                map.put(p.name(), new Property(p.name(), p.value()));
                            }
                        }
                        result.put("properties",properties);
                    }

                    LOGGER.info("logging from " + name);
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
}