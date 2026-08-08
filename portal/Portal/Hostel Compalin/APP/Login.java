
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.io.File;

public class Login extends JFrame implements ActionListener {

    JTextField userField;
    JPasswordField passField;
    JButton loginButton;
    JCheckBox remember;
    JRadioButton student, staff;
    JLabel userIcon;

    public Login() {
        setTitle("Hostel Portal");
        setSize(1536, 864);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // ================= BACKGROUND IMAGE =================
        ImageIcon i1 = loadBackgroundImage();
        Image img = i1.getImage().getScaledInstance(1299, 770, Image.SCALE_SMOOTH);

        JLabel background = new JLabel(new ImageIcon(img));
        background.setBounds(0, 0, 1299, 770);
        background.setLayout(null);

        add(background);

        // ================= LEFT SIDE =================
        // Logo
        JLabel logo = new JLabel("🏨");
        logo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 50));
        logo.setBounds(40, 30, 60, 60);
        background.add(logo);

        JLabel title = new JLabel(
                "<html><span style='color:#ffffff'>Hostel </span><span style='color:#f59218'>Portal</span></html>");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 25));
        title.setBounds(110, 25, 260, 40);
        background.add(title);

        JLabel subTitle = new JLabel("Smart Hostel Management");
        subTitle.setForeground(Color.BLACK);
        subTitle.setFont(new Font("Arial", Font.PLAIN, 22));
        subTitle.setBounds(110, 65, 320, 25);
        background.add(subTitle);

        // Heading
        JLabel head1 = new JLabel("Smarter Hostel.");
        head1.setForeground(Color.WHITE);
        head1.setFont(new Font("Arial", Font.BOLD, 26));
        head1.setBounds(40, 80, 250, 60);
        background.add(head1);

        JLabel head2 = new JLabel("Happier Life.");
        head2.setForeground(new Color(245, 146, 24));
        head2.setFont(new Font("Arial", Font.BOLD, 22));
        head2.setBounds(40, 105, 500, 60);
        background.add(head2);

        // Description
        JLabel des = new JLabel("<html>AI-Powered solutions for modern<br>hostel living and management.</html>");
        des.setForeground(new Color(80, 150, 255));
        des.setFont(new Font("Arial", Font.PLAIN, 22));
        des.setBounds(40, 150, 400, 70);
        background.add(des);

        // ---------------- Feature 1 ----------------
        JPanel f1 = new JPanel(null);
        f1.setOpaque(true);
        f1.setBackground(new Color(12, 20, 40, 220));
        f1.setBorder(new LineBorder(new Color(86, 103, 156, 150), 1, true));
        f1.setBounds(40, 250, 380, 100);

        JLabel i1L = new JLabel("💬");
        i1L.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        i1L.setForeground(new Color(245, 146, 24));
        i1L.setBounds(15, 20, 50, 50);
        f1.add(i1L);

        JLabel t1 = new JLabel("Smart Complaints");
        t1.setForeground(Color.WHITE);
        t1.setFont(new Font("Arial", Font.BOLD, 22));
        t1.setBounds(90, 15, 250, 30);
        f1.add(t1);

        JLabel d1 = new JLabel("<html>AI-powered complaint categorization<br>and quick resolution</html>");
        d1.setForeground(new Color(205, 210, 225));
        d1.setFont(new Font("Arial", Font.PLAIN, 12));
        d1.setBounds(90, 55, 260, 40);
        f1.add(d1);

        addFeatureCardHover(f1, i1L, t1, d1, new Color(245, 146, 24));
        background.add(f1);

        // ---------------- Feature 2 ----------------
        JPanel f2 = new JPanel(null);
        f2.setOpaque(true);
        f2.setBackground(new Color(12, 20, 40, 220));
        f2.setBorder(new LineBorder(new Color(86, 103, 156, 150), 1, true));
        f2.setBounds(45, 380, 370, 90);

        JLabel i2L = new JLabel("🔔");
        i2L.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        i2L.setForeground(new Color(245, 146, 24));
        i2L.setBounds(15, 20, 50, 50);
        f2.add(i2L);

        JLabel t2 = new JLabel("Real-time Updates");
        t2.setForeground(Color.WHITE);
        t2.setFont(new Font("Arial", Font.BOLD, 22));
        t2.setBounds(90, 15, 250, 30);
        f2.add(t2);

        JLabel d2 = new JLabel("<html>Get instant updates on your complaints<br>and announcements</html>");
        d2.setForeground(new Color(205, 210, 225));
        d2.setFont(new Font("Arial", Font.PLAIN, 12));
        d2.setBounds(90, 35, 260, 40);
        f2.add(d2);

        addFeatureCardHover(f2, i2L, t2, d2, new Color(245, 146, 24));
        background.add(f2);

        // ---------------- Feature 3 ----------------
        JPanel f3 = new JPanel(null);
        f3.setOpaque(true);
        f3.setBackground(new Color(12, 20, 40, 220));
        f3.setBorder(new LineBorder(new Color(86, 103, 156, 150), 1, true));
        f3.setBounds(40, 490, 380, 100);

        JLabel i3L = new JLabel("📊");
        i3L.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        i3L.setForeground(new Color(102, 204, 255));
        i3L.setBounds(15, 20, 50, 50);
        f3.add(i3L);

        JLabel t3 = new JLabel("Hostel Analytics");
        t3.setForeground(Color.WHITE);
        t3.setFont(new Font("Arial", Font.BOLD, 22));
        t3.setBounds(90, 15, 250, 30);
        f3.add(t3);

        JLabel d3 = new JLabel("<html>Data-driven insights for better<br>hostel management</html>");
        d3.setForeground(new Color(205, 210, 225));
        d3.setFont(new Font("Arial", Font.PLAIN, 17));
        d3.setBounds(90, 45, 260, 40);
        f3.add(d3);

        addFeatureCardHover(f3, i3L, t3, d3, new Color(102, 204, 255));
        background.add(f3);

        // ---------------- Feature 4 ----------------
        JPanel f4 = new JPanel(null);
        f4.setOpaque(true);
        f4.setBackground(new Color(12, 20, 40, 220));
        f4.setBorder(new LineBorder(new Color(86, 103, 156, 150), 1, true));
        f4.setBounds(40, 610, 380, 100);

        JLabel i4L = new JLabel("🤖");
        i4L.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        i4L.setForeground(new Color(245, 146, 24));
        i4L.setBounds(15, 20, 50, 50);
        f4.add(i4L);

        JLabel t4 = new JLabel("24/7 Assistant");
        t4.setForeground(Color.WHITE);
        t4.setFont(new Font("Arial", Font.BOLD, 22));
        t4.setBounds(90, 15, 250, 30);
        f4.add(t4);

        JLabel d4 = new JLabel("<html>AI assistant to help you anytime,<br>anywhere</html>");
        d4.setForeground(new Color(205, 210, 225));
        d4.setFont(new Font("Arial", Font.PLAIN, 17));
        d4.setBounds(90, 45, 260, 40);
        f4.add(d4);

        addFeatureCardHover(f4, i4L, t4, d4, new Color(245, 146, 24));
        background.add(f4);

        // ================= LOGIN PANEL =================
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(715, 30, 550, 630);
        panel.setBackground(new Color(10, 18, 35, 220));
        panel.setBorder(new LineBorder(new Color(134, 93, 255, 180), 2, true));

        background.add(panel);

        // ================= ICON =================
        JLabel icon = new JLabel("🏨");
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
        icon.setOpaque(true);
        icon.setBackground(new Color(245, 146, 24));
        icon.setForeground(Color.WHITE);
        icon.setHorizontalAlignment(SwingConstants.CENTER);

        icon.setBounds(215, 18, 90, 100);
        panel.add(icon);

        JLabel welcome = new JLabel("Welcome Back!");
        welcome.setForeground(Color.WHITE);
        welcome.setFont(new Font("Arial", Font.BOLD, 38));
        welcome.setBounds(90, 110, 400, 50);
        welcome.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(welcome);

        JLabel sub = new JLabel("Sign in to continue to Hostel Portal");
        sub.setForeground(new Color(205, 210, 225));
        sub.setFont(new Font("Arial", Font.PLAIN, 22));
        sub.setBounds(100, 170, 420, 30);
        panel.add(sub);

        // ================= RADIO BUTTONS =================
        student = new JRadioButton("Student");
        student.setBounds(60, 220, 220, 50);
        student.setForeground(Color.WHITE);
        student.setFont(new Font("Arial", Font.BOLD, 20));
        student.setOpaque(true);
        student.setBorder(new LineBorder(new Color(255, 195, 115), 1, true));
        student.addActionListener(e -> updateRoleButtonStyles());

        staff = new JRadioButton("Warden / Staff");
        staff.setBounds(300, 220, 210, 50);
        staff.setForeground(Color.WHITE);
        staff.setFont(new Font("Arial", Font.BOLD, 20));
        staff.setOpaque(true);
        staff.setBorder(new LineBorder(new Color(90, 100, 135), 1, true));
        staff.addActionListener(e -> updateRoleButtonStyles());

        ButtonGroup g = new ButtonGroup();
        g.add(student);
        g.add(staff);
        student.setSelected(true);
        updateRoleButtonStyles();

        panel.add(student);
        panel.add(staff);

        // ================= USERNAME =================
        JLabel u = new JLabel("Email ID");
        u.setForeground(new Color(225, 230, 240));
        u.setBounds(70, 290, 220, 25);
        u.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(u);

        JPanel userPanel = new JPanel(null);
        userPanel.setBounds(70, 320, 430, 55);
        userPanel.setBackground(new Color(17, 26, 48));
        userPanel.setBorder(new LineBorder(new Color(86, 107, 154), 1, true));
        panel.add(userPanel);

        userIcon = new JLabel("✉");
        userIcon.setForeground(new Color(245, 146, 24));
        userIcon.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 20));
        userIcon.setBounds(18, 15, 24, 24);
        userPanel.add(userIcon);

        userField = new JTextField();
        userField.setBounds(60, 10, 350, 35);
        userField.setFont(new Font("Arial", Font.PLAIN, 18));
        userField.setBackground(new Color(10, 18, 35));
        userField.setForeground(Color.WHITE);
        userField.setCaretColor(Color.WHITE);
        userField.setBorder(null);
        userField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                updateEmailIcon();
            }

            public void removeUpdate(DocumentEvent e) {
                updateEmailIcon();
            }

            public void changedUpdate(DocumentEvent e) {
                updateEmailIcon();
            }

            private void updateEmailIcon() {
                userIcon.setText(getIconForEmail(userField.getText().trim()));
            }
        });
        userPanel.add(userField);

        // ================= PASSWORD =================
        JLabel p = new JLabel("Password");
        p.setForeground(new Color(225, 230, 240));
        p.setBounds(70, 385, 150, 25);
        p.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(p);

        JPanel passPanel = new JPanel(null);
        passPanel.setBounds(70, 410, 430, 55);
        passPanel.setBackground(new Color(17, 26, 48));
        passPanel.setBorder(new LineBorder(new Color(86, 107, 154), 1, true));
        panel.add(passPanel);

        JLabel passIcon = new JLabel("🔒");
        passIcon.setForeground(new Color(245, 146, 24));
        passIcon.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 20));
        passIcon.setBounds(18, 15, 24, 24);
        passPanel.add(passIcon);

        passField = new JPasswordField();
        passField.setBounds(60, 10, 350, 35);
        passField.setFont(new Font("Arial", Font.PLAIN, 18));
        passField.setBackground(new Color(10, 18, 35));
        passField.setForeground(Color.WHITE);
        passField.setCaretColor(Color.WHITE);
        passField.setBorder(null);
        passPanel.add(passField);

        // ================= REMEMBER ME =================
        remember = new JCheckBox("Remember Me");
        remember.setBounds(70, 475, 180, 30);
        remember.setBackground(new Color(10, 18, 35));
        remember.setForeground(new Color(210, 215, 230));
        remember.setFont(new Font("Arial", Font.PLAIN, 18));
        remember.setOpaque(false);
        panel.add(remember);

        JLabel forgot = new JLabel("Forgot Password?");
        forgot.setBounds(340, 475, 170, 25);
        forgot.setForeground(new Color(245, 146, 24));
        forgot.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(forgot);

        // ================= LOGIN BUTTON =================
        loginButton = new JButton("Sign In");
        loginButton.setBounds(70, 520, 430, 55);
        loginButton.setBackground(new Color(245, 146, 24));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 24));
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.addActionListener(this);
        panel.add(loginButton);

        // ================= SIGNUP =================
        JLabel signup = new JLabel("Don't have an account? Sign up here");
        signup.setForeground(new Color(245, 146, 24));
        signup.setFont(new Font("Arial", Font.PLAIN, 15));
        signup.setBounds(150, 585, 380, 25);
        panel.add(signup);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == loginButton) {

            String user = userField.getText().trim();
            String pass = String.valueOf(passField.getPassword()).trim();

            if (user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please enter both username and password.",
                        "Login Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // If student role selected but the entered email is a staff account, block
            // early.
            if (student.isSelected() && isStaffEmail(user)) {
                JOptionPane.showMessageDialog(
                        this,
                        "The entered email appears to be a staff/warden account.\nPlease select 'Warden / Staff' to continue.",
                        "Role Mismatch",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!authenticate(user, pass)) {
                JOptionPane.showMessageDialog(
                        this,
                        "Invalid username or password for the selected role.\n"
                                + "Use:\nStudent => student / student123\n"
                                + "Staff => staff / staff123\n"
                                + "Warden => wardan@kiit.ac.in or wardan@kiitac.in with password warden123",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Successful auth. Route staff/warden to StaffDashboard and keep login open.
            if (isStaffEmail(user)) {
                if (student.isSelected()) {
                    student.setSelected(false);
                    staff.setSelected(true);
                    updateRoleButtonStyles();
                }
                JOptionPane.showMessageDialog(
                        this,
                        "Staff/Warden login successful. Opening staff dashboard.",
                        "Staff Login",
                        JOptionPane.INFORMATION_MESSAGE);
                new StaffDashboard(user);
                return;
            }

            if (student.isSelected()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Login successful! Redirecting to the dashboard.",
                        "Welcome",
                        JOptionPane.INFORMATION_MESSAGE);
                new StudentDashboard(user);
                dispose();
                return;
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Please select the correct role for the entered account.\n"
                            + "Use Student for student accounts and Warden / Staff for staff accounts.",
                    "Role Selection Required",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Invalid credentials for selected role.\n"
                            + "Use:\nStudent => student / student123\n"
                            + "Staff => staff / staff123\n"
                            + "Warden => wardan@kiit.ac.in or wardan@kiitac.in with password warden123",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isStaffEmail(String user) {
        if (user == null) {
            return false;
        }
        String u = user.trim().toLowerCase();
        if (u.isEmpty()) {
            return false;
        }
        // Known staff/warden identifiers
        if (u.equals("staff")) {
            return true;
        }
        if (u.equals("wardan@kiit.ac.in") || u.equals("wardan@kiitac.in")) {
            return true;
        }
        // Add more staff email checks here if needed
        return false;
    }

    private boolean authenticate(String user, String pass) {
        if (student.isSelected()) {
            return !user.isEmpty() && !pass.isEmpty();
        }
        // Staff/Warden authentication
        if ("staff".equalsIgnoreCase(user) && "staff123".equals(pass)) {
            return true;
        }
        if ("wardan@kiit.ac.in".equalsIgnoreCase(user) && "warden123".equals(pass)) {
            return true;
        }
        if ("wardan@kiitac.in".equalsIgnoreCase(user) && "warden123".equals(pass)) {
            return true;
        }
        return false;
    }

    private void updateRoleButtonStyles() {
        if (student.isSelected()) {
            student.setBackground(new Color(245, 146, 24));
            student.setBorder(new LineBorder(new Color(255, 195, 115), 1, true));
            staff.setBackground(new Color(30, 35, 55));
            staff.setBorder(new LineBorder(new Color(90, 100, 135), 1, true));
        } else {
            staff.setBackground(new Color(245, 146, 24));
            staff.setBorder(new LineBorder(new Color(255, 195, 115), 1, true));
            student.setBackground(new Color(30, 35, 55));
            student.setBorder(new LineBorder(new Color(90, 100, 135), 1, true));
        }
        if (userField != null) {
            userField.setText("");
        }
    }

    private void addFeatureCardHover(JPanel card, JLabel icon, JLabel title, JLabel description,
            Color iconNormalColor) {
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(20, 34, 65, 230));
                card.setBorder(new LineBorder(new Color(245, 146, 24), 1, true));
                title.setForeground(new Color(245, 146, 24));
                icon.setForeground(new Color(245, 146, 24));
                description.setForeground(new Color(225, 230, 240));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(new Color(12, 20, 40, 220));
                card.setBorder(new LineBorder(new Color(86, 103, 156, 150), 1, true));
                title.setForeground(Color.WHITE);
                icon.setForeground(iconNormalColor);
                description.setForeground(new Color(205, 210, 225));
            }
        });
    }

    private String getIconForEmail(String email) {
        if (email.equalsIgnoreCase("24052641@kiit.ac.in")) {
            return "👨‍🎓";
        }
        if (email.equalsIgnoreCase("24052604@kiit.ac.in")) {
            return "👨‍💻";
        }
        if (email.equalsIgnoreCase("wardan@kiit.ac.in") || email.equalsIgnoreCase("wardan@kiitac.in")) {
            return "👮";
        }
        if (email.endsWith("@gmail.com")) {
            return "📧";
        }
        if (email.endsWith("@yahoo.com")) {
            return "💌";
        }
        if (email.endsWith("@outlook.com") || email.endsWith("@hotmail.com")) {
            return "📩";
        }
        if (email.endsWith("@kiit.ac.in")) {
            return "🎓";
        }
        if (email.endsWith("@edu") || email.matches(".*@.*\\.edu$")) {
            return "🎓";
        }
        if (email.endsWith("@company.com") || email.endsWith("@hostel.com")) {
            return "🏢";
        }
        if (email.contains("@")) {
            return "✉";
        }
        return "✉";
    }

    /**
     * Finds the hostel photograph whether the app is started from the
     * workspace, project, or APP folder.
     */
    private ImageIcon loadBackgroundImage() {
        String[] locations = {
                "Icon" + File.separator + "hostel.jpg",
                ".." + File.separator + "Icon" + File.separator + "hostel.jpg",
                "Portal" + File.separator + "Hostel Compalin" + File.separator + "Icon" + File.separator + "hostel.jpg"
        };

        for (String location : locations) {
            File imageFile = new File(location);
            if (imageFile.isFile()) {
                ImageIcon icon = new ImageIcon(imageFile.getAbsolutePath());
                if (icon.getIconWidth() > 0) {
                    return icon;
                }
            }
        }

        throw new IllegalStateException("Background image not found: Icon" + File.separator + "hostel.jpg");
    }

    public static void main(String[] args) {
        new Login();
    }
}
