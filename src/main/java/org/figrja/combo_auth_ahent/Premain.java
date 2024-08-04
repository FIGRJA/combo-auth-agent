package org.figrja.combo_auth_ahent;

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
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.Objects;
import java.util.jar.JarFile;


public class Premain implements ClassFileTransformer {


    public static LoggerMain LOGGER = new Logger("combo_auth");
    static Config config;

    private static URLLoader myLoader;
    private static HashMap<String,Class<?>> kostbll = new HashMap<>();
    static Instrumentation ins;

    static  File file;

    private static boolean in = false;

    public void setLogger(LoggerMain LOGGER){
        Premain.LOGGER = LOGGER;
    }

    public Premain(){
            loadURLLoader();

            LOGGER.info("start load");
            try {
                config = (Config) callMethod("org.figrja.combo_auth_ahent.auth", "onInitializeServer");
                LOGGER.info("loadded");
            } catch (Throwable e) {
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
        if (Objects.equals(className, "net/md_5/bungee/Bootstrap")||
                Objects.equals(className, "com/velocitypowered/proxy/Velocity")){
            //LOGGER.info("try work with it");
        }
        else if (Objects.equals(className, "net/minecraft/bundler/Main")) {
            LOGGER.info("hiii vanila!!!");

        }
        else if (Objects.equals(className, "com/mojang/authlib/yggdrasil/YggdrasilMinecraftSessionService")||
                Objects.equals(className, "net/md_5/bungee/connection/InitialHandler")||
                Objects.equals(className, "com/velocitypowered/proxy/connection/client/InitialLoginSessionHandler")) {

            try {
                //callMethod("org.figrja.combo_auth_ahent.Premain","setLogger",LOGGER);
                byte[] bytes = (byte[]) callMethod("org.figrja.combo_auth_ahent.ClassTransformer","start",classfileBuffer);
                try {
                    ins.appendToBootstrapClassLoaderSearch(new JarFile(file));
                } catch (Throwable e) {
                    e.printStackTrace();
                }/*
                File fil = new File("class0.class");
                int tfi = 1;
                while (fil.exists()){
                    fil = new File("class"+ tfi++ +".class");
                }
                try(FileOutputStream fos = new FileOutputStream(fil)){
                    fos.write(bytes);
                }*/
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
        return (T) callMethod("org.figrja.combo_auth_ahent.KOSTblL","fromGson",json,classOfT);
    }
    public static String toGson(Object json)  {
        LOGGER.debugRes("start transform");
        return (String) callMethod("org.figrja.combo_auth_ahent.KOSTblL","toGson",json);
    }

    private static Object callMethod(String _class,String name,Object... args){
        try {
            if (kostbll.get(_class) ==null) {
                kostbll.put(_class,myLoader.findClass(_class));
            }
            Constructor constructors = kostbll.get(_class).getConstructor();
            Object o = constructors.newInstance();
            Method method = null;
            for (Method m :kostbll.get(_class).getMethods()){
                if (m.getName().equals(name)) method = m;
            }
            return method.invoke(o,args);
        }
        catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }



    static private void loadURLLoader (){
        String[][] names = new String[4][7];
        names[0] = new String[]{"com", "google", "code", "gson", "gson","2.10.1","gson-2.10.1.jar"};
        names[1] = new String[]{"org", "figrja", "combo-auth","1.5.0","combo-auth-cut.jar"};
        names[2] = new String[]{"org", "ow2","asm", "asm","9.2","asm-9.2.jar"};
        if (!in)names[3] = new String[]{"org", "figrja", "combo-auth","1.5.0","combo-auth.jar"};
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