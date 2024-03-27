CREATE TABLE IF NOT EXISTS public.deposits
(
    id              SERIAL PRIMARY KEY,
    account_id      BIGINT REFERENCES public.accounts (id) ON DELETE CASCADE,
    amount          NUMERIC(19, 2) NOT NULL,
    interest_rate   NUMERIC(19, 2) NOT NULL,
    yearly_interest BOOLEAN        NOT NULL
);
