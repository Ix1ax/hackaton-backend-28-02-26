alter table users
    add column age int;

alter table users
    add column parent_full_name text;

alter table users
    add column parent_phone text;

alter table users
    add constraint users_age_range_check check (age is null or (age >= 0 and age <= 150));

alter table users
    add constraint users_parent_required_check check (
        age is null
        or age >= 14
        or (parent_full_name is not null and parent_phone is not null)
    );

