package org.example.homework_003.repository;

import jakarta.validation.Valid;
import org.apache.ibatis.annotations.*;
import org.example.homework_003.model.entity.Attendee;
import org.example.homework_003.model.request.AttendeeRequest;
import org.example.homework_003.model.respone.ApiRespone;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface AttendeeRepository {
    @Results(id = "attendeeMapper",value = {
            @Result(property = "attendeeId",column = "attendee_id"),
            @Result(property = "attendeeName",column = "attendee_name"),
            @Result(property = "email",column = "attendee_email"),

    })
    @Select("""
       SELECT * FROM attendee 
       OFFSET #{offset} LIMIT #{size};
       """)
    List<Attendee> getAllAttendee(int offset, Integer size);

    @ResultMap("attendeeMapper")
    @Select("""
       SELECT * FROM attendee
       WHERE attendee_id = #{id}
       """)
    Attendee getAttendeeById(Long id);


    @ResultMap("attendeeMapper")
    @Select("""
       INSERT INTO attendee VALUES (
                   default,
                   #{req.attendeeName},
                   #{req.email}
       )
      RETURNING * ;
   
       """)
   Attendee postAttendee( @Valid @Param("req")AttendeeRequest request);


    @ResultMap("attendeeMapper")
    @Select("""
       UPDATE  attendee SET 
                   attendee_name = #{req.attendeeName},
                   attendee_email = #{req.email}
                   WHERE attendee_id = #{id};
       
 
       """)
   Attendee updateAttendeeById(@Valid @Param("req") AttendeeRequest request, Long id);



    @ResultMap("attendeeMapper")
    @Select("""
        DELETE FROM attendee
         WHERE attendee_id = #{id};
        """)
    Attendee deleteAttendeeById(Long id);
}
