package loginapp;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;

public class BillGeneratorPanel extends JPanel {
    private Transaction transaction;

    public BillGeneratorPanel(Transaction transaction) {
        this.transaction = transaction;
        setLayout(new BorderLayout());

        JTextArea billArea = new JTextArea();
        billArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        billArea.setEditable(false);

        StringBuilder sb = new StringBuilder();
        sb.append("\tPHARMACY BILL\n");
        sb.append("---------------------------------------------\n");
        sb.append("Buyer: ").append(transaction.getBuyer().getName()).append("\n");
        sb.append("Phone: ").append(transaction.getBuyer().getContact()).append("\n");
        sb.append("Email: ").append(transaction.getBuyer().getEmail()).append("\n");
        sb.append("Address: ").append(transaction.getBuyer().getAddress()).append("\n");
        sb.append("Date: ").append(new SimpleDateFormat("dd-MM-yyyy").format(transaction.getDate())).append("\n");
        sb.append("Payment Method: ").append(transaction.getPaymentMethod()).append("\n");
        sb.append("---------------------------------------------\n");
        sb.append(String.format("%-25s %-10s %-10s\n", "Product", "Qty", "Total"));
        sb.append("---------------------------------------------\n");

        for (Product p : transaction.getProductList()) {
            sb.append(String.format("%-25s %-10d ₹%-10.2f\n", p.getName(), p.getQuantity(), p.getTotalPrice()));
        }

        sb.append("---------------------------------------------\n");

        // Calculations without GST
        double subtotal = transaction.getSubtotal();

        double discount = 0.0;
        if (subtotal >= 3000 && subtotal < 20000) {
            discount = subtotal * 0.02;
        } else if (subtotal >= 20000) {
            discount = subtotal * 0.01;
        } else {
            discount = subtotal * 0.05;
        }

        double finalAmount = subtotal - discount;

        // Final summary
        sb.append(String.format("Subtotal       : ₹%.2f\n", subtotal));
        sb.append(String.format("Discount       : ₹%.2f\n", discount));
        sb.append("---------------------------------------------\n");
        sb.append(String.format("Final Payable  : ₹%.2f\n", finalAmount));
        sb.append("---------------------------------------------\n");
        sb.append("Thank you for shopping with us!\n");

        billArea.setText(sb.toString());
        add(new JScrollPane(billArea), BorderLayout.CENTER);
    }
}