package org.figrja.combo_auth_ahent;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.List;
import java.util.Objects;

import org.figrja.combo_auth.auth;
import org.figrja.combo_auth.config.debuglogger.LoggerMain;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.util.Printer;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceMethodVisitor;

import static org.objectweb.asm.Opcodes.ASM9;


public class Premain implements ClassFileTransformer {

    static LoggerMain LOGGER;

    static auth auth = new auth();
    public static void premain(String args, Instrumentation inst){
        System.out.println("Hello world!");


        inst.addTransformer(new Premain());

    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer){

        if (Objects.equals(className, "net/md_5/bungee/Bootstrap")){
            System.out.println("not support");
        }
         else if (Objects.equals(className, "net/minecraft/bundler/Main")) {
            System.out.println("hiii vanila!!");

        }
        else if (Objects.equals(className, "com/mojang/authlib/yggdrasil/YggdrasilMinecraftSessionService")) {
            try {
                auth.onInitializeServer();
                LOGGER = auth.Logger;
                ClassReader classReader = new ClassReader(classfileBuffer);
                ClassWriter classWriter = new ClassWriter(classReader,2);
                ClassVisitor classVisitor = new SWCV(ASM9, classWriter);
                printByteCode(classReader);
                classReader.accept(classVisitor, 0);
                LOGGER.debug("URA");
                byte[] _class = classWriter.toByteArray();
                classReader = new ClassReader(_class);
                printByteCode(classReader);
                return _class;
            }catch (Throwable a){

                a.printStackTrace();
            }

        }

        return classfileBuffer;
    }

    private void printByteCode(ClassReader classReader){
        ClassNode classNode = new ClassNode();
        classReader.accept(classNode, 0);
        printer = new Textifier();
        mp = new TraceMethodVisitor(printer);
        @SuppressWarnings("unchecked") final List<MethodNode> methods = classNode.methods;
        for (MethodNode m : methods) {
            InsnList inList = m.instructions;
            if (m.name.equals("hasJoinedServer")) {
                for (int i = 0; i < inList.size(); i++) {
                    LOGGER.debugRes(insnToString(inList.get(i)));
                }
            }
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





}