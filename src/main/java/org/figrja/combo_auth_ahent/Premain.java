package org.figrja.combo_auth_ahent;

import com.google.gson.Gson;
import org.figrja.combo_auth_ahent.config.Config;
import org.figrja.combo_auth_ahent.config.debuglogger.Debug;
import org.figrja.combo_auth_ahent.config.debuglogger.DebugAll;
import org.figrja.combo_auth_ahent.config.debuglogger.Logger;
import org.figrja.combo_auth_ahent.config.debuglogger.LoggerMain;

import java.io.*;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.ProtectionDomain;
import java.util.Objects;
import java.util.jar.JarFile;


public class Premain implements ClassFileTransformer {


    public static LoggerMain LOGGER = new Logger("combo_auth");
    static Config config;
    private static URLLoader myLoader;
    static Instrumentation ins;

    static  File file;

    private static boolean in = false;

    public Premain(){
        loadURLLoader();

        LOGGER.info("start load config");
        try {
            String s = "org.figrja.combo_auth_ahent.auth";
            Class<?> aClass = myLoader.findClass(s);
            Constructor constructors = aClass.getConstructor();
            Object o = constructors.newInstance();
            Method method = aClass.getMethod("onInitializeServer");
            config = (Config) method.invoke(o);
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
    }
    public static void newClass(){
        LOGGER.info("first authentication");
        in = true;
    }

    public static void premain(String args, Instrumentation inst){
        ins = inst;

        inst.addTransformer(new Premain());

    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer){
        if (Objects.equals(className, "net/md_5/bungee/Bootstrap")){
            System.out.println("not support");
        }
        else if (Objects.equals(className, "net/minecraft/bundler/Main")) {
            System.out.println("hiii vanila!!!");

        }
        else if (Objects.equals(className, "com/mojang/authlib/yggdrasil/YggdrasilMinecraftSessionService")) {

            try {

                byte[] bytes = ClassTransformer.start(classfileBuffer);
                try {
                    ins.appendToBootstrapClassLoaderSearch(new JarFile(file));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                return bytes;
            } catch (Throwable e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        return classfileBuffer;
    }


    public static <T> T fromGson(String json, Class<T> classOfT)  {
        LOGGER.debugRes("start transform");
        Class<?> aClass = null;
        try {
            String s = "org.figrja.combo_auth_ahent.KOSTblL";
            aClass = myLoader.findClass(s);
            Constructor constructors = aClass.getConstructor();
            Object o = constructors.newInstance();
            Method method = null;
            for (Method m :aClass.getMethods()){
                if (m.getName().equals("fromGson")) method = m;
            }
            return (T) method.invoke(o,json,classOfT);
        }
        catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }



    static private void loadURLLoader (){
        String[][] names = new String[3][7];
        names[0] = new String[]{"com", "google", "code", "gson", "gson","2.10.1","gson-2.10.1.jar"};
        names[1] = new String[]{"org", "figrja", "combo-auth","1.3.0","combo-auth-cut.jar"};
        if (!in)names[2] = new String[]{"org", "figrja", "combo-auth","1.3.0","combo-auth.jar"};
        try{
            int count = 0;
            URL[] urls;
            if (in) {
                urls = new URL[names.length-1];
            }
            else{
                urls = new URL[names.length];
            }
            for (String[] lib : names) {
                if (lib[0]==null)continue;
                File path = new File("libraries");
                for (String s:lib){
                    if (!path.exists()){
                        path.mkdir();
                        LOGGER.info("create dir : " + s);
                    }
                    path = new File(path,s);

                }

                File f = path;
                if (!f.exists()) {
                    LOGGER.info("copy "+f.getName());
                    Files.copy(Premain.class.getClassLoader().getResourceAsStream(f.getName()), f.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
                LOGGER.info("load "+f.getName());
                urls[count] = path.toURI().toURL();
                count++;
                file = f;
            }

            myLoader = new URLLoader(urls,Premain.class.getClassLoader());
        }catch (Throwable e){
            e.printStackTrace();
        }
    }


}