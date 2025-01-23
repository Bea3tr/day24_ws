package paf.day24_workshop.models;

import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

import jakarta.json.JsonArray;
import jakarta.json.JsonObject;


public class Order {

    private int orderId;
    private Date orderDate;
    private String customerName;
    private String shipAddress;
    private String notes;
    private float tax;
    private List<OrderDetails> orderDetails = new LinkedList<>();

    public int getOrderId() {return orderId;}
    public void setOrderId(int orderId) {this.orderId = orderId;}
    
    public Date getOrderDate() {return orderDate;}
    public void setOrderDate(Date orderDate) {this.orderDate = orderDate;}
    
    public String getCustomerName() {return customerName;}
    public void setCustomerName(String customerName) {this.customerName = customerName;}
    
    public String getShipAddress() {return shipAddress;}
    public void setShipAddress(String shipAddress) {this.shipAddress = shipAddress;}
    
    public String getNotes() {return notes;}
    public void setNotes(String notes) {this.notes = notes;}
    
    public float getTax() {return tax;}
    public void setTax(float tax) {this.tax = tax;}
    
    public List<OrderDetails> getOrderDetails() {return orderDetails;}
    public void setOrderDetails(List<OrderDetails> orderDetails) {this.orderDetails = orderDetails;}
    
    public static Order JsonToOrder(JsonObject obj) {
        Order o = new Order();
        o.setCustomerName(obj.getString("customer_name"));
        o.setShipAddress(obj.getString("ship_address"));
        o.setNotes(obj.getString("notes"));
        o.setTax(Float.parseFloat(obj.getJsonNumber("tax").toString()));
        List<OrderDetails> ods = new LinkedList<>();
        JsonArray lineItems = obj.getJsonArray("line_items");
        for (int i = 0; i < lineItems.size(); i++) {
            JsonObject li = lineItems.getJsonObject(i);
            ods.add(new OrderDetails(li.getString("product"), Float.parseFloat(li.getJsonNumber("unit_price").toString()), 
                Float.parseFloat(li.getJsonNumber("discount").toString()), li.getInt("quantity")));
        }
        return o;
    }
    @Override
    public String toString() {
        return "Order [orderId=" + orderId + ", orderDate=" + orderDate + ", customerName=" + customerName
                + ", shipAddress=" + shipAddress + ", notes=" + notes + ", tax=" + tax + ", orderDetails="
                + orderDetails + "]";
    }

    
}
