What This Swing App Does:
Collects order details using input fields.

Accepts comma-separated product IDs.

On clicking Place Order, it:

Creates an Order object.
Creates Product entries.
Shows confirmation.


code- 
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

class Order {
    String userId, stripeId, name, address, zipcode, city, country;
    double total;
    List<Product> products;

    public Order(String userId, String stripeId, String name, String address, String zipcode,
                 String city, String country, double total, List<Product> products) {
        this.userId = userId;
        this.stripeId = stripeId;
        this.name = name;
        this.address = address;
        this.zipcode = zipcode;
        this.city = city;
        this.country = country;
        this.total = total;
        this.products = products;
    }
}

class Product {
    int id;
    String name;
    public Product(int id, String name) {
        this.id = id;
        this.name = name;
    }
}

public class OrderFormSwing extends JFrame {
    private JTextField nameField, addressField, zipField, cityField, countryField, stripeField;
    private JTextField totalField;
    private JTextArea productArea;

    public OrderFormSwing() {
        setTitle("Place Order");
        setSize(400, 500);
        setLayout(new GridLayout(10, 1));

        stripeField = new JTextField();
        nameField = new JTextField();
        addressField = new JTextField();
        zipField = new JTextField();
        cityField = new JTextField();
        countryField = new JTextField();
        totalField = new JTextField();
        productArea = new JTextArea(3, 20);

        add(new JLabel("Stripe ID:"));
        add(stripeField);
        add(new JLabel("Name:"));
        add(nameField);
        add(new JLabel("Address:"));
        add(addressField);
        add(new JLabel("Zipcode:"));
        add(zipField);
        add(new JLabel("City:"));
        add(cityField);
        add(new JLabel("Country:"));
        add(countryField);
        add(new JLabel("Total (₹):"));
        add(totalField);
        add(new JLabel("Product IDs (comma separated):"));
        add(new JScrollPane(productArea));

        JButton placeOrderBtn = new JButton("Place Order");
        add(placeOrderBtn);

        placeOrderBtn.addActionListener(e -> {
            String userId = "user123"; // simulate Supabase user
            String stripeId = stripeField.getText();
            String name = nameField.getText();
            String addr = addressField.getText();
            String zip = zipField.getText();
            String city = cityField.getText();
            String country = countryField.getText();
            double total = Double.parseDouble(totalField.getText());

            String[] productIds = productArea.getText().split(",");
            List<Product> products = new ArrayList<>();
            for (String pid : productIds) {
                int id = Integer.parseInt(pid.trim());
                products.add(new Product(id, "Product#" + id)); // dummy names
            }

            Order order = new Order(userId, stripeId, name, addr, zip, city, country, total, products);

            // You can later store this order to a database or a file
            JOptionPane.showMessageDialog(this, "✅ Order Placed for " + name + " with " + products.size() + " items.");
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(OrderFormSwing::new);
    }
}
