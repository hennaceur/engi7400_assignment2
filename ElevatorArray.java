package mun.concurrent.assignment.two;
import mun.concurrent.assignment.two.ElevatorSimulator;

import java.util.concurrent.Semaphore;
import java.lang.Math;
import java.util.concurrent.TimeUnit;

import mun.concurrent.assignment.two.state;

public class ElevatorArray
{
	static state states[];
	static int currFloor[];
	static int numElevator;
	static Semaphore[] ElevatorLocks;

	//Initialize a mutex for each elevator with max capacity of people
	public ElevatorArray(int NumberElevator, int maxCap){
		numElevator = NumberElevator;
		states = new state[numElevator];
		ElevatorLocks = new Semaphore[numElevator];
		currFloor = new int[numElevator];
		
		for (int i = 0; i < numElevator; i++) {
			ElevatorLocks[i] = new Semaphore(maxCap,false);
			states[i] = state.STATIONARY;
			currFloor[i] = 0;
		}
	}

	//try to acquire an available elevator
	public Semaphore acquireElevator(state direction){
		for (int i = 0; i < numElevator; i++) {
			try {
				boolean hasPermits = ElevatorLocks[i].tryAcquire(100, TimeUnit.MILLISECONDS);
				if (hasPermits) {
					if (direction == states[i] || states[i] == state.STATIONARY) {
						states[i] = direction;
						return ElevatorLocks[i];
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}




