create table if not exists outbox_events (
    id uuid primary key default gen_random_uuid(),
    event_key varchar(120) not null unique,
    event_type varchar(120) not null,
    aggregate_type varchar(80) not null,
    aggregate_id varchar(120),
    occurred_at timestamptz not null,
    payload text not null,
    status varchar(30) not null default 'PENDING',
    retry_count integer not null default 0,
    last_attempt_at timestamptz,
    published_at timestamptz,
    last_error text,
    created_by varchar(100) not null,
    created_date timestamptz not null,
    updated_by varchar(100) not null,
    updated_date timestamptz not null,
    is_active boolean not null default true,
    deleted boolean not null default false,
    deleted_by varchar(100),
    deleted_date timestamptz,
    version bigint not null default 0,
    constraint chk_outbox_events_status check (status in ('PENDING','PUBLISHED','FAILED')),
    constraint chk_outbox_events_retry_count check (retry_count >= 0)
);

create index if not exists idx_outbox_events_status_created on outbox_events(status, created_date);
create index if not exists idx_outbox_events_aggregate on outbox_events(aggregate_type, aggregate_id);
create index if not exists idx_outbox_events_occurred_at on outbox_events(occurred_at);

create table if not exists platform_runtime_config (
    id uuid primary key default gen_random_uuid(),
    config_key varchar(160) not null unique,
    config_value text not null,
    description text,
    secret boolean not null default false,
    created_by varchar(100) not null,
    created_date timestamptz not null,
    updated_by varchar(100) not null,
    updated_date timestamptz not null,
    is_active boolean not null default true,
    deleted boolean not null default false,
    deleted_by varchar(100),
    deleted_date timestamptz,
    version bigint not null default 0
);

create index if not exists idx_platform_runtime_config_active on platform_runtime_config(is_active, deleted);
