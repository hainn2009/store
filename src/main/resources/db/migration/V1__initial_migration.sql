----------------------
-- Create tables
----------------------
CREATE TABLE IF NOT EXISTS public.users
(
	id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    email character varying(255) NOT NULL UNIQUE,
    role character varying(20) NOT NULL DEFAULT 'USER'
);

CREATE TABLE IF NOT EXISTS public.addresses
(
	id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    street character varying(255) NOT NULL,
    city character varying(255) NOT NULL,
    state character varying(255) NOT NULL,
    zip character varying(255) NOT NULL,
    user_id bigint NOT NULL,
    CONSTRAINT addresses_users_id_fk FOREIGN KEY (user_id)
        REFERENCES public.users (id)
);

CREATE TABLE IF NOT EXISTS public.profiles
(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    phone_number character varying(15),
    date_of_birth date,
    loyalty_points integer DEFAULT 0,
    bio text,
    CONSTRAINT profile_user_id FOREIGN KEY (id)
        REFERENCES public.users (id)
);

CREATE TABLE IF NOT EXISTS public.categories
(
    id SMALLINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name character varying(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS public.products
(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name character varying(255) NOT NULL,
    price decimal(10,2) NOT NULL,
    description TEXT NOT NULL,
    category_id smallint NOT NULL,
    CONSTRAINT products_categories_id_fk FOREIGN KEY (category_id)
        REFERENCES public.categories (id)
);

CREATE TABLE IF NOT EXISTS public.wishlist
(
    product_id bigint NOT NULL,
    user_id bigint NOT NULL,
	CONSTRAINT wishlist_pkey PRIMARY KEY (product_id, user_id),
    CONSTRAINT wishlist_product_id_fk FOREIGN KEY (product_id)
        REFERENCES public.products (id) MATCH SIMPLE
        ON DELETE CASCADE,
    CONSTRAINT wishlists_user_id_fk FOREIGN KEY (user_id)
        REFERENCES public.users (id)
);

-- Need this extension to call generate UUID function
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE IF NOT EXISTS public.carts
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    date_created DATE NOT NULL DEFAULT CURRENT_DATE
);

CREATE TABLE IF NOT EXISTS public.cart_items
(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    cart_id UUID NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INTEGER DEFAULT 1 NOT NULL,
    CONSTRAINT cart_items_cart_product_unique UNIQUE (cart_id, product_id),
    CONSTRAINT cart_items_cart_id_fk FOREIGN KEY (cart_id)
        REFERENCES public.carts (id) ON DELETE CASCADE,
    CONSTRAINT cart_items_product_id_fk FOREIGN KEY (product_id)
        REFERENCES public.products (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS public.orders
(
    id bigint GENERATED ALWAYS AS IDENTITY NOT NULL,
    customer_id bigint NOT NULL,
    status character varying(20) NOT NULL,
    created_at timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    total_price decimal (10,2) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT orders_users_fk FOREIGN KEY (customer_id)
        REFERENCES public.users (id)
);

CREATE TABLE IF NOT EXISTS public.order_items
(
    id bigint GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    order_id bigint NOT NULL,
    product_id bigint NOT NULL,
    unit_price decimal (10,2) NOT NULL,
    quantity integer NOT NULL,
    total_price decimal (10,2) NOT NULL,
    CONSTRAINT order_items_orders_fk FOREIGN KEY (order_id)
        REFERENCES public.orders (id),
    CONSTRAINT order_items_products_fk FOREIGN KEY (product_id)
        REFERENCES public.products (id)
);
