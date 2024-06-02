package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import http.adapters.InstantAdapter;

import java.time.Instant;

public class Managers {
    public static HistoryManager getDefaultHistoryManager() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getInMemoryTaskManager(HistoryManager historyManager) {
        return new InMemoryTaskManager(historyManager);
    }

    public static Gson getGson() {
        return new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .registerTypeAdapter(Instant.class, new InstantAdapter())
                .create();
    }
}