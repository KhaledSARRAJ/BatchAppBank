package fr.khaled.sarraj.BatchAppBank.Configuration;

import fr.khaled.sarraj.BatchAppBank.Dao.BankTransaction;
import fr.khaled.sarraj.BatchAppBank.repository.BankTransactionItemAnalytics;
import fr.khaled.sarraj.BatchAppBank.repository.BankTransactionItemProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableBatchProcessing
public class SpringDataConfig {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private ItemReader<BankTransaction> bankItemReader;
    //@Autowired
    // private ItemProcessor<BankTransaction, BankTransaction> bankBankItemProcessor;
    @Autowired
    private ItemWriter<BankTransaction> bankItemWriter;
    @Autowired
    private BankTransactionItemAnalytics bankTransactionItemAnalytics;
    @Autowired
    private BankTransactionItemProcessor bankTransactionItemProcessor;

    @Bean
    public Job BankJob() {
        Step step1 = stepBuilderFactory.get("step-load-data")
                .<BankTransaction, BankTransaction>chunk(100)
                .reader(bankItemReader)
                //.processor(bankBankItemProcessor)
                .processor(CompositeItemProcessor())
                .writer(bankItemWriter)
                .build();
        return jobBuilderFactory.get("bank-data-loader-start").start(step1).build();
    }

    @Bean
    public ItemProcessor<? super BankTransaction, ? extends BankTransaction> CompositeItemProcessor() {
        List<ItemProcessor<BankTransaction, BankTransaction>> itemProcessorList = new ArrayList<>();
        itemProcessorList.add(bankTransactionItemProcessor);
        itemProcessorList.add(bankTransactionItemAnalytics);
        CompositeItemProcessor<BankTransaction, BankTransaction> compositeItemProcessor = new CompositeItemProcessor<>();
        compositeItemProcessor.setDelegates(itemProcessorList);
        return compositeItemProcessor;

    }

    @Bean
    public FlatFileItemReader<BankTransaction> flatFileItemReader(@Value("${inputFile}") Resource inputFile) {
        FlatFileItemReader<BankTransaction> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setName("FFIR1");
        flatFileItemReader.setLinesToSkip(1);
        flatFileItemReader.setResource(inputFile);
        flatFileItemReader.setLineMapper(linMapper());
        return flatFileItemReader;
    }

    @Bean
    public LineMapper<BankTransaction> linMapper() {
        DefaultLineMapper<BankTransaction> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
        delimitedLineTokenizer.setDelimiter(",");
        delimitedLineTokenizer.setStrict(false);
        delimitedLineTokenizer.setNames("id", "accountId", "strTransaction", "transactionType", "amount");
        lineMapper.setLineTokenizer(delimitedLineTokenizer);
        BeanWrapperFieldSetMapper<BankTransaction> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(BankTransaction.class);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }

}
