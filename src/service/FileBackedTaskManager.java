package service;

import exception.ManagerSaveException;
import model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;
    private static final String HEADER_CSV_FILE = "id,type,name,status,description,startTime,endTime,duration,epic\n";

    public FileBackedTaskManager(HistoryManager historyManager, File file) {
        super(historyManager);
        this.file = file;
    }

    private void save() {
        try (FileWriter fileWriter = new FileWriter(file, StandardCharsets.UTF_8)) {
            fileWriter.write(HEADER_CSV_FILE);

            for (Task task : getAllTasks()) {
                fileWriter.write(task.toString());
            }

            for (Epic epic : getAllEpics()) {
                fileWriter.write(epic.toString());
            }

            for (SubTask subTask : getAllSubTasks()) {
                fileWriter.write(subTask.toString());
            }
            if (getHistory() != null) {
                fileWriter.write("\n");            // Отделяем пустой строкой историю от сериализованных задач
                fileWriter.write(ConverterCSV.historyToString(getHistoryManager()));
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка, файл не записался!", e);
        }
    }

    public void loadFromFile(File file) {
        int idMax = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {

            br.readLine();
            int maxId = 0;
            while (br.ready()) {
                String line = br.readLine();
                if (line.isBlank()) {
                    break;
                }
                Task task = fromString(line);
                if (task != null) {
                    int id = task.getId();
                    switch (task.getType()) {
                        case TASK:
                            tasks.put(task.getId(), task);
                            prioritizedTasks.add(task);
                            break;
                        case EPIC:
                            epics.put(task.getId(), (Epic) task);
                            calculateStartTimeAndDuration((Epic) task);
                            break;
                        case SUBTASK:
                            Epic epic = epics.get(((SubTask) task).getEpicId());
                            if (epic != null) {
                                subTasks.put(task.getId(), (SubTask) task);
                                epic.addSubTask((SubTask) task);
                                prioritizedTasks.add(task);
                                calculateStatus(epic);
                                calculateStartTimeAndDuration(epic);
                            }
                            break;
                        default:
                            System.out.println("Это не задача");
                    }
                    if (maxId < id) {
                        maxId = id;
                    }
                }
                setStartId(idMax);
            }

            String historyLine = br.readLine();
            for (Integer id : ConverterCSV.historyFromString(historyLine)) {
                addToHistory(id);
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось прочитать данные из файла", e);
        }
    }

    private static Task fromString(String value) {
        String[] values = value.split(",");
        String id = values[0];
        String type = values[1];
        String name = values[2];
        String status = values[3];
        String description = values[4];
        LocalDateTime startTime = LocalDateTime.parse(values[5], Task.FORMATTER);
        LocalDateTime endTime = LocalDateTime.parse(values[6], Task.FORMATTER);
        int duration = Integer.parseInt(values[7]);
        Integer idOfEpic = type.equals(TaskType.SUBTASK.toString()) ? Integer.valueOf(values[8]) : null;
        switch (type) {
            case "EPIC":
                Epic epic = new Epic(name, description);
                epic.setId(Integer.parseInt(id));
                epic.setStatus(TaskStatus.valueOf(status.toUpperCase()));
                epic.setStartTime(startTime);
                epic.setStartTime(endTime);
                epic.setDuration(duration);
                return epic;
            case "SUBTASK":
                SubTask subTask = new SubTask(name, description, idOfEpic, startTime, duration);
                subTask.setStatus(TaskStatus.valueOf(status.toUpperCase()));
                subTask.setId(Integer.parseInt(id));
                return subTask;
            case "TASK":
                Task task = new Task(name, description, TaskStatus.valueOf(status.toUpperCase()), startTime, duration);
                task.setId(Integer.parseInt(id));
                return task;
            default:
                return null;
        }
    }


    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public void removeAllSubtasks() {
        super.removeAllSubtasks();
        save();
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        save();
    }

    @Override
    public Task getTask(int id) {
        Task getTask = super.getTask(id);
        save();
        return getTask;
    }

    @Override
    public SubTask getSubTask(int id) {
        SubTask getSubTask = super.getSubTask(id);
        save();
        return getSubTask;
    }

    @Override
    public Epic getEpic(int id) {
        Epic getEpic = super.getEpic(id);
        save();
        return getEpic;
    }

    @Override
    public Task createTask(Task task) {
        Task newTask = super.createTask(task);
        save();
        return newTask;
    }

    @Override
    public SubTask createSubTask(SubTask subTask) {
        SubTask newSubtask = super.createSubTask(subTask);
        save();
        return newSubtask;
    }

    @Override
    public Epic createEpic(Epic epic) {
        Epic newEpic = super.createEpic(epic);
        save();
        return newEpic;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteSubTask(int id) {
        super.deleteSubTask(id);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }
}
