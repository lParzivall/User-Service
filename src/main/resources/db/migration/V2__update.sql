insert into app_user(id, name, email, password)
values (1, 'john@mail.ru', 'John', 1234);
insert into app_user(id, name, email, password)
values (2, 'mike@mail.ru', 'Mike', 1234);

insert into app_user_role(id, name)
values (1, 'ROLE_ADMIN');
insert into app_user_role(id, name)
values (2, 'ROLE_USER');


insert into app_user_roles(app_user_id, roles_id)
values (1, 1);
insert into app_user_roles(app_user_id, roles_id)
values (2, 2);