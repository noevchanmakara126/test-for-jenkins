package org.example.homework_003.model.respone;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiRespone <T> {
    private String message ;
    private T payload;
    private LocalDateTime timestamp;
    private HttpStatus status ;
    public ApiRespone (String message,HttpStatus status , LocalDateTime date){
        this.message=message;
        this.status=status;
        this.timestamp=date;
    }
}
