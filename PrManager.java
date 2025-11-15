package simulationcontroller;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

public class PrManager {

    private long internalClock;
    private int SR = 0;                // Sum of remaining bursts in Ready
    private int AR = 0;// Average = SR / count
    private OtherKerServices oks;
    private Scheduler scheduler;

    private final Queue submitQ = new Queue("Submit");
    private final Queue holdQ1 = new Queue("HQ1");
    private final Queue holdQ2 = new Queue("HQ2");
    private final Queue readyQ = new Queue("Ready");
    private final Queue CompleteQ = new Queue("Complete");

    //Constructor
    public PrManager(OtherKerServices oks) {
        this.oks = oks;
        this.internalClock = 0;
    }

   //Admit a process into the system (arrival handler)
    public void procArrivingRoutine(PCB proc) {

    // 1) Reject if exceeds system limits (without canEverAdmit())
    if (proc.getMemoryReq() > oks.getTotalMemory() ||
        proc.getDevReq() > oks.getTotalDevices()) {

        proc.setState(5); // REJECTED
        System.out.println("Process " + proc.getPID() + " rejected (exceeds total limits)");
        return;
    }

    // 2) If resources available now → admit into Ready
    if (proc.getMemoryReq() <= oks.getAvailableMemory() &&
        proc.getDevReq() <= oks.getAvailableDevices()) {

        oks.allocateMemory(proc.getMemoryReq());
        oks.reserveDevices(proc.getDevReq());
        readyQ.enqueue(proc);
        proc.setState(1); // READY
        updateSRAR();
    }

    // 3) Otherwise, send to hold queue
    else {

        if (proc.getMemoryReq() > oks.getAvailableMemory()) {
            holdQ1.enqueue(proc); // sorted by memory
        } else {
            holdQ2.enqueue(proc); // FIFO
        }

        proc.setState(2); // HOLD
    }

    }

    //Dispatch a job (CPU scheduling step) 
    public void dispatch() {
        // If no process is running or current process finished, get next one
        if (runningProcess == null || runningProcess.getRemainingBurst() <= 0) {
            // If current process finished, handle completion
            if (runningProcess != null && runningProcess.getRemainingBurst() <= 0) {
                processCompletion(runningProcess);
            }
            
            // Select next process
            if (!readyQ.isEmpty()) {
                runningProcess = scheduler.selectNextProcess(readyQ, internalClock);
                
                if (runningProcess != null) {
                    runningProcess.setState(1); // RUNNING
                    
                    // For Dynamic RR, set first quantum if this is the first process
                    if (scheduler instanceof DRoundRobinScheduler) {
                        DRoundRobinScheduler drr = (DRoundRobinScheduler) scheduler;
                        if (drr.getTimeQuantum() == 0) {
                            drr.setFirstQuantum((int)runningProcess.getBurstTime());
                        }
                    }
                    
                    // Compute time quantum for this process
                    currentQuantum = scheduler.computeTimeQuantum(readyQ, runningProcess);
                    quantumUsed = 0;
                }
            } else {
                runningProcess = null;
            }
        }
    //Advance CPU clock
    public void cpuTimeAdvance(long duration) {
        internalClock += duration;
    }

    //Next decision time 
    public long getNextDecisionTime() {
        return 1; //fixed later in phase 2
    }

    private void tryAdmitFromHold() {
    boolean moved = true;

    // Repeat while at least one process is moved from hold to ready
    while (moved) {
        moved = false;

        // =====================================
        //      HQ1 (memory-based admission)
        // =====================================
        while (!holdQ1.isEmpty()) {

            PCB p = holdQ1.peek();

            // Check if resources are sufficient for this process
            boolean enoughMemory  = p.getMemoryReq() <= oks.getAvailableMemory();
            boolean enoughDevices = p.getDevReq()   <= oks.getAvailableDevices();

            if (enoughMemory && enoughDevices) {

                // Move process from HQ1 to Ready
                p = holdQ1.dequeue();
                oks.allocateMemory(p.getMemoryReq());
                oks.reserveDevices(p.getDevReq());

                readyQ.enqueue(p);
                p.setState(1); // READY

                updateSRAR();
                moved = true;

            } else {
                // Cannot admit the first process in HQ1 → stop checking HQ1
                break;
            }
        }

        // =====================================
        //      HQ2 (FIFO admission)
        // =====================================
        while (!holdQ2.isEmpty()) {

            PCB p = holdQ2.peek();

            // Check if resources are sufficient for this process
            boolean enoughMemory  = p.getMemoryReq() <= oks.getAvailableMemory();
            boolean enoughDevices = p.getDevReq()   <= oks.getAvailableDevices();

            if (enoughMemory && enoughDevices) {

                // Move process from HQ2 to Ready
                p = holdQ2.dequeue();
                oks.allocateMemory(p.getMemoryReq());
                oks.reserveDevices(p.getDevReq());

                readyQ.enqueue(p);
                p.setState(1); // READY

                updateSRAR();
                moved = true;

            } else {
                // Cannot admit the first process in HQ2 → stop checking HQ2
                break;
            }
        }
    }
}

    // Currently running process
    public long getRunningProcId() {
        return readyQ.isEmpty() ? -1 : ((PCB) readyQ.peek()).getPID();
    }

}


