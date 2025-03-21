package dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBContext {
    protected Connection connect;

    private final String serverName = "localhost";
    private final String dbName = "ISP";
    private final String portNumber = "1433";
    private final String userID = "sa";
    private final String password = "123";

    public DBContext() {
        try {
            String url = "jdbc:sqlserver://" + serverName + ":" + portNumber +
                         ";databaseName=" + dbName + ";encrypt=false";
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); 
            connect = DriverManager.getConnection(url, userID, password);
            
            if (connect != null) {
                System.out.println("✅ Kết nối database thành công!");
            } else {
                System.err.println("❌ Kết nối database thất bại!");
            }

        } catch (ClassNotFoundException e) {
            System.err.println("❌ Không tìm thấy driver JDBC!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("❌ Lỗi kết nối SQL Server!");
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connect;
    }
}
