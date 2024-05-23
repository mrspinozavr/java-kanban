package service;

import model.Epic;
import model.SubTask;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTasksManagerTest {
    @Test
    public void shouldSaveAndLoad() {
        Path path = Path.of("src/resources/test.csv");
        File file = new File(String.valueOf(path));
        Epic epic = new Epic("name", "description");
        FileBackedTaskManager manager = new FileBackedTaskManager(Managers.getDefaultHistoryManager(), file);
        manager.createEpic(epic);
        SubTask subtask11 = new SubTask("Подзадача 1-1", "Записаться на курс", epic.getId(), LocalDateTime.of(2024, 5, 19, 0, 0), 30);
        manager.createSubTask(subtask11);
        SubTask subTask12 = new SubTask("Подзадача 1-2", "Купить книгу", epic.getId(), LocalDateTime.of(2024, 5, 20, 0, 0), 26);
        manager.createSubTask(subTask12);
        manager.addToHistory(epic.getId());
        manager.addToHistory(subtask11.getId());
        manager.addToHistory(subTask12.getId());
        manager.loadFromFile(file);
        Epic epicById = manager.getEpic(epic.getId());
        assertEquals(epic, epicById);
        assertArrayEquals(epic.getSubTasks().toArray(), epicById.getSubTasks().toArray());
    }

    @Test
    public void shouldSaveEmptyHistory() {
        Path path = Path.of("src/resources/empty.data.csv");
        File file = new File(String.valueOf(path));
        FileBackedTaskManager manager = new FileBackedTaskManager(Managers.getDefaultHistoryManager(), file);
        assertEquals(Collections.emptyList(), manager.getHistory());
    }

    @Test
    public void shouldLoadEmptyListOfTasks() {
        Path path = Path.of("src/resources/empty.data.csv");
        File file = new File(String.valueOf(path));
        FileBackedTaskManager manager = new FileBackedTaskManager(Managers.getDefaultHistoryManager(), file);
        manager.loadFromFile(file);
        assertEquals(Collections.emptyList(), manager.getAllTasks());
    }

    @Test
    public void shouldLoadEmptyHistory() {
        Path path = Path.of("src/resources/empty.data.csv");
        File file = new File(String.valueOf(path));
        FileBackedTaskManager manager = new FileBackedTaskManager(Managers.getDefaultHistoryManager(), file);
        manager.loadFromFile(file);
        assertEquals(Collections.emptyList(), manager.getHistory());
    }
}
