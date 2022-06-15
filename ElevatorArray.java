package mun.concurrent.assignment.two;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

public class ElevatorArray {
    static state[] states;
    static int[] currFloor;
    static int numElevator;
    static Semaphore[] ElevatorLocks;

    //Initialize a semaphore for each elevator with max capacity of people
    public ElevatorArray(int NumberElevator, int maxCap) {
        numElevator = NumberElevator;
        states = new state[numElevator];
        ElevatorLocks = new Semaphore[numElevator];
        currFloor = new int[numElevator];

        for (int i = 0; i < numElevator; i++) {
            ElevatorLocks[i] = new Semaphore(maxCap, false);
            states[i] = state.STATIONARY;
            currFloor[i] = 0;
        }
    }

    //try to acquire an available elevator
    public Semaphore acquireElevator(ElevatorRiderFactory rider) {
        for (int i = 0; i < numElevator; i++) {
            try {
                boolean hasPermits = ElevatorLocks[i].tryAcquire(100, TimeUnit.MILLISECONDS);
                if (hasPermits) {
                    if (rider.directionRequested == states[i] || states[i] == state.STATIONARY) {
						states[i] = rider.directionRequested;
						return ElevatorLocks[i];
                    } else {
                        ElevatorLocks[i].release(1);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void releaseRider(Semaphore elevatorLock) {
		elevatorLock.release(1);
    }

    public void releaseAll() {
		for (Semaphore elevatorLock : ElevatorLocks) {
			elevatorLock.release();
		}
    }
}




