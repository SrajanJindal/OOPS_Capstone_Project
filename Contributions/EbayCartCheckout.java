/*# Ebay Cart and Checkout
This Java code creates a simple graphical user interface (GUI) for a marketplace shopping cart and checkout process. It simulates a user adding a single item to a cart and then proceeding to a checkout screen.

## Functionality
The application consists of two main screens, implemented using a `CardLayout`:

1.  Cart Screen:
    * Displays the item in the cart (labeled "Example Product Name").
    * Shows the seller ("Sold by: example\_seller") and the price (₹49.99).
    * Allows the user to adjust the quantity of the item using "+" and "-" buttons. The quantity defaults to 1.
    * Dynamically updates the "Subtotal" based on the quantity.
    * Provides a "Remove" button to set the quantity to 0 and the subtotal to ₹0.00.
    * Includes a "Save for later" button (functionality not implemented).
    * Has a "Proceed to Checkout" button that navigates the user to the Checkout screen.

2.  Checkout Screen:
    * Displays a "Checkout" title.
    * Shows a summary of the shipping address ("123 Main Street\nCity, State 12345") with an "Edit" button (functionality not implemented).
    * Shows a summary of the payment method ("Visa ending in 1234") with an "Edit" button (functionality not implemented).
    * Presents an "Item Total" and an "Order Total", both calculated based on the quantity and price of the item.
    * Has a "Place Order" button that displays a simple "Order placed successfully!" message using a `JOptionPane`.

## Structure

The code is organized into a single class, `EbayCartCheckout`, which extends `JFrame`. It utilizes various Swing components for creating the UI:

* `JFrame`: The main window of the application.
* `JPanel`: Used as containers to organize other components.
* `JLabel`: Displays text information like titles, product details, and prices.
* `JButton`: Interactive buttons for actions like increasing/decreasing quantity, proceeding to checkout, and placing the order.
* `BoxLayout`, `BorderLayout`, `GridLayout`, `FlowLayout`: Layout managers to arrange components within the panels.
* `CardLayout`: Manages the switching between the Cart and Checkout panels.
* `EmptyBorder`, `LineBorder`, `CompoundBorder`: Used for styling the appearance of panels and components.
* `Box.createRigidArea()`: Creates invisible components for adding spacing.
* `JOptionPane`: Used to display a simple message upon placing the order.

## How to Run

1.  Save the code as `EbayCartCheckout.java`.
2.  Compile the code using a Java compiler: `javac EbayCartCheckout.java`
3.  Run the compiled class: `java EbayCartCheckout`

This will open the GUI application, allowing you to interact with the simulated shopping cart and checkout process.

## Limitations

* This is a simplified simulation with a single hardcoded item.
* The "Edit" functionality for shipping address and payment method is not implemented.
* The "Save for later" functionality is not implemented.
* There is no actual order processing or data persistence.
* The UI design is basic and for demonstration purposes.*/


//updated and fixed
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EbayCartCheckout extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JLabel quantityLabel, subtotalLabel, itemTotalLabel, orderTotalLabel;
    private int quantity = 1;
    private final double price = 49.99;
    private boolean itemSavedForLater = false;

    private JPanel itemPanel;
    private JPanel savedForLaterPanel;
    private JButton addToCartButton;

    private JLabel addressLabel;
    private JLabel paymentLabel;

    public EbayCartCheckout() {
        setTitle("Ebay Cart and Checkout");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(650, 550);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createCartPanel(), "Cart");
        mainPanel.add(createCheckoutPanel(), "Checkout");

        add(mainPanel);
        setVisible(true);
    }

    private JPanel createCartPanel() {
        JPanel cartPanel = new JPanel(new BorderLayout(10, 10));
        cartPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        cartPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Shopping Cart");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        cartPanel.add(titleLabel, BorderLayout.NORTH);

        itemPanel = new JPanel();
        itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.Y_AXIS));
        itemPanel.setBorder(new CompoundBorder(new LineBorder(Color.GRAY), new EmptyBorder(10, 10, 10, 10)));
        itemPanel.setBackground(Color.WHITE);

        JLabel nameLabel = new JLabel("Example Product Name");
        JLabel sellerLabel = new JLabel("Sold by: example_seller");
        JLabel priceLabel = new JLabel("Price: ₹" + price);

        quantityLabel = new JLabel("Quantity: " + quantity);
        subtotalLabel = new JLabel("Subtotal: ₹" + String.format("%.2f", price * quantity));

        JButton plusButton = createButton("+");
        JButton minusButton = createButton("-");
        JButton removeButton = createButton("Remove");
        JButton saveButton = createButton("Save for later");
        JButton checkoutButton = createButton("Proceed to Checkout");

        plusButton.addActionListener(e -> updateQuantity(quantity + 1));
        minusButton.addActionListener(e -> {
            if (quantity > 1) updateQuantity(quantity - 1);
        });
        removeButton.addActionListener(e -> updateQuantity(0));

        saveButton.addActionListener(e -> {
            if (!itemSavedForLater) {
                itemSavedForLater = true;
                updateQuantity(0);
                itemPanel.setVisible(false);
                savedForLaterPanel.setVisible(true);
            }
        });

        checkoutButton.addActionListener(e -> cardLayout.show(mainPanel, "Checkout"));

        JPanel controls = new JPanel();
        controls.add(minusButton);
        controls.add(plusButton);
        controls.setBackground(Color.WHITE);

        itemPanel.add(nameLabel);
        itemPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        itemPanel.add(sellerLabel);
        itemPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        itemPanel.add(priceLabel);
        itemPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        itemPanel.add(quantityLabel);
        itemPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        itemPanel.add(controls);
        itemPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        itemPanel.add(subtotalLabel);
        itemPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        itemPanel.add(removeButton);
        itemPanel.add(saveButton);

        // Save for later panel
        savedForLaterPanel = new JPanel();
        savedForLaterPanel.setLayout(new BoxLayout(savedForLaterPanel, BoxLayout.Y_AXIS));
        savedForLaterPanel.setBorder(new CompoundBorder(new LineBorder(Color.LIGHT_GRAY), new EmptyBorder(10, 10, 10, 10)));
        savedForLaterPanel.setBackground(Color.WHITE);
        savedForLaterPanel.setVisible(false);

        JLabel savedLabel = new JLabel("Saved for Later:");
        JLabel savedItemLabel = new JLabel("Example Product Name - ₹" + price);

        addToCartButton = createButton("Add to Cart");
        addToCartButton.addActionListener(e -> {
            itemSavedForLater = false;
            quantity = 1;
            updateQuantity(quantity);
            savedForLaterPanel.setVisible(false);
            itemPanel.setVisible(true);
        });

        savedForLaterPanel.add(savedLabel);
        savedForLaterPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        savedForLaterPanel.add(savedItemLabel);
        savedForLaterPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        savedForLaterPanel.add(addToCartButton);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(itemPanel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(savedForLaterPanel);

        cartPanel.add(centerPanel, BorderLayout.CENTER);
        cartPanel.add(checkoutButton, BorderLayout.SOUTH);

        return cartPanel;
    }

    private JPanel createCheckoutPanel() {
        JPanel checkoutPanel = new JPanel(new BorderLayout(10, 10));
        checkoutPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        checkoutPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Checkout");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        checkoutPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);

        // Shipping Address Panel
        JPanel addressPanel = new JPanel(new BorderLayout());
        addressPanel.setBackground(Color.WHITE);
        addressLabel = new JLabel("<html><b>Shipping Address:</b><br>123 Main Street<br>City, State 12345</html>");
        JButton editAddressButton = createButton("Edit");
        editAddressButton.addActionListener(e -> {
            String newAddress = JOptionPane.showInputDialog(this, "Enter new shipping address:");
            if (newAddress != null && !newAddress.trim().isEmpty()) {
                addressLabel.setText("<html><b>Shipping Address:</b><br>" + newAddress + "</html>");
            }
        });
        addressPanel.add(addressLabel, BorderLayout.CENTER);
        addressPanel.add(editAddressButton, BorderLayout.EAST);

        // Payment Method Panel
        JPanel paymentPanel = new JPanel(new BorderLayout());
        paymentPanel.setBackground(Color.WHITE);
        paymentLabel = new JLabel("<html><b>Payment Method:</b><br>Visa ending in 1234</html>");
        JButton editPaymentButton = createButton("Edit");
        editPaymentButton.addActionListener(e -> {
            String newPayment = JOptionPane.showInputDialog(this, "Enter new payment method:");
            if (newPayment != null && !newPayment.trim().isEmpty()) {
                paymentLabel.setText("<html><b>Payment Method:</b><br>" + newPayment + "</html>");
            }
        });
        paymentPanel.add(paymentLabel, BorderLayout.CENTER);
        paymentPanel.add(editPaymentButton, BorderLayout.EAST);

        itemTotalLabel = new JLabel("Item Total: ₹" + String.format("%.2f", price * quantity));
        orderTotalLabel = new JLabel("Order Total: ₹" + String.format("%.2f", price * quantity));

        JButton placeOrderButton = createButton("Place Order");
        placeOrderButton.addActionListener(e -> {
            double total = price * quantity;
            if (total <= 0.0) {
                JOptionPane.showMessageDialog(this, "Cannot place an order with ₹0.00 total.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                // Order processing - Save the order details to a file
                saveOrderDetails();
                JOptionPane.showMessageDialog(this, "Order placed successfully!");
            }
        });

        JButton homeButton = createButton("Home");
        homeButton.addActionListener(e -> cardLayout.show(mainPanel, "Cart"));

        centerPanel.add(addressPanel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(paymentPanel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(itemTotalLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        centerPanel.add(orderTotalLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(placeOrderButton);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(homeButton);

        checkoutPanel.add(centerPanel, BorderLayout.CENTER);

        return checkoutPanel;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(Color.BLUE);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 40));
        return button;
    }

    private void updateQuantity(int newQuantity) {
        this.quantity = newQuantity;
        quantityLabel.setText("Quantity: " + quantity);
        subtotalLabel.setText("Subtotal: ₹" + String.format("%.2f", price * quantity));
        itemTotalLabel.setText("Item Total: ₹" + String.format("%.2f", price * quantity));
        orderTotalLabel.setText("Order Total: ₹" + String.format("%.2f", price * quantity));
    }

    private void saveOrderDetails() {
        // Prepare the order details to save
        String orderDetails = "Order Date: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "\n";
        orderDetails += "Item: Example Product Name\n";
        orderDetails += "Quantity: " + quantity + "\n";
        orderDetails += "Subtotal: ₹" + String.format("%.2f", price * quantity) + "\n";
        orderDetails += "Shipping Address: " + addressLabel.getText() + "\n";
        orderDetails += "Payment Method: " + paymentLabel.getText() + "\n";
        orderDetails += "---------------------------------------------\n\n";

        // Write the order details to a file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("orders.txt", true))) {
            writer.write(orderDetails);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving the order.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EbayCartCheckout::new);
    }
}
