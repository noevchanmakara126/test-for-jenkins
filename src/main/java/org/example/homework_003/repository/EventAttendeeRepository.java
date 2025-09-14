package org.example.homework_003.repository;

import org.apache.ibatis.annotations.*;
import org.example.homework_003.model.entity.Attendee;

import java.util.List;


@Mapper
public interface EventAttendeeRepository {
    @Results(id ="event_attendeeMapper",value = {
            @Result(property = "attendeeId",column = "attendee_id"),
            @Result(property = "attendeeName",column = "attendee_name"),
            @Result(property = "email",column = "attendee_email"),
    })
    @Select("""
    SELECT * FROM event_attendee et INNER JOIN attendee a 
    ON et.attendee_id = a.attendee_id WHERE event_id = #{eventId};
    """)
    List<Attendee> getallAttendeeByEventId(Long eventId );

    @Insert("""
    INSERT INTO event_attendee VALUES (DEFAULT,#{eventId},#{attendeeId})
    """)
    void saveEventAttendee(Long eventId, Long attendeeId);
}
