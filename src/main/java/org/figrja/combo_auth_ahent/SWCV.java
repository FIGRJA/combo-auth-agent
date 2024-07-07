package org.figrja.combo_auth_ahent;

import org.figrja.combo_auth_ahent.config.debuglogger.LoggerMain;
import org.objectweb.asm.*;

import static org.objectweb.asm.Opcodes.*;


public class SWCV extends ClassVisitor {

    public SWCV(int api, ClassVisitor cv) {
        super(api, cv);
    }


    static LoggerMain LOGGER = Premain.LOGGER;
    boolean needLamdaLOL0 = false;



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
            return new EXT(cv.visitMethod(access, name, desc, signature, exceptions),version);
        } else if (name.equals("done")&&desc.equals("(Ljava/lang/String;Ljava/lang/Throwable;)V")) {
            return new startWith(cv.visitMethod(access, name, desc, signature, exceptions));
        }else if (name.equals("handle")&&desc.equals("(Lnet/md_5/bungee/protocol/packet/EncryptionResponse;)V")){
            return new endWith(cv.visitMethod(access, name, desc, signature, exceptions));
        }else if (name.equals("handle")&&desc.equals("(Lcom/velocitypowered/proxy/protocol/packet/EncryptionResponsePacket;)Z")){
            needLamdaLOL0 = true;
            MethodVisitor method = cv.visitMethod(access, name, desc, signature, exceptions);
            return new insertURL(method);
        }
        return cv.visitMethod(access, name, desc, signature, exceptions);

    }

    private static class findURL extends MethodVisitor{
        public findURL( MethodVisitor methodVisitor) {
            super(ASM9, methodVisitor);
        }

    }

    private static class insertURL extends MethodVisitor{
        public insertURL( MethodVisitor methodVisitor) {
            super(ASM9, methodVisitor);

        }

        int index;Label url;boolean one = true;

        public void visitLocalVariable(
                final String name,
                final String descriptor,
                final String signature,
                final Label start,
                final Label end,
                final int index) {
            if (name.equals("url")){
                LOGGER.debug(start.toString());
                LOGGER.debug(String.valueOf(index));
                this.url = start;
                this.index = index;
            }
            mv.visitLocalVariable(name, descriptor, signature, start, end, index);
        }

        public void visitVarInsn(final int opcode, final int var) {
            mv.visitVarInsn(opcode, var);
            if (opcode == ASTORE&&var==7&&one) {
                one = false;
                //thx @konloch for bytecode viewer 2.12
                LOGGER.debug("i see it");
                MethodVisitor methodVisitor = mv;
                Label label2 = new Label();
                methodVisitor.visitLabel(label2);
                methodVisitor.visitFieldInsn(GETSTATIC, "com/velocitypowered/proxy/connection/client/InitialLoginSessionHandler", "logger", "Lorg/apache/logging/log4j/Logger;");
                methodVisitor.visitLdcInsn("test combo-auth1");
                methodVisitor.visitMethodInsn(INVOKEINTERFACE, "org/apache/logging/log4j/Logger", "info", "(Ljava/lang/String;)V", true);
                Label label3 = new Label();
                methodVisitor.visitLabel(label3);
                methodVisitor.visitVarInsn(ALOAD, 0);
                methodVisitor.visitVarInsn(ALOAD, 7);
                methodVisitor.visitVarInsn(ALOAD, 4);
                methodVisitor.visitMethodInsn(INVOKESPECIAL, "com/velocitypowered/proxy/connection/client/InitialLoginSessionHandler", "lol", "(Ljava/lang/String;[B)V", false);
                Label label4 = new Label();
                methodVisitor.visitLabel(label4);
                methodVisitor.visitInsn(ICONST_1);
                methodVisitor.visitInsn(IRETURN);
            }
        }
    }

    private static class startWith extends MethodVisitor{
        public startWith( MethodVisitor methodVisitor) {
            super(ASM9, methodVisitor);
        }

        public void visitCode(){
            mv.visitCode();
            Label insert = new Label();
            mv.visitLabel(insert);
            mv.visitVarInsn(ALOAD,1);
            mv.visitMethodInsn(INVOKESTATIC,"org/figrja/combo_auth_ahent/checkauth","reBuildResult","(Ljava/lang/String;)Ljava/lang/String;",false);
            mv.visitVarInsn(ASTORE,1);
            mv.visitMethodInsn(INVOKESTATIC,"org/figrja/combo_auth_ahent/checkauth","getError","()Ljava/lang/Throwable;",false);
            mv.visitVarInsn(ASTORE,2);
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
        public EXT(MethodVisitor mv,int version){
            super(ASM9,null);
            this.version = version;
            this.mv = mv;

        }

        @Override
        public void visitCode(){
            mv.visitCode();
            Label label0 = new Label();
            Label label1 = new Label();
            Label label2 = new Label();
            mv.visitTryCatchBlock(label0, label1, label2, "java/lang/Exception");
            Label label3 = new Label();
            Label label4 = new Label();
            mv.visitTryCatchBlock(label3, label4, label2, "java/lang/Exception");
            mv.visitLabel(label0);
            mv.visitTypeInsn(NEW, "org/figrja/combo_auth_ahent/checkauth");
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, "org/figrja/combo_auth_ahent/checkauth", "<init>", "()V", false);
            mv.visitVarInsn(ALOAD, 1);
            if (version == 0) {
                mv.visitMethodInsn(INVOKEVIRTUAL, "com/mojang/authlib/GameProfile", "getName", "()Ljava/lang/String;",false);}
            mv.visitVarInsn(ALOAD, 2);
            mv.visitMethodInsn(INVOKEVIRTUAL, "org/figrja/combo_auth_ahent/checkauth", "AuthListCheck", "(Ljava/lang/String;Ljava/lang/String;)Ljava/util/HashMap;", false);
            mv.visitVarInsn(ASTORE, 4);
            Label label5 = new Label();
            mv.visitLabel(label5);
            mv.visitVarInsn(ALOAD, 4);
            mv.visitJumpInsn(IFNONNULL, label3);
            Label label6 = new Label();
            mv.visitLabel(label6);
            mv.visitInsn(ACONST_NULL);
            mv.visitLabel(label1);
            mv.visitInsn(ARETURN);
            mv.visitLabel(label3);
            mv.visitFrame(Opcodes.F_NEW, 5, new Object[] {"com/mojang/authlib/yggdrasil/YggdrasilMinecraftSessionService", "java/lang/String", "java/lang/String", "java/net/InetAddress", "java/util/HashMap"}, 0, new Object[] {});
            mv.visitTypeInsn(NEW, "com/mojang/authlib/GameProfile");
            mv.visitInsn(DUP);
            mv.visitVarInsn(ALOAD, 4);
            mv.visitLdcInsn("id");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/HashMap", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", false);
            mv.visitTypeInsn(CHECKCAST, "java/util/UUID");
            mv.visitVarInsn(ALOAD, 4);
            mv.visitLdcInsn("name");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/HashMap", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", false);
            mv.visitTypeInsn(CHECKCAST, "java/lang/String");
            mv.visitMethodInsn(INVOKESPECIAL, "com/mojang/authlib/GameProfile", "<init>", "(Ljava/util/UUID;Ljava/lang/String;)V", false);
            mv.visitVarInsn(ASTORE, 5);
            Label label7 = new Label();
            mv.visitLabel(label7);
            mv.visitVarInsn(ALOAD, 4);
            mv.visitLdcInsn("properties");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/HashMap", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", false);
            mv.visitTypeInsn(CHECKCAST, "java/util/ArrayList");
            mv.visitVarInsn(ASTORE, 6);
            Label label8 = new Label();
            mv.visitLabel(label8);
            mv.visitVarInsn(ALOAD, 6);
            Label label9 = new Label();
            mv.visitJumpInsn(IFNULL, label9);
            Label label10 = new Label();
            mv.visitLabel(label10);
            mv.visitVarInsn(ALOAD, 6);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/ArrayList", "iterator", "()Ljava/util/Iterator;", false);
            mv.visitVarInsn(ASTORE, 7);
            Label label11 = new Label();
            mv.visitLabel(label11);
            mv.visitFrame(Opcodes.F_NEW, 8, new Object[] {"com/mojang/authlib/yggdrasil/YggdrasilMinecraftSessionService", "java/lang/String", "java/lang/String", "java/net/InetAddress", "java/util/HashMap", "com/mojang/authlib/GameProfile", "java/util/ArrayList", "java/util/Iterator"}, 0, new Object[] {});
            mv.visitVarInsn(ALOAD, 7);
            mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Iterator", "hasNext", "()Z", true);
            mv.visitJumpInsn(IFEQ, label9);
            Label label12 = new Label();
            mv.visitLabel(label12);
            mv.visitVarInsn(ALOAD, 7);
            mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Iterator", "next", "()Ljava/lang/Object;", true);
            mv.visitTypeInsn(CHECKCAST, "[Ljava/lang/String;");
            mv.visitVarInsn(ASTORE, 8);
            Label label13 = new Label();
            mv.visitLabel(label13);
            mv.visitVarInsn(ALOAD, 5);
            mv.visitMethodInsn(INVOKEVIRTUAL, "com/mojang/authlib/GameProfile", "getProperties", "()Lcom/mojang/authlib/properties/PropertyMap;", false);
            mv.visitVarInsn(ALOAD, 8);
            mv.visitInsn(ICONST_0);
            mv.visitInsn(AALOAD);
            mv.visitTypeInsn(NEW, "com/mojang/authlib/properties/Property");
            mv.visitInsn(DUP);
            mv.visitVarInsn(ALOAD, 8);
            mv.visitInsn(ICONST_0);
            mv.visitInsn(AALOAD);
            mv.visitVarInsn(ALOAD, 8);
            mv.visitInsn(ICONST_1);
            mv.visitInsn(AALOAD);
            mv.visitVarInsn(ALOAD, 8);
            mv.visitInsn(ICONST_2);
            mv.visitInsn(AALOAD);
            mv.visitMethodInsn(INVOKESPECIAL, "com/mojang/authlib/properties/Property", "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", false);
            mv.visitMethodInsn(INVOKEVIRTUAL, "com/mojang/authlib/properties/PropertyMap", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Z", false);
            mv.visitInsn(POP);
            Label label14 = new Label();
            mv.visitLabel(label14);
            mv.visitJumpInsn(GOTO, label11);
            mv.visitLabel(label9);
            mv.visitFrame(Opcodes.F_NEW, 7, new Object[] {"com/mojang/authlib/yggdrasil/YggdrasilMinecraftSessionService", "java/lang/String", "java/lang/String", "java/net/InetAddress", "java/util/HashMap", "com/mojang/authlib/GameProfile", "java/util/ArrayList"}, 0, new Object[] {});
            if (version == 1) {
                mv.visitTypeInsn(NEW, "com/mojang/authlib/yggdrasil/ProfileResult");
                mv.visitInsn(DUP);
            }
            mv.visitVarInsn(ALOAD, 5);
            if (version == 1) {
                mv.visitVarInsn(ALOAD, 4);
                mv.visitLdcInsn("actions");
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/HashMap", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", false);
                mv.visitTypeInsn(CHECKCAST, "java/util/Set");
                mv.visitMethodInsn(INVOKESPECIAL, "com/mojang/authlib/yggdrasil/ProfileResult", "<init>", "(Lcom/mojang/authlib/GameProfile;Ljava/util/Set;)V", false);
            }mv.visitLabel(label4);
            mv.visitInsn(ARETURN);
            mv.visitLabel(label2);
            mv.visitFrame(Opcodes.F_NEW, 4, new Object[] {"com/mojang/authlib/yggdrasil/YggdrasilMinecraftSessionService", "java/lang/String", "java/lang/String", "java/net/InetAddress"}, 1, new Object[] {"java/lang/Exception"});
            mv.visitVarInsn(ASTORE, 4);
            Label label15 = new Label();
            mv.visitLabel(label15);
            mv.visitTypeInsn(NEW, "com/mojang/authlib/exceptions/AuthenticationUnavailableException");
            mv.visitInsn(DUP);
            mv.visitLdcInsn("Cannot contact authentication server");
            mv.visitVarInsn(ALOAD, 4);
            mv.visitMethodInsn(INVOKESPECIAL, "com/mojang/authlib/exceptions/AuthenticationUnavailableException", "<init>", "(Ljava/lang/String;Ljava/lang/Throwable;)V", false);
            mv.visitInsn(ATHROW);
            Label label16 = new Label();
            mv.visitLabel(label16);
            mv.visitLocalVariable("pro", "[Ljava/lang/String;", null, label13, label14, 8);
            mv.visitLocalVariable("result", "Lcom/mojang/authlib/GameProfile;", null, label7, label2, 5);
            mv.visitLocalVariable("propery", "Ljava/util/ArrayList;", null, label8, label2, 6);
            mv.visitLocalVariable("resultHash", "Ljava/util/HashMap;", null, label5, label2, 4);
            mv.visitLocalVariable("e", "Ljava/lang/Exception;", null, label15, label16, 4);
            mv.visitLocalVariable("this", "Lcom/mojang/authlib/yggdrasil/YggdrasilMinecraftSessionService;", null, label0, label16, 0);
            mv.visitLocalVariable("profileName", "Ljava/lang/String;", null, label0, label16, 1);
            mv.visitLocalVariable("sarverid", "Ljava/lang/String;", null, label0, label16, 2);
            mv.visitMaxs(8, 9);
            mv.visitEnd();
        }
    }



    @Override
    public void visitEnd() {
        if (needLamdaLOL0) {
            addlol();
            addlambdalol();
        }LOGGER.debug("end visit");
        cv.visitEnd();
    }

    private void addlol(){
        MethodVisitor methodVisitor = cv.visitMethod(ACC_PRIVATE, "lol", "(Ljava/lang/String;[B)V", null, null);
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
        methodVisitor.visitLineNumber(40, label3);
        methodVisitor.visitFieldInsn(GETSTATIC, "com/velocitypowered/proxy/connection/client/InitialLoginSessionHandler", "logger", "Lorg/apache/logging/log4j/Logger;");
        methodVisitor.visitLdcInsn("test combo-auth2");
        methodVisitor.visitMethodInsn(INVOKEINTERFACE, "org/apache/logging/log4j/Logger", "info", "(Ljava/lang/String;)V", true);
        Label label8 = new Label();
        methodVisitor.visitLabel(label8);
        methodVisitor.visitLineNumber(41, label8);
        methodVisitor.visitVarInsn(ALOAD, 1);
        methodVisitor.visitMethodInsn(INVOKESTATIC, "org/figrja/combo_auth_ahent/checkauth", "setSettings", "(Ljava/lang/String;)V", false);
        Label label9 = new Label();
        methodVisitor.visitLabel(label9);
        methodVisitor.visitLineNumber(43, label9);
        methodVisitor.visitFieldInsn(GETSTATIC, "com/velocitypowered/proxy/connection/client/InitialLoginSessionHandler", "logger", "Lorg/apache/logging/log4j/Logger;");
        methodVisitor.visitLdcInsn("test combo-auth #1");
        methodVisitor.visitMethodInsn(INVOKEINTERFACE, "org/apache/logging/log4j/Logger", "info", "(Ljava/lang/String;)V", true);
        methodVisitor.visitLabel(label0);
        methodVisitor.visitLineNumber(45, label0);
        methodVisitor.visitFieldInsn(GETSTATIC, "com/velocitypowered/proxy/connection/client/InitialLoginSessionHandler", "logger", "Lorg/apache/logging/log4j/Logger;");
        methodVisitor.visitLdcInsn("test combo-auth #2");
        methodVisitor.visitMethodInsn(INVOKEINTERFACE, "org/apache/logging/log4j/Logger", "info", "(Ljava/lang/String;)V", true);
        Label label10 = new Label();
        methodVisitor.visitLabel(label10);
        methodVisitor.visitLineNumber(46, label10);
        methodVisitor.visitVarInsn(ALOAD, 0);
        methodVisitor.visitFieldInsn(GETFIELD, "com/velocitypowered/proxy/connection/client/InitialLoginSessionHandler", "mcConnection", "Lcom/velocitypowered/proxy/connection/MinecraftConnection;");
        methodVisitor.visitVarInsn(ALOAD, 2);
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "com/velocitypowered/proxy/connection/MinecraftConnection", "enableEncryption", "([B)V", false);
        methodVisitor.visitLabel(label1);
        methodVisitor.visitLineNumber(51, label1);
        methodVisitor.visitJumpInsn(GOTO, label6);
        methodVisitor.visitLabel(label2);
        methodVisitor.visitLineNumber(47, label2);
        methodVisitor.visitFrame(Opcodes.F_NEW, 3, new Object[] {"com/velocitypowered/proxy/connection/client/InitialLoginSessionHandler", "java/lang/String", "[B"}, 1, new Object[] {"java/security/GeneralSecurityException"});
        methodVisitor.visitVarInsn(ASTORE, 3);
        Label label11 = new Label();
        methodVisitor.visitLabel(label11);
        methodVisitor.visitLineNumber(48, label11);
        methodVisitor.visitFieldInsn(GETSTATIC, "com/velocitypowered/proxy/connection/client/InitialLoginSessionHandler", "logger", "Lorg/apache/logging/log4j/Logger;");
        methodVisitor.visitLdcInsn("Unable to enable encryption for connection");
        methodVisitor.visitVarInsn(ALOAD, 3);
        methodVisitor.visitMethodInsn(INVOKEINTERFACE, "org/apache/logging/log4j/Logger", "error", "(Ljava/lang/String;Ljava/lang/Throwable;)V", true);
        Label label12 = new Label();
        methodVisitor.visitLabel(label12);
        methodVisitor.visitLineNumber(49, label12);
        methodVisitor.visitVarInsn(ALOAD, 0);
        methodVisitor.visitFieldInsn(GETFIELD, "com/velocitypowered/proxy/connection/client/InitialLoginSessionHandler", "mcConnection", "Lcom/velocitypowered/proxy/connection/MinecraftConnection;");
        methodVisitor.visitInsn(ICONST_1);
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "com/velocitypowered/proxy/connection/MinecraftConnection", "close", "(Z)V", false);
        methodVisitor.visitLabel(label4);
        methodVisitor.visitLineNumber(50, label4);
        methodVisitor.visitInsn(RETURN);
        methodVisitor.visitLabel(label6);
        methodVisitor.visitLineNumber(52, label6);
        methodVisitor.visitFrame(Opcodes.F_NEW, 3, new Object[] {"com/velocitypowered/proxy/connection/client/InitialLoginSessionHandler", "java/lang/String", "[B"}, 0, new Object[] {});
        methodVisitor.visitTypeInsn(NEW, "java/util/concurrent/CompletableFuture");
        methodVisitor.visitInsn(DUP);
        methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/util/concurrent/CompletableFuture", "<init>", "()V", false);
        methodVisitor.visitVarInsn(ASTORE, 3);
        Label label13 = new Label();
        methodVisitor.visitLabel(label13);
        methodVisitor.visitLineNumber(53, label13);
        methodVisitor.visitVarInsn(ALOAD, 3);
        methodVisitor.visitInsn(POP);
        methodVisitor.visitInvokeDynamicInsn("get", "()Ljava/util/function/Supplier;", new Handle(Opcodes.H_INVOKESTATIC, "java/lang/invoke/LambdaMetafactory", "metafactory", "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;", false), new Object[]{Type.getType("()Ljava/lang/Object;"), new Handle(Opcodes.H_INVOKESTATIC, "com/velocitypowered/proxy/connection/client/InitialLoginSessionHandler", "lambda$lol$0", "()Ljava/lang/String;", false), Type.getType("()Ljava/lang/String;")});
        methodVisitor.visitMethodInsn(INVOKESTATIC, "java/util/concurrent/CompletableFuture", "supplyAsync", "(Ljava/util/function/Supplier;)Ljava/util/concurrent/CompletableFuture;", false);
        methodVisitor.visitVarInsn(ALOAD, 0);
        methodVisitor.visitInvokeDynamicInsn("accept", "(Lcom/velocitypowered/proxy/connection/client/InitialLoginSessionHandler;)Ljava/util/function/BiConsumer;", new Handle(Opcodes.H_INVOKESTATIC, "java/lang/invoke/LambdaMetafactory", "metafactory", "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;", false), new Object[]{Type.getType("(Ljava/lang/Object;Ljava/lang/Object;)V"), new Handle(Opcodes.H_INVOKESPECIAL, "com/velocitypowered/proxy/connection/client/InitialLoginSessionHandler", "lambda$lol$1", "(Ljava/lang/String;Ljava/lang/Throwable;)V", false), Type.getType("(Ljava/lang/String;Ljava/lang/Throwable;)V")});
        Label label14 = new Label();
        methodVisitor.visitLabel(label14);
        methodVisitor.visitLineNumber(60, label14);
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/util/concurrent/CompletableFuture", "whenCompleteAsync", "(Ljava/util/function/BiConsumer;)Ljava/util/concurrent/CompletableFuture;", false);
        methodVisitor.visitInsn(POP);
        methodVisitor.visitLabel(label7);
        methodVisitor.visitLineNumber(78, label7);
        Label label15 = new Label();
        methodVisitor.visitJumpInsn(GOTO, label15);
        methodVisitor.visitLabel(label5);
        methodVisitor.visitLineNumber(76, label5);
        methodVisitor.visitFrame(Opcodes.F_NEW, 3, new Object[] {"com/velocitypowered/proxy/connection/client/InitialLoginSessionHandler", "java/lang/String", "[B"}, 1, new Object[] {"java/lang/Throwable"});
        methodVisitor.visitVarInsn(ASTORE, 3);
        Label label16 = new Label();
        methodVisitor.visitLabel(label16);
        methodVisitor.visitLineNumber(77, label16);
        methodVisitor.visitVarInsn(ALOAD, 3);
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Throwable", "printStackTrace", "()V", false);
        methodVisitor.visitLabel(label15);
        methodVisitor.visitLineNumber(78, label15);
        methodVisitor.visitFrame(Opcodes.F_NEW, 3, new Object[] {"com/velocitypowered/proxy/connection/client/InitialLoginSessionHandler", "java/lang/String", "[B"}, 0, new Object[] {});
        methodVisitor.visitInsn(RETURN);
        Label label17 = new Label();
        methodVisitor.visitLabel(label17);
        methodVisitor.visitLocalVariable("var9", "Ljava/security/GeneralSecurityException;", null, label11, label6, 3);
        methodVisitor.visitLocalVariable("stringCompletableFuture", "Ljava/util/concurrent/CompletableFuture;", "Ljava/util/concurrent/CompletableFuture<Ljava/lang/String;>;", label13, label7, 3);
        methodVisitor.visitLocalVariable("throwable", "Ljava/lang/Throwable;", null, label16, label15, 3);
        methodVisitor.visitLocalVariable("this", "Lcom/velocitypowered/proxy/connection/client/InitialLoginSessionHandler;", null, label3, label17, 0);
        methodVisitor.visitLocalVariable("url", "Ljava/lang/String;", null, label3, label17, 1);
        methodVisitor.visitLocalVariable("decryptedSharedSecret", "[B", null, label3, label17, 2);
        methodVisitor.visitMaxs(3, 4);
        methodVisitor.visitEnd();
    }
    private void addlambdalol(){
        MethodVisitor methodVisitor = cv.visitMethod(ACC_PRIVATE | ACC_STATIC | ACC_SYNTHETIC, "lambda$lol$0", "()Ljava/lang/String;", null, null);
        methodVisitor.visitCode();
        Label label0 = new Label();
        methodVisitor.visitLabel(label0);
        methodVisitor.visitLineNumber(56, label0);
        methodVisitor.visitFieldInsn(GETSTATIC, "com/velocitypowered/proxy/connection/client/InitialLoginSessionHandler", "logger", "Lorg/apache/logging/log4j/Logger;");
        methodVisitor.visitLdcInsn("test combo-auth #3");
        methodVisitor.visitMethodInsn(INVOKEINTERFACE, "org/apache/logging/log4j/Logger", "info", "(Ljava/lang/String;)V", true);
        Label label1 = new Label();
        methodVisitor.visitLabel(label1);
        methodVisitor.visitLineNumber(57, label1);
        methodVisitor.visitInsn(ACONST_NULL);
        methodVisitor.visitMethodInsn(INVOKESTATIC, "org/figrja/combo_auth_ahent/checkauth", "reBuildResult", "(Ljava/lang/String;)Ljava/lang/String;", false);
        methodVisitor.visitVarInsn(ASTORE, 0);
        Label label2 = new Label();
        methodVisitor.visitLabel(label2);
        methodVisitor.visitLineNumber(58, label2);
        methodVisitor.visitFieldInsn(GETSTATIC, "com/velocitypowered/proxy/connection/client/InitialLoginSessionHandler", "logger", "Lorg/apache/logging/log4j/Logger;");
        methodVisitor.visitLdcInsn("test combo-auth #4");
        methodVisitor.visitMethodInsn(INVOKEINTERFACE, "org/apache/logging/log4j/Logger", "info", "(Ljava/lang/String;)V", true);
        Label label3 = new Label();
        methodVisitor.visitLabel(label3);
        methodVisitor.visitLineNumber(59, label3);
        methodVisitor.visitVarInsn(ALOAD, 0);
        methodVisitor.visitInsn(ARETURN);
        Label label4 = new Label();
        methodVisitor.visitLabel(label4);
        methodVisitor.visitLocalVariable("result", "Ljava/lang/String;", null, label2, label4, 0);
        methodVisitor.visitMaxs(2, 1);
        methodVisitor.visitEnd();
    }
    private void addlambdalol2(){
        MethodVisitor methodVisitor = cv.visitMethod(ACC_PRIVATE | ACC_SYNTHETIC, "lambda$lol$1", "(Ljava/lang/String;Ljava/lang/Throwable;)V", null, null);
        methodVisitor.visitCode();
        Label label0 = new Label();
        methodVisitor.visitLabel(label0);
        methodVisitor.visitLineNumber(61, label0);
        methodVisitor.visitVarInsn(ALOAD, 1);
        Label label1 = new Label();
        methodVisitor.visitJumpInsn(IFNULL, label1);
        Label label2 = new Label();
        methodVisitor.visitLabel(label2);
        methodVisitor.visitLineNumber(62, label2);
        methodVisitor.visitFieldInsn(GETSTATIC, "com/velocitypowered/proxy/connection/client/InitialLoginSessionHandler", "logger", "Lorg/apache/logging/log4j/Logger;");
        methodVisitor.visitLdcInsn("test combo-auth #5");
        methodVisitor.visitMethodInsn(INVOKEINTERFACE, "org/apache/logging/log4j/Logger", "info", "(Ljava/lang/String;)V", true);
        Label label3 = new Label();
        methodVisitor.visitLabel(label3);
        methodVisitor.visitLineNumber(63, label3);
        methodVisitor.visitFieldInsn(GETSTATIC, "com/velocitypowered/proxy/VelocityServer", "GENERAL_GSON", "Lcom/google/gson/Gson;");
        methodVisitor.visitVarInsn(ALOAD, 1);
        methodVisitor.visitLdcInsn(Type.getType("Lcom/velocitypowered/api/util/GameProfile;"));
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "com/google/gson/Gson", "fromJson", "(Ljava/lang/String;Ljava/lang/Class;)Ljava/lang/Object;", false);
        methodVisitor.visitTypeInsn(CHECKCAST, "com/velocitypowered/api/util/GameProfile");
        methodVisitor.visitVarInsn(ASTORE, 3);
        Label label4 = new Label();
        methodVisitor.visitLabel(label4);
        methodVisitor.visitLineNumber(64, label4);
        methodVisitor.visitFieldInsn(GETSTATIC, "com/velocitypowered/proxy/connection/client/InitialLoginSessionHandler", "logger", "Lorg/apache/logging/log4j/Logger;");
        methodVisitor.visitVarInsn(ALOAD, 3);
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "com/velocitypowered/api/util/GameProfile", "toString", "()Ljava/lang/String;", false);
        methodVisitor.visitMethodInsn(INVOKEINTERFACE, "org/apache/logging/log4j/Logger", "info", "(Ljava/lang/String;)V", true);
        Label label5 = new Label();
        methodVisitor.visitLabel(label5);
        methodVisitor.visitLineNumber(65, label5);
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
        Label label6 = new Label();
        methodVisitor.visitLabel(label6);
        methodVisitor.visitLineNumber(66, label6);
        Label label7 = new Label();
        methodVisitor.visitJumpInsn(GOTO, label7);
        methodVisitor.visitLabel(label1);
        methodVisitor.visitLineNumber(67, label1);
        methodVisitor.visitFrame(Opcodes.F_NEW, 3, new Object[] {"com/velocitypowered/proxy/connection/client/InitialLoginSessionHandler", "java/lang/String", "java/lang/Throwable"}, 0, new Object[] {});
        methodVisitor.visitMethodInsn(INVOKESTATIC, "org/figrja/combo_auth_ahent/checkauth", "getError", "()Ljava/lang/Throwable;", false);
        Label label8 = new Label();
        methodVisitor.visitJumpInsn(IFNULL, label8);
        Label label9 = new Label();
        methodVisitor.visitLabel(label9);
        methodVisitor.visitLineNumber(68, label9);
        methodVisitor.visitVarInsn(ALOAD, 0);
        methodVisitor.visitFieldInsn(GETFIELD, "com/velocitypowered/proxy/connection/client/InitialLoginSessionHandler", "inbound", "Lcom/velocitypowered/proxy/connection/client/LoginInboundConnection;");
        methodVisitor.visitLdcInsn("multiplayer.disconnect.authservers_down");
        methodVisitor.visitMethodInsn(INVOKESTATIC, "net/kyori/adventure/text/Component", "translatable", "(Ljava/lang/String;)Lnet/kyori/adventure/text/TranslatableComponent;", true);
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "com/velocitypowered/proxy/connection/client/LoginInboundConnection", "disconnect", "(Lnet/kyori/adventure/text/Component;)V", false);
        methodVisitor.visitJumpInsn(GOTO, label7);
        methodVisitor.visitLabel(label8);
        methodVisitor.visitLineNumber(70, label8);
        methodVisitor.visitFrame(Opcodes.F_NEW, 3, new Object[] {"com/velocitypowered/proxy/connection/client/InitialLoginSessionHandler", "java/lang/String", "java/lang/Throwable"}, 0, new Object[] {});
        methodVisitor.visitVarInsn(ALOAD, 0);
        methodVisitor.visitFieldInsn(GETFIELD, "com/velocitypowered/proxy/connection/client/InitialLoginSessionHandler", "inbound", "Lcom/velocitypowered/proxy/connection/client/LoginInboundConnection;");
        methodVisitor.visitLdcInsn("velocity.error.online-mode-only");
        methodVisitor.visitFieldInsn(GETSTATIC, "net/kyori/adventure/text/format/NamedTextColor", "RED", "Lnet/kyori/adventure/text/format/NamedTextColor;");
        methodVisitor.visitMethodInsn(INVOKESTATIC, "net/kyori/adventure/text/Component", "translatable", "(Ljava/lang/String;Lnet/kyori/adventure/text/format/TextColor;)Lnet/kyori/adventure/text/TranslatableComponent;", true);
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "com/velocitypowered/proxy/connection/client/LoginInboundConnection", "disconnect", "(Lnet/kyori/adventure/text/Component;)V", false);
        methodVisitor.visitLabel(label7);
        methodVisitor.visitLineNumber(73, label7);
        methodVisitor.visitFrame(Opcodes.F_NEW, 3, new Object[] {"com/velocitypowered/proxy/connection/client/InitialLoginSessionHandler", "java/lang/String", "java/lang/Throwable"}, 0, new Object[] {});
        methodVisitor.visitFieldInsn(GETSTATIC, "com/velocitypowered/proxy/connection/client/InitialLoginSessionHandler", "logger", "Lorg/apache/logging/log4j/Logger;");
        methodVisitor.visitLdcInsn("test combo-auth #6");
        methodVisitor.visitMethodInsn(INVOKEINTERFACE, "org/apache/logging/log4j/Logger", "info", "(Ljava/lang/String;)V", true);
        Label label10 = new Label();
        methodVisitor.visitLabel(label10);
        methodVisitor.visitLineNumber(75, label10);
        methodVisitor.visitInsn(RETURN);
        Label label11 = new Label();
        methodVisitor.visitLabel(label11);
        methodVisitor.visitLocalVariable("profile", "Lcom/velocitypowered/api/util/GameProfile;", null, label4, label6, 3);
        methodVisitor.visitLocalVariable("this", "Lcom/velocitypowered/proxy/connection/client/InitialLoginSessionHandler;", null, label0, label11, 0);
        methodVisitor.visitLocalVariable("result", "Ljava/lang/String;", null, label0, label11, 1);
        methodVisitor.visitLocalVariable("Throwable", "Ljava/lang/Throwable;", null, label0, label11, 2);
        methodVisitor.visitMaxs(8, 4);
        methodVisitor.visitEnd();
    }
}
