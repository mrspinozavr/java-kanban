package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {

    private final List<SubTask> subTasks;
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description, TaskStatus.NEW, LocalDateTime.of(2024, 1, 1, 0, 0), 0);
        this.endTime = getEndTime();
        subTasks = new ArrayList<>();
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return this.getStartTime().plusMinutes(getDuration());
    }

    public TaskType getType() {
        return TaskType.EPIC;
    }

    public List<SubTask> getSubTasks() {
        return subTasks;
    }

    public void addSubTask(SubTask subTask) {
        subTasks.add(subTask);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(startTime, epic.startTime) && Objects.equals(endTime, epic.endTime);
    }

    @Override
    public String toString() {
        if (getStartTime() != null) {
            return getId() + "," + TaskType.EPIC + "," + getName() + "," + getStatus() + "," + getDescription() + "," + getStartTime().format(Task.FORMATTER) + "," + getEndTime().format(Task.FORMATTER) + "," + getDuration() + ",null\n";
        } else {
            return "startTime = null";
        }
    }

}
