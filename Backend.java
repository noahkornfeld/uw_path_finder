import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is a backend class for an app that reads in a paths between buildings, returns the shortest
 * path between 2 buildings, and various statistics on the data set
 * 
 * @author noahkornfeld
 *
 */
public class Backend implements BackendInterface {

  // class variables
  GraphADT<String, Double> graph;
  private double totalEdgeWeight = 0.0;

  /**
   * constructor that takes in a Dijkstra Graph to use
   * 
   * @param graph the graph to add nodes too
   */
  public Backend(DijkstraGraph<String, Double> graph) {
    this.graph = graph;
  }

  public static void main(String[] args) {
    DijkstraGraph<String,Double> graph = new DijkstraGraph<String, Double>(new PlaceholderMap<>());
    Backend backend = new Backend(graph);
    Scanner scanner = new Scanner(System.in);
    Frontend frontend = new Frontend(backend, scanner);
    frontend.run();
  }

  /**
   * Overridden method that reads in a .DOT data file and turns it into a fully functioning Dijkstra
   * Graph
   * 
   * @throws FileNotFoundException if the file does not exist
   */
  @Override
  public void readData(String filePath) throws FileNotFoundException {
    File file = new File(filePath); // reads file in
    if (!file.exists()) { // throws FileNotFound exception if file does not exist
      throw new FileNotFoundException("File Does Not Exist");
    }

    // line reader to read each line of the DOT file
    BufferedReader reader = new BufferedReader(new FileReader(filePath));
    String line; // current line reader is on
    // Regex expression to split line into Node1, Node2, and Edge Cost (Weight)
    Pattern pattern = Pattern.compile("\\s*\"([^\"]*)\" -- \"([^\"]*)\" \\[seconds=([0-9.]+)\\];");


    try {
      // while there is still another line to read
      while ((line = reader.readLine()) != null) {
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) { // if the pattern is found in the line
          // match node 1 to group 1, node 2 to group 2 and weight to group
          String node1 = matcher.group(1);
          String node2 = matcher.group(2);
          double weight = Double.parseDouble(matcher.group(3));

          // insert nodes and edges if they do not already exist
          if (!graph.containsNode(node1)) {
            graph.insertNode(node1);

          }
          if (!graph.containsNode(node2)) {
            graph.insertNode(node2);

          }
          if (!graph.containsEdge(node1, node2)) {
            graph.insertEdge(node1, node2, weight);
          }
          if (graph.containsEdge(node2, node1)) {
            graph.insertEdge(node2, node1, weight);
          }
          totalEdgeWeight += weight; // add weight to totalEdgeWeight
        }
      }
      reader.close(); // end reader
    } catch (NumberFormatException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }



  /**
   * Overridden method that returns a string containing the statistics about the data set
   * 
   * @return a string with the statistics of the data set
   */
  @Override
  public String getStatistics() {
    // returns total nodes, total undirected edges, and total weight of the data set
    return "Total Nodes: " + graph.getNodeCount() + " Total Edges: " + graph.getEdgeCount() / 2
        + " Total Walking Time: " + totalEdgeWeight / 2;
  }

  /**
   * Overridden method that returns an instance of the shortest path interface to store the data of
   * the shortest path between two given nodes
   * 
   * @param startBuiliding      is the starting building of the path
   * @param destinationBuilding is the destination building of the path
   * @return an instance of the ShortestPathInterface to store the shorest path info
   */
  public ShortestPathInterface getShortestPath(String startBuilding, String destinationBuilding) {

    // shortest path of the two buildings
    List<String> shortestPath = graph.shortestPathData(startBuilding, destinationBuilding);
    // total path cost between the two buildings
    Double totalPathCost = graph.shortestPathCost(startBuilding, destinationBuilding);
    List<Double> pathEdges = new LinkedList<Double>(); // list of edges along path

    // loops through to find each edge cost in the path and adds it to pathEdges
    for (int i = 0; i < shortestPath.size() - 1; i++) {
      pathEdges.add(graph.getEdge(shortestPath.get(i), shortestPath.get(i + 1)));
    }
    // returns new ShortestPath will all of the necessary info
    return new ShortestPath(shortestPath, pathEdges, totalPathCost);

  }


}
