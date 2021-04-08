import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class LocalHistoryService implements HistoryService{

    private final String FILENAME = "history.txt";

    @Override
    public List<String> getHistory() {
        List<String> arrayList = new ArrayList<>();
        int lines = 0;
        byte[] bytes = new byte[256];
        int numBytes = 0;
        File file = new File(FILENAME);
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(file, "r");
            long fileLength = file.length() - 1;
            randomAccessFile.seek(fileLength);
            for (long pointer = fileLength; pointer >= 0; pointer--) {
                randomAccessFile.seek(pointer);
                byte b;
                b = (byte) randomAccessFile.read();
                if ((char)b == '\n') {
                    if (numBytes > 0) {
                        for(int i = 0; i < numBytes / 2; i++)
                        {
                            byte temp = bytes[i];
                            bytes[i] = bytes[numBytes - i - 1];
                            bytes[numBytes - i - 1] = temp;
                        }

                        bytes[numBytes] = '\n';
                        arrayList.add(new String(bytes, StandardCharsets.UTF_8));
                        lines++;
                        bytes = new byte[256];
                        numBytes = 0;
                    }
                    continue;
                }
                bytes[numBytes] = b;
                numBytes++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (randomAccessFile != null) {
                try {
                    randomAccessFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return arrayList;
    }

    @Override
    public List<String> getHistory(int limit) {
        List<String> arrayList = new ArrayList<>();
        int lines = 0;
        byte[] bytes = new byte[256];
        int numBytes = 0;
        File file = new File(FILENAME);
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(file, "r");
            long fileLength = file.length() - 1;
            randomAccessFile.seek(fileLength);
            for (long pointer = fileLength; pointer >= 0; pointer--) {
                randomAccessFile.seek(pointer);
                byte b;
                b = (byte) randomAccessFile.read();
                if ((char)b == '\n') {
                    if (numBytes > 0) {
                        for(int i = 0; i < numBytes / 2; i++)
                        {
                            byte temp = bytes[i];
                            bytes[i] = bytes[numBytes - i - 1];
                            bytes[numBytes - i - 1] = temp;
                        }

                        bytes[numBytes] = '\n';
                        arrayList.add(new String(bytes, StandardCharsets.UTF_8));
                        lines++;
                        bytes = new byte[256];
                        numBytes = 0;
                    }
                    if (lines > limit) break;
                    continue;
                }
                bytes[numBytes] = b;
                numBytes++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (randomAccessFile != null) {
                try {
                    randomAccessFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return arrayList;
    }

    @Override
    public void saveHistory(List<String> history) {
        try {
            DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(
                    new FileOutputStream(FILENAME, true)));
            for (String mes:
                    history) {
                dataOutputStream.writeUTF(mes);
            }
            dataOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveMessage(String message) {
        try {
            DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(
                    new FileOutputStream(FILENAME, true)));
            dataOutputStream.writeUTF(message);
            dataOutputStream.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}