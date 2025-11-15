
package simulationcontroller;

public class OtherKerServices {

    private long memorySize; // available memory in the system
    private int noDevs;      // available devices in the system

    // Constructor to initialize system resources
    public OtherKerServices(long memorySize, int noDevs) {
        this.memorySize = memorySize;
        this.noDevs = noDevs;
    }

    // Try to allocate memory for a process 
    public boolean allocateMemory(long amount) {
        if (amount <= memorySize) {
            memorySize -= amount;
            return true; // allocation successful
        }
        return false; // not enough memory
    }

    // Release memory back to the system 
    public void deallocateMemory(long amount) {
        memorySize += amount;
    }

    // Try to reserve devices for a process 
    public boolean reserveDevices(int count) {
        if (count <= noDevs) {
            noDevs -= count;
            return true; // reservation successful
        }
        return false; // not enough devices
    }

    // Release devices back to the system 
    public void releaseDevices(int count) {
        noDevs += count;
    }

    // Get current available memory 
    public long getAvailableMemory() {
        return memorySize;
    }

    // Get current available devices
    public int getAvailableDevices() {
        return noDevs;
    }
}