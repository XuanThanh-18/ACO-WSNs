import java.util.List;

public class AdaptiveChaosACO extends BasicACO {
    private double chaosValue = 0.5;
    private double chaosR = 3.99;  // Logistic map parameter
    private double chaosWeight = 0.1;  // γ

    // Override phương thức update pheromone
    @Override
    protected void updatePheromoneWithChaos(List<AntSolution> solutions) {
        // Update chaos value
        updateChaos();

        // Adaptive evaporation rate
        double adaptiveRho = calculateAdaptiveEvaporation();

        // Evaporate với adaptive rate
        evaporateWithRate(adaptiveRho);

        // Deposit với chaos disturbance
        for (AntSolution solution : solutions) {
            depositWithChaos(solution);
        }
    }

    // Logistic chaos map: x(n+1) = r × x(n) × (1 - x(n))
    private void updateChaos() {
        chaosValue = chaosR * chaosValue * (1 - chaosValue);

        // Đảm bảo chaos value trong [0,1]
        if (chaosValue <= 0 || chaosValue >= 1) {
            chaosValue = 0.5;  // Reset nếu thoát khỏi chaotic region
        }
    }

    // Adaptive evaporation: ρ(t) = ρmax - (t/T)(ρmax - ρmin)
    private double calculateAdaptiveEvaporation() {
        double rhoMax = 0.9;
        double rhoMin = 0.1;
        double progress = (double)currentIteration / maxIterations;

        return rhoMax - progress * (rhoMax - rhoMin);
    }

    // Deposit với chaos: τ = τ + Δτ + γ×Chaos
    private void depositWithChaos(AntSolution solution) {
        double Q = 100;
        double deposit = Q / solution.getTotalCost();

        List<Integer> path = solution.getPath();
        for (int i = 0; i < path.size() - 1; i++) {
            int from = path.get(i);
            int to = path.get(i + 1);

            // Thêm chaos disturbance
            double chaosDisturbance = chaosWeight * chaosValue;

            pheromone[from][to] += deposit + chaosDisturbance;
            pheromone[to][from] += deposit + chaosDisturbance;
        }
    }

    // Adaptive beta với sigmoid
    private double calculateAdaptiveBeta() {
        double betaMin = 1.0;
        double betaMax = 5.0;
        double k = 0.01;  // Sigmoid steepness

        double t = currentIteration;
        double T = maxIterations;

        // Sigmoid function
        double sigmoid = 1.0 / (1.0 + Math.exp(-k * (t - T/2)));

        return betaMin + (betaMax - betaMin) * sigmoid;
    }
}