/*
- Java-based Shopping Management System  
- GUI built using Swing  
- Features include:
  - User Authentication (Login & Registration)
  - Product Management
  - Order Management
  - Admin and Manager Functionalities
- Role-based access in the main application window  
- Uses a simple in-memory database for data storage (no external database setup required)  
- Basic Java GUI and logic separation principles

CONTRIBUTED BY VANSH BHASIN
*/


import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.SimpleDateFormat;

public class ShoppingManagerSystem {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DatabaseService db = new DatabaseService();
            new LoginFrame(db).setVisible(true);
        });
    }
}

// Database and Service Classes
class DatabaseService {
    private Map<String, User> users = new HashMap<>();
    private Map<Integer, Product> products = new HashMap<>();
    private Map<Integer, Order> orders = new HashMap<>();
    private int nextProductId = 1;
    private int nextOrderId = 1;
    private User currentUser;

    public DatabaseService() {
        initializeSampleData();
    }

    private void initializeSampleData() {
        // Initialize admin and manager accounts
        users.put("admin", new User("admin", "admin123", "admin", "Admin User"));
        users.put("manager", new User("manager", "manager123", "manager", "Store Manager"));
        
        // Initialize sample products
        addProduct(new Product("Apple", 0.99, 100, "Fruits", "Fresh red apples"));
        addProduct(new Product("Milk", 2.49, 50, "Dairy", "Whole milk 1 gallon"));
        addProduct(new Product("Bread", 1.99, 75, "Bakery", "Whole wheat bread"));
        addProduct(new Product("Eggs", 3.29, 60, "Dairy", "Large eggs, dozen"));
        addProduct(new Product("Chicken", 5.99, 30, "Meat", "Boneless chicken breast"));
    }

    // User management
    public boolean register(String username, String password, String role, String fullName) {
        if (users.containsKey(username)) return false;
        users.put(username, new User(username, password, role, fullName));
        return true;
    }

    public User login(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            currentUser = user;
            return user;
        }
        return null;
    }

    public void logout() {
        currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    // Product management
    public void addProduct(Product product) {
        product.setId(nextProductId++);
        products.put(product.getId(), product);
    }

    public boolean updateProduct(Product product) {
        if (!products.containsKey(product.getId())) return false;
        products.put(product.getId(), product);
        return true;
    }

    public boolean deleteProduct(int productId) {
        return products.remove(productId) != null;
    }

    public List<Product> getAllProducts() {
        return new ArrayList<>(products.values());
    }

    public Product getProductById(int id) {
        return products.get(id);
    }

    // Order management
    public void placeOrder(Order order) {
        order.setId(nextOrderId++);
        order.setOrderDate(new Date());
        orders.put(order.getId(), order);
        
        // Update inventory
        for (OrderItem item : order.getItems()) {
            Product p = products.get(item.getProductId());
            p.setStock(p.getStock() - item.getQuantity());
        }
    }

    public List<Order> getOrdersForUser(String username) {
        List<Order> userOrders = new ArrayList<>();
        for (Order order : orders.values()) {
            if (order.getCustomerUsername().equals(username)) {
                userOrders.add(order);
            }
        }
        return userOrders;
    }

    public List<Order> getAllOrders() {
        return new ArrayList<>(orders.values());
    }

    // User management for admin
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public boolean updateUser(User user) {
        if (!users.containsKey(user.getUsername())) return false;
        users.put(user.getUsername(), user);
        return true;
    }

    public boolean deleteUser(String username) {
        if (username.equals("admin") || username.equals("manager")) return false;
        return users.remove(username) != null;
    }
}

// Model Classes
class User {
    private String username;
    private String password;
    private String role;
    private String fullName;

    public User(String username, String password, String role, String fullName) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.fullName = fullName;
    }

    // Getters and setters
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public String getFullName() { return fullName; }
    
    public void setPassword(String password) { this.password = password; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setRole(String role) { this.role = role; } // This was missing
}

class Product {
    private int id;
    private String name;
    private double price;
    private int stock;
    private String category;
    private String description;

    public Product(String name, double price, int stock, String category, String description) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.category = category;
        this.description = description;
    }

    // Getters and setters
    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setPrice(double price) { this.price = price; }
    public void setStock(int stock) { this.stock = stock; }
    public void setCategory(String category) { this.category = category; }
    public void setDescription(String description) { this.description = description; }
}

class Order {
    private int id;
    private String customerUsername;
    private Date orderDate;
    private List<OrderItem> items;
    private String status;

    public Order(String customerUsername) {
        this.customerUsername = customerUsername;
        this.items = new ArrayList<>();
        this.status = "Processing";
    }

    // Getters and setters
    public int getId() { return id; }
    public String getCustomerUsername() { return customerUsername; }
    public Date getOrderDate() { return orderDate; }
    public List<OrderItem> getItems() { return items; }
    public String getStatus() { return status; }
    public double getTotal() {
        double total = 0;
        for (OrderItem item : items) {
            total += item.getSubtotal();
        }
        return total;
    }
    
    public void setId(int id) { this.id = id; }
    public void setOrderDate(Date orderDate) { this.orderDate = orderDate; }
    public void setStatus(String status) { this.status = status; }
    
    public void addItem(OrderItem item) {
        items.add(item);
    }
}

class OrderItem {
    private int productId;
    private String productName;
    private int quantity;
    private double unitPrice;

    public OrderItem(int productId, String productName, int quantity, double unitPrice) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    // Getters
    public int getProductId() { return productId; }
    public String getProductName() { return productName; }
    public int getQuantity() { return quantity; }
    public double getUnitPrice() { return unitPrice; }
    public double getSubtotal() { return quantity * unitPrice; }
}

// GUI Classes
class LoginFrame extends JFrame {
    private DatabaseService db;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame(DatabaseService db) {
        this.db = db;
        setupUI();
    }

    private void setupUI() {
        setTitle("Shopping Manager - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Logo
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel logoLabel = new JLabel("SHOPPING MANAGER", SwingConstants.CENTER);
        logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(logoLabel, gbc);

        // Username
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        panel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        usernameField = new JTextField(15);
        panel.add(usernameField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        panel.add(passwordField, gbc);

        // Buttons
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        panel.add(buttonPanel, gbc);

        // Event Listeners
        loginButton.addActionListener(e -> handleLogin());
        registerButton.addActionListener(e -> new RegisterFrame(db).setVisible(true));

        // Add key listener for Enter key
        passwordField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleLogin();
                }
            }
        });

        add(panel);
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        User user = db.login(username, password);
        if (user != null) {
            JOptionPane.showMessageDialog(this, "Login successful! Welcome " + user.getFullName());
            new MainFrame(db).setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials!", "Error", JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
        }
    }
}

class RegisterFrame extends JFrame {
    private DatabaseService db;
    private JTextField usernameField, fullNameField;
    private JPasswordField passwordField, confirmPasswordField;
    private JComboBox<String> roleComboBox;

    public RegisterFrame(DatabaseService db) {
        this.db = db;
        setupUI();
    }

    private void setupUI() {
        setTitle("Shopping Manager - Register");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel titleLabel = new JLabel("Create New Account", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titleLabel, gbc);

        // Full Name
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        panel.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1;
        fullNameField = new JTextField(15);
        panel.add(fullNameField, gbc);

        // Username
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        usernameField = new JTextField(15);
        panel.add(usernameField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        panel.add(passwordField, gbc);

        // Confirm Password
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Confirm Password:"), gbc);
        gbc.gridx = 1;
        confirmPasswordField = new JPasswordField(15);
        panel.add(confirmPasswordField, gbc);

        // Role
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Role:"), gbc);
        gbc.gridx = 1;
        String[] roles = {"customer", "manager", "admin"};
        roleComboBox = new JComboBox<>(roles);
        panel.add(roleComboBox, gbc);

        // Register Button
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        JButton registerButton = new JButton("Register");
        registerButton.setPreferredSize(new Dimension(150, 30));
        panel.add(registerButton, gbc);

        // Event Listener
        registerButton.addActionListener(e -> handleRegistration());

        add(panel);
    }

    private void handleRegistration() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String role = (String) roleComboBox.getSelectedItem();
        String fullName = fullNameField.getText();

        if (username.isEmpty() || password.isEmpty() || fullName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (db.register(username, password, role, fullName)) {
            JOptionPane.showMessageDialog(this, "Registration successful! You can now login.");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Username already exists!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

class MainFrame extends JFrame {
    private DatabaseService db;
    private User currentUser;
    private JTabbedPane tabbedPane;

    public MainFrame(DatabaseService db) {
        this.db = db;
        this.currentUser = db.getCurrentUser();
        setupUI();
    }

    private void setupUI() {
        setTitle("Shopping Manager - " + currentUser.getFullName() + " (" + currentUser.getRole() + ")");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Menu Bar
        JMenuBar menuBar = new JMenuBar();
        
        // File Menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem logoutItem = new JMenuItem("Logout");
        JMenuItem exitItem = new JMenuItem("Exit");
        
        logoutItem.addActionListener(e -> {
            db.logout();
            new LoginFrame(db).setVisible(true);
            dispose();
        });
        
        exitItem.addActionListener(e -> System.exit(0));
        
        fileMenu.add(logoutItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);

        // Add role-specific menus
        if (currentUser.getRole().equals("admin")) {
            setupAdminMenu(menuBar);
        } else if (currentUser.getRole().equals("manager")) {
            setupManagerMenu(menuBar);
        }
        
        // Help Menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> showAboutDialog());
        helpMenu.add(aboutItem);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        // Main Content - Tabbed Pane
        tabbedPane = new JTabbedPane();
        
        // Dashboard Tab
        tabbedPane.addTab("Dashboard", createDashboardPanel());
        
        // Products Tab (visible to all)
        tabbedPane.addTab("Products", createProductsPanel());
        
        // Shopping Cart Tab (for customers)
        if (currentUser.getRole().equals("customer")) {
            tabbedPane.addTab("My Cart", createCartPanel());
        }
        
        // Orders Tab
        tabbedPane.addTab("Orders", createOrdersPanel());
        
        // Admin/Manager specific tabs
        if (currentUser.getRole().equals("admin") || currentUser.getRole().equals("manager")) {
            tabbedPane.addTab("Inventory", createInventoryPanel());
        }
        
        if (currentUser.getRole().equals("admin")) {
            tabbedPane.addTab("User Management", createUserManagementPanel());
        }

        add(tabbedPane);
    }

    private void setupAdminMenu(JMenuBar menuBar) {
        JMenu adminMenu = new JMenu("Admin");
        JMenuItem reportsItem = new JMenuItem("Reports");
        JMenuItem settingsItem = new JMenuItem("Settings");
        
        reportsItem.addActionListener(e -> showFeatureMessage("Reports"));
        settingsItem.addActionListener(e -> showFeatureMessage("System Settings"));
        
        adminMenu.add(reportsItem);
        adminMenu.add(settingsItem);
        menuBar.add(adminMenu);
    }

    private void setupManagerMenu(JMenuBar menuBar) {
        JMenu managerMenu = new JMenu("Manager");
        JMenuItem suppliersItem = new JMenuItem("Suppliers");
        JMenuItem promotionsItem = new JMenuItem("Promotions");
        
        suppliersItem.addActionListener(e -> showFeatureMessage("Supplier Management"));
        promotionsItem.addActionListener(e -> showFeatureMessage("Promotion Management"));
        
        managerMenu.add(suppliersItem);
        managerMenu.add(promotionsItem);
        menuBar.add(managerMenu);
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Welcome message
        JLabel welcomeLabel = new JLabel(
                "Welcome, " + currentUser.getFullName() + " (" + currentUser.getRole() + ")",
                SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(welcomeLabel, BorderLayout.NORTH);
        
        // Dashboard content based on role
        JTextArea dashboardContent = new JTextArea();
        dashboardContent.setEditable(false);
        dashboardContent.setFont(new Font("Monospaced", Font.PLAIN, 14));
        
        if (currentUser.getRole().equals("admin")) {
            dashboardContent.setText("ADMINISTRATOR DASHBOARD\n\n" +
                    "• System Overview\n" +
                    "• User Management\n" +
                    "• Sales Reports\n" +
                    "• System Configuration\n\n" +
                    "Quick Stats:\n" +
                    "Total Products: " + db.getAllProducts().size() + "\n" +
                    "Total Orders: " + db.getAllOrders().size() + "\n" +
                    "Total Users: " + db.getAllUsers().size());
        } else if (currentUser.getRole().equals("manager")) {
            dashboardContent.setText("MANAGER DASHBOARD\n\n" +
                    "• Inventory Status\n" +
                    "• Sales Analytics\n" +
                    "• Supplier Management\n" +
                    "• Staff Scheduling\n\n" +
                    "Quick Stats:\n" +
                    "Total Products: " + db.getAllProducts().size() + "\n" +
                    "Orders Today: " + getTodaysOrderCount() + "\n" +
                    "Low Stock Items: " + getLowStockCount());
        } else {
            dashboardContent.setText("CUSTOMER DASHBOARD\n\n" +
                    "• Browse Products\n" +
                    "• View Order History\n" +
                    "• Manage Account\n\n" +
                    "Your Recent Orders:\n" + getCustomerOrderSummary());
        }
        
        panel.add(new JScrollPane(dashboardContent), BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createProductsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Product table
        String[] columnNames = {"ID", "Name", "Price", "Stock", "Category"};
        List<Product> products = db.getAllProducts();
        Object[][] data = new Object[products.size()][5];
        
        for (int i = 0; i < products.size(); i++) {
            Product p = products.get(i);
            data[i][0] = p.getId();
            data[i][1] = p.getName();
            data[i][2] = String.format("$%.2f", p.getPrice());
            data[i][3] = p.getStock();
            data[i][4] = p.getCategory();
        }
        
        JTable productTable = new JTable(data, columnNames);
        productTable.setFillsViewportHeight(true);
        
        // Add table selection listener for product details
        productTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = productTable.getSelectedRow();
                if (row >= 0) {
                    int productId = (int) productTable.getValueAt(row, 0);
                    showProductDetails(productId);
                }
            }
        });
        
        // Add buttons based on role
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        if (currentUser.getRole().equals("customer")) {
            JButton addToCartButton = new JButton("Add to Cart");
            addToCartButton.addActionListener(e -> {
                int row = productTable.getSelectedRow();
                if (row >= 0) {
                    int productId = (int) productTable.getValueAt(row, 0);
                    addToCart(productId);
                } else {
                    JOptionPane.showMessageDialog(this, "Please select a product first!");
                }
            });
            buttonPanel.add(addToCartButton);
        }
        
        if (currentUser.getRole().equals("admin") || currentUser.getRole().equals("manager")) {
            JButton addProductButton = new JButton("Add Product");
            JButton editProductButton = new JButton("Edit Product");
            JButton deleteProductButton = new JButton("Delete Product");
            
            addProductButton.addActionListener(e -> showAddProductDialog());
            editProductButton.addActionListener(e -> {
                int row = productTable.getSelectedRow();
                if (row >= 0) {
                    int productId = (int) productTable.getValueAt(row, 0);
                    showEditProductDialog(productId);
                } else {
                    JOptionPane.showMessageDialog(this, "Please select a product first!");
                }
            });
            deleteProductButton.addActionListener(e -> {
                int row = productTable.getSelectedRow();
                if (row >= 0) {
                    int productId = (int) productTable.getValueAt(row, 0);
                    deleteProduct(productId);
                } else {
                    JOptionPane.showMessageDialog(this, "Please select a product first!");
                }
            });
            
            buttonPanel.add(addProductButton);
            buttonPanel.add(editProductButton);
            buttonPanel.add(deleteProductButton);
        }
        
        panel.add(new JScrollPane(productTable), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    private JPanel createCartPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        // Implementation for shopping cart
        panel.add(new JLabel("Shopping Cart (Implementation would go here)"), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createOrdersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        List<Order> orders;
        if (currentUser.getRole().equals("customer")) {
            orders = db.getOrdersForUser(currentUser.getUsername());
        } else {
            orders = db.getAllOrders();
        }
        
        String[] columnNames = {"Order ID", "Date", "Customer", "Total", "Status"};
        Object[][] data = new Object[orders.size()][5];
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm");
        
        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            data[i][0] = order.getId();
            data[i][1] = dateFormat.format(order.getOrderDate());
            data[i][2] = order.getCustomerUsername();
            data[i][3] = String.format("$%.2f", order.getTotal());
            data[i][4] = order.getStatus();
        }
        
        JTable orderTable = new JTable(data, columnNames);
        orderTable.setFillsViewportHeight(true);
        
        // Add table selection listener for order details
        orderTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = orderTable.getSelectedRow();
                if (row >= 0) {
                    int orderId = (int) orderTable.getValueAt(row, 0);
                    showOrderDetails(orderId);
                }
            }
        });
        
        // Add buttons based on role
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        if (currentUser.getRole().equals("customer")) {
            JButton newOrderButton = new JButton("Place New Order");
            newOrderButton.addActionListener(e -> showFeatureMessage("New Order"));
            buttonPanel.add(newOrderButton);
        }
        
        if (currentUser.getRole().equals("admin") || currentUser.getRole().equals("manager")) {
            JButton updateStatusButton = new JButton("Update Status");
            updateStatusButton.addActionListener(e -> {
                int row = orderTable.getSelectedRow();
                if (row >= 0) {
                    int orderId = (int) orderTable.getValueAt(row, 0);
                    updateOrderStatus(orderId);
                } else {
                    JOptionPane.showMessageDialog(this, "Please select an order first!");
                }
            });
            buttonPanel.add(updateStatusButton);
        }
        
        panel.add(new JScrollPane(orderTable), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    private JPanel createInventoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        // Implementation for inventory management
        panel.add(new JLabel("Inventory Management (Implementation would go here)"), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createUserManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        List<User> users = db.getAllUsers();
        String[] columnNames = {"Username", "Full Name", "Role"};
        Object[][] data = new Object[users.size()][3];
        
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            data[i][0] = user.getUsername();
            data[i][1] = user.getFullName();
            data[i][2] = user.getRole();
        }
        
        JTable userTable = new JTable(data, columnNames);
        userTable.setFillsViewportHeight(true);
        
        // Add buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addUserButton = new JButton("Add User");
        JButton editUserButton = new JButton("Edit User");
        JButton deleteUserButton = new JButton("Delete User");
        
        addUserButton.addActionListener(e -> showAddUserDialog());
        editUserButton.addActionListener(e -> {
            int row = userTable.getSelectedRow();
            if (row >= 0) {
                String username = (String) userTable.getValueAt(row, 0);
                showEditUserDialog(username);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a user first!");
            }
        });
        deleteUserButton.addActionListener(e -> {
            int row = userTable.getSelectedRow();
            if (row >= 0) {
                String username = (String) userTable.getValueAt(row, 0);
                deleteUser(username);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a user first!");
            }
        });
        
        buttonPanel.add(addUserButton);
        buttonPanel.add(editUserButton);
        buttonPanel.add(deleteUserButton);
        
        panel.add(new JScrollPane(userTable), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    // Helper methods for various operations
    private int getTodaysOrderCount() {
        // Simplified - in real app would filter by date
        return db.getAllOrders().size();
    }

    private int getLowStockCount() {
        int count = 0;
        for (Product p : db.getAllProducts()) {
            if (p.getStock() < 10) count++;
        }
        return count;
    }

    private String getCustomerOrderSummary() {
        List<Order> orders = db.getOrdersForUser(currentUser.getUsername());
        if (orders.isEmpty()) return "No recent orders";
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        StringBuilder sb = new StringBuilder();
        
        int maxOrders = Math.min(3, orders.size());
        for (int i = 0; i < maxOrders; i++) {
            Order order = orders.get(i);
            sb.append(String.format("#%d - %s - $%.2f - %s\n", 
                order.getId(), 
                dateFormat.format(order.getOrderDate()), 
                order.getTotal(), 
                order.getStatus()));
        }
        
        if (orders.size() > 3) {
            sb.append("... and " + (orders.size() - 3) + " more");
        }
        
        return sb.toString();
    }

    private void showProductDetails(int productId) {
        Product product = db.getProductById(productId);
        if (product == null) return;
        
        String message = String.format(
            "Product Details\n\n" +
            "ID: %d\n" +
            "Name: %s\n" +
            "Price: $%.2f\n" +
            "Stock: %d\n" +
            "Category: %s\n\n" +
            "Description:\n%s",
            product.getId(),
            product.getName(),
            product.getPrice(),
            product.getStock(),
            product.getCategory(),
            product.getDescription());
        
        JOptionPane.showMessageDialog(this, message, "Product Details", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showOrderDetails(int orderId) {
        // Implementation would show detailed order information
        JOptionPane.showMessageDialog(this, "Details for order #" + orderId, "Order Details", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showAddProductDialog() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
        
        JTextField nameField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField stockField = new JTextField();
        JTextField categoryField = new JTextField();
        JTextArea descriptionArea = new JTextArea(3, 20);
        
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Price:"));
        panel.add(priceField);
        panel.add(new JLabel("Stock:"));
        panel.add(stockField);
        panel.add(new JLabel("Category:"));
        panel.add(categoryField);
        panel.add(new JLabel("Description:"));
        panel.add(new JScrollPane(descriptionArea));
        
        int result = JOptionPane.showConfirmDialog(
            this, panel, "Add New Product", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText();
                double price = Double.parseDouble(priceField.getText());
                int stock = Integer.parseInt(stockField.getText());
                String category = categoryField.getText();
                String description = descriptionArea.getText();
                
                Product product = new Product(name, price, stock, category, description);
                db.addProduct(product);
                
                refreshProductsTab();
                JOptionPane.showMessageDialog(this, "Product added successfully!");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid number format!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showEditProductDialog(int productId) {
        Product product = db.getProductById(productId);
        if (product == null) return;
        
        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
        
        JTextField nameField = new JTextField(product.getName());
        JTextField priceField = new JTextField(String.valueOf(product.getPrice()));
        JTextField stockField = new JTextField(String.valueOf(product.getStock()));
        JTextField categoryField = new JTextField(product.getCategory());
        JTextArea descriptionArea = new JTextArea(product.getDescription(), 3, 20);
        
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Price:"));
        panel.add(priceField);
        panel.add(new JLabel("Stock:"));
        panel.add(stockField);
        panel.add(new JLabel("Category:"));
        panel.add(categoryField);
        panel.add(new JLabel("Description:"));
        panel.add(new JScrollPane(descriptionArea));
        
        int result = JOptionPane.showConfirmDialog(
            this, panel, "Edit Product", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                product.setName(nameField.getText());
                product.setPrice(Double.parseDouble(priceField.getText()));
                product.setStock(Integer.parseInt(stockField.getText()));
                product.setCategory(categoryField.getText());
                product.setDescription(descriptionArea.getText());
                
                db.updateProduct(product);
                refreshProductsTab();
                JOptionPane.showMessageDialog(this, "Product updated successfully!");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid number format!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteProduct(int productId) {
        int confirm = JOptionPane.showConfirmDialog(
            this, 
            "Are you sure you want to delete this product?", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (db.deleteProduct(productId)) {
                refreshProductsTab();
                JOptionPane.showMessageDialog(this, "Product deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete product!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void addToCart(int productId) {
        // Simplified - in real app would add to a shopping cart
        Product product = db.getProductById(productId);
        if (product != null) {
            JOptionPane.showMessageDialog(this, 
                "Added " + product.getName() + " to cart", 
                "Cart Update", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void updateOrderStatus(int orderId) {
        // Simplified - in real app would update order status in database
        String[] options = {"Processing", "Shipped", "Delivered", "Cancelled"};
        String newStatus = (String) JOptionPane.showInputDialog(
            this, 
            "Select new status for order #" + orderId, 
            "Update Order Status", 
            JOptionPane.PLAIN_MESSAGE, 
            null, 
            options, 
            options[0]);
        
        if (newStatus != null) {
            JOptionPane.showMessageDialog(this, 
                "Order #" + orderId + " status updated to: " + newStatus, 
                "Status Updated", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void showAddUserDialog() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        
        JTextField usernameField = new JTextField();
        JTextField fullNameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JComboBox<String> roleComboBox = new JComboBox<>(new String[]{"customer", "manager", "admin"});
        
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Full Name:"));
        panel.add(fullNameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel("Role:"));
        panel.add(roleComboBox);
        
        int result = JOptionPane.showConfirmDialog(
            this, panel, "Add New User", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            String fullName = fullNameField.getText();
            String password = new String(passwordField.getPassword());
            String role = (String) roleComboBox.getSelectedItem();
            
            if (db.register(username, password, role, fullName)) {
                refreshUserManagementTab();
                JOptionPane.showMessageDialog(this, "User added successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Username already exists!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showEditUserDialog(String username) {
        User user = null;
        for (User u : db.getAllUsers()) {
            if (u.getUsername().equals(username)) {
                user = u;
                break;
            }
        }
        if (user == null) return;
        
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        
        JTextField usernameField = new JTextField(user.getUsername());
        usernameField.setEditable(false);
        JTextField fullNameField = new JTextField(user.getFullName());
        JPasswordField passwordField = new JPasswordField();
        JComboBox<String> roleComboBox = new JComboBox<>(new String[]{"customer", "manager", "admin"});
        roleComboBox.setSelectedItem(user.getRole());
        
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Full Name:"));
        panel.add(fullNameField);
        panel.add(new JLabel("New Password:"));
        panel.add(passwordField);
        panel.add(new JLabel("Role:"));
        panel.add(roleComboBox);
        
        int result = JOptionPane.showConfirmDialog(
            this, panel, "Edit User", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String newFullName = fullNameField.getText();
            String newPassword = new String(passwordField.getPassword());
            String newRole = (String) roleComboBox.getSelectedItem();
            
            // Only update password if it's not empty
            if (!newPassword.isEmpty()) {
                user.setPassword(newPassword);
            }
            
            user.setFullName(newFullName);
            user.setRole(newRole);
            
            if (db.updateUser(user)) {
                refreshUserManagementTab();
                JOptionPane.showMessageDialog(this, "User updated successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update user!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteUser(String username) {
        if (username.equals(currentUser.getUsername())) {
            JOptionPane.showMessageDialog(this, "You cannot delete your own account!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(
            this, 
            "Are you sure you want to delete user '" + username + "'?", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (db.deleteUser(username)) {
                refreshUserManagementTab();
                JOptionPane.showMessageDialog(this, "User deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete user!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void refreshProductsTab() {
        tabbedPane.setComponentAt(1, createProductsPanel());
    }

    private void refreshUserManagementTab() {
        if (currentUser.getRole().equals("admin")) {
            tabbedPane.setComponentAt(tabbedPane.indexOfTab("User Management"), createUserManagementPanel());
        }
    }

    private void showAboutDialog() {
        String aboutMessage = "Shopping Manager System\n" +
                             "Version 2.0\n\n" +
                             "A comprehensive retail management solution\n" +
                             "© 2025 RetailSoft Inc.";
        
        JOptionPane.showMessageDialog(this, aboutMessage, "About", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showFeatureMessage(String feature) {
        JOptionPane.showMessageDialog(this, 
            feature + " functionality would be fully implemented in the complete system", 
            "Feature: " + feature, 
            JOptionPane.INFORMATION_MESSAGE);
    }
}
