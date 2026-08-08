
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class StudentDashboard extends JFrame {

    private static final Color BACKGROUND = new Color(246, 247, 251);
    private static final Color SIDEBAR = new Color(28, 24, 43);
    private static final Color SIDEBAR_HOVER = new Color(52, 45, 78);
    private static final Color PURPLE = new Color(116, 73, 224);
    private static final Color ORANGE = new Color(245, 146, 24);
    private static final Color TEXT_DARK = new Color(35, 38, 47);
    private static final Color TEXT_MUTED = new Color(119, 126, 144);
    private static final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 15);
    private static final Font FONT_SEMIBOLD = new Font("Segoe UI", Font.BOLD, 16);

    private final CardLayout contentLayout;
    private final JPanel contentCards;

    private String studentName = "Aarav Sharma";
    private String rollNumber = "CS26-1042";
    private String roomNumber = "B-214";
    private String hostel = "Maple Residency";
    private String branch = "Computer Science";
    private String email = "aarav.sharma@college.edu";
    private String phone = "+91 98765 43210";
    private String guardianName = "Rajesh Sharma";
    private String guardianPhone = "+91 91234 56789";
    private int pendingComplaintCount = 3;
    private int resolvedComplaintCount = 18;
    private int unreadNoticeCount = 5;
    private String todayMessMenu = "Paneer Bowl";
    private final List<String[]> latestNotices = new ArrayList<>();
    private final List<String[]> feedbackList = new ArrayList<>();
    private final Map<String, String[]> weeklyMessMenu = new LinkedHashMap<>();

    public StudentDashboard() {
        setTitle("Hostel Portal - Student Dashboard");
        setSize(1280, 760);
        setMinimumSize(new Dimension(1100, 680));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND);

        contentLayout = new CardLayout();
        contentCards = new JPanel(contentLayout);
        contentCards.setOpaque(false);
        seedSampleData();
        contentCards.add(createDashboardView(), "Dashboard");
        contentCards.add(createComplaintView(), "Submit Complaint");
        contentCards.add(createMyComplaintsView(), "My Complaints");
        contentCards.add(createNoticeBoardView(), "Notice Board");
        contentCards.add(createMessMenuView(), "Mess Menu");
        contentCards.add(createFeedbackView(), "Feedback");
        contentCards.add(createProfileView(), "Profile");
        contentCards.add(createSettingsView(), "Settings");

        JButton aiButton = createFloatingAI();
        aiButton.addActionListener(e -> showChatbotPopup());
        JLayeredDashboard layeredMain = new JLayeredDashboard(contentCards, aiButton);
        add(createSidebar(), BorderLayout.WEST);
        add(layeredMain, BorderLayout.CENTER);
        setVisible(true);
    }

    public JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(250, 0));
        sidebar.setBackground(SIDEBAR);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(28, 18, 24, 18));

        JLabel logo = new JLabel("🏨  Hostel Portal");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 23));
        logo.setForeground(Color.WHITE);
        logo.setAlignmentX(LEFT_ALIGNMENT);
        sidebar.add(logo);

        JLabel role = new JLabel("Student Workspace");
        role.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        role.setForeground(new Color(180, 177, 198));
        role.setBorder(new EmptyBorder(6, 4, 26, 0));
        role.setAlignmentX(LEFT_ALIGNMENT);
        sidebar.add(role);

        String[] items = {"Dashboard", "Submit Complaint", "My Complaints", "Notice Board", "Mess Menu", "Feedback", "Profile", "Settings", "Logout"};
        String[] icons = {"▣", "+", "☰", "●", "🍽", "✎", "👤", "⚙", "↪"};

        for (int i = 0; i < items.length; i++) {
            JButton item = createSidebarButton(icons[i] + "   " + items[i], items[i].equals("Dashboard"));
            final String cardName = items[i];
            item.addActionListener(e -> {
                if (!"Logout".equals(cardName)) {
                    contentLayout.show(contentCards, cardName);
                }
            });
            sidebar.add(item);
            sidebar.add(Box.createVerticalStrut(8));
        }

        sidebar.add(Box.createVerticalGlue());
        JLabel footer = new JLabel("Need help? Tap the AI button");
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footer.setForeground(new Color(170, 166, 190));
        footer.setAlignmentX(LEFT_ALIGNMENT);
        sidebar.add(footer);
        return sidebar;
    }

    public JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(4, 4, 22, 4));

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        JLabel welcome = new JLabel("Welcome back, " + studentName);
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 30));
        welcome.setForeground(TEXT_DARK);
        JLabel greeting = new JLabel(getGreeting() + " Here's your hostel overview for today.");
        greeting.setFont(FONT_REGULAR);
        greeting.setForeground(TEXT_MUTED);
        text.add(welcome);
        text.add(Box.createVerticalStrut(6));
        text.add(greeting);

        JLabel badge = new JLabel("Student Dashboard", SwingConstants.CENTER);
        badge.setOpaque(true);
        badge.setBackground(new Color(239, 233, 255));
        badge.setForeground(PURPLE);
        badge.setFont(FONT_SEMIBOLD);
        badge.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        header.add(text, BorderLayout.WEST);
        header.add(badge, BorderLayout.EAST);
        return header;
    }

    public JPanel createDashboardCards() {
        JPanel cards = new JPanel(new GridBagLayout());
        cards.setOpaque(false);
        cards.setMaximumSize(new Dimension(1096, 150));

        String[][] cardData = {
            {"Pending Complaints", String.format("%02d", pendingComplaintCount), "Awaiting review"},
            {"Resolved Complaints", String.valueOf(resolvedComplaintCount), "This semester"},
            {"New Notices", String.format("%02d", unreadNoticeCount), "Unread updates"},
            {"Today's Mess", "Dinner", todayMessMenu}
        };
        Color[] accents = {PURPLE, new Color(34, 172, 113), ORANGE, new Color(42, 132, 246)};

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(0, 0, 0, 18);
        for (int i = 0; i < cardData.length; i++) {
            gbc.gridx = i;
            gbc.insets = new Insets(0, 0, 0, i == cardData.length - 1 ? 0 : 18);
            cards.add(createStatCard(cardData[i][0], cardData[i][1], cardData[i][2], accents[i]), gbc);
        }
        return cards;
    }

    public JPanel createQuickActions() {
        JPanel wrapper = sectionPanel("Quick Actions");
        wrapper.setPreferredSize(new Dimension(544, 250));
        wrapper.setMaximumSize(new Dimension(544, 250));

        JPanel actions = new JPanel(new GridLayout(2, 2, 18, 18));
        actions.setOpaque(false);
        actions.add(createActionCard("Submit Complaint", "Raise a new issue", "+"));
        actions.add(createActionCard("My Complaints", "Track status", "☰"));
        actions.add(createActionCard("Notice Board", "Read updates", "●"));
        actions.add(createActionCard("Today's Menu", "View meals", "🍽"));
        wrapper.add(actions, BorderLayout.CENTER);
        return wrapper;
    }

    public JPanel createRecentActivity() {
        JPanel wrapper = sectionPanel("Recent Activity");
        wrapper.setPreferredSize(new Dimension(544, 210));
        wrapper.setMaximumSize(new Dimension(544, 210));

        JPanel list = new RoundedPanel(22, Color.WHITE, true);
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setBorder(new EmptyBorder(18, 22, 18, 22));
        list.add(activityRow("Complaint #HP-1024 moved to pending review", "Today, 10:25 AM", ORANGE));
        list.add(activityRow("New notice posted: Water maintenance schedule", "Yesterday, 6:10 PM", PURPLE));
        list.add(activityRow("Mess menu updated for this week", "Yesterday, 12:05 PM", new Color(42, 132, 246)));
        wrapper.add(list, BorderLayout.CENTER);
        return wrapper;
    }

    public JButton createFloatingAI() {
        JButton ai = new CircularAIButton();
        ai.setFont(new Font("Segoe UI", Font.BOLD, 17));
        ai.setForeground(Color.WHITE);
        ai.setContentAreaFilled(false);
        ai.setBorderPainted(false);
        ai.setFocusPainted(false);
        ai.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        ai.setToolTipText("AI Assistant");
        return ai;
    }

    private JPanel createDashboardView() {
        JPanel page = new JPanel(new GridBagLayout());
        page.setOpaque(false);
        page.setBorder(new EmptyBorder(30, 34, 30, 34));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(0, 0, 24, 0);
        page.add(createHeader(), gbc);

        gbc.gridy++;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        page.add(createDashboardCards(), gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 0, 0, 24);
        page.add(createQuickActions(), gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        page.add(createRecentActivity(), gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.weighty = 1;
        page.add(Box.createGlue(), gbc);
        return page;
    }

    private JPanel createMyComplaintsView() {
        JPanel page = new JPanel(new BorderLayout(0, 24));
        page.setOpaque(false);
        page.setBorder(new EmptyBorder(30, 34, 30, 34));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("My Complaints");
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setForeground(TEXT_DARK);
        JLabel subtitle = new JLabel("Track your submitted hostel issues and review their latest status.");
        subtitle.setFont(FONT_REGULAR);
        subtitle.setForeground(TEXT_MUTED);
        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(6));
        titlePanel.add(subtitle);

        JLabel countBadge = new JLabel("6 Active Records", SwingConstants.CENTER);
        countBadge.setOpaque(true);
        countBadge.setBackground(new Color(239, 233, 255));
        countBadge.setForeground(PURPLE);
        countBadge.setFont(FONT_SEMIBOLD);
        countBadge.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        header.add(titlePanel, BorderLayout.WEST);
        header.add(countBadge, BorderLayout.EAST);
        page.add(header, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(0, 2, 22, 22));
        grid.setOpaque(false);
        grid.setBorder(new EmptyBorder(2, 2, 18, 2));

        String[][] complaints = {
            {"HP-1024", "B-214", "Electrical", "High", "Pending", "07 Aug 2026"},
            {"HP-1019", "B-214", "Plumbing", "Medium", "In Progress", "05 Aug 2026"},
            {"HP-1008", "B-214", "Housekeeping", "Low", "Resolved", "30 Jul 2026"},
            {"HP-0996", "B-214", "Wi-Fi", "High", "Rejected", "26 Jul 2026"},
            {"HP-0988", "B-214", "Furniture", "Medium", "Resolved", "20 Jul 2026"},
            {"HP-0977", "B-214", "Mess", "Low", "Pending", "17 Jul 2026"}
        };

        for (String[] complaint : complaints) {
            grid.add(createComplaintCard(complaint));
        }

        JScrollPane scrollPane = new JScrollPane(grid);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        page.add(scrollPane, BorderLayout.CENTER);
        return page;
    }

    private JPanel createComplaintCard(String[] complaint) {
        AnimatedComplaintCard card = new AnimatedComplaintCard();
        card.setPreferredSize(new Dimension(500, 230));
        card.setLayout(new BorderLayout(0, 18));
        card.setBorder(new EmptyBorder(22, 24, 24, 24));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        JLabel id = new JLabel("Complaint ID  " + complaint[0]);
        id.setFont(new Font("Segoe UI", Font.BOLD, 20));
        id.setForeground(TEXT_DARK);
        JLabel status = statusBadge(complaint[4]);
        top.add(id, BorderLayout.WEST);
        top.add(status, BorderLayout.EAST);

        JPanel details = new JPanel(new GridLayout(2, 2, 18, 12));
        details.setOpaque(false);
        details.add(complaintField("Room Number", complaint[1]));
        details.add(complaintField("Category", complaint[2]));
        details.add(complaintField("Priority", complaint[3]));
        details.add(complaintField("Date", complaint[5]));

        JButton viewDetails = new JButton("View Details");
        viewDetails.setFont(FONT_SEMIBOLD);
        viewDetails.setForeground(Color.WHITE);
        viewDetails.setBackground(PURPLE);
        viewDetails.setFocusPainted(false);
        viewDetails.setBorder(BorderFactory.createEmptyBorder(11, 18, 11, 18));
        viewDetails.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        actions.setOpaque(false);
        actions.add(viewDetails);

        card.add(top, BorderLayout.NORTH);
        card.add(details, BorderLayout.CENTER);
        card.add(actions, BorderLayout.SOUTH);
        return card;
    }

    private JPanel complaintField(String label, String value) {
        JPanel field = new JPanel();
        field.setOpaque(false);
        field.setLayout(new BoxLayout(field, BoxLayout.Y_AXIS));
        JLabel labelText = new JLabel(label);
        labelText.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        labelText.setForeground(TEXT_MUTED);
        JLabel valueText = new JLabel(value);
        valueText.setFont(FONT_SEMIBOLD);
        valueText.setForeground(TEXT_DARK);
        field.add(labelText);
        field.add(Box.createVerticalStrut(4));
        field.add(valueText);
        return field;
    }

    private JLabel statusBadge(String statusText) {
        JLabel badge = new JLabel(statusText, SwingConstants.CENTER);
        Color statusColor = statusColor(statusText);
        badge.setOpaque(true);
        badge.setBackground(new Color(statusColor.getRed(), statusColor.getGreen(), statusColor.getBlue(), 35));
        badge.setForeground(statusColor.darker());
        badge.setFont(new Font("Segoe UI", Font.BOLD, 13));
        badge.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
        return badge;
    }

    private Color statusColor(String statusText) {
        if ("Pending".equals(statusText)) {
            return ORANGE;
        }
        if ("In Progress".equals(statusText)) {
            return new Color(42, 132, 246);
        }
        if ("Resolved".equals(statusText)) {
            return new Color(34, 172, 113);
        }
        if ("Rejected".equals(statusText)) {
            return new Color(224, 72, 72);
        }
        return TEXT_MUTED;
    }

    private JButton createSidebarButton(String text, boolean active) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFont(FONT_SEMIBOLD);
        button.setForeground(Color.WHITE);
        button.setBackground(active ? PURPLE : SIDEBAR);
        button.setBorder(new EmptyBorder(0, 16, 0, 14));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(active ? PURPLE : SIDEBAR_HOVER);
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(active ? PURPLE : SIDEBAR);
            }
        });
        return button;
    }

    private JPanel createStatCard(String title, String value, String subtitle, Color accent) {
        JPanel card = new RoundedPanel(24, Color.WHITE, true);
        card.setPreferredSize(new Dimension(260, 150));
        card.setMinimumSize(new Dimension(260, 150));
        card.setMaximumSize(new Dimension(260, 150));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(20, 20, 18, 20));
        JLabel label = new JLabel(title);
        label.setFont(FONT_REGULAR);
        label.setForeground(TEXT_MUTED);
        JLabel number = new JLabel(value);
        number.setFont(new Font("Segoe UI", Font.BOLD, 29));
        number.setForeground(TEXT_DARK);
        JLabel sub = new JLabel(subtitle);
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sub.setForeground(accent);
        card.add(label);
        card.add(Box.createVerticalStrut(10));
        card.add(number);
        card.add(Box.createVerticalStrut(8));
        card.add(sub);
        return card;
    }

    private JPanel createActionCard(String title, String subtitle, String icon) {
        JPanel card = new RoundedPanel(22, Color.WHITE, true);
        card.setPreferredSize(new Dimension(263, 88));
        card.setLayout(new BorderLayout(12, 0));
        card.setBorder(new EmptyBorder(18, 18, 18, 18));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        JLabel iconLabel = new JLabel(icon, SwingConstants.CENTER);
        iconLabel.setOpaque(true);
        iconLabel.setBackground(new Color(255, 241, 222));
        iconLabel.setForeground(ORANGE);
        iconLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        iconLabel.setPreferredSize(new Dimension(48, 48));
        JPanel text = new JPanel(new GridLayout(2, 1));
        text.setOpaque(false);
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(FONT_SEMIBOLD);
        titleLabel.setForeground(TEXT_DARK);
        JLabel subLabel = new JLabel(subtitle);
        subLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subLabel.setForeground(TEXT_MUTED);
        text.add(titleLabel);
        text.add(subLabel);
        card.add(iconLabel, BorderLayout.WEST);
        card.add(text, BorderLayout.CENTER);
        return card;
    }

    private JPanel sectionPanel(String title) {
        JPanel panel = new JPanel(new BorderLayout(0, 14));
        panel.setOpaque(false);
        JLabel label = new JLabel(title);
        label.setFont(new Font("Segoe UI", Font.BOLD, 21));
        label.setForeground(TEXT_DARK);
        panel.add(label, BorderLayout.NORTH);
        return panel;
    }

    private JPanel activityRow(String message, String time, Color accent) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(10, 0, 10, 0));
        JLabel left = new JLabel("●  " + message);
        left.setFont(FONT_REGULAR);
        left.setForeground(TEXT_DARK);
        JLabel right = new JLabel(time);
        right.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        right.setForeground(TEXT_MUTED);
        left.setForeground(accent.darker());
        row.add(left, BorderLayout.WEST);
        row.add(right, BorderLayout.EAST);
        return row;
    }

    private JPanel createComplaintView() {
        JPanel page = new JPanel(new BorderLayout(0, 22));
        page.setOpaque(false);
        page.setBorder(new EmptyBorder(30, 34, 30, 34));

        page.add(createComplaintHeader(), BorderLayout.NORTH);

        JPanel formCard = new LiftPanel(28, Color.WHITE, true);
        formCard.setLayout(new GridBagLayout());
        formCard.setBorder(new EmptyBorder(26, 28, 28, 28));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(0, 0, 18, 18);
        formCard.add(labeledField("Room Number", "e.g. B-204"), gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, 18, 0);
        formCard.add(labeledField("Complaint Title", "Short title for your issue"), gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 18, 0);
        formCard.add(categorySection(), gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 18, 0);
        formCard.add(descriptionSection(), gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 0, 18, 18);
        formCard.add(uploadCard(), gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, 18, 0);
        formCard.add(prioritySection(), gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 22, 0);
        formCard.add(aiSuggestionCard(), gbc);

        JButton submit = createLiftButton("Submit Complaint", PURPLE, Color.WHITE, 18, 52);
        gbc.gridy++;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        formCard.add(submit, gbc);

        page.add(formCard, BorderLayout.CENTER);
        return page;
    }

    private JPanel createComplaintHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("Submit Complaint");
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setForeground(TEXT_DARK);
        JLabel subtitle = new JLabel("Tell us what went wrong. Add details so the hostel team can resolve it faster.");
        subtitle.setFont(FONT_REGULAR);
        subtitle.setForeground(TEXT_MUTED);
        text.add(title);
        text.add(Box.createVerticalStrut(6));
        text.add(subtitle);
        JLabel badge = new JLabel("New Ticket", SwingConstants.CENTER);
        badge.setOpaque(true);
        badge.setBackground(new Color(239, 233, 255));
        badge.setForeground(PURPLE);
        badge.setFont(FONT_SEMIBOLD);
        badge.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        header.add(text, BorderLayout.WEST);
        header.add(badge, BorderLayout.EAST);
        return header;
    }

    private JPanel labeledField(String label, String placeholder) {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setOpaque(false);
        JLabel fieldLabel = fieldLabel(label);
        JTextField field = new JTextField(placeholder);
        field.setFont(FONT_REGULAR);
        field.setForeground(TEXT_MUTED);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 229, 238), 1, true),
                new EmptyBorder(13, 14, 13, 14)));
        panel.add(fieldLabel, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }

    private JPanel categorySection() {
        JPanel wrapper = new JPanel(new BorderLayout(0, 10));
        wrapper.setOpaque(false);
        wrapper.add(fieldLabel("Category"), BorderLayout.NORTH);
        JPanel grid = new JPanel(new GridLayout(2, 4, 14, 14));
        grid.setOpaque(false);
        String[][] categories = {{"⚡", "Electrical"}, {"🚿", "Plumbing"}, {"🪑", "Furniture"}, {"🍽", "Mess"}, {"🧹", "Cleaning"}, {"📶", "WiFi"}, {"🛡", "Security"}, {"⋯", "Others"}};
        for (String[] category : categories) {
            grid.add(createCategoryCard(category[0], category[1]));
        }
        wrapper.add(grid, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel createCategoryCard(String icon, String title) {
        JPanel card = new LiftPanel(20, new Color(250, 250, 255), true);
        card.setLayout(new BorderLayout(8, 0));
        card.setBorder(new EmptyBorder(14, 16, 14, 16));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        JLabel iconLabel = new JLabel(icon, SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 23));
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(FONT_SEMIBOLD);
        titleLabel.setForeground(TEXT_DARK);
        card.add(iconLabel, BorderLayout.WEST);
        card.add(titleLabel, BorderLayout.CENTER);
        return card;
    }

    private JPanel descriptionSection() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setOpaque(false);
        panel.add(fieldLabel("Description"), BorderLayout.NORTH);
        JTextArea area = new JTextArea("Describe the issue, location, and any useful details...");
        area.setRows(5);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(FONT_REGULAR);
        area.setForeground(TEXT_MUTED);
        area.setBorder(new EmptyBorder(12, 14, 12, 14));
        JScrollPane scroll = new JScrollPane(area);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(226, 229, 238), 1, true));
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel uploadCard() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setOpaque(false);
        panel.add(fieldLabel("Upload Image"), BorderLayout.NORTH);
        JButton upload = createLiftButton("＋  Choose Image", new Color(255, 241, 222), ORANGE, 15, 50);
        upload.addActionListener(e -> new JFileChooser().showOpenDialog(this));
        panel.add(upload, BorderLayout.CENTER);
        return panel;
    }

    private JPanel prioritySection() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setOpaque(false);
        panel.add(fieldLabel("Priority"), BorderLayout.NORTH);
        JPanel chips = new JPanel(new GridLayout(1, 3, 12, 0));
        chips.setOpaque(false);
        ButtonGroup group = new ButtonGroup();
        String[] priorities = {"Low", "Medium", "High"};
        Color[] colors = {new Color(34, 172, 113), ORANGE, new Color(232, 86, 86)};
        for (int i = 0; i < priorities.length; i++) {
            JRadioButton chip = createPriorityChip(priorities[i], colors[i]);
            group.add(chip);
            chips.add(chip);
            if (i == 1) {
                chip.setSelected(true);
            }
        }
        panel.add(chips, BorderLayout.CENTER);
        return panel;
    }

    private JRadioButton createPriorityChip(String text, Color accent) {
        JRadioButton chip = new JRadioButton(text);
        chip.setHorizontalAlignment(SwingConstants.CENTER);
        chip.setFont(FONT_SEMIBOLD);
        chip.setForeground(accent.darker());
        chip.setBackground(new Color(250, 250, 255));
        chip.setBorder(new EmptyBorder(12, 12, 12, 12));
        chip.setFocusPainted(false);
        chip.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addHoverFeedback(chip, new Color(250, 250, 255), new Color(239, 233, 255), new Color(232, 225, 252));
        return chip;
    }

    private JPanel aiSuggestionCard() {
        JPanel card = new LiftPanel(22, new Color(239, 233, 255), true);
        card.setLayout(new BorderLayout(14, 0));
        card.setBorder(new EmptyBorder(18, 20, 18, 20));
        JLabel icon = new JLabel("AI", SwingConstants.CENTER);
        icon.setOpaque(true);
        icon.setBackground(PURPLE);
        icon.setForeground(Color.WHITE);
        icon.setFont(FONT_SEMIBOLD);
        icon.setPreferredSize(new Dimension(50, 50));
        JLabel text = new JLabel("AI Suggestion card — smart troubleshooting tips will appear here after submission details are analyzed.");
        text.setFont(FONT_REGULAR);
        text.setForeground(TEXT_DARK);
        card.add(icon, BorderLayout.WEST);
        card.add(text, BorderLayout.CENTER);
        return card;
    }

    private JLabel fieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_SEMIBOLD);
        label.setForeground(TEXT_DARK);
        return label;
    }

    private JButton createLiftButton(String text, Color bg, Color fg, int fontSize, int height) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(210, height));
        button.setFont(new Font("Segoe UI", Font.BOLD, fontSize));
        button.setForeground(fg);
        button.setBackground(bg);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(0, 20, 0, 20));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addHoverFeedback(button, bg, brighten(bg), darken(bg));
        return button;
    }

    private void addHoverFeedback(JComponent component, Color normal, Color hover, Color pressed) {
        component.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                applyInteractiveState(component, hover, 26, new Color(PURPLE.getRed(), PURPLE.getGreen(), PURPLE.getBlue(), 90));
            }

            public void mouseExited(MouseEvent e) {
                applyInteractiveState(component, normal, 18, null);
            }

            public void mousePressed(MouseEvent e) {
                applyInteractiveState(component, pressed, 10, new Color(PURPLE.getRed(), PURPLE.getGreen(), PURPLE.getBlue(), 110));
            }

            public void mouseReleased(MouseEvent e) {
                applyInteractiveState(component, component.contains(e.getPoint()) ? hover : normal, component.contains(e.getPoint()) ? 26 : 18, component.contains(e.getPoint()) ? new Color(PURPLE.getRed(), PURPLE.getGreen(), PURPLE.getBlue(), 90) : null);
            }
        });
    }

    private void applyInteractiveState(JComponent component, Color color, int shadow, Color glow) {
        component.setBackground(color);
        if (component instanceof RoundedPanel) {
            RoundedPanel panel = (RoundedPanel) component;
            panel.setPanelColor(color);
            panel.setShadowStrength(shadow);
            panel.setGlowColor(glow);
        }
        component.repaint();
    }

    private Color brighten(Color color) {
        return blend(color, Color.WHITE, 0.18f);
    }

    private Color darken(Color color) {
        return blend(color, Color.BLACK, 0.08f);
    }

    private Color blend(Color a, Color b, float ratio) {
        return new Color((int) (a.getRed() * (1 - ratio) + b.getRed() * ratio), (int) (a.getGreen() * (1 - ratio) + b.getGreen() * ratio), (int) (a.getBlue() * (1 - ratio) + b.getBlue() * ratio));
    }

    private JPanel createNoticeBoardView() {
        JPanel page = pagePanel("Notice Board", "Latest hostel announcements, pinned updates, and important deadlines.", "Search & Read");
        JPanel content = new JPanel(new BorderLayout(0, 18));
        content.setOpaque(false);
        JTextField search = createSearchField("Search notices by title, category, or priority...");
        JPanel list = verticalListPanel();
        loadNotices(list, "");
        search.getDocument().addDocumentListener(new SimpleDocumentListener() {
            public void update() {
                loadNotices(list, search.getText());
            }
        });
        content.add(search, BorderLayout.NORTH);
        content.add(wrapScroll(list), BorderLayout.CENTER);
        page.add(content, BorderLayout.CENTER);
        return page;
    }

    private JPanel createMessMenuView() {
        JPanel page = pagePanel("Mess Menu", "Weekly meals from Monday to Sunday with today's menu highlighted.", "Weekly Menu");
        JPanel grid = new JPanel(new GridLayout(0, 2, 20, 20));
        grid.setOpaque(false);
        loadMessMenu(grid);
        page.add(wrapScroll(grid), BorderLayout.CENTER);
        return page;
    }

    private JPanel createFeedbackView() {
        JPanel page = pagePanel("Feedback", "Share clear feedback with ratings, reactions, categories, and optional anonymity.", "Student Voice");
        JPanel card = new RoundedPanel(28, Color.WHITE, true);
        card.setLayout(new GridBagLayout());
        card.setBorder(new EmptyBorder(28, 30, 30, 30));
        GridBagConstraints gbc = formGbc();
        card.add(fieldLabel("5-star Rating"), gbc);
        gbc.gridy++;
        JPanel stars = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        stars.setOpaque(false);
        for (int i = 1; i <= 5; i++) {
            stars.add(createStarButton(i));

        }
        card.add(stars, gbc);
        gbc.gridy++;
        card.add(fieldLabel("Emoji Reaction"), gbc);
        gbc.gridy++;
        card.add(chipRow(new String[]{"😍 Excellent", "🙂 Good", "😐 Okay", "😟 Needs work"}), gbc);
        gbc.gridy++;
        card.add(fieldLabel("Category"), gbc);
        gbc.gridy++;
        JComboBox<String> category = new JComboBox<>(new String[]{"Mess", "Maintenance", "Cleanliness", "Security", "Administration"});
        category.setFont(FONT_REGULAR);
        card.add(category, gbc);
        gbc.gridy++;
        card.add(fieldLabel("Feedback"), gbc);
        gbc.gridy++;
        JTextArea box = new JTextArea();
        box.setRows(8);
        box.setFont(FONT_REGULAR);
        box.setLineWrap(true);
        box.setWrapStyleWord(true);
        box.setBorder(new EmptyBorder(14, 14, 14, 14));
        card.add(new JScrollPane(box), gbc);
        gbc.gridy++;
        JLabel counter = new JLabel("0 / 500 characters");
        counter.setForeground(TEXT_MUTED);
        box.getDocument().addDocumentListener(new SimpleDocumentListener() {
            public void update() {
                counter.setText(Math.min(box.getText().length(), 500) + " / 500 characters");
            }
        });
        card.add(counter, gbc);
        gbc.gridy++;
        JCheckBox anonymous = new JCheckBox("Submit anonymously");
        anonymous.setOpaque(false);
        anonymous.setFont(FONT_REGULAR);
        card.add(anonymous, gbc);
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.EAST;
        JButton submit = createLiftButton("Submit Feedback", PURPLE, Color.WHITE, 16, 52);
        submit.addActionListener(e -> {
            feedbackList.add(new String[]{String.valueOf(category.getSelectedItem()), box.getText(), String.valueOf(anonymous.isSelected())});
            showSuccessDialog("Feedback Submitted", "Thank you! Your feedback has been recorded locally as sample UI data.");
            box.setText("");
        });
        card.add(submit, gbc);
        page.add(card, BorderLayout.CENTER);
        return page;
    }

    private JPanel createProfileView() {
        JPanel page = pagePanel("Profile", "Student, hostel, academic, and guardian details in one place.", "Profile Data");
        JPanel grid = new JPanel(new GridLayout(0, 2, 20, 20));
        grid.setOpaque(false);
        loadProfile(grid);
        page.add(wrapScroll(grid), BorderLayout.CENTER);
        return page;
    }

    private JPanel createSettingsView() {
        JPanel page = pagePanel("Settings", "Manage portal preferences and account actions.", "Preferences");
        JPanel list = verticalListPanel();
        list.add(settingRow("Notifications", "Receive updates for complaints and notices.", true));
        list.add(Box.createVerticalStrut(14));
        list.add(settingRow("Sound", "Enable lightweight confirmation sounds.", false));
        list.add(Box.createVerticalStrut(14));
        list.add(settingRow("Dark Mode", "Placeholder for future dark theme support.", false));
        list.add(Box.createVerticalStrut(14));
        list.add(infoCard("About", "Hostel Complaint Management System\nProduction-ready Swing student module UI."));
        list.add(Box.createVerticalStrut(14));
        list.add(createLiftButton("Logout", new Color(255, 235, 235), new Color(210, 60, 60), 16, 50));
        page.add(wrapScroll(list), BorderLayout.CENTER);
        return page;
    }

    private void seedSampleData() {
        latestNotices.add(new String[]{"Pinned", "High", "Maintenance", "Water supply maintenance", "Water supply will pause from 10:00 AM to 12:00 PM on Saturday for tank cleaning.", "07 Aug 2026"});
        latestNotices.add(new String[]{"Normal", "Medium", "Mess", "Special dinner menu", "Friday dinner includes paneer bowl, jeera rice, salad, and gulab jamun.", "06 Aug 2026"});
        latestNotices.add(new String[]{"Normal", "Low", "Events", "Hostel sports meet", "Register at the warden office before Monday evening.", "04 Aug 2026"});
        weeklyMessMenu.put("Monday", new String[]{"🥣 Poha & tea", "🍛 Dal rice", "☕ Sandwich", "🍲 Roti sabzi"});
        weeklyMessMenu.put("Tuesday", new String[]{"🥞 Idli sambar", "🍚 Rajma rice", "🍪 Biscuits", "🍜 Veg noodles"});
        weeklyMessMenu.put("Wednesday", new String[]{"🍞 Toast omelette", "🥘 Chole rice", "🍌 Fruit bowl", "🍛 Paneer roti"});
        weeklyMessMenu.put("Thursday", new String[]{"🥣 Upma", "🍲 Kadhi rice", "☕ Tea pakora", "🍝 Pasta"});
        weeklyMessMenu.put("Friday", new String[]{"🥪 Paratha curd", "🍛 Veg biryani", "🍰 Cake", "🍚 Paneer bowl"});
        weeklyMessMenu.put("Saturday", new String[]{"🥞 Dosa", "🍛 Thali", "🍿 Corn chaat", "🍲 Dal makhani"});
        weeklyMessMenu.put("Sunday", new String[]{"🥐 Chole bhature", "🍜 Fried rice", "🍨 Ice cream", "🍕 Pizza night"});
    }

    public void updateDashboard() {
        refreshDashboard();
    }

    public void refreshDashboard() {
        contentCards.removeAll();
        contentCards.add(createDashboardView(), "Dashboard");
        contentCards.add(createComplaintView(), "Submit Complaint");
        contentCards.add(createMyComplaintsView(), "My Complaints");
        contentCards.add(createNoticeBoardView(), "Notice Board");
        contentCards.add(createMessMenuView(), "Mess Menu");
        contentCards.add(createFeedbackView(), "Feedback");
        contentCards.add(createProfileView(), "Profile");
        contentCards.add(createSettingsView(), "Settings");
        contentLayout.show(contentCards, "Dashboard");
    }

    public void loadComplaints() {
    }

    private JPanel pagePanel(String title, String subtitle, String badgeText) {
        JPanel page = new JPanel(new BorderLayout(0, 22));
        page.setOpaque(false);
        page.setBorder(new EmptyBorder(30, 34, 30, 34));
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        JLabel h = new JLabel(title);
        h.setFont(new Font("Segoe UI", Font.BOLD, 30));
        h.setForeground(TEXT_DARK);
        JLabel sub = new JLabel(subtitle);
        sub.setFont(FONT_REGULAR);
        sub.setForeground(TEXT_MUTED);
        text.add(h);
        text.add(Box.createVerticalStrut(6));
        text.add(sub);
        JLabel badge = statusPill(badgeText, PURPLE);
        header.add(text, BorderLayout.WEST);
        header.add(badge, BorderLayout.EAST);
        page.add(header, BorderLayout.PAGE_START);
        return page;
    }

    private JTextField createSearchField(String placeholder) {
        JTextField f = new JTextField(placeholder);
        f.setFont(FONT_REGULAR);
        f.setForeground(TEXT_MUTED);
        f.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(226, 229, 238), 1, true), new EmptyBorder(14, 16, 14, 16)));
        return f;
    }

    private JPanel verticalListPanel() {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        return p;
    }

    private JScrollPane wrapScroll(Component c) {
        JScrollPane sp = new JScrollPane(c);
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getVerticalScrollBar().setUnitIncrement(16);
        return sp;
    }

    private JLabel statusPill(String text, Color color) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setOpaque(true);
        l.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 35));
        l.setForeground(color.darker());
        l.setFont(FONT_SEMIBOLD);
        l.setBorder(new EmptyBorder(8, 14, 8, 14));
        return l;
    }

    public void loadNotices(JPanel list, String query) {
        list.removeAll();
        String q = query.toLowerCase(Locale.ROOT);
        for (String[] n : latestNotices) {
            if (q.isEmpty() || String.join(" ", n).toLowerCase(Locale.ROOT).contains(q)) {
                list.add(noticeCard(n));

            }
            list.add(Box.createVerticalStrut(16));
        }
        list.revalidate();
        list.repaint();
    }

    private JPanel noticeCard(String[] n) {
        JPanel c = new RoundedPanel(24, Color.WHITE, true);
        c.setLayout(new BorderLayout(16, 10));
        c.setBorder(new EmptyBorder(22, 24, 22, 24));
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        top.setOpaque(false);
        top.add(statusPill(n[0], PURPLE));
        top.add(statusPill(n[1], statusColor(n[1].equals("High") ? "Rejected" : n[1].equals("Medium") ? "Pending" : "Resolved")));
        top.add(statusPill(n[2], new Color(42, 132, 246)));
        JLabel title = new JLabel(n[3]);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(TEXT_DARK);
        JTextArea body = new JTextArea(n[4]);
        body.setOpaque(false);
        body.setEditable(false);
        body.setLineWrap(true);
        body.setWrapStyleWord(true);
        body.setFont(FONT_REGULAR);
        body.setForeground(TEXT_MUTED);
        JButton read = createLiftButton("Read More", new Color(239, 233, 255), PURPLE, 14, 42);
        read.addActionListener(e -> JOptionPane.showMessageDialog(this, n[4], n[3], JOptionPane.INFORMATION_MESSAGE));
        c.add(top, BorderLayout.NORTH);
        c.add(title, BorderLayout.WEST);
        c.add(body, BorderLayout.CENTER);
        c.add(read, BorderLayout.EAST);
        addHoverFeedback(c, Color.WHITE, new Color(252, 252, 255), new Color(246, 247, 251));
        return c;
    }

    public void loadMessMenu(JPanel grid) {
        grid.removeAll();
        String today = LocalDate.now().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        for (Map.Entry<String, String[]> e : weeklyMessMenu.entrySet()) {
            grid.add(mealDayCard(e.getKey(), e.getValue(), e.getKey().equals(today)));

        }
    }

    private JPanel mealDayCard(String day, String[] meals, boolean today) {
        JPanel c = new RoundedPanel(24, today ? new Color(247, 244, 255) : Color.WHITE, true);
        c.setLayout(new GridLayout(0, 1, 8, 8));
        c.setBorder(new EmptyBorder(20, 22, 20, 22));
        JLabel d = new JLabel((today ? "✨ " : "") + day);
        d.setFont(new Font("Segoe UI", Font.BOLD, 21));
        d.setForeground(today ? PURPLE : TEXT_DARK);
        c.add(d);
        String[] labels = {"Breakfast", "Lunch", "Snacks", "Dinner"};
        for (int i = 0; i < labels.length; i++) {
            c.add(new JLabel(labels[i] + ":  " + meals[i]));

        }
        return c;
    }

    public void loadProfile(JPanel grid) {
        grid.removeAll();
        grid.add(profilePhotoCard());
        String[][] data = {{"Name", studentName}, {"Roll Number", rollNumber}, {"Room Number", roomNumber}, {"Hostel", hostel}, {"Branch", branch}, {"Email", email}, {"Phone", phone}, {"Guardian Name", guardianName}, {"Guardian Phone", guardianPhone}};
        for (String[] d : data) {
            grid.add(infoCard(d[0], d[1]));

        }
        JPanel actions = infoCard("Actions", "Update profile details or account security.");
        actions.add(createLiftButton("Edit Profile", PURPLE, Color.WHITE, 15, 44));
        actions.add(createLiftButton("Change Password", new Color(239, 233, 255), PURPLE, 15, 44));
        grid.add(actions);
    }

    private JPanel profilePhotoCard() {
        JPanel c = infoCard("Photo", "👤\nStudent profile picture placeholder");
        return c;
    }

    private JPanel infoCard(String title, String value) {
        JPanel c = new RoundedPanel(24, Color.WHITE, true);
        c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
        c.setBorder(new EmptyBorder(20, 22, 20, 22));
        JLabel t = new JLabel(title);
        t.setFont(FONT_SEMIBOLD);
        t.setForeground(TEXT_MUTED);
        JTextArea v = new JTextArea(value);
        v.setOpaque(false);
        v.setEditable(false);
        v.setFont(new Font("Segoe UI", Font.BOLD, 18));
        v.setForeground(TEXT_DARK);
        c.add(t);
        c.add(Box.createVerticalStrut(8));
        c.add(v);
        return c;
    }

    private GridBagConstraints formGbc() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(0, 0, 14, 0);
        return gbc;
    }

    private JButton createStarButton(int i) {
        JButton b = createLiftButton("★", new Color(255, 248, 224), ORANGE, 22, 46);
        b.setPreferredSize(new Dimension(56, 46));
        return b;
    }

    private JPanel chipRow(String[] chips) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        p.setOpaque(false);
        for (String c : chips) {
            p.add(statusPill(c, PURPLE));

        }
        return p;
    }

    private JPanel settingRow(String title, String subtitle, boolean selected) {
        JPanel c = infoCard(title, subtitle);
        JToggleButton toggle = new JToggleButton(selected ? "On" : "Off", selected);
        toggle.addActionListener(e -> toggle.setText(toggle.isSelected() ? "On" : "Off"));
        c.add(toggle);
        return c;
    }

    private void showSuccessDialog(String title, String msg) {
        JOptionPane.showMessageDialog(this, msg, title, JOptionPane.INFORMATION_MESSAGE);
    }

    private void showChatbotPopup() {
        JDialog d = new JDialog(this, "Hostel AI Assistant", false);
        d.setSize(440, 560);
        d.setLocationRelativeTo(this);
        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBorder(new EmptyBorder(18, 18, 18, 18));
        root.setBackground(BACKGROUND);
        JPanel history = verticalListPanel();
        history.add(chatBubble("Hi! I can help draft complaints, explain notices, and summarize hostel updates.", false));
        JTextField input = createSearchField("Type a message...");
        JButton send = createLiftButton("Send", PURPLE, Color.WHITE, 14, 44);
        send.addActionListener(e -> {
            String question = input.getText().trim();
            if (question.isEmpty()) return;

            history.add(chatBubble(question, true));
            JPanel loading = chatBubble("Thinking…", false);
            history.add(loading);
            input.setText("");
            input.setEnabled(false);
            send.setEnabled(false);
            history.revalidate();
            history.repaint();

            new SwingWorker<String, Void>() {
                @Override
                protected String doInBackground() {
                    return GeminiService.askGemini("You are HostelMate, a helpful assistant for a hostel student. "
                            + "Give concise, practical answers about complaints, notices, mess, safety, and hostel life. "
                            + "Do not invent official policies or claim that you performed an action. Student question: " + question);
                }

                @Override
                protected void done() {
                    history.remove(loading);
                    try {
                        history.add(chatBubble(get(), false));
                    } catch (Exception exception) {
                        history.add(chatBubble("Error: " + exception.getMessage(), false));
                    }
                    input.setEnabled(true);
                    send.setEnabled(true);
                    input.requestFocusInWindow();
                    history.revalidate();
                    history.repaint();
                }
            }.execute();
        });
        input.addActionListener(e -> send.doClick());
        JPanel bottom = new JPanel(new BorderLayout(8, 0));
        bottom.setOpaque(false);
        bottom.add(input, BorderLayout.CENTER);
        bottom.add(send, BorderLayout.EAST);
        JButton close = createLiftButton("Close", new Color(255, 235, 235), new Color(210, 60, 60), 13, 38);
        close.addActionListener(e -> d.dispose());
        JButton min = createLiftButton("Minimize", new Color(239, 233, 255), PURPLE, 13, 38);
        min.addActionListener(e -> d.setVisible(false));
        JPanel top = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        top.setOpaque(false);
        top.add(min);
        top.add(close);
        root.add(top, BorderLayout.NORTH);
        root.add(wrapScroll(history), BorderLayout.CENTER);
        root.add(bottom, BorderLayout.SOUTH);
        d.setContentPane(root);
        d.setVisible(true);
    }

    private JPanel chatBubble(String text, boolean mine) {
        JPanel p = new JPanel(new FlowLayout(mine ? FlowLayout.RIGHT : FlowLayout.LEFT));
        p.setOpaque(false);
        JLabel b = statusPill("<html><body style='width:230px'>" + html(text).replace("\n", "<br>")
                + "</body></html>", mine ? PURPLE : new Color(90, 96, 115));
        p.add(b);
        return p;
    }

    private String html(String text) {
        return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    private abstract static class SimpleDocumentListener implements DocumentListener {

        public void insertUpdate(DocumentEvent e) {
            update();
        }

        public void removeUpdate(DocumentEvent e) {
            update();
        }

        public void changedUpdate(DocumentEvent e) {
            update();
        }

        public abstract void update();
    }

    private String getGreeting() {
        int hour = LocalTime.now().getHour();
        if (hour < 12) {
            return "Good morning!";
        }
        if (hour < 17) {
            return "Good afternoon!";
        }
        return "Good evening!";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentDashboard().setVisible(true));
    }

    private static class AnimatedComplaintCard extends RoundedPanel {

        AnimatedComplaintCard() {
            super(24, Color.WHITE, true);
        }
    }

    private static class LiftPanel extends RoundedPanel {

        LiftPanel(int radius, Color color, boolean shadow) {
            super(radius, color, shadow);
        }
    }

    private static class RoundedPanel extends JPanel {

        private final int radius;
        private Color color;
        private final boolean shadow;
        private int shadowStrength = 18;
        private Color glowColor;

        RoundedPanel(int radius, Color color, boolean shadow) {
            this.radius = radius;
            this.color = color;
            this.shadow = shadow;
            setOpaque(false);
        }

        void setPanelColor(Color color) {
            this.color = color;
        }

        void setShadowStrength(int shadowStrength) {
            this.shadowStrength = shadowStrength;
        }

        void setGlowColor(Color glowColor) {
            this.glowColor = glowColor;
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (shadow) {
                g2.setColor(new Color(0, 0, 0, shadowStrength));
                g2.fillRoundRect(4, 6, getWidth() - 8, getHeight() - 10, radius, radius);
            }
            g2.setColor(color);
            g2.fillRoundRect(0, 0, getWidth() - 8, getHeight() - 10, radius, radius);
            if (glowColor != null) {
                g2.setColor(glowColor);
                g2.drawRoundRect(1, 1, getWidth() - 10, getHeight() - 12, radius, radius);
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class CircularAIButton extends JButton {

        CircularAIButton() {
            super("AI");
            setOpaque(false);
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(0, 0, 0, 38));
            g2.fillOval(4, 6, getWidth() - 8, getHeight() - 8);
            g2.setColor(PURPLE);
            g2.fillOval(0, 0, getWidth() - 8, getHeight() - 8);
            g2.setColor(ORANGE);
            g2.fillOval(getWidth() - 22, 8, 10, 10);
            g2.dispose();
            super.paintComponent(g);
        }

        public boolean contains(int x, int y) {
            int diameter = Math.min(getWidth(), getHeight()) - 8;
            int center = diameter / 2;
            int dx = x - center;
            int dy = y - center;
            return dx * dx + dy * dy <= center * center;
        }
    }

    private static class JLayeredDashboard extends JPanel {

        private final JPanel content;
        private final JButton floatingButton;

        JLayeredDashboard(JPanel content, JButton floatingButton) {
            this.content = content;
            this.floatingButton = floatingButton;
            setLayout(null);
            setBackground(BACKGROUND);
            add(content);
            add(floatingButton);
        }

        public void doLayout() {
            content.setBounds(0, 0, getWidth(), getHeight());
            int size = 64;
            floatingButton.setBounds(getWidth() - size - 32, getHeight() - size - 32, size, size);
        }

        protected void paintChildren(Graphics g) {
            super.paintChildren(g);
        }
    }
}
