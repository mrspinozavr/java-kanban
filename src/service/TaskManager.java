package service;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    List<Task> getHistory();

    //Task
    List<Task> getAllTasks();

    Task createTask(Task task);

    Task getTask(int id);

    void deleteTask(int id);

    void updateTask(Task task);

    void removeAllTasks();

    //Epic
    List<Epic> getAllEpics();

    Epic createEpic(Epic epic);

    Epic getEpic(int id);

    void deleteEpic(int id);

    void removeAllEpics();

    void updateEpic(Epic epic);

    //SubTask
    List<SubTask> getAllSubTasks();

    List<SubTask> getEpicSubTasks(Epic epic);

    SubTask createSubTask(SubTask subTask);

    void deleteSubTask(int id);

    void updateSubTask(SubTask subTask);

    void removeAllSubtasks();

    SubTask getSubTask(int id);

}
