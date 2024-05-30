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
    public SWCV(int api, ClassVisitor cv) {
        super(api, cv);

    }


    LoggerMain LOGGER = auth.Logger;

    static boolean twoLayer = false;


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
        if (name.equals("hasJoinedServer") && !twoLayer) {
            LOGGER.info("found method");
            //ClassReader classReader;
            int version;
            if (desc.equals("(Lcom/mojang/authlib/GameProfile;Ljava/lang/String;Ljava/net/InetAddress;)Lcom/mojang/authlib/GameProfile;")) {
                LOGGER.info("version with GameProfile");
                //classReader = new ClassReader(methodKOSTblL.class.getCanonicalName().replace('/', '.'));
                version = 0;
            } else if (desc.equals("(Ljava/lang/String;Ljava/lang/String;Ljava/net/InetAddress;)Lcom/mojang/authlib/yggdrasil/ProfileResult;")) {
                LOGGER.info("version with ProfileResult");
                //classReader = new ClassReader(methodKOSTblLnew.class.getCanonicalName().replace('/', '.'));
                version = 1;
            } else {
                LOGGER.info("unknown version");
                return null;
            }
            //ClassWriter classWriter = new ClassWriter(classReader, 0);
            //ClassVisitor classVisitor = new SWCV(ASM9, classWriter);
            //twoLayer = true;
            LOGGER.debug("insert our method");
            /*
            ClassNode classNode = new ClassNode();
            classReader.accept(classNode, 0);
            @SuppressWarnings("unchecked") final List<MethodNode> methods = classNode.methods;
            for (MethodNode m : methods) {
                InsnList inList = m.instructions;
                System.out.println(m.name);
                for (int i = 0; i < inList.size(); i++) {
                    LOGGER.debugRes(insnToString(inList.get(i)));
                }
            }
             */
            MethodVisitor method = cv.visitMethod(access, name, desc, signature, exceptions);
            generate(method,version);
            return method;
        }
        return cv.visitMethod(access, name, desc, signature, exceptions);

    }

    void generate(MethodVisitor mv,int version){
        mv.visitCode();
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
        mv.visitEnd();
    }

    String insnToString(AbstractInsnNode insn){
        insn.accept(mp);
        StringWriter sw = new StringWriter();
        printer.print(new PrintWriter(sw));
        printer.getText().clear();
        return sw.toString();
    }

    private static Printer printer = new Textifier();
    private static TraceMethodVisitor mp = new TraceMethodVisitor(printer);

    @Override
    public void visitEnd() {
        LOGGER.debug("end visit");
    }
}
