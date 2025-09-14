package com.jpmc.midascore.kafka;

import com.jpmc.midascore.component.DatabaseConduit;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionListener {

    private final DatabaseConduit databaseConduit;

    public TransactionListener(DatabaseConduit databaseConduit) {
        this.databaseConduit = databaseConduit;
    }

    static final Logger log = LoggerFactory.getLogger(TransactionListener.class);


    @KafkaListener(topics = "${general.kafka-topic}")
    public void listenToTransactions(Transaction transaction) {
        System.out.println("Recieved transaction amount: " + transaction.getAmount());
        //log.info("Received transaction: {}", transaction.getAmount());

        if (databaseConduit.doesUserExist(transaction.getRecipientId()) &&
            databaseConduit.doesUserExist(transaction.getSenderId())) {
            if (databaseConduit.getUserBalance(transaction.getSenderId()) < transaction.getAmount()) {
                // success
            }
            // user balance too low to send
            else {
                log.info("Sender has insufficient funds");
                log.info("Current balance {}", transaction.getAmount());
            }
        }
        // one of the users doesn't exist
        else {
            log.info("Either Recipient or User do not exist");
        }

    }
}
