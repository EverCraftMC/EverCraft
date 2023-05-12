package io.github.evercraftmc.core;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;

public class ECModuleClassLoader extends ClassLoader {
    protected final ClassLoader parent;

    protected File file;

    public ECModuleClassLoader(ClassLoader parent, File file) {
        this.parent = parent;

        this.file = file;
    }

    public final ClassLoader getParentClassLoader() {
        return this.parent;
    }

    public File getFile() {
        return this.file;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            try {
                return this.parent.loadClass(name);
            } catch (ClassNotFoundException e) {
                byte[] data = this.loadClassData(name);

                return this.defineClass(name, data, 0, data.length);
            }
        } catch (IOException e) {
            throw new ClassNotFoundException(name, e);
        }
    }

    protected byte[] loadClassData(String name) throws ClassNotFoundException, IOException {
        JarInputStream jar = new JarInputStream(new BufferedInputStream(new FileInputStream(this.file)));
        ZipEntry entry;
        while ((entry = jar.getNextEntry()) != null) {
            if (entry.getName().equals(name.replace(".", "/") + ".class")) {
                ByteBuffer data = ByteBuffer.allocate((int) entry.getSize());

                byte[] buf = new byte[2048];
                int read;
                while ((read = jar.read(buf)) != -1) {
                    data.put(buf, 0, read);
                }

                jar.close();

                return data.array();
            }
        }
        jar.close();

        throw new ClassNotFoundException(name);
    }
}