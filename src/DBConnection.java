import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    public static Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(
            "jdbc:mysql:///appointmentscheduler?useSSL=false", // Change DB name
            "root", // Change to your MySQL username
            "Root"  // Change to your MySQL password
        );
    }
}
