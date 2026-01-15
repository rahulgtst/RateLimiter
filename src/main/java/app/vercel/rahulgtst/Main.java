package app.vercel.rahulgtst;

import app.vercel.rahulgtst.entities.Request;
import app.vercel.rahulgtst.strategies.*;

import java.util.Scanner;

class RateLimiter {
    private final RateLimiterStrategy rateLimiterStrategy;

    public RateLimiter(RateLimiterStrategy strategy) {
        rateLimiterStrategy = strategy;
    }

    public boolean check(Request req) {
        return rateLimiterStrategy.check(req);
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Duration in seconds:");
        long duration = sc.nextLong();

        System.out.println("Max request limit:");
        long limit = sc.nextLong();

        RateLimiter limiter =
                new RateLimiter(new FixedWindowStrategy(limit, duration));

        for(int i = 1; i <= 5; i++) {
            boolean isValid = limiter.check(new Request("123"));

            System.out.println(
                    i + (isValid ? " Request is valid!" : " Request is invalid!")
            );
        }

        RateLimiter slidingLimiter =
                new RateLimiter(new SlidingWindowStrategy(limit, duration));

        for(int i = 1; i <= 5; i++) {
            boolean isValid = slidingLimiter.check(new Request("1234"));

            System.out.println(
                    i + (isValid ? " Request is valid!" : " Request is invalid!")
            );
        }

        System.out.println("Refill rate per seconds:");
        double refillRate = sc.nextDouble();

        System.out.println("Max capacity:");
        double capacity = sc.nextDouble();

        RateLimiter tokenBucket =
                new RateLimiter(new TokenBucketStrategy(refillRate, capacity));

        for(int i = 1; i <= 5; i++) {
            boolean isValid = tokenBucket.check(new Request("12345"));

            System.out.println(
                    i + (isValid ? " Request is valid!" : " Request is invalid!")
            );
        }

        RateLimiter leakyBucket =
                new RateLimiter(new LeakyBucketStrategy(refillRate, capacity));

        for(int i = 1; i <= 5; i++) {
            boolean isValid = leakyBucket.check(new Request("12345"));

            System.out.println(
                    i + (isValid ? " Request is valid!" : " Request is invalid!")
            );
        }
    }
}