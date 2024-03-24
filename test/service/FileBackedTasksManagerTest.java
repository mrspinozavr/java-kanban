package service;

import model.Epic;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTasksManagerTest {
    @Test
    public void shouldSaveAndLoadEpicWithoutSubtasks() {
        Path path = Path.of("src/resources/epicWithoutSubtasks.csv");
        File file = new File(String.valueOf(path));
        Epic epic = new Epic("name", "description");
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        manager.createEpic(epic);
        manager.addToHistory(epic.getId());
        manager.save();
        FileBackedTaskManager fileBackedTasksManager = FileBackedTaskManager.loadFromFile(file);
        Epic epicById = fileBackedTasksManager.getEpic(epic.getId());
        assertEquals(epic, epicById);
        assertArrayEquals(Collections.emptyList().toArray(), epicById.getSubTasks().toArray());
    }

    @Test
    public void shouldSaveEmptyHistory() {
        Path path = Path.of("src/resources/empty.data.csv");
        File file = new File(String.valueOf(path));
        FileBackedTaskManager fileBackedTasksManager = new FileBackedTaskManager(file);
        assertEquals(Collections.emptyList(), fileBackedTasksManager.getHistory());
    }


    @Test
    public void shouldLoadEmptyListOfTasks() {
        Path path = Path.of("src/resources/empty.data.csv");
        File file = new File(String.valueOf(path));
        FileBackedTaskManager fileBackedTasksManager = FileBackedTaskManager.loadFromFile(file);
        assertEquals(Collections.emptyList(), fileBackedTasksManager.getAllTasks());
    }

    @Test
    public void shouldLoadEmptyHistory() {
        Path path = Path.of("src/resources/empty.data.csv");
        File file = new File(String.valueOf(path));
        FileBackedTaskManager fileBackedTasksManager = FileBackedTaskManager.loadFromFile(file);
        assertEquals(Collections.emptyList(), fileBackedTasksManager.getHistory());
    }
}
