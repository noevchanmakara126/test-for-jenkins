package org.example.homework_003.service.serviceimp;

import jakarta.validation.Valid;
import org.example.homework_003.exception.NotFoundException;
import org.example.homework_003.model.entity.Venue;
import org.example.homework_003.model.request.VenueRequest;
import org.example.homework_003.model.respone.ApiRespone;
import org.example.homework_003.model.respone.MyErrorRespone;
import org.example.homework_003.repository.VenueRepository;
import org.example.homework_003.service.VenueService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VenueServiceImp implements VenueService {
    private final VenueRepository venueRepository;
    public VenueServiceImp(VenueRepository venueRepository) {
        this.venueRepository = venueRepository;
    }

    @Override
    public ResponseEntity<ApiRespone<List<Venue>>> getAllVenue(Integer page , Integer size) {
            int offset = (page -1 )*size;
            List<Venue> venues = venueRepository.getAllVenue(offset,size);
            if (venues.isEmpty()){
                throw new NotFoundException("Venue Doesn't Existed");
            }
            ApiRespone  <List <Venue>> respone = new ApiRespone <>("Get Venue Successfully",venues, LocalDateTime.now(), HttpStatus.OK );
        return ResponseEntity.ok().body(respone);
    }

    @Override
    public ResponseEntity<ApiRespone<Venue>> saveVenue(@Valid VenueRequest request) {
        Venue venue = venueRepository.saveVenue(request);
        ApiRespone<Venue> response = new ApiRespone<>("Post New Venue Successfully", HttpStatus.CREATED, LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<?> getVenueById(Long id) {
        Venue venue = venueRepository.getVenueById(id);
            if (venue == null){
                throw new NotFoundException("Venue with Id : "+id+" Doesn't exited");

            }
        ApiRespone <Venue> respone = new ApiRespone<>("Get Venue with id  " + id +" Successfully",venue,LocalDateTime.now(),HttpStatus.OK);
        return new  ResponseEntity<>(venue,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> updateVenueById(VenueRequest request, Long id) {
        getVenueById(id);
        Venue venue = venueRepository.updateVenueById(request,id);
        ApiRespone<?> respone = new ApiRespone<>("Update Successfully",venue,LocalDateTime.now(),HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK).body(respone);
    }

    @Override
    public ResponseEntity<?> deleteVenueById(Long id) {
        getVenueById(id);
        Venue venue = venueRepository.deleteVenueById(id);
        ApiRespone<?> respone = new ApiRespone<>("Delete Successfully",venue,LocalDateTime.now(),HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(respone);
    }
}
