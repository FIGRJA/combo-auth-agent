package org.figrja.combo_auth_ahent;

import org.figrja.combo_auth_ahent.config.Config;
import org.figrja.combo_auth_ahent.config.debuglogger.Debug;
import org.figrja.combo_auth_ahent.config.debuglogger.DebugAll;
import org.figrja.combo_auth_ahent.config.debuglogger.Logger;
import org.figrja.combo_auth_ahent.config.debuglogger.LoggerMain;

import java.io.File;
import java.io.InputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;


public class Premain implements ClassFileTransformer {
    static LoggerMain LOGGER = new DebugAll("combo_auth");
    static Config config;
    private static ByteClassLoader myLoader;
    static Instrumentation ins;

    public static void premain(String args, Instrumentation inst) {
        ins = inst;
        try {
            loadLoader();
            LOGGER.info("test");
            String s = "org.figrja.combo_auth_ahent.auth";
            Class<?> aClass = myLoader.findClass(s);
            Constructor constructors = aClass.getConstructor();
            Object o = constructors.newInstance();
            Method method = aClass.getMethod("onInitializeServer");
            config = (Config) method.invoke(o);
            LOGGER.info(String.valueOf(config!=null));
            LOGGER.info("loadded");
        }catch (Throwable e){
            e.printStackTrace();
        }


        if (config.getGebugStatus() != null) {
            LOGGER.info(config.getGebugStatus());
            if (config.getGebugStatus().equals("detail")) {
                LOGGER = new Debug("combo_auth");
            }

            if (config.getGebugStatus().equals("all")) {
                LOGGER = new DebugAll("combo_auth");
            }
        }

        inst.addTransformer(new Premain());



    }

    private static void loadLoader() throws Throwable{
        Map<String,byte[]> map = new HashMap<String, byte[]>();
        String[] urls = {"self","asm-9.2.jar","gson-2.10.1.jar"};
        for(String s :urls){
            LOGGER.info(s);
            InputStream resourceAsStream;
            if (s.equals("self")) {
                resourceAsStream = Premain.class.getProtectionDomain().getCodeSource().getLocation().openStream();
            }else {
                resourceAsStream = Premain.class.getClassLoader().getResourceAsStream(s);
            }
            JarInputStream jarFile;
            if (resourceAsStream != null) {
                jarFile = new JarInputStream(resourceAsStream);
                JarEntry e;
                while (true) {
                    e = (JarEntry) jarFile.getNextEntry();
                    if (e == null) {
                        break;
                    }
                    if (e.getName().endsWith(".class")&&!e.getName().equals("org/igrja/combo_auth_ahent/Premain.class")) {
                        String path = e.getName().substring(0, e.getName().length() - 6).replace('/', '.');
                        LOGGER.info("   "+path);
                        map.put(path, jarFile.readAllBytes());
                    }
                }
            }
            myLoader = new ByteClassLoader(map);
    }}

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

                String s = "org.figrja.combo_auth_ahent.ClassTransformer";
                Class<?> aClass = myLoader.findClass(s);
                Constructor constructors = aClass.getConstructor();
                Object o = constructors.newInstance();
                byte[] bytes = new byte[0];
                for (Method m: aClass.getMethods()){
                    if (m.getName().equals("start")){
                        bytes = (byte[]) m.invoke(o, classfileBuffer);
                    }
                }
                try {
                    String ss = Premain.class.getProtectionDomain().getCodeSource().getLocation().getFile();
                    File file = new File(ss);
                    ins.appendToBootstrapClassLoaderSearch(new JarFile(file));
                    ins.appendToSystemClassLoaderSearch(new JarFile(file));
                } catch (Throwable e) {
                    e.printStackTrace();
                }loadLoader();
                return bytes;
            } catch (Throwable e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        return classfileBuffer;
    }
    public static <T> T fromGson(String json, Class<T> classOfT)  {
        try {
            String s = "com.google.gson.Gson";
            Class<?> aClass = myLoader.findClass(s);
            Constructor constructors = aClass.getConstructor();
            Object o = constructors.newInstance();
            Method method = aClass.getMethod("fromJson");
            return (T) method.invoke(o,json,classOfT);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

}