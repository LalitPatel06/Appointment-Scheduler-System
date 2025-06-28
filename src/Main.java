import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Welcome to Appointment Scheduler");

        String role = "";
        while (true) {
            System.out.print("Are you Admin? (yes/no): ");
            role = sc.nextLine().trim().toLowerCase();

            if (role.equals("yes")) {
                Admin admin = new Admin();
                admin.showAdminDashboard(sc);
                break;
            } else if (role.equals("no")) {
                User user = new User();
                user.showUserDashboard(sc);
                break;
            } else {
                System.out.println(" Invalid input. Please enter only 'yes' or 'no'.");
            }
        }

        sc.close();
    }
}
