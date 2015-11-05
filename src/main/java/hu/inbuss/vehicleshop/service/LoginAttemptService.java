package hu.inbuss.vehicleshop.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 *
 * @author Szecskó Zoltán <zoltan.szecsko@gmail.com>
 */
@Service
public class LoginAttemptService {

    public static final int MAX_ATTEMPT = 3;
    private static LoadingCache<String, Integer> attemptsCache;
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginAttemptService.class);

    public LoginAttemptService() {
        attemptsCache = CacheBuilder.newBuilder().
                expireAfterWrite(1, TimeUnit.DAYS).build(new CacheLoader<String, Integer>() {
                    @Override
                    public Integer load(String key) {
                        return 0;
                    }
                });
    }

    public void loginSucceeded(final String key) {
        LOGGER.info("loginSucceeded(): ip = " + key);
        attemptsCache.invalidate(key);
    }

    public void loginFailed(final String key) {
        LOGGER.info("loginFailed(): ip = " + key);
        int attempts;

        try {
            attempts = attemptsCache.get(key);
        } catch (ExecutionException e) {
            attempts = 0;
        }

        attempts++;
        attemptsCache.put(key, attempts);
        LOGGER.debug("login attempts for that user: " + attempts);
    }

    public boolean isBlocked(final String key) {
        try {
            return attemptsCache.get(key) >= MAX_ATTEMPT;
        } catch (ExecutionException e) {
            return false;
        }
    }
}
