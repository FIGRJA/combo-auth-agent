package org.figrja.combo_auth_ahent;

import org.figrja.combo_auth.auth;
import org.figrja.combo_auth.config.debuglogger.LoggerMain;
import org.objectweb.asm.*;

public class methodBOP extends ClassVisitor {

    public methodBOP(int api, ClassVisitor cv) {
        super(api, cv);

    }

    LoggerMain LOGGER = auth.Logger;


    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {

        LOGGER.debug("start find our method");

    }


    @Override
    public MethodVisitor visitMethod(int access,String name , String desc , String signature,String[] exceptions){
        LOGGER.debug("    "+name+desc);
        if (name.equals("KOSTblL()")){
            LOGGER.debug("found our method");
            MethodVisitor nnn = cv.visitMethod(access, name, desc, signature, exceptions);
            LOGGER.debug(String.valueOf(nnn==null));
            SWCV.mv = nnn;
            return nnn;
        }

        return null;
    }

}
