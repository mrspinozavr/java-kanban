package service;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class ConverterCSV {

    public static String historyToString(HistoryManager manager) {
        List<Task> history = manager.getHistory();
        StringBuilder stringBuilder = new StringBuilder();

        if (history.isEmpty()) {
            return "";
        }

        for (Task task : history) {
            stringBuilder.append(task.getId()).append(",");
        }

        if (stringBuilder.length() != 0) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        return stringBuilder.toString();
    }

    public static List<Integer> historyFromString(String value) {
        List<Integer> recoveryList = new ArrayList<>();
        if (value != null) {
            String[] tasks = value.split(",");

            for (String id : tasks) {
                recoveryList.add(Integer.parseInt(id));
            }
        }
        return recoveryList;
    }
}
