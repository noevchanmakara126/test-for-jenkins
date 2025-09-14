package org.example.homework_003.model.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    private Long eventId;
    private String eventName;
    private String eventDate;
    private Venue venue;
    private List<Attendee> attendees;
}
