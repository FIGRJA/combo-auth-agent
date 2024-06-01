package org.figrja.combo_auth_ahent;

import org.figrja.combo_auth.checkauth;
import org.figrja.combo_auth.config.debuglogger.LoggerMain;
import org.figrja.combo_auth.auth;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import static org.objectweb.asm.Opcodes.ASM9;

public class ClassTransformer implements ClassFileTransformer {
    static LoggerMain LOGGER = org.figrja.combo_auth.auth.Logger;

    static auth auth = new auth();

    public ClassTransformer(){

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
                ClassReader classReader = new ClassReader(classfileBuffer);
                ClassWriter classWriter = new ClassWriter(classReader,1);
                SWCV classVisitor = new SWCV(ASM9, classWriter);
                classReader.accept(classVisitor, 0);
                LOGGER.debug("URA");
                Map<String,byte[]> map = new HashMap<>();
                try {
                    InputStream resourceAsStream = Premain.class.getClassLoader().getResourceAsStream("combo-auth-1.0.jar");
                    JarInputStream jarFile = null;
                    if (resourceAsStream != null) {
                        jarFile = new JarInputStream(resourceAsStream);
                    }else {
                        System.out.println("lol");
                    }
                    JarEntry e;
                    while (true){
                        e = jarFile.getNextJarEntry();
                        if (e == null){
                            break;
                        }
                        if (e.getName().endsWith(".class")) {
                            switch (e.getName().substring(0, e.getName().length() - 6)) {
                                case "org.figrja.combo_auth.ely.by.httpHelper":
                                case "org.figrja.combo_auth.ely.by.propery":
                                case "org.figrja.combo_auth.ely.by.resultElyGson":
                                case "org.figrja.combo_auth.checkauth":
                                    map.put(e.getName().substring(0, e.getName().length() - 6), e.getExtra());
                            }
                        }
                    }
                    ByteClassLoader loader_ = new ByteClassLoader( map);
                    for (String s:map.keySet()){
                        Class<?> aClass = loader_.loadClass(s);
                        Constructor constructors = aClass.getConstructor();
                        Object o = constructors.newInstance();
                        Method method = aClass.getMethod("<init>");
                        method.invoke(o);
                    }
                } catch (Throwable e) {
                    System.out.println("lol");
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
                return classWriter.toByteArray();
            }catch (Throwable a){

                a.printStackTrace();
            }

        }

        return classfileBuffer;
    }

}
