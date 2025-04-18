package utils;

import java.awt.Component;
import java.awt.Dialog;
import javax.swing.*;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

public class BackgroundTaskWithLoading<T> {

    private final Component parentFrame;
    private final Callable<T> backgroundTask;
    private final Consumer<T> onDone;
    private final String loadingMessage;

    public BackgroundTaskWithLoading(Component parentFrame, String loadingMessage,
            Callable<T> backgroundTask, Consumer<T> onDone) {
        this.parentFrame = parentFrame;
        this.loadingMessage = loadingMessage;
        this.backgroundTask = backgroundTask;
        this.onDone = onDone;
    }

    public BackgroundTaskWithLoading(JFrame frame, String loadingMessage,
            Callable<T> backgroundTask, Consumer<T> onDone) {
        this((Component) frame, loadingMessage, backgroundTask, onDone);
    }

    public BackgroundTaskWithLoading(JDialog dialog, String loadingMessage,
            Callable<T> backgroundTask, Consumer<T> onDone) {
        this((Component) dialog, loadingMessage, backgroundTask, onDone);
    }

    public BackgroundTaskWithLoading(JPanel panel, String loadingMessage,
            Callable<T> backgroundTask, Consumer<T> onDone) {
        this((Component) panel, loadingMessage, backgroundTask, onDone);
    }

    public void execute() {
        // Setup loading dialog
        JDialog loadingDialog = new JDialog(SwingUtilities.getWindowAncestor(parentFrame), "Loading", Dialog.ModalityType.APPLICATION_MODAL);
        JLabel loadingLabel = new JLabel(loadingMessage != null ? loadingMessage : "Please wait...");
        loadingLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        loadingDialog.add(loadingLabel);
        loadingDialog.pack();
        loadingDialog.setLocationRelativeTo(parentFrame);

        // Define the SwingWorker
        SwingWorker<T, Void> worker = new SwingWorker<>() {
            @Override
            protected T doInBackground() throws Exception {
                return backgroundTask.call();
            }

            @Override
            protected void done() {
                loadingDialog.dispose(); // Close the loading dialog
                try {
                    T result = get();
                    onDone.accept(result);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(parentFrame,
                            "An unexpected error occurred.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        // Start the worker and show the dialog
        worker.execute();
        loadingDialog.setVisible(true); // This blocks until dialog is disposed
    }
}
