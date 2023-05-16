package io.github.evercraftmc.messaging;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.file.Path;
import io.github.kale_ko.bjsl.parsers.YamlParser;
import io.github.kale_ko.ejcl.file.bjsl.YamlFileConfig;

public class ECMessagingMain {
    private class MessagingDetails {
        public InetAddress host;
        public int port;

        public boolean useSSL = true;
    }

    public static void main(String[] args) {
        try {
            YamlFileConfig<MessagingDetails> messagingDetails = new YamlFileConfig<MessagingDetails>(MessagingDetails.class, Path.of("messaging.yml").toFile(), new YamlParser.Builder().build());
            messagingDetails.load(true);

            ECMessagingServer server = new ECMessagingServer(new InetSocketAddress(messagingDetails.get().host, messagingDetails.get().port), messagingDetails.get().useSSL);
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}