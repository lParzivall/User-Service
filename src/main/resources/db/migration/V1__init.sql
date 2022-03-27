drop table if exists app_user cascade;
drop table if exists app_user_roles cascade;
drop table if exists app_user_role cascade;

drop sequence if exists appuser_sequence;
drop sequence if exists role_sequence;


create table app_user
(
    id       int8 not null,
    email    varchar(255),
    name     varchar(255),
    password varchar(255),
    primary key (id)
);
create sequence appuser_sequence start 1 increment 1;




create table app_user_role
(
    id   int8 not null,
    name varchar(255),
    primary key (id)
);
create sequence role_sequence start 1 increment 1;



create table app_user_roles
(
    app_user_id int8 not null,
    roles_id    int8 not null
);

alter table app_user_roles
    add constraint FKphgqfhso6hu17lt8ax3yh55eh
        foreign key (roles_id)
            references app_user_role;

alter table app_user_roles
    add constraint FKkwxexnudtp5gmt82j0qtytnoe
        foreign key (app_user_id)
            references app_user;




