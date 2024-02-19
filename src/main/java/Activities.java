import java.sql.*;
import java.sql.Date;
import java.util.*;
public class Activities extends TableCreate{
    @Override
    public void createTable(Connection conn, String table_name){
        Statement statement;
        try{
            String query = "create table " +table_name+"(ActivityID SERIAL, UserID INTEGER,type varchar(200), Date date, duration time, distance double precision, calories_burned int, notes varchar(200), primary key(ActivityID), foreign key (UserID) references users(userID))";
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Table Activity sdelan");
        }catch(Exception e){
            System.out.println(e);
        }
    }

    @Override
    public boolean createRow(Connection conn, String username){
        Scanner scanner = new Scanner(System.in);

        int userID = Users.getUserId(conn, username);;

        System.out.println("Enter activity type:");
        String type = scanner.next();

        System.out.println("Enter date (yyyy-mm-dd):");
        Date date = Date.valueOf(scanner.next());

        System.out.println("Enter duration (HH:MM:SS):");
        Time duration = Time.valueOf(scanner.next());

        System.out.println("Enter distance:");
        int distance = scanner.nextInt();

        System.out.println("Enter calories burned:");
        int caloriesBurned = scanner.nextInt();

        System.out.println("Enter notes:");
        String notes = scanner.next();

        PreparedStatement statement = null;
        try{
            String query = "insert into Activities(UserID, type, Date, duration, distance, calories_burned, notes) values(?, ?, ?, ?, ?, ?, ?)";
            statement = conn.prepareStatement(query);
            statement.setInt(1, userID);
            statement.setString(2, type);
            statement.setDate(3, date);
            statement.setTime(4, duration);
            statement.setInt(5, distance);
            statement.setInt(6, caloriesBurned);
            statement.setString(7, notes);
            statement.executeUpdate();
            System.out.println("Activity created");
        }catch(Exception e){
            System.out.println("Activity was not added: "+e);
        }
        return false;
    }

    @Override
    public void deleteRow(Connection conn, String username) {
        PreparedStatement statement = null;
        String query = "DELETE FROM activities " + " WHERE id = ?";
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter ActivityID that will be deleted: ");
        int ActivityID = scanner.nextInt();
        try {
            statement.setInt(1, ActivityID);
            statement.executeUpdate(query);
            System.out.println("Activity was deleted");
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    @Override
    public void updateRow(Connection conn, String username) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter ActivityID to modify: ");
        int activityid = scanner.nextInt();
        System.out.println("Enter data to modify:\nUserID\n" +
                "type\n" +
                "Date\n" +
                "duration\n" +
                "distance\n" +
                "calories_burned\n" +
                "notes");
        String column_name = scanner.next();
        System.out.println("Enter new value");
        String new_value = scanner.next();
        String query = "UPDATE activities"+ " SET "+ column_name +" = ? WHERE activityid = ?";
        try {
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, new_value);
            statement.setInt(2, activityid);
            statement.executeUpdate();
            System.out.println("Value has been changed");
        } catch (SQLException e) {
            System.out.println(e);
        }

    }
    @Override
    public void readRow(Connection conn, String username) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter ActivityID: ");
        int ActivityID = scanner.nextInt();
        System.out.println("Enter required information to display:\nUserID\n" +
                "type\n" +
                "Date\n" +
                "duration\n" +
                "distance\n" +
                "calories_burned\n" +
                "notes");
        String column_name = scanner.next();
        String query = "SELECT "+ column_name +" FROM activities WHERE activityid = ?";
        try {
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, ActivityID);
            ResultSet ans = statement.executeQuery();
            if(ans.next()){
                System.out.println(column_name+": "+ans.getString(column_name));
            }
            else{
                System.out.println("No activity was found");
            }
        } catch (SQLException e) {
            System.out.println("Error: "+e);
        }
    }
    public void report(Connection conn, String username) {
        try {
            String query = "SELECT * FROM users WHERE username = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, username);
            ResultSet ans = statement.executeQuery();

            if (ans.next()) {
                String fname = ans.getString("fname");
                String sname = ans.getString("sname");
                Date dateofbirth = ans.getDate("dateofbirth");
                int height = ans.getInt("height");
                int weight = ans.getInt("weight");
                String gender = ans.getString("gender");

                System.out.println("Health Report for " + fname + " " + sname + ":");
                System.out.println("Date of Birth: " + dateofbirth);
                System.out.println("Height: " + height + " cm");
                System.out.println("Weight: " + weight + " kg");
                System.out.println("Gender: " + gender);

                query = "SELECT * FROM activities WHERE username = ?";
                statement = conn.prepareStatement(query);
                statement.setString(1, username);
                ans = statement.executeQuery();

                System.out.println("\nActivities:");
                while (ans.next()) {
                    String type = ans.getString("type");
                    Date date = ans.getDate("Date");
                    Time duration = ans.getTime("duration");
                    int distance = ans.getInt("distance");
                    int caloriesBurned = ans.getInt("calories_burned");
                    String notes = ans.getString("notes");

                    System.out.println(type + " on " + date + " for " + duration + " hours, covering " + distance + " km and burning " + caloriesBurned + " calories. Notes: " + notes);
                }

                query = "SELECT * FROM measurements WHERE username = ?";
                statement = conn.prepareStatement(query);
                statement.setString(1, username);
                ans = statement.executeQuery();

                System.out.println("\nMeasurements:");
                while (ans.next()) {
                    String type = ans.getString("type");
                    Date date = ans.getDate("Date");
                    double value = ans.getDouble("value");
                    String unit = ans.getString("unit");

                    System.out.println(type + " on " + date + ": " + value + " " + unit);
                }

                query = "SELECT * FROM goals WHERE username = ?";
                statement = conn.prepareStatement(query);
                statement.setString(1, username);
                ans = statement.executeQuery();

                System.out.println("\nGoals:");
                while (ans.next()) {
                    String type = ans.getString("type");
                    Date startDate = ans.getDate("start_date");
                    double targetValue = ans.getDouble("target_value");
                    int durationDays = ans.getInt("duration_days");
                    boolean achieved = ans.getBoolean("achieved");

                    System.out.println(type + " goal starting on " + startDate + " with a target value of " + targetValue + " over " + durationDays + " days. Achieved: " + (achieved ? "Yes" : "No"));
                }
            } else {
                System.out.println("User not found.");
            }

            ans.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println("Error:"+e);;
        }
    }
}
