package org.example.homework_003.service;

import jakarta.validation.Valid;
import org.example.homework_003.model.entity.Event;
import org.example.homework_003.model.request.EventRequest;
import org.example.homework_003.model.respone.ApiRespone;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface EventService {
    ResponseEntity<ApiRespone<List<Event>>> getAllEvents(Integer page, Integer size);

    ResponseEntity<?> getEventbyId(Long id);

    ResponseEntity<ApiRespone<Event>> postEvent(@Valid EventRequest request);

    ResponseEntity<?> updateEventById(@Valid EventRequest request, Long id);

    ResponseEntity<?> deleteEventById(Long id);
}
