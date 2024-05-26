package model;

import java.time.LocalDateTime;
import java.util.Objects;

public class SubTask extends Task {

    private Integer epicId;

    public SubTask(String name, String description, Integer epicId, LocalDateTime startTime, int duration) {
        super(name, description, TaskStatus.NEW, startTime, duration);
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SubTask subtask = (SubTask) o;
        return Objects.equals(epicId, subtask.epicId);
    }

    @Override
    public String toString() {
        return getId() + "," + TaskType.SUBTASK + "," + getName() + "," + getStatus() + "," + getDescription() + "," + getStartTime().format(Task.FORMATTER) + "," + getEndTime().format(Task.FORMATTER) + ","
                + getDuration() + "," + getEpicId() + "\n";
    }
}
