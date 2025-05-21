package org.milad.wallet.domain.user;

import lombok.RequiredArgsConstructor;
import org.milad.wallet.domain.wallet.Wallet;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;

    public User register(User u) {
        if (userRepo.findByEmail(u.getEmail()).isPresent())
            throw new RuntimeException("User exists");
        u.setPassword(encoder.encode(u.getPassword()));
        User saved = userRepo.save(u);
        Wallet w = new Wallet();
        w.setUser(saved);
        // walletRepo.save(w); // cascade
        return saved;
    }
}