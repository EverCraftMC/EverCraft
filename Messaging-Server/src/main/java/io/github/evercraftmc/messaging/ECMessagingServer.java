package io.github.evercraftmc.messaging;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.net.ServerSocketFactory;

public class ECMessagingServer {
    protected InetSocketAddress address;

    protected Thread socketThread;
    protected ServerSocket socket = null;
    protected boolean open = false;

    protected ExecutorService executor = null;

    protected List<Socket> connections = new ArrayList<Socket>();

    protected final Object WRITE_LOCK = new Object();

    public ECMessagingServer(InetSocketAddress address) {
        this.address = address;
    }

    public void start() {
        this.open = true;

        this.executor = Executors.newCachedThreadPool();

        this.socketThread = new Thread(() -> {
            try {
                this.socket = ServerSocketFactory.getDefault().createServerSocket(this.address.getPort(), -1, this.address.getAddress());

                while (this.open) {
                    Socket connection = this.socket.accept();

                    connection.setTcpNoDelay(true);
                    connection.setKeepAlive(true);
                    connection.setSoTimeout(1000);

                    synchronized (WRITE_LOCK) {
                        connections.add(connection);
                    }

                    this.executor.submit(() -> {
                        try {
                            DataInputStream inputStream = new DataInputStream(new BufferedInputStream(connection.getInputStream()));

                            while (this.open) {
                                try {
                                    String id = inputStream.readUTF();

                                    int size = inputStream.readInt();
                                    byte[] buf = new byte[size];
                                    inputStream.read(buf, 0, size);

                                    synchronized (WRITE_LOCK) {
                                        for (Socket connection2 : this.connections) {
                                            if (connection == connection2) {
                                                continue;
                                            }

                                            DataOutputStream outputStream = new DataOutputStream(new BufferedOutputStream(connection2.getOutputStream()));

                                            outputStream.writeUTF(id);

                                            outputStream.writeInt(size);
                                            outputStream.write(buf, 0, size);
                                        }
                                    }
                                } catch (SocketTimeoutException e) {
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "ECMessagingServer[" + this.address.toString() + "]");
        this.socketThread.start();
    }

    public void stop() {
        try {
            this.open = false;
            this.executor.shutdown();
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isOpen() {
        return this.open;
    }
}