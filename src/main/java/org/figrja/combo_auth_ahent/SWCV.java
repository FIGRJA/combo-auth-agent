package org.figrja.combo_auth_ahent;

import org.figrja.combo_auth.auth;
import org.figrja.combo_auth.config.debuglogger.LoggerMain;
import org.figrja.combo_auth.mixin.methodKOSTblL;
import org.objectweb.asm.*;

import java.io.IOException;

import static org.objectweb.asm.Opcodes.ASM7;

public class SWCV extends ClassVisitor {
    public SWCV(int api, ClassVisitor cv) {
        super(api, cv);

    }
    public static MethodVisitor mv = null;

    LoggerMain LOGGER = auth.Logger;


    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        if (name.equals("com/mojang/authlib/yggdrasil/YggdrasilMinecraftSessionService")) {
            LOGGER.info("start find method");
        }else {
            LOGGER.debug("start find our method");
        }
    }

    @Override
    public void visitSource(String source, String debug) {
    }

    @Override
    public void visitOuterClass(String owner, String name, String descriptor) {
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        return cv.visitAnnotation(descriptor, visible);
    }

    @Override
    public void visitAttribute(Attribute attribute) {
    }

    @Override
    public void visitInnerClass(String name, String outerName, String innerName, int access) {
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        LOGGER.debugRes("    "+descriptor+" "+name);
        return cv.visitField(access, name, descriptor, signature, value);
    }

    @Override
    public MethodVisitor visitMethod(int access,String name , String desc , String signature,String[] exceptions){
        LOGGER.debug("    "+name+desc);
        if (name.equals("hasJoinedServer")){
            LOGGER.info("found method");
            ClassReader classReader;
            try {
                classReader = new ClassReader(methodKOSTblL.class.getCanonicalName().replace("/","."));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ClassWriter classWriter = new ClassWriter(2);
            ClassVisitor classVisitor = new methodBOP(ASM7,classWriter);
            classReader.accept(classVisitor,0);
            LOGGER.info((this.mv == null) +" "+ (mv == null));
            return mv;
        }

        return null;
    }

    @Override
    public void visitEnd() {
        LOGGER.debug("end visit");
    }
}
