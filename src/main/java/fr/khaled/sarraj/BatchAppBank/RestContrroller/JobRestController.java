package fr.khaled.sarraj.BatchAppBank.RestContrroller;

import fr.khaled.sarraj.BatchAppBank.repository.BankTransactionItemAnalytics;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class JobRestController {
    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private Job job;

    @Autowired
    private BankTransactionItemAnalytics bankTransactionItemAnalytics;

    @GetMapping("/startJob")
    public BatchStatus Load() throws Exception {
        Map<String, JobParameter> map = new HashMap<>();
        map.put("time", new JobParameter(System.currentTimeMillis()));
        JobParameters parameters = new JobParameters(map);

        JobExecution jobExecution = jobLauncher.run(job, parameters);
        while (jobExecution.isRunning()) {
            System.out.println("....");
        }
        return jobExecution.getStatus();
    }

    @GetMapping("/analytics")
    public Map<String, Double> Analytics() throws Exception {
        Map<String, Double> map = new HashMap<>();
        map.put("Le totale de debit votre de compte bancaire est : ", bankTransactionItemAnalytics.getTotalDebit());
        map.put("Le totale de de crédit de votre compte bancaire est : ", bankTransactionItemAnalytics.getTotalCredit());

        return map;
    }
}
