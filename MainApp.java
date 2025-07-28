// Updated MainApp.java with Add Product functionality

package loginapp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel; 
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;
import java.util.*;
import java.util.List;

// Updated Main.java with product name auto-suggest feature



public class MainApp
{ 
    public static void main(String[] args)
    { 
        SwingUtilities.invokeLater(() -> new BillingForm().setVisible(true)); 
    } 
}

class BillingForm extends JFrame 
{
    private JTextField nameField, addressField, phoneField, emailField;
    private JTextField quantityField; 
    private JComboBox<String> productNameBox;
    private JTable productTable;
    private DefaultTableModel tableModel; 
    private JButton addBtn, saveBtn, resetBtn, closeBtn; 
    private JComboBox<String> paymentCombo; 
    private List<Product> selectedProducts = new ArrayList<>();

public BillingForm() {
    setTitle("Pharmacy Billing System");
    setSize(850, 600);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    // Buyer Panel
    JPanel buyerPanel = new JPanel(new GridLayout(2, 4, 10, 10));
    nameField = new JTextField(); addressField = new JTextField();
    phoneField = new JTextField(); emailField = new JTextField();
    buyerPanel.setBorder(BorderFactory.createTitledBorder("Buyer Details"));
    buyerPanel.add(new JLabel("Name:")); buyerPanel.add(nameField);
    buyerPanel.add(new JLabel("Phone:")); buyerPanel.add(phoneField);
    buyerPanel.add(new JLabel("Address:")); buyerPanel.add(addressField);
    buyerPanel.add(new JLabel("Email:")); buyerPanel.add(emailField);
    add(buyerPanel, BorderLayout.NORTH);

    // Product Input Panel
    JPanel productInputPanel = new JPanel();
    productNameBox = new JComboBox<>();
    productNameBox.setEditable(true);
    quantityField = new JTextField(5);
    addBtn = new JButton("Add ➕");
    productInputPanel.setBorder(BorderFactory.createTitledBorder("Add Product"));
    productInputPanel.add(new JLabel("Product Name:"));
    productInputPanel.add(productNameBox);
    productInputPanel.add(new JLabel("Quantity:"));
    productInputPanel.add(quantityField);
    productInputPanel.add(addBtn);
    add(productInputPanel, BorderLayout.WEST);

    // Table Setup
    tableModel = new DefaultTableModel(new Object[]{"Product Name", "Quantity", "Unit Price", "Total Price"}, 0);
    productTable = new JTable(tableModel);
    add(new JScrollPane(productTable), BorderLayout.CENTER);

    // Bottom Panel
    JPanel bottomPanel = new JPanel();
    saveBtn = new JButton("Save");
    resetBtn = new JButton("Reset");
    closeBtn = new JButton("Close");
    paymentCombo = new JComboBox<>(new String[]{"Select Payment Method", "Cash", "UPI", "Debit Card"});
    bottomPanel.add(saveBtn); bottomPanel.add(resetBtn); bottomPanel.add(closeBtn); bottomPanel.add(paymentCombo);
    add(bottomPanel, BorderLayout.SOUTH);

    // Action Listeners
    addBtn.addActionListener(e -> addProductToList());
    saveBtn.addActionListener(e -> generateBill());
    resetBtn.addActionListener(e -> resetForm());
    closeBtn.addActionListener(e -> System.exit(0));

    // Product Auto-suggest
    ((JTextField) productNameBox.getEditor().getEditorComponent()).addKeyListener(new KeyAdapter() {
        @Override
        public void keyReleased(KeyEvent e) {
            String input = ((JTextField) productNameBox.getEditor().getEditorComponent()).getText();
            fetchProductSuggestions(input);
        }
    });
}

private void fetchProductSuggestions(String input) {
    productNameBox.removeAllItems();
    try (Connection con = DBConnection.getConnection()) {
        PreparedStatement ps = con.prepareStatement("SELECT name FROM products WHERE name LIKE ?");
        ps.setString(1, input + "%");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            productNameBox.addItem(rs.getString("name"));
        }
        productNameBox.setPopupVisible(true);
    } catch (Exception e) {
        e.printStackTrace();
    }
}

private void addProductToList() {
    String productName = (String) productNameBox.getEditor().getItem();
    String qtyStr = quantityField.getText().trim();

    if (productName.isEmpty() || qtyStr.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please enter product name and quantity.");
        return;
    }

    try (Connection con = DBConnection.getConnection()) {
        PreparedStatement ps = con.prepareStatement("SELECT unit_price FROM products WHERE name = ?");
        ps.setString(1, productName);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            int qty = Integer.parseInt(qtyStr);
            double price = rs.getDouble("unit_price");
            double total = qty * price;
            selectedProducts.add(new Product(productName, qty, price));
            tableModel.addRow(new Object[]{productName, qty, price, total});
        } else {
            JOptionPane.showMessageDialog(this, "Product not found in database.");
        }

    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
    }
}

private void generateBill() {
    if (paymentCombo.getSelectedIndex() == 0) {
        JOptionPane.showMessageDialog(this, "Please select a payment method");
        return;
    }

    Buyer buyer = new Buyer(
            nameField.getText(),
            addressField.getText(),
            phoneField.getText(),
            emailField.getText()
    );

    Transaction t = new Transaction(
            String.valueOf(System.currentTimeMillis()),
            new java.util.Date(),
            paymentCombo.getSelectedItem().toString(),
            buyer,
            selectedProducts,
            ""
    );

    JFrame billFrame = new JFrame("Generated Bill");
    billFrame.setContentPane(new BillGeneratorPanel(t));
    billFrame.setSize(700, 600);
    billFrame.setLocationRelativeTo(null);
    billFrame.setVisible(true);
}

private void resetForm() {
    nameField.setText(""); addressField.setText("");
    phoneField.setText(""); emailField.setText("");
    productNameBox.setSelectedItem(""); quantityField.setText("");
    tableModel.setRowCount(0); selectedProducts.clear();
    paymentCombo.setSelectedIndex(0);
}

}