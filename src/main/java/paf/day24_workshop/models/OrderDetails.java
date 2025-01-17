package paf.day24_workshop.models;

public class OrderDetails {

    private int id;
    private String product;
    private float unitPrice;
    private float discount;
    private int quantity;
    
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    
    public String getProduct() {return product;}
    public void setProduct(String product) {this.product = product;}
    
    public float getUnitPrice() {return unitPrice;}
    public void setUnitPrice(float unitPrice) {this.unitPrice = unitPrice;}
    
    public float getDiscount() {return discount;}
    public void setDiscount(float discount) {this.discount = discount;}
    
    public int getQuantity() {return quantity;}
    public void setQuantity(int quantity) {this.quantity = quantity;}
    
    public OrderDetails(String product, float unitPrice, float discount, int quantity) {
        this.product = product;
        this.unitPrice = unitPrice;
        this.discount = discount;
        this.quantity = quantity;
    }
}
