import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Main {

    // Declare global variables to manage data and GUI components
    public static ArrayList<Student> stats = new ArrayList<>();
    public static JTable jTable;
    public static DefaultTableModel model;
    public static JFrame frameStudent;
    public static JFrame frameTable;
    public static JComboBox deleteStats;
    public static int index = -1;
    public static String lastUpdatedField = "";

    // JFrame for the main menu
    public static JFrame frameMenu;
    // Panel for the menu, overriding paintComponent for custom background image
    public static JPanel panelMenu = new JPanel() {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            try {
                // Load a background image and draw it on the panel
                Image backgroundImage = new ImageIcon(getClass().getResource("GuiMenu/menuImage.png")).getImage();
                g.drawImage(backgroundImage, 330, 410, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public static void main(String[] args) {
        // Arrays to define the sorting, editing, and undo options for student data
        String[] sortStudentStats = {"Height (select then enter)", "Age (select then enter)", "Grade (select then enter)"};
        String[] editStudentStats = {"Height (enter then select)", "Age (enter then select)", "Grade (enter then select)"};
        String[] undoStudentStats = {"Height (select then enter)", "Age (select then enter)", "Grade (select then enter)"};

        // Set up the frame for student data entry
        frameStudent = new JFrame("Class Database");
        frameStudent.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameStudent.pack();
        frameStudent.setBounds(200, 200, 780, 970);
        frameStudent.setLocationRelativeTo(null);
        Color colBackground = new Color(50, 100, 190);
        Color colButton = Color.DARK_GRAY;
        frameStudent.setBackground(colBackground);

        // Set up the table frame for displaying student data
        frameTable = new JFrame("Class Database");
        frameTable.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameTable.pack();
        frameTable.setBounds(690, 50, 1217, 1041);
        frameTable.setBackground(colBackground);

        // Set up the menu frame
        frameMenu = new JFrame("Class Database");
        frameMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameMenu.pack();
        frameMenu.setBounds(690, 50, 1217, 1041);
        frameMenu.setLocationRelativeTo(null);
        frameMenu.setBackground(colBackground);

        // Set up the panel for student data entry, no layout (absolute positioning)
        JPanel panelStudent = new JPanel();
        panelStudent.setLayout(null);
        panelStudent.setBackground(colBackground);

        // Set up the panel for the table view
        JPanel panelTable = new JPanel();
        panelTable.setLayout(null);
        panelTable.setBackground(colBackground);

        // Panel for actions like adding, editing, and deleting
        JPanel panelActions = new JPanel();
        panelActions.setLayout(null);
        panelActions.setBackground(colBackground);

        // Panel for menu settings
        panelMenu.setLayout(null);
        panelMenu.setBackground(colBackground);

        // JLabel for search field label
        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setBounds(30, 10, 200, 50);
        searchLabel.setFont(new Font("Arial", Font.BOLD, 16));
        searchLabel.setForeground(Color.white);

        // JTextField for search input
        JTextField searchField = new JTextField();
        searchField.setBounds(100, 10, 500, 50);
        searchField.setFont(new Font("Arial", Font.BOLD, 16));
        searchField.setForeground(Color.WHITE);

        // JButton for the search action
        JButton searchButton = new JButton("Find");
        searchButton.setBounds(620, 10, 100, 50);
        searchButton.setBackground(colButton);
        searchButton.setFont(new Font("Arial", Font.BOLD, 20));
        searchButton.setForeground(Color.white);

        // Labels and text fields for student details (name, height, age, grade)
        JLabel nameLabel = new JLabel("Student name:");
        nameLabel.setBounds(160, 150, 200, 50);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nameLabel.setForeground(Color.white);

        JTextField nameField = new JTextField();
        nameField.setBounds(320, 150, 200, 50);
        nameField.setFont(new Font("Arial", Font.BOLD, 16));
        nameField.setForeground(Color.black);

        JLabel heightLabel = new JLabel("Student height (ft.):");
        heightLabel.setBounds(160, 250, 200, 50);
        heightLabel.setFont(new Font("Arial", Font.BOLD, 16));
        heightLabel.setForeground(Color.white);

        JTextField heightField = new JTextField();
        heightField.setBounds(320, 250, 200, 50);
        heightField.setFont(new Font("Arial", Font.BOLD, 16));
        heightField.setForeground(Color.black);

        JLabel ageLabel = new JLabel("Student age:");
        ageLabel.setBounds(160, 350, 200, 50);
        ageLabel.setFont(new Font("Arial", Font.BOLD, 16));
        ageLabel.setForeground(Color.white);

        JTextField ageField = new JTextField();
        ageField.setBounds(320, 350, 200, 50);
        ageField.setFont(new Font("Arial", Font.BOLD, 16));
        ageField.setForeground(Color.black);

        JLabel gradeLabel = new JLabel("Student grade:");
        gradeLabel.setBounds(160, 450, 200, 50);
        gradeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gradeLabel.setForeground(Color.white);

        JTextField gradeField = new JTextField();
        gradeField.setBounds(320, 450, 200, 50);
        gradeField.setFont(new Font("Arial", Font.BOLD, 16));
        gradeField.setForeground(Color.black);

        // Buttons for submitting student info, toggling CSV load, and deleting students
        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(160, 550, 100, 50);
        submitButton.setBackground(colButton);
        submitButton.setFont(new Font("Arial", Font.BOLD, 16));
        submitButton.setForeground(Color.white);

        JButton toggleInput = new JButton("Load CSV Stats");
        toggleInput.setBounds(160, 690, 190, 50);
        toggleInput.setBackground(colButton);
        toggleInput.setFont(new Font("Arial", Font.BOLD, 16));
        toggleInput.setForeground(Color.white);

        JLabel deleteLabel = new JLabel("Delete:");
        deleteLabel.setBounds(160, 630, 200, 50);
        deleteLabel.setFont(new Font("Arial", Font.BOLD, 16));
        deleteLabel.setForeground(Color.white);

        deleteStats = new JComboBox();  // ComboBox to select student to delete
        deleteStats.getSelectedItem();
        deleteStats.setBounds(220, 630, 210, 50);
        deleteStats.setFont(new Font("Arial", Font.BOLD, 16));
        deleteStats.setForeground(Color.black);
        // Add all students to ComboBox for deletion
        for (int i = 0; i < stats.size(); i++) {
            deleteStats.addItem(stats.get(i));
        }

        JButton deleteButton = new JButton("Delete");
        deleteButton.setBounds(440, 630, 120, 50);
        deleteButton.setBackground(colButton);
        deleteButton.setFont(new Font("Arial", Font.BOLD, 16));
        deleteButton.setForeground(Color.white);

        JButton displayButton = new JButton("Display");
        displayButton.setBounds(160, 810, 150, 70);
        displayButton.setBackground(colButton);
        displayButton.setFont(new Font("Arial", Font.BOLD, 20));
        displayButton.setForeground(Color.white);

        JButton loadData = new JButton("Load Saved Data");
        loadData.setBounds(160, 750, 180, 50);
        loadData.setBackground(colButton);
        loadData.setFont(new Font("Arial", Font.BOLD, 16));
        loadData.setForeground(Color.white);


        // Create an ArrayList to hold row data for the table
        ArrayList<String[]> rowDataList = new ArrayList<>();

        // Define column names for the table
        String[] columnNames = {"Name", "Height (ft.)", "Age", "Grade", "oldHeight", "oldAge", "oldGrade"};

        // Create a table model using the column names and an initial row count of 0
        model = new DefaultTableModel(columnNames, 0);

        // Create a new JTable using the table model
        jTable = new JTable(model);

        // Set table appearance and behavior
        jTable.setBackground(colBackground);
        jTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));
        jTable.setFont(new Font("Arial", Font.BOLD, 20));
        jTable.setForeground(Color.white);
        jTable.setEnabled(false);
        hideLastThreeColumns(jTable);

        jTable.setRowHeight(40);

        jTable.revalidate();
        jTable.repaint();

        // Create a scroll pane to wrap the JTable for better usability
        JScrollPane sp = new JScrollPane(jTable);
        sp.setBounds(0, 0, 1200, 1000);

        // Creates and combines UI components for new student data input (Height, Age, Grade)
        JLabel newHeightLabel = new JLabel("New Height:");
        newHeightLabel.setBounds(30, 430, 300, 500);
        newHeightLabel.setFont(new Font("Arial", Font.BOLD, 20));
        newHeightLabel.setForeground(Color.white);

        JTextField newHeightField = new JTextField(5);
        newHeightField.setBounds(150, 664, 160, 30);
        newHeightField.setFont(new Font("Arial", Font.BOLD, 20));
        newHeightField.setForeground(Color.black);

        JLabel newAgeLabel = new JLabel("New Age:");
        newAgeLabel.setBounds(30, 430, 300, 500);
        newAgeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        newAgeLabel.setForeground(Color.white);

        JTextField newAgeField = new JTextField(5);
        newAgeField.setBounds(150, 664, 160, 30);
        newAgeField.setFont(new Font("Arial", Font.BOLD, 20));
        newAgeField.setForeground(Color.black);

        JLabel newGradeLabel = new JLabel("New Grade:");
        newGradeLabel.setBounds(30, 430, 300, 500);
        newGradeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        newGradeLabel.setForeground(Color.white);

        JTextField newGradeField = new JTextField(5);
        newGradeField.setBounds(150, 664, 160, 30);
        newGradeField.setFont(new Font("Arial", Font.BOLD, 20));
        newGradeField.setForeground(Color.black);

        // Create a button to update the student stats (Height, Age, Grade)
        JButton updateStatsButton = new JButton("Update");
        updateStatsButton.setBackground(colButton);
        updateStatsButton.setBounds(320, 662, 130, 35);
        updateStatsButton.setFont(new Font("Arial", Font.BOLD, 20));
        updateStatsButton.setForeground(Color.white);

        // Create a button to go back (or enter new data)
        JButton enterNewData = new JButton("Back");
        enterNewData.setBounds(10, 500, 200, 80);
        enterNewData.setBackground(colButton);
        enterNewData.setFont(new Font("Arial", Font.BOLD, 20));
        enterNewData.setForeground(Color.white);

        // Create a label and combo box for sorting
        JLabel sortLabel = new JLabel("Sort:");
        sortLabel.setBounds(10, 100, 200, 50);
        sortLabel.setFont(new Font("Arial", Font.BOLD, 16));
        sortLabel.setForeground(Color.white);

        JComboBox<String> sortStats = new JComboBox<>(sortStudentStats);
        sortStats.setBounds(100, 100, 220, 50);
        sortStats.setFont(new Font("Arial", Font.BOLD, 16));
        searchField.setForeground(Color.black);

        // Create a button to trigger sorting
        JButton sortButton = new JButton("Sort");
        sortButton.setBounds(330, 100, 100, 50);
        sortButton.setBackground(colButton);
        sortButton.setFont(new Font("Arial", Font.BOLD, 16));
        sortButton.setForeground(Color.white);

        // Create a label and combo box for editing stats
        JLabel editLabel = new JLabel("Edit:");
        editLabel.setBounds(10, 200, 200, 50);
        editLabel.setFont(new Font("Arial", Font.BOLD, 16));
        editLabel.setForeground(Color.white);

        JComboBox editStats = new JComboBox();
        editStats.getSelectedItem();
        editStats.setBounds(100, 200, 220, 50);
        editStats.setFont(new Font("Arial", Font.BOLD, 16));
        editStats.setForeground(Color.black);

        // Populate the editStats combo box with available student stats
        for (int i = 0; i < editStudentStats.length; i++) {
            editStats.addItem(editStudentStats[i]);
        }

        // Create a button to edit selected stats
        JButton editButton = new JButton("Edit");
        editButton.setBounds(330, 200, 100, 50);
        editButton.setBackground(colButton);
        editButton.setFont(new Font("Arial", Font.BOLD, 16));
        editButton.setForeground(Color.white);

        // Create a label and combo box for undoing changes
        JLabel undoLabel = new JLabel("Undo:");
        undoLabel.setBounds(10, 300, 200, 50);
        undoLabel.setFont(new Font("Arial", Font.BOLD, 16));
        undoLabel.setForeground(Color.white);

        JComboBox undoStats = new JComboBox();
        undoStats.getSelectedItem();
        undoStats.setBounds(100, 300, 220, 50);
        undoStats.setFont(new Font("Arial", Font.BOLD, 16));
        undoStats.setForeground(Color.black);

        // Populate the undoStats combo box with available undo options
        for (int i = 0; i < undoStudentStats.length; i++) {
            undoStats.addItem(undoStudentStats[i]);
        }

        // Create a button to undo changes
        JButton undoButton = new JButton("Undo");
        undoButton.setBounds(330, 300, 100, 50);
        undoButton.setBackground(colButton);
        undoButton.setFont(new Font("Arial", Font.BOLD, 16));
        undoButton.setForeground(Color.white);

        // Create a button to clear the table
        JButton refreshJTable = new JButton("Clear Table");
        refreshJTable.setBounds(280, 400, 150, 80);
        refreshJTable.setBackground(colButton);
        refreshJTable.setFont(new Font("Arial", Font.BOLD, 20));
        refreshJTable.setForeground(Color.white);

        // Create a button to save data
        JButton saveButton = new JButton("Save");
        saveButton.setBounds(20, 400, 120, 80);
        saveButton.setBackground(colButton);
        saveButton.setFont(new Font("Arial", Font.BOLD, 20));
        saveButton.setForeground(Color.white);

        // Create a button to move to the next screen
        JButton nextButton = new JButton("Next");
        nextButton.setBounds(900, 900, 250, 70);
        nextButton.setBackground(colButton);
        nextButton.setFont(new Font("Arial", Font.BOLD, 35));
        nextButton.setForeground(Color.white);

        // Add components to the panel for student actions
        panelStudent.add(searchLabel);
        panelStudent.add(searchField);
        panelStudent.add(searchButton);

        panelStudent.add(nameLabel);
        panelStudent.add(nameField);

        panelStudent.add(heightLabel);
        panelStudent.add(heightField);

        panelStudent.add(ageLabel);
        panelStudent.add(ageField);

        panelStudent.add(gradeLabel);
        panelStudent.add(gradeField);

        panelStudent.add(submitButton);
        panelStudent.add(toggleInput);

        panelStudent.add(deleteLabel);
        panelStudent.add(deleteStats);
        panelStudent.add(deleteButton);

        panelStudent.add(displayButton);
        panelStudent.add(loadData);

        // Add components for actions such as sorting, editing, undoing, and saving
        panelActions.add(enterNewData);
        panelActions.add(sortLabel);
        panelActions.add(sortStats);
        panelActions.add(sortButton);

        panelActions.add(editLabel);
        panelActions.add(editStats);
        panelActions.add(editButton);

        panelActions.add(undoLabel);
        panelActions.add(undoStats);
        panelActions.add(undoButton);

        panelActions.add(refreshJTable);
        panelActions.add(saveButton);

        panelActions.add(newHeightLabel);
        panelActions.add(newHeightField);
        panelActions.add(newAgeLabel);
        panelActions.add(newAgeField);
        panelActions.add(newGradeLabel);
        panelActions.add(newGradeField);
        panelActions.add(updateStatsButton);

        // Add the next button to the menu panel
        panelMenu.add(nextButton);

        // Add panels to the frames (Student and Menu)
        frameStudent.add(panelStudent);
        frameMenu.add(panelMenu);

        // Set visibility for different panels and frames
        panelStudent.setVisible(false);
        panelTable.setVisible(false);
        panelMenu.setVisible(true);
        frameStudent.setVisible(false);
        frameTable.setVisible(false);
        frameMenu.setVisible(true);



        nextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == nextButton) {
                    // Shows the student entry panel and frame while hiding the menu panel and frame
                    panelStudent.setVisible(true);
                    frameStudent.setVisible(true);
                    panelMenu.setVisible(false);
                    frameMenu.setVisible(false);
                }
            }
        });

        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == submitButton) {
                    // Checks if any required fields are empty, shows a warning message if true
                    if (nameField.getText().trim().isEmpty() || heightField.getText().trim().isEmpty() ||
                            ageField.getText().trim().isEmpty() || gradeField.getText().trim().isEmpty()) {
                        JOptionPane.showMessageDialog(submitButton, "Please Enter All Data!", "Input Error", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    String studentName = nameField.getText().trim();

                    // Checks if a student with the same name already exists, shows a warning if found
                    for (int i = 0; i < stats.size(); i++) {
                        if (stats.get(i).getName().equalsIgnoreCase(studentName)) {
                            JOptionPane.showMessageDialog(submitButton, "A student with this name already exists!", "Duplicate Name", JOptionPane.WARNING_MESSAGE);
                            nameField.setText("");
                            return;
                        }
                    }

                    int studentHeight, studentAge, studentGrade, studentOldHeight, studentOldAge, studentOldGrade;

                    try {
                        // Attempts to parse height, age, and grade as integers
                        studentHeight = Integer.parseInt(heightField.getText().trim());
                        studentAge = Integer.parseInt(ageField.getText().trim());
                        studentGrade = Integer.parseInt(gradeField.getText().trim());
                        studentOldHeight = 0;
                        studentOldAge = 0;
                        studentOldGrade = 0;
                    } catch (NumberFormatException ex) {
                        // Displays an error if the inputs cannot be parsed as integers
                        JOptionPane.showMessageDialog(submitButton, "Please enter an integer for height, age, and grade!", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                        heightField.setText("");
                        ageField.setText("");
                        gradeField.setText("");
                        return;
                    }

                    // Adds the new student data to the table and stats list
                    DefaultTableModel model = (DefaultTableModel) jTable.getModel();
                    model.addRow(new Object[]{studentName, studentHeight, studentAge, studentGrade, studentOldHeight, studentOldAge, studentOldGrade});
                    stats.add(new Student(studentName, studentHeight, studentAge, studentGrade, studentOldHeight, studentOldAge, studentOldGrade));

                    // Updates the delete options with the new student's name
                    deleteStats.addItem(studentName);

                    // Displays a success message and clears the fields
                    JOptionPane.showMessageDialog(submitButton, "Student Added Successfully!");
                    nameField.setText("");
                    heightField.setText("");
                    ageField.setText("");
                    gradeField.setText("");

                }
            }
        });

        displayButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Switches to the table view, hides the student panel, and makes the necessary buttons visible
                frameTable.add(panelTable);
                frameStudent.add(panelActions);
                frameStudent.setBounds(150, 200, 550, 750);

                panelStudent.setVisible(false);
                panelTable.setVisible(true);
                panelActions.setVisible(true);

                sortStats.setVisible(true);
                sortButton.setVisible(true);
                editStats.setVisible(true);
                editButton.setVisible(true);
                undoStats.setVisible(true);
                undoButton.setVisible(true);
                enterNewData.setVisible(true);

                frameTable.setVisible(true);

                // Hides the data update labels and fields
                newHeightLabel.setVisible(false);
                newHeightField.setVisible(false);
                newAgeLabel.setVisible(false);
                newAgeField.setVisible(false);
                newGradeLabel.setVisible(false);
                newGradeField.setVisible(false);
                updateStatsButton.setVisible(false);

                // Adds the table component to the panel
                panelTable.add(sp, BorderLayout.CENTER);

                // Clears the input fields after adding data to the table
                String[] rowData = new String[4];
                rowData[0] = nameField.getText();
                rowData[1] = heightField.getText();
                rowData[2] = ageField.getText();
                rowData[3] = gradeField.getText();

                rowDataList.add(rowData);

                nameField.setText("");
                heightField.setText("");
                ageField.setText("");
                gradeField.setText("");

                // Clears table selection
                jTable.clearSelection();
            }
        });

        toggleInput.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Toggles display of CSV data
                if (e.getSource() == toggleInput) {
                    displayStatsInCSV();
                }
            }
        });

        enterNewData.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Resets the view to the student input panel
                panelStudent.setVisible(true);
                panelActions.setVisible(false);
                panelTable.setVisible(false);
                frameTable.setVisible(false);
                frameStudent.setBounds(200, 200, 780, 970);
                frameStudent.setLocationRelativeTo(null);
                submitButton.setEnabled(true);
                nameField.setEnabled(true);
                heightField.setEnabled(true);
                ageField.setEnabled(true);
                gradeField.setEnabled(true);

                // Hides other buttons and fields not needed for data entry
                sortStats.setVisible(false);
                sortButton.setVisible(false);
                editStats.setVisible(false);
                editButton.setVisible(false);
                undoStats.setVisible(false);
                undoButton.setVisible(false);
                enterNewData.setVisible(false);
                newHeightLabel.setVisible(false);
                newHeightField.setVisible(false);
                newAgeLabel.setVisible(false);
                newAgeField.setVisible(false);
                newGradeLabel.setVisible(false);
                newGradeField.setVisible(false);
                updateStatsButton.setVisible(false);

                // Clears input fields and search field
                nameField.setText("");
                heightField.setText("");
                ageField.setText("");
                gradeField.setText("");
                searchField.setText("");
            }
        });

        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == searchButton) {
                    // Searches for the student by name
                    String searchText = searchField.getText().trim();
                    if (searchText.isEmpty()) {
                        JOptionPane.showMessageDialog(frameTable, "Please enter a name to search!", "Input Error", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    // Highlights the row if found
                    boolean rowFound = searchAndHighlightRow(searchText);

                    if (!rowFound) {
                        JOptionPane.showMessageDialog(frameTable, "No name found! Please enter the name of a real student!", "Search Result", JOptionPane.INFORMATION_MESSAGE);
                        searchField.setText("");
                        return;
                    }

                    // Switches to the table view after a successful search
                    frameTable.add(panelTable);
                    frameStudent.add(panelActions);
                    frameStudent.setBounds(150, 200, 550, 750);
                    panelStudent.setVisible(false);
                    panelTable.setVisible(true);
                    panelActions.setVisible(true);
                    sortStats.setVisible(true);
                    sortButton.setVisible(true);
                    editStats.setVisible(true);
                    editButton.setVisible(true);
                    undoStats.setVisible(true);
                    undoButton.setVisible(true);
                    enterNewData.setVisible(true);
                    newHeightLabel.setVisible(false);
                    newHeightField.setVisible(false);
                    newAgeLabel.setVisible(false);
                    newAgeField.setVisible(false);
                    newGradeLabel.setVisible(false);
                    newGradeField.setVisible(false);
                    updateStatsButton.setVisible(false);
                    frameTable.setVisible(true);
                    panelTable.add(sp, BorderLayout.CENTER);
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Deletes the selected student entry from the table, list, and delete combo box
                String selectedName = (String) deleteStats.getSelectedItem();

                if (selectedName == null || selectedName.isEmpty()) {
                    JOptionPane.showMessageDialog(frameTable, "Please select a name to delete!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                DefaultTableModel model = (DefaultTableModel) jTable.getModel();
                int rowToDelete = -1;

                // Finds the row corresponding to the selected name
                for (int row = 0; row < model.getRowCount(); row++) {
                    if (model.getValueAt(row, 0).toString().equalsIgnoreCase(selectedName)) {
                        rowToDelete = row;
                        break;
                    }
                }

                // Deletes the row if found
                if (rowToDelete != -1) {
                    model.removeRow(rowToDelete);
                }

                // Removes the student from the stats and row data lists
                boolean isRemoved = false;
                Iterator<Student> iterator = stats.iterator();
                while (iterator.hasNext()) {
                    if (iterator.next().getName().equalsIgnoreCase(selectedName)) {
                        iterator.remove();
                        isRemoved = true;
                        break;
                    }
                }

                // Removes the row data from the list
                Iterator<String[]> rowIterator = rowDataList.iterator();
                while (rowIterator.hasNext()) {
                    if (rowIterator.next()[0].equalsIgnoreCase(selectedName)) {
                        rowIterator.remove();
                        break;
                    }
                }

                // Displays success or failure message
                if (isRemoved) {
                    JOptionPane.showMessageDialog(frameTable, "Entry deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    updateDeleteStatsComboBox();

                    frameTable.revalidate();
                    frameTable.repaint();
                } else {
                    JOptionPane.showMessageDialog(frameTable, "No matching entry found!", "Delete Failed", JOptionPane.ERROR_MESSAGE);
                }

                // Resets the input fields
                submitButton.setEnabled(true);
                nameField.setEnabled(true);
                heightField.setEnabled(true);
                ageField.setEnabled(true);
                gradeField.setEnabled(true);
                nameField.setText("");
                heightField.setText("");
                ageField.setText("");
                gradeField.setText("");
                searchField.setText("");
            }
        });

        sortButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Sorts the student data based on selected criteria (height, age, grade)
                int currentIndex = sortStats.getSelectedIndex();
                DefaultTableModel model = (DefaultTableModel) jTable.getModel();
                model.setRowCount(0);

                // Sorts based on the selected index
                if (currentIndex == 0) {
                    insertionSortHeight(stats);
                } else if (currentIndex == 1) {
                    insertionSortAge(stats);
                } else if (currentIndex == 2) {
                    insertionSortGrade(stats);
                }

                // Re-populates the table with sorted data
                for (int i = 0; i < stats.size(); i++) {
                    Student student = stats.get(i);
                    model.addRow(new Object[]{student.getName(), student.getHeight(), student.getAge(), student.getGrade(), student.getHeight(), student.getOldAge(), student.getOldGrade()});
                }
            }
        });


        // Action listener for the "editButton" which allows users to edit student information
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Initially hide the "Update Stats" button and fields for height, age, and grade
                updateStatsButton.setVisible(false);
                newHeightLabel.setVisible(false);
                newHeightField.setVisible(false);
                newAgeLabel.setVisible(false);
                newAgeField.setVisible(false);
                newGradeLabel.setVisible(false);
                newGradeField.setVisible(false);

                // Prompt the user to enter the student's name
                String sr = JOptionPane.showInputDialog(frameStudent, "Enter the student's name:");

                // If no name is entered, show an error message and hide the fields again
                if (sr == null || sr.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(frameStudent, "No name entered!", "Error", JOptionPane.ERROR_MESSAGE);

                    updateStatsButton.setVisible(false);
                    newHeightLabel.setVisible(false);
                    newHeightField.setVisible(false);
                    newAgeLabel.setVisible(false);
                    newAgeField.setVisible(false);
                    newGradeLabel.setVisible(false);
                    newGradeField.setVisible(false);
                    return;
                }

                // Search for the student in the "stats" list by comparing names
                index = -1;
                for (int i = 0; i < stats.size(); i++) {
                    if (sr.equalsIgnoreCase(stats.get(i).getName())) {
                        index = i;
                        break;
                    }
                }

                // If the student is found, show the fields to allow the user to update information
                if (index != -1) {
                    // Save the old values of height, age, and grade
                    stats.get(index).setOldHeight(stats.get(index).getHeight());
                    stats.get(index).setOldAge(stats.get(index).getAge());
                    stats.get(index).setOldGrade(stats.get(index).getGrade());

                    // Set the text fields with the current values of the selected student's data
                    newHeightField.setText(String.valueOf(stats.get(index).getHeight()));
                    newAgeField.setText(String.valueOf(stats.get(index).getAge()));
                    newGradeField.setText(String.valueOf(stats.get(index).getGrade()));

                    // Set the "editStats" dropdown visible and select the first option
                    editStats.setVisible(true);
                    editStats.setSelectedIndex(0);

                    // Show the "Update Stats" button and only the height field for editing
                    updateStatsButton.setVisible(true);
                    newHeightLabel.setVisible(true);
                    newHeightField.setVisible(true);
                    newAgeLabel.setVisible(false);
                    newAgeField.setVisible(false);
                    newGradeLabel.setVisible(false);
                    newGradeField.setVisible(false);
                } else {
                    // Show an error if the student was not found
                    JOptionPane.showMessageDialog(frameStudent, "Student not found!", "Error", JOptionPane.ERROR_MESSAGE);
                    updateStatsButton.setVisible(false);
                    newHeightLabel.setVisible(false);
                    newHeightField.setVisible(false);
                    newAgeLabel.setVisible(false);
                    newAgeField.setVisible(false);
                    newGradeLabel.setVisible(false);
                    newGradeField.setVisible(false);
                }
            }
        });

        // Action listener for the "editStats" dropdown menu to select which student data to edit (height, age, or grade)
        editStats.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the selected option from the dropdown
                String selectedOption = (String) editStats.getSelectedItem();

                // Display the appropriate input field based on the selected option
                if ("Height (enter then select)".equalsIgnoreCase(selectedOption)) {
                    updateStatsButton.setVisible(true);
                    newHeightLabel.setVisible(true);
                    newHeightField.setVisible(true);
                    newAgeLabel.setVisible(false);
                    newAgeField.setVisible(false);
                    newGradeLabel.setVisible(false);
                    newGradeField.setVisible(false);
                } else if ("Age (enter then select)".equalsIgnoreCase(selectedOption)) {
                    updateStatsButton.setVisible(true);
                    newHeightLabel.setVisible(false);
                    newHeightField.setVisible(false);
                    newAgeLabel.setVisible(true);
                    newAgeField.setVisible(true);
                    newGradeLabel.setVisible(false);
                    newGradeField.setVisible(false);
                } else if ("Grade (enter then select)".equalsIgnoreCase(selectedOption)) {
                    updateStatsButton.setVisible(true);
                    newHeightLabel.setVisible(false);
                    newHeightField.setVisible(false);
                    newAgeLabel.setVisible(false);
                    newAgeField.setVisible(false);
                    newGradeLabel.setVisible(true);
                    newGradeField.setVisible(true);
                }
            }
        });

        // Action listener for the "updateStatsButton" to save the updated information for the selected student
        updateStatsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // If no student is selected, show an error message and return
                if (index == -1) {
                    JOptionPane.showMessageDialog(frameStudent, "No student selected!", "Error", JOptionPane.ERROR_MESSAGE);
                    updateStatsButton.setVisible(false);
                    newHeightLabel.setVisible(false);
                    newHeightField.setVisible(false);
                    newAgeLabel.setVisible(false);
                    newAgeField.setVisible(false);
                    newGradeLabel.setVisible(false);
                    newGradeField.setVisible(false);
                    return;
                }

                // Get the selected option (height, age, or grade) from the dropdown
                String selectedOption = (String) editStats.getSelectedItem();
                DefaultTableModel model = (DefaultTableModel) jTable.getModel();

                // Update the corresponding field based on the selected option
                if ("Height (enter then select)".equalsIgnoreCase(selectedOption)) {
                    try {
                        int newHeight = Integer.parseInt(newHeightField.getText().trim());
                        stats.get(index).setHeight(newHeight);
                        model.setValueAt(newHeight, index, 1);

                        lastUpdatedField = "Height (enter then select)";
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frameStudent, "Invalid height entered! Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } else if ("Age (enter then select)".equalsIgnoreCase(selectedOption)) {
                    try {
                        int newAge = Integer.parseInt(newAgeField.getText().trim());
                        stats.get(index).setAge(newAge);
                        model.setValueAt(newAge, index, 2);

                        lastUpdatedField = "Age (enter then select)";
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frameStudent, "Invalid age entered! Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } else if ("Grade (enter then select)".equalsIgnoreCase(selectedOption)) {
                    try {
                        int newGrade = Integer.parseInt(newGradeField.getText().trim());
                        stats.get(index).setGrade(newGrade);
                        model.setValueAt(newGrade, index, 3);

                        lastUpdatedField = "Grade (enter then select)";
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frameStudent, "Invalid grade entered! Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                // Show a success message after updating
                JOptionPane.showMessageDialog(frameStudent, "Student information updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

                // Hide the update button and input fields after the update
                updateStatsButton.setVisible(false);
                newHeightLabel.setVisible(false);
                newHeightField.setVisible(false);
                newAgeLabel.setVisible(false);
                newAgeField.setVisible(false);
                newGradeLabel.setVisible(false);
                newGradeField.setVisible(false);
                frameStudent.revalidate();
                frameStudent.repaint();

            }
        });

        // Action listener for the "undoButton" to revert the last change made to a student's information
        undoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Prompt the user to enter the student's name
                String sr = JOptionPane.showInputDialog(frameStudent, "Enter the student's name:");

                // If no name is entered, show an error message and return
                if (sr == null || sr.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(frameStudent, "No name entered!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Search for the student in the "stats" list
                index = -1;
                for (int i = 0; i < stats.size(); i++) {
                    if (sr.equalsIgnoreCase(stats.get(i).getName())) {
                        index = i;
                        break;
                    }
                }

                // If the student is not found, show an error message
                if (index == -1) {
                    JOptionPane.showMessageDialog(frameStudent, "Student not found!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Get the selected stat (height, age, or grade) to undo
                String selectedStat = (String) undoStats.getSelectedItem();
                DefaultTableModel model = (DefaultTableModel) jTable.getModel();

                // Revert the selected stat to its original value
                if ("Height (select then enter)".equalsIgnoreCase(selectedStat)) {
                    int originalHeight = stats.get(index).getOldHeight();
                    stats.get(index).setHeight(originalHeight);
                    model.setValueAt(originalHeight, index, 1);
                    JOptionPane.showMessageDialog(frameStudent, "Height change undone successfully!", "Undo Success", JOptionPane.INFORMATION_MESSAGE);
                } else if ("Age (select then enter)".equalsIgnoreCase(selectedStat)) {
                    int originalAge = stats.get(index).getOldAge();
                    stats.get(index).setAge(originalAge);
                    model.setValueAt(originalAge, index, 2);
                    JOptionPane.showMessageDialog(frameStudent, "Age change undone successfully!", "Undo Success", JOptionPane.INFORMATION_MESSAGE);
                } else if ("Grade (select then enter)".equalsIgnoreCase(selectedStat)) {
                    int originalGrade = stats.get(index).getOldGrade();
                    stats.get(index).setGrade(originalGrade);
                    model.setValueAt(originalGrade, index, 3);
                    JOptionPane.showMessageDialog(frameStudent, "Grade change undone successfully!", "Undo Success", JOptionPane.INFORMATION_MESSAGE);
                }

                model.fireTableRowsUpdated(index, index);

                // Repaint the frame to reflect the changes
                frameStudent.revalidate();
                frameStudent.repaint();

            }
        });

        // Action listener to refresh the table by clearing all data and resetting the list of stats
        refreshJTable.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.setRowCount(0);
                stats.clear();
                deleteStats.removeAllItems();
            }
        });

        // Action listener for the "saveButton" to save data to CSV
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveWriteToCSV(jTable);
            }
        });

        // Action listener for the "loadData" button to load data from a CSV file
        loadData.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                File file = new File("IAliveFile.csv");
                loadCSVData(stats, model, file);
                displayLoadStatsInCSV();
            }
        });

        // Action listener for the frame window closing event to save data before exiting
        frameTable.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
            }
        });
    }

    // Saves the current student stats and JTable data to a CSV file
    public static void saveStatsToCSV(ArrayList<Student> stats, String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {

            // Gets the table model from the JTable
            DefaultTableModel model = (DefaultTableModel) jTable.getModel();

            // Loop through each row in the table
            for (int row = 0; row < model.getRowCount(); row++) {
                // Loop through each column in the current row
                for (int col = 0; col < model.getColumnCount(); col++) {
                    Object value = model.getValueAt(row, col);
                    String cellValue = (value != null) ? value.toString() : "";

                    // Escape special CSV characters: comma, newline, or quote
                    if (cellValue.contains(",") || cellValue.contains("\n") || cellValue.contains("\"")) {
                        cellValue = "\"" + cellValue.replace("\"", "\"\"") + "\"";
                    }

                    writer.write(cellValue);

                    // Add comma if it's not the last column
                    if (col < model.getColumnCount() - 1) {
                        writer.write(",");
                    }
                }
                writer.write("\n");
            }

            // Iterate through the Student objects (but does not write them to the file)
            for (Student student : stats) {

                // Get all data fields from the student object
                String name = student.getName();
                int height = Integer.parseInt(String.valueOf(student.getHeight()));
                int age = Integer.parseInt(String.valueOf(student.getAge()));
                int grade = Integer.parseInt(String.valueOf(student.getGrade()));
                int oldHeight = Integer.parseInt(String.valueOf(student.getOldHeight()));
                int oldAge = Integer.parseInt(String.valueOf(student.getOldAge()));
                int oldGrade = Integer.parseInt(String.valueOf(student.getOldGrade()));

                String row = name + "," + height + "," + age + "," + grade + ","
                        + oldHeight + "," + oldAge + "," + oldGrade;
            }

            // Show confirmation message on success
            JOptionPane.showMessageDialog(null, "Data saved to " + fileName);
        } catch (IOException ex) {
            ex.printStackTrace();
            // Show error message if file writing fails
            JOptionPane.showMessageDialog(null, "Error saving data.");
        }
    }

    // Helper method that saves data to a specific file (IAliveFile.csv)
    public static void saveWriteToCSV(JTable jTable) {
        saveStatsToCSV(stats, "IAliveFile.csv");
    }


    // Loads data from a CSV file into the table model and avoids duplicates based on student names.
    public static void loadCSVData(ArrayList<Student> stats, DefaultTableModel model, File filePath) {
        BufferedReader reader = null;

        try {
            model.setRowCount(0);

            // Add all current student data to the table
            for (Student student : stats) {
                Object[] row = new Object[7];
                row[0] = student.getName();
                row[1] = student.getHeight();
                row[2] = student.getAge();
                row[3] = student.getGrade();
                row[4] = student.getOldHeight();
                row[5] = student.getOldAge();
                row[6] = student.getOldGrade();
                model.addRow(row);
            }

            // Read data from CSV file
            reader = new BufferedReader(new FileReader(filePath));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");

                // Only consider rows that match the expected column count
                if (row.length == model.getColumnCount()) {
                    boolean alreadyAdded = false;

                    // Check if student with same name is already in stats
                    for (Student student : stats) {
                        if (student.getName().equals(row[0])) {
                            alreadyAdded = true;
                            break;
                        }
                    }

                    // If not already added, add row from file to the table
                    if (!alreadyAdded) {
                        model.addRow(row);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Close the reader safely
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    // Loads student stats from `IAFile.csv` and updates or adds them to the `stats` list.
    public static void displayStatsInCSV() {
        deleteStats.removeAllItems();

        String file = "IAFile.csv";
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(file));
            String line2;

            // Create a map of existing students for quick lookup by name
            HashMap<String, Student> existingStats = new HashMap<>();
            for (Student s : stats) {
                existingStats.put(s.getName(), s);
            }

            while ((line2 = reader.readLine()) != null) {
                String[] row = line2.split(",");

                if (row.length != 7) {
                    continue;
                }

                try {
                    String name = row[0];
                    int height = Integer.parseInt(row[1]);
                    int age = Integer.parseInt(row[2]);
                    int grade = Integer.parseInt(row[3]);
                    int oldHeight = Integer.parseInt(row[4]);
                    int oldAge = Integer.parseInt(row[5]);
                    int oldGrade = Integer.parseInt(row[6]);

                    // Update existing student or add a new one
                    if (existingStats.containsKey(name)) {
                        Student existingStudent = existingStats.get(name);
                        existingStudent.setHeight(height);
                        existingStudent.setAge(age);
                        existingStudent.setGrade(grade);
                        existingStudent.setOldHeight(oldHeight);
                        existingStudent.setOldAge(oldAge);
                        existingStudent.setOldGrade(oldGrade);
                    } else {
                        stats.add(new Student(name, height, age, grade, oldHeight, oldAge, oldGrade));
                    }

                } catch (NumberFormatException ignored) {
                    // Skip rows with invalid number formatting
                }
            }

        } catch (IOException x) {
            x.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException x) {
                    x.printStackTrace();
                }
            }
        }

        // Refresh delete combo box and table
        deleteStats.removeAllItems();
        for (Student s : stats) {
            deleteStats.addItem(s.getName());
        }
        updateTableData();
    }


    // Similar to displayStatsInCSV() but loads from a different file: `IAliveFile.csv`
    public static void displayLoadStatsInCSV() {
        deleteStats.removeAllItems();

        String file = "IAliveFile.csv";
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(file));
            String line2;

            // Build a lookup map for existing students
            HashMap<String, Student> existingStats = new HashMap<>();
            for (Student s : stats) {
                existingStats.put(s.getName(), s);
            }

            while ((line2 = reader.readLine()) != null) {
                String[] row = line2.split(",");

                if (row.length != 7) {
                    continue;
                }

                try {
                    String name = row[0];
                    int height = Integer.parseInt(row[1]);
                    int age = Integer.parseInt(row[2]);
                    int grade = Integer.parseInt(row[3]);
                    int oldHeight = Integer.parseInt(row[4]);
                    int oldAge = Integer.parseInt(row[5]);
                    int oldGrade = Integer.parseInt(row[6]);

                    // Update or add student data
                    if (existingStats.containsKey(name)) {
                        Student existingStudent = existingStats.get(name);
                        existingStudent.setHeight(height);
                        existingStudent.setAge(age);
                        existingStudent.setGrade(grade);
                        existingStudent.setOldHeight(oldHeight);
                        existingStudent.setOldAge(oldAge);
                        existingStudent.setOldGrade(oldGrade);
                    } else {
                        stats.add(new Student(name, height, age, grade, oldHeight, oldAge, oldGrade));
                    }

                } catch (NumberFormatException _) {

                }
            }

        } catch (IOException x) {
            x.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException x) {
                    x.printStackTrace();
                }
            }
        }

        // Repopulate delete combo box and update the table display
        deleteStats.removeAllItems();
        for (Student s : stats) {
            deleteStats.addItem(s.getName());
        }
        updateTableData();
    }


    // Sorts the list of students in ascending order based on height using insertion sort
    public static void insertionSortHeight(ArrayList<Student> stats) {
        for (int i = 1; i < stats.size(); i++) {
            // Store the values of the current student
            String currentName = stats.get(i).getName();
            int currentHeight = stats.get(i).getHeight();
            int currentAge = stats.get(i).getAge();
            int currentGrade = stats.get(i).getGrade();
            int j = i - 1;

            // Shift students with greater height to the right
            while (j >= 0 && stats.get(j).getHeight() > currentHeight) {
                stats.get(j + 1).name = stats.get(j).getName();
                stats.get(j + 1).height = stats.get(j).getHeight();
                stats.get(j + 1).age = stats.get(j).getAge();
                stats.get(j + 1).grade = stats.get(j).getGrade();
                j--;
            }

            // Insert the current student in the correct position
            stats.get(j + 1).name = currentName;
            stats.get(j + 1).height = currentHeight;
            stats.get(j + 1).age = currentAge;
            stats.get(j + 1).grade = currentGrade;
        }
    }

    // Sorts the list of students in ascending order based on age using insertion sort
    public static void insertionSortAge(ArrayList<Student> stats) {
        for (int i = 1; i < stats.size(); i++) {
            String currentName = stats.get(i).getName();
            int currentHeight = stats.get(i).getHeight();
            int currentAge = stats.get(i).getAge();
            int currentGrade = stats.get(i).getGrade();
            int j = i - 1;

            while (j >= 0 && stats.get(j).getAge() > currentAge) {
                stats.get(j + 1).name = stats.get(j).getName();
                stats.get(j + 1).height = stats.get(j).getHeight();
                stats.get(j + 1).age = stats.get(j).getAge();
                stats.get(j + 1).grade = stats.get(j).getGrade();
                j--;
            }

            stats.get(j + 1).name = currentName;
            stats.get(j + 1).height = currentHeight;
            stats.get(j + 1).age = currentAge;
            stats.get(j + 1).grade = currentGrade;
        }
    }

    // Sorts the list of students in ascending order based on grade using insertion sort
    public static void insertionSortGrade(ArrayList<Student> stats) {
        for (int i = 1; i < stats.size(); i++) {
            String currentName = stats.get(i).getName();
            int currentHeight = stats.get(i).getHeight();
            int currentAge = stats.get(i).getAge();
            int currentGrade = stats.get(i).getGrade();
            int j = i - 1;

            while (j >= 0 && stats.get(j).getGrade() > currentGrade) {
                stats.get(j + 1).name = stats.get(j).getName();
                stats.get(j + 1).height = stats.get(j).getHeight();
                stats.get(j + 1).age = stats.get(j).getAge();
                stats.get(j + 1).grade = stats.get(j).getGrade();
                j--;
            }

            stats.get(j + 1).name = currentName;
            stats.get(j + 1).height = currentHeight;
            stats.get(j + 1).age = currentAge;
            stats.get(j + 1).grade = currentGrade;
        }
    }

    // Searches for a student by name in the table, highlights and scrolls to the row if found
    public static boolean searchAndHighlightRow(String searchText) {
        for (int row = 0; row < model.getRowCount(); row++) {
            String name = model.getValueAt(row, 0).toString();

            if (name.equalsIgnoreCase(searchText)) {
                jTable.setRowSelectionInterval(row, row);
                jTable.scrollRectToVisible(new Rectangle(jTable.getCellRect(row, 0, true)));
                return true;
            }
        }
        return false;
    }

    // Updates the deleteStats combo box with all student names from the table
    public static void updateDeleteStatsComboBox() {
        deleteStats.removeAllItems();
        DefaultTableModel model = (DefaultTableModel) jTable.getModel();

        for (int row = 0; row < model.getRowCount(); row++) {
            String studentName = model.getValueAt(row, 0).toString();
            deleteStats.addItem(studentName);
        }
    }

    // Refreshes the table data using the current student list
    public static void updateTableData() {
        DefaultTableModel model = (DefaultTableModel) jTable.getModel();
        model.setRowCount(0);

        for (int i = 0; i < stats.size(); i++) {
            Student student = stats.get(i);
            model.addRow(new Object[] {
                    student.getName(),
                    student.getHeight(),
                    student.getAge(),
                    student.getGrade(),
                    student.getOldHeight(),
                    student.getOldAge(),
                    student.getOldGrade()
            });
        }
    }

    // Hides the last three columns of the table (likely oldHeight, oldAge, oldGrade)
    public static void hideLastThreeColumns(JTable jTable) {
        TableColumnModel columnModel = jTable.getColumnModel();
        int columnCount = columnModel.getColumnCount();

        for (int i = columnCount - 3; i < columnCount; i++) {
            TableColumn column = columnModel.getColumn(i);
            column.setMinWidth(0);
            column.setMaxWidth(0);
            column.setPreferredWidth(0); // Effectively hides the column
        }
    }
}