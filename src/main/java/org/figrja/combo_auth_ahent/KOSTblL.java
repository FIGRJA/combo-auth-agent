package org.figrja.combo_auth_ahent;

import com.google.gson.Gson;
import org.figrja.combo_auth_ahent.ely.by.LoginResultGson;
import org.figrja.combo_auth_ahent.ely.by.propery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class KOSTblL {

    static Object gson ;

    public static <T> T fromGson(String json, Class<T> classOfT){
        if (gson==null)gson = new Gson();
        return ((Gson)gson).fromJson(json,classOfT);
    }
}
