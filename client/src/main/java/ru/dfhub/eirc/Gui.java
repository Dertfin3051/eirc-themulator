package ru.dfhub.eirc;

import ru.dfhub.eirc.util.Theme;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;

public class Gui {

    public static enum MessageType {
        SELF_USER_MESSAGE, USER_MESSAGE, SYSTEM_GOOD, SYSTEM_INFO, SYSTEM_ERROR, USER_SESSION
    }

    private static final JFrame window = new JFrame("EnigmaIRC");
    private static final Box mainPanel = Box.createVerticalBox();

    private static final Box messageBox = Box.createVerticalBox();
    private static JScrollPane messageBoxScrollbar = new JScrollPane(messageBox);

    private static final Box inputFieldPanel = Box.createHorizontalBox();
    private static final JTextField inputField = new JTextField();

    private static Theme activeTheme;

    private static boolean isMinimized = false;


    public static void init() {
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setSize(800, 500);
        window.setResizable(false);
        window.addWindowStateListener(new WindowAdapter() {
            @Override
            public void windowStateChanged(WindowEvent e) {
                isMinimized = (e.getNewState() & JFrame.ICONIFIED) == JFrame.ICONIFIED;
            }
        }); // Window minimized event
        applyAppIcon(window);

        mainPanel.setPreferredSize(new Dimension(800, 500));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Window padding
        //setBackgroundColor(mainPanel, activeTheme.getColor("background"));

        messageBoxScrollbar.setPreferredSize(new Dimension(780, 450));
        messageBoxScrollbar.getHorizontalScrollBar().setPreferredSize(new Dimension(800, 8));
        messageBoxScrollbar.getVerticalScrollBar().setPreferredSize(new Dimension(8, 500));
        messageBox.setBorder(new EmptyBorder(7, 7, 7, 7)); // Message Box padding
        //setBackgroundColor(messageBox, activeTheme.getColor("message-read-box"));
        //messageBoxScrollbar.setBorder(new LineBorder(COMPONENT_BORDER_COLOR));

        inputFieldPanel.setBorder(new EmptyBorder(5, 0, 0, 0)); // Top padding
        inputFieldPanel.add(inputField);
        inputField.setPreferredSize(new Dimension(800, 30)); // 25px to input field & 5px free space
        //inputField.setForeground(activeTheme.getColor("input-text-color"));
        //inputField.setCaretColor(activeTheme.getColor("input-text-color")); // Input text color
        //setBackgroundColor(inputField, activeTheme.getColor("message-input-box"));
        //inputField.setBorder(new LineBorder(COMPONENT_BORDER_COLOR));


        inputField.addActionListener(Gui::inputAction);

        applyTheme(Theme.DEFAULT_THEME);
        mainPanel.add(messageBoxScrollbar); mainPanel.add(inputFieldPanel);
        window.add(mainPanel);
        show();
    }

    /**
     * Pack all components and show window ad center of screen
     */
    public static void show() {
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    /**
     * Show new message
     * @param formattedMessage Formatted message
     */
    public static void showNewMessage(String formattedMessage, MessageType type) {
        JLabel message = new JLabel(formattedMessage);
        switch (type) {
            case SYSTEM_GOOD -> message.setForeground(activeTheme.getColor("system-good-message"));
            case SYSTEM_INFO -> message.setForeground(activeTheme.getColor("system-info-message"));
            case SYSTEM_ERROR -> message.setForeground(activeTheme.getColor("system-error-message"));
            case SELF_USER_MESSAGE -> message.setForeground(activeTheme.getColor("self-user-message"));
            case USER_MESSAGE -> message.setForeground(activeTheme.getColor("other-user-message"));
            case USER_SESSION -> message.setForeground(activeTheme.getColor("user-session-message"));
        }
        message.setFont(new Font(Font.SANS_SERIF, Font.BOLD, activeTheme.getSize("message-font-size")));

        messageBox.add(message);
        scrollDown();
        updateWindow();
    }

    /**
     * Show welcome message.
     * Used instead of SYSTEM_GOOD due to increased font size
     */
    public static void showWelcomeMessage() {
        JLabel message = new JLabel("Welcome to EnigmaIRC!");
        message.setForeground(new Color(0, 245, 0));
        message.setFont(new Font(Font.SANS_SERIF, Font.BOLD, activeTheme.getSize("welcome-message-font-size")));
        message.setBorder(new EmptyBorder(0, 0, 10, 0));

        messageBox.add(message);
        updateWindow();
    }

    /**
     * Block input and exit the program after 2 minutes
     * Used for critical errors, implying the inability to further work with the program
     */
    public static void breakInput() {
        inputField.setVisible(false);
        updateWindow();
        try {
            Thread.sleep(1000 * 120);
        } catch (InterruptedException e) {}
        System.exit(0);
    }

    /**
     * Scroll down messageBox. Only for vertical scroll
     */
    public static void scrollDown() {
        JScrollBar newScroll = messageBoxScrollbar.getVerticalScrollBar();
        newScroll.setValue(messageBoxScrollbar.getVerticalScrollBar().getMaximum());
        messageBoxScrollbar.setVerticalScrollBar(newScroll);
    }

    public static boolean isMinimized() {
        return isMinimized;
    }

    /**
     * Revalidate all elements and repaint window
     */
    private static void updateWindow() { window.revalidate(); window.repaint(); }

    /**
     * Handle inputField "Enter" button
     * @param e Event
     */
    private static void inputAction(ActionEvent e) {
        String input = inputField.getText();
        switch (input) {
            case "!!clear" -> messageBox.removeAll();
            case "!!exit" -> {
                // Send exit message
                System.exit(0);
            }
            default -> {
                DataParser.handleOutputMessage(inputField.getText());
            }
        }

        inputField.setText("");
        scrollDown();
        updateWindow(); // Update
    }

    public static void applyAppIcon(JFrame frame) {
        try (InputStream iconStream = Main.class.getClassLoader().getResourceAsStream("icon.png")) {
            frame.setIconImage(
                    ImageIO.read(iconStream)
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Change JComponent background color
     * @param component JComponent
     * @param color Color
     */
    private static void setBackgroundColor(JComponent component, Color color) {
        component.setOpaque(true); component.setBackground(color);
    }

    public static void applyTheme(Theme theme) {
        activeTheme = theme;

        Color componentBorderColor = activeTheme.getColor("component-border-color");

        setBackgroundColor(mainPanel, activeTheme.getColor("background"));
        setBackgroundColor(messageBox, activeTheme.getColor("message-read-box"));
        messageBoxScrollbar.setBorder(new LineBorder(componentBorderColor));
        inputField.setForeground(activeTheme.getColor("input-text-color"));
        inputField.setCaretColor(activeTheme.getColor("input-text-color"));
        setBackgroundColor(inputField, activeTheme.getColor("message-input-box"));
        inputField.setBorder(new LineBorder(componentBorderColor));
        updateWindow();
    }

}