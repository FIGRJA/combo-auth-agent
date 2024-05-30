package org.figrja.combo_auth_ahent;

import org.figrja.combo_auth.auth;
import org.figrja.combo_auth.config.debuglogger.LoggerMain;
import org.objectweb.asm.*;
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
    public static MethodVisitor mv;

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
            ClassReader classReader;
            try {
                if (desc.equals("(Lcom/mojang/authlib/GameProfile;Ljava/lang/String;Ljava/net/InetAddress;)Lcom/mojang/authlib/GameProfile;")) {
                    LOGGER.info("version with GameProfile");
                    classReader = new ClassReader(methodKOSTblL.class.getCanonicalName().replace('/', '.'));
                } else if (desc.equals("(Ljava/lang/String;Ljava/lang/String;Ljava/net/InetAddress;)Lcom/mojang/authlib/yggdrasil/ProfileResult;")) {
                    LOGGER.info("version with ProfileResult");
                    classReader = new ClassReader(methodKOSTblLnew.class.getCanonicalName().replace('/', '.'));
                } else {
                    LOGGER.info("unknown version");
                    return null;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ClassWriter classWriter = new ClassWriter(classReader, 2);
            ClassVisitor classVisitor = new SWCV(Opcodes.ASM9, classWriter);
            twoLayer = true;
            //LOGGER.debug("insert our method");
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
        }
        return cv.visitMethod(access, name, desc, signature, exceptions);

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
