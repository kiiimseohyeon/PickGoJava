package pick_go;

import java.sql.*;
import java.util.*;

public class HashtagServiceOutput {

    static class Destination {
        int id;
        String name;
        String category;
        String description;
        String district;
        List<String> ageGroups;
        List<String> companionTypes;

        public Destination(int id, String name, String category, String description, String district,
                           List<String> ageGroups, List<String> companionTypes) {
            this.id = id;
            this.name = name;
            this.category = category;
            this.description = description;
            this.district = district;
            this.ageGroups = ageGroups;
            this.companionTypes = companionTypes;
        }
    }

    private static List<Destination> initializeSampleData() {
        return List.of(
            new Destination(1, "한밭수목원", "볼거리", "자연을 느낄 수 있는 수목원", "서구",
                    List.of("20대", "30대", "40대", "50대 이상"), List.of("가족", "친구")),
            new Destination(2, "성심당", "맛집", "대전 대표 빵집", "중구",
                    List.of("10대", "20대", "30대"), List.of("친구", "연인")),
            new Destination(3, "유성온천", "볼거리", "따뜻한 온천욕", "유성구",
                    List.of("40대", "50대 이상"), List.of("가족")),
            new Destination(4, "카페베네", "카페", "뷰가 좋은 카페", "서구",
                    List.of("20대", "30대"), List.of("연인")),
            new Destination(5, "대청호 오백리길", "볼거리", "자연 경관을 즐길 수 있는 산책로", "대덕구",
                    List.of("30대", "40대", "50대 이상"), List.of("가족", "친구")),
            new Destination(6, "도룡동 카페거리", "카페", "감성적인 분위기의 카페 거리", "유성구",
                    List.of("20대", "30대"), List.of("연인", "친구")),
            new Destination(7, "서구 맛집식당", "맛집", "서구의 인기 맛집", "서구",
                    List.of("10대", "20대", "30대"), List.of("가족", "친구"))
        );
    }

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/ipp_pickgo?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8";
        String user = "root";
        String password = "비밀번호";

        List<Destination> sampleData = initializeSampleData();

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("검색할 지역명 또는 태그를 입력하세요(프로그램 종료: exit) : ");
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("exit")) {
                    System.out.println("프로그램을 종료합니다.");
                    break;
                }

                String[] keywords = input.split("\\s+");
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection conn = DriverManager.getConnection(url, user, password);

                String districtSql = "SELECT DISTINCT p.name, p.description " +
                                     "FROM place p " +
                                     "JOIN district d ON p.district_id = d.id " +
                                     "WHERE d.name = ?";

                String tagSql = "SELECT DISTINCT p.name, p.description " +
                                "FROM place p " +
                                "JOIN place_tag pt ON p.id = pt.place_id " +
                                "JOIN tag t ON pt.tag_id = t.id " +
                                "WHERE t.name = ?";

                PreparedStatement districtStmt = conn.prepareStatement(districtSql);
                PreparedStatement tagStmt = conn.prepareStatement(tagSql);

                Set<String> printedPlaces = new HashSet<>();
                boolean anyResult = false;

                for (String keyword : keywords) {
                    keyword = keyword.trim();
                    boolean hasResult = false;

                    districtStmt.setString(1, keyword);
                    ResultSet rs1 = districtStmt.executeQuery();
                    while (rs1.next()) {
                        String name = rs1.getString("name");
                        String description = rs1.getString("description");
                        String key = name + description;
                        if (printedPlaces.add(key)) {
                            hasResult = true;
                            anyResult = true;
                            System.out.println("=================");
                            System.out.println(name);
                            System.out.println("ㄴ" + description);
                            System.out.println("=================");
                        }
                    }
                    rs1.close();

                    tagStmt.setString(1, keyword);
                    ResultSet rs2 = tagStmt.executeQuery();
                    while (rs2.next()) {
                        String name = rs2.getString("name");
                        String description = rs2.getString("description");
                        String key = name + description;
                        if (printedPlaces.add(key)) {
                            hasResult = true;
                            anyResult = true;
                            System.out.println("=================");
                            System.out.println(name);
                            System.out.println("ㄴ" + description);
                            System.out.println("=================");
                        }
                    }
                    rs2.close();

                    for (Destination d : sampleData) {
                        if (d.name.contains(keyword) || d.description.contains(keyword) ||
                            d.district.equals(keyword) || d.category.equals(keyword) ||
                            d.ageGroups.contains(keyword) || d.companionTypes.contains(keyword)) {

                            String key = d.name + d.description;
                            if (printedPlaces.add(key)) {
                                hasResult = true;
                                anyResult = true;
                                System.out.println("=================");
                                System.out.println(d.name);
                                System.out.println("ㄴ" + d.description);
                                System.out.println("=================");
                            }
                        }
                    }

                    if (!hasResult) {
                        System.out.println("=================");
                        System.out.println(keyword);
                        System.out.println("ㄴ관련 정보가 없습니다.");
                        System.out.println("=================");
                    }
                }

                districtStmt.close();
                tagStmt.close();
                conn.close();

                if (!anyResult) {
                    System.out.println("다시 검색하시겠습니까? (Y/N): ");
                    String retry = scanner.nextLine();
                    if (!retry.equalsIgnoreCase("Y")) {
                        System.out.println("프로그램을 종료합니다.");
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
