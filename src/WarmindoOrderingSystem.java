import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class WarmindoOrderingSystem {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(WarmindoOrderingSystem::new);
    }

    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private List<MenuItem> foodMenu;
    private List<MenuItem> drinkMenu;
    private DefaultListModel<Order> orderListModel;
    private JLabel totalLabel;
    private List<String> selectedToppings = new ArrayList<>();
    private String selectedCookingOption;
    private int selectedSpiceLevel;

    public WarmindoOrderingSystem() {
        initializeMenus();
        createAndShowGUI();
    }

    private void initializeMenus() {
        foodMenu = new ArrayList<>();
        foodMenu.add(new MenuItem("Indomie Goreng", 12000));
        foodMenu.add(new MenuItem("Indomie Soto", 12000));
        foodMenu.add(new MenuItem("Indomie Kari Ayam", 13000));
        foodMenu.add(new MenuItem("Indomie Ayam Bawang", 14000));
        foodMenu.add(new MenuItem("Indomie Laksa", 15000));
        foodMenu.add(new MenuItem("Indomie Rendang", 16000));
        foodMenu.add(new MenuItem("Indomie Goreng Jumbo", 17000));
        foodMenu.add(new MenuItem("Indomie Seblak", 18000));
        foodMenu.add(new MenuItem("Indomie Spicy Korean", 21000));
        foodMenu.add(new MenuItem("Indomie Coto Makassar", 31000));
        foodMenu.add(new MenuItem("Nasi Goreng", 12000));
        foodMenu.add(new MenuItem("Sate - satean", 2000));

        drinkMenu = new ArrayList<>();
        drinkMenu.add(new MenuItem("Es Teh", 5000));
        drinkMenu.add(new MenuItem("Es Jeruk", 6000));
        drinkMenu.add(new MenuItem("Joshua", 7000));
        drinkMenu.add(new MenuItem("Milo", 8000));
    }

    private void createAndShowGUI() {
        frame = new JFrame("Warmindo Ordering System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(new Color(255, 223, 186)); // Warmindo beige color

        mainPanel.add(createWelcomePanel(), "Welcome");
        mainPanel.add(createMenuPanel("Food Menu", foodMenu), "FoodMenu");
        mainPanel.add(createMenuPanel("Drink Menu", drinkMenu), "DrinkMenu");
        mainPanel.add(createOrderSummaryPanel(), "OrderSummary");
        mainPanel.add(createCashierPanel(), "Cashier");

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout()); // Use GridBagLayout for full centering
        panel.setBackground(new Color(255, 223, 186)); // Warmindo beige color

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add spacing between components
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER; // Center alignment

        // Welcome message
        JLabel welcomeLabel = new JLabel("SELAMAT DATANG DI WARMINDO", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(new Color(255, 150, 0)); // Warmindo orange color
        panel.add(welcomeLabel, gbc);

        // Add some vertical space
        gbc.gridy++;
        panel.add(Box.createRigidArea(new Dimension(0, 20)), gbc);

        // Create a panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // Center the buttons
        buttonPanel.setBackground(new Color(255, 223, 186)); // Match the background color

        // Button to start ordering
        JButton startButton = new JButton("Mulai Pemesanan");
        startButton.setBackground(new Color(255, 150, 0)); // Warmindo orange color
        startButton.setForeground(Color.WHITE);
        startButton.addActionListener(e -> cardLayout.show(mainPanel, "FoodMenu"));
        buttonPanel.add(startButton);

        // Button to reset the order
        JButton resetButton = new JButton("Reset Pesanan");
        resetButton.setBackground(new Color(255, 150, 0)); // Warmindo orange color
        resetButton.setForeground(Color.WHITE);
        resetButton.addActionListener(e -> {
            orderListModel.clear(); // Clear all orders from the model
            totalLabel.setText("Total: IDR 0"); // Reset total price
            JOptionPane.showMessageDialog(frame, "Pesanan telah direset!");
        });
        buttonPanel.add(resetButton);

        // Add button panel to the main panel
        gbc.gridy++;
        panel.add(buttonPanel, gbc);

        return panel;
    }



    private JPanel createMenuPanel(String title, List<MenuItem> menu) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(title));

        DefaultListModel<MenuItem> menuModel = new DefaultListModel<>();
        menu.forEach(menuModel::addElement);
        JList<MenuItem> menuList = new JList<>(menuModel);
        menuList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(menuList);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton nextButton = new JButton("Pilih");
        nextButton.addActionListener(e -> {
            MenuItem selectedItem = menuList.getSelectedValue();
            if (selectedItem != null) {
                showItemCustomizationPopup(selectedItem, menu == foodMenu);
            }
        });
        panel.add(nextButton, BorderLayout.SOUTH);

        return panel;
    }

    private void showItemCustomizationPopup(MenuItem selectedItem, boolean isFood) {
        JPanel popupPanel = new JPanel(new BorderLayout());
        popupPanel.setBorder(BorderFactory.createTitledBorder("Customize " + selectedItem.getName()));

        if (isFood) {
            // Toppings with checkboxes
            JPanel toppingsPanel = new JPanel(new GridLayout(0, 1)); // Use a vertical layout for checkboxes
            String[] toppings = {"Nugget", "Sayur", "Kornet", "Telur"};
            JCheckBox[] toppingCheckboxes = new JCheckBox[toppings.length];

            // Create checkboxes for each topping
            for (int i = 0; i < toppings.length; i++) {
                toppingCheckboxes[i] = new JCheckBox(toppings[i]);
                toppingsPanel.add(toppingCheckboxes[i]);
            }

            JScrollPane toppingScrollPane = new JScrollPane(toppingsPanel);
            popupPanel.add(toppingScrollPane, BorderLayout.CENTER);

            // Cooking options dropdown
            String[] cookingOptions = {"Nyemek", "Goreng", "Kuah"};
            JComboBox<String> cookingComboBox = new JComboBox<>(cookingOptions);
            popupPanel.add(cookingComboBox, BorderLayout.NORTH);

            // Spice level slider
            JSlider spiceSlider = new JSlider(0, 10, 5);
            spiceSlider.setMajorTickSpacing(1);
            spiceSlider.setPaintTicks(true);
            spiceSlider.setPaintLabels(true);
            popupPanel.add(spiceSlider, BorderLayout.SOUTH);

            // Show the popup and handle the result
            int result = JOptionPane.showOptionDialog(frame, popupPanel, "Customize Item",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);

            if (result == JOptionPane.OK_OPTION) {
                // Collect selected toppings
                List<String> selectedToppings = new ArrayList<>();
                for (JCheckBox checkbox : toppingCheckboxes) {
                    if (checkbox.isSelected()) {
                        selectedToppings.add(checkbox.getText());
                    }
                }

                // Get selected cooking option and spice level
                String selectedCookingOption = (String) cookingComboBox.getSelectedItem();
                int selectedSpiceLevel = spiceSlider.getValue();

                // Add the order with selected toppings, cooking option, and spice level
                orderListModel.addElement(new Order(selectedItem, 1, selectedToppings, selectedCookingOption, selectedSpiceLevel));
                cardLayout.show(mainPanel, "DrinkMenu");
            }
        } else {
            int result = JOptionPane.showConfirmDialog(frame, "Do you want to add " + selectedItem.getName() + " to your order?",
                    "Confirm Drink Selection", JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION) {
                orderListModel.addElement(new Order(selectedItem, 1, new ArrayList<>(), null, 0));
                cardLayout.show(mainPanel, "OrderSummary");
            }
        }
    }

    private JPanel createOrderSummaryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Ringkasan Pesanan"));
        panel.setBackground(new Color(255, 223, 186)); // Warmindo beige color

        orderListModel = new DefaultListModel<>();
        JList<Order> orderList = new JList<>(orderListModel);

        JScrollPane scrollPane = new JScrollPane(orderList);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel summaryPanel = new JPanel();
        summaryPanel.setBackground(new Color(255, 223, 186)); // Warmindo beige color

        totalLabel = new JLabel("Total: IDR 0");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 18));
        totalLabel.setForeground(new Color(255, 150, 0)); // Warmindo orange color
        summaryPanel.add(totalLabel);

        JButton proceedToCashierButton = new JButton("Proceed to Cashier");
        proceedToCashierButton.addActionListener(e -> {
            updateTotalPrice();
            cardLayout.show(mainPanel, "Cashier");
        });
        summaryPanel.add(proceedToCashierButton);

        JButton loadButton = new JButton("Load Order from File");
        loadButton.addActionListener(e -> loadOrderFromFile());
        summaryPanel.add(loadButton);

        JButton saveButton = new JButton("Save Order to File");
        saveButton.addActionListener(e -> saveOrderToFile());
        summaryPanel.add(saveButton);

        // Tambahkan tombol "Tambah Pesanan"
        JButton addOrderButton = new JButton("Tambah Pesanan");
        addOrderButton.addActionListener(e -> cardLayout.show(mainPanel, "FoodMenu"));
        summaryPanel.add(addOrderButton);

        panel.add(summaryPanel, BorderLayout.SOUTH);

        return panel;
    }



    private void updateTotalPrice() {
        int total = 0;
        for (int i = 0; i < orderListModel.size(); i++) {
            Order order = orderListModel.getElementAt(i);
            total += order.getTotalPrice();
        }
        totalLabel.setText("Total: IDR " + total);
    }

    private void saveOrderToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("order.txt"))) {
            for (int i = 0; i < orderListModel.size(); i++) {
                Order order = orderListModel.getElementAt(i);
                writer.write(order.toString() + "\n");
            }
            JOptionPane.showMessageDialog(frame, "Pesanan berhasil disimpan!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Terjadi kesalahan saat menyimpan pesanan: " + e.getMessage());
        }
    }

    private void loadOrderFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("order.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Memasukkan data pesanan ke dalam orderListModel
                String[] orderDetails = line.split(" \\| ");
                String itemName = orderDetails[0].split(" x")[0];
                int quantity = Integer.parseInt(orderDetails[0].split(" x")[1].trim());
                List<String> toppings = new ArrayList<>();
                if (orderDetails.length > 1) {
                    String[] toppingsArray = orderDetails[1].split(": ")[1].split(", ");
                    for (String topping : toppingsArray) {
                        toppings.add(topping);
                    }
                }
                String cookingOption = orderDetails.length > 2 ? orderDetails[2].split(": ")[1] : null;
                int spiceLevel = orderDetails.length > 3 ? Integer.parseInt(orderDetails[3].split(": ")[1]) : 0;

                // Menambahkan Order ke dalam orderListModel
                MenuItem item = findMenuItemByName(itemName);
                if (item != null) {
                    orderListModel.addElement(new Order(item, quantity, toppings, cookingOption, spiceLevel));
                }
            }
            JOptionPane.showMessageDialog(frame, "Pesanan berhasil dimuat!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Terjadi kesalahan saat memuat pesanan: " + e.getMessage());
        }
    }

    private MenuItem findMenuItemByName(String name) {
        for (MenuItem item : foodMenu) {
            if (item.getName().equals(name)) {
                return item;
            }
        }
        for (MenuItem item : drinkMenu) {
            if (item.getName().equals(name)) {
                return item;
            }
        }
        return null;
    }

    private JPanel createCashierPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Kasir"));
        panel.setBackground(new Color(255, 223, 186)); // Warmindo beige color

        JTextArea receiptArea = new JTextArea(20, 40);
        receiptArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(receiptArea);

        JPanel inputPanel = new JPanel(new GridLayout(2, 1));
        JLabel nameLabel = new JLabel("Masukkan Nama Pelanggan:");
        JTextField nameField = new JTextField();
        inputPanel.add(nameLabel);
        inputPanel.add(nameField);

        JButton calculateButton = new JButton("Hitung Total");
        calculateButton.setBackground(new Color(255, 150, 0)); // Warmindo orange color
        calculateButton.setForeground(Color.WHITE);
        calculateButton.addActionListener(e -> {
            updateTotalPrice();
            StringBuilder receipt = new StringBuilder();
            String customerName = nameField.getText().trim();

            if (!customerName.isEmpty()) {
                receipt.append("Nama Pelanggan: ").append(customerName).append("\n\n");
            }
            for (int i = 0; i < orderListModel.size(); i++) {
                Order order = orderListModel.getElementAt(i);
                receipt.append(order.toString()).append(" | Harga: IDR ").append(order.getTotalPrice()).append("\n");
            }
            receipt.append("\nTotal: IDR ").append(calculateTotal());
            receiptArea.setText(receipt.toString());
        });

        JButton finishPaymentButton = new JButton("Selesaikan Pembayaran");
        finishPaymentButton.setBackground(new Color(255, 150, 0)); // Warmindo orange color
        finishPaymentButton.setForeground(Color.WHITE);
        finishPaymentButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Terima kasih atas pembelian Anda!");
            resetOrder();
            cardLayout.show(mainPanel, "Welcome");
        });

        JButton backButton = new JButton("Kembali ke Ringkasan");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "OrderSummary"));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(calculateButton);
        buttonPanel.add(finishPaymentButton);
        buttonPanel.add(backButton);

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }


    private void resetOrder() {
    }


    private int calculateTotal() {
        int total = 0;
        for (int i = 0; i < orderListModel.size(); i++) {
            total += orderListModel.get(i).getTotalPrice();
        }
        return total;
    }

    // Kelas MenuItem untuk item menu (makanan/minuman)
    private class MenuItem {
        private String name;
        private int price;

        public MenuItem(String name, int price) {
            this.name = name;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public int getPrice() {
            return price;
        }

        @Override
        public String toString() {
            return name + " - IDR " + price;
        }
    }

    // Kelas Order untuk menyimpan data pesanan
    private class Order {
        private MenuItem menuItem;
        private int quantity;
        private List<String> toppings;
        private String cookingOption;
        private int spiceLevel;

        public Order(MenuItem menuItem, int quantity, List<String> toppings, String cookingOption, int spiceLevel) {
            this.menuItem = menuItem;
            this.quantity = quantity;
            this.toppings = toppings;
            this.cookingOption = cookingOption;
            this.spiceLevel = spiceLevel;
        }

        public int getTotalPrice() {
            int total = menuItem.getPrice() * quantity;
            return total + (toppings.size() * 1000); // Topping extra cost (assuming 1000 per topping)
        }

        @Override
        public String toString() {
            StringBuilder orderDetails = new StringBuilder();
            orderDetails.append(menuItem.getName()).append(" x ").append(quantity);
            if (!toppings.isEmpty()) {
                orderDetails.append(" | Toppings: ").append(String.join(", ", toppings));
            }
            if (cookingOption != null) {
                orderDetails.append(" | Cooking Option: ").append(cookingOption);
            }
            if (spiceLevel > 0) {
                orderDetails.append(" | Spice Level: ").append(spiceLevel);
            }
            return orderDetails.toString();
        }
    }
}