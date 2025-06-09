package model;

public class Destination {
    private final String name;
    private final String category;

    public Destination(String name, String category) {
        this.name = name;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }
}
