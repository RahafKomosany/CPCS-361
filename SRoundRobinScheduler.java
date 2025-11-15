package simulationcontroller;

public class SRoundRobinScheduler extends Scheduler {

    private static final int TEAM_NUMBER = 7;
    private static final int TIME_QUANTUM = 10 + TEAM_NUMBER; // = 17

    // Select the next process in FIFO order
    @Override
    public PCB selectNextProcess(Queue readyQ, long currentTime) {
        if (readyQ.isEmpty()) return null;
        return readyQ.dequeue(); // FIFO selection
    }

    // Return fixed quantum
    @Override
    public int computeTimeQuantum(Queue readyQ, PCB runningProcess) {
        return TIME_QUANTUM;
    }
}


