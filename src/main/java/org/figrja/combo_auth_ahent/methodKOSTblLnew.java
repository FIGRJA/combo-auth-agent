package org.figrja.combo_auth_ahent;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.yggdrasil.ProfileResult;
import org.figrja.combo_auth.mixin.ReCheckAuth;

import java.net.InetAddress;

public class methodKOSTblLnew {

    public ProfileResult hasJoinedServer(String profileName, String serverId, InetAddress address) throws AuthenticationUnavailableException {
        return new ProfileResult( new ReCheckAuth().AuthListCheck(profileName,serverId,address));
    }
}
