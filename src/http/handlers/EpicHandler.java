package http.handlers;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import model.Epic;
import service.TaskManager;

import java.io.IOException;

public class EpicHandler extends BaseHttpHandler {
    public EpicHandler(TaskManager taskManager) {
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
                    response = gson.toJson(taskManager.getAllEpics());
                    sendText(httpExchange, response, 200);
                } else if (pathParts.length == 3) {
                    try {
                        int id = Integer.parseInt(pathParts[2]);
                        Epic epic = taskManager.getEpic(id);
                        if (epic != null) {
                            response = gson.toJson(epic);
                            sendText(httpExchange, response, 200);
                        } else {
                            sendNotFound(httpExchange);
                        }
                    } catch (StringIndexOutOfBoundsException | NumberFormatException e) {
                        sendIncorrect(httpExchange);
                    }
                } else if (pathParts.length == 4 && pathParts[3].equals("subtasks")) {
                    try {
                        int id = Integer.parseInt(pathParts[2]);
                        Epic epic = taskManager.getEpic(id);
                        if (epic != null) {
                            response = gson.toJson(epic);
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
                    Epic epic = gson.fromJson(bodyRequest, Epic.class);
                    if (taskManager.getEpic(epic.getId()) != null) {
                        sendIncorrect(httpExchange);
                    } else {
                        Epic epicCreated = taskManager.createEpic(epic);
                        int idCreated = epicCreated.getId();
                        sendText(httpExchange, "Создан эпик с id=" + idCreated, 201);
                    }
                } catch (JsonSyntaxException e) {
                    sendIncorrect(httpExchange);
                }
                break;
            case "DELETE":
                if (pathParts.length == 2) {
                    taskManager.removeAllEpics();
                    sendText(httpExchange, "Все эпики удалены успешно", 204);
                } else if (pathParts.length == 3) {
                    try {
                        int id = Integer.parseInt(pathParts[2]);
                        taskManager.deleteEpic(id);
                        sendText(httpExchange, "Эпик удален успешно", 204);
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