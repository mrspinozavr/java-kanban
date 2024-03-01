package service;

import model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager{

    private final CustomLinkedList historyStorage;

    public InMemoryHistoryManager() {
        this.historyStorage = new CustomLinkedList();
    }

    @Override
    public void add(Task task) {
        historyStorage.linkLast(task);
    }

    @Override
    public void remove(int id) {
        historyStorage.removeNode(historyStorage.getNode(id));
    }

    @Override
    public List<Task> getHistory() {
        return historyStorage.getTasks();
    }

    private static class CustomLinkedList {
        private final Map<Integer, Node> nodes = new HashMap<>();
        private Node head;
        private Node tail;

        private void linkLast(Task task) {
            if (task != null) {
                Node element = new Node(tail, task, null);
                if (nodes.containsKey(task.getId())) {
                    removeNode(nodes.get(task.getId()));
                }
                if (head == null) {
                    head = element;
                    tail = element;
                } else {
                    tail.setNext(element);
                    element.setPrevious(tail);
                    tail = element;
                }
                nodes.put(task.getId(), element);
            }
        }

        private List<Task> getTasks() {
            List<Task> taskList = new ArrayList<>();
            Node element = head;
            while (element != null) {
                taskList.add(element.getTask());
                element = element.getNext();
            }
            return taskList;
        }

        private void removeNode(Node node) {
            if (node != null) {
                nodes.remove(node.getTask().getId());
                Node prev = node.getPrevious();
                Node next = node.getNext();

                if (head == node) {
                    head = node.getNext();
                }
                if (tail == node) {
                    tail = node.getPrevious();
                }
                if (prev != null) {
                    prev.setNext(next);
                }
                if (next != null) {
                    next.setPrevious(prev);
                }
            }
        }

        private Node getNode(int id) {
            return nodes.get(id);
        }

    }
}

