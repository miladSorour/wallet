create table transaction
(
    id                 number(19, 0)      not null,
    created_by         number(19, 0)      not null,
    created_date       timestamp(6) with time zone,
    last_modified_by   number(19, 0),
    last_modified_date timestamp(6) with time zone,
    amount             float(53)          not null,
    detail             varchar2(255 char),
    timestamp          timestamp(6)       not null,
    type               varchar2(255 char) not null check (type in ('TOPUP', 'WITHDRAW', 'TRANSFER')),
    wallet_id          number(19, 0)      not null,
    primary key (id)
);
create table wallet
(
    id                 number(19, 0) not null,
    created_by         number(19, 0) not null,
    created_date       timestamp(6) with time zone,
    last_modified_by   number(19, 0),
    last_modified_date timestamp(6) with time zone,
    balance            float(53)     not null,
    version            number(19, 0),
    user_id            number(19, 0) not null,
    primary key (id)
);
create table wallet_user
(
    id                 number(19, 0)      not null,
    created_by         number(19, 0)      not null,
    created_date       timestamp(6) with time zone,
    last_modified_by   number(19, 0),
    last_modified_date timestamp(6) with time zone,
    enabled            number(1, 0)       not null check (enabled in (0, 1)),
    full_name          varchar2(255 char) not null,
    password           varchar2(255 char) not null,
    username           varchar2(255 char) not null,
    version            number(19, 0),
    primary key (id)
);
alter table wallet
    add constraint UKhgee4p1hiwadqinr0avxlq4eb unique (user_id);
alter table wallet_user
    add constraint UKegqusy937sbn54ed75mhknm71 unique (username);
alter table transaction
    add constraint FKtfwlfspv2h4wcgc9rjd1658a6 foreign key (wallet_id) references wallet;
alter table wallet
    add constraint FKmkf27omhr0j9t77rpxemavecp foreign key (user_id) references wallet_user;

create sequence tx_sequence_generator start with 1 increment by 1;
create sequence user_sequence_generator start with 1 increment by 1;
create sequence wallet_sequence_generator start with 1 increment by 1;
