package Controller.Queue;

import dal.DAODebtRecords;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import model.DebtRecords;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class WebAppListener implements ServletContextListener {

    public static final BlockingQueue<DebtRecords> debtQueue = new LinkedBlockingQueue<>();
    private final ExecutorService executorService = Executors.newFixedThreadPool(1); // 1 luồng xử lý chính
    private final ExecutorService debtChecker = Executors.newSingleThreadExecutor(); // 1 luồng check lại mỗi 5 giây
    private final Lock lock = new ReentrantLock();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        DAODebtRecords dao = new DAODebtRecords();

        // Luồng chính xử lý hàng đợi debtQueue
            executorService.submit(() -> {
                while (true) {
                    try {
                        DebtRecords record = debtQueue.take(); // Lấy dữ liệu từ hàng đợi
                        processDebtRecord(record, dao); // Gọi hàm xử lý
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            });
        

        // Luồng phụ check lại các bản ghi chưa xử lý mỗi 5 giây
        debtChecker.submit(() -> {
            while (true) {
                try {
                    List<DebtRecords> unprocessedDebts = dao.getDebtRecordsActive(); // Lấy bản ghi chưa xử lý
                    debtQueue.addAll(unprocessedDebts); // Đẩy lại vào queue
                    Thread.sleep(5000); // Chờ 5 giây trước khi kiểm tra lại
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
    }

    private void processDebtRecord(DebtRecords record, DAODebtRecords dao) {
        lock.lock(); // Chặn xung đột giữa các thread
        try {
            dao.connect.setAutoCommit(false); // Bắt đầu transaction

            // Cập nhật nợ khách hàng
            dao.updateCustomerDebt(record.getCustomerID(), record.getPaymentStatus(), record.getAmountOwed());

            // Đánh dấu là đã xử lý trong bảng DebtRecords
            dao.updateDebtRecordActive(record);

            dao.connect.commit(); // Commit nếu thành công

        } catch (SQLException e) {
            try {
                dao.connect.rollback(); // Rollback nếu lỗi
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            lock.unlock(); // Mở khóa
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        executorService.shutdown(); // Tắt luồng chính khi server shutdown
        debtChecker.shutdown(); // Tắt luồng check lại mỗi 5 giây
    }
}



