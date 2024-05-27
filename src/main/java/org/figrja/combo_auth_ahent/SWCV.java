package org.figrja.combo_auth_ahent;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.ClassVisitor;

public class SWCV extends ClassVisitor {
    public SWCV(int api, ClassVisitor cv) {
        super(api, cv);

    }

    @Override
    public MethodVisitor visitMethod(int access,String name , String desc , String signature,String[] exceptions){
        System.out.println("name:"+name+" desc:"+desc);
        if (name.equals("hasJoinedServer")){
            return null;
        }

        return cv.visitMethod(access, name, desc, signature, exceptions);
    }

}
