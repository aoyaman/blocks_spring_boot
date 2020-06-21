
create table GAME
(
  id integer primary key,
  date varchar(255),
  nowPlayer integer,
  counter integer,
  author varchar(255),
  updated_at timestamp not null default current_timestamp,
  created_at timestamp not null default current_timestamp
);
