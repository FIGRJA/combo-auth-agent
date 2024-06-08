package org.figrja.combo_auth_ahent;


import org.figrja.combo_auth_ahent.config.debuglogger.DebugAll;
import org.figrja.combo_auth_ahent.config.debuglogger.LoggerMain;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import static org.objectweb.asm.Opcodes.ASM9;

public class ClassTransformer {
    static LoggerMain LOGGER = Premain.LOGGER;
    public ClassTransformer(){
        LOGGER.debug("hii");
    }

    public static byte[] start(byte[] classfileBuffer){
        LOGGER.info("try");
        try {
            ClassReader classReader = new ClassReader(classfileBuffer);
            ClassWriter classWriter = new ClassWriter(classReader,1);
            SWCV classVisitor = new SWCV(ASM9, classWriter);
            classReader.accept(classVisitor, 0);

            LOGGER.info("combo_auth has been enabled!");
            return classWriter.toByteArray();
        }catch (Throwable a){
            a.printStackTrace();
        }
        return classfileBuffer;
    }



}
