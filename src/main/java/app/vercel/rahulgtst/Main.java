package app.vercel.rahulgtst;

import app.vercel.rahulgtst.entities.Request;
import app.vercel.rahulgtst.strategies.FixedWindowStrategy;
import app.vercel.rahulgtst.strategies.RateLimiterStrategy;

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
    }
}