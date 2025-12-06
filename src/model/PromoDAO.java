package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import util.Connect;

public class PromoDAO {

    private Connect connect = Connect.getInstance();

    public Promo getPromoByCode(String code) {
        String sql = "SELECT id_promo, code, headline, discount_percentage FROM promos WHERE code = ?";
        try (PreparedStatement pstmt = connect.preparedStatement(sql)) {
            pstmt.setString(1, code);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Promo(
                        String.valueOf(rs.getInt("id_promo")),
                        rs.getString("code"),
                        rs.getString("headline"),
                        rs.getDouble("discount_percentage")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
