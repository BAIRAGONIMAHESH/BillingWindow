package loginapp;

import java.util.Date;
import java.util.List;

public class Transaction {
    private String id;
    private Date date;
    private String paymentMethod;
    private Buyer buyer;
    private List<Product> productList;
    private String patientName;

    public Transaction(String id, Date date, String paymentMethod, Buyer buyer, List<Product> productList, String patientName) {
        this.id = id;
        this.date = date;
        this.paymentMethod = paymentMethod;
        this.buyer = buyer;
        this.productList = productList;
        this.patientName = patientName;
    }

    public String getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public Buyer getBuyer() {
        return buyer;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public String getPatientName() {
        return patientName;
    }

    // Calculate subtotal before tax
    public double getSubtotal() {
        return productList.stream().mapToDouble(Product::getTotalPrice).sum();
    }

    // SGST 9%
//    public double getSGST() {
//        return getSubtotal() * 0.09;
//    }
//
//    // CGST 9%
//    public double getCGST() {
//        return getSubtotal() * 0.09;
//    }

    // Final total with tax
    public double getTotal() {
        return getSubtotal();
    }

    Iterable<Product> getProducts() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}