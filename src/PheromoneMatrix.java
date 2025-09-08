import java.util.List;

public class PheromoneMatrix {
    private double[][] pheromone;
    private int nodeCount;
    private double initialPheromone = 1.0;
    private double evaporationRate = 0.2;  // ρ

    public PheromoneMatrix(int nodeCount) {
        this.nodeCount = nodeCount;
        this.pheromone = new double[nodeCount][nodeCount];
        initializePheromone();
    }

    // Khởi tạo pheromone ban đầu
    private void initializePheromone() {
        for (int i = 0; i < nodeCount; i++) {
            for (int j = 0; j < nodeCount; j++) {
                pheromone[i][j] = initialPheromone;
            }
        }
    }

    // Cập nhật pheromone sau mỗi iteration
    public void updatePheromone(List<AntSolution> solutions) {
        // Bước 1: Evaporation (bay hơi)
        evaporatePheromone();

        // Bước 2: Deposit (thêm pheromone mới)
        for (AntSolution solution : solutions) {
            depositPheromone(solution);
        }
    }

    // Bay hơi pheromone: τ = (1-ρ) × τ
    private void evaporatePheromone() {
        for (int i = 0; i < nodeCount; i++) {
            for (int j = 0; j < nodeCount; j++) {
                pheromone[i][j] *= (1 - evaporationRate);

                // Giới hạn pheromone minimum
                if (pheromone[i][j] < 0.01) {
                    pheromone[i][j] = 0.01;
                }
            }
        }
    }

    // Thêm pheromone: Δτ = Q/L
    private void depositPheromone(AntSolution solution) {
        double Q = 100;  // Hằng số pheromone
        double deposit = Q / solution.getTotalCost();

        List<Integer> path = solution.getPath();
        for (int i = 0; i < path.size() - 1; i++) {
            int from = path.get(i);
            int to = path.get(i + 1);

            // Cập nhật cả 2 chiều
            pheromone[from][to] += deposit;
            pheromone[to][from] += deposit;
        }
    }
}