package org.figrja.combo_auth_ahent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.Objects;

import org.figrja.combo_auth.auth;
import org.figrja.combo_auth.config.debuglogger.LoggerMain;
import org.figrja.combo_auth.mixin.ReCheckAuth;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import static org.objectweb.asm.Opcodes.ASM9;


public class Premain implements ClassFileTransformer {

    static LoggerMain LOGGER = org.figrja.combo_auth.auth.Logger;

    static auth auth = new auth();
    public static void premain(String args, Instrumentation inst){
        System.out.println("Hello world!");


        inst.addTransformer(new Premain());

    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer){

        if (Objects.equals(className, "net/md_5/bungee/Bootstrap")){
            System.out.println("not support");
        }
         else if (Objects.equals(className, "net/minecraft/bundler/Main")) {
            System.out.println("hiii vanila!!");

        }
        else if (Objects.equals(className, "com/mojang/authlib/yggdrasil/YggdrasilMinecraftSessionService")) {
            try {
                auth.onInitializeServer();
                new ReCheckAuth();
                ClassReader classReader = new ClassReader(classfileBuffer);
                ClassWriter classWriter = new ClassWriter(classReader,1);
                SWCV classVisitor = new SWCV(ASM9, classWriter);
                classReader.accept(classVisitor, 0);
                LOGGER.debug("URA");
                return classWriter.toByteArray();
            }catch (Throwable a){

                a.printStackTrace();
            }

        }

        return classfileBuffer;
    }




}