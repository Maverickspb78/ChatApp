import java.sql.ResultSet;
import java.sql.SQLException;

public class DBAuthServiceImpl implements DBAuthService{
    private static DBAuthServiceImpl instance;
    private SQLiteServiceImpl sqLiteService;
    private String url = "jdbc:sqlite:C:\\ChatApp\\src\\main\\java\\DB";

    private DBAuthServiceImpl() {
        sqLiteService = SQLiteServiceImpl.getInstance();
    }

    public static DBAuthServiceImpl getInstance() {
        if (instance == null) {
            instance = new DBAuthServiceImpl();
        }
        return instance;
    }

    public void init() {
        String query = "CREATE TABLE IF NOT EXISTS users " +
                "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "password TEXT NOT NULL" +
                ");";

        try {
            sqLiteService.connect(url);
            sqLiteService.update(query);
            sqLiteService.disconnect();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private String getPassword(String user) {
        String query = "SELECT * FROM users WHERE name = '" + user + "';";
        ResultSet rs;
        try {
            sqLiteService.connect(url);
            rs = sqLiteService.query(query);
            if(rs.next()) {
                return rs.getString("password");
            }
            sqLiteService.disconnect();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public int addUser(String name, String pass) {
        if(getPassword(name).equals("")) {
            String query = String.format("INSERT INTO users (name, password) " +
                    "VALUES ('%s', '%s');", name, pass);
            try {
                sqLiteService.connect(url);
                sqLiteService.update(query);
                sqLiteService.disconnect();
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                return -1;
            }
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public boolean auth(String name, String pass) {
        return (getPassword(name).equals(pass));
    }

}
