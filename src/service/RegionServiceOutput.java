package service;

import java.util.*;

public class RegionServiceOutput {
    // 사용자 입력을 받기 위한 Scanner 객체
    private final Scanner scanner;

    // 여행지 정보가 저장된 리스트 (하드코딩된 샘플 데이터)
    private final List<Destination> destinations;

    // 생성자: Scanner 객체를 받아서 필드 초기화 및 샘플 데이터 로드
    public RegionServiceOutput(Scanner scanner) {
        this.scanner = scanner;
        this.destinations = loadSampleData();  // 하드코딩된 여행지 데이터 로딩
    }

    // 프로그램의 메인 로직 실행
    public void run() {
        System.out.println("=== 대전 여행지 검색 ===");

        while (true) {
            System.out.print("\n검색할 지역명, 태그 또는 해시태그를 입력하세요 (종료: exit) : ");
            String input = scanner.nextLine().trim();

            // 종료 조건
            if (input.equalsIgnoreCase("exit")) {
                System.out.println("프로그램을 종료합니다.");
                break;
            }

            // 공백으로 구분된 여러 키워드를 입력받음
            String[] keywords = input.split("\\s+");
            Set<String> printed = new HashSet<>();  // 중복 출력 방지를 위한 Set
            boolean anyResult = false;  // 검색 결과가 하나라도 있었는지 여부

            // 입력된 키워드들 순회
            for (String keyword : keywords) {
                // # 해시태그인 경우 # 제거
                String cleanKeyword = keyword.startsWith("#") ? keyword.substring(1) : keyword;
                boolean matched = false;  // 해당 키워드로 검색된 결과가 있는지 여부

                // 모든 여행지에 대해 키워드와 일치하는지 확인
                for (Destination d : destinations) {
                    boolean match =
                        d.name.contains(cleanKeyword) ||                           // 이름 포함 여부
                        d.description.contains(cleanKeyword) ||                    // 설명 포함 여부
                        d.region.equalsIgnoreCase(cleanKeyword) ||                 // 지역명 일치 여부
                        d.category.equalsIgnoreCase(cleanKeyword) ||               // 카테고리 일치 여부
                        d.ageGroups.contains(cleanKeyword) ||                      // 나이대 태그 포함 여부
                        d.travelTypes.contains(cleanKeyword);                      // 여행 유형 태그 포함 여부

                    if (match) {
                        // 동일 장소 중복 출력 방지
                        String key = d.name + d.description;
                        if (printed.add(key)) {
                            anyResult = true;
                            matched = true;
                            System.out.println("=================");
                            System.out.println(d.name);
                            System.out.println("ㄴ" + d.description);
                            System.out.println("=================");
                        }
                    }
                }

                // 해당 키워드로 매칭된 결과가 없을 경우
                if (!matched) {
                    System.out.println("=================");
                    System.out.println(cleanKeyword);
                    System.out.println("ㄴ관련 정보가 없습니다.");
                    System.out.println("=================");
                }
            }

            // 전체 키워드 검색 결과가 없었을 경우 재검색 여부 확인
            if (!anyResult) {
                System.out.println("다시 검색하시겠습니까? (ㅇ/ㄴ): ");
                String retry = scanner.nextLine();
                if (!retry.equalsIgnoreCase("ㅇ")) {
                    System.out.println("프로그램을 종료합니다.");
                    break;
                }
            }
        }
    }

    // 하드코딩된 샘플 여행지 데이터 로딩
    private List<Destination> loadSampleData() {
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

    // 프로그램 진입점
    public static void main(String[] args) {
        new RegionServiceOutput(new Scanner(System.in)).run();
    }

    // Destination 클래스: 여행지 정보를 담는 데이터 구조
    static class Destination {
        private final int id;                    // 고유 ID
        private final String name;               // 장소 이름
        private final String category;           // 카테고리 (볼거리, 맛집, 카페 등)
        private final String description;        // 설명
        private final String region;             // 지역명 (예: 서구, 중구 등)
        private final List<String> ageGroups;    // 연령대 태그 (예: 20대, 30대 등)
        private final List<String> travelTypes;  // 동반 유형 태그 (예: 가족, 친구 등)

        public Destination(int id, String name, String category, String description,
                           String region, List<String> ageGroups, List<String> travelTypes) {
            this.id = id;
            this.name = name;
            this.category = category;
            this.description = description;
            this.region = region;
            this.ageGroups = ageGroups;
            this.travelTypes = travelTypes;
        }
    }
}
