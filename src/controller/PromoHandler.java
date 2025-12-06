package controller;

import model.Payload;
import model.Promo;
import model.PromoDAO;
import model.Session;
import model.User;

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

        // Additional checks: expiry date, minimum purchase, etc.
        // For now, only checking for user usage.
        // Assuming these checks are handled within the PromoDAO or Promo model if they exist.

        if (promoDAO.isPromoUsedByUser(userId, promo.getIdPromo())) {
            return new Payload("This promo code has already been used by you.", null, false);
        }

        // Check if promo is expired (example, assuming Promo has an expiryDate field)
        // if (promo.getExpiryDate().before(new Date())) {
        //     return new Payload("This promo code has expired.", null, false);
        // }

        // Check for minimum purchase amount (example, assuming Promo has a minPurchaseAmount field)
        // if (currentTotal < promo.getMinPurchaseAmount()) {
        //     return new Payload("Minimum purchase of Rp " + promo.getMinPurchaseAmount() + " required.", null, false);
        // }


        return new Payload("Promo code is valid.", promo, true);
    }

    public boolean isPromoUsedByUser(String promoId, String userId) {
        return promoDAO.isPromoUsedByUser(userId, promoId);
    }

    public void markPromoAsUsed(String promoId, String userId) {
        promoDAO.markPromoAsUsed(userId, promoId);
    }

    public double applyPromoDiscount(Promo promo, double originalTotal) {
        double discountAmount = originalTotal * (promo.getDiscountPercentage() / 100.0);
        return originalTotal - discountAmount;
    }
}
