
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class StaffDashboard extends JFrame {

    private final String userEmail;
    private final List<String[]> noticeList = new ArrayList<>();
    private final List<String[]> complaintList = new ArrayList<>();

    public StaffDashboard(String email) {
        this.userEmail = email == null ? "staff" : email;
        seedSampleData();
        initUI();
    }

    private void seedSampleData() {
        noticeList.add(new String[]{"Water maintenance", "08 Aug 2026", "Water supply will be paused in KP-26 from 10 AM to 12 PM."});
        noticeList.add(new String[]{"Mess menu", "07 Aug 2026", "Friday dinner will include paneer bowl and jeera rice."});
        noticeList.add(new String[]{"Hostel meeting", "09 Aug 2026", "Staff meeting at 4 PM in the warden office."});

        complaintList.add(new String[]{"HP-1024", "Pending", "Electrical issue in A-16."});
        complaintList.add(new String[]{"HP-1019", "In Progress", "Plumbing issue in B-214."});
        complaintList.add(new String[]{"HP-1008", "Resolved", "Wi-Fi outage in C-104."});
    }

    private void initUI() {
        setTitle("Hostel Portal - Staff Dashboard");
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(18, 18));
        root.setBorder(new EmptyBorder(18, 18, 18, 18));
        root.setBackground(new Color(245, 247, 250));

        root.add(createSidebar(), BorderLayout.WEST);
        root.add(createMainContent(), BorderLayout.CENTER);

        setContentPane(root);
        setVisible(true);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(260, 0));
        sidebar.setBackground(new Color(34, 39, 71));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(20, 18, 20, 18));

        JLabel title = new JLabel("Hostel Staff");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        sidebar.add(title);

        JLabel subtitle = new JLabel("Warden / Staff Portal");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(190, 196, 215));
        subtitle.setBorder(new EmptyBorder(6, 0, 18, 0));
        sidebar.add(subtitle);

        sidebar.add(createSidebarButton("Dashboard", true));
        sidebar.add(createSidebarButton("Complaints", false));
        sidebar.add(createSidebarButton("Assignments", false));
        sidebar.add(createSidebarButton("Notices", false));
        sidebar.add(createSidebarButton("Student List", false));
        sidebar.add(createSidebarButton("Profile", false));

        sidebar.add(Box.createVerticalGlue());

        JButton logout = createSidebarButton("Logout", false);
        logout.addActionListener(e -> dispose());
        sidebar.add(logout);

        return sidebar;
    }

    private JButton createSidebarButton(String text, boolean active) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        button.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        button.setForeground(active ? Color.WHITE : new Color(210, 214, 230));
        button.setBackground(active ? new Color(85, 93, 165) : new Color(42, 48, 83));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        return button;
    }

    private JPanel createMainContent() {
        JPanel main = new JPanel(new BorderLayout(18, 18));
        main.setOpaque(false);

        main.add(createHeaderPanel(), BorderLayout.NORTH);
        main.add(createSummaryPanel(), BorderLayout.CENTER);
        main.add(createOverviewPanel(), BorderLayout.SOUTH);

        return main;
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout(8, 8));
        header.setOpaque(false);

        JLabel title = new JLabel("Welcome, " + getDisplayName());
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(30, 35, 53));

        JLabel subtitle = new JLabel("Review complaints, assign tasks, and manage hostel notices.");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        subtitle.setForeground(new Color(100, 108, 132));

        JLabel emailLabel = new JLabel("Logged in as: " + userEmail);
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        emailLabel.setForeground(new Color(120, 129, 152));

        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(8));
        titlePanel.add(subtitle);
        titlePanel.add(Box.createVerticalStrut(10));
        titlePanel.add(emailLabel);

        header.add(titlePanel, BorderLayout.WEST);

        return header;
    }

    private String getDisplayName() {
        if (userEmail.toLowerCase().contains("wardan")) {
            return "Warden";
        }
        return "Hostel Staff";
    }

    private JPanel createSummaryPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 16, 16));
        panel.setOpaque(false);
        panel.add(createStatCard("Open Complaints", "12", new Color(105, 178, 255)));
        panel.add(createStatCard("Assigned Tasks", "7", new Color(127, 194, 125)));
        panel.add(createStatCard("Pending Notices", "4", new Color(251, 161, 69)));
        panel.add(createStatCard("Active Students", "234", new Color(142, 118, 255)));
        return panel;
    }

    private JPanel createStatCard(String label, String value, Color accent) {
        JPanel card = new JPanel();
        card.setOpaque(true);
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(222, 226, 236)), new EmptyBorder(18, 18, 18, 18)));
        card.setLayout(new BorderLayout(0, 8));

        JLabel title = new JLabel(label);
        title.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        title.setForeground(new Color(99, 108, 132));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        valueLabel.setForeground(new Color(26, 33, 63));

        JPanel accentBar = new JPanel();
        accentBar.setPreferredSize(new Dimension(4, 40));
        accentBar.setBackground(accent);

        card.add(accentBar, BorderLayout.WEST);
        card.add(title, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    private JPanel createOverviewPanel() {
        JPanel overview = new JPanel(new GridLayout(1, 2, 16, 16));
        overview.setOpaque(false);
        overview.add(createNoticePanel());
        overview.add(createComplaintPanel());
        return overview;
    }

    private JPanel createNoticePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setOpaque(true);
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(222, 226, 236)), new EmptyBorder(18, 18, 18, 18)));

        JLabel title = new JLabel("Latest Notices");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(new Color(26, 33, 63));

        JPanel list = new JPanel();
        list.setOpaque(false);
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));

        for (String[] notice : noticeList) {
            list.add(createNoticeItem(notice));
            list.add(Box.createVerticalStrut(10));
        }

        JScrollPane scroll = new JScrollPane(list);
        scroll.setBorder(null);
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);

        panel.add(title, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createNoticeItem(String[] notice) {
        JPanel item = new JPanel(new BorderLayout(10, 4));
        item.setOpaque(true);
        item.setBackground(new Color(245, 247, 250));
        item.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));

        JLabel heading = new JLabel(notice[0]);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 15));
        heading.setForeground(new Color(35, 43, 68));

        JLabel date = new JLabel(notice[1]);
        date.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        date.setForeground(new Color(111, 118, 141));

        JTextArea body = new JTextArea(notice[2]);
        body.setLineWrap(true);
        body.setWrapStyleWord(true);
        body.setOpaque(false);
        body.setEditable(false);
        body.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        body.setForeground(new Color(88, 96, 119));

        item.add(heading, BorderLayout.NORTH);
        item.add(body, BorderLayout.CENTER);
        item.add(date, BorderLayout.SOUTH);
        return item;
    }

    private JPanel createComplaintPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setOpaque(true);
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(222, 226, 236)), new EmptyBorder(18, 18, 18, 18)));

        JLabel title = new JLabel("Recent Complaints");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(new Color(26, 33, 63));

        JPanel list = new JPanel();
        list.setOpaque(false);
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));

        for (String[] complaint : complaintList) {
            list.add(createComplaintItem(complaint));
            list.add(Box.createVerticalStrut(10));
        }

        JScrollPane scroll = new JScrollPane(list);
        scroll.setBorder(null);
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);

        panel.add(title, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createComplaintItem(String[] complaint) {
        JPanel item = new JPanel(new BorderLayout(10, 8));
        item.setOpaque(true);
        item.setBackground(new Color(245, 247, 250));
        item.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));

        JLabel id = new JLabel(complaint[0]);
        id.setFont(new Font("Segoe UI", Font.BOLD, 15));
        id.setForeground(new Color(35, 43, 68));

        JLabel status = new JLabel(complaint[1]);
        status.setFont(new Font("Segoe UI", Font.BOLD, 12));
        status.setForeground(new Color(94, 113, 188));
        status.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        status.setOpaque(true);
        status.setBackground(new Color(226, 231, 250));

        JLabel description = new JLabel(complaint[2]);
        description.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        description.setForeground(new Color(88, 96, 119));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(id, BorderLayout.WEST);
        top.add(status, BorderLayout.EAST);

        item.add(top, BorderLayout.NORTH);
        item.add(description, BorderLayout.CENTER);
        return item;
    }
}
