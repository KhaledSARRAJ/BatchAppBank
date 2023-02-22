package fr.khaled.sarraj.BatchAppBank.repository;

import fr.khaled.sarraj.BatchAppBank.Dao.BankTransaction;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Component
public class BankTransactionItemProcessor implements ItemProcessor<BankTransaction, BankTransaction> {
    private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy-HH:mm");
    @Override
    public BankTransaction process(BankTransaction bank) throws Exception {
         bank.setTransactionDate(format.parse(bank.getStrTransaction()));
        return bank;
    }
}
