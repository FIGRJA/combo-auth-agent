package org.figrja.combo_auth_ahent;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.Objects;

import org.figrja.combo_auth.auth;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import static org.objectweb.asm.Opcodes.ASM4;


public class Premain implements ClassFileTransformer {
    static PrintWriter printWriter;

    static auth auth;
    public static void premain(String args, Instrumentation inst){
        System.out.println("Hello world!");

        auth.onInitializeServer();
        inst.addTransformer(new Premain());

    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        printWriter.println(loader.toString() +" - "+ className);
        printWriter.flush();
        if (Objects.equals(className, "net/md_5/bungee/Bootstrap")){
            System.out.println("not support");
        }
         else if (Objects.equals(className, "net/minecraft/bundler/Main")) {
            System.out.println("hiii vanila!!");

        }
        else if (Objects.equals(className, "com/mojang/authlib/yggdrasil/YggdrasilMinecraftSessionService")) {
            printWriter.println("LOL");
            printWriter.flush();


                ClassReader classReader = new ClassReader(classfileBuffer);
                ClassWriter classWriter = new ClassWriter(2);
                ClassVisitor classVisitor = new SWCV(ASM4,classWriter);
                classReader.accept(classVisitor,0);
                return classWriter.toByteArray();

        }

        return classfileBuffer;
    }

}