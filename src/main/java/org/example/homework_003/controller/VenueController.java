package org.example.homework_003.controller;

import jakarta.validation.Valid;
import org.example.homework_003.model.entity.Venue;
import org.example.homework_003.model.request.VenueRequest;
import org.example.homework_003.model.respone.ApiRespone;
import org.example.homework_003.service.VenueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/venues")
public class VenueController {
    private final VenueService venueService ;
    public VenueController(VenueService venueService ) {
        this.venueService = venueService;
    }
    @GetMapping
    public ResponseEntity<ApiRespone<List<Venue>>>  getAllVenue (@RequestParam(defaultValue = "1")Integer page,@RequestParam(defaultValue = "5")Integer size){
        return venueService.getAllVenue(page,size);
    }
    @GetMapping("/{venue-id}")
    public ResponseEntity<?>  getVenueById(@PathVariable("venue-id")Long id ){
        return venueService.getVenueById(id);
    }
    @PostMapping
    public ResponseEntity<ApiRespone<Venue>>  saveVenue(@Valid @RequestBody VenueRequest request){
        return venueService.saveVenue(request);
    }
    @PutMapping("{venue-id}")
    public ResponseEntity<?> updateVenueById (@Valid @RequestBody  VenueRequest request,@PathVariable("venue-id") Long id){
        return venueService.updateVenueById(request,id);
    }
    @DeleteMapping("{venue-id}")
    public ResponseEntity<?> deleteVenueById(@PathVariable("venue-id") Long id ){
        return venueService.deleteVenueById(id);
    }
}
