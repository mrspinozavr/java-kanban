package service;

import model.Task;

public class Node {

    private Node previous;
    private Task task;
    private Node next;

    public Node(Node previous, Task task, Node next) {
        this.previous = previous;
        this.task = task;
        this.next = next;
    }

    public void setPrevious(Node previous) {
        this.previous = previous;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public Node getPrevious() {
        return previous;
    }

    public Task getTask() {
        return task;
    }

    public Node getNext() {
        return next;
    }
}
