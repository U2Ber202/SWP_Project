package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Account;
import util.PasswordUtil;

public class AcountDAO extends DBContext {

    private static final Logger LOGGER = Logger.getLogger(AcountDAO.class.getName());

    private static final String ACCOUNT_SELECT = "SELECT [uID], [user], [pass], [isAdmin], [active], "
            + "ISNULL([role], CASE WHEN isAdmin = 1 THEN 'admin' ELSE 'customer' END) AS role, "
            + "[fullname], [phone], [email], [address], [token] FROM Account";



    private Account mapAccount(ResultSet rs) throws SQLException {
        Account a = new Account();
        a.setUid(rs.getInt("uID"));
        a.setUser(rs.getString("user"));
        a.setPass(rs.getString("pass"));
        a.setRole(rs.getString("role"));
        a.setIsAdmin(rs.getInt("isAdmin"));
        a.setActive(rs.getBoolean("active"));
        a.setFullname(rs.getString("fullname"));
        a.setPhone(rs.getString("phone"));
        a.setEmail(rs.getString("email"));
        a.setAddress(rs.getString("address"));
        a.setToken(rs.getString("token"));
        return a;
    }

    public List<Account> getAllAccount() {
        return getAccountsBySql(ACCOUNT_SELECT + " ORDER BY uID DESC");
    }

    public List<Account> searchAccounts(String keyword) {
        String normalizedKeyword = keyword == null ? "" : keyword.trim();
        if (normalizedKeyword.isEmpty()) {
            return getAllAccount();
        }

        String searchPattern = "%" + normalizedKeyword.toLowerCase() + "%";
        return getAccountsBySql(ACCOUNT_SELECT
                + " WHERE LOWER(CAST([uID] AS NVARCHAR(50))) LIKE ?"
                + " OR LOWER([user]) LIKE ?"
                + " OR LOWER(ISNULL([role], CASE WHEN isAdmin = 1 THEN 'admin' ELSE 'customer' END)) LIKE ?"
                + " OR LOWER(ISNULL([email], '')) LIKE ?"
                + " OR LOWER(CASE WHEN [active] = 1 THEN 'active' ELSE 'inactive' END) LIKE ?"
                + " ORDER BY uID DESC",
                searchPattern, searchPattern, searchPattern, searchPattern, searchPattern);
    }

    public List<Account> getAccountsByRole(String role) {
        return getAccountsBySql(ACCOUNT_SELECT
                + " WHERE ISNULL([role], CASE WHEN isAdmin = 1 THEN 'admin' ELSE 'customer' END) = ? ORDER BY uID DESC", role);
    }

    public List<Account> getWarehouseManagersAvailableForStore(Integer storeId) {
        if (storeId == null) {
            // For Add Store modal: show only global managers (no history) who are not assigned to any store and are active
            return getAccountsBySql(ACCOUNT_SELECT
                    + " WHERE [role] = ? AND [active] = 1"
                    + " AND NOT EXISTS (SELECT 1 FROM Store s WHERE s.warehouse_manager_id = Account.uID)"
                    + " AND NOT EXISTS (SELECT 1 FROM StaffActionHistory h WHERE h.staff_id = Account.uID)"
                    + " ORDER BY uID DESC",
                    Account.ROLE_WAREHOUSE_MANAGER);
        }

        // For Edit Store modal: 
        // Only show staff who are ACTIVE (active = 1)
        // AND (Already assigned to this store OR (Not assigned anywhere AND (Global OR belong to this store's owner)))
        return getAccountsBySql(ACCOUNT_SELECT
                + " WHERE [role] = ? AND [active] = 1"
                + " AND ("
                + "     EXISTS (SELECT 1 FROM Store s WHERE s.warehouse_manager_id = Account.uID AND s.store_id = ?)"
                + "     OR ("
                + "         NOT EXISTS (SELECT 1 FROM Store s WHERE s.warehouse_manager_id = Account.uID)"
                + "         AND ("
                + "             NOT EXISTS (SELECT 1 FROM StaffActionHistory h WHERE h.staff_id = Account.uID)"
                + "             OR EXISTS (SELECT 1 FROM StaffActionHistory h JOIN Store s ON h.owner_id = s.owner_id WHERE h.staff_id = Account.uID AND s.store_id = ?)"
                + "         )"
                + "     )"
                + " )"
                + " ORDER BY uID DESC",
                Account.ROLE_WAREHOUSE_MANAGER, storeId, storeId);
    }

    public List<Account> getShippersByStoreId(int storeId) {
        return getAccountsBySql("SELECT DISTINCT a.*, ISNULL(a.[role], CASE WHEN a.isAdmin = 1 THEN 'admin' ELSE 'customer' END) as role "
                + "FROM Account a "
                + "LEFT JOIN Store s ON s.shipper_id = a.uID "
                + "LEFT JOIN Shipping sh ON sh.shipper_id = a.uID "
                + "WHERE a.role = 'shipper' AND a.active = 1 AND (s.store_id = ? OR sh.store_id = ?) "
                + "ORDER BY a.uID DESC",
                storeId, storeId);
    }


    public void insertOwnerAccount(String user, String pass, String email, String fullname, String phone) {
        insertAccountByRole(user, pass, email, fullname, phone, Account.ROLE_OWNER);
    }

    public void insertShipperAccount(String user, String pass, String email) {
        insertAccountByRole(user, pass, email, null, null, Account.ROLE_SHIPPER);
    }

    public int insertStaffAndReturnId(String user, String pass, String email, String fullname, String phone, String role) {
        String sql = "INSERT INTO [Account] ([user], [pass], [isAdmin], [role], [active], [email], [fullname], [phone]) VALUES (?, ?, 0, ?, 1, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement stm = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
            stm.setString(1, user);
            stm.setString(2, PasswordUtil.hash(pass));
            stm.setString(3, role);
            stm.setString(4, email);
            stm.setString(5, fullname);
            stm.setString(6, phone);
            stm.executeUpdate();
            try (ResultSet rs = stm.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public void updateStaff(int uid, String fullname, String phone, String email, boolean active) {
        executeUpdate("UPDATE [Account] SET fullname=?, phone=?, email=?, active=? WHERE uID=?",
                fullname, phone, email, active ? 1 : 0, uid);
    }

    public void insertWarehouseManagerAccount(String user, String pass, String email) {
        insertAccountByRole(user, pass, email, null, null, Account.ROLE_WAREHOUSE_MANAGER);
    }

    public Account createShipperAccount(String user, String pass, String email, String fullname, String phone) {
        insertAccountByRole(user, pass, email, fullname, phone, Account.ROLE_SHIPPER);
        return checkAccountExist(user);
    }

    public Account createWarehouseManagerAccount(String user, String pass, String email, String fullname, String phone) {
        insertAccountByRole(user, pass, email, fullname, phone, Account.ROLE_WAREHOUSE_MANAGER);
        return checkAccountExist(user);
    }

    public Account login(String user, String rawPassword) {
        Account account = checkAccountExist(user);
        if (account == null || !PasswordUtil.matches(rawPassword, account.getPass())) {
            return null;
        }
        if (PasswordUtil.needsRehash(account.getPass())) {
            String hashedPassword = PasswordUtil.hash(rawPassword);
            updatePasswordHashByUser(user, hashedPassword);
            account.setPass(hashedPassword);
        }
        return account;
    }

    public Account checkAccountExistByUserPass(String user, String pass) {
        return login(user, pass);
    }

    public Account checkAccountExist(String user) {
        return getSingleBySql(ACCOUNT_SELECT + " WHERE [user] = ?", user);
    }

    public Account getAccountByEmail(String email) {
        return getSingleBySql(ACCOUNT_SELECT + " WHERE LOWER([email]) = LOWER(?)", email);
    }

    public boolean isEmailUsedByAnotherAccount(String email, int accountId) {
        Account account = getAccountByEmail(email);
        return account != null && account.getUid() != accountId;
    }

    public void insertAccount(String user, String pass, String email) {
        insertAccountByRole(user, pass, email, null, null, Account.ROLE_CUSTOMER);
    }

    public void insertAccountWithStatus(String user, String pass, String email, boolean active, String token) {
        executeUpdate("INSERT INTO [Account] ([user], [pass], [isAdmin], [role], [active], [email], [token]) VALUES (?, ?, 0, ?, ?, ?, ?)",
                user, PasswordUtil.hash(pass), Account.ROLE_CUSTOMER, active ? 1 : 0, email, token);
    }

    public boolean activateAccount(String email, String token) {
        Account account = getAccountByEmail(email);
        if (account != null && token != null && token.equals(account.getToken())) {
            executeUpdate("UPDATE [Account] SET [active] = 1, [token] = NULL WHERE LOWER([email]) = LOWER(?)", email);
            return true;
        }
        return false;
    }



    public Account getAccountById(int accountId) {
        return getSingleBySql(ACCOUNT_SELECT + " WHERE uID = ?", accountId);
    }

    public void updateAccount(Account account) {
        executeUpdate("UPDATE [Account] SET [active] = ?, [email] = ? WHERE uId = ?",
                account.isActive(), account.getEmail(), account.getUid());
    }

    public void updateProfile(Account account) {
        executeUpdate("UPDATE [Account] SET fullname=?, phone=?, email=?, address=? WHERE uID=?",
                account.getFullname(), account.getPhone(), account.getEmail(), account.getAddress(), account.getUid());
    }

    public void UpDatePassWord(String pass, String user) {
        updatePasswordHashByUser(user, PasswordUtil.hash(pass));
    }

    private void updatePasswordHashByUser(String user, String hashedPassword) {
        executeUpdate("UPDATE [Account] SET [pass] = ? WHERE [user] = ?", hashedPassword, user);
    }

    private void insertAccountByRole(String user, String pass, String email, String fullname, String phone, String role) {
        executeUpdate("INSERT INTO [Account] ([user], [pass], [isAdmin], [role], [active], [email], [fullname], [phone]) VALUES (?, ?, 0, ?, 1, ?, ?, ?)",
                user, PasswordUtil.hash(pass), role, email, fullname, phone);
    }

    private List<Account> getAccountsBySql(String sql, Object... params) {
        List<Account> list = new ArrayList<>();
        try (Connection connection = getConnection();
                PreparedStatement stm = connection.prepareStatement(sql)) {
            bindParams(stm, params);
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    list.add(mapAccount(rs));
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return list;
    }

    private Account getSingleBySql(String sql, Object... params) {
        try (Connection connection = getConnection();
                PreparedStatement stm = connection.prepareStatement(sql)) {
            bindParams(stm, params);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    return mapAccount(rs);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private void executeUpdate(String sql, Object... params) {
        try (Connection connection = getConnection();
                PreparedStatement stm = connection.prepareStatement(sql)) {
            bindParams(stm, params);
            stm.executeUpdate();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    private void bindParams(PreparedStatement stm, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            stm.setObject(i + 1, params[i]);
        }
    }

    public List<Account> getStaffByOwner(int ownerId) {
        String sql = ACCOUNT_SELECT + " WHERE uID IN ("
                + "    SELECT h.staff_id FROM StaffActionHistory h WHERE h.owner_id = ?"
                + "    UNION"
                + "    SELECT s.shipper_id FROM Store s WHERE s.owner_id = ? AND s.shipper_id IS NOT NULL"
                + "    UNION"
                + "    SELECT s.warehouse_manager_id FROM Store s WHERE s.owner_id = ? AND s.warehouse_manager_id IS NOT NULL"
                + "    UNION"
                + "    SELECT sh.shipper_id FROM Shipping sh JOIN Store s ON sh.store_id = s.store_id WHERE s.owner_id = ? AND sh.shipper_id IS NOT NULL"
                + "    UNION"
                + "    SELECT si.created_by FROM StockImport si JOIN Store s ON si.store_id = s.store_id WHERE s.owner_id = ? AND si.created_by IS NOT NULL"
                + ") AND (role = 'shipper' OR role = 'warehouse_manager')";
        return getAccountsBySql(sql, ownerId, ownerId, ownerId, ownerId, ownerId);
    }
}
