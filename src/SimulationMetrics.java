import java.util.ArrayList;
import java.util.List;

public class SimulationMetrics {
    private int firstNodeDeath = -1;
    private int halfNodesDeath = -1;
    private int lastNodeDeath = -1;
    private double totalEnergyConsumed = 0;
    private List<Double> roundEnergy;
    private List<Integer> aliveNodesPerRound;
    private double packetDeliveryRatio;
    private double averageDelay;

    public SimulationMetrics() {
        this.roundEnergy = new ArrayList<>();
        this.aliveNodesPerRound = new ArrayList<>();
    }

    // Update metrics mỗi round
    public void updateRound(int round, NetworkTopology network,
                            double energyConsumed) {
        int aliveNodes = network.getAliveNodeCount();
        aliveNodesPerRound.add(aliveNodes);
        roundEnergy.add(energyConsumed);
        totalEnergyConsumed += energyConsumed;

        // Track node deaths
        if (firstNodeDeath == -1 &&
                aliveNodes < network.getNodeCount()) {
            firstNodeDeath = round;
        }

        if (halfNodesDeath == -1 &&
                aliveNodes <= network.getNodeCount() / 2) {
            halfNodesDeath = round;
        }

        if (aliveNodes == 0) {
            lastNodeDeath = round;
        }
    }

    // Generate report
    public void printReport() {
        System.out.println("=== SIMULATION RESULTS ===");
        System.out.println("First Node Death: Round " + firstNodeDeath);
        System.out.println("Half Nodes Death: Round " + halfNodesDeath);
        System.out.println("Last Node Death: Round " + lastNodeDeath);
        System.out.println("Total Energy Consumed: " + totalEnergyConsumed + " J");
        System.out.println("Average Energy/Round: " +
                (totalEnergyConsumed / roundEnergy.size()) + " J");
        System.out.println("Packet Delivery Ratio: " +
                (packetDeliveryRatio * 100) + "%");
        System.out.println("Average End-to-End Delay: " + averageDelay + " ms");
    }
}