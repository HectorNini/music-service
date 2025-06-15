-- Create tables
CREATE TABLE IF NOT EXISTS public."Roles"
(
    role_id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    name character varying(50) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT "Roles_pkey" PRIMARY KEY (role_id),
    CONSTRAINT "Roles_name_key" UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS public."Users"
(
    user_id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    username character varying(50) COLLATE pg_catalog."default" NOT NULL,
    password_hash character varying(255) COLLATE pg_catalog."default" NOT NULL,
    email character varying(255) COLLATE pg_catalog."default" NOT NULL,
    full_name character varying(100) COLLATE pg_catalog."default",
    created_at timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_login timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    role_id integer NOT NULL,
    CONSTRAINT "Users_pkey" PRIMARY KEY (user_id),
    CONSTRAINT "Users_username_key" UNIQUE (username),
    CONSTRAINT "Users_email_key" UNIQUE (email),
    CONSTRAINT role_id FOREIGN KEY (role_id)
        REFERENCES public."Roles" (role_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
);

CREATE TABLE IF NOT EXISTS public."Tracks"
(
    track_id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    title character varying(100) COLLATE pg_catalog."default" NOT NULL,
    artist character varying(100) COLLATE pg_catalog."default" NOT NULL,
    duration integer NOT NULL,
    file_path character varying(255) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT tracks_pkey PRIMARY KEY (track_id),
    CONSTRAINT "Tracks_title_artist_key" UNIQUE (title, artist)
);

CREATE TABLE IF NOT EXISTS public."Playlists"
(
    playlist_id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    description text COLLATE pg_catalog."default",
    CONSTRAINT playlists_pkey PRIMARY KEY (playlist_id),
    CONSTRAINT "Playlists_name_key" UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS public."PlaylistTracks"
(
    playlist_id integer NOT NULL,
    track_id integer NOT NULL,
    "position" integer NOT NULL,
    CONSTRAINT playlisttracks_pkey PRIMARY KEY (playlist_id, track_id),
    CONSTRAINT playlisttracks_playlist_id_fkey FOREIGN KEY (playlist_id)
        REFERENCES public."Playlists" (playlist_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    CONSTRAINT playlisttracks_track_id_fkey FOREIGN KEY (track_id)
        REFERENCES public."Tracks" (track_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS public."Pricing"
(
    price_id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    track_id integer,
    playlist_id integer,
    price numeric(10, 2) NOT NULL,
    valid_from timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    valid_to timestamp without time zone DEFAULT '9999-12-31 00:00:00'::timestamp without time zone,
    CONSTRAINT pricing_pkey PRIMARY KEY (price_id),
    CONSTRAINT "Pricing_track_id_key" UNIQUE (track_id),
    CONSTRAINT "Pricing_playlist_id_key" UNIQUE (playlist_id),
    CONSTRAINT pricing_playlist_id_fkey FOREIGN KEY (playlist_id)
        REFERENCES public."Playlists" (playlist_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    CONSTRAINT pricing_track_id_fkey FOREIGN KEY (track_id)
        REFERENCES public."Tracks" (track_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS public."Licenses"
(
    license_id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    user_id integer NOT NULL,
    start_date date NOT NULL,
    end_date date NOT NULL,
    price_id integer,
    CONSTRAINT licenses_pkey PRIMARY KEY (license_id),
    CONSTRAINT licenses_price_id_fkey FOREIGN KEY (price_id)
        REFERENCES public."Pricing" (price_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID,
    CONSTRAINT licenses_user_id_fkey FOREIGN KEY (user_id)
        REFERENCES public."Users" (user_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS public."Payments"
(
    payment_id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
    license_id integer NOT NULL,
    amount numeric(10, 2) NOT NULL,
    payment_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    status character varying(20) COLLATE pg_catalog."default" NOT NULL,
    payment_method character varying(50) COLLATE pg_catalog."default" NOT NULL,
    transaction_id character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT payments_pkey PRIMARY KEY (payment_id),
    CONSTRAINT payments_license_id_fkey FOREIGN KEY (license_id)
        REFERENCES public."Licenses" (license_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    CONSTRAINT payments_transaction_id_key UNIQUE (transaction_id)
);

-- Insert initial data
-- Roles
INSERT INTO public."Roles" (name) VALUES 
('ADMIN'),
('USER')
ON CONFLICT (name) DO NOTHING;

-- Users (password_hash is 'admin' for admin and 'password' for users)
INSERT INTO public."Users" (username, password_hash, email, full_name, role_id) VALUES 
('admin', '$2a$10$d527gQJSqdSYTm2mu9DIOuj89V8ODIhunPUyG7zMlMpNPe02o4WTO', 'admin@example.com', 'Администратор', 1),
('user1', '$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW', 'user1@example.com', 'Иван Петров', 2),
('user2', '$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW', 'user2@example.com', 'Мария Иванова', 2)
ON CONFLICT (username) DO NOTHING;

-- Tracks
INSERT INTO public."Tracks" (title, artist, duration, file_path) VALUES 
('Летние вибрации', 'Артист 1', 180, '/tracks/summer_vibes.mp3'),
('Зимние мечты', 'Артист 2', 240, '/tracks/winter_dreams.mp3'),
('Осенние листья', 'Артист 3', 200, '/tracks/autumn_leaves.mp3'),
('Весенние цветы', 'Артист 4', 220, '/tracks/spring_flowers.mp3'),
('Полуночные звезды', 'Артист 5', 260, '/tracks/midnight_stars.mp3'),
('Океанские волны', 'Артист 6', 190, '/tracks/ocean_waves.mp3'),
('Горные вершины', 'Артист 7', 210, '/tracks/mountain_high.mp3'),
('Городские огни', 'Артист 8', 230, '/tracks/city_lights.mp3'),
('Лесные эхо', 'Артист 9', 250, '/tracks/forest_echoes.mp3'),
('Пустынный ветер', 'Артист 10', 270, '/tracks/desert_wind.mp3')
ON CONFLICT (title, artist) DO NOTHING;

-- Playlists
INSERT INTO public."Playlists" (name, description) VALUES 
('Летние хиты', 'Лучшие летние треки года'),
('Спокойные мелодии', 'Расслабляющая музыка для вашего дня'),
('Тренировочный микс', 'Энергичные треки для тренировки'),
('Музыка для учебы', 'Музыка, помогающая сосредоточиться')
ON CONFLICT (name) DO NOTHING;

-- PlaylistTracks
INSERT INTO public."PlaylistTracks" (playlist_id, track_id, position) VALUES 
(1, 1, 1), (1, 4, 2), (1, 7, 3),
(2, 2, 1), (2, 5, 2), (2, 8, 3),
(3, 3, 1), (3, 6, 2), (3, 9, 3),
(4, 4, 1), (4, 7, 2), (4, 10, 3)
ON CONFLICT (playlist_id, track_id) DO NOTHING;

-- Pricing
INSERT INTO public."Pricing" (track_id, price) VALUES 
(1, 1.99), (2, 1.99), (3, 1.99), (4, 1.99), (5, 1.99),
(6, 1.99), (7, 1.99), (8, 1.99), (9, 1.99), (10, 1.99)
ON CONFLICT (track_id) DO NOTHING;

INSERT INTO public."Pricing" (playlist_id, price) VALUES 
(1, 4.99), (2, 4.99), (3, 4.99), (4, 4.99)
ON CONFLICT (playlist_id) DO NOTHING; 