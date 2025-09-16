package org.example.homework_003.controller;

import jakarta.validation.Valid;
import org.example.homework_003.model.entity.Event;
import org.example.homework_003.model.request.EventRequest;
import org.example.homework_003.model.respone.ApiRespone;
import org.example.homework_003.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
public class EventController {
    private final EventService eventService;
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }
    @GetMapping
    public ResponseEntity<ApiRespone<List<Event>>> getAllEvents(@RequestParam(defaultValue = "1") Integer page , @RequestParam(defaultValue = "5")Integer size){
        return eventService.getAllEvents(page,size);
    }
    @GetMapping("/{event-id}")
    public ResponseEntity<?> getEventbyId(@PathVariable("event-id") Long id){
        return eventService.getEventbyId(id);
    }
    @PostMapping
    public ResponseEntity<ApiRespone<Event>> postEvent(@Valid @RequestBody EventRequest request){
        return eventService.postEvent(request);
    }
    @PutMapping("/{event-id}")
    public ResponseEntity<?> updateEventById(@Valid @RequestBody EventRequest request , @PathVariable("event-id") Long id){
        return eventService.updateEventById(request,id);
    }
    @DeleteMapping("/{event-id}")
    public ResponseEntity<?> deleteEventById(@PathVariable("event-id")Long id){
        return eventService.deleteEventById(id);
    }
//    @GetMapping("/{test}")
//    public ResponseEntity<ApiRespone<List<Event>>> getHello(@RequestParam(defaultValue = "1") Integer page , @RequestParam(defaultValue = "5")Integer size){
//        return eventService.getAllEvents(page,size);
//    }
}
