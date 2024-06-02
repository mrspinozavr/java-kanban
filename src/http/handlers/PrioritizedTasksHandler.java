package http.handlers;

import com.sun.net.httpserver.HttpExchange;
import service.TaskManager;

import java.io.IOException;

public class PrioritizedTasksHandler extends BaseHttpHandler {

    public PrioritizedTasksHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();

        if (method.equals("GET")) {
            response = gson.toJson(taskManager.getPrioritizedTasks());
            sendText(httpExchange, response, 200);
        } else {
            sendIncorrect(httpExchange);
        }
    }
}
