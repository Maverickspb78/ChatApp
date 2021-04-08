import java.util.List;

public interface HistoryService {

    List<String> getHistory();

    List<String> getHistory(int limit);

    void saveHistory(List<String> history);

    void saveMessage(String message);

}