import java.util.ArrayList;
import java.util.List;

public class AntSolution {
    private List<Integer> path;
    private double totalCost;
    private double totalEnergy;
    private double totalDelay;
    private double fitness;
    private long timestamp;

    public AntSolution() {
        this.path = new ArrayList<>();
        this.totalCost = 0;
        this.totalEnergy = 0;
        this.totalDelay = 0;
        this.timestamp = System.currentTimeMillis();
    }

    // Thêm node vào path
    public void addNode(int nodeId) {
        path.add(nodeId);
    }

    // Tính fitness dựa trên multi-objective
    public void calculateFitness(double w1, double w2, double w3) {
        // Fitness = w1*Energy + w2*Delay + w3*HopCount
        fitness = w1 * totalEnergy +
                w2 * totalDelay +
                w3 * path.size();
    }

    // So sánh với solution khác
    public boolean isBetterThan(AntSolution other) {
        return this.fitness < other.fitness;
    }

    // Clone solution
    public AntSolution clone() {
        AntSolution copy = new AntSolution();
        copy.path = new ArrayList<>(this.path);
        copy.totalCost = this.totalCost;
        copy.totalEnergy = this.totalEnergy;
        copy.totalDelay = this.totalDelay;
        copy.fitness = this.fitness;
        return copy;
    }
}