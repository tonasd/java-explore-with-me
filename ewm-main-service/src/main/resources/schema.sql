CREATE TABLE IF NOT EXISTS public.users (
	id bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY,
	email varchar(256) NOT NULL,
	name varchar(250) NOT NULL,
	CONSTRAINT users_pk PRIMARY KEY (id),
	CONSTRAINT uq_email UNIQUE (email)
);

-- Column comments

COMMENT ON COLUMN public.users.id IS 'Идентификатор';
COMMENT ON COLUMN public.users.email IS 'Почтовый адрес';
COMMENT ON COLUMN public.users.name IS 'Имя с фамилией';

CREATE TABLE IF NOT EXISTS public.categories (
	id int NOT NULL GENERATED BY DEFAULT AS IDENTITY,
	name varchar(50) NOT NULL,
	CONSTRAINT categories_pk PRIMARY KEY (id),
	CONSTRAINT uq_category_name UNIQUE (name)
);

-- Column comments

COMMENT ON COLUMN public.categories.id IS 'Идентификатор категории';
COMMENT ON COLUMN public.categories.name IS 'Наименование категории';

CREATE TABLE IF NOT EXISTS public.events (
	id bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY,
	title varchar(120) NOT NULL,
	annotation varchar(2000) NOT NULL,
	category_id int NOT NULL,
	description varchar(7000) NOT NULL,
	created_on timestamp without time zone NOT NULL,
	event_date timestamp without time zone NOT NULL,
	initiator_id bigint NOT NULL,
	paid boolean DEFAULT false,
	participant_limit int DEFAULT 0,
	request_moderation boolean DEFAULT true,
	state varchar(10) DEFAULT 'PENDING',
	published_on timestamp without time zone NULL,
	location_lat float8 NOT NULL,
	location_lon float8 NOT NULL,
	CONSTRAINT events_pk PRIMARY KEY (id),
	CONSTRAINT categories_events_fk FOREIGN KEY (category_id) REFERENCES public.categories(id) ON DELETE restrict,
	CONSTRAINT users_events_fk FOREIGN KEY (initiator_id) REFERENCES public.users(id) ON DELETE restrict

);

-- Column comments

COMMENT ON COLUMN public.events.id IS 'Идентификатор';
COMMENT ON COLUMN public.events.title IS 'Заголовок события';
COMMENT ON COLUMN public.events.annotation IS 'Краткое описание события';
COMMENT ON COLUMN public.events.category_id IS 'id категории к которой относится событие';
COMMENT ON COLUMN public.events.description IS 'Полное описание события';
COMMENT ON COLUMN public.events.created_on IS 'Дата и время создания события';
COMMENT ON COLUMN public.events.event_date IS 'Дата и время на которые намечено событие';
COMMENT ON COLUMN public.events.initiator_id IS 'id пользователя создавшего событие';
COMMENT ON COLUMN public.events.paid IS 'Нужно ли оплачивать участие';
COMMENT ON COLUMN public.events.participant_limit IS 'Ограничение на количество участников. Значение 0 - означает отсутствие ограничения';
COMMENT ON COLUMN public.events.request_moderation IS 'Нужна ли пре-модерация заявок на участие';
COMMENT ON COLUMN public.events.state IS 'Cостояние жизненного цикла события';
COMMENT ON COLUMN public.events.published_on IS 'Дата и время публикации события';
COMMENT ON COLUMN public.events.location_lat IS 'Широта места проведения события';
COMMENT ON COLUMN public.events.location_lon IS 'Долгота места проведения события';

CREATE TABLE IF NOT EXISTS public.requests (
	id bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY,
	event_id bigint NOT NULL,
	requester_id bigint NOT NULL,
	status varchar(10) NOT NULL DEFAULT 'PENDING',
	created timestamp without time zone NOT NULL,
	CONSTRAINT requests_pk PRIMARY KEY (id),
	CONSTRAINT events_requests_fk FOREIGN KEY (event_id) REFERENCES public.events(id) ON DELETE RESTRICT,
	CONSTRAINT users_requests_fk FOREIGN KEY (requester_id) REFERENCES public.users(id) ON DELETE RESTRICT,
	CONSTRAINT uq_requester_event UNIQUE (event_id,requester_id)
);

-- Column comments

COMMENT ON COLUMN public.requests.id IS 'Идентификатор заявки';
COMMENT ON COLUMN public.requests.event_id IS 'Идентификатор события';
COMMENT ON COLUMN public.requests.requester_id IS 'Идентификатор пользователя, отправившего заявку';
COMMENT ON COLUMN public.requests.status IS 'Статус заявки';
COMMENT ON COLUMN public.requests.created IS 'Дата и время создания заявки';

CREATE TABLE IF NOT EXISTS public.compilations (
	id bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY,
	pinned boolean NOT NULL DEFAULT false,
	title varchar(50) NOT NULL,
	CONSTRAINT compilations_pk PRIMARY KEY (id)
);

-- Column comments

COMMENT ON COLUMN public.compilations.id IS 'Идентификатор подборки';
COMMENT ON COLUMN public.compilations.pinned IS 'Закреплена ли подборка на главной странице сайта';
COMMENT ON COLUMN public.compilations.title IS 'Заголовок подборки';

CREATE table IF NOT EXISTS public.compilation_event (
	compilation_id bigint NOT NULL,
	event_id bigint NOT NULL,
	CONSTRAINT compilation_event_pk PRIMARY KEY (compilation_id,event_id),
	CONSTRAINT compilation_event_fk_compilation FOREIGN KEY (compilation_id) REFERENCES public.compilations(id),
	CONSTRAINT compilation_event_fk_event FOREIGN KEY (event_id) REFERENCES public.events(id)
);

-- Column comments

COMMENT ON COLUMN public.compilation_event.compilation_id IS 'Внешний ключ на идентификатор подборки';
COMMENT ON COLUMN public.compilation_event.event_id IS 'Внешний ключ на идентификатор события';