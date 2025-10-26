create table if not exists tokens (
    id uuid primary key default gen_random_uuid(),

    is_revoked boolean default false,

    roles text[] default '{}'::text[],

    username text not null references users (username) on delete cascade on update cascade,

    created_by text not null references users (username) on delete restrict on update cascade,
    updated_by text not null references users (username) on delete restrict on update cascade,

    updated_at timestamptz(3) default now(),
    created_at timestamptz(3) default now()
);

--    expires_at timestamptz(0) default now() + '7 days'::interval,
