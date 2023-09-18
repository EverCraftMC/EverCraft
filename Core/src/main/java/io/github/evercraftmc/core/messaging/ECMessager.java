package io.github.evercraftmc.core.messaging;

import io.github.evercraftmc.core.ECPlugin;
import io.github.evercraftmc.core.api.events.messaging.MessageEvent;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.UUID;
import javax.net.SocketFactory;

public class ECMessager {
    protected final ECPlugin parent;

    protected InetSocketAddress address;

    protected UUID id;

    protected Thread connectionThread;
    protected Socket connection;

    protected DataInputStream inputStream;
    protected DataOutputStream outputStream;

    protected final Object READ_LOCK = new Object();
    protected final Object WRITE_LOCK = new Object();

    public ECMessager(ECPlugin parent, InetSocketAddress address, UUID id) {
        this.parent = parent;

        this.address = address;

        this.id = id;
    }

    public void connect() {
        this.connectionThread = new Thread(() -> {
            try {
                this.connection = SocketFactory.getDefault().createSocket(this.address.getAddress(), this.address.getPort());

                this.inputStream = new DataInputStream(this.connection.getInputStream());
                this.outputStream = new DataOutputStream(this.connection.getOutputStream());

                ByteArrayOutputStream helloMessageData = new ByteArrayOutputStream();
                DataOutputStream helloMessage = new DataOutputStream(helloMessageData);
                helloMessage.writeInt(ECMessageType.HELLO);
                helloMessage.writeUTF(this.id.toString());
                writeMessage(new ECMessage(ECSender.fromServer(this.id), ECRecipient.fromAll(), helloMessageData.toByteArray()));
                helloMessage.close();

                while (!this.connection.isClosed() && this.connection.isConnected()) {
                    try {
                        ECMessage message = readMessage();

                        this.parent.getServer().getEventManager().emit(new MessageEvent(this, message));
                    } catch (SocketTimeoutException ignored) {
                    } catch (EOFException e) {
                        parent.getLogger().error("[Messager] Error reading message", e);

                        break;
                    } catch (IOException e) {
                        parent.getLogger().warn("[Messager] Got disconnected from server", e);

                        break;
                    }
                }

                this.connection.shutdownInput();
                this.connection.shutdownOutput();
                this.connection.close();
            } catch (Exception e) {
                parent.getLogger().error("[Messager] Error", e);
            }
        }, "ECMessager");
        this.connectionThread.start();
    }

    public void send(ECRecipient recipient, byte[] data) {
        this.send(recipient, data, data.length);
    }

    public void send(ECRecipient recipient, byte[] data, int size) {
        try {
            ECMessage message = new ECMessage(ECSender.fromServer(this.id), recipient, data, size);
            this.writeMessage(message);
        } catch (IOException e) {
            parent.getLogger().error("[Messager] Error writing message", e);
        }
    }

    protected ECMessage readMessage() throws IOException {
        synchronized (READ_LOCK) {
            String sender = this.inputStream.readUTF();
            String recipient = this.inputStream.readUTF();

            int size = this.inputStream.readInt();
            byte[] data = new byte[size];
            int read = this.inputStream.read(data, 0, size);
            if (read != size) {
                parent.getLogger().error("[Messager] Error reading message");
            }

            return new ECMessage(ECSender.parse(sender), ECRecipient.parse(recipient), data, size);
        }
    }

    protected void writeMessage(ECMessage message) throws IOException {
        synchronized (WRITE_LOCK) {
            this.outputStream.writeUTF(message.getSender().toString());
            this.outputStream.writeUTF(message.getRecipient().toString());

            this.outputStream.writeInt(message.getSize());
            this.outputStream.write(message.getData(), 0, message.getSize());
        }
    }
}