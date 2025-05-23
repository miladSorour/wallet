package org.milad.wallet.domain.wallet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Optional<Wallet> findByUserUsername(@Param("username") String username);

    @Query(value = "select t from Wallet t where t.user.id = :userId")
    Optional<Wallet> findByUser(Long userId);
}