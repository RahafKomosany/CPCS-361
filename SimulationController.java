
package simulationcontroller;
import java.io.*;
import java.util.*;

public class SimulationController {

    
    private long currentTime;
    private File inputFile;
    private File outputFile;

    private PrManager prManager;
    private OtherKerServices kernel;

    

    
    // Reads the first configuration line and initializes the system
    public void sysGen() throws FileNotFoundException  {
        try (Scanner sc = new Scanner(inputFile)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.startsWith("C")) {
                    String[] parts = line.split(" ");
                    currentTime = Long.parseLong(parts[1]);
                    int memory = Integer.parseInt(parts[2].substring(2));
                    int devices = Integer.parseInt(parts[3].substring(2));

                    kernel=new OtherKerServices(memory, devices);
                    prManager= new PrManager (kernel);

                   
                    break; // only first configuration handled here
                }
            }
         
        }
    }

    // Prints the final state of the system 
    public void displayFinalStatistics() {
        // later disscused in phase 2
        }

    // Reads and interprets a single line command (C, A, or D)
    public void parseCmd(String line) {
        if (line.isEmpty()) return;

        char type = line.charAt(0);

            switch (type) {
                case 'C': // Configuration
                    String[] conf = line.split(" ");
                    currentTime = Long.parseLong(conf[1]);
                    int mem = Integer.parseInt(conf[2].substring(2));
                    int dev = Integer.parseInt(conf[3].substring(2));
                    kernel= new OtherKerServices(mem, dev);
                    prManager= new PrManager(kernel);
                    break;

                case 'A': // Job arrival
                    String[] arr = line.split(" ");
                    currentTime = Long.parseLong(arr[1]);
                    int jobID = Integer.parseInt(arr[2].substring(2));
                    int m = Integer.parseInt(arr[3].substring(2));
                    int s = Integer.parseInt(arr[4].substring(2));
                    long r       = Long.parseLong(arr[5].substring(2));
                    int p = Integer.parseInt(arr[6].substring(2));

                    PCB pcb = new PCB(jobID, currentTime, r, p, m, s, 0);
                    prManager.procArrivingRoutine(pcb);
                    break;

                case 'D': // Display
                    long dispTime = Long.parseLong(line.split(" ")[1]);
                    currentTime = dispTime;
                    break;

                default:
                    System.out.println("Unknown command: " + type);  
        }
    }

    
    public static void main(String[] args) throws FileNotFoundException {
        SimulationController sim = new SimulationController();
        sim.inputFile = new File("input.txt");
        sim.outputFile = new File("output.txt");

        sim.sysGen(); // initialize system

        try (Scanner sc = new Scanner(sim.inputFile)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                sim.parseCmd(line);
            }
            sim.displayFinalStatistics();
        } catch (IOException e) {
            System.out.println("Error reading input file: " + e.getMessage());
        }
    }
}

