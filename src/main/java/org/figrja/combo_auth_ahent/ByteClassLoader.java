package org.figrja.combo_auth_ahent;

import java.util.HashMap;
import java.util.Map;

public class ByteClassLoader extends ClassLoader {
    private final Map<String, byte[]> extraClassDefs;

    public ByteClassLoader( Map<String, byte[]> extraClassDefs) {
        super();
        this.extraClassDefs = extraClassDefs;
    }

    @Override
    protected Class<?> findClass(final String name) throws ClassNotFoundException {
        byte[] classBytes = this.extraClassDefs.remove(name);
        System.out.println(name);
        if (classBytes != null) {
            return defineClass(name, classBytes, 0, classBytes.length);
        }
        System.out.println("lox");
        return super.findClass(name);
    }

}

