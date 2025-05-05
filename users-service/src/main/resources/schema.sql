/*drop table if exists users cascade;*/

CREATE TABLE IF NOT EXISTS users
(
    id             bigserial PRIMARY KEY,
    user_name      text unique not null,
    email          text unique not null,
    first_name     text,
    last_name      text,
    password       text        not null,
    phone          text,
    user_role      integer     default 0,
    created_at     timestamptz default now(),
    updated_at     timestamptz,
    image          bytea default null,
    last_login_date timestamptz
);

/*
CREATE OR REPLACE FUNCTION update_users_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_users_timestamp
BEFORE UPDATE ON users
FOR EACH ROW
EXECUTE FUNCTION update_users_timestamp();
*/