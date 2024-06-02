package http.handlers;

import com.sun.net.httpserver.HttpExchange;
import service.TaskManager;

import java.io.IOException;

public class HistoryHandler extends BaseHttpHandler {

    public HistoryHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();

        if (method.equals("GET")) {
            response = gson.toJson(taskManager.getHistory());
            sendText(httpExchange, response, 200);
        } else {
            sendIncorrect(httpExchange);
        }
    }
}