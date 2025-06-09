package Main;

import service.HashtagService;
import service.RegionService;
import service.SearchService;
import util.ConsolePrinter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String url = "jdbc:mysql://localhost:3306/ipp_pickgo?useSSL=false&serverTimezone=UTC";
        String user = "root";
        String password = "eun1224!!";

        while (true) {
            ConsolePrinter.printMainMenu();
            System.out.print("▶ 기능 선택 (번호 입력): \n");
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("❌ 숫자를 입력해주세요.");
                continue;
            }

            switch (choice) {
                case 1 -> {
                    try (Connection conn = DriverManager.getConnection(url, user, password)) {
                        SearchService hashtag = new HashtagService(scanner, conn);
                        hashtag.run();
                    } catch (Exception e) {
                        System.out.println("[오류] DB 연결 실패: " + e.getMessage());
                    }
                }
                case 2 -> {
                    SearchService region = new RegionService(scanner);
                    region.run();
                }
                case 3 -> {
                    System.out.println("👋 프로그램을 종료합니다.");
                    return;
                }
                default -> System.out.println("❌ 올바른 번호를 입력해주세요.");
            }
        }
    }
}
