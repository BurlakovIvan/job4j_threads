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
            return account != null && accounts.putIfAbsent(account.id(), account) == null;
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
            return account.isPresent() && accounts.remove(account.get().id()) != null;
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
            var accountToOpt = getById(toId);
            if (accountFromOpt.isEmpty() || accountToOpt.isEmpty()
                    || amount <= 0 || accountFromOpt.get().amount() < amount) {
                return false;
            }
            var accountFrom = accountFromOpt.get();
            var accountTo = accountToOpt.get();
            var rsl = update(new Account(accountFrom.id(),
                    (accountFrom.amount() - amount)));
            if (rsl) {
                rsl = update(new Account(accountTo.id(),
                        (accountTo.amount() + amount)));
            }
            return rsl;
        }
    }
}

