ALTER TABLE IF EXISTS public.users
    ADD COLUMN role character varying(20) NOT NULL DEFAULT USER;