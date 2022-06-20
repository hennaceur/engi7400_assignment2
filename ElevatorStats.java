package mun.concurrent.assignment.two;

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

    public void IncrementAcceptedRiders() {
        AcceptedRiders++;
    }

    public void IncrementRejectedRiders() {
        RejectedRiders++;
    }

    public int ReturnTotalRequestedRiders() {
        return AcceptedRiders + RejectedRiders;
    }

    public int ReturnAverageRequestTime() {
        if (ReturnTotalRequestedRiders() > 0) {
            return TotalRiderTravelTime / ReturnTotalRequestedRiders();
        }
        return 0;
    }

    public void getStats(ElevatorArray elevatorArray) {
        try { TotalRiderTravelTime = elevatorArray.getTravelTime(); } catch (Exception ignored) { }
        System.out.print("Total riders: " + ReturnTotalRequestedRiders() + "\t\t\t\t\t |" + "\n");
        System.out.print("Accepted riders: " + AcceptedRiders + "\t\t\t\t\t |" + "\n");
        System.out.print("Rejected riders: " + RejectedRiders + "\t\t\t\t\t |" + "\n");
        System.out.print("Average elevator request time: " + ReturnAverageRequestTime() + " s |" + "\n");
    }
}