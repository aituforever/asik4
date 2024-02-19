import java.sql.*;
import java.sql.Date;
import java.util.*;
public class Main {
    public static void main(String[] args) {
        Users users = new Users();
        Activities activities = new Activities();
        Measurements measurements = new Measurements();
        Goals goals = new Goals();
        Connection conn = users.connecttodb("aituforever", "postgres", "123456789");

        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("Welcome to Health Tracker! Please choose an option:");
            System.out.println("1. Log in");
            System.out.println("2. Sign up");
            System.out.println("3. Exit");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Enter username: ");
                    String username = scanner.next();
                    System.out.println("Enter password: ");
                    String pass = scanner.next();
                    if (users.login(conn, username, pass)) {
                        System.out.println("Welcome back, " + username + "!");
                        userMenu(conn, username, users, activities, measurements, goals);
                    } else {
                        System.out.println("Invalid username or password.");
                    }
                    break;
                case 2:
                    System.out.println("Create username: ");
                    String Newusername = scanner.next();
                    if (users.createRow(conn, Newusername)) {
                        System.out.println("Account created successfully!");
                        userMenu(conn, Newusername, users, activities, measurements, goals);
                    } else {
                        System.out.println("Username already exists. Please choose another one.");
                    }
                    break;
                case 3:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter 1, 2, or 3.");
                    break;
            }
        }
    }

    private static void userMenu(Connection conn, String username, Users users,Activities activities, Measurements measurements, Goals goals) {
        Scanner scanner = new Scanner(System.in);
        boolean back = false;

        while (!back) {
            System.out.println("What would you like to do?");
            System.out.println("1. Add a record");
            System.out.println("2. Delete a record");
            System.out.println("3. Select a record");
            System.out.println("4. Update a record");
            System.out.println("5. Calculate BMI");
            System.out.println("6. Get suggestion about goal");
            System.out.println("7. Log out");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Enter a data you would like to add:\n1.Activity\n2.Measurement\n3.Goal\n4.User");
                    String table_a = scanner.next();
                    switch (table_a){
                        case "4":
                            users.createRow(conn, username);
                            break;
                        case "1":
                            activities.createRow(conn, username);
                            break;
                        case "2":
                            measurements.createRow(conn, username);
                            break;
                        case "3":
                            goals.createRow(conn, username);
                            break;
                    }
                    break;
                case 2:
                    System.out.println("Enter a data you would like to delete:\n1.Activity\n2.Measurement\n3.Goal\n4.User");
                    String table_d = scanner.next();
                    switch (table_d){
                        case "4":
                            users.deleteRow(conn, username);
                            break;
                        case "1":
                            activities.deleteRow(conn, username);
                            break;
                        case "2":
                            measurements.deleteRow(conn, username);
                            break;
                        case "3":
                            goals.deleteRow(conn, username);
                            break;
                    }
                    break;
                case 3:
                    System.out.println("Enter a data you would like to read:\n1.Activity\n2.Measurement\n3.Goal\n4.User");
                    String table_r = scanner.next();
                    switch (table_r){
                        case "4":
                            users.readRow(conn, username);
                            break;
                        case "1":
                            activities.readRow(conn, username);
                            break;
                        case "2":
                            measurements.readRow(conn, username);
                            break;
                        case "3":
                            goals.readRow(conn, username);
                            break;
                    }
                    break;
                case 4:
                    System.out.println("Enter a data you would like to update:\n1.Activity\n2.Measurement\n3.Goal\n4.User");
                    String table_u = scanner.next();
                    switch (table_u){
                        case "4":
                            users.updateRow(conn, username);
                            break;
                        case "1":
                            activities.updateRow(conn, username);
                            break;
                        case "2":
                            measurements.updateRow(conn, username);
                            break;
                        case "3":
                            goals.updateRow(conn, username);
                            break;
                    }
                    break;
                case 5:
                    measurements.BMI(conn,username);
                    break;
                case 6:
                    goals.getSuggestion(conn, username);
                    break;
                case 7:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 7.");
                    break;
            }
        }
    }
}
