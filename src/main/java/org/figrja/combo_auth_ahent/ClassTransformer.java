package org.figrja.combo_auth_ahent;


import org.figrja.combo_auth_ahent.config.debuglogger.LoggerMain;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.IOException;

import static org.objectweb.asm.Opcodes.ASM9;

public class ClassTransformer {

    static PreCV classData;
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
        ClassReader cr = new ClassReader(classfileBuffer);
        ClassWriter cw = new ClassWriter(cr,ClassWriter.COMPUTE_FRAMES);
        classData = new PreCV(ASM9, cw);
        cr.accept(classData, 0);
        return trans(new ClassReader(classfileBuffer));
    }
    private static byte[] trans(ClassReader classReader){
        LOGGER.info("try");
        try {
            ClassWriter classWriter = new ClassWriter(classReader,ClassWriter.COMPUTE_FRAMES);
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
