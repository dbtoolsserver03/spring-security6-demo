create table demo_user
(
    user_id         bigint auto_increment,
    user_account_id varchar(30)  not null,
    user_password   varchar(255) not null,
    user_name       varchar(25)  not null,
    user_age        int
);

create unique index DEMO_USER_USER_ACCOUNT_ID_UINDEX
    on demo_user (user_account_id);

create unique index DEMO_USER_USER_ID_UINDEX
    on demo_user (user_id);

alter table demo_user
    add constraint DEMO_USER_PK
        primary key (user_id);

