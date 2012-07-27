

# task TABLE
create table task (
	id int primary key,
	complete int,
	shortdescr varchar, 
	project int
);

# project TABLE
create table project (
	id int primary key,
	shortdescr varchar
);

#context TABLE
create table context (
	id int primary key,
	shortdescr varchar
);

