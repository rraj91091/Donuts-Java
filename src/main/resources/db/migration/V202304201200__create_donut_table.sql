CREATE TABLE public.donut(
    id uuid PRIMARY KEY,
    flavour VARCHAR(100),
    diameter NUMERIC(15,2),
    quantity INTEGER,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);
