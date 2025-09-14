CREATE TABLE venues (
    venue_Id SERIAL PRIMARY KEY ,
    venue_name VARCHAR(100) NOT NULL ,
    venue_location VARCHAR(100) NOT NULL
);
ALTER TABLE venues ALTER COLUMN venue_name set NOT NULL ;
ALTER TABLE venues ALTER COLUMN venue_location set NOT NULL ;

CREATE TABLE attendee (
    attendee_id SERIAL PRIMARY KEY ,
    attendee_name VARCHAR(100) NOT NULL ,
    attendee_email VARCHAR(100) NOT NULL
);
CREATE TABLE event (
    event_id SERIAL PRIMARY KEY ,
    event_name VARCHAR(100) NOT NULL ,
    event_data DATE  NOT NULL ,
    venue_id INTEGER ,
    FOREIGN KEY (venue_id) REFERENCES venues (venue_Id)
);
ALTER TABLE event DROP CONSTRAINT IF EXISTS venue_id;

ALTER TABLE event
    ADD CONSTRAINT fk_venue_id
        FOREIGN KEY (venue_id)
            REFERENCES venues (venue_id)
            ON DELETE CASCADE
            ON UPDATE CASCADE;
CREATE TABLE event_attendee (
    evernt_attende_id SERIAL PRIMARY KEY ,
    event_id INTEGER NOT NULL ,
    attendee_id INTEGER NOT NULL,
    CONSTRAINT fk_event FOREIGN KEY (event_id) REFERENCES event (event_id) ON DELETE CASCADE  ON UPDATE  CASCADE ,
    CONSTRAINT fk_attendee FOREIGN KEY (attendee_id) REFERENCES  attendee(attendee_id) ON DELETE CASCADE ON UPDATE CASCADE
)
