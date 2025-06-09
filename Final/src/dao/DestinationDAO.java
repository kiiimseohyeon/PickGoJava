package dao;

import model.Destination;

import java.sql.*;

public class DestinationDAO {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/ipp_pickgo?useSSL=false&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "eun1224!!";

    public static Destination getPlaceDetailByName(String placeName) {
        String query = "SELECT name, category, address, description FROM place WHERE name = ?";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement pstmt = conn.prepareStatement(query)) {

                pstmt.setString(1, placeName);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        String name = rs.getString("name");
                        String category = rs.getString("category");
                        String address = rs.getString("address");
                        String description = rs.getString("description");

                        return new Destination(name, category, address, description);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("[DB 오류] 장소 정보 조회 실패: " + e.getMessage());
        }

        return null;
    }
}
