package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.EventService;
import ru.yandex.practicum.filmorate.storage.EventStorage;
import ru.yandex.practicum.filmorate.storage.impl.UserDbStorageImpl;


import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventStorage eventStorage;
    private final UserDbStorageImpl userDbStorage;

    @Override
    public void addEvent(Event event) {

        eventStorage.addEvent(event);
    }

    @Override
    public List<Event> getEventsByUserId(Long userId) {

        User user = userDbStorage.getUserById(userId);

        return eventStorage.getEventsByUserId(user.getId());
    }
}
