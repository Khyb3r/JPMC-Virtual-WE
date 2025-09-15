package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.repository.TransactionRecordsRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class DatabaseConduit {
    private final UserRepository userRepository;

    private final TransactionRecordsRepository transactionRecordsRepository;

    public DatabaseConduit(UserRepository userRepository, TransactionRecordsRepository transactionRecordsRepository) {
        this.userRepository = userRepository;
        this.transactionRecordsRepository = transactionRecordsRepository;
    }

    @Transactional
    public void save(UserRecord userRecord) {
        userRepository.save(userRecord);
    }

    @Transactional
    public boolean doesUserExist(long id) {
        return userRepository.existsById(id);
    }

    // should use map and orElse to throw exception to be more safe
    @Transactional
    public float getUserBalance(long id) {
        return userRepository.findById(id).getBalance();
    }


    // needed to make this atomic
    @Transactional
    public void saveTransaction(long senderID, long recipientID, float amount, float incentive) {
        UserRecord sender = userRepository.findById(senderID);
        UserRecord recipient = userRepository.findById(recipientID);
        TransactionRecord transactionRecord = new TransactionRecord(recipient, sender, amount, incentive);
        transactionRecord.setTimestamp(LocalDateTime.now());
        transactionRecordsRepository.save(transactionRecord);
        // update user balance
        sender.setBalance(sender.getBalance() - amount);
        recipient.setBalance(recipient.getBalance() + amount + incentive);
        userRepository.save(sender);
        userRepository.save(recipient);
    }

    @Transactional
    public String isPerson(long id) {
        return userRepository.findById(id).getName();
    }
}
