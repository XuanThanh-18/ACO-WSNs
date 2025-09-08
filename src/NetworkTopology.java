import java.util.*;

public class NetworkTopology {
    private List<SensorNode> nodes;
    private double[][] adjacencyMatrix;
    private Map<Integer, List<Integer>> neighborMap;
    private double communicationRange;
    private BaseStation baseStation;

    public NetworkTopology(int nodeCount, double areaSize,
                           double commRange) {
        this.nodes = new ArrayList<>();
        this.communicationRange = commRange;
        this.adjacencyMatrix = new double[nodeCount][nodeCount];
        this.neighborMap = new HashMap<>();

        // Khởi tạo base station ở giữa
        this.baseStation = new BaseStation(areaSize/2, areaSize/2);

        // Deploy nodes ngẫu nhiên
        deployNodes(nodeCount, areaSize);

        // Xây dựng topology
        buildTopology();
    }

    // Deploy nodes ngẫu nhiên trong area
    private void deployNodes(int count, double areaSize) {
        Random rand = new Random();

        for (int i = 0; i < count; i++) {
            double x = rand.nextDouble() * areaSize;
            double y = rand.nextDouble() * areaSize;
            double initialEnergy = 0.5 + rand.nextDouble() * 0.5; // 0.5-1.0J

            SensorNode node = new SensorNode(i, x, y, initialEnergy);
            nodes.add(node);
        }
    }

    // Xây dựng adjacency matrix và neighbor list
    private void buildTopology() {
        for (int i = 0; i < nodes.size(); i++) {
            neighborMap.put(i, new ArrayList<>());

            for (int j = 0; j < nodes.size(); j++) {
                if (i != j) {
                    double distance = nodes.get(i).distanceTo(nodes.get(j));

                    // Nodes trong communication range là neighbors
                    if (distance <= communicationRange) {
                        adjacencyMatrix[i][j] = distance;
                        neighborMap.get(i).add(j);
                    } else {
                        adjacencyMatrix[i][j] = Double.POSITIVE_INFINITY;
                    }
                }
            }
        }
    }

    // Lấy neighbors của một node
    public List<Integer> getNeighbors(int nodeId) {
        return new ArrayList<>(neighborMap.get(nodeId));
    }

    // Kiểm tra connectivity
    public boolean isConnected() {
        // BFS từ node 0 để check có reach được tất cả nodes
        boolean[] visited = new boolean[nodes.size()];
        Queue<Integer> queue = new LinkedList<>();

        queue.offer(0);
        visited[0] = true;
        int count = 1;

        while (!queue.isEmpty()) {
            int current = queue.poll();

            for (int neighbor : getNeighbors(current)) {
                if (!visited[neighbor] && nodes.get(neighbor).isAlive()) {
                    visited[neighbor] = true;
                    queue.offer(neighbor);
                    count++;
                }
            }
        }

        return count == getAliveNodeCount();
    }

    // Đếm số nodes còn sống
    public int getAliveNodeCount() {
        return (int) nodes.stream()
                .filter(SensorNode::isAlive)
                .count();
    }
}