package tracker;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class CRUDtracker {
    private static final String FILE_PATH = "src/info.csv";
    private static Map<String, Integer> operationCount = new HashMap<>();

    static {
        loadOperationCounts();
    }

    public static void recordOperation(String operation) {
        operationCount.put(operation, operationCount.getOrDefault(operation, 0) + 1);
        writeToFile();
    }

    private static void writeToFile() {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            for (Map.Entry<String, Integer> entry : operationCount.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue() + "\n");
            }
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }

    private static void loadOperationCounts() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String operation = parts[0];
                    int count = Integer.parseInt(parts[1]);
                    operationCount.put(operation, count);
                }
            }
        } catch (IOException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}
