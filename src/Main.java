import service.InMemoryTaskManager;
import service.Managers;
import service.TaskManager;
import model.Epic;
import model.SubTask;
import model.Task;

public class Main {

    public static void main(String[] args) {
        InMemoryTaskManager taskManager = new InMemoryTaskManager(Managers.getDefaultHistory());
        Task task = taskManager.createTask(new Task("Новая задача", "Пока не знаю для чего"));
        System.out.println("Созданная задача: " + task);

        Task taskFromManager = taskManager.getTask(task.getId());
        System.out.println("Полученная задача из менеджера: " + taskFromManager);

        taskFromManager.setName("Закрыть 4й спринт");
        taskFromManager.setDescription("Закрыть финальное задание");
        taskManager.updateTask(taskFromManager);
        System.out.println("Обновленная задача: " + taskFromManager);

        taskManager.deleteTask(taskFromManager.getId());
        System.out.println("Удаленная задача: " + task);
        System.out.println();

        Epic epic1 = taskManager.createEpic(new Epic("Планы на работу в 2024", "Поменять работу"));
        Epic epic2 = taskManager.createEpic(new Epic("Планы на здоровье 2024", "Заняться здоровьем"));
        System.out.println("Придумал себе первый Эпик: " + epic1);
        System.out.println("Придумал себе ворой Эпик: " + epic2);
        SubTask subTask1 = new SubTask("Курсы java","Записаться на курс Java - разработчик", epic1);
        SubTask subTask2 = new SubTask("Пройти пять спринтов","Не сорваться и закончить начатое", epic1);
        SubTask subTask3 = new SubTask("Пройти чекап","Сдать анализы", epic2);

        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);
        taskManager.createSubTask(subTask3);

        System.out.println("Список всех эпиков " + taskManager.getAllEpics());
        System.out.println("Список всех подзадач " + taskManager.getAllSubTasks().size() + taskManager.getAllSubTasks());
        System.out.println("Список подзадач второго эпика " + taskManager.getEpicSubTasks(epic2));
        System.out.println();
        System.out.println("Ищем конкретную подзадачу: " + taskManager.getSubTask(5));

        SubTask newSubTask2 = new SubTask("Пройти шесть спринтов","Не сорваться и закончить начатое", epic1);
        newSubTask2.setId(5);
        taskManager.updateSubTask(newSubTask2);
        System.out.println("Список подзадач первого эпика " + epic1.getSubTasks().size() + taskManager.getEpicSubTasks(epic1));
        System.out.println("Список всех подзадач " + taskManager.getAllSubTasks().size() + taskManager.getAllSubTasks());

        taskManager.deleteEpic(epic2.getId());
        System.out.println("Список всех эпиков " + taskManager.getAllEpics().size() + taskManager.getAllEpics());
        taskManager.deleteSubTask(newSubTask2.getId());
        System.out.println("Список всех подзадач " + taskManager.getAllSubTasks().size() + taskManager.getAllSubTasks());
    }
}
