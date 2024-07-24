import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * This class is a tester class designed to test the function of both the Backend Class and
 * ShortestPath class and how their functionality works together
 * 
 * @author noahkornfeld
 *
 */
public class BackendDeveloperTests {

  /**
   * Tester method to test that the readData method throws a FileNotFoundException when the given
   * string is a file that does not exist
   */
  @Test
  public void test1() {

    // create graph and backend objects
    DijkstraGraph<String, Double> graph = new DijkstraGraph<String, Double>(new PlaceholderMap<>());
    Backend backend = new Backend(graph);

    // try to read in a file that does not exist
    try {
      backend.readData("wrong_file.dot");
      // fails if readData() does not throw the exception
      Assertions.fail("Exception was not thrown");
    } catch (FileNotFoundException e) { // catch exception

      // makes sure message is what is desired
      Assertions.assertEquals(e.getMessage(), "File Does Not Exist");

    }
  }

  /**
   * This tester method ensures that readData() correctly reads in a file when it exists, the
   * backend class correctly handles the file
   */
  @Test
  public void test2() {

    // create graph and backend objects
    DijkstraGraph<String, Double> graph = new DijkstraGraph<String, Double>(new PlaceholderMap<>());
    Backend backend = new Backend(graph);
    // tries to read in a file that exists
    try {
      backend.readData("campus.dot");
      // arbitrary tester to ensure readData() correctly handles the data file
      String statistics =
          "Total Nodes: 160 Total Edges: 400 Total Walking Time: 55337.749999999985";
      Assertions.assertEquals(backend.getStatistics(), statistics);
    } catch (Exception e) { // fails if any exception is thrown
      Assertions.fail("Exception was incorrectly thrown");
    }
  }


  /**
   * Tests the function of both the backend class and the shortest path classs with an example path
   * from memorial union to bascom hall
   * 
   * @throws FileNotFoundException if file path is not found
   */
  @Test
  public void test3() throws FileNotFoundException {

    // create graph and backend objects
    DijkstraGraph<String, Double> graph = new DijkstraGraph<String, Double>(new PlaceholderMap<>());
    Backend backend = new Backend(graph);
    // read data file in
    backend.readData("campus.dot");

    // get path from MemU to Bascom Hall
    ShortestPathInterface path = backend.getShortestPath("Memorial Union", "Bascom Hall");

    // create list of nodes expected to be along path
    List<String> expectedPath = new ArrayList<String>();
    expectedPath.add("Memorial Union");
    expectedPath.add("Radio Hall");
    expectedPath.add("Education Building");
    expectedPath.add("North Hall");
    expectedPath.add("Bascom Hall");

    // tests that every node that is expected is in the actual path
    for (int i = 0; i < expectedPath.size(); i++) {
      Assertions.assertEquals(path.getPath().get(i), expectedPath.get(i));
    }

    // create list of expected edge costs along path
    List<Double> expectedPathCosts = new ArrayList<Double>();
    expectedPathCosts.add(176.70000000000002);
    expectedPathCosts.add(113.00000000000001);
    expectedPathCosts.add(99.19999999999999);
    expectedPathCosts.add(219.79999999999998);

    // expected total path cost
    Double expectedTotalCost = 0.0;


    // assertions to check correctness of the edges in the path
    for (int i = 0; i < path.getWalkingTimes().size(); i++) {
      // ensures expected and actual edge costs are the same
      Assertions.assertEquals(path.getWalkingTimes().get(i), expectedPathCosts.get(i));

      // ensures each edge corresponds to an edge in the graph between the 2 nodes
      Assertions.assertEquals(graph.getEdge(path.getPath().get(i), path.getPath().get(i + 1)),
          path.getWalkingTimes().get(i));
      expectedTotalCost += expectedPathCosts.get(i); // add expected costs to expected total cost
    }

    // tests that expected total cost is equal to actual total cost
    Assertions.assertEquals(expectedTotalCost, path.totalPathCost());

  }

  /**
   * another tester method to test the function of the backend class and the shortest path class,
   * this time coming in the other direction (Ag Hall to Grainger)
   * 
   * @throws FileNotFoundException if file path does not exist
   */
  @Test
  public void test4() throws FileNotFoundException {
    // create graph and backend objects
    DijkstraGraph<String, Double> graph = new DijkstraGraph<String, Double>(new PlaceholderMap<>());
    Backend backend = new Backend(graph);
    // read data file in
    backend.readData("campus.dot");

    // get path from Ag Hall to Grainger
    ShortestPathInterface path = backend.getShortestPath("Agricultural Hall", "Grainger Hall");

    // create list of nodes expected to be along path
    List<String> expectedPath = new ArrayList<String>();
    expectedPath.add("Agricultural Hall");
    expectedPath.add("Agricultural Engineering");
    expectedPath.add("DeLuca Biochemistry Building");
    expectedPath.add("Materials Science and Engineering");
    expectedPath.add("1410 Engineering Dr");
    expectedPath.add("Wisconsin Institute for Discovery");
    expectedPath.add("Meiklejohn House");
    expectedPath.add("Noland Hall");
    expectedPath.add("Teacher Education");
    expectedPath.add("206 Bernard Ct.");
    expectedPath.add("Merit Residence Hall");
    expectedPath.add("Ogg Residence Hall");
    expectedPath.add("Grainger Hall");

    // tests that every node that is expected is in the actual path
    for (int i = 0; i < expectedPath.size(); i++) {
      Assertions.assertEquals(path.getPath().get(i), expectedPath.get(i));
    }

    // create list of expected edge costs along path
    List<Double> expectedPathCosts = new ArrayList<Double>();
    expectedPathCosts.add(90.10000000000001);
    expectedPathCosts.add(103.90000000000002);
    expectedPathCosts.add(133.6);
    expectedPathCosts.add(203.2);
    expectedPathCosts.add(118.89999999999999);
    expectedPathCosts.add(210.59999999999997);
    expectedPathCosts.add(124.20000000000002);
    expectedPathCosts.add(173.2);
    expectedPathCosts.add(141.29999999999998);
    expectedPathCosts.add(171.6);
    expectedPathCosts.add(193.2);
    expectedPathCosts.add(308.6999999999999);

    // expected total path cost
    Double expectedTotalCost = 0.0;


    // assertions to check correctness of the edges in the path
    for (int i = 0; i < path.getWalkingTimes().size(); i++) {
      // ensures expected and actual edge costs are the same
      Assertions.assertEquals(path.getWalkingTimes().get(i), expectedPathCosts.get(i));

      // ensures each edge corresponds to an edge in the graph between the 2 nodes
      Assertions.assertEquals(graph.getEdge(path.getPath().get(i), path.getPath().get(i + 1)),
          path.getWalkingTimes().get(i));

      expectedTotalCost += expectedPathCosts.get(i); // add expected costs to expected total cost
    }

    // tests that expected total cost is equal to actual total cost
    Assertions.assertEquals(expectedTotalCost, path.totalPathCost());
  }

  /**
   * This method uses fake parameters for the Shortest Path class and ensures that the class returns
   * the correct information
   * 
   * @throws FileNotFoundException
   */
  @Test
  public void test5() {
    // creats fake path of my Fall 2023 classes
    List<String> testerPath = new ArrayList<String>();
    testerPath.add("CS400 Lecture");
    testerPath.add("Stat 309 Lecture");
    testerPath.add("English 182 Discussion");
    testerPath.add("Journalism 150 Lecture");

    // creates fake costs of the time it takes me to walk between
    List<Double> testerCosts = new ArrayList<Double>();
    testerCosts.add(100.0);
    testerCosts.add(35.1);
    testerCosts.add(0.6);

    // creates fake total cost
    Double testerTotalCost = 100.0 + 35.1 + 0.6;

    // initializes a shortest path with fake data
    ShortestPath path = new ShortestPath(testerPath, testerCosts, testerTotalCost);

    // tests to ensure the shortest path class returns all of the info back correctly
    for (int i = 0; i < testerPath.size(); i++) {
      Assertions.assertEquals(path.getPath().get(i), testerPath.get(i));
    }

    for (int i = 0; i < testerCosts.size(); i++) {
      Assertions.assertEquals(path.getWalkingTimes().get(i), testerCosts.get(i));
    }
    Assertions.assertEquals(testerTotalCost, path.totalPathCost());
  }

  /**
   * This integration test ensures that the frontend can correctly read in a DOT file and when given
   * certain (fixed) user inputs can return the desired statistics about that DOT file
   */
  @Test
  public void integrationTestOne() {
    // fix user input: import data file, file name, print stats, quit
    TextUITester test = new TextUITester("1\ncampus.dot\n2\n4\n");
    // initialize necessary objects
    Backend backend = new Backend(new DijkstraGraph<String, Double>(new PlaceholderMap<>()));
    Scanner scanner = new Scanner(System.in);
    Frontend frontend = new Frontend(backend, scanner);

    frontend.run(); // run the frontend

    // ensure that the output has the right structure and right statistical values
    String actualOutput = test.checkOutput();
    System.out.println(actualOutput);
    System.out.println(actualOutput);

    Assertions.assertTrue(actualOutput.contains("Total Nodes: 160"));
    Assertions.assertTrue(actualOutput.contains("Total Edges: 400"));
    Assertions.assertTrue(actualOutput.contains("Total Walking Time: 55337.749999999985"));

  }

  /**
   * This integration test ensures that after read in a DOT file, the frontend and backend correctly
   * work together to find and print the path between two buildings
   */
  @Test
  public void integrationTestTwo() {
    // fix user input: import data file, file name, show path from MemU to Bascom Hall, Quit
    TextUITester test = new TextUITester("1\ncampus.dot\n3\nMemorial Union\nBascom Hall\n4\n");
    // initialize necessary objects
    Backend backend = new Backend(new DijkstraGraph<String, Double>(new PlaceholderMap<>()));
    Scanner scanner = new Scanner(System.in);
    Frontend frontend = new Frontend(backend, scanner);

    frontend.run(); // run the frontend

    // ensures that the output contains all of the buildings between MemU and Bascom Hall
    String actualOutput = test.checkOutput();
    Assertions.assertTrue(actualOutput.contains("Memorial Union"));
    Assertions.assertTrue(actualOutput.contains("Radio Hall"));
    Assertions.assertTrue(actualOutput.contains("Education Building"));
    Assertions.assertTrue(actualOutput.contains("North Hall"));
    Assertions.assertTrue(actualOutput.contains("Bascom Hall"));

  }

  /**
   * This method is frontend tester that ensures that when an invald integer is inputted, the output is correct
   */
  @Test
  public void frontendTestOne() {
    // fix user input: invalid integer, quit
    TextUITester test = new TextUITester("7\n4\n");
    // initialize necessary objects
    Backend backend = new Backend(new DijkstraGraph<String, Double>(new PlaceholderMap<>()));
    Scanner scanner = new Scanner(System.in);
    Frontend frontend = new Frontend(backend, scanner);

    frontend.run(); // run the frontend

    // ensures that the output is correct
    String actualOutput = test.checkOutput();
    String expectedOutput =
        "Invalid choice, please enter a valid option !\n"
        + "\n"
        + "UW PATH FINDER\n"
        + "1. Select a data file to load into Path Finder: \n"
        + "2. Show statistics about the dataset: \n"
        + "3. Lists all information between start and destination buildings: \n"
        + "4. Exit the app\n"
        + "";
    Assertions.assertTrue(actualOutput.contains(expectedOutput));
  }

  /**
   * This method ensures that for an non integer input the output is the expected string
   */
  @Test
  public void frontendTestTwo() {
    // fix user input: invalid input, quit
    TextUITester test = new TextUITester("x\n4\n");
    // initialize necessary objects
    Backend backend = new Backend(new DijkstraGraph<String, Double>(new PlaceholderMap<>()));
    Scanner scanner = new Scanner(System.in);
    Frontend frontend = new Frontend(backend, scanner);

    frontend.run(); // run the frontend
    // ensures that the output contains the right handling string
    String actualOutput = test.checkOutput();
    String expectedOutput = "Invalid input type. Please enter an integer.";
    Assertions.assertTrue(actualOutput.contains(expectedOutput));
  


  }
}
