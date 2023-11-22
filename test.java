import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class test extends JFrame {

    public test() {
        super("Exit Prompt");

        // Create a label with the prompt message
        JLabel messageLabel = new JLabel("Do you want to exit the program?");
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setPreferredSize(new Dimension(300, 50));

        // Create buttons for confirmation and cancellation
        JButton exitButton = new JButton("Exit");
        JButton cancelButton = new JButton("Cancel");

        // Add action listener to the exit button
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Exiting the program.");
                System.exit(0);
            }
        });

        // Add action listener to the cancel button
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false); // Hide the prompt window
            }
        });

        // Create a panel to hold the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(exitButton);
        buttonPanel.add(cancelButton);

        // Create the main content pane
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(messageLabel, BorderLayout.CENTER);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        // Set the content pane of the JFrame
        setContentPane(contentPane);

        // Set JFrame properties
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 150);
        setLocationRelativeTo(null); // Center the frame on the screen
        setResizable(false); // Disable frame resizing
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                test frame = new test();
                frame.setVisible(true);
            }
        });
    }
}
