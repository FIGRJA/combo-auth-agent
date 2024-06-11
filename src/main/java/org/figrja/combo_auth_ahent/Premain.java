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
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.ProtectionDomain;
import java.util.Objects;
import java.util.jar.JarFile;


public class Premain implements ClassFileTransformer {

    private static final URL PATH = Premain.class.getProtectionDomain().getCodeSource().getLocation();
    static LoggerMain LOGGER = new Logger("combo_auth");
    static Config config;
    private static URLLoader myLoader;
    static Instrumentation ins;

    public static void premain(String args, Instrumentation inst) throws Throwable {
        ins = inst;
        String[][] libs = new String[2][7];
        libs[0] = new String[]{"org", "ow2", "asm", "asm", "9.2","asm-9.2.jar"};
        libs[1] = new String[]{"com", "google", "code", "gson", "gson","2.10.1","gson-2.10.1.jar"};
        LOGGER.info("start load lib");
        try {
            loadURLLoader(libs);
        } catch (Throwable e) {
            e.printStackTrace();
            throw e;
        }

        LOGGER.info("start load config");try {
            String s = "org.figrja.combo_auth_ahent.ClassTransformer";
            Class<?> aClass = myLoader.findClass(s);
            Constructor constructors = aClass.getConstructor();
            Object o = constructors.newInstance();
            LOGGER.info("loadded");
        }catch (Throwable e){
            e.printStackTrace();
        }
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
                Class<?> mClass = myLoader.findClass("org.figrja.combo_auth_ahent.ClassTransformer");
                Constructor mconstructors = mClass.getConstructor();
                Object mo = mconstructors.newInstance();
                Method method = mClass.getMethod("start",new Class[]{byte.class});
                byte[] bytes = (byte[]) method.invoke(mo, new Object[] { classfileBuffer });
                try {
                    String ss = Premain.class.getProtectionDomain().getCodeSource().getLocation().getFile();
                    File file = new File(ss);
                    ins.appendToBootstrapClassLoaderSearch(new JarFile(file));
                    ins.appendToSystemClassLoaderSearch(new JarFile(file));
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

    static private void CopyResource(String resourceName , File Path) throws IOException {
        LOGGER.info("create jar : " + resourceName +" at "+ Path);
        InputStream stream = null;
        OutputStream resStreamOut = null;
        try {
            stream = Premain.class.getClassLoader().getResourceAsStream(resourceName);//note that each / is a directory down in the "jar tree" been the jar the root of the tree
            if(stream == null) {
                LOGGER.info("lol");
            }
            int readBytes;
            byte[] buffer = new byte[4096];
            resStreamOut = Files.newOutputStream(Path.toPath());
            if(resourceName == null) {
                LOGGER.info("lol x2");
            }while ((readBytes = stream.read(buffer)) > 0) {
                resStreamOut.write(buffer, 0, readBytes);
            }
        } finally {
            stream.close();
            resStreamOut.close();
        }
    }

    static private void loadURLLoader (String[][] names ) throws Throwable{
        int count = 0;
        URL[] urls = new URL[names.length+1];
        urls[count] = PATH;
        for (String[] lib : names) {
            File path = new File("libraries");
            for (String s:lib){
                if (!path.exists()){
                    path.mkdir();
                    LOGGER.info("create dir : " + s);
                }
                path = new File(path,s);

                LOGGER.info(path.getCanonicalPath());
            }

            File f = path;
            if (!f.exists()) {
                Files.copy(Premain.class.getClassLoader().getResourceAsStream(f.getName()), f.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
            LOGGER.info("load "+f.getName());
            count++;
            urls[count] = path.toURI().toURL();

        }
        myLoader = new URLLoader(urls);
    }


}