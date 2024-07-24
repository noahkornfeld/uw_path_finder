// --== CS400 Fall 2023 File Header Information ==--
// Name: Suraj Naveen
// Email: snaveen@wisc.edu
// Group: E 19
// TA: Lakshika Rathi
// Lecturer: Gary Dahl
// Notes to Grader: <optional extra notes>

import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * this is the main frontend class important for handling the frontend for the UW Path finder app. it implements the
 * FrontendInterface which contains the necessary methods for developing the frontend.
 */
public class Frontend implements FrontendInterface{

    private final Backend backend; // The instance of the backend's class
    private final Scanner userInput; // Where user input comes from

    private  boolean continueLoop = true ;
//    private boolean exit ; // field for handling exit functionality
    /**
     * constructor for Frontend
     * @param backend backend instance
     * @param userInput scanner input
     */
    public Frontend(Backend backend, Scanner userInput) {
        this.backend = backend;
        this.userInput = userInput;
    }

   
    /**
     * This method starts a new GUI instance of the app and handles user input. It will execute
     * commands based on user-input and will call other functions when necessary.
     */
    @Override
    public void run() {

        System.out.println("Welcome to  path finder");
        runMainMenu();
    }
    /**
     * Displays a list of commands that the user can choose from
     */
    @Override
    public void runMainMenu() {
        int userChoice = 0;
        while (continueLoop) { //using while loop to make a looping menu
            System.out.println("\nUW PATH FINDER");
            System.out.println("1. Select a data file to load into Path Finder: ");
            System.out.println("2. Show statistics about the dataset: ");
            System.out.println("3. Lists all information between start and destination buildings: ");
            System.out.println("4. Exit the app");
            try {
                userChoice = userInput.nextInt();
                if (userChoice < 1 || userChoice > 4) {
                    System.out.println("Invalid choice, please enter a valid option !");
                }
            } catch (java.util.InputMismatchException e) { //making sure of an integer input
                System.out.println("Invalid input type. Please enter an integer.");
                userInput.next(); // Consume invalid input
            }
            //calling submenu methods based on choice
            if (userChoice == 1) {
                loadData("");
            } else if (userChoice == 2) {
                displayStats();
            } else if (userChoice == 3) {
                userInput.nextLine();
                findRoute("", "");
            }
            else if(userChoice == 4) {
                exit();

            }
        }
    }

    /**
     * Loads the data from the file that the user enters
     */
    @Override
    public void loadData(String filename) {
        System.out.println("Please enter the file path( should be dot format):");

        filename = userInput.next();
        if (!(filename.endsWith(".dot"))) {
            System.out.println("Please try again with a .dot formatted file");
            filename = userInput.next();
        } else {
            try {
                backend.readData(filename);
                System.out.println("File read successfully.");
            } catch (FileNotFoundException e) {
                System.out.println("File reading failed");
            }
        }
    }
    /**
     * This method displays statistics of the dataset in the application window. The specific
     * stats shown are the total number of buildings (nodes), total walking time (weight) of the
     * graph.
     */
    @Override
    public void displayStats() {
        System.out.println("here, we display statistics: ");
        String statistics = backend.getStatistics();
        System.out.println(statistics);

    }
    /**
     * Finds the route from the start point to the end point that the user specifies. This will call
     * a backend method that will find the shortest path (using Dijkstra's algorithm) to take.
     */
    @Override
    public void findRoute(String startNode, String endNode) {
        System.out.println("here we print the shortest Path, total walking time and total path cost.");
        System.out.println("Please enter the start building name:");
        startNode = userInput.nextLine();
        while(!backend.graph.containsNode(startNode)){
            System.out.println("node not in graph !! enter again ");
            startNode = userInput.nextLine();
        }
        System.out.println("Please enter the end building name:");
        endNode = userInput.nextLine();
        while(!backend.graph.containsNode(endNode)){
            System.out.println("node not in graph !! enter again ");
            endNode = userInput.nextLine();
        }
        ShortestPathInterface backendShortestPath = backend.getShortestPath(startNode, endNode);

        if (backendShortestPath == null) {
            System.out.println("no path found! ");
        }

        System.out.println("The shortest path: "+backendShortestPath.getPath());
        System.out.println("Total Walking time: "+backendShortestPath.getWalkingTimes());
        System.out.println("Total Path cost: "+backendShortestPath.totalPathCost());
      }


    /**
     * This method closes the application
     */
    @Override
    public void exit() {
        continueLoop = false;
        System.out.println("thanks for using the UW Path Finder ! ");
    }
}
