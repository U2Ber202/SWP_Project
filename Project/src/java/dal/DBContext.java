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
        DataSource dataSource = lookupDataSource();
        if (dataSource != null) {
            return dataSource.getConnection();
        }
        return createFallbackConnection();
    }

    private DataSource lookupDataSource() {
        try {
            InitialContext context = new InitialContext();
            return (DataSource) context.lookup(DATASOURCE_JNDI);
        } catch (NamingException ex) {
            LOGGER.log(Level.FINE, "JNDI datasource not available, falling back to direct JDBC connection.", ex);
            return null;
        }
    }

    private Connection createFallbackConnection() throws SQLException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException ex) {
            throw new SQLException("SQL Server JDBC driver not found.", ex);
        }

        String url = AppConfig.getRequired("DB_URL", null);
        String user = AppConfig.getRequired("DB_USERNAME", null);
        String password = AppConfig.getRequired("DB_PASSWORD", null);
        return DriverManager.getConnection(url, user, password);
    }
}
