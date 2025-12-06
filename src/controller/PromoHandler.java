package controller;

import model.Payload;
import model.Promo;
import model.PromoDAO;

public class PromoHandler {

    private PromoDAO promoDAO = new PromoDAO();

    public Payload validatePromoCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            return new Payload("Promo code cannot be empty.", null, false);
        }

        Promo promo = promoDAO.getPromoByCode(code);
        if (promo == null) {
            return new Payload("Invalid promo code.", null, false);
        }

        return new Payload("Promo code is valid.", promo, true);
    }

    public Payload applyPromo(String code, double currentTotal) {
        Payload validationPayload = validatePromoCode(code);
        if (!validationPayload.isSuccess()) {
            return validationPayload;
        }

        Promo promo = (Promo) validationPayload.getData();
        double discountAmount = currentTotal * (promo.getDiscountPercentage() / 100.0);
        double discountedTotal = currentTotal - discountAmount;

        return new Payload("Promo applied successfully.", discountedTotal, true, promo.getIdPromo());
    }
}
