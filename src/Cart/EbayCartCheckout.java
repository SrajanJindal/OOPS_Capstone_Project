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



import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class EbayCartCheckout extends JFrame {
    CardLayout cardLayout;
    JPanel mainPanel;
    JLabel quantityLabel;
    JLabel subtotalLabel;
    int quantity = 1;
    double price = 49.99;

    JLabel itemTotalLabel;
    JLabel orderTotalLabel;

    public EbayCartCheckout() {
        setTitle("Marketplace Cart and Checkout");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 600);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createCartPanel(), "Cart");
        mainPanel.add(createCheckoutPanel(), "Checkout");

        add(mainPanel);
        setVisible(true);
    }

    private JPanel createCartPanel() {
        JPanel cartPanel = new JPanel(new BorderLayout());
        cartPanel.setBackground(Color.decode("#f5f5f5"));

        cartPanel.add(createHeader(), BorderLayout.NORTH);

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBackground(Color.decode("#f5f5f5"));
        center.setBorder(new EmptyBorder(20, 50, 20, 50));

        JLabel title = new JLabel("Your Shopping Cart");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        center.add(title);
        center.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel productCard = createProductCard();
        center.add(productCard);
        center.add(Box.createRigidArea(new Dimension(0, 30)));

        subtotalLabel = new JLabel("Subtotal (" + quantity + " item): ₹" + String.format("%.2f", price * quantity));
        subtotalLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        subtotalLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        center.add(subtotalLabel);

        center.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton checkoutButton = createPrimaryButton("Proceed to Checkout");
        checkoutButton.addActionListener(e -> {
            updateCheckout();
            cardLayout.show(mainPanel, "Checkout");
        });

        center.add(checkoutButton);

        cartPanel.add(center, BorderLayout.CENTER);
        return cartPanel;
    }

    private JPanel createProductCard() {
        JPanel card = new JPanel(new BorderLayout(20, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                new EmptyBorder(15, 15, 15, 15)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);

        JLabel productName = new JLabel("Example Product Name");
        productName.setFont(new Font("SansSerif", Font.BOLD, 18));

        JLabel seller = new JLabel("Sold by: example_seller");
        seller.setFont(new Font("SansSerif", Font.PLAIN, 14));
        seller.setForeground(Color.GRAY);

        JLabel priceLabel = new JLabel("₹" + price);
        priceLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        priceLabel.setForeground(new Color(0, 102, 255));

        infoPanel.add(productName);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(seller);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        infoPanel.add(priceLabel);

        JPanel qtyPanel = new JPanel();
        qtyPanel.setBackground(Color.WHITE);

        JButton minusButton = createCircleButton("-");
        JButton plusButton = createCircleButton("+");

        quantityLabel = new JLabel(String.valueOf(quantity), JLabel.CENTER);
        quantityLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));

        minusButton.addActionListener(e -> updateQuantity(-1));
        plusButton.addActionListener(e -> updateQuantity(1));

        qtyPanel.add(minusButton);
        qtyPanel.add(quantityLabel);
        qtyPanel.add(plusButton);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        JButton removeButton = createFlatButton("Remove");
        JButton saveLaterButton = createFlatButton("Save for later");

        removeButton.addActionListener(e -> {
            quantity = 0;
            quantityLabel.setText("0");
            subtotalLabel.setText("Subtotal: ₹0.00");
        });

        buttonPanel.add(removeButton);
        buttonPanel.add(saveLaterButton);

        card.add(infoPanel, BorderLayout.WEST);
        card.add(qtyPanel, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createCheckoutPanel() {
        JPanel checkoutPanel = new JPanel(new BorderLayout());
        checkoutPanel.setBackground(Color.decode("#f5f5f5"));
        checkoutPanel.add(createHeader(), BorderLayout.NORTH);

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBackground(Color.decode("#f5f5f5"));
        center.setBorder(new EmptyBorder(20, 50, 20, 50));

        JLabel title = new JLabel("Checkout");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        center.add(title);
        center.add(Box.createRigidArea(new Dimension(0, 20)));

        center.add(createInfoCard("Shipping Address", "123 Main Street\nCity, State 12345", "Edit"));
        center.add(Box.createRigidArea(new Dimension(20, 20)));
        center.add(createInfoCard("Payment Method", "Visa ending in 1234", "Edit"));
        center.add(Box.createRigidArea(new Dimension(20, 20)));

        JPanel summary = new JPanel(new GridLayout(3, 2, 10, 10));
        summary.setBackground(Color.WHITE);
        summary.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                new EmptyBorder(15, 15, 15, 15)
        ));
        summary.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        summary.add(new JLabel("Example Product"));
        summary.add(new JLabel(""));

        summary.add(new JLabel("Item Total"));
        itemTotalLabel = new JLabel("₹" + String.format("%.2f", price * quantity));
        itemTotalLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        summary.add(itemTotalLabel);

        summary.add(new JLabel("Order Total"));
        orderTotalLabel = new JLabel("₹" + String.format("%.2f", price * quantity));
        orderTotalLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        summary.add(orderTotalLabel);

        center.add(summary);
        center.add(Box.createRigidArea(new Dimension(0, 30)));

        JButton placeOrderButton = createPrimaryButton("Place Order");
        placeOrderButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Order placed successfully!"));

        center.add(placeOrderButton);
        checkoutPanel.add(center, BorderLayout.CENTER);

        return checkoutPanel;
    }

    private JPanel createInfoCard(String title, String detail, String buttonText) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                new EmptyBorder(15, 15, 15, 15)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        JLabel detailLabel = new JLabel("<html>" + detail.replace("\n", "<br>") + "</html>");
        detailLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        detailLabel.setForeground(Color.GRAY);

        textPanel.add(titleLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        textPanel.add(detailLabel);

        JButton editButton = createFlatButton(buttonText);

        card.add(textPanel, BorderLayout.CENTER);
        card.add(editButton, BorderLayout.EAST);

        return card;
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel logo = new JLabel("Marketplace");
        logo.setFont(new Font("SansSerif", Font.BOLD, 24));
        logo.setForeground(new Color(255, 0, 0));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);

        JButton homeButton = createPrimaryButtonSmall("Home");
        JButton cartButton = createPrimaryButtonSmall("Cart");

        homeButton.addActionListener(e -> cardLayout.show(mainPanel, "Cart"));
        cartButton.addActionListener(e -> cardLayout.show(mainPanel, "Cart"));

        buttonPanel.add(homeButton);
        buttonPanel.add(cartButton);

        header.add(logo, BorderLayout.WEST);
        header.add(buttonPanel, BorderLayout.EAST);

        return header;
    }

    private void updateQuantity(int delta) {
        quantity += delta;
        if (quantity < 1) quantity = 1;
        quantityLabel.setText(String.valueOf(quantity));
        subtotalLabel.setText("Subtotal (" + quantity + " item): ₹" + String.format("%.2f", price * quantity));
    }

    private void updateCheckout() {
        itemTotalLabel.setText("₹" + String.format("%.2f", price * quantity));
        orderTotalLabel.setText("₹" + String.format("%.2f", price * quantity));
    }

    private JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setBackground(new Color(0, 102, 255));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setPreferredSize(new Dimension(200, 40));
        return button;
    }

    private JButton createPrimaryButtonSmall(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.PLAIN, 14));
        button.setBackground(new Color(0, 102, 255));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        return button;
    }

    private JButton createFlatButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.PLAIN, 14));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        return button;
    }

    private JButton createCircleButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(40, 40));
        button.setFocusPainted(false);
        button.setBackground(Color.LIGHT_GRAY);
        button.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EbayCartCheckout::new);
    }
}
