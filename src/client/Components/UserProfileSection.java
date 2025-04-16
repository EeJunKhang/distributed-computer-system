package client.Components;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import model.Customer;

public class UserProfileSection extends JPanel {
    private Customer customer;
    private TextField firstNameField, lastNameField, usernameField, emailField, addressField, contactNumberField;

    public UserProfileSection(Customer customer) {
        this.customer = customer;
        initializeUI();
        loadCustomerData();
    }
    
    public UserProfileSection(){
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Main form panel
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 15, 15));
        
//        // User ID (non-editable)
//        formPanel.add(new JLabel("User ID:"));
//        JLabel userIdLabel = new JLabel(String.valueOf(customer.getUserId()));
//        userIdLabel.setForeground(Color.GRAY);
//        formPanel.add(userIdLabel);

        // First Name
        formPanel.add(new JLabel("First Name:"));
        firstNameField = new TextField("", 20);
        firstNameField.setPlaceholder("Enter first name");
        formPanel.add(firstNameField);

        // Last Name
        formPanel.add(new JLabel("Last Name:"));
        lastNameField = new TextField("", 20);
        lastNameField.setPlaceholder("Enter last name");
        formPanel.add(lastNameField);

        // Username
        formPanel.add(new JLabel("Username:"));
        usernameField = new TextField("", 20);
        usernameField.setPlaceholder("Enter username");
        formPanel.add(usernameField);

        // Email
        formPanel.add(new JLabel("Email:"));
        emailField = new TextField("", 20);
        emailField.setPlaceholder("Enter email address");
        formPanel.add(emailField);

        // Address
        formPanel.add(new JLabel("Address:"));
        addressField = new TextField("", 20);
        addressField.setPlaceholder("Enter street address");
        formPanel.add(addressField);

        // Contact Number
        formPanel.add(new JLabel("Contact Number:"));
        contactNumberField = new TextField("", 20);
        contactNumberField.setPlaceholder("Enter phone number");
        formPanel.add(contactNumberField);

        // Created Time (non-editable)
//        formPanel.add(new JLabel("Member Since:"));
//        JLabel createdTimeLabel = new JLabel(customer.getCreatedTime());
//        createdTimeLabel.setForeground(Color.GRAY);
//        formPanel.add(createdTimeLabel);

        // Save button
        JButton saveButton = new JButton("Save Changes");
        saveButton.addActionListener(this::handleSave);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(saveButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadCustomerData() {
        firstNameField.setText(customer.getFirstName());
        lastNameField.setText(customer.getLastName());
        usernameField.setText(customer.getUsername());
        emailField.setText(customer.getEmail());
        addressField.setText(customer.getAddress());
        contactNumberField.setText(customer.getContactNumber());
    }

    private void handleSave(ActionEvent e) {
        // Update customer object with new values
        customer.setFirstName(firstNameField.getTextValue());
        customer.setLastName(lastNameField.getTextValue());
        customer.setUsername(usernameField.getTextValue());
        customer.setEmail(emailField.getTextValue());
        customer.setAddress(addressField.getTextValue());
        customer.setContactNumber(contactNumberField.getTextValue());

        // Show confirmation
        JOptionPane.showMessageDialog(this,
            "Profile updated successfully!",
            "Success",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
}
