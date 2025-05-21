package org.milad.wallet.domain.user;

import lombok.RequiredArgsConstructor;
import org.milad.wallet.domain.wallet.Wallet;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;

    public User register(User u) {
        if (repository.findByUsername(u.getUsername()).isPresent())
            throw new RuntimeException("User exists");
        u.setPassword(encoder.encode(u.getPassword()));
        User saved = repository.save(u);
        Wallet w = new Wallet();
        w.setUser(saved);
        // walletRepo.save(w); // cascade
        return saved;
    }

    public Optional<User> findByUsername(String username) {
       return repository.findByUsername(username);
    }
}