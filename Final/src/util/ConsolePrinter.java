package util;

import model.Destination;

import java.util.*;
import java.util.stream.Collectors;

public class ConsolePrinter {

    public static void printMainMenu() {
        System.out.println("===================================================\n");
        System.out.println("            📍 PICKGO에 오신 것을 환영합니다!");
        System.out.println("    1. #해시태그 검색   2. 지역 추천   3. 프로그램 종료    \n");
        System.out.println("===================================================");
    }

    public static void printHashtagIntro() {
        System.out.println("\n🔎 해시태그 기반 장소 검색을 시작합니다.");
        System.out.println("👉 여러 키워드를 공백으로 구분하여 입력하세요.");
        System.out.println("   검색 가능한 키워드: 10대  20대   30대  40대   50대   커플   가족   힐링   맛집   친구\n");
    }

    public static void printPlaceResults(List<String> places) {
        System.out.println("\n✅ 검색 결과:");
        for (int i = 0; i < places.size(); i++) {
            String name = places.get(i);
            if (name.length() > 10) {
                name = name.substring(0, 10) + "…";
            }
            System.out.printf("%-15s", "- " + name);
            if ((i + 1) % 3 == 0 || i == places.size() - 1) {
                System.out.println();
            }
        }
        System.out.println();
    }

    public static void printRegionIntro() {
        System.out.println("🔎 지역 기반 장소 추천을 시작합니다.");
        System.out.println("👉 아래 지역 중 하나를 선택하여 추천을 받아보세요.");
    }

    public static void printRegionResults(String district, List<Destination> results) {
        System.out.println("\n✅ 지역 추천 결과 (" + district + "):");

        // 카테고리를 '맛집', '카페', '놀거리'로 정리
        Map<String, List<Destination>> grouped = new HashMap<>();
        for (Destination dest : results) {
            String original = dest.getCategory().toLowerCase();
            String mapped;

            if (original.contains("맛집")) {
                mapped = "맛집";
            } else if (original.contains("카페")) {
                mapped = "카페";
            } else {
                mapped = "놀거리";
            }

            grouped.computeIfAbsent(mapped, k -> new ArrayList<>()).add(dest);
        }

        List<String> order = Arrays.asList("맛집", "카페", "놀거리");
        for (String category : order) {
            List<Destination> list = grouped.getOrDefault(category, Collections.emptyList());
            if (!list.isEmpty()) {
                System.out.println("\n📌 " + category + " 추천 목록:");
                for (int i = 0; i < list.size(); i++) {
                    String name = list.get(i).getName();
                    if (name.length() > 10) {
                        name = name.substring(0, 10) + "…";
                    }
                    System.out.printf("%-15s", "- " + name);
                    if ((i + 1) % 3 == 0 || i == list.size() - 1) {
                        System.out.println();
                    }
                }
            }
        }
    }
}

