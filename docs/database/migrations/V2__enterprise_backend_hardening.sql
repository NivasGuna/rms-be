create extension if not exists pgcrypto;

alter table if exists app_users add column if not exists deleted boolean not null default false;
alter table if exists app_users add column if not exists deleted_by varchar(100);
alter table if exists app_users add column if not exists deleted_date timestamptz;
alter table if exists app_users add column if not exists failed_login_attempts integer not null default 0;
alter table if exists app_users add column if not exists account_locked_until timestamptz;
alter table if exists app_users add column if not exists password_changed_at timestamptz;
alter table if exists app_users add column if not exists password_expires_at timestamptz;
alter table if exists refresh_tokens add column if not exists deleted boolean not null default false;
alter table if exists refresh_tokens add column if not exists deleted_by varchar(100);
alter table if exists refresh_tokens add column if not exists deleted_date timestamptz;
alter table if exists job_requests add column if not exists deleted boolean not null default false;
alter table if exists job_requests add column if not exists deleted_by varchar(100);
alter table if exists job_requests add column if not exists deleted_date timestamptz;
alter table if exists job_status_history add column if not exists deleted boolean not null default false;
alter table if exists job_status_history add column if not exists deleted_by varchar(100);
alter table if exists job_status_history add column if not exists deleted_date timestamptz;
alter table if exists candidates add column if not exists deleted boolean not null default false;
alter table if exists candidates add column if not exists deleted_by varchar(100);
alter table if exists candidates add column if not exists deleted_date timestamptz;
alter table if exists candidate_status_history add column if not exists deleted boolean not null default false;
alter table if exists candidate_status_history add column if not exists deleted_by varchar(100);
alter table if exists candidate_status_history add column if not exists deleted_date timestamptz;
alter table if exists interviews add column if not exists deleted boolean not null default false;
alter table if exists interviews add column if not exists deleted_by varchar(100);
alter table if exists interviews add column if not exists deleted_date timestamptz;
alter table if exists offers add column if not exists deleted boolean not null default false;
alter table if exists offers add column if not exists deleted_by varchar(100);
alter table if exists offers add column if not exists deleted_date timestamptz;
alter table if exists joinings add column if not exists deleted boolean not null default false;
alter table if exists joinings add column if not exists deleted_by varchar(100);
alter table if exists joinings add column if not exists deleted_date timestamptz;
alter table if exists file_metadata add column if not exists deleted boolean not null default false;
alter table if exists file_metadata add column if not exists deleted_by varchar(100);
alter table if exists file_metadata add column if not exists deleted_date timestamptz;
alter table if exists file_metadata add column if not exists file_version integer not null default 1;
alter table if exists file_metadata add column if not exists duplicate_of_file_id uuid references file_metadata(id);
alter table if exists file_metadata add column if not exists storage_strategy varchar(30) not null default 'LOCAL_DISK';
alter table if exists file_metadata add column if not exists virus_scan_status varchar(30) not null default 'PENDING';
alter table if exists notification_logs add column if not exists deleted boolean not null default false;
alter table if exists notification_logs add column if not exists deleted_by varchar(100);
alter table if exists notification_logs add column if not exists deleted_date timestamptz;
alter table if exists audit_logs add column if not exists deleted boolean not null default false;
alter table if exists audit_logs add column if not exists deleted_by varchar(100);
alter table if exists audit_logs add column if not exists deleted_date timestamptz;

create table if not exists business_units (
    id uuid primary key default gen_random_uuid(),
    code varchar(60) not null unique,
    name varchar(150) not null,
    description text,
    sort_order integer not null default 100,
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

create table if not exists departments (
    id uuid primary key default gen_random_uuid(),
    code varchar(60) not null unique,
    name varchar(150) not null,
    description text,
    sort_order integer not null default 100,
    business_unit_id uuid references business_units(id),
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

create table if not exists designations (
    id uuid primary key default gen_random_uuid(),
    code varchar(60) not null unique,
    name varchar(150) not null,
    description text,
    sort_order integer not null default 100,
    department_id uuid references departments(id),
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

create table if not exists clients (
    id uuid primary key default gen_random_uuid(),
    code varchar(60) not null unique,
    name varchar(150) not null,
    description text,
    sort_order integer not null default 100,
    business_unit_id uuid references business_units(id),
    gst_number varchar(30),
    website varchar(200),
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

create table if not exists client_contacts (
    id uuid primary key default gen_random_uuid(),
    client_id uuid not null references clients(id),
    contact_name varchar(150) not null,
    email varchar(150) not null,
    phone varchar(30),
    designation varchar(120),
    primary_contact boolean not null default false,
    created_by varchar(100) not null,
    created_date timestamptz not null,
    updated_by varchar(100) not null,
    updated_date timestamptz not null,
    is_active boolean not null default true,
    deleted boolean not null default false,
    deleted_by varchar(100),
    deleted_date timestamptz,
    version bigint not null default 0,
    constraint uq_client_contacts_client_email unique (client_id, email)
);

create table if not exists skills (
    id uuid primary key default gen_random_uuid(),
    code varchar(60) not null unique,
    name varchar(150) not null,
    description text,
    sort_order integer not null default 100,
    category varchar(80),
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

create table if not exists locations (
    id uuid primary key default gen_random_uuid(),
    code varchar(60) not null unique,
    name varchar(150) not null,
    description text,
    sort_order integer not null default 100,
    city varchar(100) not null,
    state varchar(100) not null,
    country varchar(100) not null,
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

create table if not exists candidate_sources (
    id uuid primary key default gen_random_uuid(),
    code varchar(60) not null unique,
    name varchar(150) not null,
    description text,
    sort_order integer not null default 100,
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

create table if not exists interview_types (
    id uuid primary key default gen_random_uuid(),
    code varchar(60) not null unique,
    name varchar(150) not null,
    description text,
    sort_order integer not null default 100,
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

create table if not exists interview_modes (
    id uuid primary key default gen_random_uuid(),
    code varchar(60) not null unique,
    name varchar(150) not null,
    description text,
    sort_order integer not null default 100,
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

create table if not exists employment_types (
    id uuid primary key default gen_random_uuid(),
    code varchar(60) not null unique,
    name varchar(150) not null,
    description text,
    sort_order integer not null default 100,
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

create table if not exists priorities (
    id uuid primary key default gen_random_uuid(),
    code varchar(60) not null unique,
    name varchar(150) not null,
    description text,
    sort_order integer not null default 100,
    sla_hours integer,
    created_by varchar(100) not null,
    created_date timestamptz not null,
    updated_by varchar(100) not null,
    updated_date timestamptz not null,
    is_active boolean not null default true,
    deleted boolean not null default false,
    deleted_by varchar(100),
    deleted_date timestamptz,
    version bigint not null default 0,
    constraint chk_priorities_sla_hours check (sla_hours is null or sla_hours >= 0)
);

create table if not exists job_statuses (
    id uuid primary key default gen_random_uuid(),
    code varchar(60) not null unique,
    name varchar(150) not null,
    description text,
    sort_order integer not null default 100,
    terminal_status boolean not null default false,
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

create table if not exists candidate_statuses (
    id uuid primary key default gen_random_uuid(),
    code varchar(60) not null unique,
    name varchar(150) not null,
    description text,
    sort_order integer not null default 100,
    terminal_status boolean not null default false,
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

create table if not exists offer_statuses (
    id uuid primary key default gen_random_uuid(),
    code varchar(60) not null unique,
    name varchar(150) not null,
    description text,
    sort_order integer not null default 100,
    terminal_status boolean not null default false,
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

create table if not exists joining_statuses (
    id uuid primary key default gen_random_uuid(),
    code varchar(60) not null unique,
    name varchar(150) not null,
    description text,
    sort_order integer not null default 100,
    terminal_status boolean not null default false,
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

create table if not exists notification_types (
    id uuid primary key default gen_random_uuid(),
    code varchar(60) not null unique,
    name varchar(150) not null,
    description text,
    sort_order integer not null default 100,
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

create table if not exists email_templates (
    id uuid primary key default gen_random_uuid(),
    code varchar(60) not null unique,
    name varchar(150) not null,
    description text,
    sort_order integer not null default 100,
    notification_type_id uuid references notification_types(id),
    subject varchar(180) not null,
    html_body text not null,
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

create table if not exists menus (
    id uuid primary key default gen_random_uuid(),
    code varchar(60) not null unique,
    name varchar(150) not null,
    description text,
    sort_order integer not null default 100,
    parent_menu_id uuid references menus(id),
    route varchar(200),
    icon varchar(80),
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

create table if not exists permissions (
    id uuid primary key default gen_random_uuid(),
    code varchar(60) not null unique,
    name varchar(150) not null,
    description text,
    sort_order integer not null default 100,
    resource varchar(80) not null,
    action varchar(60) not null,
    created_by varchar(100) not null,
    created_date timestamptz not null,
    updated_by varchar(100) not null,
    updated_date timestamptz not null,
    is_active boolean not null default true,
    deleted boolean not null default false,
    deleted_by varchar(100),
    deleted_date timestamptz,
    version bigint not null default 0,
    constraint uq_permissions_resource_action unique (resource, action)
);

create table if not exists role_permissions (
    id uuid primary key default gen_random_uuid(),
    role varchar(40) not null,
    permission_id uuid not null references permissions(id),
    created_by varchar(100) not null,
    created_date timestamptz not null,
    updated_by varchar(100) not null,
    updated_date timestamptz not null,
    is_active boolean not null default true,
    deleted boolean not null default false,
    deleted_by varchar(100),
    deleted_date timestamptz,
    version bigint not null default 0,
    constraint uq_role_permissions_role_permission unique (role, permission_id),
    constraint chk_role_permissions_role check (role in ('ADMINISTRATOR','SALES_TEAM','BUSINESS_TEAM','TAG_MANAGER','TAG_ASSOCIATE','READ_ONLY_USER'))
);

create table if not exists menu_permissions (
    id uuid primary key default gen_random_uuid(),
    menu_id uuid not null references menus(id),
    permission_id uuid not null references permissions(id),
    created_by varchar(100) not null,
    created_date timestamptz not null,
    updated_by varchar(100) not null,
    updated_date timestamptz not null,
    is_active boolean not null default true,
    deleted boolean not null default false,
    deleted_by varchar(100),
    deleted_date timestamptz,
    version bigint not null default 0,
    constraint uq_menu_permissions_menu_permission unique (menu_id, permission_id)
);

create table if not exists api_permissions (
    id uuid primary key default gen_random_uuid(),
    http_method varchar(12) not null,
    path_pattern varchar(250) not null,
    permission_id uuid not null references permissions(id),
    created_by varchar(100) not null,
    created_date timestamptz not null,
    updated_by varchar(100) not null,
    updated_date timestamptz not null,
    is_active boolean not null default true,
    deleted boolean not null default false,
    deleted_by varchar(100),
    deleted_date timestamptz,
    version bigint not null default 0,
    constraint uq_api_permissions_method_path unique (http_method, path_pattern)
);

create index if not exists idx_app_users_active_deleted on app_users(is_active, deleted);
create index if not exists idx_app_users_account_locked_until on app_users(account_locked_until);
create index if not exists idx_job_requests_active_deleted_status on job_requests(is_active, deleted, status);
create index if not exists idx_candidates_active_deleted_status on candidates(is_active, deleted, status);
create index if not exists idx_interviews_active_deleted_result on interviews(is_active, deleted, result);
create index if not exists idx_offers_active_deleted_status on offers(is_active, deleted, status);
create index if not exists idx_joinings_active_deleted_status on joinings(is_active, deleted, status);
create index if not exists idx_audit_logs_created_date on audit_logs(created_date);
create index if not exists idx_audit_logs_actor_event_date on audit_logs(actor, event_type, created_date);
create index if not exists idx_file_metadata_checksum on file_metadata(sha256_checksum);
create index if not exists idx_file_metadata_duplicate_of on file_metadata(duplicate_of_file_id);
create index if not exists idx_business_units_active_deleted on business_units(is_active, deleted);
create index if not exists idx_departments_business_unit on departments(business_unit_id);
create index if not exists idx_designations_department on designations(department_id);
create index if not exists idx_clients_business_unit on clients(business_unit_id);
create index if not exists idx_client_contacts_client on client_contacts(client_id);
create index if not exists idx_skills_category on skills(category);
create index if not exists idx_locations_city_state_country on locations(city, state, country);
create index if not exists idx_email_templates_notification_type on email_templates(notification_type_id);
create index if not exists idx_menus_parent on menus(parent_menu_id);
create index if not exists idx_permissions_resource_action on permissions(resource, action);
create index if not exists idx_role_permissions_role on role_permissions(role);
create index if not exists idx_menu_permissions_menu on menu_permissions(menu_id);
create index if not exists idx_api_permissions_method_path on api_permissions(http_method, path_pattern);
