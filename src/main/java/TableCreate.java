import java.sql.Connection;

public abstract class TableCreate {
    public abstract void createTable(Connection conn, String tableName);
    public abstract boolean createRow(Connection conn, String username);
    public abstract void deleteRow(Connection conn, String username);
    public abstract void updateRow(Connection conn, String username);
    public abstract void readRow(Connection conn, String username);
}
