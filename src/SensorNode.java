import java.util.ArrayList;
import java.util.List;

public class SensorNode {
    private int nodeId;
    private double x, y;           // Vị trí trong mạng
    private double currentEnergy;  // Năng lượng hiện tại
    private double initialEnergy;  // Năng lượng ban đầu
    private boolean isAlive;
    private List<Integer> neighbors;  // Danh sách láng giềng
    private RoutingTable routingTable;

    // Constructor khởi tạo node
    public SensorNode(int id, double x, double y, double initialEnergy) {
        this.nodeId = id;
        this.x = x;
        this.y = y;
        this.initialEnergy = initialEnergy;
        this.currentEnergy = initialEnergy;
        this.isAlive = true;
        this.neighbors = new ArrayList<>();
        this.routingTable = new RoutingTable(id);
    }

    // Tính khoảng cách Euclidean đến node khác
    public double distanceTo(SensorNode other) {
        return Math.sqrt(Math.pow(x - other.x, 2) +
                Math.pow(y - other.y, 2));
    }

    // Tính năng lượng truyền theo mô hình radio
    public double calculateTransmissionEnergy(int bits, double distance) {
        double Eelec = 50e-9;  // 50 nJ/bit
        double Efs = 10e-12;   // Free space
        double Emp = 0.0013e-12; // Multipath
        double d0 = Math.sqrt(Efs/Emp); // Threshold distance

        if (distance < d0) {
            // Mô hình free space (d²)
            return bits * (Eelec + Efs * distance * distance);
        } else {
            // Mô hình multipath (d⁴)
            return bits * (Eelec + Emp * Math.pow(distance, 4));
        }
    }

    // Cập nhật năng lượng sau khi truyền/nhận
    public void updateEnergy(double energyConsumed) {
        currentEnergy -= energyConsumed;
        if (currentEnergy <= 0) {
            currentEnergy = 0;
            isAlive = false;
        }
    }
}