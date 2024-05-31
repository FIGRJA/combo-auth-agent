package org.figrja.combo_auth;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import org.figrja.combo_auth.config.AuthSchemaList;
import org.figrja.combo_auth.config.configGson;
import org.figrja.combo_auth.config.debuglogger.LoggerMain;
import org.figrja.combo_auth.config.pro;
import org.figrja.combo_auth.ely.by.httpHelper;
import org.figrja.combo_auth.ely.by.resultElyGson;

import java.net.URL;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class checkauth {

    LoggerMain LOGGER = auth.Logger;

    configGson CONFIG = auth.getConfig();

    public GameProfile AuthListCheck(String profileName, String serverId) throws AuthenticationUnavailableException {
        Map<String, Object> arguments = new HashMap();
        arguments.put("username", profileName);
        arguments.put("serverId", serverId);
        boolean tr = false;
        AuthenticationUnavailableException var6 = null;
        boolean AuthenticationException = false;
        for(String name :CONFIG.getAuthList()) {
            LOGGER.debug("try "+name);
            AuthSchemaList authSchema = CONFIG.getAuthSchema().get(name);
            LOGGER.debugRes("in "+authSchema.getUrlCheck());
            URL url = httpHelper.concatenateURL(httpHelper.constantURL(authSchema.getUrlCheck()), httpHelper.buildQuery(arguments));
            try {
                resultElyGson response = httpHelper.makeRequest(url);
                if (response != null && response.getId() != null) {
                    LOGGER.debug("response not null");
                    final GameProfile result = new GameProfile(response.getId(), response.getName());

                    if (response.getProperties() != null) {
                        PropertyMap properties = new PropertyMap();
                        LOGGER.debug("properties not null");
                        if (authSchema.getUrlProperty()!=null) {
                            LOGGER.debug("custom property");
                            LOGGER.debugRes("in "+authSchema.getUrlProperty() );
                            String PROPERTY_URL = authSchema.getUrlProperty();
                            URL p_url = httpHelper.concatenateURL(httpHelper.constantURL(MessageFormat.format(PROPERTY_URL, profileName, response.getId())), httpHelper.buildQuery(null));
                            resultElyGson pr = httpHelper.makeRequest(p_url);
                            if (pr != null) {
                                properties = pr.getProperties();
                            }else {
                                LOGGER.debug("custom property is null");
                                properties = response.getProperties();
                            }
                        } else {
                            properties = response.getProperties();
                        }
                        result.getProperties().putAll(properties);
                    }
                    if (authSchema.getAddProperty()!=null){
                        LOGGER.debug("add custom property");
                        result.getProperties().putAll(getProperty(authSchema.getAddProperty()));
                    }
                    LOGGER.info("logging from "+name);
                    return result;
                }
            } catch (AuthenticationUnavailableException var7) {
                tr = true;
                var6 = var7;
            }
        }
        if (tr){
            throw var6;
        }
        return null;
    }

    public PropertyMap getProperty(pro[] AddProperty){
        PropertyMap map = new PropertyMap();
        for (pro p : AddProperty){
            if (p.signature()!=null) {
                map.put(p.name(), new Property(p.name(), p.value(), p.signature()));
            }else {
                map.put(p.name(), new Property(p.name(), p.value()));
            }
        }
        return map;
    }


}
