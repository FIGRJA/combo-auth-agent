package org.figrja.combo_auth_ahent;

import java.net.URL;
import java.net.URLClassLoader;

public class URLLoader extends URLClassLoader {


    public URLLoader(URL[] urls, ClassLoader parent) {
        super(urls,parent);
    }

    public Class<?> findClass(String name) throws ClassNotFoundException {
        return super.findClass(name);
    }
}
