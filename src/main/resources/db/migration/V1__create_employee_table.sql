create table employee(
    id varchar(36),
    name varchar(100),
    apartment varchar(100),
    age smallint,
    role varchar(100),
    hired_date datetime(6),
    primary key (id)
) engine = InnoDB;
