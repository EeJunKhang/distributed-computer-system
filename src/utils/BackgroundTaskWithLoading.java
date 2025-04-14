package utils;

import javax.swing.*;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

public class BackgroundTaskWithLoading<T> {
    private final JFrame parentFrame;
    private final Callable<T> backgroundTask;
    private final Consumer<T> onDone;
    private final String loadingMessage;

    public BackgroundTaskWithLoading(JFrame parentFrame, String loadingMessage,
                                     Callable<T> backgroundTask, Consumer<T> onDone) {
        this.parentFrame = parentFrame;
        this.loadingMessage = loadingMessage;
        this.backgroundTask = backgroundTask;
        this.onDone = onDone;
    }

    public void execute() {
        // Setup loading dialog
        JDialog loadingDialog = new JDialog(parentFrame, "Loading", true);
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

