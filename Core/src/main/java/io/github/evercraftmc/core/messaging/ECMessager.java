package io.github.evercraftmc.core.messaging;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import io.github.evercraftmc.core.ECPlugin;

public class ECMessager {
    protected final ECPlugin parent;

    protected InetSocketAddress address;
    protected boolean useSSL;

    protected String id;

    protected Thread connectionThread;
    protected Socket connection;

    public ECMessager(ECPlugin parent, InetSocketAddress address, boolean useSSL, String id) {
        this.parent = parent;

        this.address = address;
        this.useSSL = useSSL;

        this.id = id;
    }

    public void connect() throws IOException {
        this.connectionThread = new Thread(() -> {
            try {
                if (this.useSSL) {
                    this.connection = SSLSocketFactory.getDefault().createSocket(this.address.getAddress(), this.address.getPort());
                    ((SSLSocket) this.connection).setEnabledProtocols(new String[] { "TLSv1.1", "TLSv1.2", "TLSv1.3" });
                } else {
                    this.connection = SocketFactory.getDefault().createSocket(this.address.getAddress(), this.address.getPort());
                }

                DataInputStream inputStream = new DataInputStream(new BufferedInputStream(this.connection.getInputStream()));

                while (!this.connection.isClosed()) {
                    String id = inputStream.readUTF();
                    int size = inputStream.readInt();
                    byte[] buf = new byte[size];
                    inputStream.read(buf, 0, size);

                    
                }
            } catch (Exception e) {
                parent.getLogger().error("Error in messager", e);
            }
        }, "ECMessager");
    }
}