//Basic UI, already combined with main src


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MarketplaceUI extends JFrame {
    public MarketplaceUI() {
        // Frame basics
        setTitle("Marketplace");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Top Panel (Marketplace + Nav)
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel logoLabel = new JLabel("Marketplace");
        logoLabel.setFont(new Font("Arial", Font.BOLD, 32));
        logoLabel.setForeground(new Color(220, 20, 60)); // Dark Red

        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 30, 10));
        navPanel.setBackground(Color.WHITE);
        String[] navItems = {"Buy", "Sell", "Bet&Buy"};
        for (String item : navItems) {
            JButton navButton = new JButton(item);
            navButton.setFocusPainted(false);
            navButton.setBackground(Color.WHITE);
            navButton.setForeground(Color.BLACK);
            navButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
            navButton.setFont(new Font("Arial", Font.PLAIN, 16));
            navButton.addActionListener(e -> JOptionPane.showMessageDialog(this, item + " page clicked!"));
            navPanel.add(navButton);
        }

        topPanel.add(logoLabel, BorderLayout.WEST);
        topPanel.add(navPanel, BorderLayout.EAST);

        // Search Bar Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
        searchPanel.setBackground(Color.WHITE);
        JTextField searchField = new JTextField(30);
        searchField.setPreferredSize(new Dimension(300, 30));
        JButton searchButton = new JButton("🔍");
        searchButton.setPreferredSize(new Dimension(50, 30));
        searchButton.setFocusPainted(false);
        searchButton.setBackground(Color.LIGHT_GRAY);

        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // Center Title
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

        // Category Grid
        JPanel categoryPanel = new JPanel(new GridLayout(2, 4, 20, 20));
        categoryPanel.setBackground(Color.WHITE);
        String[] categories = {"Electronics", "Clothing", "Home & Garden", "Sports",
                               "Toys", "Motors", "Collectibles", "Deals"};

        for (String cat : categories) {
            JButton catButton = new JButton(cat);
            catButton.setFocusPainted(false);
            catButton.setBackground(Color.WHITE);
            catButton.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
            catButton.setFont(new Font("Arial", Font.PLAIN, 16));
            catButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            catButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(null, "You clicked on " + cat);
                }
            });
            categoryPanel.add(catButton);
        }

        // Main Center Panel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(searchPanel, BorderLayout.NORTH);
        centerPanel.add(centerTitlePanel, BorderLayout.CENTER);
        centerPanel.add(categoryPanel, BorderLayout.SOUTH);

        // Add everything
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    public static void main(String[] args) {
        // Run app
        SwingUtilities.invokeLater(() -> new MarketplaceUI());
    }
}
