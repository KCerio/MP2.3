import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.*;
import javax.swing.filechooser.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;


public class Main {
    public static class Frame {
        private final JTextArea codeArea;
        private JTextArea outputArea;
        private File savedFile;
        private boolean textChanged;

        public Frame() {
            JFrame mainFrame = new JFrame();

            codeArea = new JTextArea(10, 40);
            Font font = new Font("Times New Roman", Font.BOLD, 14);
            codeArea.setFont(font);
            codeArea.setBackground(Color.BLACK);
            codeArea.setForeground(Color.WHITE);
            codeArea.setSelectedTextColor(Color.BLACK);
            codeArea.setCaretColor(Color.WHITE);

            Icon saveIcon = new ImageIcon("save.png");
            Icon saveAsIcon = new ImageIcon("save_as.png");
            Icon openIcon = new ImageIcon("open.png");
            Icon runIcon = new ImageIcon("run.png");
            Icon compileIcon = new ImageIcon("compile.png");



            JButton run = new JButton(runIcon);
            JButton compile = new JButton(compileIcon);
            JButton open = new JButton(openIcon);
            JButton save = new JButton(saveIcon);
            JButton saveAs = new JButton(saveAsIcon);
            JButton openNew = new JButton("openNew");
            JButton cut= new JButton("cut");
            JButton copy= new JButton("copy");
            JButton paste= new JButton("paste");










            mainFrame.setSize(750, 750);
            mainFrame.setTitle("Survivors");
            mainFrame.setLocationRelativeTo(null); //Center the mainFrame


            Dimension buttonSize = new Dimension(50, 50); // Adjust the size as needed
            open.setPreferredSize(buttonSize);
            save.setPreferredSize(buttonSize);
            compile.setPreferredSize(buttonSize);
            run.setPreferredSize(buttonSize);
            saveAs.setPreferredSize(buttonSize);
            openNew.setPreferredSize(buttonSize);
            cut.setPreferredSize(buttonSize);
            copy.setPreferredSize(buttonSize);
            paste.setPreferredSize(buttonSize);


            // Create a panel for the buttons at the top
            JPanel buttonPanel = new JPanel(new FlowLayout());
            buttonPanel.add(open);
            buttonPanel.add(save);
            buttonPanel.add(compile);
            buttonPanel.add(run);
            buttonPanel.add(saveAs);
            buttonPanel.add(openNew);
            buttonPanel.add(cut);
            buttonPanel.add(copy);
            buttonPanel.add(paste);
            //save.setEnabled(false);

            //hover
            open.setToolTipText("Open existing file");
            save.setToolTipText("Save code");
            compile.setToolTipText("Compile");
            run.setToolTipText("Run code");
            saveAs.setToolTipText("Save As new file");
            openNew.setToolTipText("Open New File");
            cut.setToolTipText("Cut Text");
            copy.setToolTipText("Copy Text");
            paste.setToolTipText("Paste Text");


            // Wrap the codeArea in a JScrollPane
            JScrollPane scrollPane = new JScrollPane(codeArea);

            // Create a panel for the codeArea using BorderLayout
            JPanel codePanel = new JPanel(new BorderLayout());
            codePanel.add(scrollPane, BorderLayout.CENTER);

            // Add the buttonPanel and codePanel to the mainFrame
            mainFrame.add(buttonPanel, BorderLayout.NORTH);
            mainFrame.add(codePanel, BorderLayout.CENTER);


            //EXit
            if (save.isEnabled()) { //dili mo prompt if new and unsaved  file
                mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                mainFrame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        int choice = JOptionPane.showConfirmDialog(mainFrame, "File not saved. Do you want to save? ", "Exit Confirmation", JOptionPane.YES_NO_OPTION);
                        if (choice == JOptionPane.NO_OPTION) {
                            System.exit(0);
                        }
                        else{
                            System.out.println("WIll save");
                            performSave(savedFile);
                            System.exit(0);
                        }
                    }
                });
            }
            else{
                System.out.println("not work");
                mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            }



            mainFrame.setVisible(true);

            codeArea.getDocument().addDocumentListener(new DocumentListener() {

                @Override
                public void insertUpdate(DocumentEvent e) {
                    textChanged = true;
                    save.setEnabled(true);
                    //save.isEnabled(); //checking true or false
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    textChanged = true;
                    save.setEnabled(true);
                    //save.isEnabled();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    textChanged = true;
                    save.setEnabled(true);
                    //save.isEnabled();
                }
            });

            save.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                    if (savedFile != null) {
                        // File has been saved before, perform "Save" operation
                        performSave(savedFile);
                        save.setEnabled(false);
                        textChanged = false;
                    } else {
                        // File has not been saved before, perform "Save As" operation
                        performSaveAs();
                        save.setEnabled(false);
                    }
                }
            });

            saveAs.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // Always perform "Save As" operation
                    performSaveAs();
                }
            });

            open.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    performOpen();
                    save.setEnabled(false);
                }
            });

            run.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    performRun();
                }
            });

            openNew.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if(save.isEnabled()){
                        int choice = JOptionPane.showConfirmDialog(mainFrame, "File not saved. Do you want to save? ", "Exit Confirmation", JOptionPane.YES_NO_OPTION);
                        if (choice == JOptionPane.NO_OPTION) {
                            new Frame();
                        }
                        else{
                            //performSaveAs();
                            new Frame();
                        }
                    }else{
                        new Frame();
                    }

                }
            });

            cut.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    codeArea.cut();
                }
            });

            copy.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    codeArea.copy();
                }
            });

            paste.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    codeArea.paste();
                }
            });
        }




        private void performSave(File fileToSave) {
            try (PrintWriter writer = new PrintWriter(fileToSave)) {
                writer.print(codeArea.getText());
                savedFile = fileToSave; // Update the savedFile reference
                JOptionPane.showMessageDialog(null, "File saved successfully!");
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error saving the file!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void performSaveAs() {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save File");

            // Add a file filter to accept only .surv files
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Survivor Files (*.surv)", "surv");
            fileChooser.setFileFilter(filter);

            int userChoice = fileChooser.showSaveDialog(null);
            if (userChoice == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();

                // Ensure the selected file has the .surv extension
                if (!selectedFile.getName().toLowerCase().endsWith(".surv")) {
                    selectedFile = new File(selectedFile.getParentFile(), selectedFile.getName() + ".surv");
                }

                performSave(selectedFile); // Perform the "Save As" operation
            }
        }

        private void performOpen(){
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Survivor Files (*.surv)", "surv");

            fileChooser.setFileFilter(filter);
            int returnVal = fileChooser.showOpenDialog(null);
            if(returnVal == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                // Ensure the selected file has the .surv extension
                if (!selectedFile.getName().toLowerCase().endsWith(".surv")) {
                    selectedFile = new File(selectedFile.getParentFile(), selectedFile.getName() + ".surv");

                }
                savedFile = selectedFile;

                try {
                    // Read the contents of the selected file
                    BufferedReader reader = new BufferedReader(new FileReader(selectedFile));
                    StringBuilder text = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        text.append(line).append("\n");
                    }
                    reader.close();

                    // Set the contents of codeArea with the text from the file
                    codeArea.setText(text.toString());
                } catch (IOException e) {
                    e.printStackTrace(); // Handle the exception appropriately
                }
            }


        }

        private void performRun(){
            //wala sa
        }
    }


    public static void main(String[] args) {
        new Frame();
    }
}