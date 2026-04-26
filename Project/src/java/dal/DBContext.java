package dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import util.AppConfig;

public class DBContext {

    private static final Logger LOGGER = Logger.getLogger(DBContext.class.getName());
    private static final String DATASOURCE_JNDI = "java:comp/env/jdbc/ProjectDS";

    protected Connection getConnection() throws SQLException {
        String url = "jdbc:sqlserver://localhost:1433;databaseName=PRJ301;encrypt=true;trustServerCertificate=true";
        String user = "SA";
        String pass = "123456";

        try {
            // Đảm bảo Driver được load
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(url, user, pass);
        } catch (ClassNotFoundException ex) {
            LOGGER.log(Level.SEVERE, "Không tìm thấy Driver SQL Server (sqljdbc42.jar). Hãy đảm bảo file này có trong thư mục lib của dự án.", ex);
            throw new SQLException("JDBC Driver not found", ex);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Lỗi kết nối Database. Hãy kiểm tra: 1. SQL Server đã chạy? 2. Port 1433 đã mở? 3. User/Pass đúng?", ex);
            throw ex;
        }
    }
}
