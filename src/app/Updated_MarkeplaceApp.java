/* New updated code for database connectivity and a bidding system ,
updated on 30-04-2025  8:51*/



import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*; // For database connectivity
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List; // Use List interface

public class MarketplaceApp extends JFrame {
    CardLayout cardLayout;
    JPanel mainPanel;
    ArrayList<Product> cart = new ArrayList<>();
    HashMap<String, List<Product>> categoryProducts = new HashMap<>();
    Connection connection; // Database connection
    String userRole = "buyer"; // Default user role

    public MarketplaceApp() {
        setTitle("Marketplace");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Initialize database connection
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/marketplace", "root", ""); // Replace with your credentials
            System.out.println("Connected to the database!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database connection failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            // Consider exiting the application if the database connection is crucial.
            e.printStackTrace(); // Good practice to print the stack trace for debugging.
        }

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
        // Clear existing data
        categoryProducts.clear();

        // Initialize categories (ensure these match your database)
        categoryProducts.put("Electronics", new ArrayList<>());
        categoryProducts.put("Clothing", new ArrayList<>());
        categoryProducts.put("Home & Garden", new ArrayList<>());
        categoryProducts.put("Sports", new ArrayList<>());

        try {
            if (connection != null) {
                // Fetch products from the database
                String query = "SELECT * FROM products";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                while (resultSet.next()) {
                    String category = resultSet.getString("category");
                    String name = resultSet.getString("name");
                    String price = resultSet.getString("price");
                    int productId = resultSet.getInt("id"); // Fetch the product ID
                    // Fetch the is_auction column.  If it's 1, then isAuction is true.
                    boolean isAuction = resultSet.getInt("is_auction") == 1;
                    Product product = new Product(name, price, isAuction, productId); // Store the ID
                    categoryProducts.get(category).add(product);
                }
                resultSet.close();
                statement.close();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching products: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
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
            navButton.addActionListener(e -> {
                if (item.equals("Sell")) {
                    userRole = "seller";
                    cardLayout.show(mainPanel, "Sell"); //show the Sell Panel
                } else if (item.equals("Buy")) {
                    userRole = "buyer";
                    cardLayout.show(mainPanel, "Home");
                } else if (item.equals("Bet&Buy")) {
                    userRole = "buyer"; //or seller, depending on your logic
                    cardLayout.show(mainPanel, "Home");
                } else {
                    JOptionPane.showMessageDialog(this, item + " page clicked!");
                }
            });
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
        JButton searchButton = new JButton("Search");
        searchButton.setPreferredSize(new Dimension(50, 30));
        searchButton.setFocusPainted(false);
        searchButton.setBackground(Color.LIGHT_GRAY);
        searchButton.addActionListener(e -> {
            String searchText = searchField.getText().toLowerCase();
            //create a new JPanel to display search results
            JPanel searchResultsPanel = new JPanel(new BorderLayout());
            searchResultsPanel.setBackground(Color.WHITE);
            JLabel titleLabel = new JLabel("Search Results for \"" + searchText + "\"", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
            searchResultsPanel.add(titleLabel, BorderLayout.NORTH);

            DefaultTableModel searchTableModel = new DefaultTableModel(new String[]{"Product", "Price", "Add to Cart"}, 0);
            JTable searchTable = new JTable(searchTableModel);
            searchTable.setRowHeight(40);
            searchTable.setFont(new Font("Arial", Font.PLAIN, 18));
            searchTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));
            searchTable.getColumn("Add to Cart").setCellRenderer(new ButtonRenderer());
            searchTable.getColumn("Add to Cart").setCellEditor(new ButtonEditor());
            JScrollPane searchScrollPane = new JScrollPane(searchTable);
            searchResultsPanel.add(searchScrollPane, BorderLayout.CENTER);

            //search the products
            for (List<Product> productList : categoryProducts.values()) {
                for (Product product : productList) {
                    if (product.name.toLowerCase().contains(searchText)) {
                        searchTableModel.addRow(new Object[]{product.name, product.price, "Add"});
                    }
                }
            }
            if (searchTableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "No products found matching your search.", "No Results", JOptionPane.INFORMATION_MESSAGE);
                return; // Exit the search
            }
            mainPanel.add(searchResultsPanel, "Search Results");
            cardLayout.show(mainPanel, "Search Results");

        });

        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        panel.add(searchPanel, BorderLayout.NORTH);

        JPanel centerTitlePanel = new JPanel();
        centerTitlePanel.setBackground(Color.WHITE);
        centerTitlePanel.setLayout(new BoxLayout(centerTitlePanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Shop for Anything", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Buy and sell items from a wide range of categories", JLabel.CENTER);
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
        //add the Sell Panel to the mainPanel
        mainPanel.add(sellPage(), "Sell");
        return panel;
    }

    private JPanel sellPage() {
        JPanel sellPanel = new JPanel(new BorderLayout());
        sellPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Sell Your Product", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        sellPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10)); // 5 rows, 2 columns
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JLabel nameLabel = new JLabel("Product Name:");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        JTextField nameField = new JTextField(20);
        nameField.setFont(new Font("Arial", Font.PLAIN, 18));

        JLabel categoryLabel = new JLabel("Category:");
        categoryLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        String[] categories = {"Electronics", "Clothing", "Home & Garden", "Sports"};
        JComboBox<String> categoryComboBox = new JComboBox<>(categories);
        categoryComboBox.setFont(new Font("Arial", Font.PLAIN, 18));

        JLabel priceLabel = new JLabel("Price:");
        priceLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        JTextField priceField = new JTextField(20);
        priceField.setFont(new Font("Arial", Font.PLAIN, 18));

        JLabel descriptionLabel = new JLabel("Description:");
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        JTextArea descriptionArea = new JTextArea(5, 20); // 5 rows, 20 columns
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 18));
        JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);

        JLabel isAuctionLabel = new JLabel("Auction Item:");
        isAuctionLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        JCheckBox isAuctionCheckBox = new JCheckBox();
        isAuctionCheckBox.setFont(new Font("Arial", Font.PLAIN, 18));

        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(categoryLabel);
        formPanel.add(categoryComboBox);
        formPanel.add(priceLabel);
        formPanel.add(priceField);
        formPanel.add(descriptionLabel);
        formPanel.add(descriptionScrollPane);
        formPanel.add(isAuctionLabel);
        formPanel.add(isAuctionCheckBox);

        sellPanel.add(formPanel, BorderLayout.CENTER);

        JButton sellButton = new JButton("List Product");
        sellButton.setFont(new Font("Arial", Font.BOLD, 20));
        sellButton.setBackground(new Color(46, 204, 113));
        sellButton.setForeground(Color.white);
        sellButton.setFocusPainted(false);
        sellButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        sellButton.addActionListener(e -> {
            String name = nameField.getText();
            String category = (String) categoryComboBox.getSelectedItem();
            String price = priceField.getText();
            String description = descriptionArea.getText();
            boolean isAuction = isAuctionCheckBox.isSelected();

            if (name.isEmpty() || price.isEmpty() || description.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                double priceValue = Double.parseDouble(price);
                if (priceValue <= 0) {
                    JOptionPane.showMessageDialog(this, "Price must be greater than zero.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid price format.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Insert into database
            try {
                if (connection != null) {
                    String insertQuery = "INSERT INTO products (name, category, price, description, is_auction) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
                    preparedStatement.setString(1, name);
                    preparedStatement.setString(2, category);
                    preparedStatement.setString(3, price);
                    preparedStatement.setString(4, description);
                    preparedStatement.setInt(5, isAuction ? 1 : 0); // Store 1 for true, 0 for false.
                    preparedStatement.executeUpdate();
                    preparedStatement.close();
                    JOptionPane.showMessageDialog(this, "Product listed successfully!");

                    // Clear the form
                    nameField.setText("");
                    priceField.setText("");
                    descriptionArea.setText("");
                    isAuctionCheckBox.setSelected(false);
                    populateProducts(); // Refresh product lists
                    cardLayout.show(mainPanel, "Home"); // Go back to home page
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error listing product: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(sellButton);
        sellPanel.add(buttonPanel, BorderLayout.SOUTH);

        return sellPanel;
    }

    private void showProductsForCategory(String category) {
        JPanel productPanel = new JPanel(new BorderLayout());
        productPanel.setBackground(Color.WHITE);

        JLabel title = new JLabel("Products in " + category, JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        productPanel.add(title, BorderLayout.NORTH);

        List<Product> products = categoryProducts.getOrDefault(category, new ArrayList<>());

        String[] columns = {"Product", "Price", "Add to Cart"};
        if (userRole.equals("buyer")) {
            columns = new String[]{"Product", "Price", "Add to Cart"};
        } else if (userRole.equals("seller")) {
            columns = new String[]{"Product", "Price"};
        }

        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (Product p : products) {
            if (userRole.equals("buyer")) {
                model.addRow(new Object[]{p.name, p.price, "Add"});
            } else if (userRole.equals("seller")) {
                model.addRow(new Object[]{p.name, p.price});
            }
        }

        JTable table = new JTable(model);
        table.setRowHeight(40);
        table.setFont(new Font("Arial", Font.PLAIN, 18));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));
        if (userRole.equals("buyer")) {
            table.getColumn("Add to Cart").setCellRenderer(new ButtonRenderer());
            table.getColumn("Add to Cart").setCellEditor(new ButtonEditor());
        }

        JScrollPane scrollPane = new JScrollPane(table);
        productPanel.add(scrollPane, BorderLayout.CENTER);

        if (userRole.equals("buyer")) {
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
        }

        mainPanel.add(productPanel, category);
        cardLayout.show(mainPanel, category);
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
            if (cart.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Your cart is empty!", "Checkout Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Implement the checkout process (e.g., create an order in the database)
            try {
                if (connection != null) {
                    // Start a transaction (optional, but good for data integrity)
                    connection.setAutoCommit(false);

                    // 1. Create an order
                    String insertOrderQuery = "INSERT INTO orders (order_date, total_amount) VALUES (NOW(), ?)";
                    PreparedStatement orderStatement = connection.prepareStatement(insertOrderQuery, Statement.RETURN_GENERATED_KEYS);
                    double totalAmount = 0;
                    for (Product p : cart) {
                        totalAmount += Double.parseDouble(p.price.substring(1)); // Remove "₹" and parse
                    }
                    orderStatement.setDouble(1, totalAmount);
                    orderStatement.executeUpdate();

                    // Get the generated order ID
                    ResultSet generatedKeys = orderStatement.getGeneratedKeys();
                    int orderId = 0;
                    if (generatedKeys.next()) {
                        orderId = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Failed to retrieve order ID.");
                    }
                    generatedKeys.close();
                    orderStatement.close();

                    // 2. Insert order items
                    String insertOrderItemQuery = "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
                    PreparedStatement orderItemStatement = connection.prepareStatement(insertOrderItemQuery);
                    for (Product p : cart) {
                        // Get the product ID from the database, assuming you have the product name
                        int productId = getProductIdByName(p.name);
                        orderItemStatement.setInt(1, orderId);
                        orderItemStatement.setInt(2, productId);
                        orderItemStatement.setInt(3, 1); // Quantity is 1 for each item in this example
                        orderItemStatement.setDouble(4, Double.parseDouble(p.price.substring(1))); // Remove "₹"
                        orderItemStatement.executeUpdate();
                    }
                    orderItemStatement.close();

                    // Commit the transaction
                    connection.commit();
                    connection.setAutoCommit(true); //reset
                    JOptionPane.showMessageDialog(this, "Order Placed Successfully! Thank you for shopping.");
                    cart.clear();
                    refreshCartPage();
                    cardLayout.show(mainPanel, "Home");
                }
            } catch (SQLException ex) {
                try {
                    connection.rollback();
                    connection.setAutoCommit(true);
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
                JOptionPane.showMessageDialog(this, "Error placing order: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        bottom.add(back);
        bottom.add(checkout);

        cartPanel.add(bottom, BorderLayout.SOUTH);

        return cartPanel;
    }

    private int getProductIdByName(String productName) throws SQLException {
        String query = "SELECT id FROM products WHERE name = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, productName);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            int id = resultSet.getInt("id");
            resultSet.close();
            preparedStatement.close();
            return id;
        } else {
            resultSet.close();
            preparedStatement.close();
            throw new SQLException("Product not found: " + productName);
        }
    }

    private void refreshCartPage() {
        cartModel.setRowCount(0);
        for (Product p : cart) {
            cartModel.addRow(new Object[]{p.name, p.price});
        }
    }

    class Product {
        String name, price;
        boolean isAuction;
        int id; // Add product ID

        Product(String name, String price, boolean isAuction, int id) {
            this.name = name;
            this.price = price;
            this.isAuction = isAuction;
            this.id = id;
        }
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

    @Override
    public void dispose() {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            super.dispose(); // Call superclass dispose() to ensure proper cleanup.
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MarketplaceApp::new);
    }
}

