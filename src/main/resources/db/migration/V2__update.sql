insert into app_user(id, name, email, password)
values (nextval('appuser_sequence'), 'John', 'john@mail.ru', '$2a$12$EEcLPaiGJ9mEKgfn69o8Q.MllyXlDa2nMxcw5jSZxbRCViqewjbUW');
insert into app_user(id, name, email, password)
values (nextval('appuser_sequence'), 'Mike', 'mike@mail.ru', '$2a$12$EEcLPaiGJ9mEKgfn69o8Q.MllyXlDa2nMxcw5jSZxbRCViqewjbUW');

insert into app_user_role(id, name)
values (nextval('role_sequence'), 'ROLE_ADMIN');
insert into app_user_role(id, name)
values (nextval('role_sequence'), 'ROLE_USER');

insert into app_user_roles(app_user_id, roles_id)
values (1, 1);
insert into app_user_roles(app_user_id, roles_id)
values (2, 2);