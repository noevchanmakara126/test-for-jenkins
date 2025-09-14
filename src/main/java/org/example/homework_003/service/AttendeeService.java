package org.example.homework_003.service;

import jakarta.validation.Valid;
import org.example.homework_003.model.entity.Attendee;
import org.example.homework_003.model.request.AttendeeRequest;
import org.example.homework_003.model.respone.ApiRespone;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AttendeeService {

    ResponseEntity<ApiRespone<List<Attendee>>> getAllAttendee(Integer page, Integer size);

    ResponseEntity<?> getAttendeeById(Long id);

    ResponseEntity<?> updateAttendeeById( AttendeeRequest request, Long id);

    ResponseEntity<ApiRespone<Attendee>> postAttendee(@Valid AttendeeRequest request);

    ResponseEntity<?> deleteAttendeeById(Long id);
}
