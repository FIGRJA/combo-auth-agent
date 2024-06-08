package org.figrja.combo_auth_ahent;


import org.figrja.combo_auth_ahent.config.debuglogger.LoggerMain;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.util.jar.JarFile;

import static org.objectweb.asm.Opcodes.ASM9;

public class ClassTransformer {
    LoggerMain LOGGER = Premain.LOGGER;


    Instrumentation inst;

    public ClassTransformer(){
    }

    public byte[] start(byte[] classfileBuffer){
        try {
            ClassReader classReader = new ClassReader(classfileBuffer);
            ClassWriter classWriter = new ClassWriter(classReader,1);
            SWCV classVisitor = new SWCV(ASM9, classWriter);
            classReader.accept(classVisitor, 0);
            byte[] bytes = classWriter.toByteArray();
            try {
                String s = Premain.class.getProtectionDomain().getCodeSource().getLocation().getFile();
                File file = new File(s);
                inst.appendToBootstrapClassLoaderSearch(new JarFile(file));
                inst.appendToSystemClassLoaderSearch(new JarFile(file));
            } catch (Throwable e) {
                e.printStackTrace();
            }
            LOGGER.info("combo_auth has been enabled!");
            return bytes;
        }catch (Throwable a){
            a.printStackTrace();
            throw a;
        }
    }



}
