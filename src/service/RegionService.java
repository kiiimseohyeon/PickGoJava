package service;

import java.util.*;

public class RegionService {
    private final Scanner scanner;
    private final List<Destination> destinations;

    // 생성자: Scanner 객체를 받아 여행지 데이터 초기화
    public RegionService(Scanner scanner) {
        this.scanner = scanner;
        this.destinations = initializeSampleData();
    }

    // 프로그램 실행 메서드
    public void run() {
        System.out.println("=== 대전 여행지 추천 ===\n");

        // 사용자 입력 받기: 지역, 연령대, 여행 유형, 카테고리
        String region = promptSelection("지역을 선택하세요", List.of("서구", "동구", "유성구", "중구", "대덕구"));
        String ageGroup = promptSelection("연령대를 선택하세요", List.of("10대", "20대", "30대", "40대", "50대 이상"));
        String travelType = promptSelection("여행 유형을 선택하세요", List.of("가족", "친구", "연인"));
        String category = promptSelection("카테고리를 선택하세요", List.of("맛집", "카페", "볼거리"));

        // 필터링: 조건에 맞는 여행지 리스트 가져오기
        List<Destination> results = filterDestinations(region, category, ageGroup, travelType);

        // 결과 출력
        System.out.println("\n=== 추천 결과 ===");
        if (results.isEmpty()) {
            System.out.println("조건에 맞는 여행지가 없습니다.");
        } else {
            results.forEach(dest -> System.out.println(dest + "\n"));
        }
    }

    /**
     * 사용자에게 선택지를 보여주고 번호 입력을 받아 선택된 항목 반환
     * @param prompt 질문 문구
     * @param options 선택지 리스트
     * @return 선택된 옵션 문자열
     */
    private String promptSelection(String prompt, List<String> options) {
        while (true) {
            System.out.println("\n" + prompt + ":");
            // 번호와 함께 옵션 출력
            for (int i = 0; i < options.size(); i++) {
                System.out.printf("%d. %s  ", i + 1, options.get(i));
            }
            System.out.println();

            System.out.print("번호 입력: ");
            String input = scanner.nextLine().trim();

            try {
                int index = Integer.parseInt(input);
                if (index >= 1 && index <= options.size()) {
                    return options.get(index - 1);
                } else {
                    System.out.println("⚠️ 유효한 번호를 입력하세요 (1~" + options.size() + ")");
                }
            } catch (NumberFormatException e) {
                System.out.println("⚠️ 숫자를 입력해주세요.");
            }
        }
    }

    /**
     * 여행지 리스트에서 조건에 맞는 결과를 필터링하는 메서드
     * 우선 모든 조건(지역, 카테고리, 연령대, 여행 유형)이 일치하는 결과를 찾고,
     * 없으면 지역 + 카테고리만 일치하는 결과를 반환
     * @param region 지역
     * @param category 카테고리
     * @param ageGroup 연령대
     * @param travelType 여행 유형
     * @return 필터링된 여행지 리스트
     */
    private List<Destination> filterDestinations(String region, String category, String ageGroup, String travelType) {
        // 1차 필터: 모든 조건이 완벽히 일치하는 여행지 찾기
        List<Destination> strictMatches = destinations.stream()
                .filter(d -> d.matches(region, category, ageGroup, travelType))
                .toList();

        if (!strictMatches.isEmpty()) {
            return strictMatches;
        }

        // 2차 필터: 지역과 카테고리만 일치하는 여행지 반환 (대체 추천)
        return destinations.stream()
                .filter(d -> d.getRegion().equals(region) && d.getCategory().equals(category))
                .toList();
    }

    /**
     * 샘플 여행지 데이터를 초기화하는 메서드
     * @return 여행지 리스트
     */
    private List<Destination> initializeSampleData() {
        return List.of(
            new Destination(1, "한밭수목원", "볼거리", "자연을 느낄 수 있는 수목원", "서구",
                    List.of("20대", "30대", "40대", "50대 이상"), List.of("가족", "친구")),
            new Destination(2, "성심당", "맛집", "대전 대표 빵집", "중구",
                    List.of("10대", "20대", "30대"), List.of("친구", "연인")),
            new Destination(3, "유성온천", "볼거리", "따뜻한 온천욕을 즐길 수 있음", "유성구",
                    List.of("40대", "50대 이상"), List.of("가족")),
            new Destination(4, "카페베네", "카페", "뷰가 좋은 감성 카페", "서구",
                    List.of("20대", "30대"), List.of("연인")),
            new Destination(5, "대청호 오백리길", "볼거리", "자연 경관이 뛰어난 산책로", "대덕구",
                    List.of("30대", "40대", "50대 이상"), List.of("가족", "친구")),
            new Destination(6, "도룡동 카페거리", "카페", "분위기 있는 카페 거리", "유성구",
                    List.of("20대", "30대"), List.of("연인", "친구")),
            new Destination(7, "서구 맛집식당", "맛집", "서구에서 인기 있는 맛집", "서구",
                    List.of("10대", "20대", "30대"), List.of("가족", "친구")),
            new Destination(8, "대동하늘공원", "볼거리", "전망이 아름다운 공원", "동구",
                    List.of("20대", "30대", "40대"), List.of("연인", "친구")),
            new Destination(9, "중구 숯불갈비집", "맛집", "전통 숯불구이 전문점", "중구",
                    List.of("30대", "40대", "50대 이상"), List.of("가족")),
            new Destination(10, "계족산 황톳길", "볼거리", "맨발로 걷기 좋은 트레킹 코스", "대덕구",
                    List.of("20대", "30대", "40대", "50대 이상"), List.of("가족", "친구")),
            new Destination(11, "동구 시장 맛집거리", "맛집", "현지인 추천 재래시장 먹거리", "동구",
                    List.of("10대", "20대", "30대"), List.of("친구")),
            new Destination(12, "대전 아트 카페", "카페", "전시도 함께 볼 수 있는 예술 감성 카페", "중구",
                    List.of("30대", "40대"), List.of("연인")),
            new Destination(13, "엑스포과학공원", "볼거리", "가족과 함께 즐길 수 있는 과학 테마파크", "유성구",
                    List.of("10대", "20대", "30대", "40대"), List.of("가족")),
            new Destination(14, "유림공원", "볼거리", "산책과 피크닉이 가능한 도심 공원", "서구",
                    List.of("40대", "50대 이상"), List.of("가족")),
            new Destination(15, "대덕구 브런치 카페", "카페", "한적한 분위기의 브런치 맛집", "대덕구",
                    List.of("30대", "40대"), List.of("연인", "친구")),
            new Destination(16, "동구 청소년 거리", "볼거리", "청소년들을 위한 문화 공간", "동구",
                    List.of("10대"), List.of("친구")),
            new Destination(17, "유성구 퓨전레스토랑", "맛집", "연인과 분위기 있게 즐길 수 있는 레스토랑", "유성구",
                    List.of("20대", "30대"), List.of("연인")),
            new Destination(18, "중구 전통찻집", "카페", "고풍스러운 인테리어의 전통찻집", "중구",
                    List.of("40대", "50대 이상"), List.of("가족", "연인"))
        );
    }

    public static void main(String[] args) {
        new RegionService(new Scanner(System.in)).run();
    }

    // 여행지 정보를 담는 내부 클래스
    static class Destination {
        private final int id;                  // 고유 아이디
        private final String name;             // 여행지 이름
        private final String category;         // 카테고리 (맛집, 카페, 볼거리)
        private final String description;      // 여행지 설명
        private final String region;           // 지역 (서구, 동구 등)
        private final List<String> ageGroups; // 해당 여행지 추천 연령대
        private final List<String> travelTypes; // 추천 여행 유형 (가족, 친구, 연인)

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

        /**
         * 모든 조건이 일치하는지 여부 판단
         * @param region 지역
         * @param category 카테고리
         * @param ageGroup 연령대
         * @param travelType 여행 유형
         * @return 조건 일치 여부
         */
        public boolean matches(String region, String category, String ageGroup, String travelType) {
            return this.region.equals(region)
                    && this.category.equals(category)
                    && this.ageGroups.contains(ageGroup)
                    && this.travelTypes.contains(travelType);
        }

        public String getRegion() {
            return region;
        }

        public String getCategory() {
            return category;
        }

        @Override
        public String toString() {
            return String.format(
                "여행지: %s\n지역: %s | 카테고리: %s\n설명: %s",
                name, region, category, description
            );
        }
    }
}
