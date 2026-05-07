package ru.yandex.practicum.accounts.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.accounts.exception.UserNotFoundException;
import ru.yandex.practicum.accounts.model.UserData;
import ru.yandex.practicum.accounts.repository.UserDataRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class UserDataService {
    private final UserDataRepository userDataRepository;

    public UserData getUserDataByUsername(String username) {
        return userDataRepository.findByUsername(username)
                .orElseGet(() -> createNewUser(username));
    }

    public UserData getUserDataOrNotFound(String username) {
        return userDataRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    public UserData createNewUser(String userName) {
        UserData userData = UserData.builder()
                .username(userName)
                .nameSurename("John Doe")
                .birthdate(LocalDate.of(1990, 1, 1))
                .build();
        return userDataRepository.save(userData);
    }

    public UserData updateUserData(UserData userData) {
        if (userDataRepository.existsByUsername(userData.getUsername())) {
            return userDataRepository.save(userData);
        } else {
            throw new UserNotFoundException(userData.getUsername());
        }
    }

    public List<UserData> getUsersToTransfer(String currentUser) {
        return userDataRepository.findAll().stream()
                .filter(ud -> !ud.getUsername().equals(currentUser))
                .toList();
    }
}
