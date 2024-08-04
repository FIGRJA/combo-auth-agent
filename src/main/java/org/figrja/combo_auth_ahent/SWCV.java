package org.figrja.combo_auth_ahent;

import org.figrja.combo_auth_ahent.config.debuglogger.LoggerMain;
import org.objectweb.asm.*;

import static org.objectweb.asm.Opcodes.*;


public class SWCV extends ClassVisitor {

    public SWCV(int api, ClassVisitor cv) {
        super(api, cv);
    }


    static LoggerMain LOGGER = Premain.LOGGER;
    boolean needVelocityLOL0 = false;
    boolean needBungeeCordLOL0 = false;



    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        LOGGER.info("start find method");
        LOGGER.debug(name);
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
            return new EXT(cv.visitMethod(access, name, desc, signature, exceptions),version,api);
        }else if (name.equals("handle")&&desc.equals("(Lnet/md_5/bungee/protocol/packet/EncryptionResponse;)V")){
            needBungeeCordLOL0 = true;
            return new insertURL(cv.visitMethod(access, name, desc, signature, exceptions),"BungeeCord",api);
        }else if (name.equals("handle")&&desc.equals("(Lcom/velocitypowered/proxy/protocol/packet/EncryptionResponsePacket;)Z")){
            needVelocityLOL0 = true;
            return new insertURL(cv.visitMethod(access, name, desc, signature, exceptions),"Velocity",api);
        }
        return cv.visitMethod(access, name, desc, signature, exceptions);

    }

    private static class insertURL extends MethodVisitor{
        public insertURL( MethodVisitor methodVisitor,String name,int api) {
            super(api, methodVisitor);
            this.name = name;
        }
        boolean one = true;
        String name;

        public void visitVarInsn(final int opcode, final int var) {
            mv.visitVarInsn(opcode, var);
            if (opcode == ASTORE&&var== PreCV.VelocityURLIndex &&one&&name.equals("Velocity")) {
                one = false;
                //thx @konloch for bytecode viewer 2.12
                LOGGER.debug("i see it");
                MethodVisitor methodVisitor = mv;
                Label label3 = new Label();
                methodVisitor.visitLabel(label3);
                methodVisitor.visitVarInsn(ALOAD, 0);
                methodVisitor.visitVarInsn(ALOAD, PreCV.VelocitySecretIndex);
                methodVisitor.visitVarInsn(ALOAD, PreCV.VelocityURLIndex);
                methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "com/velocitypowered/proxy/connection/client/InitialLoginSessionHandler", "lol", "([BLjava/lang/String;)V", false);
                Label label34 = new Label();
                methodVisitor.visitLabel(label34);
                methodVisitor.visitInsn(ICONST_1);
                methodVisitor.visitInsn(IRETURN);
            }else if (opcode == ASTORE&&var== PreCV.BungeeCordURLIndex &&one&&name.equals("BungeeCord")) {
                one = false;
                LOGGER.debug("i see it");
                MethodVisitor methodVisitor = mv;
                Label label3 = new Label();
                methodVisitor.visitLabel(label3);
                methodVisitor.visitVarInsn(ALOAD, 0);
                methodVisitor.visitVarInsn(ALOAD, PreCV.BungeeCordURLIndex);
                methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "net/md_5/bungee/connection/InitialHandler", "lol", "(Ljava/lang/String;)V", false);
                Label label34 = new Label();
                methodVisitor.visitLabel(label34);
                methodVisitor.visitInsn(RETURN);
            }
        }
    }

    private static class startWith extends MethodVisitor{
        public startWith( MethodVisitor methodVisitor,int version) {
            super(ASM9, methodVisitor);
            this.version = version;
        }
        int version;
        public void visitCode(){
                mv.visitCode();
                Label insert = new Label();
                mv.visitLabel(insert);
                mv.visitVarInsn(ALOAD, 1);
                mv.visitMethodInsn(INVOKESTATIC, "org/figrja/combo_auth_ahent/checkauth", "reBuildResult", "(Ljava/lang/String;)Ljava/lang/String;", false);
                mv.visitVarInsn(ASTORE, 1);
                mv.visitMethodInsn(INVOKESTATIC, "org/figrja/combo_auth_ahent/checkauth", "getError", "()Ljava/lang/Throwable;", false);
                mv.visitVarInsn(ASTORE, 2);

        }

    }

    private static class endWith extends MethodVisitor{
        public endWith( MethodVisitor methodVisitor) {
            super(ASM9, methodVisitor);
        }

        @Override
        public void visitInsn(final int opcode) {

            if (mv != null) {
                if (opcode == RETURN) {
                    LOGGER.info("insert");
                    mv.visitVarInsn(ALOAD, 5);
                    mv.visitVarInsn(ALOAD, 7);
                    mv.visitMethodInsn(INVOKESTATIC, "org/figrja/combo_auth_ahent/checkauth", "setSettings", "(Ljava/lang/String;Ljava/lang/String;)V", false);

                }
                mv.visitInsn(opcode);
            }
        }

    }


    public static class EXT extends MethodVisitor{
        int version;
        MethodVisitor mv;
        public EXT(MethodVisitor mv,int version,int api){
            super(api,null);
            this.version = version;
            this.mv = mv;

        }

        @Override
        public void visitCode(){
            //thx @konloch for bytecode viewer 2.12
            if (version == 0) {
                MethodVisitor methodVisitor = mv;
                methodVisitor.visitCode();
                Label label0 = new Label();
                Label label1 = new Label();
                Label label2 = new Label();
                methodVisitor.visitTryCatchBlock(label0, label1, label2, "java/lang/Exception");
                Label label3 = new Label();
                Label label4 = new Label();
                methodVisitor.visitTryCatchBlock(label3, label4, label2, "java/lang/Exception");
                methodVisitor.visitLabel(label0);
                methodVisitor.visitLineNumber(38, label0);
                methodVisitor.visitTypeInsn(NEW, "org/figrja/combo_auth_ahent/checkauth");
                methodVisitor.visitInsn(DUP);
                methodVisitor.visitMethodInsn(INVOKESPECIAL, "org/figrja/combo_auth_ahent/checkauth", "<init>", "()V", false);
                methodVisitor.visitVarInsn(ALOAD, 1);
                methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "com/mojang/authlib/GameProfile", "getName", "()Ljava/lang/String;", false);
                methodVisitor.visitVarInsn(ALOAD, 2);
                methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "org/figrja/combo_auth_ahent/checkauth", "AuthListCheck", "(Ljava/lang/String;Ljava/lang/String;)Ljava/util/HashMap;", false);
                methodVisitor.visitVarInsn(ASTORE, 4);
                Label label5 = new Label();
                methodVisitor.visitLabel(label5);
                methodVisitor.visitLineNumber(39, label5);
                methodVisitor.visitVarInsn(ALOAD, 4);
                methodVisitor.visitJumpInsn(IFNONNULL, label3);
                Label label6 = new Label();
                methodVisitor.visitLabel(label6);
                methodVisitor.visitLineNumber(40, label6);
                methodVisitor.visitInsn(ACONST_NULL);
                methodVisitor.visitLabel(label1);
                methodVisitor.visitInsn(ARETURN);
                methodVisitor.visitLabel(label3);
                methodVisitor.visitLineNumber(42, label3);
                methodVisitor.visitFrame(Opcodes.F_NEW, 5, new Object[] {"com/mojang/authlib/yggdrasil/YggdrasilMinecraftSessionService", "com/mojang/authlib/GameProfile", "java/lang/String", "java/net/InetAddress", "java/util/HashMap"}, 0, new Object[] {});
                methodVisitor.visitTypeInsn(NEW, "com/mojang/authlib/GameProfile");
                methodVisitor.visitInsn(DUP);
                methodVisitor.visitVarInsn(ALOAD, 4);
                methodVisitor.visitLdcInsn("id");
                methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/util/HashMap", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", false);
                methodVisitor.visitTypeInsn(CHECKCAST, "java/util/UUID");
                methodVisitor.visitVarInsn(ALOAD, 4);
                methodVisitor.visitLdcInsn("name");
                methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/util/HashMap", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", false);
                methodVisitor.visitTypeInsn(CHECKCAST, "java/lang/String");
                methodVisitor.visitMethodInsn(INVOKESPECIAL, "com/mojang/authlib/GameProfile", "<init>", "(Ljava/util/UUID;Ljava/lang/String;)V", false);
                methodVisitor.visitVarInsn(ASTORE, 5);
                Label label7 = new Label();
                methodVisitor.visitLabel(label7);
                methodVisitor.visitLineNumber(43, label7);
                methodVisitor.visitVarInsn(ALOAD, 4);
                methodVisitor.visitLdcInsn("properties");
                methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/util/HashMap", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", false);
                methodVisitor.visitTypeInsn(CHECKCAST, "java/util/ArrayList");
                methodVisitor.visitVarInsn(ASTORE, 6);
                Label label8 = new Label();
                methodVisitor.visitLabel(label8);
                methodVisitor.visitLineNumber(44, label8);
                methodVisitor.visitVarInsn(ALOAD, 6);
                Label label9 = new Label();
                methodVisitor.visitJumpInsn(IFNULL, label9);
                Label label10 = new Label();
                methodVisitor.visitLabel(label10);
                methodVisitor.visitLineNumber(45, label10);
                methodVisitor.visitVarInsn(ALOAD, 6);
                methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/util/ArrayList", "iterator", "()Ljava/util/Iterator;", false);
                methodVisitor.visitVarInsn(ASTORE, 7);
                Label label11 = new Label();
                methodVisitor.visitLabel(label11);
                methodVisitor.visitLineNumber(47, label11);
                methodVisitor.visitFrame(Opcodes.F_NEW, 8, new Object[] {"com/mojang/authlib/yggdrasil/YggdrasilMinecraftSessionService", "com/mojang/authlib/GameProfile", "java/lang/String", "java/net/InetAddress", "java/util/HashMap", "com/mojang/authlib/GameProfile", "java/util/ArrayList", "java/util/Iterator"}, 0, new Object[] {});
                methodVisitor.visitVarInsn(ALOAD, 7);
                methodVisitor.visitMethodInsn(INVOKEINTERFACE, "java/util/Iterator", "hasNext", "()Z", true);
                methodVisitor.visitJumpInsn(IFEQ, label9);
                Label label12 = new Label();
                methodVisitor.visitLabel(label12);
                methodVisitor.visitLineNumber(48, label12);
                methodVisitor.visitVarInsn(ALOAD, 7);
                methodVisitor.visitMethodInsn(INVOKEINTERFACE, "java/util/Iterator", "next", "()Ljava/lang/Object;", true);
                methodVisitor.visitTypeInsn(CHECKCAST, "[Ljava/lang/String;");
                methodVisitor.visitVarInsn(ASTORE, 8);
                Label label13 = new Label();
                methodVisitor.visitLabel(label13);
                methodVisitor.visitLineNumber(49, label13);
                methodVisitor.visitVarInsn(ALOAD, 5);
                methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "com/mojang/authlib/GameProfile", "getProperties", "()Lcom/mojang/authlib/properties/PropertyMap;", false);
                methodVisitor.visitVarInsn(ALOAD, 8);
                methodVisitor.visitInsn(ICONST_0);
                methodVisitor.visitInsn(AALOAD);
                methodVisitor.visitTypeInsn(NEW, "com/mojang/authlib/properties/Property");
                methodVisitor.visitInsn(DUP);
                methodVisitor.visitVarInsn(ALOAD, 8);
                methodVisitor.visitInsn(ICONST_0);
                methodVisitor.visitInsn(AALOAD);
                methodVisitor.visitVarInsn(ALOAD, 8);
                methodVisitor.visitInsn(ICONST_1);
                methodVisitor.visitInsn(AALOAD);
                methodVisitor.visitVarInsn(ALOAD, 8);
                methodVisitor.visitInsn(ICONST_2);
                methodVisitor.visitInsn(AALOAD);
                methodVisitor.visitMethodInsn(INVOKESPECIAL, "com/mojang/authlib/properties/Property", "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", false);
                methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "com/mojang/authlib/properties/PropertyMap", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Z", false);
                methodVisitor.visitInsn(POP);
                Label label14 = new Label();
                methodVisitor.visitLabel(label14);
                methodVisitor.visitLineNumber(50, label14);
                methodVisitor.visitJumpInsn(GOTO, label11);
                methodVisitor.visitLabel(label9);
                methodVisitor.visitLineNumber(53, label9);
                methodVisitor.visitFrame(Opcodes.F_NEW, 7, new Object[] {"com/mojang/authlib/yggdrasil/YggdrasilMinecraftSessionService", "com/mojang/authlib/GameProfile", "java/lang/String", "java/net/InetAddress", "java/util/HashMap", "com/mojang/authlib/GameProfile", "java/util/ArrayList"}, 0, new Object[] {});
                methodVisitor.visitVarInsn(ALOAD, 5);
                methodVisitor.visitLabel(label4);
                methodVisitor.visitInsn(ARETURN);
                methodVisitor.visitLabel(label2);
                methodVisitor.visitLineNumber(55, label2);
                methodVisitor.visitFrame(Opcodes.F_NEW, 4, new Object[] {"com/mojang/authlib/yggdrasil/YggdrasilMinecraftSessionService", "com/mojang/authlib/GameProfile", "java/lang/String", "java/net/InetAddress"}, 1, new Object[] {"java/lang/Exception"});
                methodVisitor.visitVarInsn(ASTORE, 4);
                Label label15 = new Label();
                methodVisitor.visitLabel(label15);
                methodVisitor.visitLineNumber(56, label15);
                methodVisitor.visitTypeInsn(NEW, "com/mojang/authlib/exceptions/AuthenticationUnavailableException");
                methodVisitor.visitInsn(DUP);
                methodVisitor.visitLdcInsn("Cannot contact authentication server");
                methodVisitor.visitVarInsn(ALOAD, 4);
                methodVisitor.visitMethodInsn(INVOKESPECIAL, "com/mojang/authlib/exceptions/AuthenticationUnavailableException", "<init>", "(Ljava/lang/String;Ljava/lang/Throwable;)V", false);
                methodVisitor.visitInsn(ATHROW);
                Label label16 = new Label();
                methodVisitor.visitLabel(label16);
                methodVisitor.visitLocalVariable("var8", "[Ljava/lang/String;", null, label13, label14, 8);
                methodVisitor.visitLocalVariable("var7", "Ljava/util/Iterator;", null, label11, label9, 7);
                methodVisitor.visitLocalVariable("var5", "Lcom/mojang/authlib/GameProfile;", null, label7, label2, 5);
                methodVisitor.visitLocalVariable("var6", "Ljava/util/ArrayList;", null, label8, label2, 6);
                methodVisitor.visitLocalVariable("var4", "Ljava/util/HashMap;", null, label5, label2, 4);
                methodVisitor.visitLocalVariable("e", "Ljava/lang/Exception;", null, label15, label16, 4);
                methodVisitor.visitLocalVariable("this", "Lcom/mojang/authlib/yggdrasil/YggdrasilMinecraftSessionService;", null, label0, label16, 0);
                methodVisitor.visitLocalVariable("v1", "Lcom/mojang/authlib/GameProfile;", null, label0, label16, 1);
                methodVisitor.visitLocalVariable("v2", "Ljava/lang/String;", null, label0, label16, 2);
                methodVisitor.visitLocalVariable("v3", "Ljava/net/InetAddress;", null, label0, label16, 3);
                methodVisitor.visitMaxs(8, 9);
                methodVisitor.visitEnd();
            }
            else if (version == 1 ){
                MethodVisitor methodVisitor = mv;
                methodVisitor.visitCode();
                Label label0 = new Label();
                Label label1 = new Label();
                Label label2 = new Label();
                methodVisitor.visitTryCatchBlock(label0, label1, label2, "java/lang/Exception");
                Label label3 = new Label();
                Label label4 = new Label();
                methodVisitor.visitTryCatchBlock(label3, label4, label2, "java/lang/Exception");
                methodVisitor.visitLabel(label0);
                methodVisitor.visitTypeInsn(NEW, "org/figrja/combo_auth_ahent/checkauth");
                methodVisitor.visitInsn(DUP);
                methodVisitor.visitMethodInsn(INVOKESPECIAL, "org/figrja/combo_auth_ahent/checkauth", "<init>", "()V", false);
                methodVisitor.visitVarInsn(ALOAD, 1);
                methodVisitor.visitVarInsn(ALOAD, 2);
                methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "org/figrja/combo_auth_ahent/checkauth", "AuthListCheck", "(Ljava/lang/String;Ljava/lang/String;)Ljava/util/HashMap;", false);
                methodVisitor.visitVarInsn(ASTORE, 4);
                Label label5 = new Label();
                methodVisitor.visitLabel(label5);
                methodVisitor.visitVarInsn(ALOAD, 4);
                methodVisitor.visitJumpInsn(IFNONNULL, label3);
                Label label6 = new Label();
                methodVisitor.visitLabel(label6);
                methodVisitor.visitInsn(ACONST_NULL);
                methodVisitor.visitLabel(label1);
                methodVisitor.visitInsn(ARETURN);
                methodVisitor.visitLabel(label3);
                methodVisitor.visitFrame(Opcodes.F_NEW, 5, new Object[] {"com/mojang/authlib/yggdrasil/YggdrasilMinecraftSessionService", "java/lang/String", "java/lang/String", "java/net/InetAddress", "java/util/HashMap"}, 0, new Object[] {});
                methodVisitor.visitTypeInsn(NEW, "com/mojang/authlib/GameProfile");
                methodVisitor.visitInsn(DUP);
                methodVisitor.visitVarInsn(ALOAD, 4);
                methodVisitor.visitLdcInsn("id");
                methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/util/HashMap", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", false);
                methodVisitor.visitTypeInsn(CHECKCAST, "java/util/UUID");
                methodVisitor.visitVarInsn(ALOAD, 4);
                methodVisitor.visitLdcInsn("name");
                methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/util/HashMap", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", false);
                methodVisitor.visitTypeInsn(CHECKCAST, "java/lang/String");
                methodVisitor.visitMethodInsn(INVOKESPECIAL, "com/mojang/authlib/GameProfile", "<init>", "(Ljava/util/UUID;Ljava/lang/String;)V", false);
                methodVisitor.visitVarInsn(ASTORE, 5);
                Label label7 = new Label();
                methodVisitor.visitLabel(label7);
                methodVisitor.visitVarInsn(ALOAD, 4);
                methodVisitor.visitLdcInsn("properties");
                methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/util/HashMap", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", false);
                methodVisitor.visitTypeInsn(CHECKCAST, "java/util/ArrayList");
                methodVisitor.visitVarInsn(ASTORE, 6);
                Label label8 = new Label();
                methodVisitor.visitLabel(label8);
                methodVisitor.visitVarInsn(ALOAD, 6);
                Label label9 = new Label();
                methodVisitor.visitJumpInsn(IFNULL, label9);
                Label label10 = new Label();
                methodVisitor.visitLabel(label10);
                methodVisitor.visitVarInsn(ALOAD, 6);
                methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/util/ArrayList", "iterator", "()Ljava/util/Iterator;", false);
                methodVisitor.visitVarInsn(ASTORE, 7);
                Label label11 = new Label();
                methodVisitor.visitLabel(label11);
                methodVisitor.visitFrame(Opcodes.F_NEW, 8, new Object[] {"com/mojang/authlib/yggdrasil/YggdrasilMinecraftSessionService", "java/lang/String", "java/lang/String", "java/net/InetAddress", "java/util/HashMap", "com/mojang/authlib/GameProfile", "java/util/ArrayList", "java/util/Iterator"}, 0, new Object[] {});
                methodVisitor.visitVarInsn(ALOAD, 7);
                methodVisitor.visitMethodInsn(INVOKEINTERFACE, "java/util/Iterator", "hasNext", "()Z", true);
                methodVisitor.visitJumpInsn(IFEQ, label9);
                Label label12 = new Label();
                methodVisitor.visitLabel(label12);
                methodVisitor.visitVarInsn(ALOAD, 7);
                methodVisitor.visitMethodInsn(INVOKEINTERFACE, "java/util/Iterator", "next", "()Ljava/lang/Object;", true);
                methodVisitor.visitTypeInsn(CHECKCAST, "[Ljava/lang/String;");
                methodVisitor.visitVarInsn(ASTORE, 8);
                Label label13 = new Label();
                methodVisitor.visitLabel(label13);
                methodVisitor.visitVarInsn(ALOAD, 5);
                methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "com/mojang/authlib/GameProfile", "getProperties", "()Lcom/mojang/authlib/properties/PropertyMap;", false);
                methodVisitor.visitVarInsn(ALOAD, 8);
                methodVisitor.visitInsn(ICONST_0);
                methodVisitor.visitInsn(AALOAD);
                methodVisitor.visitTypeInsn(NEW, "com/mojang/authlib/properties/Property");
                methodVisitor.visitInsn(DUP);
                methodVisitor.visitVarInsn(ALOAD, 8);
                methodVisitor.visitInsn(ICONST_0);
                methodVisitor.visitInsn(AALOAD);
                methodVisitor.visitVarInsn(ALOAD, 8);
                methodVisitor.visitInsn(ICONST_1);
                methodVisitor.visitInsn(AALOAD);
                methodVisitor.visitVarInsn(ALOAD, 8);
                methodVisitor.visitInsn(ICONST_2);
                methodVisitor.visitInsn(AALOAD);
                methodVisitor.visitMethodInsn(INVOKESPECIAL, "com/mojang/authlib/properties/Property", "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", false);
                methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "com/mojang/authlib/properties/PropertyMap", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Z", false);
                methodVisitor.visitInsn(POP);
                Label label14 = new Label();
                methodVisitor.visitLabel(label14);
                methodVisitor.visitJumpInsn(GOTO, label11);
                methodVisitor.visitLabel(label9);
                methodVisitor.visitFrame(Opcodes.F_NEW, 7, new Object[] {"com/mojang/authlib/yggdrasil/YggdrasilMinecraftSessionService", "java/lang/String", "java/lang/String", "java/net/InetAddress", "java/util/HashMap", "com/mojang/authlib/GameProfile", "java/util/ArrayList"}, 0, new Object[] {});
                methodVisitor.visitTypeInsn(NEW, "com/mojang/authlib/yggdrasil/ProfileResult");
                methodVisitor.visitInsn(DUP);
                methodVisitor.visitVarInsn(ALOAD, 5);
                methodVisitor.visitVarInsn(ALOAD, 4);
                methodVisitor.visitLdcInsn("actions");
                methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/util/HashMap", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", false);
                methodVisitor.visitTypeInsn(CHECKCAST, "java/util/Set");
                methodVisitor.visitMethodInsn(INVOKESPECIAL, "com/mojang/authlib/yggdrasil/ProfileResult", "<init>", "(Lcom/mojang/authlib/GameProfile;Ljava/util/Set;)V", false);
                methodVisitor.visitLabel(label4);
                methodVisitor.visitInsn(ARETURN);
                methodVisitor.visitLabel(label2);
                methodVisitor.visitFrame(Opcodes.F_NEW, 4, new Object[] {"com/mojang/authlib/yggdrasil/YggdrasilMinecraftSessionService", "java/lang/String", "java/lang/String", "java/net/InetAddress"}, 1, new Object[] {"java/lang/Exception"});
                methodVisitor.visitVarInsn(ASTORE, 4);
                Label label15 = new Label();
                methodVisitor.visitLabel(label15);
                methodVisitor.visitTypeInsn(NEW, "com/mojang/authlib/exceptions/AuthenticationUnavailableException");
                methodVisitor.visitInsn(DUP);
                methodVisitor.visitLdcInsn("Cannot contact authentication server");
                methodVisitor.visitVarInsn(ALOAD, 4);
                methodVisitor.visitMethodInsn(INVOKESPECIAL, "com/mojang/authlib/exceptions/AuthenticationUnavailableException", "<init>", "(Ljava/lang/String;Ljava/lang/Throwable;)V", false);
                methodVisitor.visitInsn(ATHROW);
                Label label16 = new Label();
                methodVisitor.visitLabel(label16);
                methodVisitor.visitLocalVariable("e", "Ljava/lang/Exception;", null, label15, label16, 4);
                methodVisitor.visitLocalVariable("this", "Lcom/mojang/authlib/yggdrasil/YggdrasilMinecraftSessionService;", null, label0, label16, 0);
                methodVisitor.visitMaxs(8, 9);
                methodVisitor.visitEnd();
            }

        }
    }



    @Override
    public void visitEnd() {
        if (needVelocityLOL0) {
            addVelocitylol();
            //addLambdalol();
        }else if (needBungeeCordLOL0){
            addBungeeCordlol();
        }
        LOGGER.debug("end visit");
        cv.visitEnd();
    }

    private void addVelocitylol(){
        //thx @konloch for bytecode viewer 2.12

        {
            MethodVisitor methodVisitor = cv.visitMethod(ACC_PRIVATE, "lol", "([BLjava/lang/String;)V", null, null);
            methodVisitor.visitCode();
            Label label0 = new Label();
            methodVisitor.visitLabel(label0);
            methodVisitor.visitVarInsn(ALOAD, 2);
            methodVisitor.visitMethodInsn(INVOKESTATIC, "org/figrja/combo_auth_ahent/checkauth", "setSettings", "(Ljava/lang/String;)V", false);
            Label label1 = new Label();
            methodVisitor.visitLabel(label1);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitInvokeDynamicInsn("run", "(Lcom/velocitypowered/proxy/connection/client/InitialLoginSessionHandler;[B)Ljava/lang/Runnable;", new Handle(Opcodes.H_INVOKESTATIC, "java/lang/invoke/LambdaMetafactory", "metafactory", "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;", false), new Object[]{Type.getType("()V"), new Handle(Opcodes.H_INVOKEVIRTUAL, "com/velocitypowered/proxy/connection/client/InitialLoginSessionHandler", "lambda$lol$0", "([B)V", false), Type.getType("()V")});
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitFieldInsn(GETFIELD, "com/velocitypowered/proxy/connection/client/InitialLoginSessionHandler", "mcConnection", "Lcom/velocitypowered/proxy/connection/MinecraftConnection;");
            Label label2 = new Label();
            methodVisitor.visitLabel(label2);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "com/velocitypowered/proxy/connection/MinecraftConnection", "eventLoop", "()Lio/netty/channel/EventLoop;", false);
            Label label3 = new Label();
            methodVisitor.visitLabel(label3);
            methodVisitor.visitMethodInsn(INVOKESTATIC, "java/util/concurrent/CompletableFuture", "runAsync", "(Ljava/lang/Runnable;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;", false);
            methodVisitor.visitInsn(POP);
            Label label4 = new Label();
            methodVisitor.visitLabel(label4);
            methodVisitor.visitInsn(RETURN);
            Label label5 = new Label();
            methodVisitor.visitLabel(label5);
            methodVisitor.visitLocalVariable("this", "Lcom/velocitypowered/proxy/connection/client/InitialLoginSessionHandler;", null, label0, label5, 0);
            methodVisitor.visitLocalVariable("decryptedSharedSecret", "[B", null, label0, label5, 1);
            methodVisitor.visitLocalVariable("url", "Ljava/lang/String;", null, label0, label5, 2);
            methodVisitor.visitMaxs(2, 3);
            methodVisitor.visitEnd();
        }
        {
            MethodVisitor methodVisitor = cv.visitMethod(ACC_PRIVATE | ACC_SYNTHETIC, "lambda$lol$0", "([B)V", null, null);
            methodVisitor.visitCode();
            Label label0 = new Label();
            Label label1 = new Label();
            Label label2 = new Label();
            methodVisitor.visitTryCatchBlock(label0, label1, label2, "java/security/GeneralSecurityException");
            Label label3 = new Label();
            Label label4 = new Label();
            Label label5 = new Label();
            methodVisitor.visitTryCatchBlock(label3, label4, label5, "java/lang/Throwable");
            Label label6 = new Label();
            Label label7 = new Label();
            methodVisitor.visitTryCatchBlock(label6, label7, label5, "java/lang/Throwable");
            methodVisitor.visitLabel(label3);
            methodVisitor.visitInsn(ACONST_NULL);
            methodVisitor.visitMethodInsn(INVOKESTATIC, "org/figrja/combo_auth_ahent/checkauth", "reBuildResult", "(Ljava/lang/String;)Ljava/lang/String;", false);
            methodVisitor.visitVarInsn(ASTORE, 2);
            Label label8 = new Label();
            methodVisitor.visitLabel(label8);
            methodVisitor.visitVarInsn(ALOAD, 2);
            Label label9 = new Label();
            methodVisitor.visitJumpInsn(IFNULL, label9);
            methodVisitor.visitLabel(label0);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitFieldInsn(GETFIELD, "com/velocitypowered/proxy/connection/client/InitialLoginSessionHandler", "mcConnection", "Lcom/velocitypowered/proxy/connection/MinecraftConnection;");
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "com/velocitypowered/proxy/connection/MinecraftConnection", "enableEncryption", "([B)V", false);
            methodVisitor.visitLabel(label1);
            methodVisitor.visitJumpInsn(GOTO, label6);
            methodVisitor.visitLabel(label2);
            methodVisitor.visitFrame(Opcodes.F_NEW, 3, new Object[] {"com/velocitypowered/proxy/connection/client/InitialLoginSessionHandler", "[B", "java/lang/String"}, 1, new Object[] {"java/security/GeneralSecurityException"});
            methodVisitor.visitVarInsn(ASTORE, 3);
            Label label10 = new Label();
            methodVisitor.visitLabel(label10);
            methodVisitor.visitFieldInsn(GETSTATIC, "com/velocitypowered/proxy/connection/client/InitialLoginSessionHandler", "logger", "Lorg/apache/logging/log4j/Logger;");
            methodVisitor.visitLdcInsn("Unable to enable encryption for connection");
            methodVisitor.visitVarInsn(ALOAD, 3);
            methodVisitor.visitMethodInsn(INVOKEINTERFACE, "org/apache/logging/log4j/Logger", "error", "(Ljava/lang/String;Ljava/lang/Throwable;)V", true);
            Label label11 = new Label();
            methodVisitor.visitLabel(label11);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitFieldInsn(GETFIELD, "com/velocitypowered/proxy/connection/client/InitialLoginSessionHandler", "mcConnection", "Lcom/velocitypowered/proxy/connection/MinecraftConnection;");
            methodVisitor.visitInsn(ICONST_1);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "com/velocitypowered/proxy/connection/MinecraftConnection", "close", "(Z)V", false);
            methodVisitor.visitLabel(label4);
            methodVisitor.visitInsn(RETURN);
            methodVisitor.visitLabel(label6);
            methodVisitor.visitFrame(Opcodes.F_NEW, 3, new Object[] {"com/velocitypowered/proxy/connection/client/InitialLoginSessionHandler", "[B", "java/lang/String"}, 0, new Object[] {});
            methodVisitor.visitFieldInsn(GETSTATIC, "com/velocitypowered/proxy/VelocityServer", "GENERAL_GSON", "Lcom/google/gson/Gson;");
            methodVisitor.visitVarInsn(ALOAD, 2);
            methodVisitor.visitLdcInsn(Type.getType("Lcom/velocitypowered/api/util/GameProfile;"));
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "com/google/gson/Gson", "fromJson", "(Ljava/lang/String;Ljava/lang/Class;)Ljava/lang/Object;", false);
            methodVisitor.visitTypeInsn(CHECKCAST, "com/velocitypowered/api/util/GameProfile");
            methodVisitor.visitVarInsn(ASTORE, 3);
            Label label12 = new Label();
            methodVisitor.visitLabel(label12);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitFieldInsn(GETFIELD, "com/velocitypowered/proxy/connection/client/InitialLoginSessionHandler", "inbound", "Lcom/velocitypowered/proxy/connection/client/LoginInboundConnection;");
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "com/velocitypowered/proxy/connection/client/LoginInboundConnection", "getIdentifiedKey", "()Lcom/velocitypowered/api/proxy/crypto/IdentifiedKey;", false);
            Label label13 = new Label();
            methodVisitor.visitJumpInsn(IFNULL, label13);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitFieldInsn(GETFIELD, "com/velocitypowered/proxy/connection/client/InitialLoginSessionHandler", "inbound", "Lcom/velocitypowered/proxy/connection/client/LoginInboundConnection;");
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "com/velocitypowered/proxy/connection/client/LoginInboundConnection", "getIdentifiedKey", "()Lcom/velocitypowered/api/proxy/crypto/IdentifiedKey;", false);
            methodVisitor.visitMethodInsn(INVOKEINTERFACE, "com/velocitypowered/api/proxy/crypto/IdentifiedKey", "getKeyRevision", "()Lcom/velocitypowered/api/proxy/crypto/IdentifiedKey$Revision;", true);
            methodVisitor.visitFieldInsn(GETSTATIC, "com/velocitypowered/api/proxy/crypto/IdentifiedKey$Revision", "LINKED_V2", "Lcom/velocitypowered/api/proxy/crypto/IdentifiedKey$Revision;");
            methodVisitor.visitJumpInsn(IF_ACMPNE, label13);
            Label label14 = new Label();
            methodVisitor.visitLabel(label14);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitFieldInsn(GETFIELD, "com/velocitypowered/proxy/connection/client/InitialLoginSessionHandler", "inbound", "Lcom/velocitypowered/proxy/connection/client/LoginInboundConnection;");
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "com/velocitypowered/proxy/connection/client/LoginInboundConnection", "getIdentifiedKey", "()Lcom/velocitypowered/api/proxy/crypto/IdentifiedKey;", false);
            methodVisitor.visitVarInsn(ASTORE, 4);
            Label label15 = new Label();
            methodVisitor.visitLabel(label15);
            methodVisitor.visitVarInsn(ALOAD, 4);
            methodVisitor.visitTypeInsn(INSTANCEOF, "com/velocitypowered/proxy/crypto/IdentifiedKeyImpl");
            methodVisitor.visitJumpInsn(IFEQ, label13);
            Label label16 = new Label();
            methodVisitor.visitLabel(label16);
            methodVisitor.visitVarInsn(ALOAD, 4);
            methodVisitor.visitTypeInsn(CHECKCAST, "com/velocitypowered/proxy/crypto/IdentifiedKeyImpl");
            methodVisitor.visitVarInsn(ASTORE, 5);
            Label label17 = new Label();
            methodVisitor.visitLabel(label17);
            methodVisitor.visitVarInsn(ALOAD, 5);
            methodVisitor.visitVarInsn(ALOAD, 3);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "com/velocitypowered/api/util/GameProfile", "getId", "()Ljava/util/UUID;", false);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "com/velocitypowered/proxy/crypto/IdentifiedKeyImpl", "internalAddHolder", "(Ljava/util/UUID;)Z", false);
            methodVisitor.visitJumpInsn(IFNE, label13);
            Label label18 = new Label();
            methodVisitor.visitLabel(label18);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitFieldInsn(GETFIELD, "com/velocitypowered/proxy/connection/client/InitialLoginSessionHandler", "inbound", "Lcom/velocitypowered/proxy/connection/client/LoginInboundConnection;");
            methodVisitor.visitLdcInsn("multiplayer.disconnect.invalid_public_key");
            methodVisitor.visitMethodInsn(INVOKESTATIC, "net/kyori/adventure/text/Component", "translatable", "(Ljava/lang/String;)Lnet/kyori/adventure/text/TranslatableComponent;", true);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "com/velocitypowered/proxy/connection/client/LoginInboundConnection", "disconnect", "(Lnet/kyori/adventure/text/Component;)V", false);
            methodVisitor.visitLabel(label13);
            methodVisitor.visitFrame(Opcodes.F_NEW, 4, new Object[] {"com/velocitypowered/proxy/connection/client/InitialLoginSessionHandler", "[B", "java/lang/String", "com/velocitypowered/api/util/GameProfile"}, 0, new Object[] {});
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitFieldInsn(GETFIELD, "com/velocitypowered/proxy/connection/client/InitialLoginSessionHandler", "mcConnection", "Lcom/velocitypowered/proxy/connection/MinecraftConnection;");
            methodVisitor.visitFieldInsn(GETSTATIC, "com/velocitypowered/proxy/protocol/StateRegistry", "LOGIN", "Lcom/velocitypowered/proxy/protocol/StateRegistry;");
            methodVisitor.visitTypeInsn(NEW, "com/velocitypowered/proxy/connection/client/AuthSessionHandler");
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitFieldInsn(GETFIELD, "com/velocitypowered/proxy/connection/client/InitialLoginSessionHandler", "server", "Lcom/velocitypowered/proxy/VelocityServer;");
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitFieldInsn(GETFIELD, "com/velocitypowered/proxy/connection/client/InitialLoginSessionHandler", "inbound", "Lcom/velocitypowered/proxy/connection/client/LoginInboundConnection;");
            methodVisitor.visitVarInsn(ALOAD, 3);
            methodVisitor.visitInsn(ICONST_1);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "com/velocitypowered/proxy/connection/client/AuthSessionHandler", "<init>", "(Lcom/velocitypowered/proxy/VelocityServer;Lcom/velocitypowered/proxy/connection/client/LoginInboundConnection;Lcom/velocitypowered/api/util/GameProfile;Z)V", false);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "com/velocitypowered/proxy/connection/MinecraftConnection", "setActiveSessionHandler", "(Lcom/velocitypowered/proxy/protocol/StateRegistry;Lcom/velocitypowered/proxy/connection/MinecraftSessionHandler;)V", false);
            Label label19 = new Label();
            methodVisitor.visitLabel(label19);
            methodVisitor.visitJumpInsn(GOTO, label7);
            methodVisitor.visitLabel(label9);
            methodVisitor.visitFrame(Opcodes.F_NEW, 3, new Object[] {"com/velocitypowered/proxy/connection/client/InitialLoginSessionHandler", "[B", "java/lang/String"}, 0, new Object[] {});
            methodVisitor.visitMethodInsn(INVOKESTATIC, "org/figrja/combo_auth_ahent/checkauth", "getError", "()Ljava/lang/Throwable;", false);
            Label label20 = new Label();
            methodVisitor.visitJumpInsn(IFNULL, label20);
            Label label21 = new Label();
            methodVisitor.visitLabel(label21);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitFieldInsn(GETFIELD, "com/velocitypowered/proxy/connection/client/InitialLoginSessionHandler", "inbound", "Lcom/velocitypowered/proxy/connection/client/LoginInboundConnection;");
            methodVisitor.visitLdcInsn("multiplayer.disconnect.authservers_down");
            methodVisitor.visitMethodInsn(INVOKESTATIC, "net/kyori/adventure/text/Component", "translatable", "(Ljava/lang/String;)Lnet/kyori/adventure/text/TranslatableComponent;", true);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "com/velocitypowered/proxy/connection/client/LoginInboundConnection", "disconnect", "(Lnet/kyori/adventure/text/Component;)V", false);
            methodVisitor.visitJumpInsn(GOTO, label7);
            methodVisitor.visitLabel(label20);
            methodVisitor.visitFrame(Opcodes.F_NEW, 3, new Object[] {"com/velocitypowered/proxy/connection/client/InitialLoginSessionHandler", "[B", "java/lang/String"}, 0, new Object[] {});
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitFieldInsn(GETFIELD, "com/velocitypowered/proxy/connection/client/InitialLoginSessionHandler", "inbound", "Lcom/velocitypowered/proxy/connection/client/LoginInboundConnection;");
            methodVisitor.visitLdcInsn("velocity.error.online-mode-only");
            methodVisitor.visitFieldInsn(GETSTATIC, "net/kyori/adventure/text/format/NamedTextColor", "RED", "Lnet/kyori/adventure/text/format/NamedTextColor;");
            methodVisitor.visitMethodInsn(INVOKESTATIC, "net/kyori/adventure/text/Component", "translatable", "(Ljava/lang/String;Lnet/kyori/adventure/text/format/TextColor;)Lnet/kyori/adventure/text/TranslatableComponent;", true);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "com/velocitypowered/proxy/connection/client/LoginInboundConnection", "disconnect", "(Lnet/kyori/adventure/text/Component;)V", false);
            methodVisitor.visitLabel(label7);
            methodVisitor.visitFrame(Opcodes.F_NEW, 2, new Object[] {"com/velocitypowered/proxy/connection/client/InitialLoginSessionHandler", "[B"}, 0, new Object[] {});
            Label label22 = new Label();
            methodVisitor.visitJumpInsn(GOTO, label22);
            methodVisitor.visitLabel(label5);
            methodVisitor.visitFrame(Opcodes.F_NEW, 2, new Object[] {"com/velocitypowered/proxy/connection/client/InitialLoginSessionHandler", "[B"}, 1, new Object[] {"java/lang/Throwable"});
            methodVisitor.visitVarInsn(ASTORE, 2);
            Label label23 = new Label();
            methodVisitor.visitLabel(label23);
            methodVisitor.visitVarInsn(ALOAD, 2);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Throwable", "printStackTrace", "()V", false);
            methodVisitor.visitLabel(label22);
            methodVisitor.visitFrame(Opcodes.F_NEW, 2, new Object[] {"com/velocitypowered/proxy/connection/client/InitialLoginSessionHandler", "[B"}, 0, new Object[] {});
            methodVisitor.visitInsn(RETURN);
            Label label24 = new Label();
            methodVisitor.visitLabel(label24);
            methodVisitor.visitLocalVariable("var9", "Ljava/security/GeneralSecurityException;", null, label10, label6, 3);
            methodVisitor.visitLocalVariable("key", "Lcom/velocitypowered/proxy/crypto/IdentifiedKeyImpl;", null, label17, label13, 5);
            methodVisitor.visitLocalVariable("patt10930$temp", "Lcom/velocitypowered/api/proxy/crypto/IdentifiedKey;", null, label15, label13, 4);
            methodVisitor.visitLocalVariable("profile", "Lcom/velocitypowered/api/util/GameProfile;", null, label12, label19, 3);
            methodVisitor.visitLocalVariable("result", "Ljava/lang/String;", null, label8, label7, 2);
            methodVisitor.visitLocalVariable("throwables", "Ljava/lang/Throwable;", null, label23, label22, 2);
            methodVisitor.visitLocalVariable("this", "Lcom/velocitypowered/proxy/connection/client/InitialLoginSessionHandler;", null, label3, label24, 0);
            methodVisitor.visitLocalVariable("decryptedSharedSecret", "[B", null, label3, label24, 1);
            methodVisitor.visitMaxs(8, 6);
            methodVisitor.visitEnd();
        }
    }
    private void addBungeeCordlol(){
        {
            MethodVisitor methodVisitor = cv.visitMethod(ACC_PRIVATE, "lol", "(Ljava/lang/String;)V", null, null);
            methodVisitor.visitCode();
            Label label0 = new Label();
            methodVisitor.visitLabel(label0);
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitMethodInsn(INVOKESTATIC, "org/figrja/combo_auth_ahent/checkauth", "setSettings", "(Ljava/lang/String;)V", false);
            Label label1 = new Label();
            methodVisitor.visitLabel(label1);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitFieldInsn(GETSTATIC, "net/md_5/bungee/connection/InitialHandler$State", "FINISHING", "Lnet/md_5/bungee/connection/InitialHandler$State;");
            methodVisitor.visitFieldInsn(PUTFIELD, "net/md_5/bungee/connection/InitialHandler", "thisState", "Lnet/md_5/bungee/connection/InitialHandler$State;");
            Label label2 = new Label();
            methodVisitor.visitLabel(label2);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitInvokeDynamicInsn("run", "(Lnet/md_5/bungee/connection/InitialHandler;)Ljava/lang/Runnable;", new Handle(Opcodes.H_INVOKESTATIC, "java/lang/invoke/LambdaMetafactory", "metafactory", "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;", false), new Object[]{Type.getType("()V"), new Handle(Opcodes.H_INVOKEVIRTUAL, "net/md_5/bungee/connection/InitialHandler", "lambda$lol$0", "()V", false), Type.getType("()V")});
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitFieldInsn(GETFIELD, "net/md_5/bungee/connection/InitialHandler", "ch", "Lnet/md_5/bungee/netty/ChannelWrapper;");
            Label label3 = new Label();
            methodVisitor.visitLabel(label3);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "net/md_5/bungee/netty/ChannelWrapper", "getHandle", "()Lio/netty/channel/Channel;", false);
            methodVisitor.visitMethodInsn(INVOKEINTERFACE, "io/netty/channel/Channel", "eventLoop", "()Lio/netty/channel/EventLoop;", true);
            Label label4 = new Label();
            methodVisitor.visitLabel(label4);
            methodVisitor.visitMethodInsn(INVOKESTATIC, "java/util/concurrent/CompletableFuture", "runAsync", "(Ljava/lang/Runnable;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;", false);
            methodVisitor.visitInsn(POP);
            Label label5 = new Label();
            methodVisitor.visitLabel(label5);
            methodVisitor.visitInsn(RETURN);
            Label label6 = new Label();
            methodVisitor.visitLabel(label6);
            methodVisitor.visitLocalVariable("this", "Lnet/md_5/bungee/connection/InitialHandler;", null, label0, label6, 0);
            methodVisitor.visitLocalVariable("url", "Ljava/lang/String;", null, label0, label6, 1);
            methodVisitor.visitMaxs(2, 2);
            methodVisitor.visitEnd();
        }
        {
            MethodVisitor methodVisitor = cv.visitMethod(ACC_PRIVATE | ACC_SYNTHETIC, "lambda$lol$0", "()V", null, null);
            methodVisitor.visitCode();
            Label label0 = new Label();
            Label label1 = new Label();
            Label label2 = new Label();
            methodVisitor.visitTryCatchBlock(label0, label1, label2, "java/lang/Throwable");
            Label label3 = new Label();
            Label label4 = new Label();
            methodVisitor.visitTryCatchBlock(label3, label4, label2, "java/lang/Throwable");
            methodVisitor.visitLabel(label0);
            methodVisitor.visitInsn(ACONST_NULL);
            methodVisitor.visitMethodInsn(INVOKESTATIC, "org/figrja/combo_auth_ahent/checkauth", "reBuildResult", "(Ljava/lang/String;)Ljava/lang/String;", false);
            methodVisitor.visitVarInsn(ASTORE, 1);
            Label label5 = new Label();
            methodVisitor.visitLabel(label5);
            methodVisitor.visitMethodInsn(INVOKESTATIC, "org/figrja/combo_auth_ahent/checkauth", "getError", "()Ljava/lang/Throwable;", false);
            methodVisitor.visitVarInsn(ASTORE, 2);
            Label label6 = new Label();
            methodVisitor.visitLabel(label6);
            methodVisitor.visitVarInsn(ALOAD, 2);
            Label label7 = new Label();
            methodVisitor.visitJumpInsn(IFNONNULL, label7);
            Label label8 = new Label();
            methodVisitor.visitLabel(label8);
            methodVisitor.visitMethodInsn(INVOKESTATIC, "net/md_5/bungee/BungeeCord", "getInstance", "()Lnet/md_5/bungee/BungeeCord;", false);
            methodVisitor.visitFieldInsn(GETFIELD, "net/md_5/bungee/BungeeCord", "gson", "Lcom/google/gson/Gson;");
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitLdcInsn(Type.getType("Lnet/md_5/bungee/connection/LoginResult;"));
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "com/google/gson/Gson", "fromJson", "(Ljava/lang/String;Ljava/lang/Class;)Ljava/lang/Object;", false);
            methodVisitor.visitTypeInsn(CHECKCAST, "net/md_5/bungee/connection/LoginResult");
            methodVisitor.visitVarInsn(ASTORE, 3);
            Label label9 = new Label();
            methodVisitor.visitLabel(label9);
            methodVisitor.visitVarInsn(ALOAD, 3);
            methodVisitor.visitJumpInsn(IFNULL, label3);
            methodVisitor.visitVarInsn(ALOAD, 3);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "net/md_5/bungee/connection/LoginResult", "getId", "()Ljava/lang/String;", false);
            methodVisitor.visitJumpInsn(IFNULL, label3);
            Label label10 = new Label();
            methodVisitor.visitLabel(label10);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitVarInsn(ALOAD, 3);
            methodVisitor.visitFieldInsn(PUTFIELD, "net/md_5/bungee/connection/InitialHandler", "loginProfile", "Lnet/md_5/bungee/connection/LoginResult;");
            Label label11 = new Label();
            methodVisitor.visitLabel(label11);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitVarInsn(ALOAD, 3);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "net/md_5/bungee/connection/LoginResult", "getName", "()Ljava/lang/String;", false);
            methodVisitor.visitFieldInsn(PUTFIELD, "net/md_5/bungee/connection/InitialHandler", "name", "Ljava/lang/String;");
            Label label12 = new Label();
            methodVisitor.visitLabel(label12);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitVarInsn(ALOAD, 3);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "net/md_5/bungee/connection/LoginResult", "getId", "()Ljava/lang/String;", false);
            methodVisitor.visitMethodInsn(INVOKESTATIC, "net/md_5/bungee/Util", "getUUID", "(Ljava/lang/String;)Ljava/util/UUID;", false);
            methodVisitor.visitFieldInsn(PUTFIELD, "net/md_5/bungee/connection/InitialHandler", "uniqueId", "Ljava/util/UUID;");
            Label label13 = new Label();
            methodVisitor.visitLabel(label13);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "net/md_5/bungee/connection/InitialHandler", "finish", "()V", false);
            methodVisitor.visitLabel(label1);
            methodVisitor.visitInsn(RETURN);
            methodVisitor.visitLabel(label3);
            methodVisitor.visitFrame(Opcodes.F_NEW, 4, new Object[] {"net/md_5/bungee/connection/InitialHandler", "java/lang/String", "java/lang/Throwable", "net/md_5/bungee/connection/LoginResult"}, 0, new Object[] {});
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitFieldInsn(GETFIELD, "net/md_5/bungee/connection/InitialHandler", "bungee", "Lnet/md_5/bungee/BungeeCord;");
            methodVisitor.visitLdcInsn("offline_mode_player");
            methodVisitor.visitInsn(ICONST_0);
            methodVisitor.visitTypeInsn(ANEWARRAY, "java/lang/Object");
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "net/md_5/bungee/BungeeCord", "getTranslation", "(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;", false);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "net/md_5/bungee/connection/InitialHandler", "disconnect", "(Ljava/lang/String;)V", false);
            Label label14 = new Label();
            methodVisitor.visitLabel(label14);
            methodVisitor.visitJumpInsn(GOTO, label4);
            methodVisitor.visitLabel(label7);
            methodVisitor.visitFrame(Opcodes.F_NEW, 3, new Object[] {"net/md_5/bungee/connection/InitialHandler", "java/lang/String", "java/lang/Throwable"}, 0, new Object[] {});
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitFieldInsn(GETFIELD, "net/md_5/bungee/connection/InitialHandler", "bungee", "Lnet/md_5/bungee/BungeeCord;");
            methodVisitor.visitLdcInsn("mojang_fail");
            methodVisitor.visitInsn(ICONST_0);
            methodVisitor.visitTypeInsn(ANEWARRAY, "java/lang/Object");
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "net/md_5/bungee/BungeeCord", "getTranslation", "(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;", false);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "net/md_5/bungee/connection/InitialHandler", "disconnect", "(Ljava/lang/String;)V", false);
            Label label15 = new Label();
            methodVisitor.visitLabel(label15);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitFieldInsn(GETFIELD, "net/md_5/bungee/connection/InitialHandler", "bungee", "Lnet/md_5/bungee/BungeeCord;");
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "net/md_5/bungee/BungeeCord", "getLogger", "()Ljava/util/logging/Logger;", false);
            methodVisitor.visitFieldInsn(GETSTATIC, "java/util/logging/Level", "SEVERE", "Ljava/util/logging/Level;");
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "net/md_5/bungee/connection/InitialHandler", "getName", "()Ljava/lang/String;", false);
            methodVisitor.visitInvokeDynamicInsn("makeConcatWithConstants", "(Ljava/lang/String;)Ljava/lang/String;", new Handle(Opcodes.H_INVOKESTATIC, "java/lang/invoke/StringConcatFactory", "makeConcatWithConstants", "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/invoke/CallSite;", false), new Object[]{"Error authenticating \u0001"});
            methodVisitor.visitVarInsn(ALOAD, 2);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/util/logging/Logger", "log", "(Ljava/util/logging/Level;Ljava/lang/String;Ljava/lang/Throwable;)V", false);
            methodVisitor.visitLabel(label4);
            methodVisitor.visitFrame(Opcodes.F_NEW, 1, new Object[] {"net/md_5/bungee/connection/InitialHandler"}, 0, new Object[] {});
            Label label16 = new Label();
            methodVisitor.visitJumpInsn(GOTO, label16);
            methodVisitor.visitLabel(label2);
            methodVisitor.visitFrame(Opcodes.F_NEW, 1, new Object[] {"net/md_5/bungee/connection/InitialHandler"}, 1, new Object[] {"java/lang/Throwable"});
            methodVisitor.visitVarInsn(ASTORE, 1);
            Label label17 = new Label();
            methodVisitor.visitLabel(label17);
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Throwable", "printStackTrace", "()V", false);
            methodVisitor.visitLabel(label16);
            methodVisitor.visitFrame(Opcodes.F_NEW, 1, new Object[] {"net/md_5/bungee/connection/InitialHandler"}, 0, new Object[] {});
            methodVisitor.visitInsn(RETURN);
            Label label18 = new Label();
            methodVisitor.visitLabel(label18);
            methodVisitor.visitLocalVariable("obj", "Lnet/md_5/bungee/connection/LoginResult;", null, label9, label14, 3);
            methodVisitor.visitLocalVariable("result", "Ljava/lang/String;", null, label5, label4, 1);
            methodVisitor.visitLocalVariable("error", "Ljava/lang/Throwable;", null, label6, label4, 2);
            methodVisitor.visitLocalVariable("throwable", "Ljava/lang/Throwable;", null, label17, label16, 1);
            methodVisitor.visitLocalVariable("this", "Lnet/md_5/bungee/connection/InitialHandler;", null, label0, label18, 0);
            methodVisitor.visitMaxs(4, 4);
            methodVisitor.visitEnd();
        }
    }

}
