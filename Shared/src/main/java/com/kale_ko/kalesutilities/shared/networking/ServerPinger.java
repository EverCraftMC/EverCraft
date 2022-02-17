package com.kale_ko.kalesutilities.shared.networking;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import com.google.gson.Gson;

public class ServerPinger {
    String hostname;
    Integer port;

    Integer timeout;

    public ServerPinger(String hostname, Integer port, Integer timeout) {
        this.hostname = hostname;
        this.port = port;
        this.timeout = timeout;
    }

    public int readVarInt(DataInputStream in) {
        int i = 0;
        int j = 0;

        while (true) {
            try {
                int k = in.readByte();
                i |= (k & 0x7F) << j++ * 7;

                if (j > 5) {
                    throw new RuntimeException("Int too large");
                } else if ((k & 0x80) != 128) {
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return i;
    }

    public void writeVarInt(DataOutputStream out, int paramInt) {
        while (true) {
            if ((paramInt & 0xFFFFFF80) == 0) {
                try {
                    out.writeByte(paramInt);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            }

            try {
                out.writeByte(paramInt & 0x7F | 0x80);

                paramInt >>>= 7;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ServerPing ping() {
        try {
            Socket socket = new Socket();
            socket.setSoTimeout(this.timeout);
            socket.connect(new InetSocketAddress(hostname, port), timeout * 1000);

            OutputStream outputStream = socket.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

            InputStream inputStream = socket.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream handshake = new DataOutputStream(b);
            handshake.writeByte(0x00);
            writeVarInt(handshake, 4);
            writeVarInt(handshake, new InetSocketAddress(hostname, port).getHostString().length());
            handshake.writeBytes(new InetSocketAddress(hostname, port).getHostString());
            handshake.writeShort(new InetSocketAddress(hostname, port).getPort());
            writeVarInt(handshake, 1);

            writeVarInt(dataOutputStream, b.size());
            dataOutputStream.write(b.toByteArray());

            dataOutputStream.writeByte(0x01);
            dataOutputStream.writeByte(0x00);
            DataInputStream dataInputStream = new DataInputStream(inputStream);
            int length = readVarInt(dataInputStream);

            byte[] in = new byte[length];
            dataInputStream.readFully(in);
            String json = new String(in).substring(3);

            long now = System.currentTimeMillis();
            dataOutputStream.writeByte(0x09);
            dataOutputStream.writeByte(0x01);
            dataOutputStream.writeLong(now);

            readVarInt(dataInputStream);
            long pingtime = dataInputStream.readLong();

            ServerPing response = new Gson().fromJson(json, ServerPing.class);
            response.setPing((int) (now - pingtime));

            dataOutputStream.close();
            outputStream.close();
            inputStreamReader.close();
            inputStream.close();
            socket.close();

            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}