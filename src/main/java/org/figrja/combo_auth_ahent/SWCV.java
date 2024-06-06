package org.figrja.combo_auth_ahent;

import org.figrja.combo_auth_ahent.config.debuglogger.LoggerMain;
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
        } else if (name.equals("addURL")) {
            LOGGER.info("new");
            return cv.visitMethod(ACC_PUBLIC, name, desc, signature, exceptions);

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
            ///version = 0
            ///version = 1
            Label l1 = new Label();
            Label l2 = new Label();
            Label l3 = new Label();
            Label l4 = new Label();
            Label l5 = new Label();
            Label l6 = new Label();
            Label l7 = new Label();
            Label l8 = new Label();
            Label l9 = new Label();
            Label l10 = new Label();
            Label l11 = new Label();

            ///result = new checkauth().AuthListCheck(user.getName(),serverId);
            ///result = new checkauth().AuthListCheck(user,serverId);
            mv.visitLabel(l1);
            mv.visitTypeInsn(NEW,"org/figrja/combo_auth_ahent/checkauth");
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, "org/figrja/combo_auth_ahent/checkauth", "<init>", "()V",false);
            mv.visitVarInsn(ALOAD,1);
            if (version == 0) {
                mv.visitMethodInsn(INVOKEVIRTUAL, "com/mojang/authlib/GameProfile", "getName", "()Ljava/lang/String;",false);}
            mv.visitVarInsn(ALOAD,2);
            mv.visitMethodInsn(INVOKEVIRTUAL, "org/figrja/combo_auth_ahent/checkauth", "AuthListCheck", "(Ljava/lang/String;Ljava/lang/String;)Ljava/util/HashMap;",false);
            mv.visitVarInsn(ASTORE,4);
            //exit try
            mv.visitLabel(l2);
            mv.visitJumpInsn(GOTO,l3);

            //catch (Exception e)
            mv.visitLabel(l4);
            mv.visitFrame(F_SAME1,0,null,1,new Object[]{"java/lang/Exception"});
            mv.visitVarInsn(ASTORE,5);

            //throw new AuthenticationUnavailableException("Cannot contact authentication server",e);
            mv.visitLabel(l5);
            mv.visitTypeInsn(NEW,"com/mojang/authlib/exceptions/AuthenticationUnavailableException");
            mv.visitInsn(DUP);
            mv.visitLdcInsn("Cannot contact authentication server");
            mv.visitVarInsn(ALOAD,5);
            mv.visitMethodInsn(INVOKESPECIAL, "com/mojang/authlib/exceptions/AuthenticationUnavailableException","<init>" ,"(Ljava/lang/String;Ljava/lang/Throwable;)V",false);
            mv.visitInsn(ATHROW);

            //if (result != null)
            mv.visitLabel(l3);
            mv.visitFrame(F_APPEND,1,new Object[]{"java/util/HashMap"},0,null);
            mv.visitVarInsn(ALOAD,4);
            mv.visitJumpInsn(IFNULL,l6);

            //GameProfile profile = new GameProfile((UUID) result.get("id"), (String) result.get("name"));
            mv.visitLabel(l7);
            mv.visitTypeInsn(NEW , "com/mojang/authlib/GameProfile");
            mv.visitInsn(DUP);
            mv.visitVarInsn(ALOAD,4);
            mv.visitLdcInsn("id");
            mv.visitMethodInsn(INVOKEVIRTUAL,"java/util/HashMap","get","(Ljava/lang/Object;)Ljava/lang/Object;",false);
            mv.visitTypeInsn(CHECKCAST,"java/util/UUID");
            mv.visitVarInsn(ALOAD,4);
            mv.visitLdcInsn("name");
            mv.visitMethodInsn(INVOKEVIRTUAL,"java/util/HashMap","get","(Ljava/lang/Object;)Ljava/lang/Object;",false);
            mv.visitTypeInsn(CHECKCAST,"java/lang/String");
            mv.visitMethodInsn(INVOKESPECIAL,"com/mojang/authlib/GameProfile","<init>","(Ljava/util/UUID;Ljava/lang/String;)V",false);
            mv.visitVarInsn(ASTORE,5);

            //Object properties = result.get("properties");
            mv.visitLabel(l8);
            mv.visitVarInsn(ALOAD,4);
            mv.visitLdcInsn("properties");
            mv.visitMethodInsn(INVOKEVIRTUAL,"java/util/HashMap","get","(Ljava/lang/Object;)Ljava/lang/Object;",false);
            mv.visitVarInsn(ASTORE,6);

            //if (properties != null)
            mv.visitLabel(l9);
            mv.visitVarInsn(ALOAD,4);
            mv.visitJumpInsn(IFNULL,l10);

            //profile.getProperties().putAll((PropertyMap) properties);
            mv.visitLabel(l11);
            mv.visitVarInsn(ALOAD,5);
            mv.visitMethodInsn(INVOKEVIRTUAL,"com/mojang/authlib/GameProfile","getProperties","()Lcom/mojang/authlib/properties/PropertyMap;",false);
            mv.visitVarInsn(ALOAD,6);
            mv.visitTypeInsn(CHECKCAST, "com/mojang/authlib/properties/PropertyMap");
            mv.visitMethodInsn(INVOKEVIRTUAL, "com/mojang/authlib/properties/PropertyMap","putAll", "(Lcom/google/common/collect/Multimap;)Z",false);
            mv.visitInsn(POP);
            //end if

            ///return profile;
            ///return new ProfileResult(profile, (Set<ProfileActionType>) result.get("actions")) ;
            mv.visitLabel(l10);
            mv.visitFrame(F_APPEND,2,new Object[]{"com/mojang/authlib/GameProfile","java/lang/Object"},0,null);
            if (version == 1){

            }
            mv.visitIntInsn(ALOAD,5);
            if (version == 1){

            }
            mv.visitInsn(ARETURN);
            //end if

            //return null;
            mv.visitLabel(l6);
            mv.visitFrame(F_CHOP,2,null,0,null);
            mv.visitInsn(ACONST_NULL);
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
