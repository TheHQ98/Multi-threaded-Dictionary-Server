/**
 * Server side GUI include basic information, dialog and dictionary data
 *
 * @author Josh Feng, 1266669, chenhaof@student.unimelb.edu.au
 * @date 4 April 2024
 */

import javax.swing.*;
import java.awt.*;

public class GUI {
    private JFrame frame;
    private JTabbedPane tabbedPane;
    private String file;
    private int port;
    JTextArea fileTextArea;
    JTextArea DialogTextArea;
    private static String dialogContext;

    // initial setup
    public GUI(int port, String file) {
        this.port = port;
        this.file = file;
        dialogContext = "";
        fileTextArea = new JTextArea();
        DialogTextArea = new JTextArea();
        frame = new JFrame();
        frame.setTitle(Params.GUI_TITLE);
        frame.setSize(Params.GUI_WIDTH, Params.GUI_HEIGHT);

        setScreenSize();

        combinedPanel();
        frame.add(tabbedPane);

        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // when start the gui, it will pop up in the middle of the screen
    private void setScreenSize() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((screenSize.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((screenSize.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
    }

    // a combined panel include dialog panel and dictionary data panel
    private void combinedPanel() {
        tabbedPane = new JTabbedPane();
        tabbedPane.setBounds(0, 0, 500, 300);
        tabbedPane.addTab(Params.GUI_DIALOG_NAME, dialog());
        tabbedPane.addTab(Params.GUI_DIC_NAME, dictionary());
    }

    // dialog panel
    private JPanel dialog() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // top panel shows port number and file address
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.add(new JLabel("Port: " + port));
        JLabel fileLabel = new JLabel("File address: " + file);
        fileLabel.setToolTipText(file);
        topPanel.add(fileLabel);

        // dialog text area shows all the action by users
        DialogTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(DialogTextArea);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        return mainPanel;
    }

    // dictionary panel, shows all dictionary data
    private JPanel dictionary() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        fileTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(fileTextArea);

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        return mainPanel;
    }

    // add new action dialog from the user to the dialog panel
    public synchronized void addDialog(String newContext) {
        dialogContext += newContext;
        DialogTextArea.setText(dialogContext);
        DialogTextArea.setCaretPosition(DialogTextArea.getDocument().getLength());
    }

    // after each action by user, update the data
    public synchronized void refreshContext(String context) {
        fileTextArea.setText(context);
    }
}
