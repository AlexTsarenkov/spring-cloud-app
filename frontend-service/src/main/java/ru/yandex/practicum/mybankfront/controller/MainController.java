package ru.yandex.practicum.mybankfront.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import ru.yandex.practicum.mybankfront.dto.Account;
import ru.yandex.practicum.mybankfront.dto.AccountResponse;
import ru.yandex.practicum.mybankfront.dto.CashActionDto;
import ru.yandex.practicum.mybankfront.dto.TransferActionDto;
import ru.yandex.practicum.mybankfront.dto.UserData;
import ru.yandex.practicum.mybankfront.dto.UserDataUpdateDto;
import ru.yandex.practicum.mybankfront.dto.UserToTransfer;
import ru.yandex.practicum.mybankfront.enumeration.CashAction;
import ru.yandex.practicum.mybankfront.service.SecurityService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Контроллер main.html.
 * <p>
 * Используемая модель для main.html:
 * model.addAttribute("name", name);
 * model.addAttribute("birthdate", birthdate.format(DateTimeFormatter.ISO_DATE));
 * model.addAttribute("sum", sum);
 * model.addAttribute("accounts", accounts);
 * model.addAttribute("errors", errors);
 * model.addAttribute("info", info);
 * <p>
 * Поля модели:
 * name - Фамилия Имя текущего пользователя, String (обязательное)
 * birthdate - дата рождения текущего пользователя, String в формате 'YYYY-MM-DD' (обязательное)
 * sum - сумма на счету текущего пользователя, Integer (обязательное)
 * accounts - список аккаунтов, которым можно перевести деньги, List<AccountDto> (обязательное)
 * errors - список ошибок после выполнения действий, List<String> (не обязательное)
 * info - строка успешности после выполнения действия, String (не обязательное)
 * <p>
 * С примерами использования можно ознакомиться в тестовом классе заглушке AccountStub
 */
@Controller
@AllArgsConstructor
public class MainController {
    private final SecurityService securityService;

    /**
     * GET /.
     * Редирект на GET /account
     */
    @GetMapping
    public String index() {
        return "redirect:/account";
    }

    /**
     * GET /account.
     * Что нужно сделать:
     * 1. Сходить в сервис accounts через Gateway API для получения данных аккаунта по REST
     * 2. Заполнить модель main.html полученными из ответа данными
     * 3. Текущего пользователя можно получить из контекста Security
     */
    @GetMapping("/account")
    public ModelAndView getAccount(Authentication authentication) {
        AccountResponse response = securityService.getWithUserToken(
                "/account-service/accounts",
                AccountResponse.class,
                authentication
        );

        UserData user = response.getUserData();
        String name = "";
        String birthdate = "";
        if (user != null) {
            name = user.getNameSurename() != null ? user.getNameSurename() : "";
            if (user.getBirthdate() != null) {
                birthdate = user.getBirthdate().format(DateTimeFormatter.ISO_LOCAL_DATE);
            }
        }
        int sum = response.getAccount() != null && response.getAccount().getBalance() != null
                ? (int) Math.round(response.getAccount().getBalance())
                : 0;
        List<UserToTransfer> usersToTransfer = response.getUsersToTransfer().stream()
                .map(ud -> new UserToTransfer(ud.getUsername(), ud.getNameSurename()))
                .toList();

        ModelAndView mav = new ModelAndView("main");
        mav.addObject("name", name);
        mav.addObject("birthdate", birthdate);
        mav.addObject("sum", sum);
        mav.addObject("accounts", usersToTransfer);
        return mav;
    }

    /**
     * POST /account.
     * Что нужно сделать:
     * 1. Сходить в сервис accounts через Gateway API для изменения данных текущего пользователя по REST
     * 2. Заполнить модель main.html полученными из ответа данными
     * 3. Текущего пользователя можно получить из контекста Security
     * <p>
     * Изменяемые данные:
     * 1. name - Фамилия Имя
     * 2. birthdate - дата рождения в формате YYYY-DD-MM
     */
    @PostMapping("/account")
    public RedirectView editAccount(
            Model model,
            @RequestParam("name") String name,
            @RequestParam("birthdate") LocalDate birthdate,
            Authentication authentication
    ) {
        UserDataUpdateDto userToUpdate = UserDataUpdateDto.builder()
                .username(authentication.getName())
                .nameSurename(name)
                .birthdate(birthdate)
                .build();

        securityService.postWithUserToken(
                "/account-service/accounts",
                UserData.class,
                authentication,
                userToUpdate
        );
        return new RedirectView("/account");
    }

    /**
     * POST /cash.
     * Что нужно сделать:
     * 1. Сходить в сервис cash через Gateway API для снятия/пополнения счета текущего аккаунта по REST
     * 2. Заполнить модель main.html полученными из ответа данными
     * 3. Текущего пользователя можно получить из контекста Security
     * <p>
     * Параметры:
     * 1. value - сумма списания
     * 2. action - GET (снять), PUT (пополнить)
     */
    @PostMapping("/cash")
    public RedirectView editCash(
            Authentication authentication,
            @RequestParam("value") Double value,
            @RequestParam("action") CashAction action
    ) {
        AccountResponse response = securityService.getWithUserToken(
                "/account-service/accounts",
                AccountResponse.class,
                authentication
        );
        Account account = response.getAccount();
        if (account.getBalance() < value && account.equals(CashAction.GET)) {

        }
        CashActionDto body = CashActionDto.builder()
                .accountNumber(account.getAccountNumber())
                .action(action)
                .balance(value)
                .build();

        securityService.postWithUserToken(
                "/cash-service/cash",
                Void.class,
                authentication,
                body
        );

        return new RedirectView("/account");
    }

    /**
     * POST /transfer.
     * Что нужно сделать:
     * 1. Сходить в сервис accounts через Gateway API для перевода со счета текущего аккаунта на счет другого аккаунта по REST
     * 2. Заполнить модель main.html полученными из ответа данными
     * 3. Текущего пользователя можно получить из контекста Security
     * <p>
     * Параметры:
     * 1. value - сумма списания
     * 2. login - логин пользователя получателя
     */
    @PostMapping("/transfer")
    public RedirectView transfer(
            Authentication authentication,
            @RequestParam("value") Double value,
            @RequestParam("login") String login
    ) {
        AccountResponse currentAccount = securityService.getWithUserToken(
                "/account-service/accounts",
                AccountResponse.class,
                authentication
        );

        AccountResponse transferAccount = securityService.getWithUserToken(
                String.format("/account-service/accounts/%s", login),
                AccountResponse.class,
                authentication
        );

        TransferActionDto transferActionDto = TransferActionDto.builder()
                .fromAccountId(currentAccount.getAccount().getAccountNumber())
                .toAccountId(transferAccount.getAccount().getAccountNumber())
                .amount(value)
                .build();

        securityService.postWithUserToken(
                "/transfer-service/transfer",
                Void.class,
                authentication,
                transferActionDto);

        return new RedirectView("/account");
    }
}
