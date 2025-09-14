package org.example.homework_003.service.serviceimp;

import jakarta.validation.Valid;
import org.example.homework_003.exception.NotFoundException;
import org.example.homework_003.model.entity.Attendee;
import org.example.homework_003.model.request.AttendeeRequest;
import org.example.homework_003.model.respone.ApiRespone;
import org.example.homework_003.repository.AttendeeRepository;
import org.example.homework_003.service.AttendeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AttendeeServiceImp implements AttendeeService {
    private final AttendeeRepository repository;
    public AttendeeServiceImp(AttendeeRepository repository) {
        this.repository = repository;
    }

    @Override
    public ResponseEntity<ApiRespone<List<Attendee>>> getAllAttendee(Integer page , Integer size) {
        int offset = (page-1)*size;
        List<Attendee> attendees = repository.getAllAttendee(offset,size);
            if (attendees.isEmpty()){
                throw new NotFoundException("Don't have Attendee yet ");
            }
            ApiRespone <List<Attendee>> respone = new ApiRespone<>("Get All Attendee Succesfullly",attendees, LocalDateTime.now(), HttpStatus.OK);
        return ResponseEntity.ok().body(respone);
    }

    @Override
    public ResponseEntity<?> getAttendeeById(Long id) {
         Attendee attendee = repository.getAttendeeById(id);
         if (attendee == null){
             throw new NotFoundException("Attendee with id "+id+" doesn't Exit ");
         }
         ApiRespone <Attendee> respone = new ApiRespone<>("Get Attendee id "+id+" Successfully",attendee,LocalDateTime.now(),HttpStatus.OK);
        return new ResponseEntity<>(respone,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> updateAttendeeById(AttendeeRequest request, Long id) {
        getAttendeeById(id);
        Attendee attendee = repository.updateAttendeeById(request,id);
        ApiRespone<?> respone = new ApiRespone<>("Update Successfully",attendee,LocalDateTime.now(),HttpStatus.ACCEPTED);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(respone);
    }

    @Override
    public ResponseEntity<ApiRespone<Attendee>> postAttendee(AttendeeRequest request) {
        Attendee attendee = repository.postAttendee(request);
        ApiRespone<Attendee> respone = new ApiRespone<>("Post Succesfully",attendee,LocalDateTime.now(),HttpStatus.CREATED);
        return  ResponseEntity.status(HttpStatus.CREATED).body(respone);
    }

    @Override
    public ResponseEntity<?> deleteAttendeeById(Long id) {
        getAttendeeById(id);
        Attendee attendee = repository.deleteAttendeeById(id);
        ApiRespone <?> respone = new ApiRespone<>("Delete Successfully",HttpStatus.ACCEPTED,LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(respone);
    }

}
