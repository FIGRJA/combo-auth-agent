package org.figrja.combo_auth.mixin;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.yggdrasil.ProfileResult;

import java.net.InetAddress;

public class methodKOSTblLnew {

    public ProfileResult KOSTblL(String profileName, String serverId, InetAddress address) throws AuthenticationUnavailableException {
        return new ProfileResult( new ReCheckAuth().AuthListCheck(profileName,serverId,address));
    }
}
