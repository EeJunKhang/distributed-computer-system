/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client.Components;

/**
 *
 * @author ejunk
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.border.EmptyBorder;
import model.Products;

/**
 * CustomTabbedPanel - A JPanel that mimics JTabbedPane functionality but with
 * fully customizable tab headers and content panels.
 */
public class MenuPanel extends JPanel {

    private final JPanel headerPanel;
    private final JPanel contentPanel;
    private final List<TabItem> tabItems;
    private int selectedIndex = 0;
    private Color headerBackgroundColor = new Color(255, 255, 255);
    private Color selectedTabColor = new Color(255, 128, 0);
    private Color unselectedTabColor = new Color(120, 120, 120);
    private Color indicatorColor = new Color(255, 128, 0);
    private FoodItemSection page;

    // Add setter method
    public void setFoodItemSection(FoodItemSection foodItemSection) {
        this.page = foodItemSection;
        initializeDependentComponents();
    }

    private void initializeDependentComponents() {
        //        ImageIcon nearbyIcon = createIcon("location", 24);
//        ImageIcon promotionIcon = createIcon("model/resources/", 24);
        ImageIcon newcomersIcon = createIcon("src/resources/new.png", 24);
        ImageIcon bestSellersIcon = createIcon("src/resources/star.png", 24);
        ImageIcon topRatedIcon = createIcon("src/resources/medal.png", 24);
        ImageIcon allIcon = createIcon("src/resources/menu.png", 24);
        
        // Add tabs with content
        this.addTab("All", allIcon, createAllItemsPanel());
        this.addTab("Newcomers", newcomersIcon, createNewcomersPanel());
        this.addTab("Best Sellers", bestSellersIcon, createBestSellersPanel());
        this.addTab("Top Rated", topRatedIcon, createTopRatedPanel());
    }
    /**
     * Class representing a tab item with its header and content
     */
    private class TabItem {

        private final JPanel headerComponent;
        private final JPanel contentComponent;
        private JLabel titleLabel;
        private ImageIcon icon;
        private JPanel indicatorPanel;

        public TabItem(String title, ImageIcon icon, JPanel content) {
            this.contentComponent = content;
            this.icon = icon;

            // Create header component
            headerComponent = new JPanel();
            headerComponent.setLayout(new BorderLayout());
            headerComponent.setOpaque(false);

            // Create title label
            titleLabel = new JLabel(title);
            titleLabel.setForeground(unselectedTabColor);
            titleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
            titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

            // Add icon if provided
            if (icon != null) {
                titleLabel.setIcon(icon);
                titleLabel.setHorizontalTextPosition(SwingConstants.CENTER);
                titleLabel.setVerticalTextPosition(SwingConstants.BOTTOM);
                titleLabel.setIconTextGap(5);
            }

            // Add label to header
            headerComponent.add(titleLabel, BorderLayout.CENTER);

            // Create indicator panel (the line or dot under selected tab)
            indicatorPanel = new JPanel();
            indicatorPanel.setPreferredSize(new Dimension(headerComponent.getWidth(), 3));
            indicatorPanel.setBackground(indicatorColor);
            indicatorPanel.setVisible(false);

            headerComponent.add(indicatorPanel, BorderLayout.SOUTH);

            // Add mouse listener for hover effects
            headerComponent.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (!indicatorPanel.isVisible()) {
                        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                        titleLabel.setForeground(new Color(80, 80, 80));
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (!indicatorPanel.isVisible()) {
                        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                        titleLabel.setForeground(unselectedTabColor);
                    }
                }
            });
        }

        public void setSelected(boolean selected) {
            if (selected) {
                titleLabel.setForeground(selectedTabColor);
                indicatorPanel.setVisible(true);
            } else {
                titleLabel.setForeground(unselectedTabColor);
                indicatorPanel.setVisible(false);
            }
        }

        public JPanel getHeaderComponent() {
            return headerComponent;
        }

        public JPanel getContentComponent() {
            return contentComponent;
        }
    }
    
    /**
     * Constructor
     */
    public MenuPanel() {
//        System.out.println(page.toString());
//        this.page = page;
        tabItems = new ArrayList<>();
        setLayout(new BorderLayout());

        // Create header panel
        headerPanel = new JPanel();
        headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        headerPanel.setBackground(headerBackgroundColor);
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));

        // Create content panel
        contentPanel = new JPanel();
        contentPanel.setBackground(Color.white);
        contentPanel.setLayout(new CardLayout());

        // Add panels to main panel
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        this.setColors(
                Color.WHITE, // header background
                new Color(255, 128, 0), // selected tab
                new Color(120, 120, 120), // unselected tab
                new Color(255, 128, 0) // indicator
        );
        
    }

    

    /**
     * Add a new tab with the specified title and content
     *
     * @param title The tab title
     * @param content The content panel
     * @return The index of the added tab
     */
    public int addTab(String title, JPanel content) {
        return addTab(title, null, content);
    }

    /**
     * Add a new tab with the specified title, icon, and content
     *
     * @param title The tab title
     * @param icon The tab icon
     * @param content The content panel
     * @return The index of the added tab
     */
    private int addTab(String title, ImageIcon icon, JPanel content) {
        final int index = tabItems.size();

        // Create new tab item
        TabItem tabItem = new TabItem(title, icon, content);
        tabItems.add(tabItem);

        // Add header to header panel
        headerPanel.add(tabItem.getHeaderComponent());

        // Add content to content panel
        contentPanel.add(content, String.valueOf(index));

        // Add click listener
        tabItem.getHeaderComponent().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setSelectedIndex(index);
            }
        });

        // If this is the first tab, select it
        if (index == 0) {
            tabItem.setSelected(true);
        }

        return index;
    }

    /**
     * Set the selected tab
     *
     * @param index The index of the tab to select
     */
    public void setSelectedIndex(int index) {
        if (index >= 0 && index < tabItems.size() && index != selectedIndex) {
            // Deselect current tab
            tabItems.get(selectedIndex).setSelected(false);

            // Select new tab
            selectedIndex = index;
            tabItems.get(selectedIndex).setSelected(true);

            // Show content
            CardLayout cl = (CardLayout) contentPanel.getLayout();
            cl.show(contentPanel, String.valueOf(index));

            // Repaint header
            headerPanel.repaint();
        }
    }

    /**
     * Get the selected tab index
     *
     * @return The selected tab index
     */
    public int getSelectedIndex() {
        return selectedIndex;
    }

    /**
     * Set the appearance colors
     *
     * @param headerBg Header background color
     * @param selectedTab Selected tab text color
     * @param unselectedTab Unselected tab text color
     * @param indicator Selected tab indicator color
     */
    private void setColors(Color headerBg, Color selectedTab, Color unselectedTab, Color indicator) {
        this.headerBackgroundColor = headerBg;
        this.selectedTabColor = selectedTab;
        this.unselectedTabColor = unselectedTab;
        this.indicatorColor = indicator;

        // Update header background
        headerPanel.setBackground(headerBackgroundColor);

        // Update tab colors
        for (int i = 0; i < tabItems.size(); i++) {
            TabItem item = tabItems.get(i);
            if (i == selectedIndex) {
                item.titleLabel.setForeground(selectedTabColor);
                item.indicatorPanel.setBackground(indicatorColor);
            } else {
                item.titleLabel.setForeground(unselectedTabColor);
                item.indicatorPanel.setBackground(indicatorColor);
            }
        }

        // Repaint
        repaint();
    }

    private ImageIcon createIcon(String imagePath, int size) {
        try {
            // Load image from file
            BufferedImage originalImage = ImageIO.read(new File(imagePath));

            // Scale the image to the requested size
            Image scaledImage = originalImage.getScaledInstance(size, size, Image.SCALE_SMOOTH);

            return new ImageIcon(scaledImage);
        } catch (IOException e) {
            return null; // Return null or a default icon if loading fails
        }
    }

    /**
     * Create a grid panel with food items
     */
    private JPanel createFoodGridPanel(Products[] foodItems) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));

        // Create a grid panel for food items
        JPanel gridPanel = new JPanel(new GridLayout(0, 3, 15, 15));
        gridPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        gridPanel.setBackground(new Color(245, 245, 245));

        // Add food items to the grid
        for (int i = 0; i < foodItems.length; i++) {
            gridPanel.add(new FoodItemPanel(foodItems[i], this.page));
        }

        // Add the grid to a scroll pane
        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // Create panels for each tab
//    private JPanel createNearbyPanel() {
//        String[] names = {
//            "Burger Mozza XL", "Veg Manchurian", "Fried Salad",
//            "Margherita Pizza", "Pasta Arrabiata", "Club Sandwich"
//        };
//
//        String[] prices = {"20", "39", "29", "25", "23", "15"};
//
//        String[] restaurants = {
//            "Burger Queen", "Burger Queen", "Burger Queen",
//            "Pizza Palace", "Pasta Paradise", "Sandwich Shop"
//        };
//
//        return createFoodGridPanel(names, prices, restaurants);
//    }
//    private JPanel createPromotionPanel() {
//        String[] names = {
//            "Double Cheeseburger", "Chicken Wings", "Taco Platter",
//            "Sushi Combo", "Veggie Bowl", "Ice Cream Sundae"
//        };
//
//        String[] prices = {"15", "22", "18", "30", "16", "12"};
//
//        String[] restaurants = {
//            "Burger Joint", "Wing World", "Taco Town",
//            "Sushi Spot", "Veggie Valley", "Sweet Treats"
//        };
//
//        return createFoodGridPanel(names, prices, restaurants);
//    }
    private JPanel createNewcomersPanel() {
        Products[] items = {
            new Products(1, "Truffle Fries", "Fry Factory", 31.3, "Category","src/resources/burger.jpg", 4, "24 /3/2025"),
            new Products(2, "Dragon Noodles", "Noodle House", 1.3, "Category","src/resources/burger.jpg", 6, "sda"),
            new Products(3, "Stuffed Mushrooms", "Mushroom Manor", 11.3, "category","src/resources/burger.jpg", 5,"ssd"),
            new Products(4, "Caramel Latte", "Coffee Corner", 21.3, "cateogyr","src/resources/burger.jpg", 400, "s"),
            new Products(5, "Açai Bowl", "Healthy Hut", 17.0, "Category","src/resources/burger.jpg", 40, "sda"),
            new Products(6, "Fancy Toast", "Toasty Times", 9.9, "cate","src/resources/burger.jpg", 341, "sd"),};
        return createFoodGridPanel(items);
    }

    private JPanel createBestSellersPanel() {
         Products[] items = {
            new Products(1, "Truffle Fries", "Fry Factory", 31.3, "Category","src/resources/burger.jpg", 4, "24 /3/2025"),
            new Products(2, "Dragon Noodles", "Noodle House", 1.3, "Category","src/resources/burger.jpg", 6, "sda"),
            new Products(3, "Stuffed Mushrooms", "Mushroom Manor", 11.3, "category","src/resources/burger.jpg", 5,"ssd"),
            new Products(4, "Caramel Latte", "Coffee Corner", 21.3, "cateogyr","src/resources/burger.jpg", 400, "s"),
            new Products(5, "Açai Bowl", "Healthy Hut", 17.0, "Category","src/resources/burger.jpg", 40, "sda"),
            new Products(6, "Fancy Toast", "Toasty Times", 9.9, "cate","src/resources/burger.jpg", 341, "sd"),};

        return createFoodGridPanel(items);
    }

    private JPanel createTopRatedPanel() {
        Products[] items = {
            new Products(1, "Truffle Fries", "Fry Factory", 31.3, "Category","src/resources/burger.jpg", 4, "24 /3/2025"),
            new Products(2, "Dragon Noodles", "Noodle House", 1.3, "Category","src/resources/burger.jpg", 6, "sda"),
            new Products(3, "Stuffed Mushrooms", "Mushroom Manor", 11.3, "category","src/resources/burger.jpg", 5,"ssd"),
            new Products(4, "Caramel Latte", "Coffee Corner", 21.3, "cateogyr","src/resources/burger.jpg", 400, "s"),
            new Products(5, "Açai Bowl", "Healthy Hut", 17.0, "Category","src/resources/burger.jpg", 40, "sda"),
            new Products(6, "Fancy Toast", "Toasty Times", 9.9, "cate","src/resources/burger.jpg", 341, "sd"),};

        return createFoodGridPanel(items);
    }

    private JPanel createAllItemsPanel() {
         Products[] items = {
            new Products(1, "Truffle Fries", "Fry Factory", 31.3, "Category","src/resources/burger.jpg", 4, "24 /3/2025"),
            new Products(2, "Dragon Noodles", "Noodle House", 1.3, "Category","src/resources/burger.jpg", 6, "sda"),
            new Products(3, "Stuffed Mushrooms", "Mushroom Manor", 11.3, "category","src/resources/burger.jpg", 5,"ssd"),
            new Products(4, "Caramel Latte", "Coffee Corner", 21.3, "cateogyr","src/resources/burger.jpg", 400, "s"),
            new Products(5, "Açai Bowl", "Healthy Hut", 17.0, "Category","src/resources/burger.jpg", 40, "sda"),
            new Products(6, "Açai Bowl123213", "Healthy Hut", 17.0, "Category","src/resources/burger.jpg", 40, "sda"),
            new Products(7, "Açai bow base", "Healthy Hut", 17.0, "Category","src/resources/burger.jpg", 40, "sda"),
            new Products(8, "Fancy Toast", "Toasty Times", 9.9, "cate","src/resources/burger.jpg", 341, "sd"),};

        return createFoodGridPanel(items);
    }
}
