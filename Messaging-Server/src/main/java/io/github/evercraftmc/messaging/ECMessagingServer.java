package io.github.evercraftmc.messaging;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ECMessagingServer {
    protected InetSocketAddress address;

    protected Thread socketThread;
    protected ServerSocket socket = null;
    protected boolean open = false;

    protected ExecutorService executor = null;

    protected List<Socket> connections = new ArrayList<Socket>();

    public ECMessagingServer(InetSocketAddress address) {
        this.address = address;
    }

    public void start() {
        this.open = true;

        this.executor = Executors.newCachedThreadPool();

        this.socketThread = new Thread(() -> {
            try {
                this.socket = new ServerSocket(this.address.getPort(), -1, this.address.getAddress());

                while (this.open) {
                    Socket connection = this.socket.accept();

                    connection.setTcpNoDelay(true);
                    connection.setKeepAlive(true);

                    connections.add(connection);;

                    this.executor.submit(() -> {
                        try {
                            InputStream inputStream = connection.getInputStream();

                            byte[] buf = new byte[2048];
                            int read;
                            while ((read = inputStream.read(buf)) != -1) {
                                for (Socket connection2 : this.connections) {
                                    connection2.getOutputStream().write(buf, 0, read);
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