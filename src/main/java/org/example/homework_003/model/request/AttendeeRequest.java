package org.example.homework_003.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AttendeeRequest {
    @NotNull
    @NotBlank(message = "Name is Required")
    @Size(min = 3,max = 30 , message = "Attendee's Name must be between 3 and 30 words")
    private String attendeeName;

    @NotNull
    @Size(min = 3,max = 30 , message = "Attendee's Email must be between 3 and 30 words")
    @Email(message = "Email is Required")
    private String email;

}
