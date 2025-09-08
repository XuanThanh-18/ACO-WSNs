public class BasicACO {
    protected NetworkTopology network;
    protected PheromoneMatrix pheromones;
    protected int antCount = 50;
    protected int maxIterations = 1000;
    protected double alpha = 1.0;  // Pheromone weight
    protected double beta = 3.0;   // Heuristic weight

    public Solution findOptimalPath(int source, int destination) {
        Solution bestSolution = null;

        for (int iter = 0; iter < maxIterations; iter++) {
            List<AntSolution> iterationSolutions = new ArrayList<>();

            // Bước 1: Mỗi ant tìm một path
            for (int a = 0; a < antCount; a++) {
                ForwardAnt ant = new ForwardAnt(source, destination);

                // Ant di chuyển từ source đến destination
                while (!ant.hasReachedDestination()) {
                    int currentNode = ant.getCurrentNode();
                    int nextNode = ant.selectNextNode(currentNode,
                            pheromones,
                            network);

                    if (nextNode == -1) {
                        // Dead end - restart
                        break;
                    }

                    ant.moveToNode(nextNode);
                    ant.updatePathCost(network);
                }

                if (ant.hasReachedDestination()) {
                    iterationSolutions.add(ant.getSolution());
                }
            }

            // Bước 2: Cập nhật best solution
            AntSolution iterBest = findBestSolution(iterationSolutions);
            if (bestSolution == null ||
                    iterBest.getCost() < bestSolution.getCost()) {
                bestSolution = iterBest;
            }

            // Bước 3: Update pheromone
            pheromones.updatePheromone(iterationSolutions);

            // Bước 4: Check convergence
            if (hasConverged()) {
                break;
            }
        }

        return bestSolution;
    }
}