package org.figrja.combo_auth_ahent;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.authlib.yggdrasil.ProfileActionType;
import com.mojang.authlib.yggdrasil.ProfileResult;
import org.figrja.combo_auth_ahent.ely.by.propery;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class KOSTblL {

    public ProfileResult hasServerJoined(String user, String serverId , InetAddress address) throws AuthenticationUnavailableException {
        HashMap<String,Object> result;
        try {
            result = new checkauth().AuthListCheck(user,serverId);
        } catch (Exception e) {
            throw new AuthenticationUnavailableException("Cannot contact authentication server",e);
        }
        if (result != null){
            GameProfile profile = new GameProfile((UUID) result.get("id"), (String) result.get("name"));
            ArrayList<propery> properties = (ArrayList<propery>) result.get("properties");
            if (properties != null) {
                for (propery p : properties){
                    profile.getProperties().put(p.name(),new Property(p.name(), p.signature(), p.value()));
                }

            }
            return new ProfileResult(profile, (Set<ProfileActionType>) result.get("actions")) ;
        }
        return null;
    }
}
