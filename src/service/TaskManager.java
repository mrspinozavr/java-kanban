package service;

import model.SubTask;
import model.Task;
import model.Epic;
import model.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskManager {
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, SubTask> subTasks;
    private final HashMap<Integer, Epic> epics;

    int taskId = 0;

    public TaskManager() {
        this.tasks = new HashMap<>();
        this.subTasks = new HashMap<>();
        this.epics = new HashMap<>();
    }

    private int generateId() {
        return ++taskId;
    }

    //-----Получение списка всех задач.-----

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public List<SubTask> getAllSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    //-----Удаление всех задач.-----

    public void removeAllTasks() {
        tasks.clear();
    }

    public void removeAllSubtasks() {
        for (Epic epic : epics.values()) {
            epic.getSubTasks().clear();
            epic.setStatus(calculateStatus(epic));
        }
        subTasks.clear();

    }

    public void removeAllEpics() {
        epics.clear();
        subTasks.clear();

    }

    //----- Получение по идентификатору.-----

    public Task getTask(int id) {
        return tasks.get(id);
    }

    public SubTask getSubTask(int id) {
        return subTasks.get(id);
    }

    public Epic getEpic(int id) {
        return epics.get(id);
    }

    //-----Создание.-----

    public Task createTask(Task task){
        task.setId(generateId());
        tasks.put(task.getId(), task);
        return task;
    }

    public SubTask createSubTask(SubTask subTask){
        subTask.setId(generateId());
        subTasks.put(subTask.getId(), subTask);
        Epic epic = subTask.getEpic();
        epic.addSubTask(subTask);
        subTasks.put(subTask.getId(), subTask);
        return subTask;
    }

    public Epic createEpic(Epic epic){
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    //-----Обновление.-----

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void updateEpic(Epic epic) {
        Epic saved = epics.get(epic.getId());
        saved.setName(epic.getName());
        saved.setDescription(epic.getDescription());
    }

    public void updateSubTask(SubTask subTask) {
        Epic epic = epics.get(subTask.getEpic().getId());
        SubTask updateSubTask = subTasks.get(subTask.getId());
        epic.getSubTasks().remove(updateSubTask);
        subTasks.put(subTask.getId(), subTask);
        epic.addSubTask(subTask);
        epic.setStatus(calculateStatus(epic));
    }

    //-----Удаление по идентификатору.-----

    public void deleteTask(int id) {
        tasks.remove(id);
    }

    public void deleteSubTask(int id) {
        SubTask removeSubTask = subTasks.remove(id);
        Epic epic = removeSubTask.getEpic();
        Epic epicSaved = epics.get(epic.getId());
        epicSaved.getSubTasks().remove(removeSubTask);
        epicSaved.setStatus(calculateStatus(epicSaved));
    }

    public void deleteEpic(int id) {
        Epic epic = epics.get(id);
        for (SubTask subTask : epic.getSubTasks()) {
            subTasks.remove(subTask.getId());
        }
        epics.remove(id);
    }

    //-----Получение списка всех подзадач определённого эпика.-----

    public List<SubTask> getEpicSubTasks(Epic epic) {
        return epic.getSubTasks();
    }

    private TaskStatus calculateStatus(Epic epic){
        boolean isNew = false;
        boolean isProgress = false;
        boolean isDone = false;

        if (epic.getSubTasks().isEmpty()) {
            isNew = true;
        } else {
            for (SubTask subTask : epic.getSubTasks()) {
                TaskStatus subTaskStatus = subTask.getStatus();
                if (subTaskStatus == TaskStatus.NEW) {
                    isNew = true;
                } else if (subTaskStatus == TaskStatus.DONE) {
                    isDone = true;
                } else {
                    isProgress = true;
                }
            }
        }

        if (isNew && !isProgress && !isDone) {
            return TaskStatus.NEW;
        } else if (!isNew && !isProgress && isDone) {
            return TaskStatus.DONE;
        } else {
            return TaskStatus.IN_PROGRESS;
        }
    }
}



