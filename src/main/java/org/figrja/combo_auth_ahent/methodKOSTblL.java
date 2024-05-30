package org.figrja.combo_auth_ahent;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import org.figrja.combo_auth.mixin.ReCheckAuth;

import java.net.InetAddress;

public class methodKOSTblL {

    public GameProfile hasJoinedServer(GameProfile profileName, String serverId, InetAddress address) throws AuthenticationUnavailableException {
        return new ReCheckAuth().AuthListCheck(profileName.getName(),serverId,address);
    }


}
