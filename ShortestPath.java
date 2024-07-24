import java.util.List;

/**
 * this class stores all of the information about a shortest path between two buildings
 * 
 * @author noahkornfeld
 *
 */
public class ShortestPath implements ShortestPathInterface {

  // private class variables
  private List<String> path;
  private List<Double> walkingTimes;
  private Double totalPathCost;

  /**
   * constructor for the class
   * 
   * @param path          is a list of buildings along the path
   * @param walkingTimes  is a list of edge costs between the buildings along the path
   * @param totalPathCost is the total cost of the pah
   */
  public ShortestPath(List<String> path, List<Double> walkingTimes, Double totalPathCost) {
    this.path = path;
    this.walkingTimes = walkingTimes;
    this.totalPathCost = totalPathCost;
  }

  /**
   * getter method that returns the list of buildings along the path
   */
  @Override
  public List<String> getPath() {
    return this.path;
  }

  /**
   * getter method that returns the list of edge costs between the buildings along the path
   */
  @Override
  public List<Double> getWalkingTimes() {
    return this.walkingTimes;
  }

  /**
   * getter method that returns the total cost of ts he whole path
   */
  @Override
  public double totalPathCost() {
    return this.totalPathCost;
  }

}
