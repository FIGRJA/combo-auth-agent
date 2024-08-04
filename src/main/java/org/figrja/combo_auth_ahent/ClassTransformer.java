package org.figrja.combo_auth_ahent;


import org.figrja.combo_auth_ahent.config.debuglogger.LoggerMain;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.IOException;

import static org.objectweb.asm.Opcodes.*;

public class ClassTransformer {

    static PreCV classData;
    static LoggerMain LOGGER = Premain.LOGGER;
    public ClassTransformer(){
        LOGGER.debug("hii");
    }

    static int ASM;

    private static void checkASM(){
        LOGGER.info(System.getProperty("java.version"));
        if (System.getProperty("java.version").startsWith("1.8") ){
            ASM = ASM5;
        }else {
            ASM = ASM9;
        }
    }
    public static byte[] start(byte[] classfileBuffer){
        checkASM();
        ClassReader cr = new ClassReader(classfileBuffer);
        ClassWriter cw = new ClassWriter(cr,ClassWriter.COMPUTE_FRAMES);
        classData = new PreCV(ASM, cw);
        cr.accept(classData, 0);
        return trans(new ClassReader(classfileBuffer));
    }
    private static byte[] trans(ClassReader classReader){
        LOGGER.info("try");
        try {
            ClassWriter classWriter = new ClassWriter(classReader,ClassWriter.COMPUTE_FRAMES);
            SWCV classVisitor = new SWCV(ASM, classWriter);
            classReader.accept(classVisitor, 0);
            LOGGER.info("combo_auth has been enabled!");
            return classWriter.toByteArray();
        }catch (Throwable a){
            a.printStackTrace();
        }
        return null;
    }


}
