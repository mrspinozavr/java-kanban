package service;

import model.Task;
import model.Epic;
import model.SubTask;
import model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    InMemoryHistoryManager historyManager;
    Task task;
    Epic epic;
    SubTask subtask;

    @BeforeEach
    public void beforeEach() {
        historyManager = new InMemoryHistoryManager();
        task = new Task("Задача 1", "Закрыть 5 спринт", TaskStatus.NEW);
        task.setId(1);
        epic = new Epic("Выучить Java", "Начать учиться.");
        epic.setId(2);
        subtask = new SubTask("Подзадача 1-1", "Записаться на курс", epic);
        subtask.setId(3);
    }

    @DisplayName("Проверка добавления истории")
    @Test
    void add() {
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не возвращается.");
        assertEquals(1, history.size(), "Неверное количество подзадач.");
        assertEquals(List.of(task), history, "Вернулся список не тех задач");
    }

    @DisplayName("Попытка добавить повторный просмотр задачи в историю")
    @Test
    void addDoubleTask() {
        historyManager.add(task);
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не возвращается.");
        assertEquals(1, history.size(), "Вернулось неверное количество задач.");
        assertEquals(List.of(task), history, "Вернулся список не тех задач");
    }

    @DisplayName("Удаление заглавной (head) задачи из истории")
    @Test
    void removeFromHead(){
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subtask);
        historyManager.remove(task.getId());
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не возвращается.");
        assertEquals(2, history.size(), "Вернулось неверное количество задач.");
        assertEquals(List.of(epic, subtask), history, "Вернулся список не тех задач");
    }

    @DisplayName("Удаление задачи из середины истории")
    @Test
    void removeFromMiddle(){
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subtask);
        historyManager.remove(epic.getId());
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не возвращается.");
        assertEquals(2, history.size(), "Вернулось неверное количество задач.");
        assertEquals(List.of(task, subtask), history, "Вернулся список не тех задач");
    }

    @DisplayName("Удаление конечной (tail) задачи из истории")
    @Test
    void removeFromTail(){
        historyManager.add(epic);
        historyManager.add(subtask);
        historyManager.add(task);
        historyManager.remove(task.getId());
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не возвращается.");
        assertEquals(2, history.size(), "Вернулось неверное количество задач.");
        assertEquals(List.of(epic, subtask), history, "Вернулся список не тех задач");
    }
}