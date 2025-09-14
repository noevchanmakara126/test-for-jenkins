package org.example.homework_003.controller;

import jakarta.validation.Valid;
import org.example.homework_003.model.entity.Attendee;
import org.example.homework_003.model.entity.Venue;
import org.example.homework_003.model.request.AttendeeRequest;
import org.example.homework_003.model.request.VenueRequest;
import org.example.homework_003.model.respone.ApiRespone;
import org.example.homework_003.service.AttendeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/attendees")
public class AttendeeController {

    private final AttendeeService attendeeService;
    public AttendeeController(AttendeeService attendeeService) {
        this.attendeeService = attendeeService;
    }

    @GetMapping
    public ResponseEntity<ApiRespone< List<Attendee>>> getAllAttendee(@RequestParam (defaultValue = "1") Integer page , @RequestParam(defaultValue = "5") Integer size){
        return attendeeService.getAllAttendee(page,size);
    }
    @GetMapping("/{attendee-id}")
    public ResponseEntity<?> getAttendeeById(@PathVariable("attendee-id") Long id ){
        return attendeeService.getAttendeeById(id);
    }
   @PostMapping
   public ResponseEntity<ApiRespone<Attendee>> postAttendee(@Valid @RequestBody AttendeeRequest request){
        return attendeeService.postAttendee(request);
   }
    @PutMapping("/{attendee-id}")
    public ResponseEntity<?> updateAttendeeById(@Valid @RequestBody AttendeeRequest request ,@PathVariable("attendee-id") Long id){
        return attendeeService.updateAttendeeById(request,id);
    }
    @DeleteMapping("/{attendee-id}")
    public ResponseEntity<?> deleteAttendeeById(@PathVariable("attendee-id") Long id){
        return attendeeService.deleteAttendeeById(id);
    }

}
