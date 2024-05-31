package org.figrja.combo_auth_ahent;

import org.figrja.combo_auth.auth;
import org.figrja.combo_auth.config.debuglogger.LoggerMain;
import org.objectweb.asm.*;
import static org.objectweb.asm.Opcodes.*;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.util.*;

import java.io.IOException;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.util.List;


public class SWCV extends ClassVisitor {

    ClassWriter cw;
    public SWCV(int api, ClassVisitor cv,ClassWriter cw) {
        super(api, cv);
        this.cw = cw;
    }


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
    public MethodVisitor visitMethod(int access,String name , String desc , String signature,String[] exceptions) {
        LOGGER.debug("    " + name + desc);
        if (name.equals("hhasJoinedServer")) {
            LOGGER.info("found method");
            int version;
            if (desc.equals("(Lcom/mojang/authlib/GameProfile;Ljava/lang/String;Ljava/net/InetAddress;)Lcom/mojang/authlib/GameProfile;")) {
                LOGGER.info("version with GameProfile");
                version = 0;
            } else if (desc.equals("(Ljava/lang/String;Ljava/lang/String;Ljava/net/InetAddress;)Lcom/mojang/authlib/yggdrasil/ProfileResult;")) {
                LOGGER.info("version with ProfileResult");
                version = 1;
            } else {
                LOGGER.info("unknown version");
                return null;
            }
            LOGGER.debug("insert our method");
            MethodVisitor method = new EXT(cv.visitMethod(access, name, desc, signature, exceptions),version);
            return method;
        }
        return cv.visitMethod(access, name, desc, signature, exceptions);

    }

    public static class EXT extends MethodVisitor{
        int version;
        MethodVisitor mv;
        public EXT(MethodVisitor mv,int version){
            super(ASM9,null);
            this.version = version;
            this.mv = mv;

        }

        @Override
        public void visitCode(){
            mv.visitCode();
            Label l1 = new Label();
            mv.visitLabel(l1);
            if (version == 1) {
                mv.visitTypeInsn(NEW, "com/mojang/authlib/yggdrasil/ProfileResult");
                mv.visitInsn(DUP);
            }
            mv.visitTypeInsn(NEW,"org/figrja/combo_auth/mixin/ReCheckAuth");
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL,"org/figrja/combo_auth/mixin/ReCheckAuth","<init>","()V");
            mv.visitVarInsn(ALOAD,1);
            if (version == 0) {
                mv.visitMethodInsn(INVOKEVIRTUAL, "com/mojang/authlib/GameProfile", "getName", "()Ljava/lang/String;");
            }
            mv.visitVarInsn(ALOAD,2);
            mv.visitVarInsn(ALOAD,3);
            mv.visitMethodInsn(INVOKEVIRTUAL ,"org/figrja/combo_auth/mixin/ReCheckAuth","AuthListCheck", "(Ljava/lang/String;Ljava/lang/String;Ljava/net/InetAddress;)Lcom/mojang/authlib/GameProfile;");
            if (version == 1){
                mv.visitMethodInsn(INVOKEVIRTUAL ,"com/mojang/authlib/yggdrasil/ProfileResult","<init>", "(Lcom/mojang/authlib/GameProfile;)V");
            }
            mv.visitInsn(ARETURN);
            mv.visitMaxs(1,1);
            mv.visitEnd();
        }
    }



    private void printByteCode(MethodNode MN){
        printer = new Textifier();
        mp = new TraceMethodVisitor(printer);
        InsnList inList = MN.instructions;
        for (int i = 0; i < inList.size(); i++) {
            LOGGER.debugRes(insnToString(inList.get(i)));
        }


    }
    Printer printer ;
    TraceMethodVisitor mp;

    private String insnToString(AbstractInsnNode insn){
        insn.accept(mp);
        StringWriter sw = new StringWriter();
        printer.print(new PrintWriter(sw));
        printer.getText().clear();
        return sw.toString();
    }



    @Override
    public void visitEnd() {
        LOGGER.debug("end visit");
    }
}
