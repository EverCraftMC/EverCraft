package com.kale_ko.kalesutilities.shared.networking;

public class ServerMotdComponent {
    private String text;
    private String color;

    private Boolean bold;
    private Boolean italic;
    private Boolean underline;

    public String getText() {
        return this.text;
    }

    public String getColor() {
        return this.color;
    }

    public Boolean getBold() {
        return this.bold;
    }

    public Boolean getItalic() {
        return this.italic;
    }

    public Boolean getUnderline() {
        return this.underline;
    }
}