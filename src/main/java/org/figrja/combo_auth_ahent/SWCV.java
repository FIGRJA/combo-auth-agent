package org.figrja.combo_auth_ahent;

import org.figrja.combo_auth.mixin.methodKOSTblL;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.ClassVisitor;

import java.io.IOException;

import static org.objectweb.asm.Opcodes.ASM4;

public class SWCV extends ClassVisitor {
    public SWCV(int api, ClassVisitor cv) {
        super(api, cv);

    }
    MethodVisitor mv;

    public void inputmethod(MethodVisitor mv){
        this.mv = mv;
    }

    @Override
    public MethodVisitor visitMethod(int access,String name , String desc , String signature,String[] exceptions){
        if (name.equals("hasJoinedServer")){
            try {
                ClassReader classReader = new ClassReader(methodKOSTblL.class.getName());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ClassWriter classWriter = new ClassWriter(2);
            ClassVisitor classVisitor = new SWCV(ASM4,classWriter);
            return mv;
        }
        if (name.equals("KOSTblL")){
            inputmethod(cv.visitMethod(access, name, desc, signature, exceptions));
            return cv.visitMethod(access, name, desc, signature, exceptions);
        }

        return cv.visitMethod(access, name, desc, signature, exceptions);
    }

}
