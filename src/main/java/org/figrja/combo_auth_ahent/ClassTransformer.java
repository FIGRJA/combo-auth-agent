package org.figrja.combo_auth_ahent;


import org.figrja.combo_auth_ahent.config.debuglogger.LoggerMain;
import org.figrja.org.objectweb.asm.ClassReader;
import org.figrja.org.objectweb.asm.ClassWriter;

import java.io.File;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.Objects;
import java.util.jar.JarFile;

import static org.figrja.org.objectweb.asm.Opcodes.ASM9;

public class ClassTransformer implements ClassFileTransformer {
    static LoggerMain LOGGER = org.figrja.combo_auth_ahent.auth.Logger;

    static auth auth = new auth();

    Instrumentation inst;

    public ClassTransformer(Instrumentation inst){
        this.inst = inst;
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
                LOGGER = org.figrja.combo_auth_ahent.auth.Logger;
                ClassReader classReader = new ClassReader(classfileBuffer);
                ClassWriter classWriter = new ClassWriter(classReader,1);
                SWCV classVisitor = new SWCV(ASM9, classWriter);
                classReader.accept(classVisitor, 0);
                try {
                    String s = Premain.class.getProtectionDomain().getCodeSource().getLocation().getFile();
                    File file = new File(s);
                    //inst.appendToBootstrapClassLoaderSearch(new JarFile(file));
                    inst.appendToSystemClassLoaderSearch(new JarFile(file));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                LOGGER.info("combo_auth has been enabled!");
                return classWriter.toByteArray();
            }catch (Throwable a){

                a.printStackTrace();
            }

        }

        return classfileBuffer;
    }



}
