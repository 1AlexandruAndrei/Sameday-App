package tracker;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CRUDtracker {
    private static final String FILE_PATH = "src/info.csv";
    private static List<OperationRecord> operationRecords = new ArrayList<>();

    static {
        loadOperationRecords();
    }

    public static void recordOperation(String operation) {
        String timestamp = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss").format(new Date());
        operationRecords.add(new OperationRecord(operation, timestamp));
        writeToFile();
    }

    private static void writeToFile() {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            for (OperationRecord record : operationRecords) {
                writer.write(record.getOperation() + "," + record.getTimestamp() + "\n");
            }
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }

    private static void loadOperationRecords() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String operation = parts[0];
                    String timestamp = parts[1];
                    operationRecords.add(new OperationRecord(operation, timestamp));
                }
            }
        } catch (IOException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    static class OperationRecord {
        private String operation;
        private String timestamp;

        public OperationRecord(String operation, String timestamp) {
            this.operation = operation;
            this.timestamp = timestamp;
        }

        public String getOperation() {
            return operation;
        }

        public String getTimestamp() {
            return timestamp;
        }
    }
}
