
create table ACCOUNTS
(
  username varchar(255) primary key not null,
  password char(60) not null,
  mail_address varchar(255) not null,
  mail_addressVerified tinyint(1) not null,
  updated_at timestamp not null default current_timestamp,
  created_at timestamp not null default current_timestamp
);
