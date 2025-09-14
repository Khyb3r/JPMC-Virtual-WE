package com.jpmc.midascore.kafka;

import com.jpmc.midascore.component.DatabaseConduit;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.UserRepository;
import com.jpmc.midascore.transactionincentive.TransactionIncentiveController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TransactionListener {

    private final DatabaseConduit databaseConduit;

    private final TransactionIncentiveController transactionIncentiveController;

    public TransactionListener(DatabaseConduit databaseConduit, TransactionIncentiveController transactionIncentiveController) {
        this.databaseConduit = databaseConduit;
        this.transactionIncentiveController = transactionIncentiveController;
    }

    static final Logger log = LoggerFactory.getLogger(TransactionListener.class);


    @KafkaListener(topics = "${general.kafka-topic}")
    public void listenToTransactions(Transaction transaction) {
        //System.out.println("Recieved transaction amount: " + transaction.getAmount());
        //log.info("Received transaction: {}", transaction.getAmount());

        if (databaseConduit.doesUserExist(transaction.getRecipientId()) &&
            databaseConduit.doesUserExist(transaction.getSenderId())) {
            if (databaseConduit.getUserBalance(transaction.getSenderId()) >= transaction.getAmount()) {
                // success
                float incentiveAmount = transactionIncentiveController.postToIncentivesAPI(transaction);
                databaseConduit.saveTransaction(transaction.getSenderId(),
                        transaction.getRecipientId(),
                        transaction.getAmount(), incentiveAmount);

                log.info("Successfully saved transaction to database");
                //log.info("User new balance {}, Recipient new balance {}",databaseConduit.getUserBalance(transaction.getSenderId()),databaseConduit.getUserBalance(transaction.getRecipientId()));

                if (databaseConduit.isPerson(transaction.getRecipientId()).equalsIgnoreCase("wilbur")) {
                    log.info("Wilbur balance {}", databaseConduit.getUserBalance(transaction.getRecipientId()));
                }
                else if (databaseConduit.isPerson(transaction.getSenderId()).equalsIgnoreCase("wilbur")) {
                    log.info("Wilbur balance {}", databaseConduit.getUserBalance(transaction.getSenderId()));
                }


                if (databaseConduit.isPerson(transaction.getRecipientId()).equalsIgnoreCase("waldorf")) {
                    log.info("Waldorf balance {}", databaseConduit.getUserBalance(transaction.getRecipientId()));
                }
                else if (databaseConduit.isPerson(transaction.getSenderId()).equalsIgnoreCase("waldorf")) {
                    log.info("Waldorf balance {}", databaseConduit.getUserBalance(transaction.getSenderId()));
                }
                else {
                    log.info("Neither of these are Waldorf");
                }
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
