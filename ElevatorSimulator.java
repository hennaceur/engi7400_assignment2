package mun.concurrent.assignment.two;

import mun.concurrent.assignment.two.ElevatorStats;
import mun.concurrent.assignment.two.ElevatorRiderFactory;
import mun.concurrent.assignment.two.ElevatorArray;
import mun.concurrent.assignment.two.ElevatorRiderFactory;
import mun.concurrent.assignment.two.Clock;
import mun.concurrent.assignment.two.state;

import java.util.concurrent.locks.*;
import java.util.concurrent.Semaphore;

public class ElevatorSimulator implements Runnable {

    private static Clock SimulationClock;
    private static ElevatorArray elevators;        //handles scheduling/matching of elevators

    private final int numElevators;
    private final int elevatorCapacity;

    private final int simulationTime;

    private ElevatorStats elevatorStats;    //keeps track of times required for riders to finish

    private ElevatorRiderFactory elevatorRiderFactory;    //generates elevator riders at the appropriate time

    //Allocate synchronization variables
    ReentrantLock elevatorClockLock = new ReentrantLock();

    ReentrantLock elevatorLock = new ReentrantLock();

    Condition elevatorClockTicked = elevatorClockLock.newCondition();

    // Constructor
    public ElevatorSimulator(int numElev, int elevCapacity, int simulationTime) {
        this.numElevators = numElev;
        this.elevatorCapacity = elevCapacity;
        this.simulationTime = simulationTime;
        elevators = new ElevatorArray(numElevators, elevatorCapacity);
        elevatorStats = new ElevatorStats();
    }

    public void run() {
        SimulationClock = new Clock();

        // while in simulation
        while (SimulationClock.getTick() < simulationTime) {
            try {
                Thread.sleep(50); // slow down program
                elevatorClockLock.lockInterruptibly(); // Use lockInterruptibly so that thread doesn't get stuck waiting for lock
                SimulationClock.tick();
                elevatorClockTicked.signalAll();

                ElevatorRiderFactory[] riders = new ElevatorRiderFactory[15];
                for (int i = 0; i < 15; i++) {
                    riders[i] = new ElevatorRiderFactory();
                    riders[i].start();
                    var elevatorLockAcquired = riders[i].run(elevators);
                    if (elevatorLockAcquired) {
                        elevatorStats.IncrementAcceptedRiders();
                    } else {
                        elevatorStats.IncrementRejectedRiders();
                    }
                }
            } catch (InterruptedException ignored) {
            } finally {
                elevatorClockLock.unlock();
                elevators.releaseAll();
            }
        }
        // Output elevator stats
        elevatorStats.printStats();
        SimulationClock.reset();
    }
}
