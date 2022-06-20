package mun.concurrent.assignment.two;

public class CSFElevatorSimulator {

    /**
     * The main function creates two elevator simulators, one with 4 elevators and 1 floor, and one with 2 elevators and 2
     * floors. It then runs the simulation for 7200 time units (2 simulated hours)
     */
    public static void main(String[] args) {
        int SimulationTime = 500;

        System.out.print("######################################\n");
        System.out.print("         Simulating Design A!        |\n");
        System.out.print("--------------------------------------\n");
        ElevatorSimulator smallElevatorSimulator = new ElevatorSimulator(1, 1, SimulationTime);
        smallElevatorSimulator.run();

//        System.out.print("######################################\n");
//        System.out.print("         Simulating Design B!        |\n");
//        System.out.print("--------------------------------------\n");
//        ElevatorSimulator largeElevatorSimulator = new ElevatorSimulator(2, 2, SimulationTime);
//        largeElevatorSimulator.run();

        System.out.print("######################################\n");
        System.out.print("Simulation is complete!");
    }
}