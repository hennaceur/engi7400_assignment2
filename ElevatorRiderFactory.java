package mun.concurrent.assignment.two;

import java.util.Random;
import java.util.concurrent.Semaphore;

import mun.concurrent.assignment.two.state;

public class ElevatorRiderFactory extends Thread{

	public int current;
	public int floor;
	public state DirectionRequested;

	private Semaphore elevatorPass;

	//Generates rider randomlly to call from the 5 floors.
	public ElevatorRiderFactory ()
	{
		Random x = new Random();
		int currentFloor = x.nextInt(5);
		int destinationFloor = x.nextInt(5);

		while(currentFloor == destinationFloor)
		{
			destinationFloor = x.nextInt(5);
		}

		this.floor = destinationFloor;
		this.current = currentFloor;
		this.DirectionRequested = current > floor ? state.DOWN : state.UP;
	}
	
	//Logic when the rider presses the button.
	//The rider tries to acquire a lock for the elevator if available
	//otherwise, the rider is rejected 
	public boolean run(ElevatorArray elevators) {
		try{
			Semaphore elevator = elevators.acquireElevator(this.DirectionRequested);
			if(elevator != null){
				System.out.print("Going "+ this.DirectionRequested + " from Floor " + (this.current + 1)+ " to Floor " +  (this.floor + 1) + "\n");
			} else {
				System.out.print("Rejected: " + this.DirectionRequested + " from Floor " + (this.current + 1)+ " to Floor " +  (this.floor + 1) + "\n");
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	// 	while(true)
	// 	{
	// 	try {
	// 		Master.mutex.acquire();
	// 	} catch (InterruptedException e) {
	// 		// TODO Auto-generated catch block
	// 		e.printStackTrace();
	// 	}
		
	// 	Master.NeedElevator[current] += 1;
	// 	System.out.println("Person " + id + " calls elevator on floor " + current);
	// 	Master.mutex.release();
		
		
	// 	try {
	// 		Master.floors[current].acquire();
	// 	} catch (InterruptedException e) {
	// 		// TODO Auto-generated catch block
	// 		e.printStackTrace();
	// 	}
	// 	/*
	// 	try {
	// 		Cleaning.Cleaner.acquire();
	// 	} catch (InterruptedException e1) {
	// 		// TODO Auto-generated catch block
	// 		e1.printStackTrace();
	// 	}
	// 	Cleaning.Cleaner.release(); */
		
	// 	System.out.println("Person " + id + " is now in elevator. Current floor " + current);
	// 	try {
	// 		Master.mutex.acquire();
	// 	} catch (InterruptedException e) {
	// 		// TODO Auto-generated catch block
	// 		e.printStackTrace();
	// 	}
	// 	Master.Destination[floor] += 1;
	// 	System.out.println("Person " + id + " wants to go to floor " + floor);
	// 	Master.mutex.release();
		
		
	// 	try {
	// 		Master.Location[floor].acquire();
	// 	} catch (InterruptedException e) {
	// 		// TODO Auto-generated catch block
	// 		e.printStackTrace();
	// 	}
		
	// 	System.out.println("Person " + id + " exits floor " + floor);
		
	// }
	}
}


