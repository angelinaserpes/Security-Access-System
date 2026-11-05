import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Security extends JFrame {

    JTextField usernameField;
    JPasswordField passwordField;
    JComboBox<String> roleField;
    JButton loginButton;

    int attempts = 0;
    boolean isLocked = false;

    public Security() {

        setTitle("Login System");
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        roleField = new JComboBox<>(new String[]{"Admin", "Employee", "Visitor"});
        loginButton = new JButton("Login");

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        panel.add(new JLabel("Role:"));
        panel.add(roleField);

        panel.add(new JLabel());
        panel.add(loginButton);

        add(panel);

        loginButton.addActionListener(e -> login());
    }

    private void login() {

        if (isLocked) {
            JOptionPane.showMessageDialog(this, "Account Locked!");
            return;
        }

        String user = usernameField.getText().trim();
        String pass = new String(passwordField.getPassword()).trim();
        String role = (String) roleField.getSelectedItem().toString().trim();

        if (authenticate(user, pass, role)) {
            JOptionPane.showMessageDialog(this, "Login Successful!");
            new Dashboard(role);
            dispose();
        } else {
            attempts++;
            if (attempts >= 3) {
                isLocked = true;
                loginButton.setEnabled(false);
                JOptionPane.showMessageDialog(this, "Account Locked!");
            } else {
                JOptionPane.showMessageDialog(this,
                        "Invalid! Attempts left: " + (3 - attempts));
            }
        }
    }

    public boolean authenticate(String username, String password, String role) {

        try {
            Connection con = DBConnection.getConnection();

            String query = "SELECT * FROM users WHERE username=? AND password=? AND LOWER(role)=LOWER(?)";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, role);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                con.close();
                return true;
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void main(String[] args) {
        new Security().setVisible(true);
    }
}

class Dashboard extends JFrame {
    // This is the "Message" everyone is looking at
    private static String currentSystemMessage = "Welcome to the Security System. All systems normal.";

    public Dashboard(String role) {
        setTitle("Security Dashboard - " + role);
        setSize(400, 300);
        setLayout(new GridLayout(3, 1, 10, 10)); // Simple vertical layout
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JButton viewBtn = new JButton("View Current Status");
        JButton editBtn = new JButton("Edit Status Message");
        JButton logsBtn = new JButton("Access System Logs");

        // --- ROLE PERMISSIONS ---
        if (role.equalsIgnoreCase("Visitor")) {
            editBtn.setEnabled(false); // Greyed out
            logsBtn.setEnabled(false); // Greyed out
        } 
        else if (role.equalsIgnoreCase("Employee")) {
            logsBtn.setEnabled(false); // Only logs are greyed out
        }
        // Admin has all buttons enabled by default

        // --- BUTTON ACTIONS ---

        // 1. VIEW: Everyone can do this
        viewBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "SYSTEM MESSAGE:\n" + currentSystemMessage);
        });

        // 2. EDIT: Employee and Admin only
        editBtn.addActionListener(e -> {
            String newMessage = JOptionPane.showInputDialog(this, "Enter new status message:", currentSystemMessage);
            if (newMessage != null && !newMessage.trim().isEmpty()) {
                currentSystemMessage = newMessage;
                JOptionPane.showMessageDialog(this, "Message Updated!");
            }
        });

        // 3. LOGS: Admin only
        logsBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "ADMIN PANEL:\nLast 5 logins: Admin, Emp, Emp, Vis, Admin");
        });

        add(viewBtn);
        add(editBtn);
        add(logsBtn);

        setVisible(true);
    }
}
