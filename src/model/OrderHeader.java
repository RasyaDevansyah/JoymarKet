package model;

import java.sql.Date;

public class OrderHeader {
    private int idOrder;
    private String idCustomer;
    private String idPromo;
    private String promoCode; // New field
    private String promoHeadline; // New field
    private String status;
    private Date orderedAt;
    private double totalAmount;

    // Constructor for creating new OrderHeaders (without idOrder)
    public OrderHeader(String idCustomer, String idPromo, String status, Date orderedAt,
            double totalAmount) {
        this.idCustomer = idCustomer;
        this.idPromo = idPromo;
        this.status = status;
        this.orderedAt = orderedAt;
        this.totalAmount = totalAmount;
    }

    // Full constructor (including idOrder and promo details for fetching from DB)
    public OrderHeader(int idOrder, String idCustomer, String idPromo, String promoCode, String promoHeadline, String status, Date orderedAt, double totalAmount) {
        this.idOrder = idOrder;
        this.idCustomer = idCustomer;
        this.idPromo = idPromo;
        this.promoCode = promoCode;
        this.promoHeadline = promoHeadline;
        this.status = status;
        this.orderedAt = orderedAt;
        this.totalAmount = totalAmount;
    }

    public int getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(int idOrder) {
        this.idOrder = idOrder;
    }

    public String getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(String idCustomer) {
        this.idCustomer = idCustomer;
    }

    public String getIdPromo() {
        return idPromo;
    }

    public void setIdPromo(String idPromo) {
        this.idPromo = idPromo;
    }

    // New getters and setters for promoCode and promoHeadline
    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public String getPromoHeadline() {
        return promoHeadline;
    }

    public void setPromoHeadline(String promoHeadline) {
        this.promoHeadline = promoHeadline;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getOrderedAt() {
        return orderedAt;
    }

    public void setOrderedAt(Date orderedAt) {
        this.orderedAt = orderedAt;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
