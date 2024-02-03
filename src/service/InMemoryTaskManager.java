package service;

import model.SubTask;
import model.Task;
import model.Epic;
import model.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager{
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, SubTask> subTasks;
    private final HashMap<Integer, Epic> epics;
    private final HistoryManager historyManager;

    int taskId = 0;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.tasks = new HashMap<>();
        this.subTasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.historyManager = historyManager;
    }

    private int generateId() {
        return ++taskId;
    }

    //-----Получение списка всех задач.-----

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());

    }
    @Override
    public List<SubTask> getAllSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    //-----Удаление всех задач.-----

    @Override
    public void removeAllTasks() {
        tasks.clear();
    }

    @Override
    public void removeAllSubtasks() {
        for (Epic epic : epics.values()) {
            epic.getSubTasks().clear();
            epic.setStatus(calculateStatus(epic));
        }
        subTasks.clear();

    }

    @Override
    public void removeAllEpics() {
        epics.clear();
        subTasks.clear();

    }

    //----- Получение по идентификатору.-----

    @Override
    public Task getTask(int id) {
        Task task = tasks.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public SubTask getSubTask(int id) {
        SubTask subTask = subTasks.get(id);
        historyManager.add(subTask);
        return subTask;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        historyManager.add(epic);
        return epic;
    }

    //-----Создание.-----

    @Override
    public Task createTask(Task task){
        task.setId(generateId());
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public SubTask createSubTask(SubTask subTask){
        subTask.setId(generateId());
        subTasks.put(subTask.getId(), subTask);
        Epic epic = subTask.getEpic();
        epic.addSubTask(subTask);
        subTasks.put(subTask.getId(), subTask);
        return subTask;
    }

    @Override
    public Epic createEpic(Epic epic){
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    //-----Обновление.-----

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) {
        Epic saved = epics.get(epic.getId());
        saved.setName(epic.getName());
        saved.setDescription(epic.getDescription());
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        Epic epic = epics.get(subTask.getEpic().getId());
        SubTask updateSubTask = subTasks.get(subTask.getId());
        epic.getSubTasks().remove(updateSubTask);
        subTasks.put(subTask.getId(), subTask);
        epic.addSubTask(subTask);
        epic.setStatus(calculateStatus(epic));
    }

    //-----Удаление по идентификатору.-----

    @Override
    public void deleteTask(int id) {
        tasks.remove(id);
    }

    @Override
    public void deleteSubTask(int id) {
        SubTask removeSubTask = subTasks.remove(id);
        Epic epic = removeSubTask.getEpic();
        Epic epicSaved = epics.get(epic.getId());
        epicSaved.getSubTasks().remove(removeSubTask);
        epicSaved.setStatus(calculateStatus(epicSaved));
    }

    @Override
    public void deleteEpic(int id) {
        Epic epic = epics.get(id);
        for (SubTask subTask : epic.getSubTasks()) {
            subTasks.remove(subTask.getId());
        }
        epics.remove(id);
    }

    //-----Получение списка всех подзадач определённого эпика.-----

    @Override
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



