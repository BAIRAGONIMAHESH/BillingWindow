package loginapp;

public class Buyer {
    private String name, address, contact, email;

    public Buyer(String name, String address, String contact, String email) {
        this.name = name; this.address = address;
        this.contact = contact; this.email = email;
    }

    // Getters
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getContact() { return contact; }
    public String getEmail() { return email; }

//    String getPhone() {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
//    }
}
