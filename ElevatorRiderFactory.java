package mun.concurrent.assignment.two;

import java.util.Random;

/**
 * The ElevatorRiderFactory class generates a random request for an elevator and tries to acquire an elevator
 */
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
        request = new Request(currFloor + 1, destFloor + 1);
    }

    /**
     * If the elevator is available, then the elevator will be acquired and the request will be processed
     *
     * @param elevators The ElevatorArray object that contains all the elevators.
     * @return A boolean value.
     */
    public boolean run(ElevatorArray elevators) {
        try {
            boolean gotElevator = elevators.acquireElevator(this);
            if (gotElevator) {
                System.out.print("Going " + this.request.direction + " from Floor " + this.request.startFloor + " to Floor " + this.request.endFloor + "\n");
            } else {
                System.out.print("Rejected: " + this.request.direction + " from Floor " + this.request.startFloor + " to Floor " + this.request.endFloor + "\n");
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}


