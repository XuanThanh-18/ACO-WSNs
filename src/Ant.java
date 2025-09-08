import java.util.List;
import java.util.Set;

public class Ant {
    private int antId;
    private int sourceNode;
    private int destinationNode;
    private List<Integer> path;      // Đường đi đã qua
    private Set<Integer> tabuList;   // Tránh lặp node
    private double pathCost;          // Chi phí đường đi
    private double totalDelay;
    private double totalEnergy;

    // Forward Ant - Tìm đường từ source đến destination
    public class ForwardAnt extends Ant {

        // Chọn next hop dựa trên xác suất ACO
        public int selectNextNode(int currentNode,
                                  PheromoneMatrix pheromones,
                                  NetworkTopology network) {

            List<Integer> candidates = network.getNeighbors(currentNode);
            candidates.removeAll(tabuList); // Loại bỏ nodes đã qua

            if (candidates.isEmpty()) return -1;

            double[] probabilities = new double[candidates.size()];
            double sum = 0;

            // Tính xác suất cho mỗi neighbor
            for (int i = 0; i < candidates.size(); i++) {
                int nextNode = candidates.get(i);

                // Pheromone component
                double tau = pheromones.get(currentNode, nextNode);

                // Heuristic component (1/distance hoặc energy/distance)
                double eta = calculateHeuristic(currentNode, nextNode, network);

                // Công thức xác suất ACO: P = (τ^α × η^β) / Σ(τ^α × η^β)
                probabilities[i] = Math.pow(tau, ALPHA) * Math.pow(eta, BETA);
                sum += probabilities[i];
            }

            // Normalize probabilities
            if (sum > 0) {
                for (int i = 0; i < probabilities.length; i++) {
                    probabilities[i] /= sum;
                }
            }

            // Roulette wheel selection
            return rouletteWheelSelection(candidates, probabilities);
        }

        // Heuristic function η
        private double calculateHeuristic(int from, int to,
                                          NetworkTopology network) {
            SensorNode nodeFrom = network.getNode(from);
            SensorNode nodeTo = network.getNode(to);

            double distance = nodeFrom.distanceTo(nodeTo);
            double energy = nodeTo.getCurrentEnergy();

            // Heuristic kết hợp khoảng cách và năng lượng
            return (energy / nodeFrom.getInitialEnergy()) / distance;
        }
    }
}