package io.github.evercraftmc.core;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ECModuleClassLoader extends ClassLoader {
    protected final @NotNull ClassLoader parent;

    protected @NotNull File file;

    public ECModuleClassLoader(@NotNull ClassLoader parent, @NotNull File file) {
        this.parent = parent;

        this.file = file;
    }

    public final @NotNull ClassLoader getParentClassLoader() {
        return this.parent;
    }

    public @NotNull File getFile() {
        return this.file;
    }

    @Override
    protected @Nullable Class<?> findClass(@NotNull String name) throws ClassNotFoundException {
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

    protected byte[] loadClassData(@NotNull String name) throws ClassNotFoundException, IOException {
        try (JarInputStream jar = new JarInputStream(new BufferedInputStream(new FileInputStream(this.file)))) {
            ZipEntry entry;
            while ((entry = jar.getNextEntry()) != null) {
                if (entry.getName().equals(name.replace(".", "/") + ".class")) {
                    ByteBuffer data = ByteBuffer.allocate((int) entry.getSize());

                    byte[] buf = new byte[2048];
                    int read;
                    while ((read = jar.read(buf)) != -1) {
                        data.put(buf, 0, read);
                    }

                    return data.array();
                }
            }
        }

        throw new ClassNotFoundException(name);
    }
}