;;
create table task (
        id INTEGER PRIMARY KEY,
	complete int,
	shortdescr varchar,
	project int,
        contexts varchar,
	longdescr TEXT,
	sort int);
;;
create table project (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        shortdescr varchar);
;;
create table context (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        parent INTEGER,
        count INTEGER,
        shortdescr varchar);
