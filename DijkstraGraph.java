// --== CS400 File Header Information ==--
// Name: Noah Kornfeld
// Email: nekornfeld@wisc.edu
// Group and Team: E19
// Group TA: Lakshika Rathi
// Lecturer: Gary Dahl
// Notes to Grader: <optional extra notes>

import java.util.PriorityQueue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * This class extends the BaseGraph data structure with additional methods for computing the total
 * cost and list of node data along the shortest path connecting a provided starting to ending
 * nodes. This class makes use of Dijkstra's shortest path algorithm.
 */
public class DijkstraGraph<NodeType, EdgeType extends Number> extends BaseGraph<NodeType, EdgeType>
    implements GraphADT<NodeType, EdgeType> {

  /**
   * While searching for the shortest path between two nodes, a SearchNode contains data about one
   * specific path between the start node and another node in the graph. The final node in this path
   * is stored in its node field. The total cost of this path is stored in its cost field. And the
   * predecessor SearchNode within this path is referened by the predecessor field (this field is
   * null within the SearchNode containing the starting node in its node field).
   *
   * SearchNodes are Comparable and are sorted by cost so that the lowest cost SearchNode has the
   * highest priority within a java.util.PriorityQueue.
   */
  protected class SearchNode implements Comparable<SearchNode> {
    public Node node;
    public double cost;
    public SearchNode predecessor;

    public SearchNode(Node node, double cost, SearchNode predecessor) {
      this.node = node;
      this.cost = cost;
      this.predecessor = predecessor;
    }

    public int compareTo(SearchNode other) {
      if (cost > other.cost)
        return +1;
      if (cost < other.cost)
        return -1;
      return 0;
    }
  }

  /**
   * Constructor that sets the map that the graph uses.
   * 
   * @param map the map that the graph uses to map a data object to the node object it is stored in
   */
  public DijkstraGraph(MapADT<NodeType, Node> map) {
    super(map);
  }

  /**
   * This helper method creates a network of SearchNodes while computing the shortest path between
   * the provided start and end locations. The SearchNode that is returned by this method is
   * represents the end of the shortest path that is found: it's cost is the cost of that shortest
   * path, and the nodes linked together through predecessor references represent all of the nodes
   * along that shortest path (ordered from end to start).
   *
   * @param start the data item in the starting node for the path
   * @param end   the data item in the destination node for the path
   * @return SearchNode for the final end node within the shortest path
   * @throws NoSuchElementException when no path from start to end is found or when either start or
   *                                end data do not correspond to a graph node
   */
  protected SearchNode computeShortestPath(NodeType start, NodeType end) {
    if (start == null || end == null) {
      throw new NoSuchElementException();
    }
    PriorityQueue<SearchNode> queue = new PriorityQueue<SearchNode>(); // create priority queue

    // map of paths already found
    MapADT<NodeType, Double> pathsFound = new PlaceholderMap<NodeType, Double>();
    // create node from start NodeType
    Node startNode = nodes.get(start);
    // get edgesLeaving


    // add start node as a search node with null predecessor
    queue.add(new SearchNode(startNode, 0, null));
    pathsFound.put(start, 0.0); // add to map of pathsFound

    // set current variables

    // while loop to add every shortest path to every shortest node until queue is empty
    while (!queue.isEmpty()) {
      // set currentSearch node to front of queue
      SearchNode currentSearchNode = queue.poll();
      Node currentNode = currentSearchNode.node; // currentNode to currentSearchNode's node

      // if shortest found path to end node
      if (currentSearchNode.node.data.equals(end)) {
        return currentSearchNode; // return path
      }
      // add all of the edges from the current node into the graph as new searchNodes
      for (Edge edge : currentNode.edgesLeaving) {
        Double pathCost = currentSearchNode.cost + edge.data.doubleValue(); // pathCost = old cost + edge
        Node successor = edge.successor;
        // add new search node to queue, node is end of edge, path cost above
        if (!pathsFound.containsKey(successor.data)) {
          pathsFound.put(successor.data, pathCost);
          queue.add(new SearchNode(successor, pathCost, currentSearchNode));

        } else if (pathCost < pathsFound.get(successor.data)) { // node is not in map
          pathsFound.remove(successor.data);
          pathsFound.put(successor.data, pathCost);
          queue.add(new SearchNode(successor, pathCost, currentSearchNode));

        }
      }
    }

    // if path to end node was never found, throw exception
    throw new NoSuchElementException("No Path Between Start and End Nodes");

  }

  /**
   * Returns the list of data values from nodes along the shortest path from the node with the
   * provided start value through the node with the provided end value. This list of data values
   * starts with the start value, ends with the end value, and contains intermediary values in the
   * order they are encountered while traversing this shorteset path. This method uses Dijkstra's
   * shortest path algorithm to find this solution.
   *
   * @param start the data item in the starting node for the path
   * @param end   the data item in the destination node for the path
   * @return list of data item from node along this shortest path
   */
  public List<NodeType> shortestPathData(NodeType start, NodeType end) {
    // get shortest path between start and end
    SearchNode shortestPathSearchNode = computeShortestPath(start, end);

    // list to hold NodeTypes along path
    List<NodeType> shortestPath = new LinkedList<NodeType>();

    // add all of nodes along path into list of NodeTypes
    while (shortestPathSearchNode != null) {
      // add next node to the front
      shortestPath.add(0, shortestPathSearchNode.node.data);
      // move through search node's path backwards
      shortestPathSearchNode = shortestPathSearchNode.predecessor;
    }
    return shortestPath; // list of NodeTypes that create the path
  }

  /**
   * Returns the cost of the path (sum over edge weights) of the shortest path freom the node
   * containing the start data to the node containing the end data. This method uses Dijkstra's
   * shortest path algorithm to find this solution.
   *
   * @param start the data item in the starting node for the path
   * @param end   the data item in the destination node for the path
   * @return the cost of the shortest path between these nodes
   */
  public double shortestPathCost(NodeType start, NodeType end) {
    // get shortest path between start and end
    SearchNode shortestPathSearchNode = computeShortestPath(start, end);

    // return cost of that path
    return shortestPathSearchNode.cost;
  }

  // TODO: implement 3+ tests in step 4.1

  /**
   * JUnit tester method that tests that the shortestPathData() and shortestPathCost() methods
   * return the right paths and costs for nodes D and I as we did in class
   */
  @Test
  public void test1() {
    // creates the map and Dijkstra objects to test on
    MapADT<String, BaseGraph<String, Integer>.Node> map = new PlaceholderMap<>();
    DijkstraGraph<String, Integer> test = new DijkstraGraph<String, Integer>(map);

    // inserts all of the nodes
    test.insertNode("A");
    test.insertNode("B");
    test.insertNode("D");
    test.insertNode("E");
    test.insertNode("F");
    test.insertNode("G");
    test.insertNode("H");
    test.insertNode("I");
    test.insertNode("L");
    test.insertNode("M");

    // A's leaving edges
    test.insertEdge("A", "B", 1);
    test.insertEdge("A", "M", 5);
    test.insertEdge("A", "H", 8);

    // B's leaving edges
    test.insertEdge("B", "M", 3);

    // D's leaving edges
    test.insertEdge("D", "A", 7);
    test.insertEdge("D", "G", 2);

    // E's leaving edges --> none

    // F's leaving edges
    test.insertEdge("F", "G", 9);

    // G's leaving edges
    test.insertEdge("G", "L", 7);

    // H's leaving edges
    test.insertEdge("H", "B", 6);
    test.insertEdge("H", "I", 2);

    // I's leaving edges
    test.insertEdge("I", "D", 1);
    test.insertEdge("I", "H", 2);
    test.insertEdge("I", "L", 5);

    // L's leaving edges --> none

    // M's leaving edges
    test.insertEdge("M", "E", 3);
    test.insertEdge("M", "F", 4);

    // list containing shortest path between D and I
    List<String> actualPath = test.shortestPathData("D", "I");

    // tests to make sure the list has the correct nodes and the right cost
    Assertions.assertEquals(actualPath.get(0), "D");
    Assertions.assertEquals(actualPath.get(1), "A");
    Assertions.assertEquals(actualPath.get(2), "H");
    Assertions.assertEquals(actualPath.get(3), "I");
    Assertions.assertEquals(test.shortestPathCost("D", "I"), 17);

  }

  /**
   * JUnit tester method that tests that the shortestPathData() and shortestPathCost() methods
   * return the right paths and costs for nodes I and G
   */
  @Test
  public void test2() {

    // creates the map and Dijkstra objects to test on
    MapADT<String, BaseGraph<String, Integer>.Node> map = new PlaceholderMap<>();
    DijkstraGraph<String, Integer> test = new DijkstraGraph<String, Integer>(map);

    // inserts all of the nodes
    test.insertNode("A");
    test.insertNode("B");
    test.insertNode("D");
    test.insertNode("E");
    test.insertNode("F");
    test.insertNode("G");
    test.insertNode("H");
    test.insertNode("I");
    test.insertNode("L");
    test.insertNode("M");

    // A's leaving edges
    test.insertEdge("A", "B", 1);
    test.insertEdge("A", "M", 5);
    test.insertEdge("A", "H", 8);

    // B's leaving edges
    test.insertEdge("B", "M", 3);

    // D's leaving edges
    test.insertEdge("D", "A", 7);
    test.insertEdge("D", "G", 2);

    // E's leaving edges --> none

    // F's leaving edges
    test.insertEdge("F", "G", 9);

    // G's leaving edges
    test.insertEdge("G", "L", 7);

    // H's leaving edges
    test.insertEdge("H", "B", 6);
    test.insertEdge("H", "I", 2);

    // I's leaving edges
    test.insertEdge("I", "D", 1);
    test.insertEdge("I", "H", 2);
    test.insertEdge("I", "L", 5);

    // L's leaving edges --> none

    // M's leaving edges
    test.insertEdge("M", "E", 3);
    test.insertEdge("M", "F", 4);

    // list containing the shortest path between I and G
    List<String> actualPath = test.shortestPathData("I", "G");

    // tests to make sure the list has the correct nodes and the right cost
    Assertions.assertEquals(actualPath.get(0), "I");
    Assertions.assertEquals(actualPath.get(1), "D");
    Assertions.assertEquals(actualPath.get(2), "G");
    Assertions.assertEquals(test.shortestPathCost("I", "G"), 3);
  }

  /**
   * JUnit tester method that tests that for a 2 nodes that have no path between them, the
   * shortestPathData() method throws a NoSuchElementException
   */
  @Test
  public void test3() {

    // creates the map and Dijkstra objects to test on
    MapADT<String, BaseGraph<String, Integer>.Node> map = new PlaceholderMap<>();
    DijkstraGraph<String, Integer> test = new DijkstraGraph<String, Integer>(map);

    // insert all of the nodes
    test.insertNode("A");
    test.insertNode("B");
    test.insertNode("D");
    test.insertNode("E");
    test.insertNode("F");
    test.insertNode("G");
    test.insertNode("H");
    test.insertNode("I");
    test.insertNode("L");
    test.insertNode("M");

    // A's leaving edges
    test.insertEdge("A", "B", 1);
    test.insertEdge("A", "M", 5);
    test.insertEdge("A", "H", 8);

    // B's leaving edges
    test.insertEdge("B", "M", 3);

    // D's leaving edges
    test.insertEdge("D", "A", 7);
    test.insertEdge("D", "G", 2);

    // E's leaving edges --> none

    // F's leaving edges
    test.insertEdge("F", "G", 9);

    // G's leaving edges
    test.insertEdge("G", "L", 7);

    // H's leaving edges
    test.insertEdge("H", "B", 6);
    test.insertEdge("H", "I", 2);

    // I's leaving edges
    test.insertEdge("I", "D", 1);
    test.insertEdge("I", "H", 2);
    test.insertEdge("I", "L", 5);

    // L's leaving edges --> none

    // M's leaving edges
    test.insertEdge("M", "E", 3);
    test.insertEdge("M", "F", 4);
    // tests that shortestPath throws a no such element exception
    Assertions.assertThrows(NoSuchElementException.class, () -> test.shortestPathCost("E", "A"));


  }

  /**
   * Tester method to test basic functions of computeShortestPath(), shortestPathData() and
   * shortestPathCost() methods. This includes ensuring every node is in the map, the edgeCount and
   * nodeCounts are correct and various paths between nodes are correct in terms of data and cost
   * 
   */
  @Test
  public void test4() {

    // creates the map and Dijkstra objects to test on
    MapADT<String, BaseGraph<String, Integer>.Node> map = new PlaceholderMap<>();
    DijkstraGraph<String, Integer> test = new DijkstraGraph<String, Integer>(map);

    // insert all of the nodes
    test.insertNode("A");
    test.insertNode("B");
    test.insertNode("C");
    test.insertNode("D");
    test.insertNode("E");
    test.insertNode("F");

    // A's leaving edges
    test.insertEdge("A", "B", 4);
    test.insertEdge("A", "C", 2);
    test.insertEdge("A", "D", 6);

    // B's leaving edges
    test.insertEdge("B", "F", 9);

    // C's leaving edges
    test.insertEdge("C", "D", 2);

    // D's leaving edges
    test.insertEdge("D", "F", 2);

    // tests to ensure all nodes are present and nodeCount and edgeCount are correct
    Assertions.assertEquals(map.containsKey("A"), true);
    Assertions.assertEquals(map.containsKey("B"), true);
    Assertions.assertEquals(map.containsKey("C"), true);
    Assertions.assertEquals(map.containsKey("D"), true);
    Assertions.assertEquals(map.containsKey("E"), true);
    Assertions.assertEquals(map.containsKey("F"), true);
    Assertions.assertEquals(test.getEdgeCount(), 6);
    Assertions.assertEquals(test.getNodeCount(), 6);



    // list containing the shortest path between A and D
    List<String> actualPath = test.shortestPathData("A", "D");

    // tests to make sure the list has the correct nodes and the right cost
    Assertions.assertEquals(actualPath.get(0), "A");
    Assertions.assertEquals(actualPath.get(1), "C");
    Assertions.assertEquals(actualPath.get(2), "D");
    Assertions.assertEquals(test.shortestPathCost("A", "D"), 4);


    // another test of a path (between A and F)
    actualPath = test.shortestPathData("A", "F");
    Assertions.assertEquals(actualPath.get(0), "A");
    Assertions.assertEquals(actualPath.get(1), "C");
    Assertions.assertEquals(actualPath.get(2), "D");
    Assertions.assertEquals(actualPath.get(3), "F");
    Assertions.assertEquals(test.shortestPathCost("A", "F"), 6);


    // ensures path between a node and itself has one node and a cost of 0
    actualPath = test.shortestPathData("A", "A");
    Assertions.assertEquals(actualPath.get(0), "A");
    Assertions.assertEquals(test.shortestPathCost("A", "A"), 0);

  }

  /**
   * Tester method to ensure when first path that is found is not the shortest path,
   * computeShorestPath() returns the correct path
   */
  @Test
  public void test5() {
    MapADT<String, BaseGraph<String, Integer>.Node> map = new PlaceholderMap<>();
    DijkstraGraph<String, Integer> test = new DijkstraGraph<String, Integer>(map);

    // insert all of the nodes
    test.insertNode("A");
    test.insertNode("B");
    test.insertNode("C");

    // insert all edges
    test.insertEdge("A", "B", 4);
    test.insertEdge("A", "C", 1);
    test.insertEdge("C", "B", 1);

    // list containing the shortest path between I and G
    List<String> actualPath = test.shortestPathData("A", "B");

    // tests to make sure the list has the correct nodes and the right cost
    Assertions.assertEquals(actualPath.get(0), "A");
    Assertions.assertEquals(actualPath.get(1), "C");
    Assertions.assertEquals(actualPath.get(2), "B");
    Assertions.assertEquals(test.shortestPathCost("A", "B"), 2);
  }
}

