/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller.Queue;

/**
 *
 * @author ADMIN
 */
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

// Object đại diện cho 1 đơn hàng nhập
public class AddImportOrderQueue {

    // Hàng đợi đơn hàng
    private final BlockingQueue<ImportOrderTask> queue;

    // Singleton instance
    private static AddImportOrderQueue instance;

    // Khởi tạo hàng đợi và worker thread
    private AddImportOrderQueue() {
        queue = new LinkedBlockingQueue<>();
        startWorker();
    }

    // Đảm bảo chỉ tạo 1 instance (Singleton pattern)
    public static synchronized AddImportOrderQueue getInstance() {
        if (instance == null) {
            instance = new AddImportOrderQueue();
        }
        return instance;
    }

    // Hàm thêm đơn hàng vào queue
    public void addOrder(ImportOrderTask orderTask) {
        queue.offer(orderTask);
    }

    // Worker thread để xử lý đơn hàng trong queue
    private void startWorker() {
        Thread worker = new Thread(() -> {
            while (true) {
                try {
                    // Lấy đơn hàng tiếp theo để xử lý
                    ImportOrderTask task = queue.take();
                    task.processOrder(); // Xử lý đơn hàng
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Khôi phục trạng thái ngắt
                    break;
                }
            }
        });
        worker.setDaemon(true); // Đảm bảo thread tắt khi ứng dụng dừng
        worker.start();
    }
    
}
