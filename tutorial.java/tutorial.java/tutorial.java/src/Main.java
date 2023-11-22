import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.*;
import javax.swing.filechooser.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.undo.*;
import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;


public class Main {
    public static class Frame {
        private final JTextArea codeArea;
        private File savedFile;
        private File selectedFile;
        private boolean textChanged;
        private UndoManager undoManager;

        public Frame() {
            JFrame mainFrame = new JFrame();

            codeArea = new JTextArea(10, 40);
            Font font = new Font("Times New Roman", Font.BOLD, 14);
            codeArea.setFont(font);
            codeArea.setBackground(Color.BLACK);
            codeArea.setForeground(Color.WHITE);
            codeArea.setSelectedTextColor(Color.BLACK);
            codeArea.setCaretColor(Color.WHITE);

            // Initialize the undo manager
            undoManager = new UndoManager();
            codeArea.getDocument().addUndoableEditListener(undoManager);

            Icon saveIcon = new ImageIcon(getClass().getResource("/images/save.png"));
            Icon saveAsIcon = new ImageIcon(getClass().getResource("/images/save_as.png"));
            Icon openIcon = new ImageIcon(getClass().getResource("/images/open.png"));
            Icon runIcon = new ImageIcon(getClass().getResource("/images/run.png"));
            Icon compileIcon = new ImageIcon(getClass().getResource("/images/compile.png"));
            Icon openNewIcon= new ImageIcon(getClass().getResource("/images/newfile.png"));
            Icon cutIcon = new ImageIcon(getClass().getResource("/images/cut.png"));
            Icon copyIcon = new ImageIcon(getClass().getResource("/images/copy.png"));
            Icon pasteIcon = new ImageIcon(getClass().getResource("/images/paste.png"));
            Icon undoIcon = new ImageIcon(getClass().getResource("/images/undo.png"));
            Icon redoIcon = new ImageIcon(getClass().getResource("/images/redo.png"));
            Icon voiceIcon = new ImageIcon(getClass().getResource("/images/voice.png"));
            ImageIcon subsideIcon = new ImageIcon(getClass().getResource("/images/subsideIcon.png"));

            Image subsideLogo= subsideIcon.getImage();


            JButton run = new JButton(runIcon);
            JButton compile = new JButton(compileIcon);
            JButton open = new JButton(openIcon);
            JButton save = new JButton(saveIcon);
            JButton saveAs = new JButton(saveAsIcon);
            JButton openNew = new JButton(openNewIcon);
            JButton cut= new JButton(cutIcon);
            JButton copy= new JButton(copyIcon);
            JButton paste= new JButton(pasteIcon);
            JButton undo = new JButton(undoIcon);
            JButton redo = new JButton(redoIcon);
            JButton voice = new JButton(voiceIcon);


            mainFrame.setSize(750, 750);
            //mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.setTitle("Subside");
            mainFrame.setIconImage(subsideLogo);
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
            redo.setPreferredSize(buttonSize);
            undo.setPreferredSize(buttonSize);
            voice.setPreferredSize(buttonSize);


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
            buttonPanel.add(redo);
            buttonPanel.add(undo);
            buttonPanel.add(voice);

            save.setEnabled(false);
            saveAs.setEnabled(false);
            compile.setEnabled(false);
            run.setEnabled(false);
            copy.setEnabled(false);
            cut.setEnabled(false);
            undo.setEnabled(false);
            redo.setEnabled(false);
            voice.setEnabled(false);

            //hover
            open.setToolTipText("Open File (Ctrl + o)");
            save.setToolTipText("Save (Ctrl + S)");
            compile.setToolTipText("Compile (Ctrl + b)");
            run.setToolTipText("Run code (Ctrl + F9)");
            saveAs.setToolTipText("Save As (F2)");
            openNew.setToolTipText("New File (Ctrl + n)");
            cut.setToolTipText("Cut (Ctrl + x)");
            copy.setToolTipText("Copy (Ctrl + c)");
            paste.setToolTipText("Paste (Ctrl + v)");
            redo.setToolTipText("Redo (Ctrl + z)");
            undo.setToolTipText("Undo (Ctrl + y)");
            voice.setToolTipText("Voice (Ctrl + y)");

            // Wrap the codeArea in a JScrollPane
            JScrollPane scrollPane = new JScrollPane(codeArea);

            // Create a panel for the codeArea using BorderLayout
            JPanel codePanel = new JPanel(new BorderLayout());
            codePanel.add(scrollPane, BorderLayout.CENTER);

            // Add the buttonPanel and codePanel to the mainFrame
            mainFrame.add(buttonPanel, BorderLayout.NORTH);
            mainFrame.add(codePanel, BorderLayout.CENTER);


            //EXit

            mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            mainFrame.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    if (save.isEnabled()) {
                        mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                        int choice = JOptionPane.showConfirmDialog(mainFrame, "File not saved. Do you want to save? ", "Exit Confirmation", JOptionPane.YES_NO_CANCEL_OPTION);
                        if (choice == JOptionPane.NO_OPTION) {
                            System.exit( 0);
                        } else if(choice == JOptionPane.CANCEL_OPTION){

                        } else {
                            if (savedFile == null) {
                                performSaveAs();
                            } else
                                performSave(savedFile);
                            System.exit(0);
                        }
                    }
                }
            });




            mainFrame.setVisible(true);

            InputMap inputMap = codeArea.getInputMap(JComponent.WHEN_FOCUSED);
            ActionMap actionMap = codeArea.getActionMap();

            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "Undo");
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "Redo");
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "Save");
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "Open");
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_B, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "Compile");
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F9, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "Run");
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0), "SaveAs");
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "NewFile");
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_H, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "Voice");
            
            actionMap.put("Undo", new AbstractAction("Undo") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (undoManager.canUndo()) {
                        undoManager.undo();
                    }
                }
            });

            actionMap.put("Redo", new AbstractAction("Redo") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (undoManager.canRedo()) {
                        undoManager.redo();
                    }
                }
            });

            actionMap.put("Save", new AbstractAction("Save") {
                @Override
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

            actionMap.put("Open", new AbstractAction("Open") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    performOpen();
                }
            });

            actionMap.put("Compile", new AbstractAction("Compile") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Implement the compile action
                }
            });

            actionMap.put("Run", new AbstractAction("Run") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Implement the run action
                }
            });

            actionMap.put("SaveAs", new AbstractAction("SaveAs") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    performSaveAs();
                }
            });

            actionMap.put("NewFile", new AbstractAction("NewFile") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (save.isEnabled()) {
                        int choice = JOptionPane.showConfirmDialog(mainFrame, "File not saved. Do you want to save? ", "Exit Confirmation",JOptionPane.YES_NO_OPTION);
                        if (choice == JOptionPane.NO_OPTION) {
                            new Frame();
                        }else {
                            // performSaveAs();
                            new Frame();
                        }
                    } else {
                        new Frame();
                    }
                }
            });

            codeArea.getDocument().addDocumentListener(new DocumentListener() {

                @Override
                public void insertUpdate(DocumentEvent e) {
                    textChanged = true;
                    save.setEnabled(true);
                    saveAs.setEnabled(true);
                    compile.setEnabled(true);
                    run.setEnabled(true);
                    copy.setEnabled(true);
                    cut.setEnabled(true);
                    undo.setEnabled(true);
                    redo.setEnabled(true);
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    textChanged = true;
                    save.setEnabled(true);
                    saveAs.setEnabled(true);
                    compile.setEnabled(true);
                    run.setEnabled(true);
                    copy.setEnabled(true);
                    cut.setEnabled(true);
                    undo.setEnabled(true);
                    redo.setEnabled(true);
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    textChanged = true;
                    save.setEnabled(true);
                    saveAs.setEnabled(true);
                    compile.setEnabled(true);
                    run.setEnabled(true);
                    copy.setEnabled(true);
                    cut.setEnabled(true);
                    undo.setEnabled(true);
                    redo.setEnabled(true);
                }
            });

            save.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                    if (savedFile != null) {
                        // File has been saved before, perform "Save" operation
                        performSave(savedFile);
                        save.setEnabled(false);
                        textChanged = false;
                        String fileName = getSavedFile().getName();
                        mainFrame.setTitle("Subside | "+fileName);
                    } else {
                        // File has not been saved before, perform "Save As" operation
                        performSaveAs();
                        save.setEnabled(false);
                        String fileName = getSavedFile().getName();
                        mainFrame.setTitle("Subside | "+fileName);
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
                    String fileName = getOpenedFile().getName();
                    mainFrame.setTitle("Subside | "+fileName);
                }
            });

            compile.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    performCompile();
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

            undo.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String code = codeArea.getText();
                    if(code.isEmpty()){
                        undo.setEnabled(false);
                    }
                    if (undoManager.canUndo()) {
                        undoManager.undo();
                    }
                }
            });

            redo.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String code = codeArea.getText();
                    if(code.isEmpty()){
                        redo.setEnabled(false);
                    }
                    if (undoManager.canRedo()) {
                        undoManager.redo();
                    }
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
                selectedFile = fileChooser.getSelectedFile();

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
                selectedFile = fileChooser.getSelectedFile();
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

        private void performCompile(){
            if (textChanged) {
                if (savedFile == null) {
                    performSaveAs();
                } else
                    performSave(savedFile);
            } //SAVE DAAN
            //compiler function
        }

        private void performRun(){
            performCompile();
            //run function
        }

        public File getSavedFile() {
            return savedFile;
        }

        public File getOpenedFile() {
            return selectedFile;
        }
    }


    public static void main(String[] args) {
        new Frame();
    }
}