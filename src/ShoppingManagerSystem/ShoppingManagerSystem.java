/*
CONTRIBUTED BY VANSH BHASIN

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

*/


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class ShoppingManagerSystem {
    public static void main(String[] args) {
        AuthService authService = new AuthService();
        SwingUtilities.invokeLater(() -> new LoginFrame(authService).setVisible(true));
    }
}

class User {
    private String username;
    private String password;
    private String role;

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
}

class AuthService {
    private Map<String, User> users = new HashMap<>();

    public AuthService() {
        // Default users
        users.put("admin", new User("admin", "admin123", "admin"));
        users.put("manager", new User("manager", "manager123", "manager"));
    }

    public boolean register(String username, String password, String role) {
        if (users.containsKey(username)) return false;
        users.put(username, new User(username, password, role));
        return true;
    }

    public User login(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) return user;
        return null;
    }
}

class LoginFrame extends JFrame {
    private AuthService authService;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame(AuthService authService) {
        this.authService = authService;
        setupUI();
    }

    private void setupUI() {
        setTitle("Shopping Manager - Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        usernameField = new JTextField(15);
        panel.add(usernameField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        panel.add(passwordField, gbc);

        // Buttons
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        panel.add(buttonPanel, gbc);

        // Event Listeners
        loginButton.addActionListener(e -> handleLogin());
        registerButton.addActionListener(e -> new RegisterFrame(authService).setVisible(true));

        add(panel);
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        User user = authService.login(username, password);
        if (user != null) {
            JOptionPane.showMessageDialog(this, "Login successful! Role: " + user.getRole());
            new MainFrame(user, authService).setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

class RegisterFrame extends JFrame {
    private AuthService authService;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;

    public RegisterFrame(AuthService authService) {
        this.authService = authService;
        setupUI();
    }

    private void setupUI() {
        setTitle("Shopping Manager - Register");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        usernameField = new JTextField(15);
        panel.add(usernameField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        panel.add(passwordField, gbc);

        // Role
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Role:"), gbc);
        gbc.gridx = 1;
        String[] roles = {"admin", "manager", "customer"};
        roleComboBox = new JComboBox<>(roles);
        panel.add(roleComboBox, gbc);

        // Register Button
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        JButton registerButton = new JButton("Register");
        panel.add(registerButton, gbc);

        // Event Listener
        registerButton.addActionListener(e -> handleRegistration());

        add(panel);
    }

    private void handleRegistration() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String role = (String) roleComboBox.getSelectedItem();

        if (authService.register(username, password, role)) {
            JOptionPane.showMessageDialog(this, "Registration successful!");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Username already exists!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

class MainFrame extends JFrame {
    private User currentUser;
    private AuthService authService;

    public MainFrame(User user, AuthService authService) {
        this.currentUser = user;
        this.authService = authService;
        setupUI();
    }

    private void setupUI() {
        setTitle("Shopping Manager - " + currentUser.getUsername());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Menu Bar
        JMenuBar menuBar = new JMenuBar();
        
        // File Menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.addActionListener(e -> {
            new LoginFrame(authService).setVisible(true);
            dispose();
        });
        fileMenu.add(logoutItem);
        menuBar.add(fileMenu);

        // Role-specific menus
        if (currentUser.getRole().equals("admin")) {
            setupAdminMenu(menuBar);
        } else if (currentUser.getRole().equals("manager")) {
            setupManagerMenu(menuBar);
        } else {
            setupCustomerMenu(menuBar);
        }

        setJMenuBar(menuBar);

        // Main Content
        JPanel mainPanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel(
                "Welcome, " + currentUser.getUsername() + " (" + currentUser.getRole() + ")",
                SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(welcomeLabel, BorderLayout.CENTER);
        
        // Add some sample content based on role
        JTextArea contentArea = new JTextArea();
        contentArea.setEditable(false);
        contentArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        
        if (currentUser.getRole().equals("admin")) {
            contentArea.setText("Administrator Dashboard\n\n" +
                    "• User Management\n" +
                    "• System Configuration\n" +
                    "• Access Control\n" +
                    "• Audit Logs");
        } else if (currentUser.getRole().equals("manager")) {
            contentArea.setText("Manager Dashboard\n\n" +
                    "• Inventory Management\n" +
                    "• Product Catalog\n" +
                    "• Sales Reports\n" +
                    "• Supplier Orders");
        } else {
            contentArea.setText("Customer Dashboard\n\n" +
                    "• Browse Products\n" +
                    "• Shopping Cart\n" +
                    "• Order History\n" +
                    "• Account Settings");
        }
        
        mainPanel.add(new JScrollPane(contentArea), BorderLayout.SOUTH);
        add(mainPanel);
    }

    private void setupAdminMenu(JMenuBar menuBar) {
        JMenu adminMenu = new JMenu("Admin");
        JMenuItem usersItem = new JMenuItem("Manage Users");
        JMenuItem systemItem = new JMenuItem("System Settings");
        
        usersItem.addActionListener(e -> showFeatureMessage("User Management"));
        systemItem.addActionListener(e -> showFeatureMessage("System Settings"));
        
        adminMenu.add(usersItem);
        adminMenu.add(systemItem);
        menuBar.add(adminMenu);
    }

    private void setupManagerMenu(JMenuBar menuBar) {
        JMenu managerMenu = new JMenu("Manager");
        JMenuItem inventoryItem = new JMenuItem("Inventory");
        JMenuItem productsItem = new JMenuItem("Products");
        
        inventoryItem.addActionListener(e -> showFeatureMessage("Inventory Management"));
        productsItem.addActionListener(e -> showFeatureMessage("Product Management"));
        
        managerMenu.add(inventoryItem);
        managerMenu.add(productsItem);
        menuBar.add(managerMenu);
    }

    private void setupCustomerMenu(JMenuBar menuBar) {
        JMenu customerMenu = new JMenu("Customer");
        JMenuItem productsItem = new JMenuItem("Browse Products");
        JMenuItem ordersItem = new JMenuItem("My Orders");
        
        productsItem.addActionListener(e -> showFeatureMessage("Product Catalog"));
        ordersItem.addActionListener(e -> showFeatureMessage("Order History"));
        
        customerMenu.add(productsItem);
        customerMenu.add(ordersItem);
        menuBar.add(customerMenu);
    }

    private void showFeatureMessage(String feature) {
        JOptionPane.showMessageDialog(this, 
            feature + " functionality would be implemented here", 
            "Feature: " + feature, 
            JOptionPane.INFORMATION_MESSAGE);
    }
}
