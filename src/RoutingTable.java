import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoutingTable {
    private int nodeId;
    private Map<Integer, List<RoutingEntry>> routes;

    // Entry trong routing table
    class RoutingEntry {
        int nextHop;
        double pheromone;
        double cost;
        double lastUpdate;
        int hopCount;

        public RoutingEntry(int nextHop, double pheromone) {
            this.nextHop = nextHop;
            this.pheromone = pheromone;
            this.lastUpdate = System.currentTimeMillis();
        }

        // Tính xác suất chọn route này
        public double getProbability(double alpha, double beta) {
            double eta = 1.0 / cost;  // Heuristic
            return Math.pow(pheromone, alpha) * Math.pow(eta, beta);
        }
    }

    public RoutingTable(int nodeId) {
        this.nodeId = nodeId;
        this.routes = new HashMap<>();
    }

    // Thêm hoặc update route
    public void updateRoute(int destination, int nextHop,
                            double pheromone, double cost) {
        if (!routes.containsKey(destination)) {
            routes.put(destination, new ArrayList<>());
        }

        // Tìm entry có sẵn hoặc tạo mới
        List<RoutingEntry> entries = routes.get(destination);
        RoutingEntry entry = entries.stream()
                .filter(e -> e.nextHop == nextHop)
                .findFirst()
                .orElse(null);

        if (entry == null) {
            entry = new RoutingEntry(nextHop, pheromone);
            entry.cost = cost;
            entries.add(entry);
        } else {
            entry.pheromone = pheromone;
            entry.cost = cost;
            entry.lastUpdate = System.currentTimeMillis();
        }
    }

    // Chọn next hop theo xác suất
    public int selectNextHop(int destination, double alpha, double beta) {
        List<RoutingEntry> entries = routes.get(destination);
        if (entries == null || entries.isEmpty()) {
            return -1;
        }

        // Tính xác suất cho mỗi route
        double[] probabilities = new double[entries.size()];
        double sum = 0;

        for (int i = 0; i < entries.size(); i++) {
            probabilities[i] = entries.get(i).getProbability(alpha, beta);
            sum += probabilities[i];
        }

        // Roulette wheel selection
        double rand = Math.random() * sum;
        double cumsum = 0;

        for (int i = 0; i < entries.size(); i++) {
            cumsum += probabilities[i];
            if (cumsum >= rand) {
                return entries.get(i).nextHop;
            }
        }

        return entries.get(entries.size()-1).nextHop;
    }
}