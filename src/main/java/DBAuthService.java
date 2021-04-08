public interface DBAuthService {

    int addUser(String name, String pass);

    boolean auth(String name, String pass);
}