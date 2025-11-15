package simulationcontroller;

public abstract class Scheduler {
    
    public abstract PCB selectNextProcess(Queue readyQ, long curTime);
    
    public abstract int computeTimeQuantum(Queue readyQ, PCB running);
    
}

