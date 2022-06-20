package mun.concurrent.assignment.two;

public class CSFElevatorSimulator {

    public static void main(String[] args) {
        int SimulationTime = 1000;

        System.out.print("######################################\n");
        System.out.print("         Simulating Design A!        |\n");
        System.out.print("--------------------------------------\n");
        ElevatorSimulator smallElevatorSimulator = new ElevatorSimulator(4, 1, SimulationTime);
        smallElevatorSimulator.run();

        System.out.print("######################################\n");
        System.out.print("         Simulating Design B!        |\n");
        System.out.print("--------------------------------------\n");
        ElevatorSimulator largeElevatorSimulator = new ElevatorSimulator(2, 2, SimulationTime);
        largeElevatorSimulator.run();

        System.out.print("######################################\n");
        System.out.print("Simulation is complete!");
    }
}