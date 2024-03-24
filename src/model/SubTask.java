package model;

public class SubTask extends Task {

    private Integer epicId;

    public SubTask(String name, String description, Integer epicId) {
        super(name, description, TaskStatus.NEW);
        this.epicId = epicId;
    }

    public void setEpicId(Integer epicId) {
        this.epicId = epicId;
    }

    public Integer getEpicId() {
        return epicId;
    }

    public TaskType getType() {
        return TaskType.SUBTASK;
    }

    @Override
    public String toString() {
        return getId() + "," + TaskType.TASK + "," + getName() + "," + getStatus() + "," + getDescription() + "," + getEpicId() + "\n";
    }
}
