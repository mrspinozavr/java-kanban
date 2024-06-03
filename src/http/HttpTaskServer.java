package http;

import com.sun.net.httpserver.HttpServer;
import http.handlers.*;
import service.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private final HttpServer httpServer;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.httpServer = HttpServer.create(new InetSocketAddress(8080), 0);

        httpServer.createContext("/tasks", new TaskHandler(taskManager));
        httpServer.createContext("/subtasks", new SubtaskHandler(taskManager));
        httpServer.createContext("/epics", new EpicHandler(taskManager));
        httpServer.createContext("/history", new HistoryHandler(taskManager));
        httpServer.createContext("/prioritized", new PrioritizedTasksHandler(taskManager));

    }

    public void start() {
        httpServer.start();
        System.out.println("Сервер запущен.");
    }

    public void stop() {
        httpServer.stop(0);
        System.out.println("Сервер остановлен.");
    }
}
