INSERT INTO venues VALUES (DEFAULT,'The Primier','Phnom Penh');
INSERT INTO venues VALUES (DEFAULT,'The Koh Pich','Phnom Penh');

INSERT INTO attendee VALUES (DEFAULT,'Makara','makara@gmail.com');
SELECT * FROM attendee;
INSERT INTO event VALUES (DEFAULT,'រៀបមង្គលការ','12-01-2025',1);
INSERT INTO event_attendee VALUES (DEFAULT,2,1);

SELECT * FROM event_attendee et INNER JOIN attendee a ON et.attendee_id = a.attendee_id WHERE event_id = 2;

