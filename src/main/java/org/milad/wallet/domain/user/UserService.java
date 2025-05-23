package org.milad.wallet.domain.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.milad.wallet.common.UtilPasswordValidator;
import org.milad.wallet.domain.wallet.Wallet;
import org.milad.wallet.domain.wallet.WalletService;
import org.milad.wallet.exception.BadRequestAlertException;
import org.milad.wallet.exception.RecordNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final WalletService walletService;
    private final PasswordEncoder encoder;

    @Transactional
    public User register(User user) {
        UtilPasswordValidator.isPasswordComplex(user.getPassword());
        if (repository.findByUsername(user.getUsername()).isPresent()) {
            throw new BadRequestAlertException("invalid username", "user", "NV");
        }

        user.setPassword(encoder.encode(user.getPassword()));
        user.setEnabled(Boolean.TRUE);
        user.setAccountNonLocked(Boolean.TRUE);
        user.setFailedAttempts(0);
        User userDB = save(user);
        Wallet wallet = new Wallet();
        wallet.setUser(userDB);
        walletService.save(wallet);
        return userDB;
    }

    @Transactional
    public void increaseFailedAttempts(String username) {
        User user = repository.findByUsername(username).orElseThrow(() -> new RecordNotFoundException("user"));
        int newFailCount = user.getFailedAttempts() + 1;

        if (newFailCount >= 10) {
            user.setAccountNonLocked(false);
            user.setLockTime(Instant.now());
        }
        user.setFailedAttempts(newFailCount);
        repository.save(user);
    }

    @Transactional
    public User save(User user) {
        return repository.save(user);
    }

    @Transactional
    public User resetFailedAttempts(User user) {
        user.setAccountNonLocked(true);
        user.setFailedAttempts(0);
        user.setLockTime(null);
        return save(user);
    }

    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username);
    }
}