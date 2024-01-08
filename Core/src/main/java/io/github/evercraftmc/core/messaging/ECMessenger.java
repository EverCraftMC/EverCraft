package io.github.evercraftmc.core.messaging;

import io.github.evercraftmc.core.ECPlugin;
import io.github.evercraftmc.core.api.events.messaging.MessageEvent;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.UUID;
import javax.net.SocketFactory;
import org.jetbrains.annotations.NotNull;

public class ECMessenger {
    protected final @NotNull ECPlugin parent;

    protected final @NotNull InetSocketAddress address;

    protected final @NotNull UUID id;

    protected boolean open;

    protected Thread connectionThread;
    protected Socket connection;

    protected DataInputStream inputStream;
    protected DataOutputStream outputStream;

    protected final @NotNull Object READ_LOCK = new Object();
    protected final @NotNull Object WRITE_LOCK = new Object();

    public ECMessenger(@NotNull ECPlugin parent, @NotNull InetSocketAddress address, @NotNull UUID id) {
        this.parent = parent;

        this.address = address;

        this.id = id;
    }

    public void connect() {
        this.open = true;

        this.connectionThread = new Thread(() -> {
            try {
                while (this.open) {
                    this.connection = SocketFactory.getDefault().createSocket(this.address.getAddress(), this.address.getPort());

                    this.inputStream = new DataInputStream(this.connection.getInputStream());
                    this.outputStream = new DataOutputStream(this.connection.getOutputStream());

                    ByteArrayOutputStream helloMessageData = new ByteArrayOutputStream();
                    DataOutputStream helloMessage = new DataOutputStream(helloMessageData);
                    helloMessage.writeInt(ECMessageType.HELLO);
                    helloMessage.writeUTF(this.id.toString());
                    writeMessage(new ECMessage(ECSender.fromServer(this.id), ECRecipient.fromAll(), helloMessageData.toByteArray()));
                    helloMessage.close();

                    while (this.open && !this.connection.isClosed() && this.connection.isConnected()) {
                        try {
                            ECMessage message = readMessage();

                            this.parent.getServer().getEventManager().emit(new MessageEvent(this, message));
                        } catch (SocketTimeoutException ignored) {
                        } catch (EOFException e) {
                            break;
                        } catch (IOException e) {
                            parent.getLogger().error("[Messenger] Error reading message", e);

                            break;
                        }
                    }

                    this.connection.shutdownInput();
                    this.connection.shutdownOutput();
                    this.connection.close();

                    if (this.open) {
                        parent.getLogger().warn("[Messenger] Reconnecting");
                    }
                }
            } catch (Exception e) {
                parent.getLogger().error("[Messenger] Error", e);
            }
        }, "ECMessenger");
        this.connectionThread.start();
    }

    public void close() {
        this.open = false;
    }

    public void send(@NotNull ECRecipient recipient, byte @NotNull [] data) {
        this.send(recipient, data, data.length);
    }

    public void send(@NotNull ECRecipient recipient, byte[] data, int size) {
        try {
            ECMessage message = new ECMessage(ECSender.fromServer(this.id), recipient, data, size);
            this.writeMessage(message);
        } catch (IOException e) {
            parent.getLogger().error("[Messenger] Error writing message", e);
        }
    }

    protected @NotNull ECMessage readMessage() throws IOException {
        synchronized (READ_LOCK) {
            String sender = this.inputStream.readUTF();
            String recipient = this.inputStream.readUTF();

            int size = this.inputStream.readInt();
            byte[] data = new byte[size];
            int read = this.inputStream.read(data, 0, size);
            if (size != read) {
                throw new IOException("Mismatched read");
            }

            return new ECMessage(ECSender.parse(sender), ECRecipient.parse(recipient), data, size);
        }
    }

    protected void writeMessage(@NotNull ECMessage message) throws IOException {
        synchronized (WRITE_LOCK) {
            this.outputStream.writeUTF(message.getSender().toString());
            this.outputStream.writeUTF(message.getRecipient().toString());

            this.outputStream.writeInt(message.getSize());
            this.outputStream.write(message.getData(), 0, message.getSize());
        }
    }
}