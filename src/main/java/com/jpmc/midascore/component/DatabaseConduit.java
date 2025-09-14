package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.repository.TransactionRecordsRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class DatabaseConduit {
    private final UserRepository userRepository;

    private final TransactionRecordsRepository transactionRecordsRepository;

    public DatabaseConduit(UserRepository userRepository, TransactionRecordsRepository transactionRecordsRepository) {
        this.userRepository = userRepository;
        this.transactionRecordsRepository = transactionRecordsRepository;
    }

    public void save(UserRecord userRecord) {
        userRepository.save(userRecord);
    }

    public boolean doesUserExist(long id) {
        return userRepository.existsById(id);
    }

    // should use map and orElse to throw exception to be more safe
    public float getUserBalance(long id) {
        return userRepository.findById(id).getBalance();
    }



}
