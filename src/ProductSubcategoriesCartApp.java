/*# Product SUb Categories Cart
This Java Swing application provides a simple two-page interface for browsing products and managing a shopping cart.

## Functionality
The application consists of two main screens, navigated using a `CardLayout`:

1.  Product Page:
    * Displays a table of available products with the following columns:
        * Category: The category of the product (e.g., Electronics, Clothing).
        * Product: The name of the product.
        * Price: The price of the product.
        * Action: A button labeled "Add to Cart" for each product.
    * Clicking the "Add to Cart" button for a product:
        * Adds the selected product (name and price) to an in-memory shopping cart.
        * Displays a confirmation message (e.g., "Wireless Headphones added to cart!").
        * Updates the cart count displayed on the "View Cart" button.
    * Includes a "View Cart" button at the bottom right. Clicking this button navigates the user to the Cart Page.
2.  Cart Page:
    * Displays a table of items currently in the shopping cart with the following columns:
        * Product: The name of the product added to the cart.
        * Price: The price of the product.
    * Includes a "Continue Shopping" button that navigates the user back to the Product Page.
    * Includes a "Proceed to Checkout" button. Clicking this button:
        * If the cart is empty, displays a warning message.
        * If the cart contains items, simulates a successful order placement with a thank you message.
        * Clears the shopping cart.
        * Updates the cart count on the "View Cart" button back to zero.
        * Navigates the user back to the Product Page.

## Structure
The application is built using Java Swing components:

* `JFrame`: The main window of the application.
* `JPanel`: Used as containers to organize other components.
* `JLabel`: Displays text information like titles.
* `JButton`: Interactive buttons for "Add to Cart", "View Cart", "Continue Shopping", and "Proceed to Checkout".
* `JTable`: Displays the list of products and the items in the shopping cart.
* `DefaultTableModel`: Manages the data displayed in the `JTable` components.
* `JScrollPane`: Provides scrollability for the tables if the content exceeds the display area.
* `CardLayout`: Manages the switching between the Product and Cart pages.
* `BorderLayout`, `FlowLayout`: Layout managers used to arrange components within panels.
* `BorderFactory`: Used to create borders for visual styling.
* `Color`, `Font`: Used for customizing the appearance of components.
* `JOptionPane`: Used to display confirmation and informational messages.
* `ArrayList`: Used to store the `Product` objects in the shopping cart.
* `TableCellRenderer`: An interface implemented by `ButtonRenderer` to customize how the "Add to Cart" button is displayed in the product table.
* `DefaultCellEditor`: A base class extended by `ButtonEditor` to handle the click events of the "Add to Cart" buttons in the product table.

The code includes inner classes:
* `Product`: A simple class to represent a product with a name and price.
* `ButtonRenderer`: A custom table cell renderer to display the "Add to Cart" button in the product table.
* `ButtonEditor`: A custom table cell editor to handle the action when the "Add to Cart" button is clicked, adding the product to the cart and updating the UI.

## How to Run
1.  Save the code as `ProductSubcategoriesCartApp.java`.
2.  Compile the code using a Java compiler: `javac ProductSubcategoriesCartApp.java`
3.  Run the compiled class: `java ProductSubcategoriesCartApp`

This will open the GUI application, allowing you to browse the products, add them to the cart, view the cart, and simulate a checkout process.

## Dependencies
* Java Development Kit (JDK) 8 or higher.

## Limitations
* In-Memory Cart: The shopping cart data is stored in an `ArrayList` within the application's memory. The cart contents are not saved once the application is closed.
* Simulated Checkout: The "Proceed to Checkout" functionality is a basic simulation. It displays a success message but does not involve any actual payment processing, order creation, or inventory management.
* No Data Persistence: The product data displayed in the product table is hardcoded within the `createProductPage()` method. There is no mechanism to load product information from an external source.
* Basic UI: The user interface is designed for demonstration purposes and is quite simple.
* Single "Add to Cart" Action: The application only provides a basic "Add to Cart" action without options for quantity adjustment or removal from the cart (except at checkout).
* No User Accounts or Authentication: There is no concept of user accounts or logins.
* Limited Error Handling: The application has minimal error handling.
* No Search or Filtering: The application lacks features for searching or filtering products.
* No Product Details Page: Clicking on a product doesn't lead to a separate details page.

## Notes
* The application uses an in-memory `ArrayList` to store the shopping cart data.
* The checkout process is a simple simulation.
* The UI is designed for basic demonstration purposes.*/

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;

public class ProductSubcategoriesCartApp {
    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JTable productTable;
    private JTable cartTable;
    private DefaultTableModel cartModel;
    private ArrayList<Product> cart = new ArrayList<>();
    private JButton cartButton; // Reference to update cart count

    public ProductSubcategoriesCartApp() {
        frame = new JFrame("Product Store");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createProductPage(), "Products");
        mainPanel.add(createCartPage(), "Cart");

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private JPanel createProductPage() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setBackground(new Color(240, 240, 240));

        // Title
        JLabel title = new JLabel("Our Products", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        panel.add(title, BorderLayout.NORTH);

        // Product table
        String[] columns = {"Category", "Product", "Price", "Action"};
        Object[][] data = {
            {"Electronics", "Wireless Headphones", "$59.99", "Add to Cart"},
            {"Electronics", "Bluetooth Speaker", "$39.99", "Add to Cart"},
            {"Clothing", "Cotton T-Shirt", "$19.99", "Add to Cart"},
            {"Home", "Coffee Maker", "$49.99", "Add to Cart"},
            {"Books", "Java Programming", "$29.99", "Add to Cart"}
        };

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public Class<?> getColumnClass(int column) {
                return column == 3 ? JButton.class : Object.class;
            }
            
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
        };

        productTable = new JTable(model);
        productTable.setRowHeight(45);
        productTable.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        productTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        productTable.setShowGrid(true);
        productTable.setGridColor(new Color(220, 220, 220));

        // Set column widths
        productTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        productTable.getColumnModel().getColumn(1).setPreferredWidth(250);
        productTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        productTable.getColumnModel().getColumn(3).setPreferredWidth(150);

        // Button renderer and editor
        TableColumn actionColumn = productTable.getColumnModel().getColumn(3);
        actionColumn.setCellRenderer(new ButtonRenderer());
        actionColumn.setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(productTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // View Cart button
        cartButton = new JButton("View Cart (" + cart.size() + ")");
        styleButton(cartButton, new Color(70, 130, 180));
        cartButton.addActionListener(e -> {
            updateCartView();
            cardLayout.show(mainPanel, "Cart");
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(240, 240, 240));
        buttonPanel.add(cartButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createCartPage() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setBackground(new Color(240, 240, 240));

        // Title
        JLabel title = new JLabel("Your Shopping Cart", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        panel.add(title, BorderLayout.NORTH);

        // Cart table
        String[] columns = {"Product", "Price"};
        cartModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        cartTable = new JTable(cartModel);
        cartTable.setRowHeight(45);
        cartTable.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        cartTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        cartTable.setShowGrid(true);
        cartTable.setGridColor(new Color(220, 220, 220));

        JScrollPane scrollPane = new JScrollPane(cartTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(240, 240, 240));

        JButton backButton = new JButton("Continue Shopping");
        styleButton(backButton, new Color(70, 130, 180));
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Products"));

        JButton checkoutButton = new JButton("Proceed to Checkout");
        styleButton(checkoutButton, new Color(60, 179, 113));
        checkoutButton.addActionListener(e -> {
            if (cart.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Your cart is empty!", "Checkout", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "Order placed successfully!", "Thank You", JOptionPane.INFORMATION_MESSAGE);
                cart.clear();
                updateCartView();
                cardLayout.show(mainPanel, "Products");
            }
        });

        buttonPanel.add(backButton);
        buttonPanel.add(checkoutButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void updateCartView() {
        // Update cart table
        cartModel.setRowCount(0);
        for (Product product : cart) {
            cartModel.addRow(new Object[]{product.name, product.price});
        }
        
        // Update cart count
        cartButton.setText("View Cart (" + cart.size() + ")");
    }

    private void styleButton(JButton button, Color color) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(color);
        button.setForeground(Color.BLUE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color.darker(), 2),
            BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
    }

    class Product {
        String name;
        String price;

        Product(String name, String price) {
            this.name = name;
            this.price = price;
        }
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setContentAreaFilled(true);
            setBorderPainted(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText("Add to Cart");
            setBackground(new Color(60, 179, 113));
            setForeground(Color.BLUE);
            setFont(getFont().deriveFont(Font.BOLD));
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private int row;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton("Add to Cart");
            button.setOpaque(true);
            button.setBackground(new Color(46, 139, 87));
            button.setForeground(Color.WHITE);
            button.setFont(button.getFont().deriveFont(Font.BOLD));
            button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            
            button.addActionListener(e -> {
                fireEditingStopped();
                
                // Add product to cart
                String product = (String) productTable.getValueAt(row, 1);
                String price = (String) productTable.getValueAt(row, 2);
                cart.add(new Product(product, price));
                
                // Update UI
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(frame, 
                        product + " added to cart!", 
                        "Success", 
                        JOptionPane.INFORMATION_MESSAGE);
                    updateCartView();
                });
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            this.row = row;
            return button;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new ProductSubcategoriesCartApp();
        });
    }
}
