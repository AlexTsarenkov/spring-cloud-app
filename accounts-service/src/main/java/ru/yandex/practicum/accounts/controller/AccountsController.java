package ru.yandex.practicum.accounts.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.accounts.model.Account;
import ru.yandex.practicum.accounts.model.AccountResponse;
import ru.yandex.practicum.accounts.model.UserData;
import ru.yandex.practicum.accounts.model.dto.CashActionDto;
import ru.yandex.practicum.accounts.model.dto.TransferActionDto;
import ru.yandex.practicum.accounts.model.dto.UserDataUpdateDto;
import ru.yandex.practicum.accounts.service.AccountService;
import ru.yandex.practicum.accounts.service.UserDataService;

import java.util.List;

@RestController
@AllArgsConstructor
public class AccountsController {
    private final AccountService accountService;
    private final UserDataService userDataService;

    @GetMapping(value = "/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
    public AccountResponse accounts(Authentication authentication) {
        String username = authentication.getName();
        UserData userData = userDataService.getUserDataByUsername(username);
        Account account = accountService.getAccountByUserId(userData.getId());
        List<UserData> userToTransfer = userDataService.getUsersToTransfer(username);

        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setAccount(account);
        accountResponse.setUserData(userData);
        accountResponse.setUsersToTransfer(userToTransfer);

        return accountResponse;
    }

    @GetMapping(value = "/accounts/{login}", produces = MediaType.APPLICATION_JSON_VALUE)
    public AccountResponse accounts(@PathVariable String login) {
        UserData userData = userDataService.getUserDataByUsername(login);
        Account account = accountService.getAccountByUserId(userData.getId());
        List<UserData> userToTransfer = userDataService.getUsersToTransfer(login);

        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setAccount(account);
        accountResponse.setUserData(userData);
        accountResponse.setUsersToTransfer(userToTransfer);

        return accountResponse;
    }

    @PostMapping(value = "/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserData updateUserData(@RequestBody UserDataUpdateDto userData) {
        UserData existingUserData = userDataService.getUserDataOrNotFound(userData.getUsername());
        existingUserData.setNameSurename(userData.getNameSurename());
        existingUserData.setBirthdate(userData.getBirthdate());
        return userDataService.updateUserData(existingUserData);
    }

    @PostMapping(value = "/accounts/cash")
    public void cashAccount(@RequestBody CashActionDto cashActionDto) {
        switch (cashActionDto.getAction()) {
            case GET ->
                    accountService.substractCashFromBalance(cashActionDto.getAccountNumber(), cashActionDto.getBalance());
            case PUT -> accountService.addCashToBalance(cashActionDto.getAccountNumber(), cashActionDto.getBalance());
        }
    }

    @PostMapping(value = "accounts/transfer")
    public void transferCash(@RequestBody TransferActionDto transferActionDto) {
        accountService.transferCash(transferActionDto.getFromAccountId(), transferActionDto.getToAccountId(),
                transferActionDto.getAmount());
    }
}
