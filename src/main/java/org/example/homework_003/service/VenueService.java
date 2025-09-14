package org.example.homework_003.service;

import org.example.homework_003.model.entity.Venue;
import org.example.homework_003.model.request.VenueRequest;
import org.example.homework_003.model.respone.ApiRespone;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface VenueService {
    ResponseEntity<ApiRespone<List<Venue> >>getAllVenue(Integer page, Integer size);

     ResponseEntity<ApiRespone<Venue> >  saveVenue(VenueRequest request);

    ResponseEntity<?>getVenueById(Long id);

    ResponseEntity<?> updateVenueById(VenueRequest request, Long id);

    ResponseEntity<?> deleteVenueById(Long id);
}
