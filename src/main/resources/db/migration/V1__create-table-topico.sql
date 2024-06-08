create table topico (
id bigint not null auto_increment,
titulo varchar (250) not null,
mensaje varchar (750) not null,
fecha_creacion varchar (11) not null,
status tinyint (2) not null,
autor varchar (25) not null,
curso varchar (75) not null,
respuestas varchar (750),

primary key(id)
);