/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.models;

import java.util.List;
import java.util.Map;


public interface AccountVoucherDAO {

    void create(AccountVoucher av) throws Exception;

    void update(AccountVoucher av) throws Exception;

    void deleteById(int id) throws Exception;

    AccountVoucher findById(int id);

    List<AccountVoucher> findPaginated(int page, int pageSize);

    int getTotalAccountVouchers();

    List<AccountVoucher> searchByKeyword(String keyword, int page, int pageSize);

    List<Map<String, Object>> findAccountsForDropdown();

    List<Map<String, Object>> findVouchersForDropdown();
}
