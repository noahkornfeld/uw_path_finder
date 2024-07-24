import java.util.List;

/**
 * This interface models a class that returns information for the shortest path between two nodes (buildings)
 * @author noahkornfeld
 *
 */
public interface ShortestPathInterface {

  //commented out constructor
  //public ShortestPathInterface(String startBuilding, String destinationBuilding, DijkstraGraph<String, Integer> graph)
  
  /**
   * getter method that returns a List of the nodes along the shortest path
   * @return a list of the nodes along the shortest path
   */
  public List<String> getPath();
  
  /**
   * getter method that returns a list of the walking times from building to building
   * @return a list of the walking times
   */
  public List<Double> getWalkingTimes();
  
  /**
   * a getter method that returns the total cost (time) to walk from the start building to the destination
   * @return the total cost of the path
   */
  public double totalPathCost();
}
