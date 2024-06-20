package org.figrja.combo_auth_ahent;

import org.figrja.combo_auth_ahent.config.SchemaList;
import org.figrja.combo_auth_ahent.config.Config;
import org.figrja.combo_auth_ahent.config.debuglogger.LoggerMain;
import org.figrja.combo_auth_ahent.ely.by.LoginResultGson;
import org.figrja.combo_auth_ahent.ely.by.httpHelper;
import org.figrja.combo_auth_ahent.ely.by.propery;
import org.figrja.combo_auth_ahent.ely.by.resultElyGson;

import java.net.URL;
import java.text.MessageFormat;
import java.util.*;

public class checkauth {
    static LoggerMain LOGGER = Premain.LOGGER;
    static Config CONFIG = Premain.config;
    static String serverid;
    static String name;


    public HashMap<String,Object> AuthListCheck(String profileName, String serverId) throws Exception {
        if (CONFIG == null){
            Premain.newClass();
            new Premain();
            LOGGER = Premain.LOGGER;
            CONFIG = Premain.config;
        }
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("username", profileName);
        arguments.put("serverId", serverId);
        Exception var6 = null;
        HashMap<String,Object> result = new HashMap<>();
        for (String name : CONFIG.getAuthList()) {
            LOGGER.debug("try " + name);
            SchemaList authSchema = CONFIG.getAuthSchema().get(name);
            LOGGER.debugRes("in " + authSchema.getUrlCheck());
            URL url = httpHelper.concatenateURL(httpHelper.constantURL(authSchema.getUrlCheck()), httpHelper.buildQuery(arguments));

            try {
                resultElyGson response = httpHelper.makeRequest(url);
                if (response != null && response.getId() != null) {
                    LOGGER.debug("response not null");
                    result.put("name",response.getName());
                    result.put("id",response.getId());
                    ArrayList<propery> properties = new ArrayList<>();

                    if (response.getProperties() != null) {
                        LOGGER.debug("properties not null");
                        if (authSchema.getUrlProperty() != null) {
                            LOGGER.debug("custom property");
                            LOGGER.debugRes("in " + authSchema.getUrlProperty());
                            String PROPERTY_URL = authSchema.getUrlProperty();
                            URL p_url = httpHelper.constantURL(MessageFormat.format(PROPERTY_URL, profileName, response.getId()));
                            resultElyGson pr = httpHelper.makeRequest(p_url);
                            if (pr != null) {
                                properties.addAll(Arrays.asList(pr.getProperties()));
                            } else {
                                LOGGER.debug("custom property is null");
                                properties.addAll(Arrays.asList(response.getProperties()));
                            }
                        } else {
                            properties.addAll(Arrays.asList(response.getProperties()));
                        }

                    }

                    if (authSchema.getAddProperty() != null) {
                        LOGGER.debug("add custom property");
                        properties.addAll(Arrays.asList(authSchema.getAddProperty()));
                    }
                    result.put("properties",properties);

                    LOGGER.info("logging from " + name);
                    return result;
                }
            } catch (Exception var17) {
                var6 = var17;
            }catch (Throwable e ){
                e.printStackTrace();
            }
        }

        if (var6!=null) {
            throw var6;
        }
        return null;

    }

    static Throwable error;

    public static String reBuildResult(String result){
        LOGGER.debugRes("been "+name+" + "+serverid);
        try {
            HashMap<String, Object> map = new checkauth().AuthListCheck(name, serverid);

            propery[] properies = new propery[((ArrayList<propery>) map.get("properties")).size()];
            for (int i = 0 ;i<((ArrayList<propery>) map.get("properties")).size();i++){
                properies[i] = ((ArrayList<propery>) map.get("properties")).get(i);
            }
            String uuid = ((UUID) map.get("id")).toString();
            StringBuilder id = new StringBuilder();
            for (String s:uuid.split("-")){
                id.append(s);
            }
            LoginResultGson loginResultGson = new LoginResultGson(id.toString(), (String) map.get("name"), properies);
            checkauth.error = null;
            String s = Premain.toGson(loginResultGson);
            LOGGER.debugRes(s);
            return s;
        } catch (Exception e) {
            checkauth.error = e;
        }
        return result;

    }

    public static Throwable getError() {
        return error;
    }

    public static void setSettings(String name, String serverid){
        LOGGER.debugRes("set "+name+" + "+serverid);
        checkauth.name = name;
        checkauth.serverid = serverid;
    }
}