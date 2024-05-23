package service;

import exception.ValidationException;
import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>();
    protected final LocalDateTimeComparator comparator = new LocalDateTimeComparator();
    protected final Set<Task> prioritizedTasks = new TreeSet<>(comparator);

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    private final HistoryManager historyManager;

    int taskId = 0;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    public void setStartId(int taskId) {
        this.taskId = taskId;
    }

    private int generateId() {
        return ++taskId;
    }

    public void addToHistory(int id) {
        if (epics.containsKey(id)) {
            historyManager.add(epics.get(id));
        } else if (subTasks.containsKey(id)) {
            historyManager.add(subTasks.get(id));
        } else if (tasks.containsKey(id)) {
            historyManager.add(tasks.get(id));
        }
    }

    //-----Получение списка всех задач.-----

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

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
    public Task createTask(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
        prioritizedTasks.add(task);
        validation(task);
        return task;
    }

    @Override
    public SubTask createSubTask(SubTask subTask) {
        subTask.setId(generateId());
        subTasks.put(subTask.getId(), subTask);
        Epic epic = epics.get(subTask.getEpicId());
        validation(subTask);
        epic.addSubTask(subTask);
        subTasks.put(subTask.getId(), subTask);
        epic.setStatus(calculateStatus(epic));
        calculateStartTimeAndDuration(epic);
        prioritizedTasks.add(subTask);
        return subTask;
    }

    @Override
    public Epic createEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
        calculateStartTimeAndDuration(epic);
        return epic;
    }

    //-----Обновление.-----

    @Override
    public void updateTask(Task task) {
        validation(task);
        prioritizedTasks.remove(tasks.get(task.getId()));
        tasks.put(task.getId(), task);
        prioritizedTasks.add(task);
    }

    @Override
    public void updateEpic(Epic epic) {
        Epic saved = epics.get(epic.getId());
        saved.setName(epic.getName());
        saved.setDescription(epic.getDescription());
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        validation(subTask);
        prioritizedTasks.remove(subTasks.get(subTask.getId()));
        Epic epic = epics.get(subTask.getEpicId());
        SubTask updateSubTask = subTasks.get(subTask.getId());
        epic.getSubTasks().remove(updateSubTask);
        subTasks.put(subTask.getId(), subTask);
        calculateStartTimeAndDuration(epic);
        prioritizedTasks.add(subTask);
        epic.addSubTask(subTask);
        epic.setStatus(calculateStatus(epic));
    }

    //-----Удаление по идентификатору.-----

    @Override
    public void deleteTask(int id) {
        prioritizedTasks.remove(tasks.get(id));
        tasks.remove(id);
    }

    @Override
    public void deleteSubTask(int id) {
        SubTask removeSubTask = subTasks.remove(id);
        Epic epic = epics.get(removeSubTask.getEpicId());
        Epic epicSaved = epics.get(epic.getId());
        epicSaved.getSubTasks().remove(removeSubTask);
        epicSaved.setStatus(calculateStatus(epicSaved));
        prioritizedTasks.remove(removeSubTask);
    }

    @Override
    public void deleteEpic(int id) {
        Epic epic = epics.get(id);
        for (SubTask subTask : epic.getSubTasks()) {
            prioritizedTasks.remove(subTask);
            subTasks.remove(subTask.getId());
        }
        prioritizedTasks.remove(epics.get(id));
        epics.remove(id);
    }

    //-----Получение списка всех подзадач определённого эпика.-----

    @Override
    public List<SubTask> getEpicSubTasks(Epic epic) {
        return epic.getSubTasks();
    }

    public void validation(Task task) {
        for (Task existTask : prioritizedTasks) {
            if (Objects.equals(task.getId(), existTask.getId())) {
                continue;
            }
            if (existTask.getStartTime() == null || task.getStartTime() == null) {
                break;
            }
            boolean check = (task.getEndTime().isBefore(existTask.getStartTime())
                    || task.getEndTime().equals(existTask.getStartTime())
                    || task.getStartTime().isAfter(existTask.getEndTime())
                    || task.getStartTime().equals(existTask.getStartTime()));
            if (!check) {
                throw new ValidationException(task + " error validation: " + existTask);
            }
        }
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    public void calculateStartTimeAndDuration(Epic epic) {
        LocalDateTime endTime = null;
        LocalDateTime startTime = null;
        int duration = 0;
        if (epic.getSubTasks() != null) {
            for (SubTask subTask : epic.getSubTasks()) {
                if (subTask.getStartTime() != null) {
                    if (startTime == null || startTime.isAfter(subTask.getStartTime())) {
                        startTime = subTask.getStartTime();
                    }
                } else {
                    System.out.println("Subtask start time equals null.");
                }
                if (subTask.getEndTime() != null) {
                    if (endTime == null || endTime.isBefore(subTask.getEndTime())) {
                        endTime = subTask.getEndTime();
                    }
                } else {
                    System.out.println("Subtask end time equals null.");
                }
                duration = duration + subTask.getDuration();
            }
        }
        epic.setStartTime(startTime);
        epic.setEndTime(endTime);
        epic.setDuration(duration);
    }

    protected TaskStatus calculateStatus(Epic epic) {
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



