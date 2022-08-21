package ru.job4j.cash;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AccountStorageTest {

    @Test
    void whenAdd() {
        var storage = new AccountStorage();
        assertThat(storage.add(new Account(1, 100))).isTrue();
        assertThat(storage.add(new Account(1, 100))).isFalse();
        var firstAccount = storage.getById(1)
                .orElseThrow(() -> new IllegalStateException("Not found account by id = 1"));
        assertThat(firstAccount.amount()).isEqualTo(100);
    }

    @Test
    void whenUpdate() {
        var storage = new AccountStorage();
        assertThat(storage.add(new Account(1, 100))).isTrue();
        assertThat(storage.update(new Account(1, 200))).isTrue();
        var firstAccount = storage.getById(1)
                .orElseThrow(() -> new IllegalStateException("Not found account by id = 1"));
        assertThat(firstAccount.amount()).isEqualTo(200);
    }

    @Test
    void whenDelete() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 100));
        assertThat(storage.delete(1)).isTrue();
        assertThat(storage.delete(3)).isFalse();
        assertThat(storage.getById(1)).isEmpty();
    }

    @Test
    void whenTransfer() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 100));
        storage.add(new Account(2, 100));
        storage.transfer(1, 2, 100);
        var firstAccount = storage.getById(1)
                .orElseThrow(() -> new IllegalStateException("Not found account by id = 1"));
        var secondAccount = storage.getById(2)
                .orElseThrow(() -> new IllegalStateException("Not found account by id = 1"));
        assertThat(firstAccount.amount()).isEqualTo(0);
        assertThat(secondAccount.amount()).isEqualTo(200);
    }

    @Test
    void whenTransferWithWrongAccountFrom() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 100));
        storage.add(new Account(2, 100));
        assertThat(storage.transfer(3, 2, 100)).isFalse();
    }

    @Test
    void whenTransferWithWrongAccountTo() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 100));
        storage.add(new Account(2, 100));
        assertThat(storage.transfer(1, 3, 100)).isFalse();
    }

    @Test
    void whenTransferWithNegativeAmount() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 300));
        storage.add(new Account(2, 100));
        assertThat(storage.transfer(1, 2, -400)).isFalse();
    }

    @Test
    void whenTransferWithWrongAmount() {
        var storage = new AccountStorage();
        assertThat(storage.add(new Account(1, 300))).isTrue();
        assertThat(storage.add(new Account(2, 100))).isTrue();
        assertThat(storage.transfer(1, 2, 400)).isFalse();
    }
}