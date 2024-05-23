package service;

import org.junit.jupiter.api.BeforeEach;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    void initManager() {
        taskManager = new InMemoryTaskManager(Managers.getDefaultHistoryManager());
    }
}