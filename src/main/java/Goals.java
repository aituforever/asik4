import java.sql.*;
import java.sql.Date;
import java.util.*;
public class Goals extends TableCreate{
    @Override
    public void createTable(Connection conn, String table_name){
        Statement statement;
        try{
            String query = "create table " +table_name+"(GoalID SERIAL, UserID INTEGER, type varchar(200), Start_Date date, target_value double precision, duration_days integer, achieved BOOLEAN,primary key(GoalID), foreign key (UserID) references users(userID))";
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Table Goals sdelan");
        }catch(Exception e){
            System.out.println(e);
        }
    }
    @Override
    public boolean createRow(Connection conn, String username){
        Scanner scanner = new Scanner(System.in);

        int userID = Users.getUserId(conn, username);

        System.out.println("Enter goal type:");
        String type = scanner.next();

        System.out.println("Enter start date (yyyy-mm-dd):");
        Date start_date = Date.valueOf(scanner.next());

        System.out.println("Enter target value:");
        double target_value = scanner.nextDouble();

        System.out.println("Enter duration(days):");
        int duration = scanner.nextInt();

        System.out.println("Has been goal achieved?(True or False):");
        boolean achieved = scanner.hasNextBoolean();

        PreparedStatement statement = null;
        try{
            String query = "insert into Goals(UserID, type, start_date, target_value, duration_days, achieved) values(?, ?, ?, ?, ?, ?)";
            statement = conn.prepareStatement(query);
            statement.setInt(1, userID);
            statement.setString(2, type);
            statement.setDate(3, start_date);
            statement.setDouble(4, target_value);
            statement.setInt(5, duration);
            statement.setBoolean(6, achieved);
            statement.executeUpdate();
            System.out.println("Goal created");
        }catch(Exception e){
            System.out.println("not created goal: "+e);
        }
        return achieved;
    }
    @Override
    public void deleteRow(Connection conn, String username) {
        PreparedStatement statement = null;
        String query = "DELETE FROM goals" + " WHERE id = ?";
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter GoalID that will be deleted: ");
        int GoalID = scanner.nextInt();
        try {
            statement.setInt(1, GoalID);
            statement.executeUpdate(query);
            System.out.println("Goal was deleted");
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    @Override
    public void updateRow(Connection conn, String username) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter GoalID to modify: ");
        int GoalID = scanner.nextInt();
        System.out.println("Enter column to modify:\nUserID\n" +
                "type\n" +
                "start_date\n" +
                "target_value\n" +
                "duration_days\n" +
                "achieved");
        String column_name = scanner.next();
        System.out.println("Enter new value: ");
        String new_value = scanner.next();
        String query = "UPDATE goals"+ " SET "+ column_name +" = ? WHERE goalid = ?";
        try {
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, new_value);
            statement.setInt(2, GoalID);
            statement.executeUpdate();
            System.out.println("Value has been changed");
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    @Override
    public void readRow(Connection conn, String username) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter GoalID: ");
        int GoalID = scanner.nextInt();
        System.out.println("Enter required information to display:\nUserID\n" +
                "type\n" +
                "start_date\n" +
                "target_value\n" +
                "duration_days\n" +
                "achieved");
        String column_name = scanner.next();
        String query = "SELECT "+ column_name +" FROM goals WHERE goalid = ?";
        try {
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, GoalID);
            ResultSet ans = statement.executeQuery();
            if(ans.next()){
                System.out.println(column_name+": "+ans.getString(column_name));
            }
            else{
                System.out.println("No goal was found");
            }
        } catch (SQLException e) {
            System.out.println("Error: "+e);
        }
    }
    public void getSuggestion(Connection conn, String username) {
        try {
            String query = "SELECT * FROM goals WHERE userid = ?";
            int UserID = Users.getUserId(conn, username);
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, UserID);
            ResultSet ans = statement.executeQuery();

            while (ans.next()) {
                String goalType = ans.getString("type");
                double targetValue = ans.getDouble("target_value");

                System.out.println("For your goal of type " + goalType + " with target value " + targetValue + ", here are some activity suggestions:");

                if (goalType.equals("weight_loss")) {
                    System.out.println("- Jogging for 30 minutes every day");
                    System.out.println("- Eating a balanced diet");
                } else if (goalType.equals("muscle_gain")) {
                    System.out.println("- Lifting weights 3 times a week");
                    System.out.println("- Eating protein-rich foods");
                } else {
                    System.out.println("- No suggestions available for this goal type");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: "+e);;
        }
    }
}
