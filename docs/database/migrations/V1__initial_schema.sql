create extension if not exists pgcrypto;

create table app_users (
    id uuid primary key default gen_random_uuid(),
    username varchar(80) not null unique,
    email varchar(150) not null unique,
    employee_code varchar(40) not null unique,
    full_name varchar(150) not null,
    password_hash varchar(120) not null,
    created_by varchar(100) not null,
    created_date timestamptz not null,
    updated_by varchar(100) not null,
    updated_date timestamptz not null,
    is_active boolean not null default true,
    version bigint not null default 0
);

create table user_roles (
    user_id uuid not null references app_users(id),
    role varchar(40) not null,
    primary key (user_id, role),
    constraint chk_user_roles_role check (role in ('ADMINISTRATOR','SALES_TEAM','BUSINESS_TEAM','TAG_MANAGER','TAG_ASSOCIATE','READ_ONLY_USER'))
);

create table refresh_tokens (
    id uuid primary key default gen_random_uuid(),
    user_id uuid not null references app_users(id),
    token_hash varchar(128) not null unique,
    expires_at timestamptz not null,
    revoked_at timestamptz,
    created_by varchar(100) not null,
    created_date timestamptz not null,
    updated_by varchar(100) not null,
    updated_date timestamptz not null,
    is_active boolean not null default true,
    version bigint not null default 0
);

create table job_requests (
    id uuid primary key default gen_random_uuid(),
    request_number varchar(40) not null unique,
    client_name varchar(150) not null,
    project_name varchar(150) not null,
    job_title varchar(150) not null,
    description text not null,
    location varchar(120) not null,
    employment_type varchar(30) not null,
    priority varchar(20) not null,
    number_of_positions integer not null,
    min_experience_years integer not null,
    max_experience_years integer not null,
    budget_amount numeric(14,2),
    target_date date not null,
    status varchar(40) not null,
    sales_owner_id uuid not null references app_users(id),
    business_approver_id uuid references app_users(id),
    tag_manager_id uuid references app_users(id),
    tag_associate_id uuid references app_users(id),
    created_by varchar(100) not null,
    created_date timestamptz not null,
    updated_by varchar(100) not null,
    updated_date timestamptz not null,
    is_active boolean not null default true,
    version bigint not null default 0,
    constraint chk_job_positions_positive check (number_of_positions > 0),
    constraint chk_job_experience_range check (min_experience_years >= 0 and max_experience_years >= min_experience_years),
    constraint chk_job_status check (status in ('DRAFT','PENDING_BUSINESS_APPROVAL','BUSINESS_APPROVED','BUSINESS_REJECTED','TAG_MANAGER_ASSIGNED','TAG_ASSOCIATE_ASSIGNED','CANDIDATE_SOURCING','INTERVIEW_IN_PROGRESS','OFFER_RELEASED','JOINED','CLOSED','CANCELLED'))
);

create table job_status_history (
    id uuid primary key default gen_random_uuid(),
    job_request_id uuid not null references job_requests(id),
    old_status varchar(40),
    new_status varchar(40) not null,
    comments text,
    created_by varchar(100) not null,
    created_date timestamptz not null,
    updated_by varchar(100) not null,
    updated_date timestamptz not null,
    is_active boolean not null default true,
    version bigint not null default 0
);

create table candidates (
    id uuid primary key default gen_random_uuid(),
    candidate_code varchar(40) not null unique,
    job_request_id uuid not null references job_requests(id),
    first_name varchar(80) not null,
    last_name varchar(80) not null,
    email varchar(150) not null,
    phone varchar(30) not null,
    current_company varchar(150),
    total_experience_years integer not null,
    expected_ctc numeric(14,2),
    current_ctc numeric(14,2),
    notice_period_days integer,
    status varchar(40) not null,
    created_by varchar(100) not null,
    created_date timestamptz not null,
    updated_by varchar(100) not null,
    updated_date timestamptz not null,
    is_active boolean not null default true,
    version bigint not null default 0,
    constraint chk_candidate_experience check (total_experience_years >= 0),
    constraint chk_candidate_notice check (notice_period_days is null or notice_period_days between 0 and 365)
);

create table candidate_status_history (
    id uuid primary key default gen_random_uuid(),
    candidate_id uuid not null references candidates(id),
    old_status varchar(40),
    new_status varchar(40) not null,
    comments text,
    created_by varchar(100) not null,
    created_date timestamptz not null,
    updated_by varchar(100) not null,
    updated_date timestamptz not null,
    is_active boolean not null default true,
    version bigint not null default 0
);

create table interviews (
    id uuid primary key default gen_random_uuid(),
    candidate_id uuid not null references candidates(id),
    round_name varchar(80) not null,
    scheduled_at timestamptz not null,
    mode varchar(40) not null,
    interviewer_name varchar(150) not null,
    feedback text,
    result varchar(30) not null,
    created_by varchar(100) not null,
    created_date timestamptz not null,
    updated_by varchar(100) not null,
    updated_date timestamptz not null,
    is_active boolean not null default true,
    version bigint not null default 0
);

create table offers (
    id uuid primary key default gen_random_uuid(),
    candidate_id uuid not null unique references candidates(id),
    offer_amount numeric(14,2) not null,
    joining_date date not null,
    status varchar(30) not null,
    created_by varchar(100) not null,
    created_date timestamptz not null,
    updated_by varchar(100) not null,
    updated_date timestamptz not null,
    is_active boolean not null default true,
    version bigint not null default 0,
    constraint chk_offer_amount_positive check (offer_amount > 0)
);

create table joinings (
    id uuid primary key default gen_random_uuid(),
    candidate_id uuid not null unique references candidates(id),
    joining_date date not null,
    status varchar(30) not null,
    remarks text,
    created_by varchar(100) not null,
    created_date timestamptz not null,
    updated_by varchar(100) not null,
    updated_date timestamptz not null,
    is_active boolean not null default true,
    version bigint not null default 0
);

create table file_metadata (
    id uuid primary key default gen_random_uuid(),
    entity_type varchar(60) not null,
    entity_id uuid not null,
    original_file_name varchar(255) not null,
    stored_file_name varchar(255) not null unique,
    content_type varchar(120) not null,
    file_size_bytes bigint not null,
    storage_path varchar(500) not null,
    uploaded_by_user_id uuid not null,
    sha256_checksum varchar(64) not null,
    created_by varchar(100) not null,
    created_date timestamptz not null,
    updated_by varchar(100) not null,
    updated_date timestamptz not null,
    is_active boolean not null default true,
    version bigint not null default 0,
    constraint chk_file_size_positive check (file_size_bytes > 0)
);

create table notification_logs (
    id uuid primary key default gen_random_uuid(),
    recipient_email varchar(150) not null,
    subject varchar(180) not null,
    html_body text not null,
    status varchar(20) not null,
    retry_count integer not null default 0,
    last_error text,
    sent_at timestamptz,
    created_by varchar(100) not null,
    created_date timestamptz not null,
    updated_by varchar(100) not null,
    updated_date timestamptz not null,
    is_active boolean not null default true,
    version bigint not null default 0
);

create table audit_logs (
    id uuid primary key default gen_random_uuid(),
    event_type varchar(40) not null,
    actor varchar(100) not null,
    action varchar(120) not null,
    entity_type varchar(80),
    entity_id varchar(80),
    details text,
    created_by varchar(100) not null,
    created_date timestamptz not null,
    updated_by varchar(100) not null,
    updated_date timestamptz not null,
    is_active boolean not null default true,
    version bigint not null default 0
);

create index idx_app_users_username on app_users(username);
create index idx_app_users_email on app_users(email);
create index idx_app_users_active on app_users(is_active);
create index idx_refresh_tokens_token_hash on refresh_tokens(token_hash);
create index idx_refresh_tokens_user on refresh_tokens(user_id);
create index idx_refresh_tokens_expires_at on refresh_tokens(expires_at);
create index idx_job_requests_status on job_requests(status);
create index idx_job_requests_sales_owner on job_requests(sales_owner_id);
create index idx_job_requests_business_approver on job_requests(business_approver_id);
create index idx_job_requests_tag_manager on job_requests(tag_manager_id);
create index idx_job_requests_tag_associate on job_requests(tag_associate_id);
create index idx_job_requests_target_date on job_requests(target_date);
create index idx_job_status_history_job on job_status_history(job_request_id);
create index idx_job_status_history_new_status on job_status_history(new_status);
create index idx_candidates_job on candidates(job_request_id);
create index idx_candidates_status on candidates(status);
create index idx_candidates_email on candidates(email);
create index idx_candidates_phone on candidates(phone);
create index idx_candidate_status_history_candidate on candidate_status_history(candidate_id);
create index idx_candidate_status_history_new_status on candidate_status_history(new_status);
create index idx_interviews_candidate on interviews(candidate_id);
create index idx_interviews_scheduled_at on interviews(scheduled_at);
create index idx_interviews_result on interviews(result);
create index idx_offers_candidate on offers(candidate_id);
create index idx_offers_status on offers(status);
create index idx_joinings_candidate on joinings(candidate_id);
create index idx_joinings_status on joinings(status);
create index idx_file_metadata_owner on file_metadata(entity_type, entity_id);
create index idx_file_metadata_uploaded_by on file_metadata(uploaded_by_user_id);
create index idx_notification_logs_status on notification_logs(status);
create index idx_notification_logs_recipient on notification_logs(recipient_email);
create index idx_audit_logs_event_type on audit_logs(event_type);
create index idx_audit_logs_actor on audit_logs(actor);
