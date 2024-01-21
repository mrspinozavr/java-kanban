package Service;

import model.SubTask;
import model.Task;
import model.Epic;
import model.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskManager {
    private HashMap<Integer, Task> tasksMap;
    private HashMap<Integer, SubTask> subTaskMap;
    private HashMap<Integer, Epic> epicMap;

    int taskId = 0;

    public TaskManager() {
        this.tasksMap = new HashMap<>();
        this.subTaskMap = new HashMap<>();
        this.epicMap = new HashMap<>();
    }

    private int generateId() {
        return ++taskId;
    }

    //-----Получение списка всех задач.-----

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasksMap.values());
    }

    public List<Epic> getAllEpics() {
        return new ArrayList<>(epicMap.values());
    }

    public List<SubTask> getAllSubTasks() {
        return new ArrayList<>(subTaskMap.values());
    }

    //-----Удаление всех задач.-----

    public void removeAllTasks() {
        tasksMap.clear();
    }

    public void removeAllSubtasks() {
        for (Epic epic : epicMap.values()) {
            epic.getSubTasks().clear();
        }
        subTaskMap.clear();

    }

    public void removeAllEpics() {
        epicMap.clear();
        subTaskMap.clear();

    }

    //----- Получение по идентификатору.-----

    public Task getTask(int id) {
        return tasksMap.get(id);
    }

    public SubTask getSubTask(int id) {
        return subTaskMap.get(id);
    }

    public Epic getEpic(int id) {
        return epicMap.get(id);
    }

    //-----Создание.-----

    public Task createTask(Task task){
        task.setId(generateId());
        tasksMap.put(task.getId(), task);
        return task;
    }

    public SubTask createSubTask(SubTask subTask){
        subTask.setId(generateId());
        subTaskMap.put(subTask.getId(), subTask);
        Epic epic = subTask.getEpic();
        epic.addSubTask(subTask);
        subTaskMap.put(subTask.getId(), subTask);
        return subTask;
    }

    public Epic createEpic(Epic epic){
        epic.setId(generateId());
        epicMap.put(epic.getId(), epic);
        return epic;
    }

    //-----Обновление.-----

    public void updateTask(Task task) {
        tasksMap.put(task.getId(), task);
    }

    public void updateEpic(Epic epic) {
        Epic saved = epicMap.get(epic.getId());
        saved.setName(epic.getName());
        saved.setDescription(epic.getDescription());
    }

    public void updateSubTask(SubTask subTask) {
        Epic epic = epicMap.get(subTask.getEpic().getId());
        SubTask updateSubTask = subTaskMap.get(subTask.getId());
        epic.getSubTasks().remove(updateSubTask);
        subTaskMap.put(subTask.getId(), subTask);
        epic.addSubTask(subTask);
        epic.setStatus(calculateStatus(epic));
    }

    //-----Удаление по идентификатору.-----

    public void deleteTask(int id) {
        tasksMap.remove(id);
    }

    public void deleteSubTask(int id) {
        SubTask removeSubTask = subTaskMap.remove(id);
        Epic epic = removeSubTask.getEpic();
        Epic epicSaved = epicMap.get(epic.getId());
        epicSaved.getSubTasks().remove(removeSubTask);
        epicSaved.setStatus(calculateStatus(epicSaved));
    }

    public void deleteEpic(int id) {
        Epic epic = epicMap.get(id);
        for (SubTask subTask : epic.getSubTasks()) {
            subTaskMap.remove(subTask.getId());
        }
        epicMap.remove(id);
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



