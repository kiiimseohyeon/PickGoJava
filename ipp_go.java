import java.util.Scanner;

public class RegionService {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("====================================");
        System.out.println("	  📍PICKGO에 오신걸 환영합니다.	");
        System.out.println("    1.#해시태그 검색 	2.지역 추천  ")
        System.out.println("====================================\n");

        // 1. 지역 선택
        System.out.println("🗺️  #지역 선택");
        System.out.println("    [1] 서구   [2] 동구   [3] 유성구   [4] 중구");
        System.out.print("    ▶ 선택 (번호 입력): ");
        int region = sc.nextInt();

        // 2. 연령대 선택
        System.out.println("\n👤  #연령대 선택");
        System.out.println("    [1] 10대   [2] 20대   [3] 30대   [4] 40대");
        System.out.print("    ▶ 선택 (번호 입력): ");
        int age = sc.nextInt();

        // 3. 여행 유형 선택
        System.out.println("\n👫  #여행 유형");
        System.out.println("    [1] 친구   [2] 가족   [3] 연인");
        System.out.print("    ▶ 선택 (번호 입력): ");
        int type = sc.nextInt();

        // 4. 카테고리 선택
        System.out.println("\n🍴  #카테고리 선택");
        System.out.println("    [1] #맛집   [2] #카페   [3] #놀거리");
        System.out.print("    ▶ 선택 (번호 입력): ");
        int category = sc.nextInt();

        // 선택 확인 출력
        System.out.println("\n====================================");
        System.out.println("🔎 선택된 조건으로 검색을 진행합니다...");
        System.out.println("⚠️  (현재는 기능 없음 - 결과 연동 예정)");
        System.out.println("====================================");
    }
}
