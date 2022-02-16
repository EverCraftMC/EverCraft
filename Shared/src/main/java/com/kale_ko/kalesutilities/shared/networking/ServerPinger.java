package com.kale_ko.kalesutilities.shared.networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ServerPinger {
    String hostname;
    Integer port;

    Integer timeout;

    ServerPing response;

    public ServerPinger(String hostname, Integer port, Integer timeout) {
        this.hostname = hostname;
        this.port = port;
        this.timeout = timeout;
    }

    public ServerPing ping() {
        try {
            Socket socket = new Socket();
            socket.setTcpNoDelay(true);
            socket.connect(new InetSocketAddress(hostname, port), timeout * 1000);
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            dos.write(0xFE);

            StringBuilder rawdata = new StringBuilder();
            int b;
            while ((b = dis.read()) != -1) {
                rawdata.append((char) b);
            }

            socket.close();

            String[] data = rawdata.substring(3).split("§");
            String motd = data[0];
            Integer onlinePlayers = Integer.parseInt(data[1].replaceAll("[^0-9]", ""));
            Integer maxPlayers = Integer.parseInt(data[2].replaceAll("[^0-9]", ""));

            return new ServerPing(motd, onlinePlayers, maxPlayers);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}