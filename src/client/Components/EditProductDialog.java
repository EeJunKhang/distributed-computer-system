package client.Components;

// Edit Product Dialog
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import model.Products;

class EditProductDialog extends JDialog {

    private final int IMAGE_SIZE = 64;
    private Products originalProduct;
    private Products updatedProduct;
    private JTextField nameField, descField, priceField, categoryField, stockField;
    private JLabel imageLabel;
    private String imagePath;

    public EditProductDialog(Frame owner, Products product) {
        super(owner, "Edit Product", true);
        originalProduct = product;
        initializeUI();
        pack();
        setLocationRelativeTo(owner);
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setSize(500, 400);
        JPanel fieldsPanel = new JPanel(new GridLayout(0, 2, 10, 30));

        // Image upload
        JPanel imagePanel = new JPanel(new BorderLayout());
        imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(IMAGE_SIZE, IMAGE_SIZE));
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JButton uploadButton = new JButton("Upload Image");
        uploadButton.addActionListener(e -> handleImageUpload());
        imagePanel.add(imageLabel, BorderLayout.CENTER);
        imagePanel.add(uploadButton, BorderLayout.SOUTH);

        // Form fields
        nameField = new JTextField(originalProduct.getItemName());
        descField = new JTextField(originalProduct.getItemDescription());
        priceField = new JTextField(String.valueOf(originalProduct.getPrice()));
        categoryField = new JTextField(originalProduct.getCategory());
        stockField = new JTextField(String.valueOf(originalProduct.getStockQuantity()));
        imagePath = originalProduct.getImage();

        if (!imagePath.isEmpty()) {
            ImageIcon foodImage = new ImageIcon(imagePath);
            Image scaled = foodImage.getImage().getScaledInstance(IMAGE_SIZE, IMAGE_SIZE, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaled));
//            imageLabel.setIcon(new ImageIcon(imagePath).getImage().getScaledInstance(IMAGE_SIZE, IMAGE_SIZE, Image.SCALE_SMOOTH));
        }

        fieldsPanel.add(new JLabel("Product Name:"));
        fieldsPanel.add(nameField);
        fieldsPanel.add(new JLabel("Description:"));
        fieldsPanel.add(descField);
        fieldsPanel.add(new JLabel("Price:"));
        fieldsPanel.add(priceField);
        fieldsPanel.add(new JLabel("Category:"));
        fieldsPanel.add(categoryField);
        fieldsPanel.add(new JLabel("Stock Quantity:"));
        fieldsPanel.add(stockField);
        fieldsPanel.add(new JLabel("Image:"));
        fieldsPanel.add(imagePanel);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> {
            if (validateInputs()) {
                updatedProduct = createUpdatedProduct();
                setVisible(false);
            }
        });

        cancelButton.addActionListener(e -> setVisible(false));

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        add(fieldsPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void handleImageUpload() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter(
                "Image files", "jpg", "jpeg", "png", "gif"));
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            imagePath = file.getAbsolutePath();
            imageLabel.setIcon(new ImageIcon(new ImageIcon(imagePath)
                    .getImage().getScaledInstance(IMAGE_SIZE, IMAGE_SIZE, Image.SCALE_SMOOTH)));
        }
    }

    private boolean validateInputs() {
        try {
            Double.valueOf(priceField.getText());
            Integer.valueOf(stockField.getText());
            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Please enter valid numbers for price and stock",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private Products createUpdatedProduct() {
        return new Products(
                originalProduct.getId(),
                nameField.getText(),
                descField.getText(),
                Double.parseDouble(priceField.getText()),
                categoryField.getText(),
                imagePath,
                Integer.parseInt(stockField.getText()),
                originalProduct.getLastUpdated()
        );
    }

    public boolean showDialog() {
        setVisible(true);
        return updatedProduct != null;
    }

    public Products getUpdatedProduct() {
        return updatedProduct;
    }
}
