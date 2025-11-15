package simulationcontroller;

import java.util.LinkedList;

public class Queue {

    // Attributes 
    private LinkedList<PCB> queue;   // FIFO queue container
    private String SchType;

    // Constructor
    public Queue(String SchType) {
        queue = new LinkedList<>();
        this.SchType = SchType;

    }

    // Basic Operations 
    // Add a process at the end (enqueue)
    public void enqueue(PCB p) {
        if (SchType.equalsIgnoreCase("HQ1")) {
            if (queue.isEmpty()) {
                queue.add(p);
                return;
            }
            int index = 0;
            for (PCB current : queue) {
                if (p.getMemoryReq() < current.getMemoryReq()) {
                    break;
                }
                index++;
            }
            queue.add(index, p);
        } else {
            // HQ2 / Ready / Submit / complete : FIFO
            queue.addLast(p);
        }
    }

    // Remove and return the first process
    public PCB dequeue() {
        if (!queue.isEmpty()) {
            return queue.removeFirst();
        }
        return null;
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public PCB peek() {
        return queue.peekFirst();
    }
    public int size() {
        return queue.size();
    }
    
    // Converts the queue contents into a List
    public java.util.List<PCB> toList() {
        return new java.util.ArrayList<>(queue);
    }

}
