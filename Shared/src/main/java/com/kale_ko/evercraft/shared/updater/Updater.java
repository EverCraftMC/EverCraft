package com.kale_ko.evercraft.shared.updater;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Paths;

import com.google.gson.Gson;

public class Updater {
    private String githubRepo;
    private String token;

    private String dirrectory;

    public Updater(String githubRepo, String token, String dirrectory) {
        this.githubRepo = githubRepo;
        this.token = token;

        this.dirrectory = dirrectory;
    }

    public boolean check(String currentVersion) {
        return isNewer(new Gson().fromJson(fetchUrl("https://api.github.com/repos/" + githubRepo + "/releases"), Release[].class)[0].tag_name, currentVersion);
    }

    public File update() {
        Release.Asset latestAsset = new Gson().fromJson(fetchUrl("https://api.github.com/repos/" + githubRepo + "/releases"), Release[].class)[0].assets[0];

        try {
            URL endpoint = new URL(latestAsset.browser_download_url);
            HttpURLConnection con = (HttpURLConnection) endpoint.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("User-Agent", "Bot evercraft-server");
            con.setRequestProperty("Authorization", "token " + token);
            con.setConnectTimeout(3000);
            con.setReadTimeout(5000);

            InputStream in = con.getInputStream();
            File file = Paths.get(dirrectory, latestAsset.name).toFile();
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
            byte[] buffer = new byte[1024];
            int numRead;
            while ((numRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, numRead);
            }
            in.close();
            out.close();
            con.disconnect();

            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String fetchUrl(String url) {
        try {
            URL endpoint = new URL(url);
            HttpURLConnection con = (HttpURLConnection) endpoint.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("User-Agent", "Bot evercraft-server");
            con.setRequestProperty("Authorization", "token " + token);
            con.setConnectTimeout(3000);
            con.setReadTimeout(5000);

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();

            return content.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean isNewer(String version1, String version2) {
        try {
            String[] version1list = version1.split("\\.");
            String[] version2list = version2.split("\\.");

            Integer[] version1dots = new Integer[version1list.length];
            Integer[] version2dots = new Integer[version2list.length];

            for (int index = 0; index < version1list.length; index++) {
                version1dots[index] = Integer.parseInt(version1list[index]);
            }

            for (int index = 0; index < version2list.length; index++) {
                version2dots[index] = Integer.parseInt(version2list[index]);
            }

            for (int index = 0; index < Math.min(version1dots.length, version2dots.length); index++) {
                if (version1dots[index] > version2dots[index]) {
                    return true;
                } else if (version2dots[index] > version1dots[index]) {
                    return false;
                }
            }

            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}