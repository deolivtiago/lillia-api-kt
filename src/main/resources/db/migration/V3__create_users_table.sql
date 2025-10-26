create table if not exists users (
    id uuid primary key default gen_random_uuid(),

    username text not null unique,
    password text not null,

    full_name text not null,

    avatar_url text default '',
    is_verified boolean default false,

    role_id text not null references roles (id) on delete restrict on update cascade,

    created_by text not null references users (username) on delete restrict on update cascade,
    updated_by text not null references users (username) on delete restrict on update cascade,

    updated_at timestamptz(3) default now(),
    created_at timestamptz(3) default now()
);
