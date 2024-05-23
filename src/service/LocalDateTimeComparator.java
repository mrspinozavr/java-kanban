package service;

import model.Task;

import java.util.Comparator;

public class LocalDateTimeComparator implements Comparator<Task> {
    @Override
    public int compare(Task task1, Task task2) {
        if (task1.getStartTime() == null && task2.getStartTime() != null) {
            return 1;
        } else if (task1.getStartTime() != null && task2.getStartTime() == null) {
            return -1;
        } else if (task1.getStartTime() == null && task2.getStartTime() == null) {
            return 0;
        } else if (task1.getStartTime().isAfter(task2.getStartTime())) { // если позже
            return 1;
        } else if (task1.getStartTime().isBefore(task2.getStartTime())) { // если раньше
            return -1;
        }
        return 0;
    }
}