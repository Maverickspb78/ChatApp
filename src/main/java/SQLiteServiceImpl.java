import java.sql.*;

public class SQLiteServiceImpl {

    private static SQLiteServiceImpl instance;
    private Connection conn;

    public void connect(String url) throws SQLException, ClassNotFoundException {
        conn = null;
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection(url);
        System.out.println("Connection to SQLite has been established.");
    }

    public void disconnect() throws SQLException {
        conn.close();
    }

    public ResultSet query(String queryString) throws SQLException {
        Statement stmt = conn.createStatement();
        return stmt.executeQuery(queryString);
    }

    public int update(String updateString) throws SQLException {
        Statement updateStmt = conn.createStatement();
        return updateStmt.executeUpdate(updateString);
    }


    public static SQLiteServiceImpl getInstance() {
        if (instance == null) {
            instance = new SQLiteServiceImpl();
        }
        return instance;
    }

}