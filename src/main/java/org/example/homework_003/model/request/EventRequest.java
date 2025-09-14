package org.example.homework_003.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EventRequest {

    @NotNull
    @NotBlank(message = "Event name is required")
    private String eventName;
    @NotNull(message = "Event date is required")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Future(message = "Date must in future date")
    private LocalDate eventDate;
    @NotNull(message = "Venue ID is required")
    private Long venueId;
    @NotNull(message = "Venue ID is required")
    private List<Long> attendees;
}
