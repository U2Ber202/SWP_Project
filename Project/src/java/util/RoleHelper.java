package util;

import model.Account;

public final class RoleHelper {

    private RoleHelper() {
    }

    public static boolean isAdmin(Account account) {
        return account != null && account.isAdmin();
    }

    public static boolean isOwner(Account account) {
        return account != null && account.isOwner();
    }

    public static boolean isCustomer(Account account) {
        return account != null && account.isCustomer();
    }

    public static boolean isShipper(Account account) {
        return account != null && account.isShipper();
    }

    public static boolean isWarehouseManager(Account account) {
        return account != null && account.isWarehouseManager();
    }

    public static boolean canManageShipping(Account account) {
        return isOwner(account) || isShipper(account);
    }

    public static boolean canManageInventory(Account account) {
        return isAdmin(account) || isWarehouseManager(account);
    }

    public static boolean canManageProduct(Account account) {
        return isAdmin(account) || isOwner(account);
    }
}