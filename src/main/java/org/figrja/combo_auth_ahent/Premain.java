package org.figrja.combo_auth_ahent;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
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



    public static void premain(String args, Instrumentation inst) {
        try {
            String s = Premain.class.getProtectionDomain().getCodeSource().getLocation().getFile();
            File file = new File(s);
            inst.appendToBootstrapClassLoaderSearch(new JarFile(file));
            inst.appendToSystemClassLoaderSearch(new JarFile(file));
        } catch (Throwable e) {
            e.printStackTrace();
        }



        inst.addTransformer(new ClassTransformer());



    }







}