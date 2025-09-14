package org.example.homework_003.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class VenueRequest {
    @NotNull
    @NotBlank(message = "Name is Required")
    @Size(min = 3,max = 30 , message = "Venue's Name must be between 3 and 30 words")
    private String venueName;

    @NotNull
    @NotBlank(message = "Location is Required")
    @Size(min = 3,max = 30 , message = "Location must be between 3 and 30 words")
    private String location;
}
