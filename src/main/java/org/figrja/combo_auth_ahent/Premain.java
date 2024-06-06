package org.figrja.combo_auth_ahent;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.security.CodeSource;
import java.util.jar.JarFile;


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


        inst.addTransformer(new ClassTransformer(inst));



    }







}