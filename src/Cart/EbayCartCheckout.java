/*
# Marketplace Cart and Checkout Application
This Java Swing application provides a basic marketplace cart and checkout functionality. Users can select products, add them to a cart, modify the cart, and proceed to a simplified checkout process.

## Features
* Product Selection: A dropdown menu allows users to choose from a list of available products, each with a name and price.
* Product Image Display: Although currently displaying placeholder text, this area is intended to show an image of the selected product.
* Add to Cart: Users can add the selected product to their shopping cart.
* Shopping Cart:
    * Displays the selected product.
    * Allows users to adjust the quantity of the item using "+" and "-" buttons.
    * Provides "Remove" and "Save for later" actions (the "Save for later" functionality is basic and uses a `JOptionPane`).
    * Includes a checkbox for "Gift wrap this item" with an additional cost.
    * Offers a text area for "Special Instructions".
    * Shows the subtotal based on the quantity and price.
    * A "Proceed to checkout" button navigates to the checkout panel.
* Checkout:
    * Displays a progress bar (currently static).
    * Shows a placeholder for the shipping address with an "Edit" button (which uses a `JOptionPane` for input).
    * Shows a placeholder for the payment method with an "Edit" button (which uses a `JOptionPane` for input).
    * Provides a dropdown to select a shipping method (with placeholder costs).
    * Displays an "Order summary" including the item name, item total, and order total (which updates based on quantity and gift wrap).
    * A "Place order" button saves basic order details to a file named `orders.txt` and shows a confirmation message.
* Header: Displays a "Marketplace" logo and navigation buttons for "Home" (back to product selection) and "Cart".
* Footer: Shows a copyright notice.

## How to Run
1. Save the code: Save the provided Java code as `MarketplaceCartCheckout.java`.
2. Compile: Open a terminal or command prompt in the directory where you saved the file and compile the code using the Java compiler:
    ```bash
    javac MarketplaceCartCheckout.java
    ```
3. Execute: After successful compilation, run the application using the Java Virtual Machine:
    ```bash
    java MarketplaceCartCheckout
    ```
    This will open the Marketplace Cart and Checkout application window.

## Notes
* This is a basic implementation and lacks many features of a real-world e-commerce application.
* Product images are not actually loaded; the application displays placeholder text for images.
* The "Save for later" functionality simply adds the item name to a message dialog.
* Shipping costs are placeholders and not dynamically calculated based on the selected method.
* The checkout process is simplified, and no actual payment processing or address validation is implemented.
* Order details are saved to a plain text file (`orders.txt`) in the same directory where the application is run.
* The application uses a `CardLayout` to switch between different panels (Product Selection, Cart, and Checkout).

## Potential Enhancements
* Implement actual loading and display of product images.
* Enhance the "Save for later" functionality, perhaps by displaying saved items in a list.
* Implement dynamic calculation of shipping costs based on the selected method and potentially the shipping address.
* Add input fields for shipping address and payment details in the checkout panel instead of using `JOptionPane`.
* Integrate with a data source (e.g., a database or CSV file) to manage products and prices.
* Implement more robust error handling and input validation.
* Add unit tests to ensure the application's functionality.
* Improve the user interface and user experience with more sophisticated layout managers and styling.
* Implement a more realistic order processing and storage mechanism.
*/
   
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class MarketplaceCartCheckout extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JLabel quantityLabel, subtotalLabel, itemTotalLabel, orderTotalLabel;
    private int quantity = 1;
    private double price = 49.99;
    private boolean itemSavedForLater = false;
    
    // Enhanced components
    private JComboBox<String> productDropdown;
    private JLabel productImageLabel;
    private JTextArea specialInstructions;
    private JCheckBox giftWrapCheckbox;
    private JLabel shippingMethodLabel;
    private JProgressBar progressBar;
    private DefaultListModel<String> savedItemsModel;
    private JList<String> savedItemsList;

    public MarketplaceCartCheckout() {
        setTitle("Marketplace Cart and Checkout");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createProductSelectionPanel(), "Products");
        mainPanel.add(createCartPanel(), "Cart");
        mainPanel.add(createCheckoutPanel(), "Checkout");

        add(createHeader(), BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(createFooter(), BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel logo = new JLabel("Marketplace");
        logo.setFont(new Font("Arial", Font.BOLD, 24));
        logo.setForeground(new Color(0, 102, 255));

        JButton homeButton = new JButton("Home");
        homeButton.addActionListener(e -> cardLayout.show(mainPanel, "Products"));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.add(homeButton);
        buttonPanel.add(createCartButton());

        header.add(logo, BorderLayout.WEST);
        header.add(buttonPanel, BorderLayout.EAST);
        return header;
    }

    private JButton createCartButton() {
        JButton cartButton = new JButton("Cart");
        cartButton.setFont(new Font("Arial", Font.PLAIN, 14));
        cartButton.setBackground(new Color(0, 102, 255));
        cartButton.setForeground(Color.WHITE);
        cartButton.setFocusPainted(false);
        cartButton.addActionListener(e -> {
            updateCart();
            cardLayout.show(mainPanel, "Cart");
        });
        return cartButton;
    }

    private JPanel createProductSelectionPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        // Product selection dropdown
        String[] products = {"Smartphone - $499.99", "Laptop - $899.99", "Headphones - $99.99", "Example Product - $49.99"};
        productDropdown = new JComboBox<>(products);
        productDropdown.addActionListener(e -> updateProductDisplay());

        // Product image display
        productImageLabel = new JLabel("", JLabel.CENTER);
        productImageLabel.setPreferredSize(new Dimension(300, 200));
        productImageLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        // Add to cart button
        JButton addToCartButton = new JButton("Add to Cart");
        addToCartButton.addActionListener(e -> {
            String selected = (String)productDropdown.getSelectedItem();
            price = Double.parseDouble(selected.split("\\$")[1]);
            updateCart();
            cardLayout.show(mainPanel, "Cart");
        });

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(productDropdown);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerPanel.add(productImageLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerPanel.add(addToCartButton);

        panel.add(centerPanel, BorderLayout.CENTER);
        return panel;
    }

    private void updateProductDisplay() {
        // In a real app, you would load actual product images here
        productImageLabel.setText("Image: " + productDropdown.getSelectedItem());
    }

    private void updateCart() {
        // Update cart quantities and totals
        if (quantityLabel != null) {
            quantityLabel.setText(String.valueOf(quantity));
        }
        if (subtotalLabel != null) {
            subtotalLabel.setText("Subtotal (" + quantity + " item): $" + String.format("%.2f", price * quantity));
        }
    }

    private JPanel createCartPanel() {
        JPanel cartPanel = new JPanel();
        cartPanel.setLayout(new BoxLayout(cartPanel, BoxLayout.Y_AXIS));
        cartPanel.setBackground(Color.WHITE);
        cartPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Title
        JLabel title = new JLabel("Shopping Cart");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        cartPanel.add(title);
        cartPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Product Item
        JPanel productPanel = new JPanel();
        productPanel.setLayout(new BoxLayout(productPanel, BoxLayout.Y_AXIS));
        productPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        productPanel.setBackground(Color.WHITE);
        productPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

        JLabel productName = new JLabel((String)productDropdown.getSelectedItem());
        productName.setFont(new Font("Arial", Font.PLAIN, 16));

        // Quantity controls
        JPanel quantityPanel = new JPanel();
        quantityPanel.setBackground(Color.WHITE);
        quantityPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        quantityLabel = new JLabel(String.valueOf(quantity));
        JButton minusButton = createQuantityButton("-");
        JButton plusButton = createQuantityButton("+");

        minusButton.addActionListener(e -> {
            if (quantity > 1) {
                quantity--;
                updateCart();
            }
        });
        plusButton.addActionListener(e -> {
            quantity++;
            updateCart();
        });

        quantityPanel.add(minusButton);
        quantityPanel.add(quantityLabel);
        quantityPanel.add(plusButton);

        // Action buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        actionPanel.setBackground(Color.WHITE);
        actionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton removeButton = createActionButton("Remove");
        JButton saveButton = createActionButton("Save for later");

        removeButton.addActionListener(e -> {
            quantity = 0;
            updateCart();
        });
        saveButton.addActionListener(e -> saveForLater());

        actionPanel.add(removeButton);
        actionPanel.add(saveButton);

        // Gift wrap checkbox
        giftWrapCheckbox = new JCheckBox("Gift wrap this item (+$5.00)");
        giftWrapCheckbox.addActionListener(e -> updateOrderTotal());

        // Special instructions
        specialInstructions = new JTextArea(3, 20);
        specialInstructions.setBorder(BorderFactory.createTitledBorder("Special Instructions"));

        productPanel.add(productName);
        productPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        productPanel.add(quantityPanel);
        productPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        productPanel.add(actionPanel);
        productPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        productPanel.add(giftWrapCheckbox);
        productPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        productPanel.add(specialInstructions);
        productPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        cartPanel.add(productPanel);

        // Subtotal
        subtotalLabel = new JLabel("Subtotal (" + quantity + " item): $" + String.format("%.2f", price * quantity));
        subtotalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        subtotalLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        cartPanel.add(subtotalLabel);
        cartPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Checkout button
        JButton checkoutButton = new JButton("Proceed to checkout");
        checkoutButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        checkoutButton.setFont(new Font("Arial", Font.BOLD, 16));
        checkoutButton.setBackground(new Color(0, 102, 255));
        checkoutButton.setForeground(Color.WHITE);
        checkoutButton.setFocusPainted(false);
        checkoutButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        checkoutButton.addActionListener(e -> {
            updateCheckout();
            cardLayout.show(mainPanel, "Checkout");
        });

        cartPanel.add(checkoutButton);
        return cartPanel;
    }

    private JPanel createCheckoutPanel() {
        JPanel checkoutPanel = new JPanel();
        checkoutPanel.setLayout(new BoxLayout(checkoutPanel, BoxLayout.Y_AXIS));
        checkoutPanel.setBackground(Color.WHITE);
        checkoutPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Title
        JLabel title = new JLabel("Checkout");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        checkoutPanel.add(title);
        checkoutPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Progress bar
        progressBar = new JProgressBar(0, 3);
        progressBar.setValue(1);
        progressBar.setStringPainted(true);
        progressBar.setString("Step 1 of 3");
        checkoutPanel.add(progressBar);
        checkoutPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Shipping Address
        JPanel addressPanel = new JPanel();
        addressPanel.setLayout(new BoxLayout(addressPanel, BoxLayout.Y_AXIS));
        addressPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        addressPanel.setBackground(Color.WHITE);
        addressPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

        JLabel addressTitle = new JLabel("Shipping address");
        addressTitle.setFont(new Font("Arial", Font.BOLD, 16));

        JLabel addressLabel = new JLabel("123 Main Street\nCity, State 12345");
        addressLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton editAddressButton = createEditButton();
        editAddressButton.addActionListener(e -> {
            String newAddress = JOptionPane.showInputDialog(this, "Enter new shipping address:");
            if (newAddress != null && !newAddress.trim().isEmpty()) {
                addressLabel.setText(newAddress);
            }
        });

        addressPanel.add(addressTitle);
        addressPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        addressPanel.add(addressLabel);
        addressPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        addressPanel.add(editAddressButton);
        addressPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        checkoutPanel.add(addressPanel);

        // Payment Method
        JPanel paymentPanel = new JPanel();
        paymentPanel.setLayout(new BoxLayout(paymentPanel, BoxLayout.Y_AXIS));
        paymentPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        paymentPanel.setBackground(Color.WHITE);
        paymentPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

        JLabel paymentTitle = new JLabel("Payment method");
        paymentTitle.setFont(new Font("Arial", Font.BOLD, 16));

        JLabel paymentLabel = new JLabel("Visa ending in 1234");
        paymentLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton editPaymentButton = createEditButton();
        editPaymentButton.addActionListener(e -> {
            String newPayment = JOptionPane.showInputDialog(this, "Enter new payment method:");
            if (newPayment != null && !newPayment.trim().isEmpty()) {
                paymentLabel.setText(newPayment);
            }
        });

        paymentPanel.add(paymentTitle);
        paymentPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        paymentPanel.add(paymentLabel);
        paymentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        paymentPanel.add(editPaymentButton);
        paymentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        checkoutPanel.add(paymentPanel);

        // Shipping Method
        JPanel shippingPanel = new JPanel();
        shippingPanel.setLayout(new BoxLayout(shippingPanel, BoxLayout.Y_AXIS));
        shippingPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        shippingPanel.setBackground(Color.WHITE);
        shippingPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

        JLabel shippingTitle = new JLabel("Shipping method");
        shippingTitle.setFont(new Font("Arial", Font.BOLD, 16));

        String[] shippingMethods = {"Standard (Free)", "Express ($9.99)", "Overnight ($19.99)"};
        JComboBox<String> shippingCombo = new JComboBox<>(shippingMethods);
        shippingCombo.addActionListener(e -> updateOrderTotal());

        shippingPanel.add(shippingTitle);
        shippingPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        shippingPanel.add(shippingCombo);
        shippingPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        checkoutPanel.add(shippingPanel);

        // Order Summary
        JPanel summaryPanel = new JPanel();
        summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.Y_AXIS));
        summaryPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        summaryPanel.setBackground(Color.WHITE);
        summaryPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

        JLabel summaryTitle = new JLabel("Order summary");
        summaryTitle.setFont(new Font("Arial", Font.BOLD, 16));

        JLabel productLabel = new JLabel((String)productDropdown.getSelectedItem());
        productLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        itemTotalLabel = new JLabel("Item total: $" + String.format("%.2f", price * quantity));
        orderTotalLabel = new JLabel("Order total: $" + String.format("%.2f", price * quantity));
        orderTotalLabel.setFont(new Font("Arial", Font.BOLD, 16));

        summaryPanel.add(summaryTitle);
        summaryPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        summaryPanel.add(productLabel);
        summaryPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        summaryPanel.add(itemTotalLabel);
        summaryPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        summaryPanel.add(orderTotalLabel);
        summaryPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        checkoutPanel.add(summaryPanel);

        // Place Order button
        JButton placeOrderButton = new JButton("Place order");
        placeOrderButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        placeOrderButton.setFont(new Font("Arial", Font.BOLD, 16));
        placeOrderButton.setBackground(new Color(0, 102, 255));
        placeOrderButton.setForeground(Color.WHITE);
        placeOrderButton.setFocusPainted(false);
        placeOrderButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        placeOrderButton.addActionListener(e -> {
            double total = price * quantity;
            if (total <= 0.0) {
                JOptionPane.showMessageDialog(this, "Cannot place an order with $0.00 total.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                saveOrderDetails();
                JOptionPane.showMessageDialog(this, "Order placed successfully!");
            }
        });

        checkoutPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        checkoutPanel.add(placeOrderButton);

        return checkoutPanel;
    }

    private JButton createQuantityButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setPreferredSize(new Dimension(30, 30));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        return button;
    }

    private JButton createActionButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        button.setForeground(new Color(0, 102, 255));
        return button;
    }

    private JButton createEditButton() {
        JButton button = new JButton("Edit");
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        button.setForeground(new Color(0, 102, 255));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        return button;
    }

    private void updateOrderTotal() {
        double total = price * quantity;
        
        if (giftWrapCheckbox.isSelected()) {
            total += 5.00;
        }
        
        // Update shipping cost based on selection
        // (You would add this logic when implementing shipping options)
        
        if (orderTotalLabel != null) {
            orderTotalLabel.setText("Order total: $" + String.format("%.2f", total));
        }
    }

    private void updateCheckout() {
        if (itemTotalLabel != null) {
            itemTotalLabel.setText("Item total: $" + String.format("%.2f", price * quantity));
        }
        if (orderTotalLabel != null) {
            updateOrderTotal();
        }
    }

    private void saveForLater() {
        if (savedItemsModel == null) {
            savedItemsModel = new DefaultListModel<>();
        }
        savedItemsModel.addElement("Saved: " + productDropdown.getSelectedItem());
        JOptionPane.showMessageDialog(this, "Item saved for later!");
    }

    private void saveOrderDetails() {
        String orderDetails = "Order Date: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "\n";
        orderDetails += "Item: " + productDropdown.getSelectedItem() + "\n";
        orderDetails += "Quantity: " + quantity + "\n";
        orderDetails += "Total: $" + String.format("%.2f", price * quantity) + "\n";
        orderDetails += "Gift Wrap: " + (giftWrapCheckbox.isSelected() ? "Yes" : "No") + "\n";
        orderDetails += "Special Instructions: " + specialInstructions.getText() + "\n";
        orderDetails += "---------------------------------------------\n\n";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("orders.txt", true))) {
            writer.write(orderDetails);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving the order.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createFooter() {
        JPanel footer = new JPanel();
        footer.setBorder(new EmptyBorder(10, 20, 10, 20));
        footer.setBackground(Color.LIGHT_GRAY);
        
        JLabel statusLabel = new JLabel("Marketplace © 2023");
        footer.add(statusLabel);
        
        return footer;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MarketplaceCartCheckout());
    }
}
