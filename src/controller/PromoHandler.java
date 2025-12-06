package controller;

import model.Payload;
import model.Promo;
import model.PromoDAO;

public class PromoHandler {

    private PromoDAO promoDAO = new PromoDAO();

    public Promo getPromoByCode(String code) {
        return promoDAO.getPromoByCode(code);
    }

    public Payload validatePromo(String promoCode, double currentTotal, String userId) {
        if (promoCode == null || promoCode.trim().isEmpty()) {
            return new Payload("Promo code cannot be empty.", null, false);
        }

        Promo promo = promoDAO.getPromoByCode(promoCode);
        if (promo == null) {
            return new Payload("Invalid promo code.", null, false);
        }

        return new Payload("Promo code is valid.", promo, true);
    }

    public void markPromoAsUsed(String promoId, String userId) {
        promoDAO.markPromoAsUsed(userId, promoId);
    }

    public double applyPromoDiscount(Promo promo, double originalTotal) {
        double discountAmount = originalTotal * (promo.getDiscountPercentage() / 100.0);
        return originalTotal - discountAmount;
    }
}
