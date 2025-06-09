package service;

import dao.DestinationDAO;
import model.Destination;
import util.ConsolePrinter;

import java.sql.*;
import java.util.*;

public class RegionService implements SearchService {
    private final Scanner scanner;
    private final Map<String, List<Destination>> dataMap;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/ipp_pickgo?useSSL=false&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "eun1224!!";

    public RegionService(Scanner scanner) {
        this.scanner = scanner;
        this.dataMap = loadDataFromDB();
    }

    @Override
    public void run() {
        ConsolePrinter.printRegionIntro();

        if (dataMap.isEmpty()) {
            System.out.println("[오류] DB에서 여행지 데이터를 불러올 수 없습니다.");
            return;
        }

        while (true) {
            String district = promptSelection("지역(구)을 선택하세요", new ArrayList<>(dataMap.keySet()));
            List<Destination> results = dataMap.getOrDefault(district, Collections.emptyList());

            ConsolePrinter.printRegionResults(district, results);

            // 🔽 상세 정보 루프
            while (true) {
                System.out.print("\n📄 특정 장소의 상세 정보를 확인하시겠습니까? (y/n): ");
                String wantDetail = scanner.nextLine().trim().toLowerCase();
                if (wantDetail.equals("y")) {
                    while (true) {
                        System.out.print("👉 상세 정보를 볼 장소명을 입력하세요: ");
                        String placeName = scanner.nextLine().trim();
                        Destination detail = DestinationDAO.getPlaceDetailByName(placeName);

                        if (detail != null) {
                            ConsolePrinter.printPlaceDetail(detail);
                            break;
                        } else {
                            System.out.println("❌ 해당 장소의 정보를 찾을 수 없습니다.");
                            System.out.print("🔁 다시 입력하시겠습니까? (y/n): ");
                            String again = scanner.nextLine().trim().toLowerCase();
                            if (!again.equals("y")) break;
                        }
                    }
                    break;
                } else if (wantDetail.equals("n")) {
                    break;
                } else {
                    System.out.println("❌ 잘못된 입력입니다. y 또는 n으로 입력해주세요.");
                }
            }

            // 🔁 다시 검색 여부
            while (true) {
                System.out.print("\n🔁 다시 지역 추천을 검색하시겠습니까? (y/n): ");
                String retry = scanner.nextLine().trim().toLowerCase();

                if (retry.equals("y")) {
                    break;
                } else if (retry.equals("n")) {
                    return;
                } else {
                    System.out.println("❌ 잘못된 입력입니다. y 또는 n으로 입력해주세요.");
                }
            }
        }
    }

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
}
