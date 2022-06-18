package mun.concurrent.assignment.two;

import java.util.Random;

public class ElevatorRiderFactory extends Thread {

    public Request request;

    //Generates rider randomly to call from the 5 floors.
    public ElevatorRiderFactory() {
        Random x = new Random();
        int currFloor = x.nextInt(5);
        int destFloor = x.nextInt(5);

        while (destFloor == currFloor) {
            destFloor = x.nextInt(5);
        }
        request = new Request(currFloor, destFloor);
    }

    //Logic when the rider presses the button.
    //The rider tries to acquire a lock for the elevator if available
    //otherwise, the rider is rejected
    public boolean run(ElevatorArray elevators) {
        try {
            boolean gotElevator = elevators.acquireElevator(this);
            if (gotElevator) {
                System.out.print("Going " + this.request.direction + " from Floor " + (this.request.startFloor + 1) + " to Floor " + (this.request.endFloor + 1) + "\n");
            } else {
                System.out.print("Rejected: " + this.request.direction + " from Floor " + (this.request.startFloor + 1) + " to Floor " + (this.request.endFloor + 1) + "\n");
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}


