package faultTolerance;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import org.eclipse.microprofile.faulttolerance.CircuitBreaker;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CoffeeRepositoryService {

    private AtomicLong counter = new AtomicLong(0);

 
    @CircuitBreaker(requestVolumeThreshold = 2)
    public Integer getAvailability(Coffee coffee) {
        maybeFail();
   
        return new Random().nextInt(30);
    }

    private void maybeFail() {

        final Long invocationNumber = counter.getAndIncrement();
        if (invocationNumber % 4 > 1) {
            throw new RuntimeException("Service failed.");
        }    
}

public Coffee getCoffeeById(int id) {
    Coffee coffee = new Coffee();
    coffee.setId(id);
    return coffee;
}
}
