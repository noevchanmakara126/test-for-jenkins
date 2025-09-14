package org.example.homework_003.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Attendee {
    private Long attendeeId ;
    private String attendeeName;
    private String email;

}
