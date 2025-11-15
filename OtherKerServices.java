package simulationcontroller;

public class OtherKerServices {

    private long memorySize;     // available memory
    private int noDevs;          // available devices

    private long totalMemory;    // total system memory
    private int totalDevices;    // total system devices

    // Constructor to initialize system resources
    public OtherKerServices(long memorySize, int noDevs) {
        this.totalMemory = memorySize;
        this.totalDevices = noDevs;

        this.memorySize = memorySize; // available = total initially
        this.noDevs = noDevs;
    }

    // Try to allocate memory for a process 
    public boolean allocateMemory(long amount) {
        if (amount <= memorySize) {
            memorySize -= amount;
            return true;
        }
        return false;
    }

    // Release memory back
    public void deallocateMemory(long amount) {
        memorySize += amount;
    }

    // Try to reserve devices
    public boolean reserveDevices(int count) {
        if (count <= noDevs) {
            noDevs -= count;
            return true;
        }
        return false;
    }

    // Release devices
    public void releaseDevices(int count) {
        noDevs += count;
    }

    // Get available memory
    public long getAvailableMemory() {
        return memorySize;
    }

    // Get available devices
    public int getAvailableDevices() {
        return noDevs;
    }

    // Get TOTAL system memory
    public long getTotalMemory() {
        return totalMemory;
    }

    // Get TOTAL system devices
    public int getTotalDevices() {
        return totalDevices;
    }
}
