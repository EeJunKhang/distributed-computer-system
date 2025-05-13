package client.Components;

// Edit Product Dialog
import client.ProductClient;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
import model.AuthToken;
import model.Products;
import utils.BackgroundTaskWithLoading;
import utils.ImagePathGetter;

class AddProductDialog extends JDialog {

    private final int IMAGE_SIZE = 64;
//    private Products newProduct;
//    private Products updatedProduct;
    private TextField nameField, descField, priceField, categoryField, stockField;
    private JLabel imageLabel;
    private String imagePath;
    private final AuthToken token;

    public AddProductDialog(Frame owner, AuthToken token) {
        super(owner, "Add Product", true);
        this.token = token;
//        originalProduct = product;
        initializeUI();
        pack();
        setLocationRelativeTo(owner);
        setPreferredSize(new Dimension(500, 400));
    }

    private void initializeUI() {
        // Main panel with border for padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Create a panel for form fields (using GridBagLayout for more control)
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Padding around components
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 0: Product Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Product Name:"), gbc);
        gbc.gridx = 1;
        nameField = new TextField("", 20); // Set preferred width
        nameField.setPlaceholder("Insert Name of Product");
        formPanel.add(nameField, gbc);

        // Row 1: Description
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        descField = new TextField("", 20);
        descField.setPlaceholder("Insert Description of Product");
        formPanel.add(descField, gbc);

        // Row 2: Price
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Price:"), gbc);
        gbc.gridx = 1;
        priceField = new TextField("", 20);
        priceField.setPlaceholder("Insert Price of Product");
        formPanel.add(priceField, gbc);

        // Row 3: Category
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1;
        categoryField = new TextField("", 20);
        categoryField.setPlaceholder("Insert Category of Product");
        formPanel.add(categoryField, gbc);

        // Row 4: Stock
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Stock Quantity:"), gbc);
        gbc.gridx = 1;
        stockField = new TextField("", 20);
        stockField.setPlaceholder("Insert Stock of Product");

        formPanel.add(stockField, gbc);

        // Row 5: Image (spanning both columns)
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2; // Span 2 columns
        formPanel.add(createImagePanel(), gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(createButtonPanel(), BorderLayout.SOUTH);
        add(mainPanel);
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> addProduct());

        cancelButton.addActionListener(e -> setVisible(false));

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        return buttonPanel;
    }

    private void addProduct() {
        if (validateInputs()) {
//            newProduct = createNewProduct();
            ProductClient a = new ProductClient(token);
            new BackgroundTaskWithLoading<>(
                    this,
                    "Loading...",
                    () -> a.addNewProduct(createNewProduct()),
                    isSuccess -> {
                        if (isSuccess) {
                            JOptionPane.showMessageDialog(this,
                                    "You successfully adding new product",
                                    "Adding Product Success",
                                    JOptionPane.INFORMATION_MESSAGE);
//                            this.setVisible(true);
                        } else {
                            JOptionPane.showMessageDialog(this,
                                    "Something went wrong while adding new product",
                                    "Adding Product Error",
                                    JOptionPane.ERROR_MESSAGE);
//                            this.dispose(); // Close the window if data is missing
                        }
                        setVisible(false);
                    }
            ).execute();
        }
    }

    private JPanel createImagePanel() {
        JPanel imagePanel = new JPanel(new BorderLayout(5, 5));
        imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(150, 150)); // Larger size
        imageLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        JButton uploadButton = new JButton("Upload Image");
        uploadButton.addActionListener(e -> handleImageUpload());

        imagePanel.add(new JLabel("Image:"), BorderLayout.NORTH);
        imagePanel.add(imageLabel, BorderLayout.CENTER);
        imagePanel.add(uploadButton, BorderLayout.SOUTH);

        return imagePanel;
    }

    private void handleImageUpload() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter(
                "Image files", "jpg", "jpeg", "png", "gif"));
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            imagePath = file.getName();
            imageLabel.setIcon(createIcon(ImagePathGetter.getImageFullPath(imagePath), IMAGE_SIZE));
        }
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

    private boolean validateInputs() {
        StringBuilder errorMessages = new StringBuilder();

        // Check for empty fields
        checkFieldNotEmpty(nameField, "Product Name", errorMessages);
        checkFieldNotEmpty(descField, "Description", errorMessages);
        checkFieldNotEmpty(priceField, "Price", errorMessages);
        checkFieldNotEmpty(categoryField, "Category", errorMessages);
        checkFieldNotEmpty(stockField, "Stock Quantity", errorMessages);
        // Check for valid numbers
        if (!priceField.getTextValue().trim().isEmpty()) {
            try {
                Double.valueOf(priceField.getText());
            } catch (NumberFormatException e) {
                errorMessages.append("- Please enter a valid number for Price\n");
            }
        }

        if (!stockField.getTextValue().trim().isEmpty()) {
            try {
                Integer.valueOf(stockField.getText());
            } catch (NumberFormatException e) {
                errorMessages.append("- Please enter a valid whole number for Stock Quantity\n");
            }
        }

        if (imageLabel.getIcon() == null) {
            errorMessages.append("- Please insert an image of product\n");
        }

        // If any errors, show them all at once
        if (errorMessages.length() > 0) {
            JOptionPane.showMessageDialog(this,
                    "Please fix the following issues:\n\n" + errorMessages.toString(),
                    "Invalid Input",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    // Helper method to check if a field is empty
    private void checkFieldNotEmpty(TextField field, String fieldName, StringBuilder errorMessages) {
        if (field.getTextValue().trim().isEmpty()) {
            errorMessages.append("- ").append(fieldName).append(" cannot be empty\n");
        }
    }

    private Products createNewProduct() {
        return new Products(
                nameField.getText(),
                descField.getText(),
                Double.parseDouble(priceField.getText()),
                categoryField.getText(),
                imagePath,
                Integer.parseInt(stockField.getText())
        );
    }

//    public Products getNewProduct() {
//        return newProduct;
//    }
}
