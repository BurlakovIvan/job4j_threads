package ru.job4j.cash;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.HashMap;
import java.util.Optional;

@ThreadSafe
public final class AccountStorage {
    @GuardedBy("accounts")
    private final HashMap<Integer, Account> accounts = new HashMap<>();

    public boolean add(Account account) {
        synchronized (accounts) {
            return account != null && accounts.putIfAbsent(account.id(), account) != null;
        }
    }

    public boolean update(Account account) {
        synchronized (accounts) {
            return account != null && accounts.replace(account.id(), account) != null;
        }
    }

    public boolean delete(int id) {
        synchronized (accounts) {
            var account = getById(id);
            return account.isPresent() && accounts.remove(account.get().id(), account.get());
        }
    }

    public Optional<Account> getById(int id) {
        synchronized (accounts) {
            return Optional.ofNullable(accounts.get(id));
        }
    }

    public boolean transfer(int fromId, int toId, int amount) {
        synchronized (accounts) {
            var accountFromOpt = getById(fromId);
            if (accountFromOpt.isEmpty()) {
                throw new IllegalArgumentException("Не существует счета с которого переводим");
            }
            var accountFrom = accountFromOpt.get();
            if (amount <= 0 || accountFrom.amount() < amount) {
                throw new IllegalArgumentException("Не корректно задана сумма перевода");
            }
            var accountToOpt = getById(toId);
            if (accountToOpt.isEmpty()) {
                throw new IllegalArgumentException("Не существует счета на который переводим");
            }
            var accountTo = accountToOpt.get();
            var rslFrom = update(new Account(accountFrom.id(),
                    (accountFrom.amount() - amount)));
            var rslTo = false;
            if (rslFrom) {
                rslTo = update(new Account(accountTo.id(),
                        (accountTo.amount() + amount)));
            }
            return rslTo;
        }
    }
}

