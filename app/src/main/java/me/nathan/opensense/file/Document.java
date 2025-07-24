package me.nathan.opensense.file;

public class Document {

    private final String name;
    private final String contents;
    private int textLength;

    public Document(String name, String contents) {
        this.name = name;
        this.contents = contents;
    }

    public String getContents() {
        return contents;
    }

    public String getName() {
        return name;
    }
}
