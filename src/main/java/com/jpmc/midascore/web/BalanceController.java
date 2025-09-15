package com.jpmc.midascore.web;

import com.jpmc.midascore.component.DatabaseConduit;
import com.jpmc.midascore.foundation.Balance;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/balance")
public class BalanceController {
    private final DatabaseConduit databaseConduit;

    public BalanceController(DatabaseConduit databaseConduit) {
        this.databaseConduit = databaseConduit;
    }

    @GetMapping
    public Balance getBalance(@RequestParam Long userId) {
        // make sure user exists
        float balance;
        if (databaseConduit.doesUserExist(userId)) {
            balance = databaseConduit.getUserBalance(userId);
        }
        else {
            balance = 0;
        }
        return new Balance(balance);
    }
}
