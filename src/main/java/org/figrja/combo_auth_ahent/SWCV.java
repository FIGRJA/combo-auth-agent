package org.figrja.combo_auth_ahent;

import org.figrja.combo_auth.auth;
import org.figrja.combo_auth.config.debuglogger.LoggerMain;
import org.figrja.combo_auth.mixin.methodKOSTblL;
import org.figrja.combo_auth.mixin.methodKOSTblLnew;
import org.objectweb.asm.*;

import java.io.IOException;

import static org.objectweb.asm.Opcodes.ASM7;

public class SWCV extends ClassVisitor {
    public SWCV(int api, ClassVisitor cv) {
        super(api, cv);

    }
    public static MethodVisitor mv;

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
                if (desc.equals("(Lcom/mojang/authlib/GameProfile;Ljava/lang/String;Ljava/net/InetAddress;)Lcom/mojang/authlib/GameProfile;")) {
                    LOGGER.info("version with GameProfile");
                    classReader = new ClassReader(methodKOSTblL.class.getCanonicalName().replace('/', '.'));
                }else if (desc.equals("(Ljava/lang/String;Ljava/lang/String;Ljava/net/InetAddress;)Lcom/mojang/authlib/yggdrasil/ProfileResult;")) {
                    LOGGER.info("version with ProfileResult");
                    classReader = new ClassReader(methodKOSTblLnew.class.getCanonicalName().replace('/', '.'));
                }else{
                    LOGGER.info("unknown version");
                    return null;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ClassWriter classWriter = new ClassWriter(classReader,2);
            ClassVisitor classVisitor = new SWCV(ASM7,classWriter);
            classReader.accept(classVisitor,0);
            LOGGER.debug("insert our method");
            return mv;
        }if (name.equals("KOSTblL")){
            LOGGER.debug("found our method");
            SWCV.mv = cv.visitMethod(access, name, desc, signature, exceptions);
            LOGGER.debug(String.valueOf(SWCV.mv != null));
            return cv.visitMethod(access, name, desc, signature, exceptions);
        }

        return cv.visitMethod(access, name, desc, signature, exceptions);
    }

    @Override
    public void visitEnd() {
        LOGGER.debug("end visit");
    }
}
