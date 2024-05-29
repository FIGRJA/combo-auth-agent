package org.figrja.combo_auth_ahent;

import org.figrja.combo_auth.auth;
import org.figrja.combo_auth.config.configGson;
import org.figrja.combo_auth.config.debuglogger.LoggerMain;
import org.figrja.combo_auth.mixin.methodKOSTblL;
import org.objectweb.asm.*;

import java.io.IOException;

import static org.objectweb.asm.Opcodes.ASM4;

public class SWCV extends ClassVisitor {
    public SWCV(int api, ClassVisitor cv) {
        super(api, cv);

    }
    MethodVisitor mv;

    LoggerMain LOGGER = auth.Logger;

    public void inputmethod(MethodVisitor mv){
        this.mv = mv;
    }

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
        LOGGER.debug("    "+descriptor+" "+name);
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
            ClassVisitor classVisitor = new SWCV(ASM4,classWriter);
            classReader.accept(classVisitor,0);

            return mv;
        }
        if (name.equals("KOSTblL()")){
            LOGGER.debug("found our method");
            inputmethod(cv.visitMethod(access, name, desc, signature, exceptions));
            return cv.visitMethod(access, name, desc, signature, exceptions);
        }

        return null;
    }

    @Override
    public void visitEnd() {
        LOGGER.debug("end visit");
    }
}
