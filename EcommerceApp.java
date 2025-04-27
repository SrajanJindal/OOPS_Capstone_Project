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
