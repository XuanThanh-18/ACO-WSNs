import java.util.ArrayList;
import java.util.List;

public class DataPacket {
    private int packetId;
    private int sourceId;
    private int destinationId;
    private int size;  // bits
    private double creationTime;
    private double arrivalTime;
    private List<Integer> path;

    public DataPacket(int id, int source, int dest, int size) {
        this.packetId = id;
        this.sourceId = source;
        this.destinationId = dest;
        this.size = size;
        this.creationTime = System.currentTimeMillis();
        this.path = new ArrayList<>();
    }

    // Tính delay end-to-end
    public double getDelay() {
        return arrivalTime - creationTime;
    }

    // Thêm node vào path
    public void addToPath(int nodeId) {
        path.add(nodeId);
    }

    // Tính hop count
    public int getHopCount() {
        return path.size() - 1;  // Trừ source node
    }
}