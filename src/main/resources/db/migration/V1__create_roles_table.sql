create table if not exists roles (
    id text primary key,

    permissions text[] default '{}'::text[],

    created_by text default '',
    updated_by text default '',

    updated_at timestamptz(3) default now(),
    created_at timestamptz(3) default now()
);
