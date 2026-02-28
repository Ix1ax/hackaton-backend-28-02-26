create table if not exists users (
    id bigserial primary key,
    name text,
    surname text,
    patronymic text,
    email text not null unique,
    password_hash text not null,
    role text not null default 'USER'
);

alter table users
    alter column role set default 'USER';

update users
set role = 'USER'
where role is null;

alter table users
    alter column role set not null;
