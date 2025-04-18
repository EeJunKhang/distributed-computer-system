package client.Components;

import client.ProductClient;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.imageio.ImageIO;
import javax.swing.border.EmptyBorder;
import model.AuthToken;
import model.Products;

/**
 * CustomTabbedPanel - A JPanel that mimics JTabbedPane functionality but with
 * fully tab headers and content panels.
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
    private AuthToken token;

    // Add setter method
    public void setFoodItemSection(FoodItemSection foodItemSection, AuthToken token) {
        this.page = foodItemSection;
        this.token = token;
        initializeDependentComponents();
    }

    private void initializeDependentComponents() {
        ImageIcon newcomersIcon = createIcon("src/resources/new.png", 24);
        ImageIcon bestSellersIcon = createIcon("src/resources/star.png", 24);
        ImageIcon allIcon = createIcon("src/resources/menu.png", 24);

        // Add tabs with content
        this.addTab("All", allIcon, createLoadingPanel());
        this.addTab("Newcomers", newcomersIcon, createLoadingPanel());
        this.addTab("Best Sellers", bestSellersIcon, createLoadingPanel());

        // Start fetching data
        fetchDataForTabs();
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
     *
     */
    public MenuPanel() {
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

        // Create a container panel for dynamic content updates
        JPanel contentContainer = new JPanel(new BorderLayout());
        contentContainer.setOpaque(true);
        contentContainer.add(content, BorderLayout.CENTER);

        // Create new tab item
        TabItem tabItem = new TabItem(title, icon, contentContainer);
        tabItems.add(tabItem);

        // Add header to header panel
        headerPanel.add(tabItem.getHeaderComponent());

        // Add content to content panel
        contentPanel.add(contentContainer, String.valueOf(index));

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
        if (foodItems.length > 0) {
            for (int i = 0; i < foodItems.length; i++) {
                gridPanel.add(new FoodItemPanel(foodItems[i], this.page));
            }

        }

        // Add the grid to a scroll pane
        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    public void refreshProductData() {
        fetchDataForTabs();
        replaceTabContent(0, createLoadingPanel());
        replaceTabContent(1, createLoadingPanel());
        replaceTabContent(2, createLoadingPanel());

    }

    private void fetchDataForTabs() {
        if (token == null) {
            System.err.println("Error: Token is null, cannot fetch data");
            replaceTabContent(0, createErrorPanel());
            replaceTabContent(1, createErrorPanel());
            replaceTabContent(2, createErrorPanel());
            return;
        }
        ProductClient productClient = new ProductClient(token);
        SwingWorker<List<Products>, Void> allWorker = new SwingWorker<>() {
            @Override
            protected List<Products> doInBackground() throws Exception {
                return productClient.fetchAllProduct(true);
            }

            @Override
            protected void done() {
                updateTabContent(0, this);
            }
        };
        allWorker.execute();

        SwingWorker<List<Products>, Void> newcomersWorker = new SwingWorker<>() {
            @Override
            protected List<Products> doInBackground() throws Exception {
                return productClient.fetchNewComerProduct();
            }

            @Override
            protected void done() {
                updateTabContent(1, this);
            }
        };
        newcomersWorker.execute();

        SwingWorker<List<Products>, Void> bestSellersWorker = new SwingWorker<>() {
            @Override
            protected List<Products> doInBackground() throws Exception {
                return productClient.fetchBestSellerProduct();
            }

            @Override
            protected void done() {
                updateTabContent(2, this);
            }
        };
        bestSellersWorker.execute();
    }

    private void updateTabContent(int tabIndex, SwingWorker<List<Products>, Void> worker) {
        try {
            List<Products> products = worker.get();
            SwingUtilities.invokeLater(() -> {
                Products[] productsArray = products.toArray(Products[]::new);
                JPanel gridPanel = createFoodGridPanel(productsArray);
                replaceTabContent(tabIndex, gridPanel);
            });
        } catch (InterruptedException | ExecutionException e) {
            SwingUtilities.invokeLater(() -> replaceTabContent(tabIndex, createErrorPanel()));
        }
    }

    private void replaceTabContent(int tabIndex, JPanel newContent) {
        TabItem tabItem = tabItems.get(tabIndex);
        JPanel contentComponent = tabItem.getContentComponent();
        contentComponent.removeAll();
        contentComponent.add(newContent, BorderLayout.CENTER);
        contentComponent.revalidate();
        contentComponent.repaint();
    }

    private JPanel createLoadingPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        JLabel label = new JLabel("Loading...", SwingConstants.CENTER);
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createErrorPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Error loading data.", SwingConstants.CENTER);
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }
}
