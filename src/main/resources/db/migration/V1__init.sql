drop table if exists app_user cascade;
drop table if exists app_user_roles cascade;
drop table if exists app_user_role cascade;

drop sequence if exists appuser_sequence;
drop sequence if exists role_sequence;


create table app_user
(
    id       bigserial,
    email    varchar(255),
    name     varchar(255),
    password varchar(255),
    primary key (id)
);
create sequence appuser_sequence start with 1 increment by 1;


create table app_user_role
(
    id   bigserial,
    name varchar(255),
    primary key (id)
);
create sequence role_sequence start with 1 increment by 1;


create table app_user_roles
(
    app_user_id bigserial not null,
    roles_id    bigserial not null
);


alter table app_user_roles
    add constraint FK_roles_role
        foreign key (roles_id)
            references app_user_role;

alter table app_user_roles
    add constraint FK_roles_user
        foreign key (app_user_id)
            references app_user;