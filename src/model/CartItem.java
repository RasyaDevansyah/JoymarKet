package model;

public class CartItem {
    private String idCustomer;
    private Product product;
    private int count;

    public CartItem(String idCustomer, Product product, int count) {
        this.idCustomer = idCustomer;
        this.product = product;
        this.count = count;
    }

    public String getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(String idCustomer) {
        this.idCustomer = idCustomer;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
