package org.figrja.combo_auth_ahent;

import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.yggdrasil.ProfileResult;

import java.net.InetAddress;

public class KOSTblL {

    public ProfileResult KOSTUL(String profileName, String serverId, InetAddress address) throws AuthenticationUnavailableException {
        try {
            return new ProfileResult(new checkauth().AuthListCheck(profileName,serverId));
        } catch (Exception var3) {
            throw new AuthenticationUnavailableException("Cannot contact authentication server", var3);
        }

    }
}
