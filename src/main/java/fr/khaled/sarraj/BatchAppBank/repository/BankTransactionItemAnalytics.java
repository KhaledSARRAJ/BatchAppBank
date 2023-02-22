package fr.khaled.sarraj.BatchAppBank.repository;

import fr.khaled.sarraj.BatchAppBank.Dao.BankTransaction;
import lombok.Getter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class BankTransactionItemAnalytics implements ItemProcessor<BankTransaction, BankTransaction> {
    @Getter
    private double totalDebit;
    @Getter
    private double totalCredit;

    @Override
    public BankTransaction process(BankTransaction bank) throws Exception {
        if (bank.getTransactionType().equals("D")) totalDebit += bank.getAmount();
        if (bank.getTransactionType().equals("C")) totalCredit += bank.getAmount();

        return bank;
    }
}
