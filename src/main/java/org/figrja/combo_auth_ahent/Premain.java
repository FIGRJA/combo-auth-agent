package org.figrja.combo_auth_ahent;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class Premain implements ClassFileTransformer {



    public static void premain(String args, Instrumentation inst){
        Map<String,byte[]> map = new HashMap<String, byte[]>();
        try {
            InputStream resourceAsStream = Premain.class.getClassLoader().getResourceAsStream("combo-auth-1.0.jar");
            ZipInputStream jarFile;
            if (resourceAsStream != null) {
                jarFile = new ZipInputStream(resourceAsStream);
                ZipEntry e;
                while (true) {
                    e = jarFile.getNextEntry();
                    if (e == null) {
                        break;
                    }
                    if (e.getName().endsWith(".class")) {
                        String s = e.getName().substring(0, e.getName().length() - 6).replace('/','.');
                        switch (s) {
                            case "org.figrja.combo_auth.auth":
                            case "org.figrja.combo_auth.checkauth":
                            case "org.figrja.combo_auth.ely.by.resultElyGson":
                            case "org.figrja.combo_auth.ely.by.propery":
                            case "org.figrja.combo_auth.ely.by.httpHelper":
                            case "org.figrja.combo_auth.config.configGson":
                            case "org.figrja.combo_auth.config.AuthSchemaList":
                            case "org.figrja.combo_auth.config.debuglogger.LoggerMain":
                            case "org.figrja.combo_auth.config.debuglogger.Logger":
                            case "org.figrja.combo_auth.config.debuglogger.DebugAll":
                            case "org.figrja.combo_auth.config.debuglogger.Debug": {
                                map.put(s,jarFile.readAllBytes());
                            }
                        }
                    }
                }
            }
            ByteClassLoader loader = new ByteClassLoader( map);

            for(String s :map.keySet()) {
                Class<?> aClass = loader.loadClass(s);
                /*
                Constructor constructors = aClass.getConstructor();
                Object o = constructors.newInstance();
                Method method = aClass.getMethod("onInitializeServer");
                method.invoke(o);
                 */
            }

        } catch (Throwable e) {
            System.out.println("lol");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        inst.addTransformer(new ClassTransformer());



    }







}