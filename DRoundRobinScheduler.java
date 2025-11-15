package simulationcontroller;

public class DRoundRobinScheduler extends Scheduler {

    private int timeQunt;
    private int firstQuantum;
    private int SR;
    private int AR;

    public DRoundRobinScheduler() {
        this.timeQunt = 0;
        this.SR = 0;
        this.AR = 0;
        this.firstQuantum = 0;
    }

    @Override
    public PCB selectNextProcess(Queue readyQ, long currentTime) {
        if (readyQ.isEmpty()) {
            return null;
        }
        return readyQ.dequeue(); // FIFO
    }

    public void setSRAR(int SR, int AR) {
        this.SR = SR;
        this.AR = AR;
    }

    public void setFirstQuantum(int burstTime) {
        this.firstQuantum = burstTime;
    }

    public int getTimeQuantum() {
        return timeQunt;
    }

    @Override
    public int computeTimeQuantum(Queue readyQ, PCB ignored) {
        if (readyQ.isEmpty()) {
            timeQunt = firstQuantum;
        } else {
            timeQunt = Math.max(1, AR);
        }
        return timeQunt;
    }

}
