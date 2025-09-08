import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseStation {
    private double x, y;
    private List<DataPacket> receivedPackets;
    private double totalDataReceived;
    private Map<Integer, Integer> packetCountBySource;

    public BaseStation(double x, double y) {
        this.x = x;
        this.y = y;
        this.receivedPackets = new ArrayList<>();
        this.packetCountBySource = new HashMap<>();
    }

    // Nhận data packet
    public void receivePacket(DataPacket packet) {
        receivedPackets.add(packet);
        totalDataReceived += packet.getSize();

        int sourceId = packet.getSourceId();
        packetCountBySource.put(sourceId,
                packetCountBySource.getOrDefault(sourceId, 0) + 1);
    }

    // Tính packet delivery ratio
    public double calculatePDR(int totalSent) {
        return (double) receivedPackets.size() / totalSent;
    }

    // Tính average delay
    public double calculateAverageDelay() {
        if (receivedPackets.isEmpty()) return 0;

        double totalDelay = receivedPackets.stream()
                .mapToDouble(DataPacket::getDelay)
                .sum();

        return totalDelay / receivedPackets.size();
    }
}