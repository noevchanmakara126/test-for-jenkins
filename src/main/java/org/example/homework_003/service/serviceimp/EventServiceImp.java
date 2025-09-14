package org.example.homework_003.service.serviceimp;

import jakarta.validation.Valid;
import org.example.homework_003.exception.NotFoundException;
import org.example.homework_003.model.entity.Event;
import org.example.homework_003.model.request.EventRequest;
import org.example.homework_003.model.respone.ApiRespone;
import org.example.homework_003.model.respone.MyErrorRespone;
import org.example.homework_003.repository.EventAttendeeRepository;
import org.example.homework_003.repository.EventRepository;
import org.example.homework_003.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventServiceImp implements EventService {
    private final EventRepository eventRepository;
    private final EventAttendeeRepository eventAttendeeRepository;

    public EventServiceImp(EventRepository eventRepository, EventAttendeeRepository eventAttendeeRepository) {
        this.eventRepository = eventRepository;
        this.eventAttendeeRepository = eventAttendeeRepository;
    }

    @Override
    public ResponseEntity<ApiRespone<List<Event>>> getAllEvents(Integer page,Integer size) {
            int offset = (page-1) * size ;
            List<Event> event = eventRepository.getAllEvents(offset,size);
            if (event.isEmpty()){
                throw new NotFoundException("Don't have Event Yet ");
            }
            ApiRespone <List<Event>> respone = new ApiRespone<>("Get Successfully",event, LocalDateTime.now(), HttpStatus.OK);
        return ResponseEntity.ok().body(respone);
    }

    @Override
    public ResponseEntity<?> getEventbyId(Long id) {
        Event event = eventRepository.getEventbyId(id);
        if (event == null){
            throw new NotFoundException("Event id "+id+" doesn't existed");
        }
        ApiRespone<?> respone = new ApiRespone<>("Get Event Id "+ id + " Successfully",event,LocalDateTime.now(),HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK).body(respone);
    }

    @Override
    public ResponseEntity<ApiRespone<Event>> postEvent(EventRequest request) {
        Event event = eventRepository.postEvent(request);
        for (Long attendeeId : request.getAttendees()){
            eventAttendeeRepository.saveEventAttendee(event.getEventId(), attendeeId);
        }
        ApiRespone<Event> respone = new ApiRespone<>("Post Successfully",event,LocalDateTime.now(),HttpStatus.CREATED);
        return ResponseEntity.status(HttpStatus.OK).body(respone);
    }

    @Override
    public ResponseEntity<?> updateEventById(EventRequest request, Long id) {
        getEventbyId(id);
        Event event= eventRepository.updateEventById(request,id);
        ApiRespone<?> respone = new ApiRespone<>("Update Event Id" +id +"Sucessfully",event,LocalDateTime.now(),HttpStatus.OK);
        return new ResponseEntity<>(respone,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> deleteEventById(Long id) {
         getEventbyId(id);
         Event event = eventRepository.deleteEventById(id);
         ApiRespone<?> respone = new ApiRespone<>("Delete Event id "+id+"Successcfully",HttpStatus.OK,LocalDateTime.now());
        return new ResponseEntity<>(respone,HttpStatus.OK);
    }
}
