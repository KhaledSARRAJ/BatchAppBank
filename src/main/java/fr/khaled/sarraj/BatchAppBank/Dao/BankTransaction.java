package fr.khaled.sarraj.BatchAppBank.Dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class BankTransaction implements Serializable {
    @Id
    private Long id;
    private long accountId;
    private Date transactionDate;
    @Transient
    private String strTransaction;
    private String transactionType;
    private double amount;
}
