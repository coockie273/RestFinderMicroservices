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

CREATE DATABASE restaurants;

\c restaurants;

CREATE USER _dbusername_ WITH ENCRYPTED PASSWORD _dbpassword_ SUPERUSER;

CREATE TABLE public.restaurant (
    id integer NOT NULL,
    name text,
    description text
);


ALTER TABLE public.restaurant OWNER TO postgres;

--
-- Name: restaurant_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.restaurant_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.restaurant_id_seq OWNER TO postgres;

--
-- Name: restaurant_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.restaurant_id_seq OWNED BY public.restaurant.id;


--
-- Name: restaurant_keyword; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.restaurant_keyword (
    restaurant_id integer NOT NULL,
    keyword text NOT NULL
);


ALTER TABLE public.restaurant_keyword OWNER TO postgres;

--
-- Name: restaurant id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.restaurant ALTER COLUMN id SET DEFAULT nextval('public.restaurant_id_seq'::regclass);


--
-- Data for Name: restaurant; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.restaurant (id, name, description) FROM stdin;
1	Thai Orchid Garden	The best Thai restaurant
2	The Rustic Table	We are so good
\.


--
-- Data for Name: restaurant_keyword; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.restaurant_keyword (restaurant_id, keyword) FROM stdin;
1	meal
1	thai
2	Vegan
2	no gluten
\.


--
-- Name: restaurant_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.restaurant_id_seq', 2, true);


--
-- Name: restaurant_keyword restaurant_keyword_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.restaurant_keyword
    ADD CONSTRAINT restaurant_keyword_pkey PRIMARY KEY (restaurant_id, keyword);


--
-- Name: restaurant restaurant_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.restaurant
    ADD CONSTRAINT restaurant_pkey PRIMARY KEY (id);


--
-- Name: restaurant_keyword restaurant_keyword_restaurant_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.restaurant_keyword
    ADD CONSTRAINT restaurant_keyword_restaurant_id_fkey FOREIGN KEY (restaurant_id) REFERENCES public.restaurant(id);


--
-- PostgreSQL database dump complete
--

