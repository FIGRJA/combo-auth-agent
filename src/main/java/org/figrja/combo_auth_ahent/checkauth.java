package org.figrja.combo_auth_ahent;

import org.figrja.combo_auth_ahent.api.base;
import org.figrja.combo_auth_ahent.api.util;
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
    public checkauth(){
        if (CONFIG == null){
            Premain.newClass();
            new Premain();
            LOGGER = Premain.LOGGER;
            CONFIG = Premain.config;
        }
    }
    static LoggerMain LOGGER = Premain.LOGGER;
    static Config CONFIG = Premain.config;
    static String serverid;
    static String profileName;
    private Map<String, Object> arguments = new HashMap<>();
    Exception var6 = null;

    static private HashMap<String, base> playerBase = util.getPlayerBase() ;

    public HashMap<String,Object> AuthListCheck(String profileName, String serverId) throws Exception {
        arguments.put("username", profileName);
        arguments.put("serverId", serverId);
        HashMap<String,Object> result = new HashMap<>();
        if (playerBase.get(profileName) == null){
            playerBase.put(profileName,new base());
        }
        String lastName = "";
        if (playerBase.get(profileName).getLast() != null) {
            lastName = playerBase.get(profileName).getLast();
            LOGGER.debug("try last " + lastName);
            result = makeReqest(lastName);
            if (result != null) {
                return result;
            }
        }
        for (String name : CONFIG.getAuthList()) {
            if (!Objects.equals(lastName, name)) {
                LOGGER.debug("try " + name);
                result = makeReqest(name);
                if (result != null) {
                    playerBase.get(profileName).setLast(name);
                    return result;
                }
            }

        }

        if (var6!=null) {
            throw var6;
        }
        return null;

    }

    static Throwable error;

    private HashMap<String,Object> makeReqest(String name){
        SchemaList authSchema = CONFIG.getAuthSchema().get(name);
        LOGGER.debugRes("in " + authSchema.getUrlCheck());
        URL url = httpHelper.concatenateURL(httpHelper.constantURL(authSchema.getUrlCheck()), httpHelper.buildQuery(arguments));
        HashMap<String,Object> result = new HashMap<>();
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
        return null;
    }

    public static String reBuildResult(String result){
        LOGGER.debugRes("been "+profileName+" + "+serverid);
        try {
            HashMap<String, Object> map = new checkauth().AuthListCheck(profileName, serverid);

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
        checkauth.profileName = name;
        checkauth.serverid = serverid;
    }public static void setSettings(URL url){
        String[] surl = url.getQuery().split("&");
        for (String s:surl){
            if (s.startsWith("username=")){
                checkauth.profileName = s.split("=")[1];
            } else if (s.startsWith("serverId=")) {
                checkauth.serverid = s.split("=")[1];
            }
        }
        

    }
}