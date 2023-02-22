package fr.khaled.sarraj.BatchAppBank.repository;

import fr.khaled.sarraj.BatchAppBank.Dao.BankTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;


public interface BankTransactionRepository extends JpaRepository<BankTransaction,Long> {
}
