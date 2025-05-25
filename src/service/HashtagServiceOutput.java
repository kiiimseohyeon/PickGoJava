package pick_go;

import java.sql.*;
import java.util.*;

public class HashtagServiceOutput {

    /**
     * 목적지 정보를 담는 내부 클래스
     */
    static class Destination {
        int id;                      // 장소 고유 ID
        String name;                // 장소 이름
        String category;            // 장소 카테고리 (예: 볼거리, 맛집)
        String description;         // 장소 설명
        String district;            // 지역구 이름 (예: 서구, 유성구 등)
        List<String> ageGroups;     // 추천 연령대 목록
        List<String> companionTypes;// 추천 동행 유형 목록 (예: 가족, 친구)

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

    /**
     * 샘플 데이터를 초기화하는 함수
     * 실제 DB 외에 보조적으로 사용할 수 있는 테스트용 데이터
     */
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

    /*메인 실행 메서드: 사용자로부터 키워드를 입력받아 검색을 수행*/
    public static void main(String[] args) {
        // DB 연결 정보
        String url = "jdbc:mysql://localhost:3306/ipp_pickgo?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8";
        String user = "root";
        String password = "jang1107"; //자기가 설정한 비밀번호

        // 샘플 데이터 초기화
        List<Destination> sampleData = initializeSampleData();

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("검색할 지역명, 태그 또는 해시태그를 입력하세요 (종료: exit) : ");
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("exit")) {
                    System.out.println("프로그램을 종료합니다.");
                    break;
                }

                // 입력된 키워드들을 공백 기준으로 분리
                String[] keywords = input.split("\\s+");

                // JDBC 드라이버 로드 및 DB 연결
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection conn = DriverManager.getConnection(url, user, password);

                // 지역 검색용 SQL 문 (district 기준)
                String districtSql = "SELECT DISTINCT p.name, p.description " +
                                     "FROM place p " +
                                     "JOIN district d ON p.district_id = d.id " +
                                     "WHERE d.name = ?";

                // 태그 검색용 SQL 문 (tag 기준)
                String tagSql = "SELECT DISTINCT p.name, p.description " +
                                "FROM place p " +
                                "JOIN place_tag pt ON p.id = pt.place_id " +
                                "JOIN tag t ON pt.tag_id = t.id " +
                                "WHERE t.name = ?";

                // PreparedStatement 객체 생성
                PreparedStatement districtStmt = conn.prepareStatement(districtSql);
                PreparedStatement tagStmt = conn.prepareStatement(tagSql);

                Set<String> printedPlaces = new HashSet<>(); // 중복 출력 방지용 집합
                boolean anyResult = false; // 하나라도 검색 결과가 있는지 여부

                // 키워드 하나하나에 대해 처리
                for (String keyword : keywords) {
                    keyword = keyword.trim();
                    boolean isHashtag = keyword.startsWith("#");            // 해시태그 여부 확인
                    String cleanKeyword = isHashtag ? keyword.substring(1) : keyword;
                    boolean hasResult = false;

                    // 태그 검색 수행
                    tagStmt.setString(1, cleanKeyword);
                    ResultSet rsTag = tagStmt.executeQuery();
                    while (rsTag.next()) {
                        String name = rsTag.getString("name");
                        String description = rsTag.getString("description");
                        String key = name + description;

                        // 중복 방지를 위한 조건
                        if (printedPlaces.add(key)) {
                            hasResult = true;
                            anyResult = true;
                            System.out.println("=================");
                            System.out.println(name);
                            System.out.println("ㄴ" + description);
                            System.out.println("=================");
                        }
                    }
                    rsTag.close();

                    // 일반 키워드일 경우 지역 검색 및 샘플 데이터도 검색
                    if (!isHashtag) {
                        // 지역 검색 수행
                        districtStmt.setString(1, cleanKeyword);
                        ResultSet rsDistrict = districtStmt.executeQuery();
                        while (rsDistrict.next()) {
                            String name = rsDistrict.getString("name");
                            String description = rsDistrict.getString("description");
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
                        rsDistrict.close();

                        // 샘플 데이터에서도 키워드 포함 여부 검색
                        for (Destination d : sampleData) {
                            if (d.name.contains(cleanKeyword) || d.description.contains(cleanKeyword) ||
                                d.district.equals(cleanKeyword) || d.category.equals(cleanKeyword) ||
                                d.ageGroups.contains(cleanKeyword) || d.companionTypes.contains(cleanKeyword)) {

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
                    }

                    // 현재 키워드에 대한 결과가 없을 경우
                    if (!hasResult) {
                        System.out.println("=================");
                        System.out.println(keyword);
                        System.out.println("ㄴ관련 정보가 없습니다.");
                        System.out.println("=================");
                    }
                }

                // PreparedStatement 및 Connection 정리
                districtStmt.close();
                tagStmt.close();
                conn.close();

                // 전체 검색 결과가 하나도 없을 경우 재시도 여부 묻기
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
            e.printStackTrace(); // 예외 발생 시 콘솔에 출력
        }
    }
}
