# Java Desktop GUI Product App

## Features
- Users can enter product details: **Title**, **Description**, **Price**
- Displays a styled list of all added products
- Uses in-memory storage with Java collections (no database yet)

## Components
- A form to add products
- A display area to show all added products
- Local storage using `ArrayList` (for simplicity, we may add a database later)

## Tech Stack
- Java Swing for UI
- Java Collections (`ArrayList`) for in-memory data storage

## Code

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

class Product {
    String title, description;
    double price;

    public Product(String title, String description, double price) {
        this.title = title;
        this.description = description;
        this.price = price;
    }
    public String toString() {
        return "<html><b>" + title + "</b><br/>" + description + "<br/>₹" + price + "<br/><br/></html>";
    }
}

public class EbayClone extends JFrame {
    private JTextField titleField, priceField;
    private JTextArea descField;
    private DefaultListModel<Product> productListModel;

    public EbayClone() {
        setTitle("eBay Clone - Swing Version");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Input Panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(6, 1));

        titleField = new JTextField();
        descField = new JTextArea(3, 20);
        priceField = new JTextField();

        JButton addButton = new JButton("Add Product");

        inputPanel.add(new JLabel("Product Title:"));
        inputPanel.add(titleField);
        inputPanel.add(new JLabel("Description:"));
        inputPanel.add(new JScrollPane(descField));
        inputPanel.add(new JLabel("Price:"));
        inputPanel.add(priceField);
        inputPanel.add(addButton);

        // Product List Panel
        productListModel = new DefaultListModel<>();
        JList<Product> productList = new JList<>(productListModel);
        productList.setCellRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setText(value.toString());
                label.setVerticalAlignment(SwingConstants.TOP);
                return label;
            }
        });

        JScrollPane listScrollPane = new JScrollPane(productList);

        add(inputPanel, BorderLayout.NORTH);
        add(listScrollPane, BorderLayout.CENTER);

        // Button Action
        addButton.addActionListener(e -> {
            String title = titleField.getText();
            String desc = descField.getText();
            String priceText = priceField.getText();

            if (!title.isEmpty() && !priceText.isEmpty()) {
                try {
                    double price = Double.parseDouble(priceText);
                    productListModel.addElement(new Product(title, desc, price));
                    titleField.setText("");
                    descField.setText("");
                    priceField.setText("");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Enter a valid price!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Title and price are required!");
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EbayClone::new);
    }
}


