package com.models;

import java.math.BigDecimal;
import java.util.List;

public interface VoucherDAO {

    void save(Voucher voucher);

    List<Voucher> findAll();

    void delete(int voucherID);

    Voucher findByCode(String code);

    void assignVoucherToAccount(int accountID, int voucherID);

    void markVoucherAsUsed(int accountID, int voucherID);

    List<Voucher> getAccountVouchers(int accountID, BigDecimal orderValue);

    List<Voucher> getAvailableVouchersForAccount(int accountID, BigDecimal orderValue);

    List<Voucher> getUnavailableVouchersForAccount(int accountID, BigDecimal orderValue);

    boolean canUseVoucher(int accountID, int voucherID, BigDecimal orderValue);

    Voucher validateAndGetVoucher(int accountID, String voucherCode, BigDecimal orderValue);

    void incrementUsageCount(int voucherID);

    int getVoucherUsageCount(int voucherID);

    boolean accountHasVoucher(int accountID, int voucherID);

    public boolean markVoucherAsUsed(Integer accountId, int voucherCode, String orderId);

    public boolean incrementVoucherUsedCount(String voucherCode);

    public boolean canUseVoucher(Integer accountId, String voucherCode);

    public AccountVoucher getAccountVoucher(Integer accountId, String voucherCode);

    public List<Voucher> searchByKeyword(String keyword, int page, int pageSize);

    public int getTotalVouchers();

    public List<Voucher> findPaginated(int page, int pageSize);

    boolean existsByCode(String code);

    void addVoucher(Voucher voucher);

    public Voucher findById(int voucherID);

    public void update(Voucher voucher) throws Exception;

    boolean existsByCodeExceptId(String code, int voucherID);
    public void deleteById(int voucherID);
}
