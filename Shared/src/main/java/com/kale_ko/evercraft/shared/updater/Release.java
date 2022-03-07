package com.kale_ko.evercraft.shared.updater;

public class Release {
    public String id;
    public String tag_name;
    public String name;

    public String body;

    public Boolean draft;
    public Boolean prerelease;

    public Asset[] assets;

    public String url;
    public String assets_url;
    public String upload_url;
    public String html_url;

    public class Asset {
        public String id;
        public String name;

        public String state;
        public Long size;

        public String url;
        public String browser_download_url;
    }
}