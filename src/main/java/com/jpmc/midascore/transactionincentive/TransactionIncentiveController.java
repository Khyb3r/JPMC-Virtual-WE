package com.jpmc.midascore.transactionincentive;

import com.jpmc.midascore.foundation.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class TransactionIncentiveController {
    private final RestTemplate restTemplate;

    public TransactionIncentiveController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public float postToIncentivesAPI(Transaction transaction) {
        final String uri = "http://localhost:8080/incentive";
        HttpEntity<Transaction> requestEntity = new HttpEntity<>(transaction);
        ResponseEntity<Incentive> response = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, Incentive.class);
        Incentive result = response.getBody();
        return Optional.ofNullable(result).map(Incentive::getAmount).orElse(0.0f);
    }
}
