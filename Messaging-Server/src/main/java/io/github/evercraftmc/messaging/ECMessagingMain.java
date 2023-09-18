package io.github.evercraftmc.messaging;

import io.github.kale_ko.bjsl.parsers.YamlParser;
import io.github.kale_ko.ejcl.file.bjsl.YamlFileConfig;
import java.net.InetSocketAddress;
import java.nio.file.Path;
import sun.misc.Signal;
import sun.misc.SignalHandler;

public class ECMessagingMain {
    private static class MessagingDetails {
        public String host = "127.0.0.1";
        public int port = 3000;
    }

    public static void main(String[] args) {
        try {
            System.out.println("Loading config");

            YamlFileConfig<MessagingDetails> messagingDetails = new YamlFileConfig<>(MessagingDetails.class, Path.of("messaging.yml").toFile(), new YamlParser.Builder().build());
            messagingDetails.load(true);

            System.out.println("Starting Messaging server");

            ECMessagingServer server = new ECMessagingServer(new InetSocketAddress(messagingDetails.get().host, messagingDetails.get().port));
            server.start();

            messagingDetails.close();

            Signal[] signals = new Signal[] { new Signal("TERM"), new Signal("INT"), new Signal("ABRT") };
            SignalHandler sigHandler = signal -> {
                System.out.println("Shutting down");

                server.stop();
            };

            for (Signal signal : signals) {
                Signal.handle(signal, sigHandler);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}