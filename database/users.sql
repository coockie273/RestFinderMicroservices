--
-- PostgreSQL database dump
--

-- Dumped from database version 16.2
-- Dumped by pg_dump version 16.2

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: restaurant; Type: TABLE; Schema: public; Owner: postgres
--

CREATE DATABASE users;

\c users;

CREATE USER users_user WITH ENCRYPTED PASSWORD 'users_user' SUPERUSER;

CREATE TABLE public.restaurant (
    id integer NOT NULL,
    description character varying(255),
    name character varying(255)
);


ALTER TABLE public.restaurant OWNER TO postgres;

--
-- Name: telegram_user; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.telegram_user (
    id bigint NOT NULL,
    username text,
    description character varying(255),
    name character varying(255)
);


ALTER TABLE public.telegram_user OWNER TO postgres;

--
-- Name: user_restaurant; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_restaurant (
    user_id bigint NOT NULL,
    restaurant text NOT NULL
);


ALTER TABLE public.user_restaurant OWNER TO postgres;

--
-- Data for Name: restaurant; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.restaurant (id, description, name) FROM stdin;
\.


--
-- Data for Name: telegram_user; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.telegram_user (id, username, description, name) FROM stdin;
725130385	fedotovStanislav	\N	\N
\.


--
-- Data for Name: user_restaurant; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.user_restaurant (user_id, restaurant) FROM stdin;
\.


--
-- Name: restaurant restaurant_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.restaurant
    ADD CONSTRAINT restaurant_pkey PRIMARY KEY (id);


--
-- Name: telegram_user telegram_user_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.telegram_user
    ADD CONSTRAINT telegram_user_pkey PRIMARY KEY (id);


--
-- Name: user_restaurant user_restaurant_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_restaurant
    ADD CONSTRAINT user_restaurant_pkey PRIMARY KEY (user_id, restaurant);


--
-- Name: user_restaurant user_restaurant_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_restaurant
    ADD CONSTRAINT user_restaurant_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.telegram_user(id);


--
-- PostgreSQL database dump complete
--


