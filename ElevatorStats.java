package mun.concurrent.assignment.two;

public class ElevatorStats {

    public static int AcceptedRiders;
    public static int RejectedRiders;
    public static int TotalRequestedRiders;
    public static int AverageRequestTime;
    public static int TotalSimulationTime;

    public ElevatorStats() {
        AcceptedRiders = 0;
        RejectedRiders = 0;
        TotalRequestedRiders = 0;
        AverageRequestTime = 0;
        TotalSimulationTime = 0;
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

    public void FloorTraversedTime() {
        TotalSimulationTime = +5;
    }

    public void ElevatorStopTime() {
        TotalSimulationTime = +15;
    }

    public int ReturnAverageRequestTime() {
        return TotalSimulationTime / AcceptedRiders;
    }

    public int ReturnTotalSimulationTime() {
        return TotalSimulationTime;
    }

    public void printStats() {
        System.out.print("Total riders: " + ReturnTotalRequestedRiders() + "\n");
        System.out.print("Accepted riders: " + AcceptedRiders + "\n");
        System.out.print("Rejected riders: " + RejectedRiders + "\n");
        System.out.print("Average elevator request time: " + AverageRequestTime + "\n");
    }
}