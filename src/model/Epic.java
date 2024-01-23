package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Epic extends Task{

    private final List<SubTask> subTasks;

    public Epic(String name, String description) {
        super(name, description, TaskStatus.NEW);
        subTasks = new ArrayList<>();
    }

    public List<SubTask> getSubTasks() {
        return subTasks;
    }

    public void addSubTask(SubTask subTask) {
        subTasks.add(subTask);
    }

}
