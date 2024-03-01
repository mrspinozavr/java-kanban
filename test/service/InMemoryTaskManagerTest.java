package service;

import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    InMemoryTaskManager taskManager = new InMemoryTaskManager(Managers.getDefaultHistory());

    // Task
    @DisplayName("Проверка получения списка всех задач")
    @Test
    void getAllTasks() {
        Task task1 = new Task("Задача 1", "Закрыть 5 спринт", TaskStatus.NEW);
        Task task2 = new Task("Задача 2", "Не бросить Яндекс Практикум", TaskStatus.IN_PROGRESS);

        taskManager.createTask(task1);
        taskManager.createTask(task2);

        final List<Task> tasks = taskManager.getAllTasks();

        assertNotNull(tasks, "Задачи не возвращаются");
        assertEquals(2, tasks.size(), "Неверное количество задач");
        assertEquals(task1, tasks.get(0), "Задачи не совпадают");
    }

    @DisplayName("Проверка удаления списка всех задач")
    @Test
    void removeAllTasks() {
        Task task1 = new Task("Задача 1", "Закрыть 5 спринт", TaskStatus.NEW);
        Task task2 = new Task("Задача 2", "Не бросить Яндекс Практикум", TaskStatus.IN_PROGRESS);

        taskManager.createTask(task1);
        taskManager.createTask(task2);

        taskManager.removeAllTasks();

        final List<Task> tasks = taskManager.getAllTasks();
        assertEquals(0, tasks.size(), "Задачи не удалены");
    }

    @DisplayName("Проверка получения задачи по id")
    @Test
    void getTaskById() {
        Task task1 = new Task("Задача 1", "Закрыть 5 спринт", TaskStatus.NEW);
        taskManager.createTask(task1);

        final int taskId = task1.getId();
        Task savedTask = taskManager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена");
        assertEquals(taskId, savedTask.getId(), "Id задач не равны");
    }

    @DisplayName("Проверка добавления задачи")
    @Test
    void addTask() {
        Task task1 = new Task("Задача 1", "Закрыть 5 спринт", TaskStatus.NEW);
        taskManager.createTask(task1);

        int taskId = task1.getId(); // получили id задачи
        Task savedTask = taskManager.getTask(taskId); // получили задачу по Id

        assertNotNull(savedTask, "Задача не найдена"); // проверяем что задача получена
        assertEquals(task1, savedTask, "Задачи не совпадают"); // пройден, если это та же задача

        final List<Task> tasks = taskManager.getAllTasks(); // получили все задачи
        assertNotNull(tasks, "Задачи не возвращаются");
        assertEquals(1, tasks.size(), "Неверное количество задач");
        assertEquals(task1, tasks.get(0), "Задачи не совпадают");
    }

    @DisplayName("Проверка обновления задачи")
    @Test
    void updateTask() {
        Task task1 = new Task("Задача 1", "Закрыть 5 спринт", TaskStatus.NEW);
        taskManager.createTask(task1);

        final int taskId = task1.getId(); // получили id первой задачи
        Task savedTask = taskManager.getTask(taskId); // сохранили задачу по id

        Task task2 = new Task("Задача 2", "Не бросить Яндекс Практикум", TaskStatus.IN_PROGRESS);
        task2.setId(savedTask.getId()); // установили для новой задачи id первой

        savedTask = task2; // присвоили сохраненной задаче вторую задачу

        taskManager.updateTask(savedTask); // обновили задачу

        assertNotNull(savedTask, "Задача не найдена");
        assertEquals(task2, savedTask, "Задачи не совпадают");

        final List<Task> tasks = taskManager.getAllTasks(); // сохранили все задачи
        assertNotNull(tasks, "Задачи не возвращаются");
        assertEquals(1, tasks.size(), "Неверное количество задач");
        assertEquals(task2, tasks.get(0), "Задачи не совпадают");
    }

    @DisplayName("Проверка удаления задачи по id")
    @Test
    void removeTaskById() {
        Task task1 = new Task("Задача 1", "Закрыть 5 спринт", TaskStatus.NEW);
        Task task2 = new Task("Задача 2", "Не бросить Яндекс Практикум", TaskStatus.IN_PROGRESS);
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        final int taskId = task1.getId(); // получили id задачи, которую собираемся удалять
        taskManager.deleteTask(taskId); // удалили задачу

        final List<Task> tasks = taskManager.getAllTasks(); // сохранили оставшиеся задачи

        assertNotNull(tasks, "Задачи не возвращаются");
        assertEquals(1, tasks.size(), "Неверное количество задач");
        assertEquals(task2, tasks.get(0), "Задачи не совпадают"); // т.к. первую удалили
    }

    // Epic
    @DisplayName("Проверка получения всех эпиков")
    @Test
    void getAllEpics() {
        Epic epic1 = new Epic("Выучить Java", "Начать учиться.");
        Epic epic2 = new Epic("Заняться поиском работы", "Составить резюме.");
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);

        final List<Epic> epics = taskManager.getAllEpics(); // сохранили все эпики
        assertNotNull(epics, "Задачи не возвращаются");
        assertEquals(2, epics.size(), "Неверное количество задач");
        assertEquals(epic1, epics.get(0), "Задачи не совпадают");
    }

    @DisplayName("Проверка удаления всех эпиков")
    @Test
    void removeAllEpics() {
        Epic epic1 = new Epic("Выучить Java", "Начать учиться.");
        Epic epic2 = new Epic("Заняться поиском работы", "Составить резюме.");
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);

        taskManager.removeAllEpics();

        final List<Epic> epics = taskManager.getAllEpics();
        assertEquals(0, epics.size(), "Задачи не удалены");
    }

    @DisplayName("Проверка получения эпика по id")
    @Test
    void getEpicById() {
        Epic epic1 = new Epic("Выучить Java", "Начать учиться.");
        taskManager.createEpic(epic1);

        final int idTask = epic1.getId();
        Epic savedEpic = taskManager.getEpic(idTask);

        assertNotNull(savedEpic, "Задача не найдена");
        assertEquals(idTask, savedEpic.getId(), "Id задач не равны");
    }

    @DisplayName("Проверка добавления эпика")
    @Test
    void createEpic() {
        Epic epic1 = new Epic("Выучить Java", "Начать учиться.");
        taskManager.createEpic(epic1);

        final int idTask = epic1.getId();
        Epic savedEpic = taskManager.getEpic(idTask);

        assertNotNull(savedEpic, "Задача не найдена");
        assertEquals(epic1, savedEpic, "Задачи не совпадают");

        final List<Epic> epics = taskManager.getAllEpics(); // сохранили все эпики
        assertNotNull(epics, "Задачи не возвращаются");
        assertEquals(1, epics.size(), "Неверное количество задач");
        assertEquals(epic1, epics.get(0), "Задачи не совпадают");
    }

    @DisplayName("Проверка обновления эпика")
    @Test
    void updateEpic() {
        Epic epic1 = new Epic("Выучить Java", "Начать учиться.");
        taskManager.createEpic(epic1);

        final int idTask = epic1.getId();
        Epic savedEpic = taskManager.getEpic(idTask);

        Epic epic2 = new Epic("Заняться поиском работы", "Составить резюме.");
        epic2.setId(savedEpic.getId()); // присвоили новой задаче id первой

        savedEpic = epic2;

        taskManager.updateEpic(savedEpic);

        assertNotNull(savedEpic, "Задача не найдена");
        assertEquals(epic2, savedEpic, "Задачи не совпадают");

        final List<Epic> epics = taskManager.getAllEpics();
        assertNotNull(epics, "Задачи не возвращаются");
        assertEquals(1, epics.size(), "Неверное количество задач");
        assertEquals(epic2, epics.get(0), "Задачи не совпадают");
    }

    @DisplayName("Проверка удаления эпика по id")
    @Test
    void removeEpicById() {
        Epic epic1 = new Epic("Выучить Java", "Начать учиться.");
        Epic epic2 = new Epic("Заняться поиском работы", "Составить резюме.");
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);

        final int idTask = epic1.getId();
        taskManager.deleteEpic(idTask);

        final List<Epic> epics = taskManager.getAllEpics();
        assertNotNull(epics, "Задачи не возвращаются");
        assertEquals(1, epics.size(), "Неверное количество задач");
        assertEquals(epic2, epics.get(0), "Задачи не совпадают");
    }

    // SubTask
    @DisplayName("Проверка получения всех подзадач")
    @Test
    void getEpicSubTasks() {
        Epic epic1 = new Epic("Выучить Java", "Начать учиться.");
        taskManager.createEpic(epic1);

        SubTask subTask11 = new SubTask("Подзадача 1-1", "Записаться на курс", epic1);
        SubTask subTask12 = new SubTask("Подзадача 1-2", "Купить книгу", epic1);
        SubTask subTask13 = new SubTask("Подзадача 1-3", "Взять отпуск", epic1);

        taskManager.createSubTask(subTask11);
        taskManager.createSubTask(subTask12);
        taskManager.createSubTask(subTask13);

        final List<SubTask> SubTasks = taskManager.getEpicSubTasks(epic1); //сохранили все подзадачи в список

        assertNotNull(SubTasks, "Подзадачи не возвращаются");
        assertEquals(3, SubTasks.size(), "Неверное количество подзадач");
        assertEquals(subTask11, SubTasks.get(0), "Подзадачи не совпадают");
    }

    @DisplayName("Проверка удаления всех подзадач")
    @Test
    void removeAllSubTask() {
        Epic epic1 = new Epic("Выучить Java", "Начать учиться.");
        taskManager.createEpic(epic1);

        SubTask subTask11 = new SubTask("Подзадача 1-1", "Записаться на курс", epic1);
        SubTask subTask12 = new SubTask("Подзадача 1-2", "Купить книгу", epic1);
        SubTask subTask13 = new SubTask("Подзадача 1-3", "Взять отпуск", epic1);

        taskManager.createSubTask(subTask11);
        taskManager.createSubTask(subTask12);
        taskManager.createSubTask(subTask13);

        taskManager.removeAllSubtasks(); // удалили все подзадачи

        final List<SubTask> SubTasks = taskManager.getEpicSubTasks(epic1); //сохранили все подзадачи в список

        assertEquals(0, SubTasks.size(), "Подзадачи не удалены");
    }

    @DisplayName("Проверка получения подзадачи по id")
    @Test
    void getSubTask() {
        Epic epic1 = new Epic("Выучить Java", "Начать учиться.");
        taskManager.createEpic(epic1);

        SubTask subTask11 = new SubTask("Подзадача 1-1", "Записаться на курс", epic1);
        ;
        taskManager.createSubTask(subTask11);

        final int idTask = subTask11.getId();
        SubTask savedSubTask = taskManager.getSubTask(idTask);

        assertNotNull(savedSubTask, "Подзадача не найдена");
        assertEquals(subTask11, savedSubTask, "Подзадачи не совпадают");
    }

    @DisplayName("Проверка добавления подзадачи")
    @Test
    void createSubTask() {
        Epic epic1 = new Epic("Эпик 1", "Большой переезд."); // id Task 3
        taskManager.createEpic(epic1);

        SubTask subTask11 = new SubTask("Подзадача 1-1", "Записаться на курс", epic1);
        SubTask subTask12 = new SubTask("Подзадача 1-2", "Купить книгу", epic1);
        SubTask subTask13 = new SubTask("Подзадача 1-3", "Взять отпуск", epic1);

        taskManager.createSubTask(subTask11);
        taskManager.createSubTask(subTask12);
        taskManager.createSubTask(subTask13);

        final int idTask = subTask11.getId();
        SubTask savedSubTask = taskManager.getSubTask(idTask);

        assertNotNull(savedSubTask, "Подзадача не найдена");
        assertEquals(subTask11, savedSubTask, "Подзадачи не совпадают");

        final List<SubTask> SubTasks = taskManager.getEpicSubTasks(epic1);

        assertNotNull(SubTasks, "Подзадачи не возвращаются");
        assertEquals(3, SubTasks.size(), "Неверное количество подзадач");
        assertEquals(subTask11, SubTasks.get(0), "Подзадачи не совпадают");
    }

    @DisplayName("Проверка обновления подзадачи")
    @Test
    void updateSubTask() {
        Epic epic1 = new Epic("Выучить Java", "Начать учиться.");
        taskManager.createEpic(epic1);

        SubTask subTask11 = new SubTask("Подзадача 1-1", "Записаться на курс", epic1);
        taskManager.createSubTask(subTask11);

        final int idTask = subTask11.getId();
        SubTask savedSubTask = taskManager.getSubTask(idTask);

        SubTask subTask12 = new SubTask("Подзадача 1-2", "Купить книгу", epic1);
        subTask12.setId(savedSubTask.getId()); // присвоили новой подзадаче id первой подзадачи

        savedSubTask = subTask12;
        taskManager.updateSubTask(savedSubTask);

        assertNotNull(savedSubTask, "Подзадача не найдена");
        assertEquals(subTask12, savedSubTask, "Подзадачи не совпадают");

        final List<SubTask> SubTasks = taskManager.getEpicSubTasks(epic1);

        assertNotNull(SubTasks, "Подзадачи не возвращаются");
        assertEquals(1, SubTasks.size(), "Неверное количество подзадач");
        assertEquals(subTask11, SubTasks.get(0), "Подзадачи не совпадают");
    }

    @DisplayName("Проверка удаления подзадачи по id")
    @Test
    void deleteSubTask() {
        Epic epic1 = new Epic("Выучить Java", "Начать учиться.");
        taskManager.createEpic(epic1);

        SubTask subTask11 = new SubTask("Подзадача 1-1", "Записаться на курс", epic1);
        taskManager.createSubTask(subTask11);

        final int idSubTask = subTask11.getId();
        taskManager.deleteSubTask(idSubTask);

        assertNull(taskManager.getSubTask(idSubTask), "Подзадача не удалена");
    }

    @DisplayName("Проверка получения списка подзадач эпика")
    @Test
    void SubTaskOfTheEpic() {
        Epic epic1 = new Epic("Выучить Java", "Начать учиться.");
        taskManager.createEpic(epic1);

        SubTask subTask11 = new SubTask("Подзадача 1-1", "Записаться на курс", epic1);
        SubTask subTask12 = new SubTask("Подзадача 1-2", "Купить книгу", epic1);
        SubTask subTask13 = new SubTask("Подзадача 1-3", "Взять отпуск", epic1);

        taskManager.createSubTask(subTask11);
        taskManager.createSubTask(subTask12);
        taskManager.createSubTask(subTask13);

        final List<SubTask> SubTasks = taskManager.getEpicSubTasks(epic1);

        assertNotNull(SubTasks, "Подзадачи не возвращаются");
        assertEquals(3, SubTasks.size(), "Неверное количество подзадач");
        assertEquals(subTask11, SubTasks.get(0), "Подзадачи не совпадают");
    }
}