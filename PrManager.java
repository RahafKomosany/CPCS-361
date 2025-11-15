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
        // Admission test: memory and devices
        if (proc.getMemoryReq() <= oks.getAvailableMemory()
                && proc.getDevReq() <= oks.getAvailableDevices()) {
            // Allocate and enqueue into Ready
            oks.allocateMemory(proc.getMemoryReq());
            oks.reserveDevices(proc.getDevReq());
            readyQ.enqueue(proc);

        } else {
            // Not enough resources, assign to a hold queue
            if (proc.getPriority() == 1) {
                holdQ1.enqueue(proc);
            } else //priority = 2
            {
                holdQ2.enqueue(proc);
            }
        }
    }

    private void updateSRAR() {
        int sum = 0, count = 0;
        for (PCB p : readyQ.toList()) {
            sum += p.getRemainingBurst();
            count++;
        }
        SR = sum;
        AR = (count == 0) ? 0 : (sum / count);

        if (scheduler instanceof DRoundRobinScheduler) {
            DRoundRobinScheduler drr = (DRoundRobinScheduler) scheduler;
            drr.setSRAR(SR, AR);
        }

    }

    //Dispatch a job (CPU scheduling step) 
    private void dispatch() {
        if (readyQ.isEmpty()) {
            return;
        }

        PCB job = (PCB) readyQ.dequeue();
        job.setBurstTime(job.getBurstTime() - 1);

        if (job.getBurstTime() <= 0) {
            // Job finished
            oks.deallocateMemory(job.getMemoryReq());
            oks.releaseDevices(job.getDevReq());
            CompleteQ.enqueue(job);
        } else {
            // Still needs CPU time 
            readyQ.enqueue(job);
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

    // Currently running process
    public long getRunningProcId() {
        return readyQ.isEmpty() ? -1 : ((PCB) readyQ.peek()).getPID();
    }

}
