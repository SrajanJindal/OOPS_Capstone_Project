
/* # 🛍️ Updated MarketplaceApp

Update:
✅ Added a fully working search bar that displays filtered results from all categories in a dedicated panel.
*/

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;

public class MarketplaceApp extends JFrame {
    CardLayout cardLayout;
    JPanel mainPanel;
    ArrayList<Product> cart = new ArrayList<>();
    HashMap<String, ArrayList<Product>> categoryProducts = new HashMap<>();

    public MarketplaceApp() {
        setTitle("Marketplace");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        populateProducts();

        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(homePage(), "Home");
        mainPanel.add(cartPage(), "Cart");

        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private void populateProducts() {
        categoryProducts.put("Electronics", new ArrayList<>() {{
            add(new Product("Smartphone", "₹14999"));
            add(new Product("Headphones", "₹2999"));
        }});

        categoryProducts.put("Clothing", new ArrayList<>() {{
            add(new Product("T-Shirt", "₹499"));
            add(new Product("Jacket", "₹1999"));
        }});

        categoryProducts.put("Home & Garden", new ArrayList<>() {{
            add(new Product("Coffee Mug", "₹299"));
            add(new Product("Garden Tools", "₹899"));
        }});

        categoryProducts.put("Sports", new ArrayList<>() {{
            add(new Product("Football", "₹999"));
            add(new Product("Tennis Racket", "₹1599"));
        }});
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel logoLabel = new JLabel("Marketplace");
        logoLabel.setFont(new Font("Arial", Font.BOLD, 38));
        logoLabel.setForeground(new Color(0, 100, 210));

        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 30, 10));
        navPanel.setBackground(Color.WHITE);
        String[] navItems = {"Buy", "Sell", "Bet&Buy"};
        for (String item : navItems) {
            JButton navButton = new JButton(item);
            navButton.setFocusPainted(false);
            navButton.setBackground(Color.WHITE);
            navButton.setForeground(Color.BLACK);
            navButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
            navButton.setFont(new Font("Arial", Font.PLAIN, 28));
            navButton.addActionListener(e -> JOptionPane.showMessageDialog(this, item + " page clicked!"));
            navPanel.add(navButton);
        }

        topPanel.add(logoLabel, BorderLayout.WEST);
        topPanel.add(navPanel, BorderLayout.EAST);
        return topPanel;
    }

    private JPanel homePage() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
        searchPanel.setBackground(Color.WHITE);
        JTextField searchField = new JTextField(30);
        searchField.setPreferredSize(new Dimension(300, 30));
        JButton searchButton = new JButton("🔍");
        searchButton.setPreferredSize(new Dimension(50, 30));
        searchButton.setFocusPainted(false);
        searchButton.setBackground(Color.LIGHT_GRAY);

        searchButton.addActionListener(e -> {
            String query = searchField.getText().trim().toLowerCase();
            if (query.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a search term.");
                return;
            }

            ArrayList<Product> results = new ArrayList<>();
            for (ArrayList<Product> products : categoryProducts.values()) {
                for (Product p : products) {
                    if (p.name.toLowerCase().contains(query)) {
                        results.add(p);
                    }
                }
            }

            if (results.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No products found for: " + query);
            } else {
                showSearchResults(results, query);
            }
        });

        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        panel.add(searchPanel, BorderLayout.NORTH);

        JPanel centerTitlePanel = new JPanel();
        centerTitlePanel.setBackground(Color.WHITE);
        centerTitlePanel.setLayout(new BoxLayout(centerTitlePanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Shop for Anything");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Buy and sell items from a wide range of categories");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerTitlePanel.add(titleLabel);
        centerTitlePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerTitlePanel.add(subtitleLabel);
        centerTitlePanel.add(Box.createRigidArea(new Dimension(0, 30)));

        panel.add(centerTitlePanel, BorderLayout.CENTER);

        JPanel categoryPanel = new JPanel(new GridLayout(2, 4, 20, 20));
        categoryPanel.setBackground(Color.WHITE);
        String[] categories = {"Electronics", "Clothing", "Home & Garden", "Sports", "Toys", "Motors", "Collectibles", "Deals"};

        for (String cat : categories) {
            JButton catButton = new JButton(cat);
            catButton.setFocusPainted(false);
            catButton.setBackground(Color.WHITE);
            catButton.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
            catButton.setFont(new Font("Arial", Font.PLAIN, 16));
            catButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            catButton.addActionListener(e -> showProductsForCategory(cat));
            categoryPanel.add(catButton);
        }

        panel.add(categoryPanel, BorderLayout.SOUTH);
        return panel;
    }

    private void showProductsForCategory(String category) {
        JPanel productPanel = new JPanel(new BorderLayout());
        productPanel.setBackground(Color.WHITE);

        JLabel title = new JLabel("Products in " + category, JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        productPanel.add(title, BorderLayout.NORTH);

        ArrayList<Product> products = categoryProducts.getOrDefault(category, new ArrayList<>());

        String[] columns = {"Product", "Price", "Add to Cart"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (Product p : products) {
            model.addRow(new Object[]{p.name, p.price, "Add"});
        }

        JTable table = new JTable(model);
        table.setRowHeight(40);
        table.setFont(new Font("Arial", Font.PLAIN, 18));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));
        table.getColumn("Add to Cart").setCellRenderer(new ButtonRenderer());
        table.getColumn("Add to Cart").setCellEditor(new ButtonEditor());

        JScrollPane scrollPane = new JScrollPane(table);
        productPanel.add(scrollPane, BorderLayout.CENTER);

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
        bottom.setBackground(Color.WHITE);
        bottom.add(viewCart);
        productPanel.add(bottom, BorderLayout.SOUTH);

        mainPanel.add(productPanel, category);
        cardLayout.show(mainPanel, category);
    }

    private void showSearchResults(ArrayList<Product> results, String query) {
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBackground(Color.WHITE);

        JLabel title = new JLabel("Search Results for \"" + query + "\"", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        searchPanel.add(title, BorderLayout.NORTH);

        String[] columns = {"Product", "Price", "Add to Cart"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (Product p : results) {
            model.addRow(new Object[]{p.name, p.price, "Add"});
        }

        JTable table = new JTable(model);
        table.setRowHeight(40);
        table.setFont(new Font("Arial", Font.PLAIN, 18));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));
        table.getColumn("Add to Cart").setCellRenderer(new ButtonRenderer());
        table.getColumn("Add to Cart").setCellEditor(new ButtonEditor());

        JScrollPane scrollPane = new JScrollPane(table);
        searchPanel.add(scrollPane, BorderLayout.CENTER);

        JButton back = new JButton("Back to Home");
        back.setFont(new Font("Arial", Font.BOLD, 18));
        back.setBackground(new Color(52, 152, 219));
        back.setForeground(Color.white);
        back.setFocusPainted(false);
        back.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        back.addActionListener(e -> cardLayout.show(mainPanel, "Home"));

        JPanel bottom = new JPanel();
        bottom.setBackground(Color.WHITE);
        bottom.add(back);

        searchPanel.add(bottom, BorderLayout.SOUTH);

        String panelName = "Search_" + System.currentTimeMillis();
        mainPanel.add(searchPanel, panelName);
        cardLayout.show(mainPanel, panelName);
    }

    private JPanel cartPanel;
    private JTable cartTable;
    private DefaultTableModel cartModel;

    private JPanel cartPage() {
        cartPanel = new JPanel(new BorderLayout());
        cartPanel.setBackground(Color.WHITE);

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
        bottom.setBackground(Color.WHITE);

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
            JOptionPane.showMessageDialog(this, "Order Placed Successfully! Thank you for shopping.");
            cart.clear();
            refreshCartPage();
            cardLayout.show(mainPanel, "Home");
        });

        bottom.add(back);
        bottom.add(checkout);

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
        String name, price;

        Product(String name, String price) {
            this.name = name;
            this.price = price;
        }
    }

    class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
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

        public ButtonEditor() {
            super(new JCheckBox());
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
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
                JTable table = (JTable) button.getParent();
                while (!(table instanceof JTable)) {
                    table = (JTable) table.getParent();
                }
                String name = (String) table.getValueAt(row, 0);
                String price = (String) table.getValueAt(row, 1);
                cart.add(new Product(name, price));
                JOptionPane.showMessageDialog(null, name + " added to cart!");
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
        SwingUtilities.invokeLater(MarketplaceApp::new);
    }
}
