INSERT INTO public.users (email,name) VALUES ('gena@yahoo.com','Gena Bookin');
INSERT INTO public.users (email,name) VALUES ('alex@yahoo.com','Alex Popov');

INSERT INTO public.categories (name) VALUES ('Noisy');


INSERT INTO public.events (title,annotation,category_id,description,created_on,event_date,initiator_id,paid,participant_limit,request_moderation,state,published_on,location_lat,location_lon)
	VALUES ('Radio party','Let''s make some noise',1,'We are going to listen a white noise on my first radio','2023-07-01 22:33:23.996','2023-07-02 03:33:23.000',1,false,2,false,'PUBLISHED','2023-07-01 22:40:24.301',-39.02360153198242,-6.359300136566162);

INSERT INTO public.requests (event_id,requester_id,status,created)
	VALUES (1,2,'CONFIRMED','2023-07-01 22:45:25.384');
