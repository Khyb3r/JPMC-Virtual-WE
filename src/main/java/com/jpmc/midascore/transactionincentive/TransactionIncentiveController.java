package com.jpmc.midascore.transactionincentive;

import com.jpmc.midascore.foundation.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger log = LoggerFactory.getLogger(TransactionIncentiveController.class);

    private final RestTemplate restTemplate;

    public TransactionIncentiveController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public float postToIncentivesAPI(Transaction transaction) {
        try {
            final String uri = "http://localhost:8080/incentive";
            HttpEntity<Transaction> requestEntity = new HttpEntity<>(transaction);
            ResponseEntity<Incentive> response = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, Incentive.class);
            Incentive result = response.getBody();
            return Optional.ofNullable(result).map(Incentive::getAmount).orElse(0.0f);
        }
        catch (Exception e) {
            log.error("Error calling transaction incentive API", e);
            return 0.0f;
        }
    }
}
