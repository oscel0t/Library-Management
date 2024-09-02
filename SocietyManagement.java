import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class SocietyManagement extends JFrame {
    private JButton continueButton;
    private Connection connection;

    public SocietyManagement() {
        super("Welcome to XYZ Society App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 300); // Increased height of the main window
        setLocationRelativeTo(null); // Center the window
        getContentPane().setBackground(new Color(135, 206, 235)); // Set background color to sky blue
        setLayout(new BorderLayout());

        // Initialize database connection
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbmsproject", "root", "Aditya@1105");
            // Replace "username" and "password" with your database credentials
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to connect to the database.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1); // Exit the application if database connection fails
        }

        // Create a label for the image
        JLabel imageLabel = new JLabel();
        ImageIcon imageIcon = new ImageIcon(getClass().getResource("society.png")); // Load image from resources
        Image scaledImage = imageIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH); // Resize the image
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        imageLabel.setIcon(scaledIcon);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(imageLabel, BorderLayout.NORTH); // Add the image label to the top of the window

        JLabel welcomeLabel = new JLabel("<html><div style='text-align: center;'><b>Welcome to XYZ Society App</b></div></html>", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(welcomeLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false); // Make button panel transparent
        continueButton = new JButton("Continue");
        continueButton.setBackground(new Color(139, 0, 0)); // Set button background color to dark red
        continueButton.setForeground(Color.WHITE); // Set button text color to white
        continueButton.setFont(new Font("Arial", Font.BOLD, 14)); // Set button text font to bold
        continueButton.setFocusPainted(false); // Remove focus border
        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showOptionsDialog();
            }
        });
        buttonPanel.add(continueButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
    private void showOptionsDialog() {
        String[] options = {"View Residents", "Add Residents", "Remove Resident", "View Complaints", "Add Complaint", "Remove Complaint", "View Clubs", "View Househelpers", "Update Househelper Availability", "View Visitors", "Add Visitor", "Remove Visitor"}; // Updated options array
        JPanel panel = new JPanel(new GridLayout(options.length, 1, 0, 10)); // Layout with rows for options
        panel.setBackground(new Color(255, 182, 193)); // Set background color to light pink

        for (String option : options) {
            JButton button = new JButton(option) {
                @Override
                protected void paintComponent(Graphics g) {
                    if (getModel().isPressed()) {
                        g.setColor(new Color(139, 0, 0)); // Set button color to dark red when pressed
                    } else {
                        g.setColor(new Color(255, 165, 0)); // Set button background color to light orange
                    }
                    g.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10); // Rounded rectangle shape
                    super.paintComponent(g);
                }

                @Override
                protected void paintBorder(Graphics g) {
                    // Remove border painting
                }
            };
            button.setBackground(new Color(139, 0, 0)); // Set button color to dark red
            button.setForeground(Color.WHITE); // Set button text color to white
            button.setFont(new Font("Arial", Font.BOLD, 14)); // Set button text font to bold
            button.setFocusPainted(false); // Remove focus border
            button.setPreferredSize(new Dimension(200, 40)); // Set preferred button size
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    handleOption(option);
                }
            });
            panel.add(button);
        }

        // Create scroll pane
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Remove border

        // Increase the height of the option window
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.getWidth() * 0.5); // Set to 50% of screen width
        int height = (int) (screenSize.getHeight() * 0.7); // Set to 70% of screen height
        scrollPane.setPreferredSize(new Dimension(width, height));

        JOptionPane.showOptionDialog(this, scrollPane, "Choose an Option", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, new Object[]{}, null);
    }

    private void handleOption(String option) {
        if (option.equals("View Residents")) {
            viewResidents();
        } else if (option.equals("Add Residents")) {
            addResident();
        } else if (option.equals("Remove Resident")) {
            removeResident();
        } else if (option.equals("View Complaints")) {
            viewComplaints();
        } else if (option.equals("Add Complaint")) {
            addComplaint();
        } else if (option.equals("Remove Complaint")) {
            removeComplaint();
        } else if (option.equals("View Clubs")) {
            viewClubs();
        } else if (option.equals("View Househelpers")) {
            viewHousehelpers();
        } else if (option.equals("Update Househelper Availability")) {
            updateHousehelperAvailability();
        } else if (option.equals("View Visitors")) {
            viewVisitors();
        } else if (option.equals("Add Visitor")) {
            addVisitor();
        } else if (option.equals("Remove Visitor")) {
            removeVisitor();
        } else {
            // Handle other options
            JOptionPane.showMessageDialog(this, option + " Selected");
        }
    }
    private boolean authenticateAdmin() {
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Admin Login", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            // Check username and password against database (replace with your database logic)
            if (username.equals("admin") && password.equals("admin123")) {
                return true; // Authentication successful
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
                return false; // Authentication failed
            }
        } else {
            return false; // User canceled the login
        }
    }
    private void removeVisitor() {
        // Create an input field for visitor ID
       if(authenticateAdmin())
       {
           JTextField visitorIdField = new JTextField();

           JPanel panel = new JPanel(new GridLayout(0, 1));
           panel.add(new JLabel("Visitor ID:"));
           panel.add(visitorIdField);

           int result = JOptionPane.showConfirmDialog(this, panel, "Remove Visitor", JOptionPane.OK_CANCEL_OPTION);
           if (result == JOptionPane.OK_OPTION) {
               // Retrieve the visitor ID entered by the user
               int visitorId = Integer.parseInt(visitorIdField.getText());

               try {
                   // Prepare the SQL statement
                   String sql = "DELETE FROM Visitors WHERE visitor_id = ?";

                   // Create a prepared statement
                   PreparedStatement statement = connection.prepareStatement(sql);

                   // Set the parameter
                   statement.setInt(1, visitorId);

                   // Execute the delete operation
                   int rowsDeleted = statement.executeUpdate();

                   // Check if any rows were deleted
                   if (rowsDeleted > 0) {
                       JOptionPane.showMessageDialog(this, "Visitor removed successfully.");
                   } else {
                       JOptionPane.showMessageDialog(this, "No visitor found with the given ID.", "Error", JOptionPane.ERROR_MESSAGE);
                   }
               } catch (SQLException e) {
                   e.printStackTrace();
                   JOptionPane.showMessageDialog(this, "Error removing visitor: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
               }
           }
       }
    }

    private void removeComplaint() {
        // Create an input field for complaint ID
        if(authenticateAdmin())
        {
            JTextField complaintIdField = new JTextField();

            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("Complaint ID:"));
            panel.add(complaintIdField);

            int result = JOptionPane.showConfirmDialog(this, panel, "Remove Complaint", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                // Retrieve the complaint ID entered by the user
                int complaintId = Integer.parseInt(complaintIdField.getText());

                try {
                    // Prepare the SQL statement
                    String sql = "DELETE FROM Complaints WHERE complaint_id = ?";

                    // Create a prepared statement
                    PreparedStatement statement = connection.prepareStatement(sql);

                    // Set the parameter
                    statement.setInt(1, complaintId);

                    // Execute the delete operation
                    int rowsDeleted = statement.executeUpdate();

                    // Check if any rows were deleted
                    if (rowsDeleted > 0) {
                        JOptionPane.showMessageDialog(this, "Complaint removed successfully.");
                    } else {
                        JOptionPane.showMessageDialog(this, "No complaint found with the given ID.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error removing complaint: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    private void removeResident() {
        // Create an input field for resident ID
       if(authenticateAdmin())
       {

           JTextField residentIdField = new JTextField();

           JPanel panel = new JPanel(new GridLayout(0, 1));
           panel.add(new JLabel("Resident ID:"));
           panel.add(residentIdField);

           int result = JOptionPane.showConfirmDialog(this, panel, "Remove Resident", JOptionPane.OK_CANCEL_OPTION);
           if (result == JOptionPane.OK_OPTION) {
               // Retrieve the resident ID entered by the user
               int residentId = Integer.parseInt(residentIdField.getText());

               try {
                   // Prepare the SQL statement
                   String sql = "DELETE FROM Residents WHERE resident_id = ?";

                   // Create a prepared statement
                   PreparedStatement statement = connection.prepareStatement(sql);

                   // Set the parameter
                   statement.setInt(1, residentId);

                   // Execute the delete operation
                   int rowsDeleted = statement.executeUpdate();

                   // Check if any rows were deleted
                   if (rowsDeleted > 0) {
                       JOptionPane.showMessageDialog(this, "Resident removed successfully.");
                   } else {
                       JOptionPane.showMessageDialog(this, "No resident found with the given ID.", "Error", JOptionPane.ERROR_MESSAGE);
                   }
               } catch (SQLException e) {
                   e.printStackTrace();
                   JOptionPane.showMessageDialog(this, "Error removing resident: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
               }
           }
       }
    }

    private void updateHousehelperAvailability() {
        // Create input fields for househelper name and availability
       if(authenticateAdmin())
       {
           JTextField nameField = new JTextField();
           JTextField availabilityField = new JTextField();

           JPanel panel = new JPanel(new GridLayout(0, 1));
           panel.add(new JLabel("Househelper Name:"));
           panel.add(nameField);
           panel.add(new JLabel("New Availability:"));
           panel.add(availabilityField);

           int result = JOptionPane.showConfirmDialog(this, panel, "Update Househelper Availability", JOptionPane.OK_CANCEL_OPTION);
           if (result == JOptionPane.OK_OPTION) {
               // Retrieve values from input fields
               String househelperName = nameField.getText();
               String newAvailability = availabilityField.getText();

               try {
                   // Prepare the SQL statement
                   String sql = "UPDATE Househelp SET availability = ? WHERE helper_name = ?";

                   // Create a prepared statement
                   PreparedStatement statement = connection.prepareStatement(sql);

                   // Set the parameters
                   statement.setString(1, newAvailability);
                   statement.setString(2, househelperName);

                   // Execute the update
                   int rowsUpdated = statement.executeUpdate();

                   // Check if the update was successful
                   if (rowsUpdated > 0) {
                       JOptionPane.showMessageDialog(this, "Househelper availability updated successfully.");
                   } else {
                       JOptionPane.showMessageDialog(this, "No househelper found with the given name.", "Error", JOptionPane.ERROR_MESSAGE);
                   }
               } catch (SQLException e) {
                   e.printStackTrace();
                   JOptionPane.showMessageDialog(this, "Error updating househelper availability: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
               }
           }
       }
    }


    private void viewResidents() {
        try {
            // Create a statement
            Statement statement = connection.createStatement();

            // Execute query to retrieve resident details
            ResultSet resultSet = statement.executeQuery("SELECT resident_id, residentname, flatnumber, mobile, block_id FROM Residents");

            // Create a table model to hold the data
            DefaultTableModel model = new DefaultTableModel();

            // Add columns to the table model
            model.addColumn("Resident ID");
            model.addColumn("Name");
            model.addColumn("Flat Number");
            model.addColumn("Mobile");
            model.addColumn("Block ID"); // Changed from "Block Name" to "Block ID"

            // Add rows to the table model
            while (resultSet.next()) {
                Object[] row = {
                        resultSet.getInt("resident_id"),
                        resultSet.getString("residentname"),
                        resultSet.getString("flatnumber"),
                        resultSet.getString("mobile"),
                        resultSet.getString("block_id") // Retrieving Block ID from the database
                };
                model.addRow(row);
            }

            // Create a table with the model
            JTable table = new JTable(model);

            // Set font size for table cells
            table.setFont(new Font("Arial", Font.PLAIN, 14));

            // Set row height
            table.setRowHeight(30);

            // Set column widths
            table.getColumnModel().getColumn(0).setPreferredWidth(100);
            table.getColumnModel().getColumn(1).setPreferredWidth(200);
            table.getColumnModel().getColumn(2).setPreferredWidth(100);
            table.getColumnModel().getColumn(3).setPreferredWidth(150);
            table.getColumnModel().getColumn(4).setPreferredWidth(100);

            // Set button color and text color in table
            table.setBackground(new Color(245, 222, 179)); // Wheat color
            table.setForeground(Color.BLACK);

            // Beautify the table appearance
            table.setGridColor(Color.BLACK);
            table.setShowGrid(true);
            table.setFillsViewportHeight(true);

            // Show the table in a scrollable dialog
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setPreferredSize(new Dimension(600, 400)); // Set preferred size for the scroll pane
            JOptionPane.showMessageDialog(this, scrollPane, "Resident Details", JOptionPane.PLAIN_MESSAGE);

            // Get the OK button in the dialog
            JButton okButton = getOkButton(table.getParent());

            // Set button color and text color for OK button
            okButton.setBackground(new Color(139, 0, 0));
            okButton.setForeground(Color.WHITE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching resident details.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewComplaints() {
        try {
            // Create a statement
            Statement statement = connection.createStatement();

            // Execute query to retrieve complaint details
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Complaints");

            // Create a table model to hold the data
            DefaultTableModel model = new DefaultTableModel();

            // Add columns to the table model
            model.addColumn("Complaint ID");
            model.addColumn("Resident ID");
            model.addColumn("Complaint Description");
            model.addColumn("Complaint Type");

            // Add rows to the table model
            while (resultSet.next()) {
                Object[] row = {
                        resultSet.getInt("complaint_id"),
                        resultSet.getInt("resident_id"),
                        resultSet.getString("complaint_text"),
                        resultSet.getString("complaint_type")
                };
                model.addRow(row);
            }

            // Create a table with the model
            JTable table = new JTable(model);

            // Set font size for table cells
            table.setFont(new Font("Arial", Font.PLAIN, 14));

            // Set row height
            table.setRowHeight(30);

            // Set column widths
            table.getColumnModel().getColumn(0).setPreferredWidth(100);
            table.getColumnModel().getColumn(1).setPreferredWidth(100);
            table.getColumnModel().getColumn(2).setPreferredWidth(300);
            table.getColumnModel().getColumn(3).setPreferredWidth(150);

            // Set button color and text color in table
            table.setBackground(new Color(245, 222, 179)); // Wheat color
            table.setForeground(Color.BLACK);

            // Beautify the table appearance
            table.setGridColor(Color.BLACK);
            table.setShowGrid(true);
            table.setFillsViewportHeight(true);

            // Show the table in a scrollable dialog
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setPreferredSize(new Dimension(600, 400)); // Set preferred size for the scroll pane
            JOptionPane.showMessageDialog(this, scrollPane, "Complaint Details", JOptionPane.PLAIN_MESSAGE);

            // Get the OK button in the dialog
            JButton okButton = getOkButton(table.getParent());

            // Set button color and text color for OK button
            okButton.setBackground(new Color(139, 0, 0));
            okButton.setForeground(Color.WHITE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching complaint details.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addResident() {


        // Create input fields for resident details
       if(authenticateAdmin())
       {
           JTextField nameField = new JTextField();
           JTextField flatNumberField = new JTextField();
           JTextField mobileField = new JTextField();
           JTextField blockNameField = new JTextField(); // Added Block Name field

           JPanel panel = new JPanel(new GridLayout(0, 1));
           panel.add(new JLabel("Name:"));
           panel.add(nameField);
           panel.add(new JLabel("Flat Number:"));
           panel.add(flatNumberField);
           panel.add(new JLabel("Mobile:"));
           panel.add(mobileField);
           panel.add(new JLabel("Block Name:")); // Added Block Name label
           panel.add(blockNameField); // Added Block Name field

           int result = JOptionPane.showConfirmDialog(this, panel, "Add Resident", JOptionPane.OK_CANCEL_OPTION);
           if (result == JOptionPane.OK_OPTION) {
               // Retrieve values from input fields
               String name = nameField.getText();
               String flatNumber = flatNumberField.getText();
               String mobile = mobileField.getText();
               String blockid = blockNameField.getText(); // Retrieve Block Name

               // Insert the new resident into the database
               try {
                   PreparedStatement statement = connection.prepareStatement("INSERT INTO Residents (residentname, flatnumber, mobile, block_id) VALUES (?, ?, ?, ?)");
                   statement.setString(1, name);
                   statement.setString(2, flatNumber);
                   statement.setString(3, mobile);
                   statement.setString(4, blockid); // Set Block Name in the statement
                   int rowsInserted = statement.executeUpdate();
                   if (rowsInserted > 0) {
                       JOptionPane.showMessageDialog(this, "Resident added successfully.");
                   }
               } catch (SQLException e) {
                   e.printStackTrace();
                   JOptionPane.showMessageDialog(this, "Error adding resident.", "Error", JOptionPane.ERROR_MESSAGE);
               }
           }
       }
    }

    private void addComplaint() {
        // Create input fields for complaint details
        JTextField residentIdField = new JTextField();
        JTextField complaintDescriptionField = new JTextField();
        JTextField complaintTypeField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Resident ID:"));
        panel.add(residentIdField);
        panel.add(new JLabel("Complaint Description:"));
        panel.add(complaintDescriptionField);
        panel.add(new JLabel("Complaint Type:"));
        panel.add(complaintTypeField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add Complaint", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            // Retrieve values from input fields
            int residentId = Integer.parseInt(residentIdField.getText());
            String complaintDescription = complaintDescriptionField.getText();
            String complaintType = complaintTypeField.getText();

            // Insert the new complaint into the database
            try {
                PreparedStatement statement = connection.prepareStatement("INSERT INTO Complaints (resident_id, complaint_text, complaint_type) VALUES (?, ?, ?)");
                statement.setInt(1, residentId);
                statement.setString(2, complaintDescription);
                statement.setString(3, complaintType);
                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    JOptionPane.showMessageDialog(this, "Complaint added successfully.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error adding complaint.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Method to get the OK button in the dialog
    private JButton getOkButton(Component parent) {
        if (parent instanceof JOptionPane) {
            JOptionPane optionPane = (JOptionPane) parent;
            return (JButton) optionPane.getComponent(1); // Assuming OK button is the second component
        }
        return null;
    }

    private void viewClubs() {
        try {
            // Create a statement
            Statement statement = connection.createStatement();

            // Execute query to retrieve club details
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Clubs");

            // Create a table model to hold the data
            DefaultTableModel model = new DefaultTableModel();

            // Add columns to the table model
            model.addColumn("Club ID");
            model.addColumn("Club Name");
            model.addColumn("Description");

            // Add rows to the table model
            while (resultSet.next()) {
                Object[] row = {
                        resultSet.getInt("club_id"),
                        resultSet.getString("club_name"),
                        resultSet.getString("club_activities")
                };
                model.addRow(row);
            }

            // Create a table with the model
            JTable table = new JTable(model);

            // Set font size for table cells
            table.setFont(new Font("Arial", Font.PLAIN, 14));

            // Set row height
            table.setRowHeight(30);

            // Set column widths
            table.getColumnModel().getColumn(0).setPreferredWidth(100);
            table.getColumnModel().getColumn(1).setPreferredWidth(200);
            table.getColumnModel().getColumn(2).setPreferredWidth(300);

            // Set button color and text color in table
            table.setBackground(new Color(245, 222, 179)); // Wheat color
            table.setForeground(Color.BLACK);

            // Beautify the table appearance
            table.setGridColor(Color.BLACK);
            table.setShowGrid(true);
            table.setFillsViewportHeight(true);

            // Show the table in a scrollable dialog
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setPreferredSize(new Dimension(600, 400)); // Set preferred size for the scroll pane
            JOptionPane.showMessageDialog(this, scrollPane, "Club Details", JOptionPane.PLAIN_MESSAGE);

            // Get the OK button in the dialog
            JButton okButton = getOkButton(table.getParent());

            // Set button color and text color for OK button
            okButton.setBackground(new Color(139, 0, 0));
            okButton.setForeground(Color.WHITE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching club details.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewHousehelpers() {
        try {
            // Create a statement
            Statement statement = connection.createStatement();

            // Execute query to retrieve househelper details
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Househelp");

            // Create a table model to hold the data
            DefaultTableModel model = new DefaultTableModel();

            // Add columns to the table model
            model.addColumn("Name");
            model.addColumn("Availability");
            model.addColumn("Aadhar Number");
            model.addColumn("Mobile");
            model.addColumn("Block ID");

            // Add rows to the table model
            while (resultSet.next()) {
                Object[] row = {
                        resultSet.getString("helper_name"),
                        resultSet.getString("availability"),
                        resultSet.getString("aadhar_number"),
                        resultSet.getString("househelper_number"),
                        resultSet.getString("block_id")
                };
                model.addRow(row);
            }

            // Create a table with the model
            JTable table = new JTable(model);

            // Set font size for table cells
            table.setFont(new Font("Arial", Font.PLAIN, 14));

            // Set row height
            table.setRowHeight(30);

            // Set column widths
            table.getColumnModel().getColumn(0).setPreferredWidth(150);
            table.getColumnModel().getColumn(1).setPreferredWidth(100);
            table.getColumnModel().getColumn(2).setPreferredWidth(200);
            table.getColumnModel().getColumn(3).setPreferredWidth(150);
            table.getColumnModel().getColumn(4).setPreferredWidth(100);

            // Set button color and text color in table
            table.setBackground(new Color(245, 222, 179)); // Wheat color
            table.setForeground(Color.BLACK);

            // Beautify the table appearance
            table.setGridColor(Color.BLACK);
            table.setShowGrid(true);
            table.setFillsViewportHeight(true);

            // Show the table in a scrollable dialog
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setPreferredSize(new Dimension(600, 400)); // Set preferred size for the scroll pane
            JOptionPane.showMessageDialog(this, scrollPane, "Househelper Details", JOptionPane.PLAIN_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching househelper details.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void addVisitor() {
        // Create input fields for visitor details
       if(authenticateAdmin())
       {
           JTextField nameField = new JTextField();
           JTextField addressField = new JTextField();
           JTextField emailField = new JTextField();
           JTextField phoneField = new JTextField();
           JTextField residentIdField = new JTextField();
           JTextField purposeField = new JTextField();

           JPanel panel = new JPanel(new GridLayout(0, 1));
           panel.add(new JLabel("Visitor Name:"));
           panel.add(nameField);
           panel.add(new JLabel("Address:"));
           panel.add(addressField);
           panel.add(new JLabel("Email:"));
           panel.add(emailField);
           panel.add(new JLabel("Phone Number:"));
           panel.add(phoneField);
           panel.add(new JLabel("Resident ID:"));
           panel.add(residentIdField);


           int result = JOptionPane.showConfirmDialog(this, panel, "Add Visitor", JOptionPane.OK_CANCEL_OPTION);
           if (result == JOptionPane.OK_OPTION) {
               // Retrieve values from input fields
               String visitorName = nameField.getText();
               String address = addressField.getText();
               String email = emailField.getText();
               String phoneNumber = phoneField.getText();
               int residentId = Integer.parseInt(residentIdField.getText());


               // Insert the new visitor into the database
               try {
                   PreparedStatement statement = connection.prepareStatement("INSERT INTO Visitors (visitor_name, v_address, v_email, phone_number, resident_id) VALUES (?, ?, ?, ?, ?)");
                   statement.setString(1, visitorName);
                   statement.setString(2, address);
                   statement.setString(3, email);
                   statement.setString(4, phoneNumber);
                   statement.setInt(5, residentId);
                   int rowsInserted = statement.executeUpdate();
                   if (rowsInserted > 0) {
                       JOptionPane.showMessageDialog(this, "Visitor added successfully.");
                   }
               } catch (SQLException e) {
                   e.printStackTrace();
                   JOptionPane.showMessageDialog(this, "Error adding visitor.", "Error", JOptionPane.ERROR_MESSAGE);
               }
           }
       }
    }

    private void viewVisitors() {
        try {
            // Create a statement
            Statement statement = connection.createStatement();

            // Execute query to retrieve visitor details
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Visitors");

            // Create a table model to hold the data
            DefaultTableModel model = new DefaultTableModel();

            // Add columns to the table model
            model.addColumn("Visitor ID");
            model.addColumn("Visitor Name");
            model.addColumn("Address");
            model.addColumn("Email");
            model.addColumn("Phone Number");
            model.addColumn("Resident ID");

            // Add rows to the table model
            while (resultSet.next()) {
                Object[] row = {
                        resultSet.getInt("visitor_id"),
                        resultSet.getString("visitor_name"),
                        resultSet.getString("v_address"),
                        resultSet.getString("v_email"),
                        resultSet.getString("phone_number"),
                        resultSet.getInt("resident_id")
                };
                model.addRow(row);
            }

            // Create a table with the model
            JTable table = new JTable(model);

            // Set font size for table cells
            table.setFont(new Font("Arial", Font.PLAIN, 14));

            // Set row height
            table.setRowHeight(30);

            // Set column widths
            table.getColumnModel().getColumn(0).setPreferredWidth(100);
            table.getColumnModel().getColumn(1).setPreferredWidth(150);
            table.getColumnModel().getColumn(2).setPreferredWidth(200);
            table.getColumnModel().getColumn(3).setPreferredWidth(150);
            table.getColumnModel().getColumn(4).setPreferredWidth(150);
            table.getColumnModel().getColumn(5).setPreferredWidth(100);


            // Set button color and text color in table
            table.setBackground(new Color(245, 222, 179)); // Wheat color
            table.setForeground(Color.BLACK);

            // Beautify the table appearance
            table.setGridColor(Color.BLACK);
            table.setShowGrid(true);
            table.setFillsViewportHeight(true);

            // Show the table in a scrollable dialog
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setPreferredSize(new Dimension(800, 400)); // Set preferred size for the scroll pane
            JOptionPane.showMessageDialog(this, scrollPane, "Visitor Details", JOptionPane.PLAIN_MESSAGE);

            // Get the OK button in the dialog
            JButton okButton = getOkButton(table.getParent());

            // Set button color and text color for OK button
            okButton.setBackground(new Color(139, 0, 0));
            okButton.setForeground(Color.WHITE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching visitor details.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public static void main(String[] args) {
        // Set up database connection
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "MySQL JDBC Driver not found.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1); // Exit the application if JDBC driver not found
        }

        SwingUtilities.invokeLater(SocietyManagement::new);
    }
}