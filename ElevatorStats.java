package mun.concurrent.assignment.two;

import java.util.Arrays;

/**
 * This class keeps track of the number of accepted riders, rejected riders, total requested riders, and the total rider
 * travel time
 */
public class ElevatorStats {

    public static int AcceptedRiders;
    public static int RejectedRiders;
    public static int TotalRequestedRiders;
    public static int TotalRiderTravelTime;

    public ElevatorStats() {
        AcceptedRiders = 0;
        RejectedRiders = 0;
        TotalRequestedRiders = 0;
        TotalRiderTravelTime = 0;
    }

    /**
     * Increment the number of accepted riders by one
     */
    public void IncrementAcceptedRiders() {
        AcceptedRiders++;
    }

    /**
     * IncrementRejectedRiders() increments the number of rejected riders by 1
     */
    public void IncrementRejectedRiders() {
        RejectedRiders++;
    }

    /**
     * This function returns the sum of the accepted and rejected riders
     *
     * @return The total number of riders that have requested a ride.
     */
    public int ReturnTotalRequestedRiders() {
        return AcceptedRiders + RejectedRiders;
    }

    /**
     * This function returns the average time it takes for a rider to request a ride and then be picked up by a driver
     *
     * @return The average time it takes for a rider to be picked up.
     */
    public int ReturnAverageRequestTime() {
        if (AcceptedRiders > 0) {
            return TotalRiderTravelTime / AcceptedRiders;
        }
        return 0;
    }

    /**
     * It gets the total rider travel time from the elevator array, then prints out the total riders, accepted riders,
     * rejected riders, and average elevator request time
     *
     * @param elevatorArray The elevator array that the simulation is using.
     */
    public void getStats(ElevatorArray elevatorArray) {
        try {
            TotalRiderTravelTime = elevatorArray.getTravelTime();
        } catch (Exception ignored) {
        }
        System.out.print("Total riders: " + ReturnTotalRequestedRiders() + "\t\t\t\t\t |" + "\n");
        System.out.print("Accepted riders: " + AcceptedRiders + "\t\t\t\t\t |" + "\n");
        for (String s : Arrays.asList("Rejected riders: " + RejectedRiders + "\t\t\t\t\t |" + "\n", "Average elevator request time: " + ReturnAverageRequestTime() + " s |" + "\n")) {
            System.out.print(s);
        }
    }
}