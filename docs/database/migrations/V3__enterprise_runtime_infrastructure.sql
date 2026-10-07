create extension if not exists pgcrypto;

create table if not exists workflow_definitions (
    id uuid primary key default gen_random_uuid(),
    code varchar(60) not null unique,
    name varchar(150) not null,
    description text,
    sort_order integer not null default 100,
    entity_type varchar(80) not null,
    default_workflow boolean not null default false,
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

create table if not exists workflow_steps (
    id uuid primary key default gen_random_uuid(),
    code varchar(60) not null,
    name varchar(150) not null,
    description text,
    sort_order integer not null default 100,
    workflow_definition_id uuid not null references workflow_definitions(id),
    initial_step boolean not null default false,
    terminal_step boolean not null default false,
    created_by varchar(100) not null,
    created_date timestamptz not null,
    updated_by varchar(100) not null,
    updated_date timestamptz not null,
    is_active boolean not null default true,
    deleted boolean not null default false,
    deleted_by varchar(100),
    deleted_date timestamptz,
    version bigint not null default 0,
    constraint uq_workflow_steps_definition_code unique (workflow_definition_id, code)
);

create table if not exists workflow_actions (
    id uuid primary key default gen_random_uuid(),
    code varchar(60) not null,
    name varchar(150) not null,
    description text,
    sort_order integer not null default 100,
    workflow_definition_id uuid not null references workflow_definitions(id),
    from_step_id uuid not null references workflow_steps(id),
    to_step_id uuid not null references workflow_steps(id),
    required_permission varchar(80),
    requires_comment boolean not null default false,
    created_by varchar(100) not null,
    created_date timestamptz not null,
    updated_by varchar(100) not null,
    updated_date timestamptz not null,
    is_active boolean not null default true,
    deleted boolean not null default false,
    deleted_by varchar(100),
    deleted_date timestamptz,
    version bigint not null default 0,
    constraint uq_workflow_actions_definition_code_from unique (workflow_definition_id, code, from_step_id),
    constraint chk_workflow_actions_no_self_transition check (from_step_id <> to_step_id)
);

create table if not exists approval_matrix (
    id uuid primary key default gen_random_uuid(),
    workflow_step_id uuid not null references workflow_steps(id),
    approver_role varchar(40) not null,
    approval_level integer not null,
    mandatory boolean not null default true,
    created_by varchar(100) not null,
    created_date timestamptz not null,
    updated_by varchar(100) not null,
    updated_date timestamptz not null,
    is_active boolean not null default true,
    deleted boolean not null default false,
    deleted_by varchar(100),
    deleted_date timestamptz,
    version bigint not null default 0,
    constraint chk_approval_matrix_level check (approval_level > 0)
);

create table if not exists approval_history (
    id uuid primary key default gen_random_uuid(),
    entity_type varchar(80) not null,
    entity_id uuid not null,
    workflow_action_id uuid not null references workflow_actions(id),
    decision varchar(30) not null,
    comments text,
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

create table if not exists business_rules (
    id uuid primary key default gen_random_uuid(),
    code varchar(60) not null unique,
    name varchar(150) not null,
    description text,
    sort_order integer not null default 100,
    context varchar(80) not null,
    field_name varchar(80) not null,
    operator varchar(30) not null,
    expected_value varchar(200) not null,
    failure_message varchar(250) not null,
    created_by varchar(100) not null,
    created_date timestamptz not null,
    updated_by varchar(100) not null,
    updated_date timestamptz not null,
    is_active boolean not null default true,
    deleted boolean not null default false,
    deleted_by varchar(100),
    deleted_date timestamptz,
    version bigint not null default 0,
    constraint chk_business_rules_operator check (operator in ('EQUALS','NOT_EQUALS','CONTAINS','IN','GREATER_THAN','LESS_THAN','GREATER_THAN_OR_EQUAL','LESS_THAN_OR_EQUAL'))
);

create table if not exists in_app_notifications (
    id uuid primary key default gen_random_uuid(),
    recipient_user_id uuid not null references app_users(id),
    title varchar(160) not null,
    message text not null,
    notification_type varchar(60) not null,
    entity_type varchar(80),
    entity_id varchar(80),
    read_at timestamptz,
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

create table if not exists notification_preferences (
    id uuid primary key default gen_random_uuid(),
    user_id uuid not null references app_users(id),
    notification_type varchar(60) not null,
    email_enabled boolean not null default true,
    in_app_enabled boolean not null default true,
    created_by varchar(100) not null,
    created_date timestamptz not null,
    updated_by varchar(100) not null,
    updated_date timestamptz not null,
    is_active boolean not null default true,
    deleted boolean not null default false,
    deleted_by varchar(100),
    deleted_date timestamptz,
    version bigint not null default 0,
    constraint uq_notification_preferences_user_type unique (user_id, notification_type)
);

create table if not exists background_job_definitions (
    id uuid primary key default gen_random_uuid(),
    code varchar(60) not null unique,
    name varchar(150) not null,
    description text,
    sort_order integer not null default 100,
    handler_name varchar(120) not null,
    cron_expression varchar(120) not null,
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

create table if not exists background_job_executions (
    id uuid primary key default gen_random_uuid(),
    job_definition_id uuid not null references background_job_definitions(id),
    status varchar(30) not null,
    started_at timestamptz,
    finished_at timestamptz,
    failure_reason text,
    created_by varchar(100) not null,
    created_date timestamptz not null,
    updated_by varchar(100) not null,
    updated_date timestamptz not null,
    is_active boolean not null default true,
    deleted boolean not null default false,
    deleted_by varchar(100),
    deleted_date timestamptz,
    version bigint not null default 0,
    constraint chk_background_job_status check (status in ('SCHEDULED','RUNNING','COMPLETED','FAILED'))
);

create index if not exists idx_workflow_definitions_entity_default on workflow_definitions(entity_type, default_workflow);
create index if not exists idx_workflow_steps_definition on workflow_steps(workflow_definition_id);
create index if not exists idx_workflow_actions_definition_from on workflow_actions(workflow_definition_id, from_step_id);
create index if not exists idx_approval_history_entity on approval_history(entity_type, entity_id);
create index if not exists idx_business_rules_context on business_rules(context);
create index if not exists idx_in_app_notifications_user_read on in_app_notifications(recipient_user_id, read_at);
create index if not exists idx_notification_preferences_user_type on notification_preferences(user_id, notification_type);
create index if not exists idx_background_job_executions_status_date on background_job_executions(status, created_date);
