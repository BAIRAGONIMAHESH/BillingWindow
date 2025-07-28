
package loginapp;



import java.sql.*;
import java.util.*;
import java.util.Date;

public class TransactionLoader { public static Transaction loadTransaction(int transactionId) throws SQLException { Connection con = DBConnection.getConnection();

PreparedStatement stmt = con.prepareStatement("SELECT * FROM transactions WHERE id=?");
    stmt.setInt(1, transactionId);
    ResultSet rs = stmt.executeQuery();

    Transaction t = null;
    if (rs.next()) {
        int buyerId = rs.getInt("buyer_id");
        String method = rs.getString("payment_method");
        Date date = rs.getDate("date");
        String patientName = rs.getString("patient_name");

        Buyer buyer = loadBuyer(buyerId);
        List<Product> products = loadProducts(transactionId);
        t = new Transaction(String.valueOf(transactionId), date, method, buyer, products, patientName);
    }

    con.close();
    return t;
}

private static Buyer loadBuyer(int id) throws SQLException {
    Connection con = DBConnection.getConnection();
    PreparedStatement stmt = con.prepareStatement("SELECT * FROM buyers WHERE id=?");
    stmt.setInt(1, id);
    ResultSet rs = stmt.executeQuery();

    Buyer b = null;
    if (rs.next()) {
        b = new Buyer(rs.getString("name"), rs.getString("address"),
                      rs.getString("contact"), rs.getString("email"));
    }

    con.close();
    return b;
}

private static List<Product> loadProducts(int transactionId) throws SQLException {
    Connection con = DBConnection.getConnection();
    List<Product> list = new ArrayList<>();

    String sql = "SELECT p.name, p.unit_price, ti.quantity FROM transaction_items ti " +
                 "JOIN products p ON ti.product_id = p.id WHERE ti.transaction_id=?";
    PreparedStatement stmt = con.prepareStatement(sql);
    stmt.setInt(1, transactionId);
    ResultSet rs = stmt.executeQuery();

    while (rs.next()) {
        list.add(new Product(rs.getString("name"),
                             rs.getInt("quantity"),
                             rs.getDouble("unit_price")));
    }

    con.close();
    return list;
}

}
