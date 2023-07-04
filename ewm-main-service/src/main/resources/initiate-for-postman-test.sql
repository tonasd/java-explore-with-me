INSERT INTO public.users (email,name) VALUES ('gena@yahoo.com','Gena Bookin');
INSERT INTO public.users (email,name) VALUES ('alex@yahoo.com','Alex Popov');
INSERT INTO public.users (email,name) VALUES ('vova@yahoo.com','Vova Vinocur');
INSERT INTO public.users (email,name) VALUES ('lev@yahoo.com','Lev Leschenko');

INSERT INTO public.categories (name) VALUES ('Noisy');


INSERT INTO public.events (title,annotation,category_id,description,created_on,event_date,initiator_id,paid,participant_limit,request_moderation,state,published_on,location_lat,location_lon)
	VALUES ('Radio party1','Let''s make some noise',1,'We are going to listen a white noise on my first radio','2023-07-01 22:33:23.996','2023-07-02 03:33:23.000',1,false,2,false,'PUBLISHED','2023-07-01 22:40:24.301',-39.02360153198242,-6.359300136566162);

INSERT INTO public.events (title,annotation,category_id,description,created_on,event_date,initiator_id,paid,participant_limit,request_moderation,state,published_on,location_lat,location_lon)
	VALUES ('Radio party2','I gonna sing the best songs ever',1,'We are going to listen a some hits of Lev Leschenko','2023-07-02 12:33:23.996','2023-07-02 15:33:23.000',4,false,3,false,'PUBLISHED','2023-07-01 22:40:24.301',-39.02360153198242,-6.359300136566162);

INSERT INTO public.events (title,annotation,category_id,description,created_on,event_date,initiator_id,paid,participant_limit,request_moderation,state,published_on,location_lat,location_lon)
	VALUES ('Radio party3','You gonna die from my jokes',1,'We are going to listen best comedy of Vova Vinocur','2023-07-02 12:33:23.997','2023-07-02 18:33:23.000',3,false,3,false,'PUBLISHED','2023-07-01 22:40:24.301',-39.02360153198242,-6.359300136566162);


INSERT INTO public.requests (event_id,requester_id,status,created)
	VALUES (1,2,'CONFIRMED','2023-07-01 22:45:25.384');
INSERT INTO public.requests (event_id,requester_id,status,created)
	VALUES (1,3,'REJECTED','2023-07-01 22:45:25.384');
INSERT INTO public.requests (event_id,requester_id,status,created)
	VALUES (2,1,'CONFIRMED','2023-07-01 22:45:25.384');
INSERT INTO public.requests (event_id,requester_id,status,created)
	VALUES (2,2,'CONFIRMED','2023-07-01 22:45:25.384');
INSERT INTO public.requests (event_id,requester_id,status,created)
	VALUES (2,3,'CONFIRMED','2023-07-01 22:45:25.384');
INSERT INTO public.requests (event_id,requester_id,status,created)
	VALUES (3,1,'CONFIRMED','2023-07-01 22:45:25.384');
INSERT INTO public.requests (event_id,requester_id,status,created)
	VALUES (3,2,'CONFIRMED','2023-07-01 22:45:25.384');
INSERT INTO public.requests (event_id,requester_id,status,created)
	VALUES (3,4,'CONFIRMED','2023-07-01 22:45:25.384');