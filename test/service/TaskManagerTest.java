package service;

import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;
    protected Task task1;
    protected Task task2;
    protected Task task3;
    protected Epic epic1;
    protected Epic epic2;
    protected SubTask subTask11;
    protected SubTask subTask12;
    protected SubTask subTask13;

    @BeforeEach
    void beforeEach() {
        task1 = new Task("Задача 1", "Закрыть 5 спринт", TaskStatus.NEW, LocalDateTime.of(2024, 5, 19, 0, 0), 25);
        task2 = new Task("Задача 2", "Не бросить Яндекс Практикум", TaskStatus.IN_PROGRESS, LocalDateTime.of(2024, 5, 18, 0, 0), 40);
        task3 = new Task("Задача 3", "Закрыть 8 спринт", TaskStatus.IN_PROGRESS, LocalDateTime.of(2024, 5, 22, 0, 0), 40);
        epic1 = new Epic("Выучить Java", "Начать учиться.");
        epic1.setId(1);
        epic2 = new Epic("Заняться поиском работы", "Составить резюме.");
        epic2.setId(2);
        subTask11 = new SubTask("Подзадача 1-1", "Записаться на курс", epic1.getId(), LocalDateTime.of(2024, 5, 17, 0, 0), 46);
        subTask12 = new SubTask("Подзадача 1-2", "Купить книгу", epic1.getId(), LocalDateTime.of(2024, 5, 16, 0, 0), 26);
        subTask13 = new SubTask("Подзадача 1-3", "Взять отпуск", epic1.getId(), LocalDateTime.of(2024, 5, 15, 0, 0), 40);
    }

    @DisplayName("Проверка получения списка всех задач")
    @Test
    void getAllTasks() {
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
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        taskManager.removeAllTasks();

        final List<Task> tasks = taskManager.getAllTasks();
        assertEquals(0, tasks.size(), "Задачи не удалены");
    }

    @DisplayName("Проверка получения задачи по id")
    @Test
    void getTaskById() {
        taskManager.createTask(task1);

        final int taskId = task1.getId();
        Task savedTask = taskManager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена");
        assertEquals(taskId, savedTask.getId(), "Id задач не равны");
    }

    @DisplayName("Проверка добавления задачи")
    @Test
    void addTask() {
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
        taskManager.createTask(task1);

        final int taskId = task1.getId(); // получили id первой задачи
        Task savedTask = taskManager.getTask(taskId); // сохранили задачу по id

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
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);

        taskManager.removeAllEpics();

        final List<Epic> epics = taskManager.getAllEpics();
        assertEquals(0, epics.size(), "Задачи не удалены");
    }

    @DisplayName("Проверка получения эпика по id")
    @Test
    void getEpicById() {
        taskManager.createEpic(epic1);

        final int idTask = epic1.getId();
        Epic savedEpic = taskManager.getEpic(idTask);

        assertNotNull(savedEpic, "Задача не найдена");
        assertEquals(idTask, savedEpic.getId(), "Id задач не равны");
    }

    @DisplayName("Проверка добавления эпика")
    @Test
    void createEpic() {
        taskManager.createEpic(epic1);

        final int idTask = epic1.getId();
        Epic savedEpic = taskManager.getEpic(idTask);

        assertNotNull(savedEpic, "Задача не найдена");
        assertEquals(epic1, savedEpic, "Задачи не совпадают");

        //final List<Epic> epics = taskManager.getAllEpics(); // сохранили все эпики
        //assertNotNull(epics, "Задачи не возвращаются");
        //assertEquals(1, epics.size(), "Неверное количество задач");
        //assertEquals(epic1, epics.get(0), "Задачи не совпадают");
    }

    /*@DisplayName("Проверка обновления эпика")
    @Test
    void updateEpic() {
        taskManager.createEpic(epic1);

        final int idTask = epic1.getId();
        Epic savedEpic = taskManager.getEpic(idTask);

        epic2.setId(savedEpic.getId()); // присвоили новой задаче id первой

        savedEpic = epic2;

        taskManager.updateEpic(savedEpic);

        assertNotNull(savedEpic, "Задача не найдена");
        assertEquals(epic2, savedEpic, "Задачи не совпадают");

        final List<Epic> epics = taskManager.getAllEpics();
        assertNotNull(epics, "Задачи не возвращаются");
        assertEquals(1, epics.size(), "Неверное количество задач");
        System.out.println(epic2);
        System.out.println(epics.get(0));
        assertEquals(epic2, epics.get(0), "Задачи не совпадают");
    }*/

    @DisplayName("Проверка удаления эпика по id")
    @Test
    void removeEpicById() {
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
        taskManager.createEpic(epic1);

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
        taskManager.createEpic(epic1);

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
        taskManager.createEpic(epic1);

        taskManager.createSubTask(subTask11);

        final int idTask = subTask11.getId();
        SubTask savedSubTask = taskManager.getSubTask(idTask);

        assertNotNull(savedSubTask, "Подзадача не найдена");
        assertEquals(subTask11, savedSubTask, "Подзадачи не совпадают");
    }

    @DisplayName("Проверка добавления подзадачи")
    @Test
    void createSubTask() {
        taskManager.createEpic(epic1);

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
        taskManager.createEpic(epic1);

        taskManager.createSubTask(subTask11);

        final int idTask = subTask11.getId();
        SubTask savedSubTask = taskManager.getSubTask(idTask);

        subTask12.setId(savedSubTask.getId()); // присвоили новой подзадаче id первой подзадачи

        savedSubTask = subTask12;
        taskManager.updateSubTask(savedSubTask);

        assertNotNull(savedSubTask, "Подзадача не найдена");
        assertEquals(subTask12, savedSubTask, "Подзадачи не совпадают");

        final List<SubTask> SubTasks = taskManager.getEpicSubTasks(epic1);

        assertNotNull(SubTasks, "Подзадачи не возвращаются");
        assertEquals(1, SubTasks.size(), "Неверное количество подзадач");
        assertNotEquals(subTask11, SubTasks.get(0), "Подзадачи не совпадают");
    }

    @DisplayName("Проверка удаления подзадачи по id")
    @Test
    void deleteSubTask() {
        taskManager.createEpic(epic1);

        taskManager.createSubTask(subTask11);

        final int idSubTask = subTask11.getId();
        taskManager.deleteSubTask(idSubTask);

        assertNull(taskManager.getSubTask(idSubTask), "Подзадача не удалена");
    }

    @DisplayName("Проверка получения списка подзадач эпика")
    @Test
    void SubTaskOfTheEpic() {
        taskManager.createEpic(epic1);

        taskManager.createSubTask(subTask11);
        taskManager.createSubTask(subTask12);
        taskManager.createSubTask(subTask13);

        final List<SubTask> SubTasks = taskManager.getEpicSubTasks(epic1);

        assertNotNull(SubTasks, "Подзадачи не возвращаются");
        assertEquals(3, SubTasks.size(), "Неверное количество подзадач");
        assertEquals(subTask11, SubTasks.get(0), "Подзадачи не совпадают");
    }

    @DisplayName("Проверка приоритета задач")
    @Test
    void getPrioritizedTasks() {
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);

        Task[] tasks = new Task[3];
        tasks = taskManager.getPrioritizedTasks().toArray(tasks);

        assertNotNull(tasks, "задачи не возвращаются");
        assertEquals(3, tasks.length, "Неверное количество задач");
        assertEquals(task2, tasks[0], "Задачи не совпадают");
    }

    @DisplayName("Проверка статуса эпика если у трех подзадач статус NEW")
    @Test
    void testStatusThreeNew() {
        taskManager.createEpic(epic1);
        subTask11.setStatus(TaskStatus.NEW);
        subTask12.setStatus(TaskStatus.NEW);
        subTask13.setStatus(TaskStatus.NEW);

        taskManager.createSubTask(subTask11);
        taskManager.createSubTask(subTask12);
        taskManager.createSubTask(subTask13);

        assertEquals(epic1.getStatus(), TaskStatus.NEW, "Неверный статус задачи");
    }

    @DisplayName("Проверка статуса эпика если у трех подзадач статус IN_PROGRESS")
    @Test
    void testStatusThreeInProgress() {
        taskManager.createEpic(epic1);
        subTask11.setStatus(TaskStatus.IN_PROGRESS);
        subTask12.setStatus(TaskStatus.IN_PROGRESS);
        subTask13.setStatus(TaskStatus.IN_PROGRESS);

        taskManager.createSubTask(subTask11);
        taskManager.createSubTask(subTask12);
        taskManager.createSubTask(subTask13);

        assertEquals(epic1.getStatus(), TaskStatus.IN_PROGRESS, "Неверный статус задачи");
    }

    @DisplayName("Проверка статуса эпика если у трех подзадач статус DONE")
    @Test
    void testStatusThreeDone() {
        taskManager.createEpic(epic1);
        subTask11.setStatus(TaskStatus.DONE);
        subTask12.setStatus(TaskStatus.DONE);
        subTask13.setStatus(TaskStatus.DONE);

        taskManager.createSubTask(subTask11);
        taskManager.createSubTask(subTask12);
        taskManager.createSubTask(subTask13);

        assertEquals(epic1.getStatus(), TaskStatus.DONE, "Неверный статус задачи");
    }

    @DisplayName("Проверка статуса эпика если у двух подзадач статус NEW и у одной DONE")
    @Test
    void testStatusTwoNew() {
        taskManager.createEpic(epic1);
        subTask11.setStatus(TaskStatus.NEW);
        subTask11.setStatus(TaskStatus.NEW);
        subTask11.setStatus(TaskStatus.DONE);

        taskManager.createSubTask(subTask11);
        taskManager.createSubTask(subTask12);
        taskManager.createSubTask(subTask13);

        assertEquals(epic1.getStatus(), TaskStatus.IN_PROGRESS, "Неверный статус задачи");
    }

}
