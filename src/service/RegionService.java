package service;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class RegionService {
    private final Scanner scanner;
    private final Map<String, List<Destination>> dataMap;

    // DB 연결 정보
    private static final String DB_URL = "jdbc:mysql://localhost:3306/ipp_pickgo?useSSL=false&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1234";

    public RegionService(Scanner scanner) {
        this.scanner = scanner;
        this.dataMap = loadDataFromDB();
    }

    public void run() {
        System.out.println("=====================================");
        System.out.println("       대전 여행지 추천 서비스       ");
        System.out.println("=====================================");

        if (dataMap.isEmpty()) {
            System.out.println("[오류] DB에서 여행지 데이터를 불러올 수 없습니다.");
            return;
        }

        String district = promptSelection("지역(구)을 선택하세요", new ArrayList<>(dataMap.keySet()));
        List<Destination> results = dataMap.getOrDefault(district, Collections.emptyList());

        System.out.println("\n------------------------------------------------");
        System.out.printf("[%s] 여행지 추천 결과\n", district);
        System.out.println("------------------------------------------------");

        if (results.isEmpty()) {
            System.out.println("조건에 맞는 여행지가 없습니다.");
        } else {
            printGroupedByCategory(results);
        }
    }

    // 사용자에게 번호 선택 입력 받기
    private String promptSelection(String prompt, List<String> options) {
        while (true) {
            System.out.println("\n" + prompt);
            for (int i = 0; i < options.size(); i++) {
                System.out.printf("   %2d. %s\n", i + 1, options.get(i));
            }

            System.out.print("입력 번호: ");
            try {
                int index = Integer.parseInt(scanner.nextLine().trim());
                if (index >= 1 && index <= options.size()) {
                    return options.get(index - 1);
                }
                System.out.println("[오류] 유효한 번호를 입력하세요.");
            } catch (NumberFormatException e) {
                System.out.println("[오류] 숫자를 입력해주세요.");
            }
        }
    }

    // 카테고리별로 묶어 출력
    private void printGroupedByCategory(List<Destination> destinations) {
        Set<String> uniqueCategories = destinations.stream()
                .map(d -> d.category)
                .collect(Collectors.toSet());

        List<String> specialCategories = Arrays.asList("맛집", "카페");

        for (String cat : specialCategories) {
            List<Destination> filtered = destinations.stream()
                    .filter(d -> cat.equals(d.category))
                    .collect(Collectors.toList());
            if (!filtered.isEmpty()) {
                System.out.println("\n* " + cat + " 추천 목록");
                System.out.println("------------------------------------------------");
                printDestinations(filtered);
            }
        }

        List<Destination> others = destinations.stream()
                .filter(d -> !specialCategories.contains(d.category))
                .collect(Collectors.toList());

        if (!others.isEmpty()) {
            System.out.println("\n* 놀거리 추천 목록");
            System.out.println("------------------------------------------------");
            printDestinations(others);
        }
    }

    private void printDestinations(List<Destination> list) {
        for (int i = 0; i < list.size(); i++) {
            System.out.printf("%-20s", "- " + list.get(i).name);
            if ((i + 1) % 3 == 0 || i == list.size() - 1) {
                System.out.println();
            }
        }
    }

    // DB에서 데이터 불러오기
    private Map<String, List<Destination>> loadDataFromDB() {
        Map<String, List<Destination>> data = new LinkedHashMap<>();

        String query = """
            SELECT d.name AS district, p.name AS place_name, p.category
            FROM place p
            LEFT JOIN district d ON p.district_id = d.id
            ORDER BY d.name, p.name
            """;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement pstmt = conn.prepareStatement(query);
                 ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    String district = Optional.ofNullable(rs.getString("district")).orElse("알 수 없음");
                    String name = rs.getString("place_name");
                    String category = Optional.ofNullable(rs.getString("category")).orElse("기타");

                    if (name == null) continue;

                    data.computeIfAbsent(district, k -> new ArrayList<>())
                        .add(new Destination(name, category));
                }
            }
        } catch (Exception e) {
            System.out.println("[DB 오류] 데이터 로드 실패: " + e.getMessage());
        }

        return data;
    }

    public static void main(String[] args) {
        new RegionService(new Scanner(System.in)).run();
    }

    // 장소 정보 클래스
    private static class Destination {
        final String name;
        final String category;

        Destination(String name, String category) {
            this.name = name;
            this.category = category;
        }
    }
}
