package model;

public class Destination {
    private final String name;
    private final String category;
    private final String address;
    private final String description;

    // 목록 출력용
    public Destination(String name, String category) {
        this.name = name;
        this.category = category;
        this.address = null;
        this.description = null;
    }

    // 상세 정보용
    public Destination(String name, String category, String address, String description) {
        this.name = name;
        this.category = category;
        this.address = address;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }
}
