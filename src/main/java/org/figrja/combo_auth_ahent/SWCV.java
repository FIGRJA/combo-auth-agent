package org.figrja.combo_auth_ahent;

import org.figrja.combo_auth.auth;
import org.figrja.combo_auth.config.debuglogger.LoggerMain;
import org.objectweb.asm.*;
import static org.objectweb.asm.Opcodes.*;


public class SWCV extends ClassVisitor {

    public SWCV(int api, ClassVisitor cv) {
        super(api, cv);
    }


    LoggerMain LOGGER = auth.Logger;



    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        LOGGER.info("start find method");
        cv.visit(version, access, name, signature, superName, interfaces);
    }



    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        LOGGER.debugRes("    "+descriptor+" "+name);
        return cv.visitField(access, name, descriptor, signature, value);
    }

    @Override
    public MethodVisitor visitMethod(int access,String name , String desc , String signature,String[] exceptions) {
        LOGGER.debug("    " + name + desc);
        if (name.equals("hasJoinedServer")) {
            LOGGER.info("found method");
            int version;
            if (desc.equals("(Lcom/mojang/authlib/GameProfile;Ljava/lang/String;Ljava/net/InetAddress;)Lcom/mojang/authlib/GameProfile;")) {
                LOGGER.info("version with GameProfile");
                version = 0;
            } else if (desc.equals("(Ljava/lang/String;Ljava/lang/String;Ljava/net/InetAddress;)Lcom/mojang/authlib/yggdrasil/ProfileResult;")) {
                LOGGER.info("version with ProfileResult");
                version = 1;
            } else {
                LOGGER.info("unknown version");
                return null;
            }
            LOGGER.debug("insert our method");
            return new EXT(cv.visitMethod(access, name, desc, signature, exceptions),version);
        }
        return cv.visitMethod(access, name, desc, signature, exceptions);

    }

    public static class EXT extends MethodVisitor{
        int version;
        MethodVisitor mv;
        public EXT(MethodVisitor mv,int version){
            super(ASM9,null);
            this.version = version;
            this.mv = mv;

        }

        @Override
        public void visitCode(){
            mv.visitCode();
            Label l1 = new Label();
            mv.visitLabel(l1);
            if (version == 1) {
                mv.visitTypeInsn(NEW, "com/mojang/authlib/yggdrasil/ProfileResult");
                mv.visitInsn(DUP);
            }
            mv.visitTypeInsn(NEW,"org/figrja/combo_auth_ahent/checkauth");
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, "org/figrja/combo_auth/checkauth", "<init>", "()V");
            mv.visitVarInsn(ALOAD,1);
            if (version == 0) {
                mv.visitMethodInsn(INVOKEVIRTUAL, "com/mojang/authlib/GameProfile", "getName", "()Ljava/lang/String;");
            }
            mv.visitVarInsn(ALOAD,2);
            mv.visitMethodInsn(INVOKEVIRTUAL, "org/figrja/combo_auth/checkauth", "AuthListCheck", "(Ljava/lang/String;Ljava/lang/String;)Lcom/mojang/authlib/GameProfile;");
            if (version == 1){
                mv.visitMethodInsn(INVOKEVIRTUAL, "com/mojang/authlib/yggdrasil/ProfileResult", "<init>", "(Lcom/mojang/authlib/GameProfile;)V");
            }
            mv.visitInsn(ARETURN);
            mv.visitMaxs(1,1);
            mv.visitEnd();
        }
    }



    @Override
    public void visitEnd() {
        LOGGER.debug("end visit");
        cv.visitEnd();
    }
}
