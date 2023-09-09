CREATE TABLE public.donut(
    id VARCHAR(36) PRIMARY KEY,
    flavour VARCHAR(100),
    diameter NUMERIC(15,2),
    quantity INTEGER,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);
