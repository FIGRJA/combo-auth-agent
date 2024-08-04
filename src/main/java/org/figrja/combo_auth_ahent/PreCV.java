package org.figrja.combo_auth_ahent;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.ASM9;

public class PreCV extends ClassVisitor {
    public PreCV(int api, ClassVisitor classVisitor) {
        super(api, classVisitor);
    }
    static int VelocityURLIndex;static int VelocitySecretIndex;

    static int BungeeCordURLIndex;

    public MethodVisitor visitMethod(int access, String name , String desc , String signature, String[] exceptions){
        if (name.equals("handle")&&desc.equals("(Lcom/velocitypowered/proxy/protocol/packet/EncryptionResponsePacket;)Z")) {
            MethodVisitor method = cv.visitMethod(access, name, desc, signature, exceptions);
            return new PreCV.velocityFindURL(method,api);
        }else if (name.equals("handle")&&desc.equals("(Lnet/md_5/bungee/protocol/packet/EncryptionResponse;)V")) {
            MethodVisitor method = cv.visitMethod(access, name, desc, signature, exceptions);
            return new PreCV.velocityFindURL(method,api);

        }
        return null;
    }

    private static class velocityFindURL extends MethodVisitor{
        public velocityFindURL( MethodVisitor methodVisitor,int api) {
            super(api, methodVisitor);

        }

        public void visitLocalVariable(
                final String name,
                final String descriptor,
                final String signature,
                final Label start,
                final Label end,
                final int index) {
            if (name.equals("url")){
                VelocityURLIndex = index;
            } else if (name.equals("decryptedSharedSecret")) {

                VelocitySecretIndex = index;
            } else if (name.equals("authURL")) {
                BungeeCordURLIndex = index;
            }
            mv.visitLocalVariable(name, descriptor, signature, start, end, index);
        }


    }
}
