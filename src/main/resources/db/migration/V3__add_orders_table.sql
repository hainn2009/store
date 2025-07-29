CREATE TABLE public.orders
(
    id bigint GENERATED ALWAYS AS IDENTITY NOT NULL,
    customer_id bigint NOT NULL,
    status character varying(20) NOT NULL,
    created_at timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    total_price decimal (10,2) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT orders_users_fk FOREIGN KEY (customer_id)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
);

ALTER TABLE IF EXISTS public.orders
    OWNER to postgres;

CREATE TABLE public.orders_items
(
    id bigint GENERATED ALWAYS AS IDENTITY NOT NULL PRIMARY KEY,
    order_id bigint NOT NULL,
    product_id bigint NOT NULL,
    unit_price decimal (10,2) NOT NULL,
    quantity integer NOT NULL,
    total_price decimal (10,2) NOT NULL,
    CONSTRAINT order_items_orders_fk FOREIGN KEY (order_id)
        REFERENCES public.orders (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT order_items_products_fk FOREIGN KEY (product_id)
        REFERENCES public.products (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);