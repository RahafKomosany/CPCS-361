
package simulationcontroller;

// Represents the Process Control Block (PCB)
public class PCB {

    // Attributes
    private long PID;           // Unique process ID
    private long arrivalTime;   // Time when the process arrives in the system
    private long burstTime;     // CPU execution time required by the process
    private int priority;       // Process priority
    private long memoryReq;     // Amount of memory required
    private int devReq;         // Number or type of device resources required
    private int state;          // Process state (e.g., 0 = Ready, 1 = Running, 2 = Terminated)
    private long remainingBurst; // Remaining CPU time

    
    //constructor
    public PCB() {
     
    }

    //Parameterized constructor
    public PCB(long PID, long arrivalTime, long burstTime, int priority,
                   long memoryReq, int devReq, int state) {
        this.PID = PID;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.memoryReq = memoryReq;
        this.devReq = devReq;
        this.state = state;
        this.remainingBurst = burstTime;
    }

    // Getters and Setters
    public long getPID() {
        return PID;
    }

    public void setPID(long PID) {
        this.PID = PID;
    }

    public long getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(long arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public long getBurstTime() {
        return burstTime;
    }

    public void setBurstTime(long burstTime) {
        this.burstTime = burstTime;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public long getMemoryReq() {
        return memoryReq;
    }

    public void setMemoryReq(long memoryReq) {
        this.memoryReq = memoryReq;
    }

    public int getDevReq() {
        return devReq;
    }

    public void setDevReq(int devReq) {
        this.devReq = devReq;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
   public long getRemainingBurst() {
    return remainingBurst;
}

public void decreaseBurst(long amount) {
    remainingBurst -= amount;
    if (remainingBurst < 0) remainingBurst = 0;
}

public boolean isFinished() {
    return remainingBurst <= 0;
}



   
}