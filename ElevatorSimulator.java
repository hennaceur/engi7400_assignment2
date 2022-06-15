package mun.concurrent.assignment.two;
//import ElevatorStats.ElevatorStats;
import mun.concurrent.assignment.two.ElevatorRiderFactory;
import mun.concurrent.assignment.two.ElevatorArray;
import mun.concurrent.assignment.two.ElevatorRiderFactory;
import mun.concurrent.assignment.two.Clock;
import mun.concurrent.assignment.two.state;

import java.util.concurrent.locks.*;
import java.util.concurrent.Semaphore;


public class ElevatorSimulator implements Runnable {

	private static Clock SimulationClock;
	private static ElevatorArray elevators;		//handles scheduling/matching of elevators

	private final int numElevators;
	private final int elevatorCapacity;
	
	private final int simulationTime;
	
//	private ElevatorStats elevatorStats;	//keeps track of times required for riders to finish
	
	private ElevatorRiderFactory elevatorRiderFactory;	//generates elevator riders at the appropriate time
	
	// Allocate synchronization variables
	ReentrantLock elevatorClockLock = new ReentrantLock();

//	ReentrantLock elevatorLock = new ReentrantLock();

	Condition elevatorClockTicked = elevatorClockLock.newCondition();	
	
//	static Semaphore mutex = new Semaphore(1,false);
//	static Semaphore[] floors = new Semaphore[5];
//	static Semaphore[] Location = new Semaphore[5];
//
//	static int[] NeedElevator = new int[5];
//	static int[] Destination = new int[5];

	static int accepted = 0;
	
	// Constructor
	public ElevatorSimulator(int numElevators, int elevatorCapacity, int simulationTime)
	{
		this.numElevators = numElevators;
		this.elevatorCapacity = elevatorCapacity;
		this.simulationTime = simulationTime;
		elevators = new ElevatorArray(numElevators, elevatorCapacity);
	}
			
	public void run() {		

		//<INITIALIZATION HERE>

		SimulationClock = new Clock();
		// Simulate Small Elevators		
		while (SimulationClock.getTick() < simulationTime)
		{
			try
			{
				Thread.sleep(50);
				elevatorClockLock.lockInterruptibly(); // Use lockInterruptibly so that thread doesn't get stuck waiting for lock
				SimulationClock.tick();
				elevatorClockTicked.signalAll();

				ElevatorRiderFactory[] riders = new ElevatorRiderFactory[20];
				for (int i = 0; i < 20; i++)
				 {
					 riders[i] = new ElevatorRiderFactory();
					 riders[i].start();
					 accepted =  riders[i].run(elevators) ? accepted + 1 : accepted;
				 }

				// ElevatorArray SCAN = new ElevatorArray(elevatorCapacity);
				// SCAN.start();
			}
			catch (InterruptedException e)
			{
			}
			finally
			{
				elevatorClockLock.unlock();
			}
		}
		System.out.print("Accepted riders: " + accepted + "\n");
		// Output elevator stats

		//<PRINT OUT STATS GATHERED DURING SIMULATION>

//		SimulationClock.reset();
	}	
}
