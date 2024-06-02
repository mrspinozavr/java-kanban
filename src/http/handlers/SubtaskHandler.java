package http.handlers;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import exception.ValidationException;
import model.SubTask;
import service.TaskManager;

import java.io.IOException;

public class SubtaskHandler extends BaseHttpHandler {

    public SubtaskHandler(TaskManager taskManager) {
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
                    response = gson.toJson(taskManager.getAllSubTasks());
                    sendText(httpExchange, response, 200);
                } else if (pathParts.length == 3) {
                    try {
                        int id = Integer.parseInt(pathParts[2]);
                        SubTask subtask = taskManager.getSubTask(id);
                        if (subtask != null) {
                            response = gson.toJson(subtask);
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
                    SubTask subtask = gson.fromJson(bodyRequest, SubTask.class);
                    int id = subtask.getId();
                    if (taskManager.getSubTask(subtask.getId()) != null) {
                        System.out.println("Пытаемся обновить подзадачу");
                        taskManager.updateSubTask(subtask);
                        sendText(httpExchange, "Подзадача с id=" + id + " обновлена", 200);
                    } else {
                        SubTask subtaskCreated = taskManager.createSubTask(subtask);
                        int idCreated = subtaskCreated.getId();
                        sendText(httpExchange, "Создана подзадача с id=" + idCreated, 201);
                    }
                } catch (ValidationException v) {
                    sendHasInteractions(httpExchange);
                } catch (JsonSyntaxException e) {
                    sendIncorrect(httpExchange);
                }
                break;
            case "DELETE":
                if (pathParts.length == 2) {
                    taskManager.removeAllSubtasks();
                    sendText(httpExchange, "Все подзадачи удалены успешно", 204);
                } else if (pathParts.length == 3) {
                    try {
                        int id = Integer.parseInt(pathParts[2]);
                        taskManager.deleteSubTask(id);
                        sendText(httpExchange, "Подзадача удалена успешно", 204);
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