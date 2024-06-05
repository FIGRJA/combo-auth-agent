package org.figrja.combo_auth_ahent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;


public class Premain implements ClassFileTransformer {



    public static void premain(String args, Instrumentation inst) {




        inst.addTransformer(new ClassTransformer(inst));



    }







}