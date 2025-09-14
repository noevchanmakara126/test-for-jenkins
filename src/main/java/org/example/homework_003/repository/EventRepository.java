package org.example.homework_003.repository;

import org.apache.ibatis.annotations.*;
import org.example.homework_003.model.entity.Event;
import org.example.homework_003.model.request.EventRequest;
import org.example.homework_003.model.respone.ApiRespone;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface EventRepository {
    @Results(id = "eventMapper",value = {
            @Result(property = "eventId",column = "event_id"),
            @Result(property = "eventName",column = "event_name"),
            @Result(property = "eventDate",column = "event_data"),
            @Result(property = "venue",column = "venue_id",one = @One(select = "org.example.homework_003.repository.VenueRepository.getVenueById")),
            @Result(property = "attendees",column = "event_id",many = @Many(select = "org.example.homework_003.repository.EventAttendeeRepository.getallAttendeeByEventId"))
    })
    @Select("""
    SELECT * FROM event
    OFFSET #{offset} LIMIT #{size};
    """)
    List<Event> getAllEvents(int offset , Integer size);
    @ResultMap("eventMapper")
    @Select("""
    SELECT * FROM event WHERE event_id =#{id};
    """)
    Event getEventbyId(Long id);
    @ResultMap("eventMapper")
    @Select("""
    INSERT INTO event VALUES (
                              DEFAULT,#{req.eventName},#{req.eventDate},#{req.venueId}
    )
    RETURNING *;
    """)
    Event postEvent(@Param("req") EventRequest request);

    @ResultMap("eventMapper")
    @Select("""
    UPDATE event SET event_name = #{req.eventName},event_data = #{req.eventDate} , venue_id =#{req.venueId}
    WHERE event_id = #{id}
    """)
    Event updateEventById(@Param("req") EventRequest request, Long id);

    @ResultMap("eventMapper")
    @Select("""
    DELETE FROM event WHERE event_id = #{id};
    """)
    Event deleteEventById(Long id);
}
