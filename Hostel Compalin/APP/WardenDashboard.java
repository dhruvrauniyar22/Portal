import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;

/**
 * HostelMate AI - Senior Java Swing Warden Dashboard
 * Premium Dark SaaS Theme with Custom Components, Live Analytics,
 * Complaint Cards, Filter Engine, and Floating Slide-Out AI Assistant.
 */
public class WardenDashboard extends JFrame {

    // =========================================================================
    // COLOR PALETTE & DESIGN SYSTEM TOKENS
    // =========================================================================
    public static final Color COLOR_BG = new Color(15, 23, 42); // #0F172A (Deep Slate Dark)
    public static final Color COLOR_SIDEBAR = new Color(30, 41, 59); // #1E293B (Dark Blue Gray)
    public static final Color COLOR_CARD = new Color(51, 65, 85); // #334155 (Slate Card)
    public static final Color COLOR_CARD_HOVER = new Color(71, 85, 105); // #475569 (Hover Slate Card)
    public static final Color COLOR_PRIMARY = new Color(99, 102, 241); // #6366F1 (Indigo Primary)
    public static final Color COLOR_ACCENT = new Color(139, 92, 246); // #8B5CF6 (Purple Accent)
    public static final Color COLOR_WARNING = new Color(245, 158, 11); // #F59E0B (Amber Warning)
    public static final Color COLOR_SUCCESS = new Color(34, 197, 94); // #22C55E (Green Success)
    public static final Color COLOR_DANGER = new Color(239, 68, 68); // #EF4444 (Red Danger)
    public static final Color COLOR_TEXT_WHITE = new Color(248, 250, 252); // #F8FAFC
    public static final Color COLOR_TEXT_MUTED = new Color(148, 163, 184);// #94A3B8
    public static final Color COLOR_TEXT_SUBTLE = new Color(203, 213, 225);// #CBD5E1
    public static final Color COLOR_BORDER = new Color(71, 85, 105, 120);

    // Modern Typography Font Hierarchy
    public static final Font FONT_TITLE = new Font("Poppins", Font.BOLD, 22);
    public static final Font FONT_HEADER = new Font("Poppins", Font.BOLD, 17);
    public static final Font FONT_SUBHEADER = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);

    // CardLayout Container & State
    private final CardLayout mainCardLayout;
    private final JPanel mainContentCards;
    private JPanel complaintCardsContainer;
    private JPanel slideOutAIPanel;
    private boolean isAIPanelVisible = false;
    private JLabel timeLabel;
    private JLabel dateLabel;

    // Search and Filter State
    private String currentSearchQuery = "";
    private String currentPriorityFilter = "All";
    private String currentCategoryFilter = "All";
    private String currentStatusFilter = "All";
    private String currentSortFilter = "Newest First";

    // Dynamic In-Memory Data Models
    private final List<ComplaintItem> complaintList = new ArrayList<>();
    private final List<NoticeItem> noticeList = new ArrayList<>();
    private final List<StaffItem> staffList = new ArrayList<>();
    private final List<StudentItem> studentList = new ArrayList<>();
    private final List<FeedbackItem> feedbackList = new ArrayList<>();
    private MessMenuData messMenuData;

    // AI Chat History Container
    private JPanel chatMessageList;
    private JScrollPane chatScrollPane;

    public WardenDashboard() {
        setTitle("HostelMate AI - Warden Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1440, 900);
        setMinimumSize(new Dimension(1200, 750));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        // Main Layout Container
        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBackground(COLOR_BG);

        seedData();

        // 1. Sidebar Creation
        JPanel sidebar = createSidebar();

        // 2. Center Content with TopBar & CardLayout Views
        JPanel centerContainer = new JPanel(new BorderLayout());
        centerContainer.setBackground(COLOR_BG);

        JPanel topBar = createTopBar();
        centerContainer.add(topBar, BorderLayout.NORTH);

        mainCardLayout = new CardLayout();
        mainContentCards = new JPanel(mainCardLayout);
        mainContentCards.setOpaque(false);

        // Add Pages to CardLayout
        mainContentCards.add(createDashboard(), "Dashboard");
        mainContentCards.add(createComplaintsPage(), "Complaints");
        mainContentCards.add(createStudentsPage(), "Students");
        mainContentCards.add(createStaffPage(), "Staff");
        mainContentCards.add(createNoticeBoardPage(), "Notice Board");
        mainContentCards.add(createMessMenuPage(), "Mess Menu");
        mainContentCards.add(createAnalyticsPageWrapper(), "Analytics");
        mainContentCards.add(createFeedbackPage(), "Feedback");
        mainContentCards.add(createSettingsPage(), "Settings");

        centerContainer.add(mainContentCards, BorderLayout.CENTER);

        rootPanel.add(sidebar, BorderLayout.WEST);
        rootPanel.add(centerContainer, BorderLayout.CENTER);

        // 3. Layered Pane for Overlay Slide-Out AI & Floating AI Button
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setLayout(new OverlayLayout());

        // Background Main App View
        rootPanel.setBounds(0, 0, 1440, 900);
        layeredPane.add(rootPanel, Integer.valueOf(1));

        // Create Floating AI Slide-out Panel & Button
        JButton aiFloatingBtn = createFloatingAI();
        slideOutAIPanel = buildSlideOutAIPanel();
        slideOutAIPanel.setVisible(false);

        // Assembly of Layered View
        JPanel overlayWrapper = new JPanel(new BorderLayout());
        overlayWrapper.setOpaque(false);

        // Floating button anchored bottom right
        JPanel bottomRightAnchor = new JPanel(new FlowLayout(FlowLayout.RIGHT, 30, 30));
        bottomRightAnchor.setOpaque(false);
        bottomRightAnchor.add(aiFloatingBtn);

        overlayWrapper.add(slideOutAIPanel, BorderLayout.EAST);
        overlayWrapper.add(bottomRightAnchor, BorderLayout.SOUTH);

        layeredPane.add(overlayWrapper, Integer.valueOf(2));

        setContentPane(layeredPane);
        startClockTimer();
    }

    // =========================================================================
    // 1. SIDEBAR CREATION (Required Method: createSidebar)
    // =========================================================================
    public JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(260, 0));
        sidebar.setBackground(COLOR_SIDEBAR);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(24, 18, 24, 18));

        // Brand Header / Logo
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        logoPanel.setOpaque(false);
        logoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel logoIcon = new JLabel("⚡");
        logoIcon.setFont(new Font("Segoe UI Emoji", Font.BOLD, 26));
        logoIcon.setForeground(COLOR_ACCENT);

        JLabel logoText = new JLabel("HostelMate AI");
        logoText.setFont(new Font("Poppins", Font.BOLD, 20));
        logoText.setForeground(COLOR_TEXT_WHITE);

        logoPanel.add(logoIcon);
        logoPanel.add(logoText);
        sidebar.add(logoPanel);

        JLabel subtitle = new JLabel(" Chief Warden Workspace");
        subtitle.setFont(FONT_SMALL);
        subtitle.setForeground(COLOR_TEXT_MUTED);
        subtitle.setBorder(new EmptyBorder(4, 38, 20, 0));
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(subtitle);

        // Sidebar Navigation Items
        String[] menuItems = {
                "Dashboard", "Complaints", "Students", "Staff",
                "Notice Board", "Mess Menu", "Analytics", "Feedback", "Settings", "Logout"
        };
        String[] icons = {
                "📊", "🚨", "👨‍🎓", "🛠️", "📢", "🍽️", "📈", "💬", "⚙️", "🚪"
        };

        List<SidebarButton> btnList = new ArrayList<>();

        for (int i = 0; i < menuItems.length; i++) {
            final String pageName = menuItems[i];
            boolean isDefaultSelected = pageName.equals("Dashboard");
            SidebarButton btn = new SidebarButton(icons[i] + "   " + pageName, isDefaultSelected);
            btn.setAlignmentX(Component.LEFT_ALIGNMENT);
            btnList.add(btn);

            btn.addActionListener(e -> {
                if ("Logout".equals(pageName)) {
                    int confirm = JOptionPane.showConfirmDialog(
                            WardenDashboard.this,
                            "Are you sure you want to log out of HostelMate AI?",
                            "Confirm Logout",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE);
                    if (confirm == JOptionPane.YES_OPTION) {
                        dispose();
                    }
                } else {
                    for (SidebarButton sb : btnList) {
                        sb.setSelected(false);
                    }
                    btn.setSelected(true);
                    mainCardLayout.show(mainContentCards, pageName);
                }
            });

            sidebar.add(btn);
            sidebar.add(Box.createVerticalStrut(6));
        }

        sidebar.add(Box.createVerticalGlue());

        // Sidebar AI Badge Footer
        RoundedPanel aiFooterCard = new RoundedPanel(14, COLOR_CARD);
        aiFooterCard.setLayout(new BorderLayout(8, 8));
        aiFooterCard.setBorder(new EmptyBorder(12, 12, 12, 12));
        aiFooterCard.setMaximumSize(new Dimension(230, 75));
        aiFooterCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel aiFootTitle = new JLabel("🤖 AI Auto-Dispatch");
        aiFootTitle.setFont(FONT_SUBHEADER);
        aiFootTitle.setForeground(COLOR_TEXT_WHITE);

        JLabel aiFootSub = new JLabel("System Status: Active & Monitoring");
        aiFootSub.setFont(FONT_SMALL);
        aiFootSub.setForeground(COLOR_SUCCESS);

        aiFooterCard.add(aiFootTitle, BorderLayout.NORTH);
        aiFooterCard.add(aiFootSub, BorderLayout.SOUTH);
        sidebar.add(aiFooterCard);

        return sidebar;
    }

    // =========================================================================
    // 2. TOP BAR CREATION (Required Method: createTopBar)
    // =========================================================================
    public JPanel createTopBar() {
        JPanel topBar = new JPanel(new BorderLayout(15, 0));
        topBar.setPreferredSize(new Dimension(0, 72));
        topBar.setBackground(COLOR_SIDEBAR);
        topBar.setBorder(new EmptyBorder(12, 24, 12, 24));

        // Left Greeting
        JPanel greetingPanel = new JPanel();
        greetingPanel.setLayout(new BoxLayout(greetingPanel, BoxLayout.Y_AXIS));
        greetingPanel.setOpaque(false);

        JLabel welcomeLabel = new JLabel("Welcome Warden 👋");
        welcomeLabel.setFont(FONT_HEADER);
        welcomeLabel.setForeground(COLOR_TEXT_WHITE);

        JLabel subWelcome = new JLabel("HostelMate AI • Realtime Operational Dashboard");
        subWelcome.setFont(FONT_SMALL);
        subWelcome.setForeground(COLOR_TEXT_MUTED);

        greetingPanel.add(welcomeLabel);
        greetingPanel.add(subWelcome);

        // Center Search Bar
        JPanel searchContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        searchContainer.setOpaque(false);

        SearchTextField searchField = new SearchTextField("Search complaints, students, staff, notices...");
        searchField.setPreferredSize(new Dimension(360, 40));
        searchField.addActionListener(e -> {
            currentSearchQuery = searchField.getText().trim();
            refreshComplaintCards();
            mainCardLayout.show(mainContentCards, "Complaints");
        });

        searchContainer.add(searchField);

        // Right Info Panel (Date, Time, Bell, Avatar)
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 0));
        rightPanel.setOpaque(false);

        // Live Clock & Date
        JPanel timePanel = new JPanel();
        timePanel.setLayout(new BoxLayout(timePanel, BoxLayout.Y_AXIS));
        timePanel.setOpaque(false);

        timeLabel = new JLabel("00:00:00 AM");
        timeLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        timeLabel.setForeground(COLOR_TEXT_WHITE);

        dateLabel = new JLabel("Saturday, Aug 8");
        dateLabel.setFont(FONT_SMALL);
        dateLabel.setForeground(COLOR_TEXT_MUTED);

        timePanel.add(timeLabel);
        timePanel.add(dateLabel);

        // Notification Bell Button with Red Badge
        JButton bellBtn = new JButton("🔔") {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Draw Red Notification Count Badge
                g2.setColor(COLOR_DANGER);
                g2.fillOval(getWidth() - 14, 2, 12, 12);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 9));
                g2.drawString("3", getWidth() - 10, 11);
                g2.dispose();
            }
        };
        bellBtn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        bellBtn.setFocusPainted(false);
        bellBtn.setBorderPainted(false);
        bellBtn.setContentAreaFilled(false);
        bellBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bellBtn.setToolTipText("3 Urgent Notifications");
        bellBtn.addActionListener(e -> JOptionPane.showMessageDialog(
                this,
                "• Urgent: Water leak reported in Room B-304!\n" +
                        "• AI Recommendation: Reassign Electrician Ramesh to Block A\n" +
                        "• Daily Mess Survey: 4.6/5.0 Rating submitted by 320 students",
                "Warden Notifications",
                JOptionPane.INFORMATION_MESSAGE));

        // Warden Profile Avatar Circle
        JPanel avatarCircle = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COLOR_PRIMARY);
                g2.fillOval(0, 0, getWidth() - 1, getHeight() - 1);
                g2.setColor(COLOR_TEXT_WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 15));
                g2.drawString("W", 13, 25);
                // Green Online Dot
                g2.setColor(COLOR_SUCCESS);
                g2.fillOval(getWidth() - 10, getHeight() - 10, 9, 9);
                g2.dispose();
            }
        };
        avatarCircle.setPreferredSize(new Dimension(38, 38));
        avatarCircle.setOpaque(false);
        avatarCircle.setToolTipText("Chief Warden Admin");

        rightPanel.add(timePanel);
        rightPanel.add(bellBtn);
        rightPanel.add(avatarCircle);

        topBar.add(greetingPanel, BorderLayout.WEST);
        topBar.add(searchContainer, BorderLayout.CENTER);
        topBar.add(rightPanel, BorderLayout.EAST);

        return topBar;
    }

    // =========================================================================
    // 3. MAIN DASHBOARD CREATION (Required Method: createDashboard)
    // =========================================================================
    public JPanel createDashboard() {
        JPanel dashboard = new JPanel(new BorderLayout(0, 20));
        dashboard.setOpaque(false);
        dashboard.setBorder(new EmptyBorder(20, 24, 20, 24));

        // Scrollable Main Content Pane
        JPanel contentHolder = new JPanel();
        contentHolder.setLayout(new BoxLayout(contentHolder, BoxLayout.Y_AXIS));
        contentHolder.setOpaque(false);

        // 3A. Welcome Card with Gradient & AI Alert Insights
        GradientPanel welcomeCard = new GradientPanel(COLOR_PRIMARY, COLOR_ACCENT, 20);
        welcomeCard.setLayout(new BorderLayout(15, 15));
        welcomeCard.setBorder(new EmptyBorder(22, 24, 22, 24));
        welcomeCard.setMaximumSize(new Dimension(2000, 140));
        welcomeCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel heroLeft = new JPanel();
        heroLeft.setLayout(new BoxLayout(heroLeft, BoxLayout.Y_AXIS));
        heroLeft.setOpaque(false);

        JLabel heroTitle = new JLabel("Good Evening, Warden 👋");
        heroTitle.setFont(new Font("Poppins", Font.BOLD, 22));
        heroTitle.setForeground(COLOR_TEXT_WHITE);

        JLabel heroSub = new JLabel("Hostel Overview: Block A, B & C | 450 Occupants | 18 Active Support Staff");
        heroSub.setFont(FONT_BODY);
        heroSub.setForeground(new Color(238, 242, 255));

        JLabel aiAlert = new JLabel(
                "🤖 AI Insight: 3 Urgent plumbing complaints flagged in Block B. Auto-dispatch suggested.");
        aiAlert.setFont(new Font("Segoe UI", Font.BOLD, 13));
        aiAlert.setForeground(new Color(254, 240, 138)); // Amber light text
        aiAlert.setBorder(new EmptyBorder(8, 0, 0, 0));

        heroLeft.add(heroTitle);
        heroLeft.add(Box.createVerticalStrut(4));
        heroLeft.add(heroSub);
        heroLeft.add(aiAlert);

        // Hero Quick Action Buttons
        JPanel heroRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        heroRight.setOpaque(false);

        ModernButton dispatchBtn = new ModernButton("⚡ Auto-Assign Staff", COLOR_ACCENT, COLOR_TEXT_WHITE);
        dispatchBtn.addActionListener(e -> triggerAIAutoAssign());

        ModernButton broadcastBtn = new ModernButton("📢 Post Notice", COLOR_SIDEBAR, COLOR_TEXT_WHITE);
        broadcastBtn.addActionListener(e -> showCreateNoticeModal());

        heroRight.add(dispatchBtn);
        heroRight.add(broadcastBtn);

        welcomeCard.add(heroLeft, BorderLayout.CENTER);
        welcomeCard.add(heroRight, BorderLayout.EAST);

        contentHolder.add(welcomeCard);
        contentHolder.add(Box.createVerticalStrut(20));

        // 3B. Statistics Cards Grid (8 Stats Cards as required)
        JPanel statsGrid = new JPanel(new GridLayout(2, 4, 16, 16));
        statsGrid.setOpaque(false);
        statsGrid.setMaximumSize(new Dimension(2000, 220));
        statsGrid.setAlignmentX(Component.LEFT_ALIGNMENT);

        statsGrid.add(createStatCard("Pending Complaints", String.valueOf(getPendingCount()), "🚨", COLOR_WARNING,
                "+2 since 1 hr ago"));
        statsGrid.add(createStatCard("Resolved Today", String.valueOf(getResolvedTodayCount()), "✅", COLOR_SUCCESS,
                "88% SLA Target Met"));
        statsGrid.add(createStatCard("Urgent Complaints", String.valueOf(getUrgentCount()), "🔥", COLOR_DANGER,
                "Requires Immediate Action"));
        statsGrid.add(createStatCard("Total Students", String.valueOf(studentList.size()), "👨‍🎓", COLOR_PRIMARY,
                "96% Room Occupancy"));
        statsGrid.add(createStatCard("Staff On Duty", String.valueOf(staffList.size()), "🛠️", COLOR_ACCENT,
                "6 Shifts Active"));
        statsGrid.add(createStatCard("Avg Resolution Time", "1.8 hrs", "⏱️", COLOR_SUCCESS, "-25% vs Last Week"));
        statsGrid.add(createStatCard("Total Notices", String.valueOf(noticeList.size()), "📢", COLOR_WARNING,
                "2 Pinned Active"));
        statsGrid.add(createStatCard("Mess Rating", "4.6 ★", "🍽️", COLOR_PRIMARY, "320 Reviews"));

        contentHolder.add(statsGrid);
        contentHolder.add(Box.createVerticalStrut(24));

        // 3C. Filter Bar Header & Controls
        JPanel filterHeader = new JPanel(new BorderLayout());
        filterHeader.setOpaque(false);
        filterHeader.setMaximumSize(new Dimension(2000, 45));
        filterHeader.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sectionTitle = new JLabel("Live Complaint Feed & Operations");
        sectionTitle.setFont(FONT_HEADER);
        sectionTitle.setForeground(COLOR_TEXT_WHITE);

        JPanel filterControls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        filterControls.setOpaque(false);

        // Priority Filter
        JComboBox<String> priorityCombo = createStyledComboBox(new String[] { "Priority: All", "Priority: Urgent",
                "Priority: High", "Priority: Medium", "Priority: Low" });
        priorityCombo.addActionListener(e -> {
            String sel = (String) priorityCombo.getSelectedItem();
            currentPriorityFilter = sel.replace("Priority: ", "");
            refreshComplaintCards();
        });

        // Category Filter
        JComboBox<String> categoryCombo = createStyledComboBox(new String[] { "Category: All", "Category: Plumbing",
                "Category: Electrical", "Category: Internet", "Category: Cleanliness", "Category: Furniture" });
        categoryCombo.addActionListener(e -> {
            String sel = (String) categoryCombo.getSelectedItem();
            currentCategoryFilter = sel.replace("Category: ", "");
            refreshComplaintCards();
        });

        // Status Filter
        JComboBox<String> statusCombo = createStyledComboBox(
                new String[] { "Status: All", "Status: Pending", "Status: In Progress", "Status: Resolved" });
        statusCombo.addActionListener(e -> {
            String sel = (String) statusCombo.getSelectedItem();
            currentStatusFilter = sel.replace("Status: ", "");
            refreshComplaintCards();
        });

        // Sort Filter
        JComboBox<String> sortCombo = createStyledComboBox(
                new String[] { "Sort: Newest First", "Sort: Priority High-Low" });
        sortCombo.addActionListener(e -> {
            String sel = (String) sortCombo.getSelectedItem();
            currentSortFilter = sel.replace("Sort: ", "");
            refreshComplaintCards();
        });

        filterControls.add(priorityCombo);
        filterControls.add(categoryCombo);
        filterControls.add(statusCombo);
        filterControls.add(sortCombo);

        filterHeader.add(sectionTitle, BorderLayout.WEST);
        filterHeader.add(filterControls, BorderLayout.EAST);

        contentHolder.add(filterHeader);
        contentHolder.add(Box.createVerticalStrut(14));

        // 3D. Live Complaint Cards Scroll Area (Required Method: createComplaintCards)
        complaintCardsContainer = createComplaintCards();
        complaintCardsContainer.setAlignmentX(Component.LEFT_ALIGNMENT);

        contentHolder.add(complaintCardsContainer);

        JScrollPane dashboardScrollPane = new JScrollPane(contentHolder);
        dashboardScrollPane.setOpaque(false);
        dashboardScrollPane.getViewport().setOpaque(false);
        dashboardScrollPane.setBorder(null);
        dashboardScrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI());
        dashboardScrollPane.getVerticalScrollBar().setUnitIncrement(16);

        dashboard.add(dashboardScrollPane, BorderLayout.CENTER);
        return dashboard;
    }

    // =========================================================================
    // 4. LIVE COMPLAINT CARDS (Required Method: createComplaintCards)
    // =========================================================================
    public JPanel createComplaintCards() {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setOpaque(false);

        List<ComplaintItem> filtered = getFilteredComplaints();

        if (filtered.isEmpty()) {
            RoundedPanel emptyPanel = new RoundedPanel(16, COLOR_CARD);
            emptyPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 30));
            emptyPanel.setMaximumSize(new Dimension(2000, 100));

            JLabel emptyText = new JLabel("🎉 No complaints found matching current filters!");
            emptyText.setFont(FONT_HEADER);
            emptyText.setForeground(COLOR_TEXT_MUTED);
            emptyPanel.add(emptyText);
            container.add(emptyPanel);
        } else {
            for (ComplaintItem item : filtered) {
                JPanel card = buildSingleComplaintCard(item);
                card.setAlignmentX(Component.LEFT_ALIGNMENT);
                container.add(card);
                container.add(Box.createVerticalStrut(12));
            }
        }

        return container;
    }

    private JPanel buildSingleComplaintCard(ComplaintItem item) {
        RoundedPanel card = new RoundedPanel(16, COLOR_CARD);
        card.setLayout(new BorderLayout(16, 12));
        card.setBorder(new EmptyBorder(16, 20, 16, 20));
        card.setMaximumSize(new Dimension(2000, 140));

        // Hover animation highlight effect
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(COLOR_CARD_HOVER);
                card.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(COLOR_CARD);
                card.repaint();
            }
        });

        // Left Section: Category Icon & Student Details
        JPanel leftSection = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
        leftSection.setOpaque(false);

        // Icon Avatar Box
        RoundedPanel iconBox = new RoundedPanel(14, getCategoryColor(item.category));
        iconBox.setPreferredSize(new Dimension(48, 48));
        iconBox.setLayout(new GridBagLayout());

        JLabel iconLabel = new JLabel(getCategoryIcon(item.category));
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
        iconBox.add(iconLabel);

        JPanel detailsText = new JPanel();
        detailsText.setLayout(new BoxLayout(detailsText, BoxLayout.Y_AXIS));
        detailsText.setOpaque(false);

        JPanel titleRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        titleRow.setOpaque(false);

        JLabel studentName = new JLabel(item.studentName);
        studentName.setFont(FONT_HEADER);
        studentName.setForeground(COLOR_TEXT_WHITE);

        JLabel roomBadge = createPillLabel(item.roomNumber + " (" + item.hostelBlock + ")", COLOR_SIDEBAR,
                COLOR_TEXT_SUBTLE);
        JLabel priorityBadge = createPillLabel(item.priority, getPriorityColor(item.priority), COLOR_TEXT_WHITE);
        JLabel statusBadge = createPillLabel(item.status, getStatusColor(item.status), COLOR_TEXT_WHITE);

        titleRow.add(studentName);
        titleRow.add(roomBadge);
        titleRow.add(priorityBadge);
        titleRow.add(statusBadge);

        JLabel descLabel = new JLabel("<html><body style='width: 500px;'>" + item.description + "</body></html>");
        descLabel.setFont(FONT_BODY);
        descLabel.setForeground(COLOR_TEXT_SUBTLE);
        descLabel.setBorder(new EmptyBorder(4, 0, 4, 0));

        JLabel metaLabel = new JLabel("🕒 Submitted: " + item.timeSubmitted + " • Assigned: " + item.assignedStaff);
        metaLabel.setFont(FONT_SMALL);
        metaLabel.setForeground(COLOR_TEXT_MUTED);

        detailsText.add(titleRow);
        detailsText.add(descLabel);
        detailsText.add(metaLabel);

        leftSection.add(iconBox);
        leftSection.add(detailsText);

        // Right Section: Image Attachment & Action Buttons (View, Assign, Resolve)
        JPanel rightSection = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        rightSection.setOpaque(false);

        // Image Attachment Thumbnail Placeholder
        RoundedPanel imgThumb = new RoundedPanel(10, COLOR_SIDEBAR);
        imgThumb.setPreferredSize(new Dimension(85, 45));
        imgThumb.setLayout(new GridBagLayout());

        JLabel imgText = new JLabel("📷 Attachment");
        imgText.setFont(FONT_SMALL);
        imgText.setForeground(COLOR_ACCENT);
        imgThumb.add(imgText);
        imgThumb.setCursor(new Cursor(Cursor.HAND_CURSOR));
        imgThumb.setToolTipText("Click to view full complaint image attachment");
        imgThumb.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showImageAttachmentDialog(item);
            }
        });

        ModernButton viewBtn = new ModernButton("View", COLOR_SIDEBAR, COLOR_TEXT_WHITE);
        viewBtn.setPreferredSize(new Dimension(75, 36));
        viewBtn.addActionListener(e -> showComplaintDetailsModal(item));

        ModernButton assignBtn = new ModernButton("Assign", COLOR_PRIMARY, COLOR_TEXT_WHITE);
        assignBtn.setPreferredSize(new Dimension(80, 36));
        assignBtn.addActionListener(e -> showAssignStaffModal(item));

        ModernButton resolveBtn = new ModernButton("Resolve", COLOR_SUCCESS, COLOR_TEXT_WHITE);
        resolveBtn.setPreferredSize(new Dimension(85, 36));
        resolveBtn.addActionListener(e -> {
            item.status = "Resolved";
            JOptionPane.showMessageDialog(this, "Complaint #" + item.id + " marked as Resolved!", "Status Updated",
                    JOptionPane.INFORMATION_MESSAGE);
            refreshComplaintCards();
        });

        rightSection.add(imgThumb);
        rightSection.add(viewBtn);
        rightSection.add(assignBtn);
        rightSection.add(resolveBtn);

        card.add(leftSection, BorderLayout.CENTER);
        card.add(rightSection, BorderLayout.EAST);

        return card;
    }

    // =========================================================================
    // 5. FLOATING AI BUTTON & SLIDE-OUT PANEL (Required Method: createFloatingAI)
    // =========================================================================
    public JButton createFloatingAI() {
        JButton aiBtn = new JButton("🤖 AI") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Purple Glow Aura
                g2.setColor(new Color(139, 92, 246, 90));
                g2.fillOval(0, 0, getWidth(), getHeight());

                // Main Gradient Circle Button
                GradientPaint gp = new GradientPaint(0, 0, COLOR_ACCENT, getWidth(), getHeight(), COLOR_PRIMARY);
                g2.setPaint(gp);
                g2.fillOval(4, 4, getWidth() - 8, getHeight() - 8);

                // White Text / Icon
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Poppins", Font.BOLD, 15));
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);

                g2.dispose();
            }
        };

        aiBtn.setPreferredSize(new Dimension(64, 64));
        aiBtn.setFocusPainted(false);
        aiBtn.setBorderPainted(false);
        aiBtn.setContentAreaFilled(false);
        aiBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        aiBtn.setToolTipText("Open HostelMate AI Assistant");

        aiBtn.addActionListener(e -> toggleAIPanel());

        return aiBtn;
    }

    private JPanel buildSlideOutAIPanel() {
        JPanel aiDrawer = new RoundedPanel(20, COLOR_SIDEBAR);
        aiDrawer.setPreferredSize(new Dimension(380, 780));
        aiDrawer.setLayout(new BorderLayout());
        aiDrawer.setBorder(new EmptyBorder(18, 18, 18, 18));

        // Drawer Header
        JPanel drawerHeader = new JPanel(new BorderLayout());
        drawerHeader.setOpaque(false);
        drawerHeader.setBorder(new EmptyBorder(0, 0, 14, 0));

        JLabel title = new JLabel("🤖 HostelMate AI Assistant");
        title.setFont(FONT_HEADER);
        title.setForeground(COLOR_TEXT_WHITE);

        JButton closeBtn = new JButton("✕");
        closeBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        closeBtn.setForeground(COLOR_TEXT_MUTED);
        closeBtn.setFocusPainted(false);
        closeBtn.setBorderPainted(false);
        closeBtn.setContentAreaFilled(false);
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.addActionListener(e -> toggleAIPanel());

        drawerHeader.add(title, BorderLayout.WEST);
        drawerHeader.add(closeBtn, BorderLayout.EAST);

        // Suggested AI Quick Prompts Pills
        JPanel suggestedContainer = new JPanel();
        suggestedContainer.setLayout(new BoxLayout(suggestedContainer, BoxLayout.Y_AXIS));
        suggestedContainer.setOpaque(false);

        JLabel suggestedTitle = new JLabel("Suggested AI Queries:");
        suggestedTitle.setFont(FONT_SMALL);
        suggestedTitle.setForeground(COLOR_TEXT_MUTED);
        suggestedTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        suggestedContainer.add(suggestedTitle);
        suggestedContainer.add(Box.createVerticalStrut(6));

        String[] quickQueries = {
                "⚡ Find urgent complaints",
                "📋 Summarize complaints",
                "👨‍🔧 Suggest staff allocation",
                "📢 Generate notice",
                "📜 Answer hostel rules",
                "📊 Generate report"
        };

        JPanel chipsGrid = new JPanel(new GridLayout(3, 2, 6, 6));
        chipsGrid.setOpaque(false);
        chipsGrid.setAlignmentX(Component.LEFT_ALIGNMENT);

        for (String q : quickQueries) {
            ModernButton chip = new ModernButton(q, COLOR_CARD, COLOR_TEXT_WHITE);
            chip.setFont(FONT_SMALL);
            chip.addActionListener(e -> processAIQuery(q));
            chipsGrid.add(chip);
        }

        suggestedContainer.add(chipsGrid);
        suggestedContainer.add(Box.createVerticalStrut(12));

        // Chat Message History Area
        chatMessageList = new JPanel();
        chatMessageList.setLayout(new BoxLayout(chatMessageList, BoxLayout.Y_AXIS));
        chatMessageList.setOpaque(false);

        // Welcome Message from AI
        appendChatMessage("HostelMate AI",
                "Hello Warden! I am your AI Assistant. How can I assist you with hostel management today?", false);

        chatScrollPane = new JScrollPane(chatMessageList);
        chatScrollPane.setOpaque(false);
        chatScrollPane.getViewport().setOpaque(false);
        chatScrollPane.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, COLOR_BORDER));
        chatScrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI());

        // Bottom Input Box
        JPanel inputPanel = new JPanel(new BorderLayout(8, 0));
        inputPanel.setOpaque(false);
        inputPanel.setBorder(new EmptyBorder(12, 0, 0, 0));

        SearchTextField inputField = new SearchTextField("Type your prompt or question...");
        ModernButton sendBtn = new ModernButton("Send", COLOR_ACCENT, COLOR_TEXT_WHITE);

        sendBtn.addActionListener(e -> {
            String userText = inputField.getText().trim();
            if (!userText.isEmpty()) {
                appendChatMessage("Warden", userText, true);
                inputField.setText("");
                processAIQuery(userText);
            }
        });

        inputField.addActionListener(e -> sendBtn.doClick());

        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendBtn, BorderLayout.EAST);

        // Assembly
        JPanel topWrapper = new JPanel(new BorderLayout());
        topWrapper.setOpaque(false);
        topWrapper.add(drawerHeader, BorderLayout.NORTH);
        topWrapper.add(suggestedContainer, BorderLayout.SOUTH);

        aiDrawer.add(topWrapper, BorderLayout.NORTH);
        aiDrawer.add(chatScrollPane, BorderLayout.CENTER);
        aiDrawer.add(inputPanel, BorderLayout.SOUTH);

        return aiDrawer;
    }

    private void toggleAIPanel() {
        isAIPanelVisible = !isAIPanelVisible;
        slideOutAIPanel.setVisible(isAIPanelVisible);
        slideOutAIPanel.revalidate();
        slideOutAIPanel.repaint();
    }

    private void appendChatMessage(String sender, String text, boolean isUser) {
        JPanel msgBubble = new RoundedPanel(14, isUser ? COLOR_PRIMARY : COLOR_CARD);
        msgBubble.setLayout(new BorderLayout(6, 4));
        msgBubble.setBorder(new EmptyBorder(10, 12, 10, 12));
        msgBubble.setMaximumSize(new Dimension(320, 1000));
        msgBubble.setAlignmentX(isUser ? Component.RIGHT_ALIGNMENT : Component.LEFT_ALIGNMENT);

        JLabel senderLabel = new JLabel(sender);
        senderLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        senderLabel.setForeground(isUser ? new Color(224, 231, 255) : COLOR_ACCENT);

        JLabel contentLabel = new JLabel(
                "<html><body style='width: 220px;'>" + text.replaceAll("\n", "<br>") + "</body></html>");
        contentLabel.setFont(FONT_BODY);
        contentLabel.setForeground(COLOR_TEXT_WHITE);

        msgBubble.add(senderLabel, BorderLayout.NORTH);
        msgBubble.add(contentLabel, BorderLayout.CENTER);

        chatMessageList.add(msgBubble);
        chatMessageList.add(Box.createVerticalStrut(8));
        chatMessageList.revalidate();

        // Scroll to bottom
        SwingUtilities.invokeLater(() -> {
            chatScrollPane.getVerticalScrollBar().setValue(chatScrollPane.getVerticalScrollBar().getMaximum());
        });
    }

    private void processAIQuery(String query) {
        String cleanQuery = query.replaceAll("^[⚡📋👨‍🔧📢📜📊]\\s*", "").toLowerCase();
        String response;

        if (cleanQuery.contains("urgent") || cleanQuery.contains("find urgent")) {
            response = "🚨 **Urgent Complaint Alert**:\n" +
                    "• **CMP-1089**: Pipe leakage in Room B-304 (Rahul Verma, Block B).\n" +
                    "• Risk: Water ingress near main switchboard.\n" +
                    "• Suggested Action: Assign Suresh Sharma (Plumber) immediately!";
        } else if (cleanQuery.contains("summarize") || cleanQuery.contains("summary")) {
            response = "📊 **Complaint Executive Summary**:\n" +
                    "• Total Pending: " + getPendingCount() + "\n" +
                    "• Plumbing: 2 | Electrical: 1 | Internet: 1\n" +
                    "• Resolution SLA: 88% on-time completion today.\n" +
                    "• Block B has the highest active complaint volume.";
        } else if (cleanQuery.contains("suggest staff") || cleanQuery.contains("staff allocation")) {
            response = "👨‍🔧 **AI Recommended Staff Assignments**:\n" +
                    "1. **CMP-1089** (Plumbing) ➔ Assign Suresh Sharma (Plumber, Available)\n" +
                    "2. **CMP-1088** (Electrical) ➔ Assign Ramesh Kumar (Electrician, On Duty)\n" +
                    "3. **CMP-1087** (Internet) ➔ Assign Amit Verma (IT Admin)";
        } else if (cleanQuery.contains("notice") || cleanQuery.contains("generate notice")) {
            response = "📢 **AI Generated Notice Draft**:\n" +
                    "\"Attention Block B Residents: Urgent plumbing maintenance will be carried out today between 3 PM - 4 PM. Water supply temporarily paused. We regret the inconvenience.\"";
        } else if (cleanQuery.contains("rules") || cleanQuery.contains("hostel rules")) {
            response = "📜 **Hostel Rules Quick Reference**:\n" +
                    "• Night Curfew: 10:00 PM Sharp.\n" +
                    "• Visitor Policy: Visitors allowed only in reception area till 7:00 PM.\n" +
                    "• Quiet Hours: 10:30 PM - 6:00 AM.\n" +
                    "• Emergency Escalation: Call Warden Desk Ext. 101.";
        } else if (cleanQuery.contains("report") || cleanQuery.contains("generate report")) {
            response = "📊 **Hostel Operations Daily Report**:\n" +
                    "• Total Occupants: 450\n" +
                    "• Active Complaints: 4\n" +
                    "• Resolved Today: " + getResolvedTodayCount() + "\n" +
                    "• Mess Satisfaction: 4.6/5.0 Stars\n" +
                    "• Overall Security Compliance: 100%";
        } else {
            response = "🤖 **HostelMate AI**: I analyzed your request regarding \"" + query
                    + "\". Everything is running smoothly across Block A, B & C. Would you like me to dispatch staff or update notices?";
        }

        appendChatMessage("HostelMate AI", response, false);
    }

    // =========================================================================
    // 6. ANALYTICS PAGE CREATION (Required Method: createAnalytics)
    // =========================================================================
    public JPanel createAnalytics() {
        JPanel analytics = new JPanel(new BorderLayout(0, 20));
        analytics.setOpaque(false);
        analytics.setBorder(new EmptyBorder(20, 24, 20, 24));

        JLabel title = new JLabel("Hostel Operations & AI Analytics");
        title.setFont(FONT_TITLE);
        title.setForeground(COLOR_TEXT_WHITE);
        analytics.add(title, BorderLayout.NORTH);

        JPanel chartsContainer = new JPanel(new GridLayout(1, 2, 20, 0));
        chartsContainer.setOpaque(false);

        // Chart 1: Painted Complaint Trend Line Graph
        RoundedPanel lineChartCard = new RoundedPanel(18, COLOR_CARD);
        lineChartCard.setLayout(new BorderLayout(10, 10));
        lineChartCard.setBorder(new EmptyBorder(18, 18, 18, 18));

        JLabel lineTitle = new JLabel("📈 Weekly Complaint Volume Trend");
        lineTitle.setFont(FONT_HEADER);
        lineTitle.setForeground(COLOR_TEXT_WHITE);

        PaintedLineChart lineChart = new PaintedLineChart();

        lineChartCard.add(lineTitle, BorderLayout.NORTH);
        lineChartCard.add(lineChart, BorderLayout.CENTER);

        // Chart 2: Category Distribution Bar Chart
        RoundedPanel barChartCard = new RoundedPanel(18, COLOR_CARD);
        barChartCard.setLayout(new BorderLayout(10, 10));
        barChartCard.setBorder(new EmptyBorder(18, 18, 18, 18));

        JLabel barTitle = new JLabel("📊 Complaint Distribution by Category");
        barTitle.setFont(FONT_HEADER);
        barTitle.setForeground(COLOR_TEXT_WHITE);

        PaintedBarChart barChart = new PaintedBarChart();

        barChartCard.add(barTitle, BorderLayout.NORTH);
        barChartCard.add(barChart, BorderLayout.CENTER);

        chartsContainer.add(lineChartCard);
        chartsContainer.add(barChartCard);

        analytics.add(chartsContainer, BorderLayout.CENTER);
        return analytics;
    }

    private JPanel createAnalyticsPageWrapper() {
        return createAnalytics();
    }

    // =========================================================================
    // DEDICATED PAGE VIEWS (Complaints, Students, Staff, Notice, Mess, Feedback,
    // Settings)
    // =========================================================================

    // Complaints View
    private JPanel createComplaintsPage() {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(20, 24, 20, 24));

        JLabel title = new JLabel("Comprehensive Complaint Management");
        title.setFont(FONT_TITLE);
        title.setForeground(COLOR_TEXT_WHITE);

        JPanel content = new JPanel(new BorderLayout());
        content.setOpaque(false);
        content.add(createComplaintCards(), BorderLayout.CENTER);

        panel.add(title, BorderLayout.NORTH);
        panel.add(content, BorderLayout.CENTER);
        return panel;
    }

    // Students View
    private JPanel createStudentsPage() {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(20, 24, 20, 24));

        JLabel title = new JLabel("Student Directory & Hostel Allocations");
        title.setFont(FONT_TITLE);
        title.setForeground(COLOR_TEXT_WHITE);

        JPanel grid = new JPanel(new GridLayout(2, 3, 16, 16));
        grid.setOpaque(false);

        for (StudentItem s : studentList) {
            RoundedPanel card = new RoundedPanel(16, COLOR_CARD);
            card.setLayout(new BorderLayout(10, 10));
            card.setBorder(new EmptyBorder(16, 16, 16, 16));

            JLabel name = new JLabel(s.name);
            name.setFont(FONT_HEADER);
            name.setForeground(COLOR_TEXT_WHITE);

            JLabel sub = new JLabel("<html>Roll: " + s.roll + "<br>Room: " + s.room + " (" + s.block + ")<br>Phone: "
                    + s.phone + "</html>");
            sub.setFont(FONT_BODY);
            sub.setForeground(COLOR_TEXT_SUBTLE);

            ModernButton btn = new ModernButton("View Profile", COLOR_SIDEBAR, COLOR_TEXT_WHITE);
            btn.addActionListener(
                    e -> JOptionPane
                            .showMessageDialog(this,
                                    "Student Details for " + s.name + "\nRoll: " + s.roll + "\nRoom: " + s.room
                                            + "\nStatus: Fee Cleared",
                                    "Student Info", JOptionPane.INFORMATION_MESSAGE));

            card.add(name, BorderLayout.NORTH);
            card.add(sub, BorderLayout.CENTER);
            card.add(btn, BorderLayout.SOUTH);

            grid.add(card);
        }

        panel.add(title, BorderLayout.NORTH);
        panel.add(grid, BorderLayout.CENTER);
        return panel;
    }

    // Staff View
    private JPanel createStaffPage() {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(20, 24, 20, 24));

        JLabel title = new JLabel("Hostel Maintenance & Support Staff");
        title.setFont(FONT_TITLE);
        title.setForeground(COLOR_TEXT_WHITE);

        JPanel grid = new JPanel(new GridLayout(2, 3, 16, 16));
        grid.setOpaque(false);

        for (StaffItem st : staffList) {
            RoundedPanel card = new RoundedPanel(16, COLOR_CARD);
            card.setLayout(new BorderLayout(10, 10));
            card.setBorder(new EmptyBorder(16, 16, 16, 16));

            JLabel name = new JLabel(st.name);
            name.setFont(FONT_HEADER);
            name.setForeground(COLOR_TEXT_WHITE);

            JLabel sub = new JLabel("<html>Role: " + st.role + "<br>Status: " + st.status + "<br>Active Tasks: "
                    + st.activeTasks + "<br>Phone: " + st.phone + "</html>");
            sub.setFont(FONT_BODY);
            sub.setForeground(COLOR_TEXT_SUBTLE);

            ModernButton btn = new ModernButton("Assign Duty", COLOR_PRIMARY, COLOR_TEXT_WHITE);
            btn.addActionListener(
                    e -> JOptionPane.showMessageDialog(this, "Assign task to " + st.name + " (" + st.role + ")",
                            "Staff Assignment", JOptionPane.INFORMATION_MESSAGE));

            card.add(name, BorderLayout.NORTH);
            card.add(sub, BorderLayout.CENTER);
            card.add(btn, BorderLayout.SOUTH);

            grid.add(card);
        }

        panel.add(title, BorderLayout.NORTH);
        panel.add(grid, BorderLayout.CENTER);
        return panel;
    }

    // Notice Board View
    private JPanel createNoticeBoardPage() {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(20, 24, 20, 24));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel title = new JLabel("Hostel Official Notice Board");
        title.setFont(FONT_TITLE);
        title.setForeground(COLOR_TEXT_WHITE);

        ModernButton createNoticeBtn = new ModernButton("➕ Create Notice", COLOR_PRIMARY, COLOR_TEXT_WHITE);
        createNoticeBtn.addActionListener(e -> showCreateNoticeModal());

        header.add(title, BorderLayout.WEST);
        header.add(createNoticeBtn, BorderLayout.EAST);

        JPanel grid = new JPanel(new GridLayout(2, 2, 16, 16));
        grid.setOpaque(false);

        for (NoticeItem n : noticeList) {
            RoundedPanel card = new RoundedPanel(16, COLOR_CARD);
            card.setLayout(new BorderLayout(10, 10));
            card.setBorder(new EmptyBorder(16, 16, 16, 16));

            JPanel cardTop = new JPanel(new BorderLayout());
            cardTop.setOpaque(false);

            JLabel nTitle = new JLabel(n.title);
            nTitle.setFont(FONT_HEADER);
            nTitle.setForeground(COLOR_TEXT_WHITE);

            JLabel pinBadge = createPillLabel(n.isPinned ? "📌 Pinned" : "Notice",
                    n.isPinned ? COLOR_ACCENT : COLOR_SIDEBAR, COLOR_TEXT_WHITE);

            cardTop.add(nTitle, BorderLayout.WEST);
            cardTop.add(pinBadge, BorderLayout.EAST);

            JLabel nBody = new JLabel("<html><body style='width: 300px;'>" + n.body + "</body></html>");
            nBody.setFont(FONT_BODY);
            nBody.setForeground(COLOR_TEXT_SUBTLE);

            JPanel cardBot = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
            cardBot.setOpaque(false);

            JLabel nMeta = new JLabel("Target: " + n.target + " • Date: " + n.date);
            nMeta.setFont(FONT_SMALL);
            nMeta.setForeground(COLOR_TEXT_MUTED);

            ModernButton deleteBtn = new ModernButton("Delete", COLOR_DANGER, COLOR_TEXT_WHITE);
            deleteBtn.setFont(FONT_SMALL);
            deleteBtn.addActionListener(e -> {
                noticeList.remove(n);
                mainCardLayout.show(mainContentCards, "Dashboard");
                mainCardLayout.show(mainContentCards, "Notice Board");
            });

            cardBot.add(nMeta);
            cardBot.add(deleteBtn);

            card.add(cardTop, BorderLayout.NORTH);
            card.add(nBody, BorderLayout.CENTER);
            card.add(cardBot, BorderLayout.SOUTH);

            grid.add(card);
        }

        panel.add(header, BorderLayout.NORTH);
        panel.add(grid, BorderLayout.CENTER);
        return panel;
    }

    // Mess Menu View
    private JPanel createMessMenuPage() {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(20, 24, 20, 24));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel title = new JLabel("Today's Mess Menu & Rating (4.6 ★)");
        title.setFont(FONT_TITLE);
        title.setForeground(COLOR_TEXT_WHITE);

        ModernButton updateBtn = new ModernButton("✏️ Update Menu", COLOR_ACCENT, COLOR_TEXT_WHITE);
        updateBtn.addActionListener(e -> showUpdateMessModal());

        header.add(title, BorderLayout.WEST);
        header.add(updateBtn, BorderLayout.EAST);

        JPanel grid = new JPanel(new GridLayout(2, 2, 16, 16));
        grid.setOpaque(false);

        grid.add(createMealCard("🥣 Breakfast (07:30 AM - 09:30 AM)", messMenuData.breakfast));
        grid.add(createMealCard("🍛 Lunch (12:30 PM - 02:30 PM)", messMenuData.lunch));
        grid.add(createMealCard("☕ Evening Snacks (05:00 PM - 06:00 PM)", messMenuData.snacks));
        grid.add(createMealCard("🍲 Dinner (07:30 PM - 09:30 PM)", messMenuData.dinner));

        panel.add(header, BorderLayout.NORTH);
        panel.add(grid, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createMealCard(String mealTitle, String items) {
        RoundedPanel card = new RoundedPanel(16, COLOR_CARD);
        card.setLayout(new BorderLayout(10, 10));
        card.setBorder(new EmptyBorder(16, 16, 16, 16));

        JLabel title = new JLabel(mealTitle);
        title.setFont(FONT_HEADER);
        title.setForeground(COLOR_PRIMARY);

        JLabel content = new JLabel(
                "<html><body style='width: 320px;'>" + items.replaceAll(", ", "<br>• ") + "</body></html>");
        content.setFont(FONT_BODY);
        content.setForeground(COLOR_TEXT_WHITE);

        card.add(title, BorderLayout.NORTH);
        card.add(content, BorderLayout.CENTER);
        return card;
    }

    // Feedback View
    private JPanel createFeedbackPage() {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(20, 24, 20, 24));

        JLabel title = new JLabel("Student Feedback & Sentiment Analysis");
        title.setFont(FONT_TITLE);
        title.setForeground(COLOR_TEXT_WHITE);

        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setOpaque(false);

        for (FeedbackItem fb : feedbackList) {
            RoundedPanel card = new RoundedPanel(14, COLOR_CARD);
            card.setLayout(new BorderLayout(10, 10));
            card.setBorder(new EmptyBorder(14, 16, 14, 16));
            card.setMaximumSize(new Dimension(2000, 80));

            JLabel student = new JLabel(fb.studentName + " (" + fb.room + ") - " + fb.rating);
            student.setFont(FONT_SUBHEADER);
            student.setForeground(COLOR_TEXT_WHITE);

            JLabel comment = new JLabel("\"" + fb.comment + "\"");
            comment.setFont(FONT_BODY);
            comment.setForeground(COLOR_TEXT_SUBTLE);

            JLabel pill = createPillLabel(fb.sentiment,
                    "Positive".equalsIgnoreCase(fb.sentiment) ? COLOR_SUCCESS : COLOR_WARNING, COLOR_TEXT_WHITE);

            card.add(student, BorderLayout.NORTH);
            card.add(comment, BorderLayout.CENTER);
            card.add(pill, BorderLayout.EAST);

            list.add(card);
            list.add(Box.createVerticalStrut(10));
        }

        panel.add(title, BorderLayout.NORTH);
        panel.add(list, BorderLayout.CENTER);
        return panel;
    }

    // Settings View
    private JPanel createSettingsPage() {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(20, 24, 20, 24));

        JLabel title = new JLabel("HostelMate AI Operational Settings");
        title.setFont(FONT_TITLE);
        title.setForeground(COLOR_TEXT_WHITE);

        RoundedPanel card = new RoundedPanel(18, COLOR_CARD);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(24, 24, 24, 24));

        JLabel s1 = new JLabel("🤖 AI Auto-Dispatch Threshold: High / Urgent Priority");
        s1.setFont(FONT_HEADER);
        s1.setForeground(COLOR_TEXT_WHITE);

        JLabel s2 = new JLabel("🔔 Push Notifications for Warden Desk: Active");
        s2.setFont(FONT_HEADER);
        s2.setForeground(COLOR_TEXT_WHITE);

        JLabel s3 = new JLabel("🎨 Theme Mode: Modern Dark SaaS (#0F172A)");
        s3.setFont(FONT_HEADER);
        s3.setForeground(COLOR_TEXT_WHITE);

        ModernButton saveBtn = new ModernButton("Save Configuration", COLOR_PRIMARY, COLOR_TEXT_WHITE);
        saveBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Settings updated successfully!",
                "Settings Saved", JOptionPane.INFORMATION_MESSAGE));

        card.add(s1);
        card.add(Box.createVerticalStrut(16));
        card.add(s2);
        card.add(Box.createVerticalStrut(16));
        card.add(s3);
        card.add(Box.createVerticalStrut(24));
        card.add(saveBtn);

        panel.add(title, BorderLayout.NORTH);
        panel.add(card, BorderLayout.CENTER);
        return panel;
    }

    // =========================================================================
    // ACTION MODALS & DIALOGS
    // =========================================================================
    private void showComplaintDetailsModal(ComplaintItem item) {
        JDialog dialog = new JDialog(this, "Complaint Details - #" + item.id, true);
        dialog.setSize(480, 360);
        dialog.setLocationRelativeTo(this);

        JPanel p = new JPanel(new BorderLayout(14, 14));
        p.setBackground(COLOR_SIDEBAR);
        p.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Complaint #" + item.id + " (" + item.category + ")");
        title.setFont(FONT_TITLE);
        title.setForeground(COLOR_TEXT_WHITE);

        JTextArea area = new JTextArea(
                "Student: " + item.studentName + "\n" +
                        "Room: " + item.roomNumber + " (" + item.hostelBlock + ")\n" +
                        "Priority: " + item.priority + "\n" +
                        "Status: " + item.status + "\n" +
                        "Assigned Staff: " + item.assignedStaff + "\n" +
                        "Time: " + item.timeSubmitted + "\n\n" +
                        "Description:\n" + item.description);
        area.setFont(FONT_BODY);
        area.setForeground(COLOR_TEXT_WHITE);
        area.setBackground(COLOR_CARD);
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(new EmptyBorder(10, 10, 10, 10));

        ModernButton close = new ModernButton("Close", COLOR_PRIMARY, COLOR_TEXT_WHITE);
        close.addActionListener(e -> dialog.dispose());

        p.add(title, BorderLayout.NORTH);
        p.add(new JScrollPane(area), BorderLayout.CENTER);
        p.add(close, BorderLayout.SOUTH);

        dialog.setContentPane(p);
        dialog.setVisible(true);
    }

    private void showAssignStaffModal(ComplaintItem item) {
        JDialog dialog = new JDialog(this, "Assign Staff to #" + item.id, true);
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);

        JPanel p = new JPanel(new GridLayout(4, 1, 10, 10));
        p.setBackground(COLOR_SIDEBAR);
        p.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Assign Support Staff for " + item.category);
        title.setFont(FONT_HEADER);
        title.setForeground(COLOR_TEXT_WHITE);

        String[] staffOptions = staffList.stream()
                .map(st -> st.name + " (" + st.role + ")")
                .toArray(String[]::new);

        JComboBox<String> combo = createStyledComboBox(staffOptions);

        ModernButton assign = new ModernButton("Confirm Assignment", COLOR_ACCENT, COLOR_TEXT_WHITE);
        assign.addActionListener(e -> {
            item.assignedStaff = (String) combo.getSelectedItem();
            item.status = "In Progress";
            JOptionPane.showMessageDialog(dialog, "Assigned " + item.assignedStaff + " to Complaint #" + item.id);
            dialog.dispose();
            refreshComplaintCards();
        });

        p.add(title);
        p.add(combo);
        p.add(assign);

        dialog.setContentPane(p);
        dialog.setVisible(true);
    }

    private void showCreateNoticeModal() {
        JDialog dialog = new JDialog(this, "Publish New Notice", true);
        dialog.setSize(450, 380);
        dialog.setLocationRelativeTo(this);

        JPanel p = new JPanel(new GridLayout(6, 1, 8, 8));
        p.setBackground(COLOR_SIDEBAR);
        p.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Publish Official Warden Notice");
        title.setFont(FONT_HEADER);
        title.setForeground(COLOR_TEXT_WHITE);

        SearchTextField titleField = new SearchTextField("Notice Title...");
        SearchTextField targetField = new SearchTextField("Target Audience (e.g. All Students, Block B)");
        SearchTextField bodyField = new SearchTextField("Notice Body Content...");

        ModernButton publishBtn = new ModernButton("Publish Notice", COLOR_PRIMARY, COLOR_TEXT_WHITE);
        publishBtn.addActionListener(e -> {
            NoticeItem n = new NoticeItem(
                    titleField.getText(),
                    bodyField.getText(),
                    targetField.getText(),
                    "Aug 8, 2026",
                    true);
            noticeList.add(0, n);
            JOptionPane.showMessageDialog(dialog, "Notice published successfully!");
            dialog.dispose();
            mainCardLayout.show(mainContentCards, "Dashboard");
            mainCardLayout.show(mainContentCards, "Notice Board");
        });

        p.add(title);
        p.add(titleField);
        p.add(targetField);
        p.add(bodyField);
        p.add(publishBtn);

        dialog.setContentPane(p);
        dialog.setVisible(true);
    }

    private void showUpdateMessModal() {
        JDialog dialog = new JDialog(this, "Update Mess Menu", true);
        dialog.setSize(420, 360);
        dialog.setLocationRelativeTo(this);

        JPanel p = new JPanel(new GridLayout(5, 1, 8, 8));
        p.setBackground(COLOR_SIDEBAR);
        p.setBorder(new EmptyBorder(20, 20, 20, 20));

        SearchTextField bField = new SearchTextField(messMenuData.breakfast);
        SearchTextField lField = new SearchTextField(messMenuData.lunch);
        SearchTextField sField = new SearchTextField(messMenuData.snacks);
        SearchTextField dField = new SearchTextField(messMenuData.dinner);

        ModernButton save = new ModernButton("Save Updated Menu", COLOR_SUCCESS, COLOR_TEXT_WHITE);
        save.addActionListener(e -> {
            messMenuData.breakfast = bField.getText();
            messMenuData.lunch = lField.getText();
            messMenuData.snacks = sField.getText();
            messMenuData.dinner = dField.getText();
            JOptionPane.showMessageDialog(dialog, "Mess Menu Updated!");
            dialog.dispose();
            mainCardLayout.show(mainContentCards, "Dashboard");
            mainCardLayout.show(mainContentCards, "Mess Menu");
        });

        p.add(bField);
        p.add(lField);
        p.add(sField);
        p.add(dField);
        p.add(save);

        dialog.setContentPane(p);
        dialog.setVisible(true);
    }

    private void showImageAttachmentDialog(ComplaintItem item) {
        JDialog dialog = new JDialog(this, "Attachment Image - #" + item.id, true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JPanel p = new RoundedPanel(20, COLOR_CARD);
        p.setLayout(new BorderLayout(10, 10));
        p.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("📷 Attachment Preview: " + item.attachmentName);
        title.setFont(FONT_HEADER);
        title.setForeground(COLOR_TEXT_WHITE);

        // Simulated Image Graphic Box
        JPanel imgBox = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COLOR_SIDEBAR);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(COLOR_ACCENT);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
                g2.drawString("📷 PHOTO EVIDENCE ATTACHMENT", 50, getHeight() / 2);
                g2.setColor(COLOR_TEXT_MUTED);
                g2.drawString("Uploaded by " + item.studentName + " • " + item.roomNumber, 50, getHeight() / 2 + 25);
                g2.dispose();
            }
        };

        ModernButton close = new ModernButton("Close Preview", COLOR_PRIMARY, COLOR_TEXT_WHITE);
        close.addActionListener(e -> dialog.dispose());

        p.add(title, BorderLayout.NORTH);
        p.add(imgBox, BorderLayout.CENTER);
        p.add(close, BorderLayout.SOUTH);

        dialog.setContentPane(p);
        dialog.setVisible(true);
    }

    private void triggerAIAutoAssign() {
        for (ComplaintItem item : complaintList) {
            if ("Pending".equals(item.status)) {
                if ("Plumbing".equalsIgnoreCase(item.category)) {
                    item.assignedStaff = "Suresh Sharma (Plumber)";
                    item.status = "In Progress";
                } else if ("Electrical".equalsIgnoreCase(item.category)) {
                    item.assignedStaff = "Ramesh Kumar (Electrician)";
                    item.status = "In Progress";
                } else if ("Internet".equalsIgnoreCase(item.category)) {
                    item.assignedStaff = "Amit Verma (IT Support)";
                    item.status = "In Progress";
                }
            }
        }
        JOptionPane.showMessageDialog(this,
                "🤖 HostelMate AI has auto-assigned support staff to all pending urgent complaints!",
                "AI Auto-Dispatch Complete", JOptionPane.INFORMATION_MESSAGE);
        refreshComplaintCards();
    }

    private void refreshComplaintCards() {
        if (complaintCardsContainer != null) {
            complaintCardsContainer.removeAll();
            JPanel fresh = createComplaintCards();
            complaintCardsContainer.add(fresh);
            complaintCardsContainer.revalidate();
            complaintCardsContainer.repaint();
        }
    }

    // =========================================================================
    // HELPER DATA METHODS & FILTERS
    // =========================================================================
    private List<ComplaintItem> getFilteredComplaints() {
        List<ComplaintItem> list = new ArrayList<>();
        for (ComplaintItem item : complaintList) {
            boolean matchSearch = currentSearchQuery.isEmpty() ||
                    item.studentName.toLowerCase().contains(currentSearchQuery.toLowerCase()) ||
                    item.roomNumber.toLowerCase().contains(currentSearchQuery.toLowerCase()) ||
                    item.description.toLowerCase().contains(currentSearchQuery.toLowerCase()) ||
                    item.category.toLowerCase().contains(currentSearchQuery.toLowerCase());

            boolean matchPriority = "All".equalsIgnoreCase(currentPriorityFilter)
                    || item.priority.equalsIgnoreCase(currentPriorityFilter);
            boolean matchCategory = "All".equalsIgnoreCase(currentCategoryFilter)
                    || item.category.equalsIgnoreCase(currentCategoryFilter);
            boolean matchStatus = "All".equalsIgnoreCase(currentStatusFilter)
                    || item.status.equalsIgnoreCase(currentStatusFilter);

            if (matchSearch && matchPriority && matchCategory && matchStatus) {
                list.add(item);
            }
        }
        return list;
    }

    private int getPendingCount() {
        return (int) complaintList.stream().filter(c -> "Pending".equalsIgnoreCase(c.status)).count();
    }

    private int getUrgentCount() {
        return (int) complaintList.stream().filter(c -> "Urgent".equalsIgnoreCase(c.priority)).count();
    }

    private int getResolvedTodayCount() {
        return (int) complaintList.stream().filter(c -> "Resolved".equalsIgnoreCase(c.status)).count();
    }

    private void startClockTimer() {
        Timer timer = new Timer(1000, e -> {
            LocalDateTime now = LocalDateTime.now();
            timeLabel.setText(now.format(DateTimeFormatter.ofPattern("hh:mm:ss a")));
            dateLabel.setText(now.format(DateTimeFormatter.ofPattern("EEEE, MMM d")));
        });
        timer.start();
    }

    private void seedData() {
        complaintList.add(new ComplaintItem("1089", "Rahul Verma", "B-304", "Block B", "Plumbing", "Urgent", "Pending",
                "Pipe_Leak_B304.jpg",
                "Severe water leakage from bathroom ceiling pipe onto floor near electrical switch.", "10 mins ago",
                "Unassigned"));
        complaintList.add(new ComplaintItem("1088", "Ananya Iyer", "A-112", "Block A", "Electrical", "High",
                "In Progress", "ShortCircuit_A112.jpg",
                "MCB keeps tripping whenever study lamp and room cooler are turned on simultaneously.", "45 mins ago",
                "Ramesh Kumar (Electrician)"));
        complaintList.add(new ComplaintItem("1087", "Vikram Singh", "C-405", "Block C", "Internet", "Medium", "Pending",
                "Router_NoSignal.jpg",
                "Wi-Fi access point on 4th floor Block C is dropping packets continuously. Latency over 800ms.",
                "2 hours ago", "Unassigned"));
        complaintList.add(new ComplaintItem("1086", "Priya Nair", "A-208", "Block A", "Cleanliness", "Low", "Resolved",
                "Dustbin_Clear.jpg", "Corridor dustbin overflow near room 208 needs prompt cleaning.", "4 hours ago",
                "Suresh Sanitation"));
        complaintList.add(new ComplaintItem("1085", "Karan Patel", "B-102", "Block B", "Furniture", "Medium",
                "In Progress", "Broken_Table.jpg", "Study table leg broke loose; drawer hinge jammed.", "5 hours ago",
                "Mohan Carpenter"));

        studentList.add(new StudentItem("Rahul Verma", "CS24-102", "B-304", "Block B", "+91 98765 11111"));
        studentList.add(new StudentItem("Ananya Iyer", "EE23-045", "A-112", "Block A", "+91 98765 22222"));
        studentList.add(new StudentItem("Vikram Singh", "ME25-088", "C-405", "Block C", "+91 98765 33333"));
        studentList.add(new StudentItem("Priya Nair", "CS23-019", "A-208", "Block A", "+91 98765 44444"));
        studentList.add(new StudentItem("Karan Patel", "EC24-110", "B-102", "Block B", "+91 98765 55555"));
        studentList.add(new StudentItem("Devansh Roy", "CE24-004", "B-210", "Block B", "+91 98765 66666"));

        staffList.add(new StaffItem("Ramesh Kumar", "Electrician", "On Duty", 2, "+91 98111 22233"));
        staffList.add(new StaffItem("Suresh Sharma", "Plumber", "On Duty", 3, "+91 98111 33344"));
        staffList.add(new StaffItem("Mohan Lal", "Carpenter", "On Duty", 1, "+91 98111 44455"));
        staffList.add(new StaffItem("Amit Verma", "IT Support", "On Duty", 1, "+91 98111 55566"));
        staffList.add(new StaffItem("Sunita Devi", "Sanitation Lead", "On Duty", 0, "+91 98111 66677"));
        staffList.add(new StaffItem("Rajesh Guard", "Security Head", "On Duty", 0, "+91 98111 77788"));

        noticeList.add(new NoticeItem("Annual Hostel Inspection Drive",
                "All rooms in Block A, B & C will undergo routine maintenance checks this Saturday from 10 AM.",
                "All Occupants", "Aug 10, 2026", true));
        noticeList.add(new NoticeItem("Revised Night Curfew & Security Rules",
                "Hostel main gates close at 10:00 PM strictly. Late entry requires prior Warden portal pass.",
                "All Students", "Aug 05, 2026", true));
        noticeList.add(new NoticeItem("Special Sunday Feast & Poll",
                "Vote for your favorite dish on the HostelMate AI portal before Friday noon.", "Block A, B, C",
                "Aug 07, 2026", false));

        messMenuData = new MessMenuData(
                "Masala Dosa, Sambhar, Coconut Chutney, Boiled Eggs, Tea/Coffee",
                "Paneer Butter Masala, Dal Tadka, Jeera Rice, Butter Roti, Curd, Salad",
                "Veg Cutlet, Samosa, Green Chutney, Hot Masala Tea",
                "Malai Kofta, Kashmiri Pulao, Chapati, Rasgulla, Hot Milk");

        feedbackList.add(new FeedbackItem("Devansh Roy", "B-210", "5 Stars",
                "Food quality on Friday dinner was superb! Clean mess hall.", "Positive"));
        feedbackList.add(new FeedbackItem("Rahul Verma", "B-304", "5 Stars",
                "Plumbing issue in B-304 attended within 20 mins! Impressive AI dispatch.", "Positive"));
        feedbackList.add(new FeedbackItem("Vikram Singh", "C-405", "3 Stars",
                "Wi-Fi speed on 4th floor Block C drops during evening peak hours.", "Needs Improvement"));
    }

    // =========================================================================
    // UI UTILITY BUILDERS (Stat Card, Pill Label, ComboBox)
    // =========================================================================
    private JPanel createStatCard(String title, String value, String iconStr, Color accentColor, String trendStr) {
        RoundedPanel card = new RoundedPanel(16, COLOR_CARD);
        card.setLayout(new BorderLayout(8, 8));
        card.setBorder(new EmptyBorder(14, 16, 14, 16));

        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(FONT_SUBHEADER);
        titleLabel.setForeground(COLOR_TEXT_MUTED);

        JLabel iconLabel = new JLabel(iconStr);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));

        topRow.add(titleLabel, BorderLayout.WEST);
        topRow.add(iconLabel, BorderLayout.EAST);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Poppins", Font.BOLD, 22));
        valueLabel.setForeground(COLOR_TEXT_WHITE);

        JLabel trendLabel = new JLabel(trendStr);
        trendLabel.setFont(FONT_SMALL);
        trendLabel.setForeground(accentColor);

        card.add(topRow, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        card.add(trendLabel, BorderLayout.SOUTH);

        return card;
    }

    private JLabel createPillLabel(String text, Color bg, Color fg) {
        JLabel pill = new JLabel(" " + text + " ") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        pill.setFont(FONT_SMALL);
        pill.setForeground(fg);
        pill.setOpaque(false);
        return pill;
    }

    private <T> JComboBox<T> createStyledComboBox(T[] items) {
        JComboBox<T> combo = new JComboBox<>(items);
        combo.setFont(FONT_BODY);
        combo.setForeground(COLOR_TEXT_WHITE);
        combo.setBackground(COLOR_CARD);
        combo.setFocusable(false);
        combo.setPreferredSize(new Dimension(160, 36));
        return combo;
    }

    private Color getPriorityColor(String priority) {
        if ("Urgent".equalsIgnoreCase(priority))
            return COLOR_DANGER;
        if ("High".equalsIgnoreCase(priority))
            return COLOR_WARNING;
        if ("Medium".equalsIgnoreCase(priority))
            return COLOR_PRIMARY;
        return COLOR_SUCCESS;
    }

    private Color getStatusColor(String status) {
        if ("Resolved".equalsIgnoreCase(status))
            return COLOR_SUCCESS;
        if ("In Progress".equalsIgnoreCase(status))
            return COLOR_ACCENT;
        return COLOR_WARNING;
    }

    private Color getCategoryColor(String cat) {
        if ("Plumbing".equalsIgnoreCase(cat))
            return new Color(59, 130, 246);
        if ("Electrical".equalsIgnoreCase(cat))
            return new Color(234, 179, 8);
        if ("Internet".equalsIgnoreCase(cat))
            return COLOR_ACCENT;
        return COLOR_PRIMARY;
    }

    private String getCategoryIcon(String cat) {
        if ("Plumbing".equalsIgnoreCase(cat))
            return "🚰";
        if ("Electrical".equalsIgnoreCase(cat))
            return "⚡";
        if ("Internet".equalsIgnoreCase(cat))
            return "📶";
        if ("Cleanliness".equalsIgnoreCase(cat))
            return "🧹";
        return "🛠️";
    }

    // =========================================================================
    // INNER DOMAIN DATA MODELS
    // =========================================================================
    private static class ComplaintItem {
        String id, studentName, roomNumber, hostelBlock, category, priority, status, attachmentName, description,
                timeSubmitted, assignedStaff;

        ComplaintItem(String id, String studentName, String roomNumber, String hostelBlock, String category,
                String priority, String status, String attachmentName, String description, String timeSubmitted,
                String assignedStaff) {
            this.id = id;
            this.studentName = studentName;
            this.roomNumber = roomNumber;
            this.hostelBlock = hostelBlock;
            this.category = category;
            this.priority = priority;
            this.status = status;
            this.attachmentName = attachmentName;
            this.description = description;
            this.timeSubmitted = timeSubmitted;
            this.assignedStaff = assignedStaff;
        }
    }

    private static class StudentItem {
        String name, roll, room, block, phone;

        StudentItem(String name, String roll, String room, String block, String phone) {
            this.name = name;
            this.roll = roll;
            this.room = room;
            this.block = block;
            this.phone = phone;
        }
    }

    private static class StaffItem {
        String name, role, status;
        int activeTasks;
        String phone;

        StaffItem(String name, String role, String status, int activeTasks, String phone) {
            this.name = name;
            this.role = role;
            this.status = status;
            this.activeTasks = activeTasks;
            this.phone = phone;
        }
    }

    private static class NoticeItem {
        String title, body, target, date;
        boolean isPinned;

        NoticeItem(String title, String body, String target, String date, boolean isPinned) {
            this.title = title;
            this.body = body;
            this.target = target;
            this.date = date;
            this.isPinned = isPinned;
        }
    }

    private static class MessMenuData {
        String breakfast, lunch, snacks, dinner;

        MessMenuData(String breakfast, String lunch, String snacks, String dinner) {
            this.breakfast = breakfast;
            this.lunch = lunch;
            this.snacks = snacks;
            this.dinner = dinner;
        }
    }

    private static class FeedbackItem {
        String studentName, room, rating, comment, sentiment;

        FeedbackItem(String studentName, String room, String rating, String comment, String sentiment) {
            this.studentName = studentName;
            this.room = room;
            this.rating = rating;
            this.comment = comment;
            this.sentiment = sentiment;
        }
    }

    // =========================================================================
    // CUSTOM SWING GRAPHICS & COMPONENTS
    // =========================================================================
    public static class RoundedPanel extends JPanel {
        private final int radius;
        private Color customBg;

        public RoundedPanel(int radius, Color bg) {
            this.radius = radius;
            this.customBg = bg;
            setOpaque(false);
        }

        @Override
        public void setBackground(Color bg) {
            this.customBg = bg;
            super.setBackground(bg);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(customBg != null ? customBg : getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static class GradientPanel extends JPanel {
        private final Color colorStart;
        private final Color colorEnd;
        private final int radius;

        public GradientPanel(Color start, Color end, int radius) {
            this.colorStart = start;
            this.colorEnd = end;
            this.radius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            GradientPaint gp = new GradientPaint(0, 0, colorStart, getWidth(), getHeight(), colorEnd);
            g2.setPaint(gp);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static class ModernButton extends JButton {
        private final Color normalBg;
        private final Color hoverBg;

        public ModernButton(String text, Color bg, Color fg) {
            super(text);
            this.normalBg = bg;
            this.hoverBg = bg.brighter();
            setFont(FONT_SUBHEADER);
            setForeground(fg);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getModel().isRollover() ? hoverBg : normalBg);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

            g2.setColor(getForeground());
            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(getText())) / 2;
            int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
            g2.drawString(getText(), x, y);
            g2.dispose();
        }
    }

    public static class SidebarButton extends JButton {
        private boolean selected = false;

        public SidebarButton(String text, boolean selected) {
            super(text);
            this.selected = selected;
            setFont(FONT_SUBHEADER);
            setForeground(selected ? COLOR_TEXT_WHITE : COLOR_TEXT_MUTED);
            setHorizontalAlignment(SwingConstants.LEFT);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setMaximumSize(new Dimension(224, 42));
            setPreferredSize(new Dimension(224, 42));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    repaint();
                }
            });
        }

        public void setSelected(boolean sel) {
            this.selected = sel;
            setForeground(sel ? COLOR_TEXT_WHITE : COLOR_TEXT_MUTED);
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (selected) {
                g2.setColor(COLOR_PRIMARY);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
            } else if (getModel().isRollover()) {
                g2.setColor(COLOR_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
            }

            g2.setColor(getForeground());
            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(getText(), 16, (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
            g2.dispose();
        }
    }

    public static class SearchTextField extends JTextField {
        private final String placeholder;

        public SearchTextField(String placeholder) {
            this.placeholder = placeholder;
            setFont(FONT_BODY);
            setForeground(COLOR_TEXT_WHITE);
            setCaretColor(COLOR_PRIMARY);
            setOpaque(false);
            setBorder(new EmptyBorder(8, 16, 8, 16));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(COLOR_CARD);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

            if (getText().isEmpty()) {
                g2.setColor(COLOR_TEXT_MUTED);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(placeholder, 16, (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static class CustomScrollBarUI extends BasicScrollBarUI {
        @Override
        protected void configureScrollBarColors() {
            this.thumbColor = new Color(99, 102, 241, 140);
            this.trackColor = COLOR_BG;
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createZeroButton();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createZeroButton();
        }

        private JButton createZeroButton() {
            JButton b = new JButton();
            b.setPreferredSize(new Dimension(0, 0));
            return b;
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(thumbColor);
            g2.fillRoundRect(thumbBounds.x + 2, thumbBounds.y + 2, thumbBounds.width - 4, thumbBounds.height - 4, 8, 8);
            g2.dispose();
        }
    }

    // Custom Painted Line Chart for Analytics
    public static class PaintedLineChart extends JPanel {
        public PaintedLineChart() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();
            int pad = 35;

            // Draw Grid Lines
            g2.setColor(COLOR_BORDER);
            for (int i = pad; i < h - pad; i += (h - 2 * pad) / 4) {
                g2.drawLine(pad, i, w - pad, i);
            }

            int[] data = { 12, 18, 14, 25, 20, 32, 24 };
            String[] days = { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun" };

            int maxVal = 40;
            int n = data.length;
            int stepX = (w - 2 * pad) / (n - 1);

            Path2D path = new Path2D.Double();
            Point[] pts = new Point[n];

            for (int i = 0; i < n; i++) {
                int x = pad + i * stepX;
                int y = (h - pad) - (data[i] * (h - 2 * pad) / maxVal);
                pts[i] = new Point(x, y);
                if (i == 0)
                    path.moveTo(x, y);
                else
                    path.lineTo(x, y);
            }

            // Fill Area Under Line Graph
            Path2D area = new Path2D.Double(path);
            area.lineTo(pts[n - 1].x, h - pad);
            area.lineTo(pts[0].x, h - pad);
            area.closePath();

            GradientPaint fillGradient = new GradientPaint(0, 0, new Color(99, 102, 241, 90), 0, h,
                    new Color(99, 102, 241, 5));
            g2.setPaint(fillGradient);
            g2.fill(area);

            // Draw Smooth Curve Line
            g2.setColor(COLOR_PRIMARY);
            g2.setStroke(new java.awt.BasicStroke(3f));
            g2.draw(path);

            // Draw Data Points & Day Labels
            g2.setFont(FONT_SMALL);
            for (int i = 0; i < n; i++) {
                g2.setColor(COLOR_ACCENT);
                g2.fillOval(pts[i].x - 5, pts[i].y - 5, 10, 10);
                g2.setColor(COLOR_TEXT_WHITE);
                g2.drawOval(pts[i].x - 5, pts[i].y - 5, 10, 10);

                g2.setColor(COLOR_TEXT_MUTED);
                g2.drawString(days[i], pts[i].x - 12, h - 10);
            }
            g2.dispose();
        }
    }

    // Custom Painted Bar Chart for Category Analytics
    public static class PaintedBarChart extends JPanel {
        public PaintedBarChart() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            String[] categories = { "Plumbing", "Electrical", "Internet", "Cleanliness", "Furniture" };
            int[] values = { 38, 26, 18, 12, 6 }; // Percentages
            Color[] colors = { new Color(59, 130, 246), COLOR_WARNING, COLOR_ACCENT, COLOR_SUCCESS, COLOR_PRIMARY };

            int y = 25;
            int barHeight = 22;

            for (int i = 0; i < categories.length; i++) {
                g2.setFont(FONT_SUBHEADER);
                g2.setColor(COLOR_TEXT_WHITE);
                g2.drawString(categories[i], 20, y + 16);

                // Track Background
                g2.setColor(COLOR_SIDEBAR);
                g2.fillRoundRect(120, y, getWidth() - 200, barHeight, 10, 10);

                // Filled Bar
                int fillWidth = (values[i] * (getWidth() - 200)) / 100;
                g2.setColor(colors[i]);
                g2.fillRoundRect(120, y, fillWidth, barHeight, 10, 10);

                // Percentage Tag
                g2.setColor(COLOR_TEXT_SUBTLE);
                g2.setFont(FONT_SMALL);
                g2.drawString(values[i] + "%", getWidth() - 65, y + 16);

                y += 38;
            }
            g2.dispose();
        }
    }

    // OverlayLayout manager for main pane & slide-out drawer
    private static class OverlayLayout implements java.awt.LayoutManager {
        @Override
        public void addLayoutComponent(String name, Component comp) {
        }

        @Override
        public void removeLayoutComponent(Component comp) {
        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            return parent.getSize();
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            return parent.getSize();
        }

        @Override
        public void layoutContainer(Container parent) {
            for (int i = 0; i < parent.getComponentCount(); i++) {
                parent.getComponent(i).setBounds(0, 0, parent.getWidth(), parent.getHeight());
            }
        }
    }

    // =========================================================================
    // MAIN ENTRY POINT
    // =========================================================================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }
            new WardenDashboard().setVisible(true);
        });
    }
}
