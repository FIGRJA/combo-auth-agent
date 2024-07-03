package org.figrja.combo_auth_ahent;

import com.google.gson.Gson;

public class KOSTblL {

    static Object gson ;

    public static <T> T fromGson(String json, Class<T> classOfT){
        if (gson==null)gson = new Gson();
        return ((Gson)gson).fromJson(json,classOfT);
    }public static String toGson(Object json){
        if (gson==null)gson = new Gson();
        return ((Gson)gson).toJson(json);
    }


}
