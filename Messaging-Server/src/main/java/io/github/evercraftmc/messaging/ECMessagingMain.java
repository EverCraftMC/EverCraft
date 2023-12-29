package io.github.evercraftmc.messaging;

import io.github.kale_ko.bjsl.parsers.YamlParser;
import io.github.kale_ko.ejcl.file.bjsl.StructuredYamlFileConfig;
import java.net.InetSocketAddress;
import java.nio.file.Path;
import org.jetbrains.annotations.NotNull;

public class ECMessagingMain {
    private static class MessagingDetails {
        public String host = "127.0.0.1";
        public int port = 3000;
    }

    public static void main(String @NotNull [] args) {
        try {
            System.out.println("Loading config");

            StructuredYamlFileConfig<MessagingDetails> messagingDetails = new StructuredYamlFileConfig<>(MessagingDetails.class, Path.of("messaging.yml").toFile(), new YamlParser.Builder().build());
            messagingDetails.load(true);

            ECMessagingServer server = new ECMessagingServer(new InetSocketAddress(messagingDetails.get().host, messagingDetails.get().port));
            server.start();

            while (true) {
                int read = System.in.read();
                if (read == -1) {
                    Thread.sleep(100);
                    continue;
                }

                if (read == 'q') {
                    server.stop();
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}