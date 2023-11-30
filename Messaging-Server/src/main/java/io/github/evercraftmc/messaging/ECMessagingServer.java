package io.github.evercraftmc.messaging;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.net.ServerSocketFactory;

public class ECMessagingServer {
    protected static class Connection {
        protected Socket socket;

        protected DataInputStream inputStream;
        protected DataOutputStream outputStream;

        public Connection(Socket socket) throws IOException {
            this.socket = socket;

            this.inputStream = new DataInputStream(socket.getInputStream());
            this.outputStream = new DataOutputStream(socket.getOutputStream());
        }

        public Socket getSocket() {
            return this.socket;
        }

        public DataInputStream getInputStream() {
            return this.inputStream;
        }

        public DataOutputStream getOutputStream() {
            return this.outputStream;
        }

        public void close() throws IOException {
            this.socket.shutdownInput();
            this.socket.shutdownOutput();
            this.socket.close();
        }
    }

    protected InetSocketAddress address;

    protected Thread socketThread;
    protected ServerSocket socket = null;
    protected boolean open = false;

    protected ExecutorService executor = null;

    protected List<Connection> connections = new ArrayList<>();

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
                    Socket client = this.socket.accept();

                    client.setTcpNoDelay(true);
                    client.setKeepAlive(true);
                    client.setSoTimeout(3000);

                    Connection connection;
                    synchronized (WRITE_LOCK) {
                        connection = new Connection(client);
                        connections.add(connection);
                    }

                    this.executor.submit(() -> {
                        try {
                            while (this.open && !connection.socket.isClosed() && connection.socket.isConnected()) {
                                try {
                                    String sender = connection.getInputStream().readUTF();
                                    String recipient = connection.getInputStream().readUTF();

                                    int size = connection.getInputStream().readInt();
                                    byte[] buf = new byte[size];
                                    int read = connection.getInputStream().read(buf, 0, size);
                                    if (size != read) {
                                        throw new IOException("Mismatched read");
                                    }

                                    synchronized (WRITE_LOCK) {
                                        for (Connection connection2 : this.connections) {
                                            if (connection == connection2) {
                                                continue;
                                            }

                                            try {
                                                connection2.getOutputStream().writeUTF(sender);
                                                connection2.getOutputStream().writeUTF(recipient);

                                                connection2.getOutputStream().writeInt(size);
                                                connection2.getOutputStream().write(buf, 0, size);
                                            } catch (IOException e) {
                                                connection2.close();
                                                connections.remove(connection2);
                                            }
                                        }
                                    }
                                } catch (SocketTimeoutException ignored) {
                                } catch (EOFException e) {
                                    connection.close();
                                    synchronized (WRITE_LOCK) {
                                        connections.remove(connection);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();

                                    connection.close();
                                    synchronized (WRITE_LOCK) {
                                        connections.remove(connection);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            } catch (SocketException e) {
                if (!e.getMessage().equalsIgnoreCase("Socket closed")) {
                    e.printStackTrace();
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