package http.handlers;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import exception.ValidationException;
import model.Task;
import service.TaskManager;

import java.io.IOException;

public class TaskHandler extends BaseHttpHandler {

    public TaskHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        String path = httpExchange.getRequestURI().getPath();

        String[] pathParts = path.split("/");

        switch (method) {
            case "GET":
                if (pathParts.length == 2) {
                    response = gson.toJson(taskManager.getAllTasks());
                    sendText(httpExchange, response, 200);
                } else if (pathParts.length == 3) {
                    try {
                        int id = Integer.parseInt(pathParts[2]);
                        Task task = taskManager.getTask(id);
                        if (task != null) {
                            response = gson.toJson(task);
                            sendText(httpExchange, response, 200);
                        } else {
                            sendNotFound(httpExchange);
                        }
                    } catch (StringIndexOutOfBoundsException | NumberFormatException e) {
                        sendIncorrect(httpExchange);
                    }
                }
                break;
            case "POST":
                String bodyRequest = readText(httpExchange);
                if (bodyRequest.isEmpty()) {
                    sendIncorrect(httpExchange);
                    return;
                }
                try {
                    Task task = gson.fromJson(bodyRequest, Task.class);
                    int id = task.getId();
                    if (taskManager.getTask(task.getId()) != null) {
                        taskManager.updateTask(task);
                        sendText(httpExchange, "Задача с id=" + id + " обновлена", 201);
                    } else {
                        Task taskCreated = taskManager.createTask(task);
                        int idCreated = taskCreated.getId();
                        sendText(httpExchange, "Создана задача с id=" + idCreated, 201);
                    }
                } catch (ValidationException v) {
                    sendHasInteractions(httpExchange);
                } catch (JsonSyntaxException e) {
                    sendIncorrect(httpExchange);
                }
                break;
            case "DELETE":
                if (pathParts.length == 2) {
                    taskManager.removeAllTasks();
                    sendText(httpExchange, "Все задачи удалены успешно", 204);
                } else if (pathParts.length == 3) {
                    try {
                        int id = Integer.parseInt(pathParts[2]);
                        taskManager.deleteTask(id);
                        sendText(httpExchange, "Задача удалена успешно", 204);
                    } catch (StringIndexOutOfBoundsException | NumberFormatException e) {
                        sendIncorrect(httpExchange);
                    }
                }
                break;
            default:
                sendIncorrect(httpExchange);
        }
    }
}
