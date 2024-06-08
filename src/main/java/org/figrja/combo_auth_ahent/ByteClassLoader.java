package org.figrja.combo_auth_ahent;

import java.util.Map;

public class ByteClassLoader extends ClassLoader {
    private final Map<String, byte[]> extraClassDefs;

    public ByteClassLoader( Map<String, byte[]> extraClassDefs) {
        super();
        this.extraClassDefs = extraClassDefs;
    }

    @Override
    protected Class<?> findClass(final String name) throws ClassNotFoundException {
        byte[] classBytes = this.extraClassDefs.get(name);
        if (classBytes != null) {
            return defineClass(name, classBytes, 0, classBytes.length);
        }
        System.out.println("lox "+name);
        return super.findClass(name);
    }

}


