package org.figrja.combo_auth_ahent;

import org.figrja.combo_auth.mixin.methodKOSTblL;
import org.objectweb.asm.*;

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
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        System.out.println(name +" ex " +superName+" {");
    }

    @Override
    public void visitSource(String source, String debug) {
        System.out.println(1);
    }

    @Override
    public void visitOuterClass(String owner, String name, String descriptor) {
        System.out.println(2);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        System.out.println(3);
        return cv.visitAnnotation(descriptor, visible);
    }

    @Override
    public void visitAttribute(Attribute attribute) {
        System.out.println(4);
    }

    @Override
    public void visitInnerClass(String name, String outerName, String innerName, int access) {
        System.out.println(5);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        System.out.println("    "+descriptor+" "+name);
        return cv.visitField(access, name, descriptor, signature, value);
    }

    @Override
    public MethodVisitor visitMethod(int access,String name , String desc , String signature,String[] exceptions){
        System.out.println("    "+name+desc);
        if (name.equals("hasJoinedServer")){
            ClassReader classReader;
            try {
                classReader = new ClassReader(methodKOSTblL.class.getCanonicalName().replace("/","."));
            } catch (IOException e) {
                System.out.println("lolop");

                throw new RuntimeException(e);
            }
            ClassWriter classWriter = new ClassWriter(2);
            ClassVisitor classVisitor = new SWCV(ASM4,classWriter);
            classReader.accept(classVisitor,0);

            return mv;
        }
        if (name.equals("KOSTblL()")){
            inputmethod(cv.visitMethod(access, name, desc, signature, exceptions));
            return cv.visitMethod(access, name, desc, signature, exceptions);
        }

        return null;
    }

    @Override
    public void visitEnd() {
        System.out.println("}");
    }
}
