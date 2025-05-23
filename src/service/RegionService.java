package service;

import java.util.*;

public class RegionService {
    private final Scanner scanner;
    private final List<Destination> destinations;

    // 생성자: Scanner 주입 및 샘플 데이터 초기화
    public RegionService(Scanner scanner) {
        this.scanner = scanner;
        this.destinations = initializeSampleData();
    }

    // 프로그램 실행 메서드
    public void run() {
        System.out.println("=== 대전 여행지 추천 ===\n");

        String region = ask("지역을 선택하세요", List.of("서구", "동구", "유성구", "중구", "대덕구"));
        String ageGroup = ask("연령대를 선택하세요", List.of("10대", "20대", "30대", "40대", "50대 이상"));
        String travelType = ask("여행 유형을 선택하세요", List.of("가족", "친구", "연인"));
        String category = ask("카테고리를 선택하세요", List.of("맛집", "카페", "볼거리"));

        List<Destination> results = filterDestinations(region, category, ageGroup, travelType);

        System.out.println("\n=== 추천 결과 ===");
        if (results.isEmpty()) {
            System.out.println("추천 결과가 없습니다.");
        } else {
            results.forEach(System.out::println);
        }
    }

    // 사용자에게 항목 선택을 요청하고 유효한 입력을 받을 때까지 반복
    private String ask(String prompt, List<String> options) {
        System.out.println(prompt + ":");
        for (int i = 0; i < options.size(); i++) {
            System.out.printf("%d. %s  ", i + 1, options.get(i));
        }
        System.out.println();

        while (true) {
            System.out.print("번호 입력: ");
            try {
                int input = Integer.parseInt(scanner.nextLine());
                if (input >= 1 && input <= options.size()) {
                    return options.get(input - 1);
                }
            } catch (NumberFormatException ignored) {
            }
            System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
        }
    }

    // 입력 조건에 따라 여행지 목록 필터링
    private List<Destination> filterDestinations(String region, String category, String ageGroup, String travelType) {
        List<Destination> result = new ArrayList<>();
        for (Destination d : destinations) {
            if (d.matches(region, category, ageGroup, travelType)) {
                result.add(d);
            }
        }
        return result;
    }

    // 샘플 여행지 데이터를 리스트로 생성
    private List<Destination> initializeSampleData() {
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
        new RegionService(new Scanner(System.in)).run();
    }

    // 여행지 정보를 담는 내부 클래스
    static class Destination {
        private final int id;
        private final String name;
        private final String category;
        private final String description;
        private final String region;
        private final List<String> ageGroups;
        private final List<String> travelTypes;

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

        // 조건에 해당하는지 여부를 판단하는 메서드
        public boolean matches(String region, String category, String ageGroup, String travelType) {
            return this.region.equals(region) &&
                   this.category.equals(category) &&
                   (this.ageGroups.contains(ageGroup) || this.travelTypes.contains(travelType));
        }

        // 객체 정보 문자열로 출력
        @Override
        public String toString() {
            return String.format("[ID: %d] %s - %s (%s)\n설명: %s", id, name, region, category, description);
        }
    }
}
