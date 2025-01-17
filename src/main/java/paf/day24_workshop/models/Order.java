package paf.day24_workshop.models;

import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

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
    
}
