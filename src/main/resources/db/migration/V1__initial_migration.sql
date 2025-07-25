----------------------
-- Create tables
----------------------
CREATE TABLE IF NOT EXISTS public.users
(
	id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    password character varying(255) COLLATE pg_catalog."default" NOT NULL,
    email character varying(255) COLLATE pg_catalog."default" NOT NULL UNIQUE
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.users
    OWNER to postgres;

CREATE TABLE IF NOT EXISTS public.addresses
(
	id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    street character varying(255) COLLATE pg_catalog."default" NOT NULL,
    city character varying(255) COLLATE pg_catalog."default" NOT NULL,
    state character varying(255) COLLATE pg_catalog."default" NOT NULL,
    zip character varying(255) COLLATE pg_catalog."default" NOT NULL,
    user_id bigint NOT NULL,
    CONSTRAINT addresses_users_id_fk FOREIGN KEY (user_id)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.addresses
    OWNER to postgres;

CREATE TABLE IF NOT EXISTS public.profiles
(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    phone_number character varying(15) COLLATE pg_catalog."default",
    date_of_birth date,
    loyalty_points integer DEFAULT 0,
    bio text COLLATE pg_catalog."default",
    CONSTRAINT profile_user_id FOREIGN KEY (id)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.profiles
    OWNER to postgres;

CREATE TABLE IF NOT EXISTS public.categories
(
    id SMALLINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name character varying(255) COLLATE pg_catalog."default" NOT NULL
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.categories
    OWNER to postgres;

CREATE TABLE IF NOT EXISTS public.products
(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    price decimal(10,2) NOT NULL,
    description TEXT NOT NULL,
    category_id smallint NOT NULL,
    CONSTRAINT products_categories_id_fk FOREIGN KEY (category_id)
        REFERENCES public.categories (id) MATCH SIMPLE
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.products
    OWNER to postgres;

CREATE TABLE IF NOT EXISTS public.wishlist
(
    product_id bigint NOT NULL,
    user_id bigint NOT NULL,
	CONSTRAINT wishlist_pkey PRIMARY KEY (product_id, user_id),
    CONSTRAINT wishlist_product_id_fk FOREIGN KEY (product_id)
        REFERENCES public.products (id) MATCH SIMPLE
        ON DELETE CASCADE,
    CONSTRAINT wishlists_user_id_fk FOREIGN KEY (user_id)
        REFERENCES public.users (id) MATCH SIMPLE
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.wishlist
    OWNER to postgres;

-- Need this extension to call generate UUID function
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE IF NOT EXISTS public.carts
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    date_created DATE NOT NULL DEFAULT CURRENT_DATE
)
TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.carts
    OWNER TO postgres;

CREATE TABLE IF NOT EXISTS public.cart_items
(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    cart_id UUID NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INTEGER DEFAULT 1 NOT NULL,
    CONSTRAINT cart_items_cart_product_unique UNIQUE (cart_id, product_id),
    CONSTRAINT cart_items_cart_id_fk FOREIGN KEY (cart_id)
        REFERENCES public.carts (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    CONSTRAINT user_tags_product_id_fk FOREIGN KEY (product_id)
        REFERENCES public.products (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.cart_items
    OWNER to postgres;

----------------------
-- Create data
----------------------
