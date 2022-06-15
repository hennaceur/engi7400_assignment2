package mun.concurrent.assignment.two;
import java.util.Scanner;
import mun.concurrent.assignment.two.ElevatorSimulator;

public class CSFElevatorSimulator {

	static ElevatorSimulator esimulator;
	public static void main(String[] args)
	{
		// Scanner reader = new Scanner(System.in);
		// System.out.println("Enter simulation time in minutes: ");
		// int SimulationTime = reader.nextInt();
		int SimulationTime = 1;
		ElevatorSimulator smallElevatorSimulator = new ElevatorSimulator(4, 1, SimulationTime);
//		ElevatorSimulator largeElevatorSimulator = new ElevatorSimulator(2, 2, SimulationTime);
		smallElevatorSimulator.run();
//		largeElevatorSimulator.run();
	
		System.out.print("######################\n");
		System.out.print("Simulation is complete!");
		// reader.close();
	}
}
:wq