package http.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class BaseHttpHandler implements HttpHandler {
    protected String response;
    protected Gson gson = Managers.getGson();
    protected TaskManager taskManager;
    protected static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public BaseHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    protected void sendText(HttpExchange httpExchange, String text, int statusCode) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        httpExchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        httpExchange.sendResponseHeaders(statusCode, 0);
        httpExchange.getResponseBody().write(resp);
        httpExchange.close();
    }

    protected void sendNotFound(HttpExchange httpExchange) throws IOException {
        byte[] resp = "Задача с данным id не найдена".getBytes(StandardCharsets.UTF_8);
        httpExchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        httpExchange.sendResponseHeaders(404, 0);
        httpExchange.getResponseBody().write(resp);
        httpExchange.close();
    }

    protected void sendHasInteractions(HttpExchange httpExchange) throws IOException {
        byte[] resp = "Задача пересекаетя по времени с уже существующей".getBytes(StandardCharsets.UTF_8);
        httpExchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        httpExchange.sendResponseHeaders(406, 0);
        httpExchange.getResponseBody().write(resp);
        httpExchange.close();
    }

    protected void sendIncorrect(HttpExchange httpExchange) throws IOException {
        byte[] resp = "Некорректный запрос".getBytes(StandardCharsets.UTF_8);
        httpExchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        httpExchange.sendResponseHeaders(400, 0);
        httpExchange.getResponseBody().write(resp);
        httpExchange.close();
    }

    protected String readText(HttpExchange httpExchange) throws IOException {
        return new String(httpExchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
    }
}