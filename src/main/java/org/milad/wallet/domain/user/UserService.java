package org.milad.wallet.domain.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.milad.wallet.common.UtilPasswordValidator;
import org.milad.wallet.domain.wallet.Wallet;
import org.milad.wallet.domain.wallet.WalletService;
import org.milad.wallet.exception.BadRequestAlertException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        User userDB = repository.save(user);
        Wallet wallet = new Wallet();
        wallet.setUser(userDB);
        walletService.save(wallet);
        return userDB;
    }

    public Optional<User> findByUsername(String username) {
       return repository.findByUsername(username);
    }
}