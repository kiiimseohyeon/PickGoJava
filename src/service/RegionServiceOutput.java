package service;

import java.util.*;

public class RegionServiceOutput {
    private final Scanner scanner;
    private final List<Destination> destinations;

    public RegionServiceOutput(Scanner scanner) {
        this.scanner = scanner;
        this.destinations = loadSampleData();  
    }

    public void run() {
        System.out.println("=== 대전 여행지 검색 ===");
        while (true) {
            System.out.print("\n검색할 지역명, 태그 또는 해시태그를 입력하세요 (종료: exit) : ");
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("exit")) {
                System.out.println("프로그램을 종료합니다.");
                break;
            }

            String[] keywords = input.split("\\s+");
            Set<String> printed = new HashSet<>();
            boolean anyResult = false;

            for (String keyword : keywords) {
                String cleanKeyword = keyword.startsWith("#") ? keyword.substring(1) : keyword;
                boolean matched = false;

                for (Destination d : destinations) {
                    boolean match = d.name.contains(cleanKeyword)
                                 || d.description.contains(cleanKeyword)
                                 || d.region.equalsIgnoreCase(cleanKeyword)
                                 || d.category.equalsIgnoreCase(cleanKeyword)
                                 || d.ageGroups.contains(cleanKeyword)
                                 || d.travelTypes.contains(cleanKeyword);

                    if (match) {
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

                if (!matched) {
                    System.out.println("=================");
                    System.out.println(cleanKeyword);
                    System.out.println("ㄴ관련 정보가 없습니다.");
                    System.out.println("=================");
                }
            }

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

    public static void main(String[] args) {
        new RegionServiceOutput(new Scanner(System.in)).run();
    }

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
    }
}
