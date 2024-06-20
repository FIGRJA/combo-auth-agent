package org.figrja.combo_auth_ahent;


import org.figrja.combo_auth_ahent.config.debuglogger.LoggerMain;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.IOException;

import static org.objectweb.asm.Opcodes.ASM9;

public class ClassTransformer {
    static LoggerMain LOGGER = Premain.LOGGER;
    public ClassTransformer(){
        LOGGER.debug("hii");
    }
    public static byte[] start(String className){
        try {
            return trans(new ClassReader(className));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] start(byte[] classfileBuffer){
        return trans(new ClassReader(classfileBuffer));
    }
    private static byte[] trans(ClassReader classReader){
        LOGGER.info("try");
        try {
            ClassWriter classWriter = new ClassWriter(classReader,1);
            SWCV classVisitor = new SWCV(ASM9, classWriter);
            classReader.accept(classVisitor, 0);

            LOGGER.info("combo_auth has been enabled!");
            return classWriter.toByteArray();
        }catch (Throwable a){
            a.printStackTrace();
        }
        return null;
    }


}
