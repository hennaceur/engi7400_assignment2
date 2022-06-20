package mun.concurrent.assignment.two;

import java.util.Arrays;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The ElevatorSimulator class is the main class that runs the simulation. It creates the clock, elevator array, and
 * elevator rider factory. It also keeps track of the number of riders that are accepted and rejected
 */
public class ElevatorSimulator implements Runnable {

    private static Clock SimulationClock;
    private static ElevatorArray elevators;        //handles scheduling/matching of elevators
    private static ElevatorRiderFactory[] riders;

    private final int numElevators;
    private final int elevatorCapacity;
    private final int simulationTime;

    //Allocate synchronization variables
    ReentrantLock elevatorClockLock = new ReentrantLock();
    Condition elevatorClockTicked = elevatorClockLock.newCondition();
    private final ElevatorStats elevatorStats;    //keeps track of times required for riders to finish

    // Constructor
    public ElevatorSimulator(int numElev, int elevCapacity, int simulationTime) {
        this.numElevators = numElev;
        this.elevatorCapacity = elevCapacity;
        this.simulationTime = simulationTime;
        elevators = new ElevatorArray(numElevators, elevatorCapacity);
        elevatorStats = new ElevatorStats();
        riders = new ElevatorRiderFactory[500];
        for (int i = 0; i < 500; i++) {
            riders[i] = new ElevatorRiderFactory();
        }
    }

    /**
     * The simulation runs for a set amount of time, and every tick, the clock is incremented and the elevators are
     * signaled to move. Every 20-120 ticks, a rider is added to the elevator system
     */
    public void run() {
        SimulationClock = new Clock();
        elevators.start();

        int inc = 20;
        int whichRider = 0;
        // while in simulation
        while (SimulationClock.getTick() < simulationTime) {
            try {
                Thread.sleep(10); // per tick (~1 second)
                elevatorClockLock.lockInterruptibly(); // Use lockInterruptibly so that thread doesn't get stuck waiting for lock
                SimulationClock.tick();
                elevatorClockTicked.signalAll();
                if (SimulationClock.getTick() % 121 == inc) {
                    boolean elevatorAcquired = riders[whichRider].run(elevators);
                    // Check weather rejected and increment accordingly
                    if (elevatorAcquired) {
                        elevatorStats.IncrementAcceptedRiders();
                    } else {
                        elevatorStats.IncrementRejectedRiders();
                    }
                    whichRider++;

                    if (inc == 120) {
                        inc = 20;
                    } else {
                        inc += 20;
                    }
                }

            } catch (InterruptedException ignored) {
                ignored.printStackTrace();
            } finally {
                elevatorClockLock.unlock();
            }
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // Output elevator stats
        elevatorStats.getStats(elevators);
        SimulationClock.reset();
        elevators.releaseAll();
    }
}
