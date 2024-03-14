package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
public class Event {

    @JsonProperty("timestamp")
    private Long addTimeStamp;
    private Long userId;
    private EventType eventType;
    private EventOperation operation;
    private Long eventId;
    private Long entityId;


    public Event(Long userId, EventType eventType, EventOperation operation, Long entityId) {

        this.addTimeStamp = new Timestamp(System.currentTimeMillis()).getTime();
        this.userId = userId;
        this.eventType = eventType;
        this.operation = operation;
        this.entityId = entityId;
    }
}
