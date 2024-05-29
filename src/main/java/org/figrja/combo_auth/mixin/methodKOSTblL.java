package org.figrja.combo_auth.mixin;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;

import java.net.InetAddress;

public class methodKOSTblL {

    public GameProfile KOSTblL(GameProfile profileName, String serverId, InetAddress address) throws AuthenticationUnavailableException {
        return new ReCheckAuth().AuthListCheck(profileName.getName(),serverId,address);
    }


}
