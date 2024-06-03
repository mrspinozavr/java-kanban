package service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import http.HttpTaskServer;
import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HttpTaskServerTest {
    private static HttpTaskServer taskServer;
    private final HttpClient client = HttpClient.newHttpClient();
    private static InMemoryTaskManager taskManager;
    private static final Gson gson = Managers.getGson();
    private static final String TASK_BASE_URL = "http://localhost:8080/tasks";
    private static final String EPIC_BASE_URL = "http://localhost:8080/epics";
    private static final String SUBTASK_BASE_URL = "http://localhost:8080/subtasks";
    private static final String HISTORY_BASE_URL = "http://localhost:8080/history";
    private static final String PRIORITIZED_BASE_URL = "http://localhost:8080/prioritized";

    Type taskType = new TypeToken<ArrayList<Task>>() {
    }.getType();
    Type epicType = new TypeToken<ArrayList<Epic>>() {
    }.getType();
    Type subtaskType = new TypeToken<ArrayList<SubTask>>() {
    }.getType();

    @BeforeEach
    void startServer() {
        try {
            taskManager = (InMemoryTaskManager) Managers.getInMemoryTaskManager(Managers.getDefaultHistoryManager());
            taskServer = new HttpTaskServer(taskManager);
            taskServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void stopServer() {
        taskServer.stop();
    }

    @Test
    void shouldGetTasks() throws IOException, InterruptedException {
        Task task1 = new Task("Задача 1", "Закрыть 5 спринт", TaskStatus.NEW, LocalDateTime.of(2024, 5, 19, 0, 0), 25);
        Task task2 = new Task("Задача 2", "Не бросить Яндекс Практикум", TaskStatus.IN_PROGRESS, LocalDateTime.of(2024, 5, 18, 0, 0), 40);
        sendPOST(TASK_BASE_URL, task1);
        sendPOST(TASK_BASE_URL, task2);

        HttpResponse<String> response = sendGET(TASK_BASE_URL, 0);
        assertEquals(200, response.statusCode());
        List<Task> actual = gson.fromJson(response.body(), taskType);
        assertNotNull(actual);
        assertEquals(2, actual.size());
    }

    @Test
    void shouldGetEpics() throws IOException, InterruptedException {
        Epic epic1 = new Epic("description1", "name1");
        Epic epic2 = new Epic("description2", "name2");
        SubTask subTask11 = new SubTask("Подзадача 1-1", "Записаться на курс", 1, LocalDateTime.of(2024, 5, 17, 0, 0), 46);
        SubTask subTask21 = new SubTask("Подзадача 1-2", "Купить книгу", 2, LocalDateTime.of(2024, 5, 16, 0, 0), 26);
        sendPOST(EPIC_BASE_URL, epic1);
        sendPOST(EPIC_BASE_URL, epic2);
        sendPOST(SUBTASK_BASE_URL, subTask11);
        sendPOST(SUBTASK_BASE_URL, subTask21);

        HttpResponse<String> response = sendGET(EPIC_BASE_URL, 0);
        assertEquals(200, response.statusCode());
        List<Epic> actual = gson.fromJson(response.body(), epicType);
        assertNotNull(actual);
        assertEquals(2, actual.size());
    }

    @Test
    void shouldGetSubtasks() throws IOException, InterruptedException {
        Epic epic1 = new Epic("description1", "name1");
        SubTask subTask11 = new SubTask("Подзадача 1-1", "Записаться на курс", 1, LocalDateTime.of(2024, 5, 17, 0, 0), 46);
        SubTask subTask12 = new SubTask("Подзадача 1-2", "Купить книгу", 1, LocalDateTime.of(2024, 5, 16, 0, 0), 26);
        SubTask subTask13 = new SubTask("Подзадача 1-3", "Взять отпуск", 1, LocalDateTime.of(2024, 5, 15, 0, 0), 40);
        sendPOST(EPIC_BASE_URL, epic1);
        sendPOST(SUBTASK_BASE_URL, subTask11);
        sendPOST(SUBTASK_BASE_URL, subTask12);
        sendPOST(SUBTASK_BASE_URL, subTask13);

        HttpResponse<String> response = sendGET(SUBTASK_BASE_URL, 0);
        assertEquals(200, response.statusCode());
        List<Epic> actual = gson.fromJson(response.body(), subtaskType);
        assertNotNull(actual);
        assertEquals(3, actual.size());
    }

    @Test
    void shouldGetTaskById() throws IOException, InterruptedException {
        Task task1 = new Task("Задача 1", "Закрыть 5 спринт", TaskStatus.NEW, LocalDateTime.of(2024, 5, 19, 0, 0), 25);
        Task task2 = new Task("Задача 2", "Не бросить Яндекс Практикум", TaskStatus.IN_PROGRESS, LocalDateTime.of(2024, 5, 18, 0, 0), 40);
        sendPOST(TASK_BASE_URL, task1);
        sendPOST(TASK_BASE_URL, task2);

        HttpResponse<String> response = sendGET(TASK_BASE_URL, 2);
        assertEquals(200, response.statusCode());
        Task responseTask = gson.fromJson(response.body(), Task.class);
        assertEquals(task2.getDescription(), responseTask.getDescription());
    }

    @Test
    void shouldGetEpicById() throws IOException, InterruptedException {
        Epic epic1 = new Epic("description1", "name1");
        Epic epic2 = new Epic("description2", "name2");
        SubTask subTask11 = new SubTask("Подзадача 1-1", "Записаться на курс", 1, LocalDateTime.of(2024, 5, 17, 0, 0), 46);
        SubTask subTask21 = new SubTask("Подзадача 1-2", "Купить книгу", 2, LocalDateTime.of(2024, 5, 16, 0, 0), 26);
        sendPOST(EPIC_BASE_URL, epic1);
        sendPOST(EPIC_BASE_URL, epic2);
        sendPOST(SUBTASK_BASE_URL, subTask11);
        sendPOST(SUBTASK_BASE_URL, subTask21);

        HttpResponse<String> response = sendGET(EPIC_BASE_URL, 2);
        assertEquals(200, response.statusCode());
        Epic responseEpic = gson.fromJson(response.body(), Epic.class);
        assertEquals(epic2.getDescription(), responseEpic.getDescription());
    }

    @Test
    void shouldGetSubtaskById() throws IOException, InterruptedException {
        Epic epic1 = new Epic("description1", "name1");
        SubTask subTask11 = new SubTask("Подзадача 1-1", "Записаться на курс", 1, LocalDateTime.of(2024, 5, 17, 0, 0), 46);
        SubTask subTask12 = new SubTask("Подзадача 1-2", "Купить книгу", 1, LocalDateTime.of(2024, 5, 16, 0, 0), 26);
        SubTask subTask13 = new SubTask("Подзадача 1-3", "Взять отпуск", 1, LocalDateTime.of(2024, 5, 15, 0, 0), 40);
        sendPOST(EPIC_BASE_URL, epic1);
        sendPOST(SUBTASK_BASE_URL, subTask11);
        sendPOST(SUBTASK_BASE_URL, subTask12);
        sendPOST(SUBTASK_BASE_URL, subTask13);

        HttpResponse<String> response = sendGET(SUBTASK_BASE_URL, 4);
        assertEquals(200, response.statusCode());
        SubTask responseSubTask = gson.fromJson(response.body(), SubTask.class);
        assertEquals(subTask13.getDescription(), responseSubTask.getDescription());
    }

    @Test
    void shouldUpdateTask() throws IOException, InterruptedException {
        Task task1 = new Task("Задача 1", "Закрыть 5 спринт", TaskStatus.NEW, LocalDateTime.of(2024, 5, 19, 0, 0), 25);
        Task task2 = new Task("Задача 2", "Не бросить Яндекс Практикум", TaskStatus.IN_PROGRESS, LocalDateTime.of(2024, 5, 18, 0, 0), 40);
        sendPOST(TASK_BASE_URL, task1);
        sendPOST(TASK_BASE_URL, task2);

        HttpResponse<String> response = sendGET(TASK_BASE_URL, 2);
        assertEquals(200, response.statusCode());
        Task responseTask = gson.fromJson(response.body(), Task.class);
        responseTask.setStatus(TaskStatus.DONE);
        sendPOST(TASK_BASE_URL, responseTask);
        HttpResponse<String> responseAfterUpdate = sendGET(TASK_BASE_URL, 2);
        Task responseTaskAfterUpdate = gson.fromJson(responseAfterUpdate.body(), Task.class);
        assertEquals(responseTask.getStatus(), responseTaskAfterUpdate.getStatus());
    }

    @Test
    void shouldUpdateSubtask() throws IOException, InterruptedException {
        Epic epic1 = new Epic("description1", "name1");
        sendPOST(EPIC_BASE_URL, epic1);
        SubTask subTask11 = new SubTask("Подзадача 1-1", "Записаться на курс", 1, LocalDateTime.of(2024, 5, 17, 0, 0), 46);
        SubTask subTask12 = new SubTask("Подзадача 1-2", "Купить книгу", 1, LocalDateTime.of(2024, 5, 16, 0, 0), 26);
        SubTask subTask13 = new SubTask("Подзадача 1-3", "Взять отпуск", 1, LocalDateTime.of(2024, 5, 15, 0, 0), 40);
        sendPOST(SUBTASK_BASE_URL, subTask11);
        sendPOST(SUBTASK_BASE_URL, subTask12);
        sendPOST(SUBTASK_BASE_URL, subTask13);

        subTask13.setDescription("Изменили описание подзадачи");
        subTask13.setId(4);
        sendPOST(SUBTASK_BASE_URL, subTask13);

        HttpResponse<String> response = sendGET(SUBTASK_BASE_URL, 4);
        assertEquals(200, response.statusCode());
        SubTask updatedSubTask = gson.fromJson(response.body(), SubTask.class);
        assertEquals("Изменили описание подзадачи", updatedSubTask.getDescription());
    }

    @Test
    void shouldDeleteTasks() throws IOException, InterruptedException {
        Task task1 = new Task("Задача 1", "Закрыть 5 спринт", TaskStatus.NEW, LocalDateTime.of(2024, 5, 19, 0, 0), 25);
        Task task2 = new Task("Задача 2", "Не бросить Яндекс Практикум", TaskStatus.IN_PROGRESS, LocalDateTime.of(2024, 5, 18, 0, 0), 40);
        sendPOST(TASK_BASE_URL, task1);
        sendPOST(TASK_BASE_URL, task2);

        HttpResponse<String> response = sendDELETE(TASK_BASE_URL, 0);
        assertEquals(204, response.statusCode());
        assertTrue(taskManager.getAllTasks().isEmpty());
    }

    @Test
    void shouldDeleteEpics() throws IOException, InterruptedException {
        Epic epic1 = new Epic("description1", "name1");
        SubTask subTask11 = new SubTask("Подзадача 1-1", "Записаться на курс", 1, LocalDateTime.of(2024, 5, 17, 0, 0), 46);
        SubTask subTask12 = new SubTask("Подзадача 1-2", "Купить книгу", 1, LocalDateTime.of(2024, 5, 16, 0, 0), 26);
        SubTask subTask13 = new SubTask("Подзадача 1-3", "Взять отпуск", 1, LocalDateTime.of(2024, 5, 15, 0, 0), 40);
        sendPOST(EPIC_BASE_URL, epic1);
        sendPOST(SUBTASK_BASE_URL, subTask11);
        sendPOST(SUBTASK_BASE_URL, subTask12);
        sendPOST(SUBTASK_BASE_URL, subTask13);

        HttpResponse<String> response = sendDELETE(EPIC_BASE_URL, 0);
        assertEquals(204, response.statusCode());
        assertTrue(taskManager.getAllEpics().isEmpty());
    }

    @Test
    void shouldDeleteSubtasks() throws IOException, InterruptedException {
        Epic epic1 = new Epic("description1", "name1");
        SubTask subTask11 = new SubTask("Подзадача 1-1", "Записаться на курс", 1, LocalDateTime.of(2024, 5, 17, 0, 0), 46);
        SubTask subTask12 = new SubTask("Подзадача 1-2", "Купить книгу", 1, LocalDateTime.of(2024, 5, 16, 0, 0), 26);
        SubTask subTask13 = new SubTask("Подзадача 1-3", "Взять отпуск", 1, LocalDateTime.of(2024, 5, 15, 0, 0), 40);
        sendPOST(EPIC_BASE_URL, epic1);
        sendPOST(SUBTASK_BASE_URL, subTask11);
        sendPOST(SUBTASK_BASE_URL, subTask12);
        sendPOST(SUBTASK_BASE_URL, subTask13);

        HttpResponse<String> response = sendDELETE(SUBTASK_BASE_URL, 0);
        assertEquals(204, response.statusCode());

        HttpResponse<String> responseEpic = sendGET(EPIC_BASE_URL, 1);
        assertEquals(200, responseEpic.statusCode());
        Epic clearEpic = gson.fromJson(responseEpic.body(), Epic.class);
        assertTrue(clearEpic.getSubTasks().isEmpty());
        assertTrue(taskManager.getAllSubTasks().isEmpty());
    }

    @Test
    void shouldDeleteTaskById() throws IOException, InterruptedException {
        Task task1 = new Task("Задача 1", "Закрыть 5 спринт", TaskStatus.NEW, LocalDateTime.of(2024, 5, 19, 0, 0), 25);
        sendPOST(TASK_BASE_URL, task1);

        HttpResponse<String> response = sendDELETE(TASK_BASE_URL, 1);
        assertEquals(204, response.statusCode());
        assertTrue(taskManager.getAllTasks().isEmpty());
    }

    @Test
    void shouldDeleteEpicById() throws IOException, InterruptedException {
        Epic epic1 = new Epic("description1", "name1");
        Epic epic2 = new Epic("description2", "name2");
        sendPOST(EPIC_BASE_URL, epic1);
        sendPOST(EPIC_BASE_URL, epic2);

        HttpResponse<String> response1 = sendDELETE(EPIC_BASE_URL, 1);
        HttpResponse<String> response2 = sendDELETE(EPIC_BASE_URL, 2);
        assertEquals(204, response1.statusCode());
        assertEquals(204, response2.statusCode());
        assertTrue(taskManager.getAllEpics().isEmpty());
    }

    @Test
    void shouldDeleteSubtaskById() throws IOException, InterruptedException {
        Epic epic1 = new Epic("description1", "name1");
        SubTask subTask11 = new SubTask("Подзадача 1-1", "Записаться на курс", 1, LocalDateTime.of(2024, 5, 17, 0, 0), 46);
        SubTask subTask12 = new SubTask("Подзадача 1-2", "Купить книгу", 1, LocalDateTime.of(2024, 5, 16, 0, 0), 26);
        SubTask subTask13 = new SubTask("Подзадача 1-3", "Взять отпуск", 1, LocalDateTime.of(2024, 5, 15, 0, 0), 40);
        sendPOST(EPIC_BASE_URL, epic1);
        sendPOST(SUBTASK_BASE_URL, subTask11);
        sendPOST(SUBTASK_BASE_URL, subTask12);
        sendPOST(SUBTASK_BASE_URL, subTask13);

        HttpResponse<String> response1 = sendDELETE(SUBTASK_BASE_URL, 2);
        HttpResponse<String> response2 = sendDELETE(SUBTASK_BASE_URL, 3);
        HttpResponse<String> response3 = sendDELETE(SUBTASK_BASE_URL, 4);
        assertEquals(204, response1.statusCode());
        assertEquals(204, response2.statusCode());
        assertEquals(204, response3.statusCode());

        HttpResponse<String> responseEpic = sendGET(EPIC_BASE_URL, 1);
        assertEquals(200, responseEpic.statusCode());
        Epic clearEpic = gson.fromJson(responseEpic.body(), Epic.class);
        assertTrue(clearEpic.getSubTasks().isEmpty());
        assertTrue(taskManager.getAllSubTasks().isEmpty());
    }

    @Test
    void shouldGetHistory() throws IOException, InterruptedException {
        Epic epic1 = new Epic("description1", "name1");
        SubTask subTask11 = new SubTask("Подзадача 1-1", "Записаться на курс", 1, LocalDateTime.of(2024, 5, 17, 0, 0), 46);
        SubTask subTask12 = new SubTask("Подзадача 1-2", "Купить книгу", 1, LocalDateTime.of(2024, 5, 16, 0, 0), 26);
        SubTask subTask13 = new SubTask("Подзадача 1-3", "Взять отпуск", 1, LocalDateTime.of(2024, 5, 15, 0, 0), 40);
        sendPOST(EPIC_BASE_URL, epic1);
        sendPOST(SUBTASK_BASE_URL, subTask11);
        sendPOST(SUBTASK_BASE_URL, subTask12);
        sendPOST(SUBTASK_BASE_URL, subTask13);
        taskManager.addToHistory(1);
        taskManager.addToHistory(2);
        taskManager.addToHistory(3);
        taskManager.addToHistory(4);

        HttpResponse<String> response = sendGET(HISTORY_BASE_URL, 0);
        assertEquals(200, response.statusCode());
        List<Task> history = gson.fromJson(response.body(), taskType);
        assertNotNull(history, "История не возвращается");
    }

    @Test
    void shouldGetPrioritized() throws IOException, InterruptedException {
        Task task1 = new Task("Задача 1", "Закрыть 5 спринт", TaskStatus.NEW, LocalDateTime.of(2024, 5, 19, 0, 0), 25);
        Task task2 = new Task("Задача 2", "Не бросить Яндекс Практикум", TaskStatus.IN_PROGRESS, LocalDateTime.of(2024, 5, 18, 0, 0), 40);
        Task task3 = new Task("Задача 3", "Закрыть 8 спринт", TaskStatus.IN_PROGRESS, LocalDateTime.of(2024, 5, 22, 0, 0), 40);
        sendPOST(TASK_BASE_URL, task1);
        sendPOST(TASK_BASE_URL, task2);
        sendPOST(TASK_BASE_URL, task3);

        HttpResponse<String> response = sendGET(PRIORITIZED_BASE_URL, 0);
        assertEquals(200, response.statusCode());
        List<Task> tasks = gson.fromJson(response.body(), taskType);

        assertNotNull(tasks, "задачи не возвращаются");
        assertEquals(3, tasks.size(), "Неверное количество задач");
        assertEquals(task2.getDescription(), tasks.get(0).getDescription(), "Задачи не совпадают");
    }

    private HttpResponse<String> sendPOST(String path, Task task) throws IOException, InterruptedException {
        URI uri = URI.create(path);
        String json = gson.toJson(task);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(body)
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> sendGET(String path, int id) throws IOException, InterruptedException {
        HttpRequest request;
        URI uri;
        if (id == 0) {
            uri = URI.create(path);
        } else {
            uri = URI.create(path + "/" + id);
        }
        request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> sendDELETE(String path, int id) throws IOException, InterruptedException {
        HttpRequest request;
        URI uri;
        if (id == 0) {
            uri = URI.create(path);
        } else {
            uri = URI.create(path + "/" + id);
        }
        request = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}