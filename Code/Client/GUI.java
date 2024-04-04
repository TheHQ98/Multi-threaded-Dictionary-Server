/**
 * Client side GUI
 *
 * @author Josh Feng, 1266669, chenhaof@student.unimelb.edu.au
 * @date 4 April 2024
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI {
    private JFrame frame;
    private JTabbedPane tabbedPane;
    private ClientServer clientServer;

    // initial setup
    public GUI(ClientServer clientServer) {
        this.clientServer = clientServer;
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

    // a combined panel include query, add, remove and update panel
    private void combinedPanel() {
        tabbedPane = new JTabbedPane();
        tabbedPane.setBounds(0, 0, 500, 300);
        tabbedPane.addTab(Params.QUERY, queryPanel());
        tabbedPane.addTab(Params.ADD, addPanel());
        tabbedPane.addTab(Params.REMOVE, removePanel());
        tabbedPane.addTab(Params.UPDATE, updatePanel());
    }

    // query panel allowed user to search the word and its meanings
    private JPanel queryPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // setup top panel, user use for search
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());
        JTextField textField = new JTextField(20);
        JButton button = new JButton("Search");
        topPanel.add(new JLabel("Word to search"));
        topPanel.add(textField);
        topPanel.add(button);

        // display feedback for user
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // button logic when user click it
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // check input
                String word = textField.getText();
                if (word.isEmpty()) {
                    textArea.setText(Params.INVALID_INPUT);
                    return;
                }

                // try to connect to the server
                clientServer.connect();
                if (!clientServer.checkServer()) {
                    JOptionPane.showMessageDialog(mainPanel, Params.UNABLE_CONNECT);
                    return;
                }

                // send and receive message, popup feedback in text area
                clientServer.sendMessage(Params.QUERY);
                clientServer.sendMessage(word);
                String result = clientServer.receiveMessage();
                textArea.setText(result);
                clientServer.disconnect();
            }
        }) ;
        return mainPanel;
    }

    // add panel allowed user to add the word and its meanings
    private JPanel addPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // setup top panel, allow user add the word
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());
        JLabel wordLabel = new JLabel("Word to add");
        JTextField wordTextField = new JTextField(20);
        topPanel.add(wordLabel);
        topPanel.add(wordTextField);

        // setup west panel, instructing the user
        JPanel westPanel = new JPanel();
        westPanel.setLayout(new BoxLayout(westPanel, BoxLayout.Y_AXIS));
        westPanel.add(new JLabel("Meaning(s)"));
        westPanel.add(new JLabel("(Make new line"));
        westPanel.add(new JLabel("for each meaning)"));

        // setup meaning text area, allow user add meanings
        JTextArea meaningTextArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(meaningTextArea);

        // add a button allow user to submit
        JPanel bottomPanel = new JPanel();
        JButton button = new JButton(Params.ADD);
        bottomPanel.add(button);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(westPanel, BorderLayout.WEST);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // button logic when user click it
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String addWord = wordTextField.getText();
                String addMeaning = correctMeanings(meaningTextArea.getText());

                // check if it is valid
                if (addWord.isEmpty() || addMeaning.isEmpty()) {
                    JOptionPane.showMessageDialog(mainPanel, Params.INVALID_INPUT);
                    return;
                }

                // try to connect to the server
                clientServer.connect();
                if (!clientServer.checkServer()) {
                    JOptionPane.showMessageDialog(mainPanel, Params.UNABLE_CONNECT);
                    return;
                }

                // send and receive message, popup a window message for feedback
                clientServer.sendMessage(Params.ADD);
                clientServer.sendMessage(addWord);
                clientServer.sendMessage(addMeaning);
                String result = clientServer.receiveMessage();
                JOptionPane.showMessageDialog(mainPanel, result);
                clientServer.disconnect();
            }
        });
        return mainPanel;
    }

    // remove panel allowed user to remove the word and its meanings
    private JPanel removePanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // setup top panel, allow user type the word and click the button
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());
        JTextField textField = new JTextField(20);
        JButton button = new JButton(Params.REMOVE);
        topPanel.add(new JLabel("Word to remove"));
        topPanel.add(textField);
        topPanel.add(button);

        // text area use for showing feedback
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // button logic when user click
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // check input if it is valid
                String word = textField.getText();
                if (word.isEmpty()) {
                    textArea.setText(Params.INVALID_INPUT);
                    return;
                }

                // try to connect to the server
                clientServer.connect();
                if (!clientServer.checkServer()) {
                    JOptionPane.showMessageDialog(mainPanel, Params.UNABLE_CONNECT);
                    return;
                }

                // send and receive message and show feedback in text area
                clientServer.sendMessage(Params.REMOVE);
                clientServer.sendMessage(word);
                String result = clientServer.receiveMessage();
                textArea.setText(result);
                clientServer.disconnect();
            }
        }) ;
        return mainPanel;
    }

    // update panel allowed user to update the exist word and its meanings
    private JPanel updatePanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // setup top panel, allow user type the word
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());
        JLabel wordLabel = new JLabel("Word to update");
        JTextField wordTextField = new JTextField(20);
        topPanel.add(wordLabel);
        topPanel.add(wordTextField);

        // setup west panel, instructing the user
        JPanel westPanel = new JPanel();
        westPanel.setLayout(new BoxLayout(westPanel, BoxLayout.Y_AXIS));
        westPanel.add(new JLabel("Meaning(s)"));
        westPanel.add(new JLabel("(Make new line"));
        westPanel.add(new JLabel("for each meaning)"));

        // setup text area, allow user to type meanings
        JTextArea meaningTextArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(meaningTextArea);

        // setup button, allow user to submit
        JPanel bottomPanel = new JPanel();
        JButton button = new JButton(Params.UPDATE);
        bottomPanel.add(button);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(westPanel, BorderLayout.WEST);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // button logic when user click
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String updateWord = wordTextField.getText();
                String updateMeaning = correctMeanings(meaningTextArea.getText());

                // check input if it is valid
                if (updateWord.isEmpty() || updateMeaning.isEmpty()) {
                    JOptionPane.showMessageDialog(mainPanel, Params.INVALID_INPUT);
                    return;
                }

                // try to connect to the server
                clientServer.connect();
                if (!clientServer.checkServer()) {
                    JOptionPane.showMessageDialog(mainPanel, Params.UNABLE_CONNECT);
                    return;
                }

                // send and receive message, popup a message to show feedback
                clientServer.sendMessage(Params.UPDATE);
                clientServer.sendMessage(updateWord);
                clientServer.sendMessage(updateMeaning);
                String result = clientServer.receiveMessage();
                JOptionPane.showMessageDialog(mainPanel, result);
                clientServer.disconnect();
            }
        });
        return mainPanel;
    }

    // Remove the line only contain spaces
    private String correctMeanings(String raw) {
        StringBuilder cleanedText = new StringBuilder();
        String[] meanings = raw.split("\n");
        for (String meaning : meanings) {
            if (!meaning.trim().isEmpty()) {
                cleanedText.append(meaning).append("\n");
            }
        }
        return cleanedText.toString();
    }

}
