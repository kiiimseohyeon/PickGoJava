//20번 줄 접속정보는 변경하셔야 합니다.
package pick_go;

import java.sql.*;
import java.util.*;

public class tag_research {
    public static void main(String[] args) {
        // Scanner 객체 생성
        Scanner scanner = new Scanner(System.in);
        System.out.print("검색할 태그들을 입력하세요 (예: 맛집 대전): ");
        // 입력받은 문자열을 앞뒤 공백 제거 후 소문자로 변환
        String input = scanner.nextLine().trim().toLowerCase();
        scanner.close();

        // 입력된 문자열을 공백을 기준으로 분리하여 키워드 리스트 생성
        String[] inputTags = input.split("\\s+");
        List<String> keywords = Arrays.asList(inputTags);

        // 데이터베이스 접속 정보 설정
        String jdbcUrl = "jdbc:mysql://localhost:3306/ipp_pickgo?useSSL=false&serverTimezone=UTC";
        String dbUser = "root";         // 아이디
        String dbPass = "jang1107";     // 비밀번호

        // try-with-resources 구문을 사용하여 Connection 객체를 생성하고 자동으로 자원 해제
        try (
            Connection conn = DriverManager.getConnection(jdbcUrl, dbUser, dbPass)
        ) {
            // 1. 입력된 태그 이름들을 기반으로 해당하는 tag_id들을 조회하는 SQL 쿼리 작성
            String tagIdSql = "SELECT id FROM tag WHERE LOWER(name) IN (" +
                    String.join(",", Collections.nCopies(keywords.size(), "?")) + ")";
            // PreparedStatement를 사용하여 성능 향상
            PreparedStatement tagStmt = conn.prepareStatement(tagIdSql);
            for (int i = 0; i < keywords.size(); i++) {
                tagStmt.setString(i + 1, keywords.get(i));
            }

            // 쿼리 실행 후 결과를 ResultSet으로 받음
            ResultSet tagRs = tagStmt.executeQuery();
            List<Integer> tagIds = new ArrayList<>();
            // 결과셋을 순회하며 tag_id들을 리스트에 추가
            while (tagRs.next()) {
                tagIds.add(tagRs.getInt("id"));
            }

            // 입력한 키워드 중 데이터베이스에 존재하지 않는 태그가 있는지 확인
            if (tagIds.size() != keywords.size()) {
                System.out.println("❌ 입력한 태그 중 일부가 존재하지 않습니다.");
                return;
            }

            // 2. 입력된 모든 태그를 포함하는 장소들을 조회하는 SQL 쿼리 작성
            StringBuilder query = new StringBuilder();
            query.append("SELECT p.name ")
                 .append("FROM place p ")
                 .append("JOIN place_tag pt ON p.id = pt.place_id ")
                 .append("WHERE pt.tag_id IN (")
                 .append(String.join(",", Collections.nCopies(tagIds.size(), "?")))
                 .append(") ")
                 .append("GROUP BY p.id ")
                 .append("HAVING COUNT(DISTINCT pt.tag_id) = ?");

            // PreparedStatement를 사용하여 성능 향상
            PreparedStatement stmt = conn.prepareStatement(query.toString());
            int idx = 1;        // PreparedStatement의 파라미터 인덱스는 1부터 시작
            for (Integer tagId : tagIds) {
                stmt.setInt(idx++, tagId);
            }
            // HAVING 절의 파라미터 설정: 입력된 태그의 개수와 일치하는 장소만 조회
            stmt.setInt(idx, tagIds.size());

            // 쿼리 실행 후 결과를 ResultSet으로 받음
            ResultSet rs = stmt.executeQuery();

            boolean found = false;  // 결과가 있는지 여부를 확인하기 위한 플래그
            System.out.println("\n🔍 모든 태그를 포함한 장소:");
            // 장소 이름을 출력
            while (rs.next()) {
                System.out.println("✅ " + rs.getString("name"));
                found = true;
            }

            // 결과가 없는 경우
            if (!found) {
                System.out.println("❌ 입력한 모든 태그를 포함한 장소가 없습니다.");
            }

        } catch (SQLException e) {
            // SQL 예외 발생 시 스택 트레이스를 출력하여 디버깅에 도움을 줌
            e.printStackTrace();
        }
    }
}
