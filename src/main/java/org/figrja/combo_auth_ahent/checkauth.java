package org.figrja.combo_auth_ahent;

import com.google.gson.Gson;
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

    static Object gson = Premain.gson;

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

    public static void reBuildResult(String result , Throwable error){
        if (gson==null){
            Premain.gson = new Gson();
            gson = Premain.gson;
        }
        if (error!=null) {
            String[] sp = result.split("=");
            String name = sp[1].split("&")[0];
            String serverId = sp[2].split("&")[0];
            try {
                HashMap<String, Object> map = new checkauth().AuthListCheck(name, serverId);
                LoginResultGson loginResultGson = new LoginResultGson(((UUID) map.get("id")).toString(), (String) map.get("name"), (propery[]) ((ArrayList<propery>) map.get("properties")).toArray());
                error = null;
                result = ((Gson)gson).toJson(loginResultGson);
            } catch (Exception e) {
                error = e;
            }
        }else {
            Premain.LOGGER.info("result is not null -> combo-auth not used");
        }
    }
}