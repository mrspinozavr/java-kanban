package service;

import model.Task;
import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{

    private final List<Task> historyStorage;

    public InMemoryHistoryManager() {
        this.historyStorage = new ArrayList<>();
    }

    @Override
    public void add(Task task) {    //Добавляем задачу в список истории
        historyStorage.add(task);
    }

    @Override
    public List<Task> getHistory() {   //Возвращаем 10 последних значений.
        int size = historyStorage.size();
        if (size < 10) {
            return historyStorage;
        }
        return historyStorage.subList(size - 10, size);
    }
}
