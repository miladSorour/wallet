package org.milad.wallet.domain.wallet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.milad.wallet.domain.transaction.Transaction;
import org.milad.wallet.domain.transaction.TransactionRepository;
import org.milad.wallet.domain.transaction.TransactionType;
import org.milad.wallet.domain.user.User;
import org.milad.wallet.exception.InsufficientBalanceException;
import org.milad.wallet.exception.RecordNotFoundException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

    private final String username = "john_doe";
    @Mock
    private WalletRepository repository;
    @Mock
    private TransactionRepository transactionRepository;
    @InjectMocks
    private WalletService service;
    private Wallet sharedWallet;

    @BeforeEach
    void setup() {
        User user = new User();
        user.setUsername(username);
        user.setPassword("password");

        sharedWallet = new Wallet();
        sharedWallet.setId(1L);
        sharedWallet.setBalance(100.0);
        sharedWallet.setUser(user);
    }

    @Test
    void testTopUpSuccessfully() {
        double topUpAmount = 50.0;

        when(repository.findByUserUsername(username)).thenReturn(Optional.of(sharedWallet));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Transaction tx = service.topUp(username, topUpAmount);

        assertNotNull(tx);
        assertEquals(TransactionType.TOPUP, tx.getType());
        assertEquals(topUpAmount, tx.getAmount());
        assertEquals("Top-up", tx.getDetail());
        assertEquals(150.0, sharedWallet.getBalance());

        verify(repository).findByUserUsername(username);
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void testTopUpWhenWalletNotFound() {
        when(repository.findByUserUsername("unknown_user")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.topUp("unknown_user", 100.0));

        assertEquals("Wallet not found", exception.getMessage());
        verify(repository).findByUserUsername("unknown_user");
        verifyNoMoreInteractions(transactionRepository);
    }

    @Test
    void testTransferSuccessfully() {
        String fromUser = "alice";
        String toUser = "bob";
        double amount = 50.0;

        User alice = new User();
        alice.setUsername(fromUser);

        User bob = new User();
        bob.setUsername(toUser);

        Wallet srcWallet = new Wallet();
        srcWallet.setId(1L);
        srcWallet.setUser(alice);
        srcWallet.setBalance(100.0);

        Wallet dstWallet = new Wallet();
        dstWallet.setId(2L);
        dstWallet.setUser(bob);
        dstWallet.setBalance(20.0);

        when(repository.findByUserUsername(fromUser)).thenReturn(Optional.of(srcWallet));
        when(repository.findByUserUsername(toUser)).thenReturn(Optional.of(dstWallet));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        service.transfer(fromUser, toUser, amount);

        // Verify balances updated correctly
        assertEquals(50.0, srcWallet.getBalance());
        assertEquals(70.0, dstWallet.getBalance());

        // Verify transactions saved
        verify(transactionRepository, times(2)).save(any(Transaction.class));

        // Verify wallets retrieved
        verify(repository).findByUserUsername(fromUser);
        verify(repository).findByUserUsername(toUser);
    }


    @Test
    void testTransferThrowsExceptionWhenSourceWalletNotFound() {
        when(repository.findByUserUsername("unknown")).thenReturn(Optional.empty());

        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> service.transfer("unknown", "bob", 10.0));
        assertEquals("record not found", exception.getMessage());

        verify(repository).findByUserUsername("unknown");
        verifyNoMoreInteractions(transactionRepository);
    }

    @Test
    void testTransferThrowsExceptionWhenDestinationWalletNotFound() {
        String fromUser = "alice";

        Wallet srcWallet = new Wallet();
        srcWallet.setId(1L);
        srcWallet.setUser(new User());
        srcWallet.setBalance(100.0);

        when(repository.findByUserUsername(fromUser)).thenReturn(Optional.of(srcWallet));
        when(repository.findByUserUsername("unknown")).thenReturn(Optional.empty());

        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> service.transfer(fromUser, "unknown", 10.0));
        assertEquals("record not found", exception.getMessage());

        verify(repository).findByUserUsername(fromUser);
        verify(repository).findByUserUsername("unknown");
        verifyNoMoreInteractions(transactionRepository);
    }

    @Test
    void testTransferThrowsExceptionWhenInsufficientFunds() {
        String fromUser = "alice";
        String toUser = "bob";

        Wallet srcWallet = new Wallet();
        srcWallet.setId(1L);
        srcWallet.setUser(new User());
        srcWallet.setBalance(30.0);

        Wallet dstWallet = new Wallet();
        dstWallet.setId(2L);
        dstWallet.setUser(new User());
        dstWallet.setBalance(20.0);

        when(repository.findByUserUsername(fromUser)).thenReturn(Optional.of(srcWallet));
        when(repository.findByUserUsername(toUser)).thenReturn(Optional.of(dstWallet));

        InsufficientBalanceException exception = assertThrows(InsufficientBalanceException.class, () -> service.transfer(fromUser, toUser, 50.0));
        assertEquals("Insufficient balance", exception.getMessage());

        verify(repository).findByUserUsername(fromUser);
        verify(repository).findByUserUsername(toUser);
        verifyNoMoreInteractions(transactionRepository);
    }
}
