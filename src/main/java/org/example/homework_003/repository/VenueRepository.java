package org.example.homework_003.repository;

import org.apache.ibatis.annotations.*;
import org.example.homework_003.model.entity.Venue;
import org.example.homework_003.model.request.VenueRequest;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface VenueRepository {
    @Results(id = "venueMapper",value = {
            @Result(property = "venueId",column = "venue_Id"),
            @Result(property = "venueName",column = "venue_Name"),
            @Result(property = "location",column = "venue_location"),
    })
    @Select("""
        SELECT * FROM venues 
        OFFSET #{offset} LIMIT #{page};
    """)
    List<Venue> getAllVenue(int offset,Integer page );

    @ResultMap("venueMapper")
    @Select("""
       INSERT INTO venues VALUES (
           default,  #{req.venueName} ,  #{req.location}
       )
       RETURNING *;
       """)
    Venue saveVenue(@Param("req") VenueRequest request);

    @ResultMap("venueMapper")
    @Select("""
        SELECT * FROM venues WHERE venue_Id = #{id}
        """)
    Venue getVenueById(Long id);

    @ResultMap("venueMapper")
    @Select("""
    UPDATE venues SET venue_Name =#{req.venueName} ,venue_location =#{req.location}
    WHERE venue_Id = #{id};
    """)
    Venue updateVenueById(@Param("req") VenueRequest request, Long id);
    @ResultMap("venueMapper")
    @Select("""
      DELETE FROM venues WHERE venue_Id = #{id}
       """)
    Venue deleteVenueById(Long id);
}
