package ru.yandex.practicum.accounts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.accounts.model.Account;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountsRepository extends JpaRepository<Account, Long> {
    Optional<Account> findAccountByUserId(Long userId);

    Optional<Account> findAccountByAccountNumber(Long accountNumber);

    @Modifying
    @Query("UPDATE Account a SET a.balance = a.balance + :amount WHERE a.accountNumber = :accountNumber")
    void addCashToBalance(@Param("accountNumber") Long accountNumber, @Param("amount") Double amount);

    @Modifying
    @Query("UPDATE Account a SET a.balance = a.balance - :amount WHERE a.accountNumber = :accountNumber")
    void substractCashFromBalance(@Param("accountNumber") Long accountNumber, @Param("amount") Double amount);
}
