package com.jpmc.midascore.kafka;

import com.jpmc.midascore.foundation.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionListener {

    static final Logger log = LoggerFactory.getLogger(TransactionListener.class);

    @KafkaListener(topics = "${general.kafka-topic}")
    public void listenToTransactions(Transaction transaction) {
        System.out.println("Recieved transaction amount: " + transaction.getAmount());
        //log.info("Received transaction: {}", transaction.getAmount());
    }
}
