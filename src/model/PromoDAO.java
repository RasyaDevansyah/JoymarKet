package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

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

    public boolean isPromoUsedByUser(String userId, String promoId) {
        String sql = "SELECT COUNT(*) FROM user_promos WHERE id_user = ? AND id_promo = ?";
        try (PreparedStatement pstmt = connect.preparedStatement(sql)) {
            pstmt.setInt(1, Integer.parseInt(userId));
            pstmt.setInt(2, Integer.parseInt(promoId));
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean markPromoAsUsed(String userId, String promoId) {
        String sql = "INSERT INTO user_promos (id_user, id_promo, used_at) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connect.preparedStatement(sql)) {
            pstmt.setInt(1, Integer.parseInt(userId));
            pstmt.setInt(2, Integer.parseInt(promoId));
            pstmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
