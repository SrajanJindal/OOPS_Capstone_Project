/*# Mini E-Commerce Store Application

This Java code creates a simple graphical user interface (GUI) for a mini e-commerce store. It allows users to browse a list of products and add them to a virtual shopping cart.

## Functionality

The application features two main pages, managed using a `CardLayout`:

1.  Home Page:
    * Displays a title "Online Marketplace".
    * Presents a table of available products with the following columns:
        * **Product:** The name of the product (e.g., T-Shirt, Headphones).
        * **Price:** The price of the product in Indian Rupees (₹).
        * **Add to Cart:** A button in each row that allows the user to add the corresponding product to their cart.
    * When the "Add" button for a product is clicked:
        * The product is added to an `ArrayList` representing the shopping cart.
        * A confirmation message (e.g., "T-Shirt added to cart!") is displayed using a `JOptionPane`.
    * Includes a "View Cart" button at the bottom. Clicking this button navigates the user to the Cart Page.

2.  Cart Page:
    * Displays a title "Your Cart".
    * Shows a table listing the products currently in the shopping cart. The table has two columns:
        * **Product:** The name of the product.
        * **Price:** The price of the product.
    * Includes a "Back to Shopping" button that navigates the user back to the Home Page.
    * Includes a "Checkout" button. Clicking this button:
        * Displays a "Order Placed Successfully!" message using a `JOptionPane`.
        * Clears the shopping cart.
        * Navigates the user back to the Home Page.

## Structure

The code is organized into a single class, `EcommerceApp`, which extends `JFrame`. It utilizes various Swing components for creating the UI:

* `JFrame`: The main window of the application.
* `JPanel`: Used as containers to organize other components.
* `JLabel`: Displays text information like titles.
* `JButton`: Interactive buttons for adding to cart, viewing cart, going back to shopping, and checking out.
* `JTable`: Displays the list of products on the Home Page and the items in the Cart Page.
* `DefaultTableModel`: Manages the data displayed in the `JTable`.
* `JScrollPane`: Provides scrollability for the product table and cart table if the content exceeds the display area.
* `CardLayout`: Manages the switching between the Home and Cart panels.
* `BorderLayout`: Used to arrange components within panels.
* `Color`, `Font`, `BorderFactory`: Used for styling the appearance of components.
* `JOptionPane`: Used to display confirmation and success messages.
* `ArrayList`: Used to store the products added to the shopping cart.

The code also includes inner classes:

* `Product`: A simple class to represent a product with a name and price.
* `ButtonRenderer`: A custom table cell renderer to display buttons in the "Add to Cart" column of the product table.
* `ButtonEditor`: A custom table cell editor to handle the click events of the "Add to Cart" buttons and add the corresponding product to the cart.

## How to Run

1.  Save the code as `EcommerceApp.java`.
2.  Compile the code using a Java compiler: `javac EcommerceApp.java`
3.  Run the compiled class: `java EcommerceApp`

This will open the GUI application, allowing you to browse products, add them to the cart, view the cart, and simulate a checkout process.

## Limitations

* This is a very basic implementation of an e-commerce store.
* Only a limited number of products are hardcoded.
* The cart functionality is simple, without features like removing items or adjusting quantities.
* The checkout process is simulated and does not involve any actual payment or order processing.
* The UI design is basic and for demonstration purposes.*/

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class EcommerceApp {
    JFrame frame;
    CardLayout cardLayout;
    JPanel mainPanel;

    ArrayList<Product> cart = new ArrayList<>();

    public EcommerceApp() {
        frame = new JFrame("Mini E-Commerce Store");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(homePage(), "Home");
        mainPanel.add(cartPage(), "Cart");

        frame.add(mainPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel homePage() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));

        JLabel title = new JLabel("Welcome to Amrit's Store", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        panel.add(title, BorderLayout.NORTH);

        String[] columns = {"Product", "Price", "Add to Cart"};
        Object[][] data = {
            {"T-Shirt", "₹499", "Add"},
            {"Headphones", "₹2999", "Add"},
            {"Coffee Mug", "₹299", "Add"},
            {"Smartphone", "₹14999", "Add"},
            {"Backpack", "₹999", "Add"}
        };

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            public boolean isCellEditable(int row, int column) {
                return column == 2;
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(40);
        table.setFont(new Font("Arial", Font.PLAIN, 18));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));
        table.getColumn("Add to Cart").setCellRenderer(new ButtonRenderer());
        table.getColumn("Add to Cart").setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton viewCart = new JButton("View Cart");
        viewCart.setFont(new Font("Arial", Font.BOLD, 20));
        viewCart.setBackground(new Color(52, 152, 219));
        viewCart.setForeground(Color.white);
        viewCart.setFocusPainted(false);
        viewCart.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        viewCart.addActionListener(e -> {
            refreshCartPage();
            cardLayout.show(mainPanel, "Cart");
        });

        JPanel bottom = new JPanel();
        bottom.add(viewCart);
        bottom.setBackground(new Color(245, 245, 245));
        panel.add(bottom, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel cartPanel;
    private JTable cartTable;
    private DefaultTableModel cartModel;

    private JPanel cartPage() {
        cartPanel = new JPanel(new BorderLayout());
        cartPanel.setBackground(new Color(245, 245, 245));

        JLabel title = new JLabel("Your Cart", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        cartPanel.add(title, BorderLayout.NORTH);

        String[] columns = {"Product", "Price"};
        cartModel = new DefaultTableModel(columns, 0);
        cartTable = new JTable(cartModel);
        cartTable.setRowHeight(40);
        cartTable.setFont(new Font("Arial", Font.PLAIN, 18));
        cartTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));

        JScrollPane scrollPane = new JScrollPane(cartTable);
        cartPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        JButton back = new JButton("Back to Shopping");
        back.setFont(new Font("Arial", Font.BOLD, 18));
        back.setBackground(new Color(52, 152, 219));
        back.setForeground(Color.white);
        back.setFocusPainted(false);
        back.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        back.addActionListener(e -> cardLayout.show(mainPanel, "Home"));

        JButton checkout = new JButton("Checkout");
        checkout.setFont(new Font("Arial", Font.BOLD, 18));
        checkout.setBackground(new Color(46, 204, 113));
        checkout.setForeground(Color.white);
        checkout.setFocusPainted(false);
        checkout.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        checkout.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Order Placed Successfully!");
            cart.clear();
            refreshCartPage();
            cardLayout.show(mainPanel, "Home");
        });

        bottom.add(back);
        bottom.add(checkout);
        bottom.setBackground(new Color(245, 245, 245));
        cartPanel.add(bottom, BorderLayout.SOUTH);

        return cartPanel;
    }

    private void refreshCartPage() {
        cartModel.setRowCount(0);
        for (Product p : cart) {
            cartModel.addRow(new Object[]{p.name, p.price});
        }
    }

    class Product {
        String name;
        String price;

        Product(String name, String price) {
            this.name = name;
            this.price = price;
        }
    }

    class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            setText((value == null) ? "Add" : value.toString());
            setBackground(new Color(46, 204, 113));
            setForeground(Color.white);
            setFont(new Font("Arial", Font.BOLD, 16));
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean clicked;
        private int row;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                      boolean isSelected, int row, int column) {
            label = (value == null) ? "Add" : value.toString();
            button.setText(label);
            button.setBackground(new Color(46, 204, 113));
            button.setForeground(Color.white);
            button.setFont(new Font("Arial", Font.BOLD, 16));
            clicked = true;
            this.row = row;
            return button;
        }

        public Object getCellEditorValue() {
            if (clicked) {
                String name = (String) ((DefaultTableModel) ((JTable) getComponent()).getModel()).getValueAt(row, 0);
                String price = (String) ((DefaultTableModel) ((JTable) getComponent()).getModel()).getValueAt(row, 1);
                cart.add(new Product(name, price));
                JOptionPane.showMessageDialog(frame, name + " added to cart!");
            }
            clicked = false;
            return label;
        }

        public boolean stopCellEditing() {
            clicked = false;
            return super.stopCellEditing();
        }

        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EcommerceApp::new);
    }
}
