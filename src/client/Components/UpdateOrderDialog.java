package client.Components;

// Edit Product Dialog
import client.OrderClient;
import enums.OrderStatus;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import model.AuthToken;
import model.Order;
import model.OrderItem;
import utils.BackgroundTaskWithLoading;

class UpdateOrderDialog extends JDialog {

    private final Order originalOrder;
    @SuppressWarnings("rawtypes")
    private JComboBox dropDownField;
    @SuppressWarnings("rawtypes")
    private JList itemList;
    private final AuthToken token;
    private boolean success;

    public UpdateOrderDialog(Frame owner, Order order, AuthToken token) {
        super(owner, "Update Order Status", true);
        originalOrder = order;
        this.token = token;
        initializeUI();
        pack();
        setLocationRelativeTo(owner);
    }

    @SuppressWarnings("rawtypes")
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

        // Row 0: Order Id
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Order Id:"), gbc);
        gbc.gridx = 1;
//        nameField = new TextField(originalProduct.getItemName(), 20); // Set preferred width
//        nameField.setPlaceholder("Insert Name of Product");
        formPanel.add(new JLabel(String.valueOf(originalOrder.getOrderId())), gbc);

        // Row 1: Items
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Items:"), gbc);
        gbc.gridx = 1;
        DefaultListModel<OrderItem> listModel = new DefaultListModel<>();
        for (OrderItem orderItem : originalOrder.getItems()) {
            listModel.addElement(orderItem);
        }
        itemList = new JList(listModel);
        formPanel.add(itemList, gbc);

        // Row 2: Status
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Order Status:"), gbc);
        gbc.gridx = 1;

        OrderStatus[] statusList = OrderStatus.values();
        String[] statusListString = new String[statusList.length];

        for (int i = 0; i < statusList.length; i++) {
            statusListString[i] = statusList[i].toString();
        }

        dropDownField = new JComboBox(statusListString);
        dropDownField.setSelectedItem(originalOrder.getStatus().toString());
        formPanel.add(dropDownField, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(createButtonPanel(), BorderLayout.SOUTH);
        add(mainPanel);
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(_ -> updateOrder());

        cancelButton.addActionListener(_ -> setVisible(false));

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        return buttonPanel;
    }

    private void updateOrder() {
        String changeStatus = (String) dropDownField.getSelectedItem();
        OrderStatus newStatus = OrderStatus.valueOf(changeStatus);
        OrderClient controller = new OrderClient(token);
        new BackgroundTaskWithLoading<>(
                this,
                "Updating Order Status...",
                () -> controller.updateOrderStatus(token, originalOrder.getOrderId(), newStatus),
                isSuccess -> {
                    if (isSuccess) {
                        JOptionPane.showMessageDialog(this,
                                "You successfully updating order status",
                                "Updating Order Success",
                                JOptionPane.INFORMATION_MESSAGE);
//                            this.setVisible(true);
                        this.setSuccess(true);
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Something went wrong while updating order status",
                                "Updating Order Status Error",
                                JOptionPane.ERROR_MESSAGE);
//                            this.dispose(); // Close the window if data is missing
                        this.setSuccess(false);
                    }
                    setVisible(false);
                }
        ).execute();
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean showDialog() {
        setVisible(true);
        return success;
    }
}
