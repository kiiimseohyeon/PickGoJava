package service;

import model.Destination;
import util.ConsolePrinter;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class HashtagService implements SearchService {
    private final Scanner scanner;
    private final Connection conn;

    public HashtagService(Scanner scanner, Connection conn) {
        this.scanner = scanner;
        this.conn = conn;
    }

    @Override
    public void run() {
        while (true) {
            ConsolePrinter.printHashtagIntro();
            System.out.print("검색할 태그들을 입력하세요 (예: 맛집 대전): ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.isEmpty()) {
                System.out.println("❌ 태그를 입력해주세요.");
                continue;
            }

            String[] inputTags = input.split("\\s+");
            List<String> keywords = Arrays.asList(inputTags);

            try {
                List<Integer> tagIds = getTagIds(keywords);
                if (tagIds.size() != keywords.size()) {
                    System.out.println("❌ 입력한 태그 중 일부가 존재하지 않습니다.");
                    continue;
                }
                List<String> places = findPlacesByTags(tagIds);
                if (places.isEmpty()) {
                    System.out.println("❌ 입력한 모든 태그를 포함한 장소가 없습니다.");
                } else {
                    ConsolePrinter.printPlaceResults(places);
                }

                if (!askContinue()) break;

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private List<Integer> getTagIds(List<String> keywords) throws SQLException {
        String sql = "SELECT id FROM tag WHERE LOWER(name) IN (" +
                String.join(",", Collections.nCopies(keywords.size(), "?")) + ")";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < keywords.size(); i++) {
                stmt.setString(i + 1, keywords.get(i));
            }
            ResultSet rs = stmt.executeQuery();
            List<Integer> ids = new ArrayList<>();
            while (rs.next()) {
                ids.add(rs.getInt("id"));
            }
            return ids;
        }
    }

    private List<String> findPlacesByTags(List<Integer> tagIds) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("SELECT p.name FROM place p ")
             .append("JOIN place_tag pt ON p.id = pt.place_id ")
             .append("WHERE pt.tag_id IN (")
             .append(String.join(",", Collections.nCopies(tagIds.size(), "?")))
             .append(") GROUP BY p.id HAVING COUNT(DISTINCT pt.tag_id) = ?");

        try (PreparedStatement stmt = conn.prepareStatement(query.toString())) {
            int idx = 1;
            for (Integer id : tagIds) {
                stmt.setInt(idx++, id);
            }
            stmt.setInt(idx, tagIds.size());
            ResultSet rs = stmt.executeQuery();
            List<String> results = new ArrayList<>();
            while (rs.next()) {
                results.add(rs.getString("name"));
            }
            return results;
        }
    }

    private boolean askContinue() {
        while (true) {
            System.out.print("🔁 다른 해시태그로 다시 검색하시겠습니까? (y/n): ");
            String response = scanner.nextLine().trim().toLowerCase();
            if (response.equals("y")) return true;
            else if (response.equals("n")) return false;
            else System.out.println("❌ 잘못된 입력입니다. y 또는 n을 입력해주세요.");
        }
    }
}
