package client.Components;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.BorderFactory;
import javax.swing.JPasswordField;
import javax.swing.border.Border;

public class PasswordField extends JPasswordField {
    
    private Color focusColor = new Color(66, 133, 244);
    private Color unfocusedColor = new Color(192, 192, 192);
    private Color textColor = new Color(50, 50, 50);
    private Color backgroundColor = new Color(245, 245, 245);
    private String placeholder = "";
    private Font textFont = new Font("Arial", Font.PLAIN, 14);
    
    /**
     * Interface for listening to text changes
     */
    public interface TextFieldListener {
        void onTextChanged(String newText);
    }
    
    /**
     * Default constructor
     */
    public PasswordField() {
        this("");
    }
    
    /**
     * Constructor with initial text
     * @param initialText the initial text for the field
     */
    public PasswordField(String initialText) {
        super(initialText);
        setupComponent();
    }
    
    /**
     * Constructor with initial text and column count
     * @param initialText the initial text for the field
     * @param columns the number of columns
     */
    public PasswordField(String initialText, int columns) {
        super(initialText, columns);
        setupComponent();
    }
    
    private void setupComponent() {
        setForeground(textColor);
        setBackground(backgroundColor);
        setFont(textFont);
        setCaretColor(focusColor);
        
        // Create a rounded border with padding
        Border roundedBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(unfocusedColor, 1, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        );
        setBorder(roundedBorder);
        
        // Add focus listeners to change border color
        addFocusListener(new FocusAdapter() {
            @SuppressWarnings("deprecation")
            @Override
            public void focusGained(FocusEvent e) {
                setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(focusColor, 2, true),
                        BorderFactory.createEmptyBorder(4, 9, 4, 9)
                ));
                
                // Clear placeholder if present
                if (getText().equals(placeholder) && !placeholder.isEmpty()) {
                    setText("");
                    setForeground(textColor);
                }
            }
            
            @SuppressWarnings("deprecation")
            @Override
            public void focusLost(FocusEvent e) {
                setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(unfocusedColor, 1, true),
                        BorderFactory.createEmptyBorder(5, 10, 5, 10)
                ));
                
                // Show placeholder if empty
                if (getText().isEmpty() && !placeholder.isEmpty()) {
                    setText(placeholder);
                    setForeground(new Color(150, 150, 150));
                }
            }
        });
    }
    
    /**
     * Set a placeholder text to display when the field is empty
     * @param placeholder the placeholder text
     */
    @SuppressWarnings("deprecation")
    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        
        // If the field is empty, show the placeholder
        if (getText().isEmpty() && !placeholder.isEmpty() && !isFocusOwner()) {
            setText(placeholder);
            setForeground(new Color(150, 150, 150));
        }
    }
    
    /**
     * Get the real text value (ignoring placeholder)
     * @return the actual text entered by the user
     */
    public String getTextValue() {
        @SuppressWarnings("deprecation")
        String text = getText();
        
        // If the current text is the placeholder, return empty string
        if (text.equals(placeholder)) {
            return "";
        }
        
        return text;
    }
    
    /**
     * Set custom colors for the text field
     * @param textColor the color of the text
     * @param backgroundColor the background color
     * @param focusColor the border color when focused
     */
    public void setCustomColors(Color textColor, Color backgroundColor, Color focusColor) {
        this.textColor = textColor;
        this.backgroundColor = backgroundColor;
        this.focusColor = focusColor;
        
        setForeground(textColor);
        setBackground(backgroundColor);
        setCaretColor(focusColor);
        
        // Update border if focused
        if (isFocusOwner()) {
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(focusColor, 2, true),
                    BorderFactory.createEmptyBorder(4, 9, 4, 9)
            ));
        }
    }
}
