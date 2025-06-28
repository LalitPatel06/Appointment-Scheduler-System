import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import java.sql.Statement;     // for createStatement()
import java.sql.Date;          // for java.sql.Date from resultSet.getDate()


public class Admin {

   

    public void showAdminDashboard(Scanner sc) {
    int choice = -1;

    do {
         System.out.println("\n=== Admin Dashboard ===");
            System.out.println("1. View All Appointments");
            System.out.println("2. Add Appointment");
            System.out.println("3. Cancel Appointment");
            System.out.println("4. Update Appointment");
            System.out.println("5. Search Appointment");
            System.out.println("6. Exit");
        boolean validInput = false;
        while (!validInput) {
            System.out.print("Enter your choice (1-6): ");
            String input = sc.nextLine().trim();

            try {
                choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= 6) {
                    validInput = true; // valid input
                } else {
                    System.out.println("Please enter a number between 1 and 6.");
                }
            } catch (NumberFormatException e) {
                System.out.println(" Invalid input. Please enter digits like 1, 2, 3, 4, 5 or 6.");
            }
        }

         switch (choice) {
                case 1:
                    viewAppointments();
                    break;

                case 2:
                    bookAppointment(sc);
                    break;

                case 3:
                    cancelAppointment(sc); 
                    break;

                case 4:
                   updateAppointment(sc); 
                   break;

                case 5:
                   searchAppointment(sc);
                   break;
                
                case 6:
                    System.out.println("Exiting Admin Dashboard.");
                    break;

                default:
                    System.out.println("Invalid choice. Try again.");
            }

        } while (choice != 6);
}

       private void bookAppointment(Scanner sc) {
    try {
        // Validate name
        String name = "";
        while (true) {
            System.out.print("Enter your name: ");
            name = sc.nextLine().trim();

            if (!name.matches("^[a-zA-Z]{3,20}$")) {
                System.out.println(" Name should be 3 to 20 characters and only contain letters.");
            } else {
                break;
            }
        }

        // Step 1: Show list of problems
        String[] problems = DoctorHelper.getAllProblems();
        System.out.println("Select your problem:");
        for (int i = 0; i < problems.length; i++) {
            System.out.println((i + 1) + ". " + problems[i]);
        }

        int problemChoice = -1;
        while (true) {
            System.out.print("Enter your choice (1 to " + problems.length + "): ");
            String input = sc.nextLine().trim();
            try {
                problemChoice = Integer.parseInt(input);
                if (problemChoice >= 1 && problemChoice <= problems.length) break;
                else System.out.println(" Please enter a number between 1 and " + problems.length + ".");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Enter a digit like 1, 2, 3...");
            }
        }

        String problem = problems[problemChoice - 1];

        // Step 2: Get doctor list
        String[] doctors = DoctorHelper.getDoctorsForProblem(problem);
        if (doctors.length == 0) {
            System.out.println("No doctors found for this problem type.");
            return;
        }

        System.out.println("Available doctors for " + problem + ":");
        for (int i = 0; i < doctors.length; i++) {
            System.out.println((i + 1) + ". " + doctors[i]);
        }

        String selectedDoctor = doctors[0]; // default
        while (true) {
            System.out.print("Enter doctor choice (or press Enter for default): ");
            String input = sc.nextLine().trim();

            if (input.isEmpty()) break;

            try {
                int index = Integer.parseInt(input);
                if (index >= 1 && index <= doctors.length) {
                    selectedDoctor = doctors[index - 1];
                    break;
                } else {
                    System.out.println(" Please enter a valid number between 1 and " + doctors.length);
                }
            } catch (NumberFormatException e) {
                System.out.println(" Invalid input. Enter digit only.");
            }
        }

        // Step 3: Get current date
        LocalDate currentDate = LocalDate.now();
        String date = currentDate.toString();

        // Step 4: Generate time slots from 10:00 AM to 3:00 PM
        ArrayList<String> allSlots = new ArrayList<>();
        LocalTime slotStart = LocalTime.of(10, 0);
        LocalTime slotEnd = LocalTime.of(15, 0);
        DateTimeFormatter dbFormat = DateTimeFormatter.ofPattern("HH:mm:ss");

        while (slotStart.isBefore(slotEnd)) {
            allSlots.add(slotStart.format(dbFormat));
            slotStart = slotStart.plusMinutes(30);
        }

        // Step 5: Fetch already booked slots
        Connection conn = DBConnection.getConnection();
        String query = "SELECT time FROM appointments WHERE doctor_name = ? AND date = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, selectedDoctor);
        ps.setString(2, date);
        ResultSet rs = ps.executeQuery();

        ArrayList<String> bookedSlots = new ArrayList<>();
        while (rs.next()) {
            bookedSlots.add(rs.getString("time"));
        }

        // Step 6: Filter available slots
        ArrayList<String> availableSlots = new ArrayList<>();
        for (String slot : allSlots) {
            if (!bookedSlots.contains(slot)) {
                availableSlots.add(slot);
            }
        }

        if (availableSlots.isEmpty()) {
            System.out.println("No available slots for " + selectedDoctor + " today.");
            return;
        }

        // Step 7: Display available time slots
        System.out.println("Available Time Slots for " + selectedDoctor + " on " + date + ":");
        DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
        DateTimeFormatter displayFormat = DateTimeFormatter.ofPattern("hh:mm a");

        for (int i = 0; i < availableSlots.size(); i++) {
            String startStr = availableSlots.get(i);
            LocalTime startTime = LocalTime.parse(startStr, inputFormat);
            LocalTime endTime = startTime.plusMinutes(30);
            System.out.println((i + 1) + ". " + startTime.format(displayFormat) + " to " + endTime.format(displayFormat));
        }

        // Select time slot
        int slotChoice = -1;
        String chosenSlot = "";
        while (true) {
            System.out.print("Choose your time slot (1 to " + availableSlots.size() + "): ");
            String input = sc.nextLine().trim();
            try {
                slotChoice = Integer.parseInt(input);
                if (slotChoice >= 1 && slotChoice <= availableSlots.size()) {
                    chosenSlot = availableSlots.get(slotChoice - 1);
                    break;
                } else {
                    System.out.println(" Please enter a valid time slot number.");
                }
            } catch (NumberFormatException e) {
                System.out.println(" Invalid input. Enter a number.");
            }
        }

        // Step 8: Insert appointment
        String insert = "INSERT INTO appointments (name, date, time, description, doctor_name) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement insertStmt = conn.prepareStatement(insert);
        insertStmt.setString(1, name);
        insertStmt.setString(2, date);
        insertStmt.setString(3, chosenSlot);
        insertStmt.setString(4, problem);
        insertStmt.setString(5, selectedDoctor);

        int rows = insertStmt.executeUpdate();
        if (rows > 0) {
            LocalTime bookedStart = LocalTime.parse(chosenSlot, inputFormat);
            LocalTime bookedEnd = bookedStart.plusMinutes(30);

            System.out.println("\nAppointment Confirmed!");
            System.out.println("Name     : " + name);
            System.out.println("Date     : " + date);
            System.out.println("Time     : " + bookedStart.format(displayFormat) + " to " + bookedEnd.format(displayFormat));
            System.out.println("Problem  : " + problem);
            System.out.println("Doctor   : " + selectedDoctor);
        } else {
            System.out.println(" Failed to book appointment.");
        }

        conn.close();

    } catch (Exception e) {
        System.out.println("Error during booking: " + e.getMessage());
    }
}

    private void viewAppointments() {
    try {
        Connection conn = DBConnection.getConnection();
        String sql = "SELECT * FROM appointments";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        System.out.println("\n--- All Appointments ---");
        System.out.println("-------------------------------------------------------------");
        System.out.printf("%-4s %-15s %-12s %-10s %-12s %-15s%n", "ID", "Name", "Date", "Time", "Problem", "Doctor");
        System.out.println("-------------------------------------------------------------");

        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("hh:mm a");

        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            Date date = rs.getDate("date");
            Time time = rs.getTime("time");
            String problem = rs.getString("description");
            String doctor = rs.getString("doctor_name");

            String formattedTime = time.toLocalTime().format(timeFormat);

            System.out.printf("%-4d %-15s %-12s %-10s %-12s %-15s%n", id, name, date, formattedTime, problem, doctor);
        }

        System.out.println("-------------------------------------------------------------");

        conn.close();
    } catch (Exception e) {
        System.out.println("Error while fetching appointments: " + e.getMessage());
    }
}

private void cancelAppointment(Scanner sc) {
    try {
        System.out.println("\n--- Cancel Appointment ---");

        // Step 1: Show all appointments
        viewAppointments();

        Connection conn = DBConnection.getConnection();
        int id = -1;
        ResultSet rs = null;

        // Step 2: Keep prompting until valid ID found
        while (true) {
            System.out.print("Enter Appointment ID to cancel: ");
            String input = sc.nextLine().trim();

            try {
                id = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println(" Invalid input. Please enter a valid numeric Appointment ID.");
                continue;
            }

            String fetchSql = "SELECT * FROM appointments WHERE id = ?";
            PreparedStatement fetchStmt = conn.prepareStatement(fetchSql);
            fetchStmt.setInt(1, id);
            rs = fetchStmt.executeQuery();

            if (rs.next()) {
                break; // ID found
            } else {
                System.out.println(" No appointment found with ID: " + id + ". Please enter a correct Appointment ID.");
            }
        }

        // Step 3: Show details to confirm
        String name = rs.getString("name");
        String date = rs.getString("date");
        String time = rs.getString("time");
        String problem = rs.getString("description");
        String doctor = rs.getString("doctor_name");

        System.out.println("\nAppointment Details:");
        System.out.println("Name    : " + name);
        System.out.println("Date    : " + date);
        System.out.println("Time    : " + time);
        System.out.println("Problem : " + problem);
        System.out.println("Doctor  : " + doctor);

        // Step 4: Ask for confirmation
        System.out.print("Are you sure you want to cancel this appointment? (yes/no): ");
        String confirm = sc.nextLine().trim().toLowerCase();

        if (!confirm.equals("yes")) {
            System.out.println(" Appointment cancellation aborted.");
            return;
        }

        // Step 5: Delete appointment
        String deleteSql = "DELETE FROM appointments WHERE id = ?";
        PreparedStatement deleteStmt = conn.prepareStatement(deleteSql);
        deleteStmt.setInt(1, id);

        int rows = deleteStmt.executeUpdate();
        if (rows > 0) {
            System.out.println(" Appointment cancelled successfully.");
        } else {
            System.out.println(" Cancellation failed.");
        }

        conn.close();

    } catch (Exception e) {
        System.out.println(" Error while cancelling appointment: " + e.getMessage());
    }
}
private void updateAppointment(Scanner sc) {
    try {
        System.out.println("\n--- Update Appointment ---");

        // Step 1: Show existing appointments
        viewAppointments();

        // Step 2: Ask for valid appointment ID
        Connection conn = DBConnection.getConnection();
        int id = -1;
        ResultSet rs = null;

        while (true) {
            System.out.print("Enter Appointment ID to update: ");
            String idInput = sc.nextLine().trim();
            try {
                id = Integer.parseInt(idInput);
            } catch (NumberFormatException e) {
                System.out.println(" Please enter a valid numeric Appointment ID.");
                continue;
            }

            String fetchSql = "SELECT * FROM appointments WHERE id = ?";
            PreparedStatement fetchStmt = conn.prepareStatement(fetchSql);
            fetchStmt.setInt(1, id);
            rs = fetchStmt.executeQuery();

            if (rs.next()) {
                break;
            } else {
                System.out.println(" No appointment found with ID: " + id);
            }
        }

        String name = rs.getString("name");
        String currentProblem = rs.getString("description");
        String currentDoctor = rs.getString("doctor_name");
        String currentTime = rs.getString("time");
        String currentDate = rs.getString("date");

        System.out.println("\nCurrent Appointment:");
        System.out.println("Name    : " + name);
        System.out.println("Date    : " + currentDate);
        System.out.println("Time    : " + currentTime);
        System.out.println("Problem : " + currentProblem);
        System.out.println("Doctor  : " + currentDoctor);

        // Step 3: Update problem
        System.out.println("\nSelect new problem (or press Enter to keep same):");
        String[] problems = DoctorHelper.getAllProblems();
        for (int i = 0; i < problems.length; i++) {
            System.out.println((i + 1) + ". " + problems[i]);
        }

        String problemInput;
        String newProblem = currentProblem;
        while (true) {
            problemInput = sc.nextLine().trim();
            if (problemInput.isEmpty()) break;
            try {
                int problemChoice = Integer.parseInt(problemInput);
                if (problemChoice >= 1 && problemChoice <= problems.length) {
                    newProblem = problems[problemChoice - 1];
                    break;
                } else {
                    System.out.println(" Enter a number between 1 and " + problems.length + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println(" Please enter a valid numeric choice.");
            }
        }

        // Step 4: Update doctor
        String[] doctors = DoctorHelper.getDoctorsForProblem(newProblem);
        System.out.println("Available doctors for " + newProblem + ":");
        for (int i = 0; i < doctors.length; i++) {
            System.out.println((i + 1) + ". " + doctors[i]);
        }

        String newDoctor = currentDoctor;
        while (true) {
            System.out.print("Enter doctor choice (or press Enter to keep " + currentDoctor + "): ");
            String doctorInput = sc.nextLine().trim();
            if (doctorInput.isEmpty()) break;
            try {
                int doctorChoice = Integer.parseInt(doctorInput);
                if (doctorChoice >= 1 && doctorChoice <= doctors.length) {
                    newDoctor = doctors[doctorChoice - 1];
                    break;
                } else {
                    System.out.println(" Enter a number between 1 and " + doctors.length + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println(" Please enter a valid numeric choice.");
            }
        }

        // Step 5: Update time slot
        ArrayList<String> allSlots = new ArrayList<>();
        LocalTime start = LocalTime.of(10, 0);
        LocalTime end = LocalTime.of(15, 0);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm:ss");

        while (start.isBefore(end)) {
            allSlots.add(start.format(format));
            start = start.plusMinutes(30);
        }

        String bookedSql = "SELECT time FROM appointments WHERE doctor_name = ? AND date = ? AND id != ?";
        PreparedStatement ps = conn.prepareStatement(bookedSql);
        ps.setString(1, newDoctor);
        ps.setString(2, currentDate);
        ps.setInt(3, id);
        ResultSet bookedRs = ps.executeQuery();

        ArrayList<String> bookedSlots = new ArrayList<>();
        while (bookedRs.next()) {
            bookedSlots.add(bookedRs.getString("time"));
        }

        ArrayList<String> availableSlots = new ArrayList<>();
        for (String slot : allSlots) {
            if (!bookedSlots.contains(slot)) {
                availableSlots.add(slot);
            }
        }

        System.out.println("Available slots:");
        DateTimeFormatter displayFormat = DateTimeFormatter.ofPattern("hh:mm a");
        for (int i = 0; i < availableSlots.size(); i++) {
            LocalTime sTime = LocalTime.parse(availableSlots.get(i));
            LocalTime eTime = sTime.plusMinutes(30);
            System.out.println((i + 1) + ". " + sTime.format(displayFormat) + " to " + eTime.format(displayFormat));
        }

        String newTime = currentTime;
        while (true) {
            System.out.print("Enter time slot number (or press Enter to keep " + currentTime + "): ");
            String timeInput = sc.nextLine().trim();
            if (timeInput.isEmpty()) break;
            try {
                int slotChoice = Integer.parseInt(timeInput);
                if (slotChoice >= 1 && slotChoice <= availableSlots.size()) {
                    newTime = availableSlots.get(slotChoice - 1);
                    break;
                } else {
                    System.out.println(" Please enter a number between 1 and " + availableSlots.size() + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println(" Please enter a valid numeric value.");
            }
        }

        // Step 6: Update DB
        String updateSql = "UPDATE appointments SET description = ?, doctor_name = ?, time = ? WHERE id = ?";
        PreparedStatement updateStmt = conn.prepareStatement(updateSql);
        updateStmt.setString(1, newProblem);
        updateStmt.setString(2, newDoctor);
        updateStmt.setString(3, newTime);
        updateStmt.setInt(4, id);

        int rows = updateStmt.executeUpdate();
        if (rows > 0) {
            System.out.println("\n Appointment updated successfully!");
            System.out.println("Name    : " + name);
            System.out.println("Date    : " + currentDate);
            System.out.println("Time    : " + LocalTime.parse(newTime).format(displayFormat));
            System.out.println("Problem : " + newProblem);
            System.out.println("Doctor  : " + newDoctor);
        } else {
            System.out.println(" Update failed.");
        }

        conn.close();

    } catch (Exception e) {
        System.out.println(" Error during update: " + e.getMessage());
    }
}

private void searchAppointment(Scanner sc) {
    try {
        System.out.println("\n--- Search Appointment ---");

        while (true) {
            System.out.println("1. Search by ID");
            System.out.println("2. Search by Name");
            System.out.print("Enter your choice (1 or 2): ");
            String input = sc.nextLine().trim();

            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = null;
            ResultSet rs = null;
            boolean validInput = false;

            if (input.equals("1")) {
                System.out.print("Enter Appointment ID: ");
                String idInput = sc.nextLine().trim();

                if (!idInput.matches("\\d+")) {
                    System.out.println(" Please enter a valid numeric ID.\n");
                    continue;
                }

                int id = Integer.parseInt(idInput);
                String sql = "SELECT * FROM appointments WHERE id = ?";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, id);
                validInput = true;

            } else if (input.equals("2")) {
                System.out.print("Enter Name to search: ");
                String name = sc.nextLine().trim();

                if (name.length() < 2 || !name.matches("[a-zA-Z ]+")) {
                    System.out.println(" Name should be at least 2 characters and contain only letters.\n");
                    continue;
                }

                String sql = "SELECT * FROM appointments WHERE LOWER(name) LIKE ?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, "%" + name.toLowerCase() + "%");
                validInput = true;

            } else {
                System.out.println("Invalid choice. Please enter 1 or 2.\n");
                continue;
            }

            if (validInput) {
                rs = ps.executeQuery();
                boolean found = false;
                DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("hh:mm a");

                System.out.println("\nSearch Results:");
                System.out.println("-------------------------------------------------------------");
                System.out.printf("%-4s %-15s %-12s %-10s %-12s %-15s%n", "ID", "Name", "Date", "Time", "Problem", "Doctor");
                System.out.println("-------------------------------------------------------------");

                while (rs.next()) {
                    found = true;
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    Date date = rs.getDate("date");
                    Time time = rs.getTime("time");
                    String problem = rs.getString("description");
                    String doctor = rs.getString("doctor_name");

                    String formattedTime = time.toLocalTime().format(timeFormat);
                    System.out.printf("%-4d %-15s %-12s %-10s %-12s %-15s%n", id, name, date, formattedTime, problem, doctor);
                }

                if (!found) {
                    System.out.println(" No appointments found.");
                }

                System.out.println("-------------------------------------------------------------");
                conn.close();
                break;
            }
        }

    } catch (Exception e) {
        System.out.println("Error while searching: " + e.getMessage());
    }

    // Show Admin Dashboard again
  //  showAdminDashboard(sc);
}


}
