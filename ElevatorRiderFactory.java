package mun.concurrent.assignment.two;

import java.util.Random;

public class ElevatorRiderFactory extends Thread {

    public int currentFloor;
    public int destinationFloor;
    public state directionRequested;

    //Generates rider randomly to call from the 5 floors.
    public ElevatorRiderFactory() {
        Random x = new Random();
        int currFloor = x.nextInt(5);
        int destFloor = x.nextInt(5);

        while (destFloor == currFloor) {
            destFloor = x.nextInt(5);
        }

        this.destinationFloor = destFloor;
        this.currentFloor = currFloor;
        this.directionRequested = currentFloor > this.destinationFloor ? state.DOWN : state.UP;
    }

    //Logic when the rider presses the button.
    //The rider tries to acquire a lock for the elevator if available
    //otherwise, the rider is rejected
    public boolean run(ElevatorArray elevators) {
        try {
            boolean gotElevator = elevators.acquireElevator(this);
            if (gotElevator) {
                System.out.print("Going " + this.directionRequested + " from Floor " + (this.currentFloor + 1) + " to Floor " + (this.destinationFloor + 1) + "\n");
            } else {
                System.out.print("Rejected: " + this.directionRequested + " from Floor " + (this.currentFloor + 1) + " to Floor " + (this.destinationFloor + 1) + "\n");
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}


