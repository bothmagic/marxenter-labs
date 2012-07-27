create table COMMENTS (
   COMMENT_ID bigint generated by default as identity (start with 1),
   VERSION integer not null,
   RATING varchar(255) not null,
   COMMENT_TEXT varchar(4000),
   ITEM_ID bigint not null,
   FROM_USER_ID bigint not null
);
create table CREDIT_CARD (
   CREDIT_CARD_ID bigint not null,
   CC_TYPE smallint not null,
   CC_NUMBER varchar(16) not null,
   EXP_MONTH varchar(2) not null,
   EXP_YEAR varchar(4) not null,
   primary key (CREDIT_CARD_ID)
);
create table BILLING_DETAILS (
   BILLING_DETAILS_ID bigint generated by default as identity (start with 1),
   CREDIT_CARD_ID bigint not null,
   VERSION integer not null,
   OWNER_NAME varchar(255) not null,
   CREATED timestamp not null,
   USER_ID bigint
);
create table AUDIT_LOG (
   AUDIT_LOG_ID bigint generated by default as identity (start with 1),
   MESSAGE varchar(255) not null,
   ENTITY_ID bigint not null,
   ENTITY_CLASS varchar(255) not null,
   USER_ID bigint not null,
   CREATED timestamp not null
);
create table BANK_ACCOUNT (
   BANK_ACCOUNT_ID bigint not null,
   ACCOUNT_NUMBER varchar(16) not null,
   BANK_NAME varchar(255) not null,
   BANK_SWIFT varchar(15) not null,
   primary key (BANK_ACCOUNT_ID)
);
create table CATEGORIZED_ITEM (
   CATEGORY_ID bigint not null,
   ITEM_ID bigint not null,
   DATE_ADDED timestamp not null,
   USERNAME varchar(255) not null,
   primary key (CATEGORY_ID, ITEM_ID)
);
create table CATEGORY (
   CATEGORY_ID bigint generated by default as identity (start with 1),
   VERSION integer not null,
   NAME varchar(255) not null,
   CREATED timestamp not null,
   PARENT_CATEGORY_ID bigint,
   unique (NAME, PARENT_CATEGORY_ID)
);
create table ITEM (
   ITEM_ID bigint generated by default as identity (start with 1),
   VERSION integer not null,
   NAME varchar(255) not null,
   DESCRIPTION varchar(4000) not null,
   INITIAL_PRICE numeric not null,
   INITIAL_PRICE_CURRENCY varchar(255) not null,
   RESERVE_PRICE numeric,
   RESERVE_PRICE_CURRENCY varchar(255),
   START_DATE timestamp not null,
   END_DATE timestamp not null,
   STATE char(1) not null,
   APPROVAL_DATETIME timestamp,
   CREATED timestamp not null,
   APPROVED_BY_USER_ID bigint,
   SELLER_ID bigint not null,
   SUCCESSFUL_BID_ID bigint
);
create table USERS (
   USER_ID bigint generated by default as identity (start with 1),
   VERSION integer not null,
   FIRSTNAME varchar(255) not null,
   LASTNAME varchar(255) not null,
   USERNAME varchar(16) not null,
   "PASSWORD" varchar(12) not null,
   EMAIL varchar(255) not null,
   RANKING integer not null,
   IS_ADMIN bit not null,
   CREATED timestamp not null,
   DEFAULT_BILLING_DETAILS_ID bigint,
   STREET varchar(255),
   ZIPCODE varchar(16),
   CITY varchar(255),
   unique (USERNAME)
);
create table BID (
   BID_ID bigint generated by default as identity (start with 1),
   AMOUNT numeric not null,
   AMOUNT_CURRENCY varchar(255) not null,
   CREATED timestamp not null,
   ITEM_ID bigint not null,
   BIDDER_ID bigint not null
);

--alter table COMMENTS add constraint FK3_ITEM_ID foreign key (ITEM_ID) references ITEM;
--alter table COMMENTS add constraint FK2_FROM_USER_ID foreign key (FROM_USER_ID) references USERS;
--alter table CREDIT_CARD add constraint FK1_CREDIT_CARD_ID foreign key (CREDIT_CARD_ID) references BILLING_DETAILS;
--alter table BILLING_DETAILS add constraint FK1_USER_ID foreign key (USER_ID) references USERS;
--alter table BANK_ACCOUNT add constraint FK1_BANK_ACCOUNT_ID foreign key (BANK_ACCOUNT_ID) references BILLING_DETAILS;
--alter table CATEGORIZED_ITEM add constraint FK1C7A5407A8EEBDC7 foreign key (ITEM_ID) references ITEM;
--alter table CATEGORIZED_ITEM add constraint FK1C7A5407DBFCB7FC foreign key (CATEGORY_ID) references CATEGORY;
--alter table CATEGORY add constraint FK1_PARENT_CATEGORY_ID foreign key (PARENT_CATEGORY_ID) references CATEGORY;
--alter table ITEM add constraint FK1_APPROVED_BY_USER_ID foreign key (APPROVED_BY_USER_ID) references USERS;
--alter table ITEM add constraint FK2_SELLER_ID foreign key (SELLER_ID) references USERS;
--alter table ITEM add constraint FK3_SUCCESSFUL_BID_ID foreign key (SUCCESSFUL_BID_ID) references BID;
--alter table USERS add constraint FK1_DEFAULT_BILLING_DETAILS_ID foreign key (DEFAULT_BILLING_DETAILS_ID) references BILLING_DETAILS;
--alter table BID add constraint FK2_BIDDER_ID foreign key (BIDDER_ID) references USERS;
--alter table BID add constraint FK1_ITEM_ID foreign key (ITEM_ID) references ITEM;


insert into CREDIT_CARD values(1, 1, '0000000000000000', '01', '09');
insert into CREDIT_CARD values(2, 1, '1111111111111111', '07', '08');
insert into CREDIT_CARD values(3, 3, '2222222222222222', '11', '09');
insert into CREDIT_CARD values(4, 2, '3333333333333333', '12', '09');
insert into CREDIT_CARD values(5, 4, '4444444444444444', '04', '07');
insert into CREDIT_CARD values(6, 3, '5555555555555555', '08', '08');
insert into CREDIT_CARD values(7, 3, '6666666666666666', '07', '08');
insert into CREDIT_CARD values(8, 4, '7777777777777777', '06', '10');
insert into CREDIT_CARD values(9, 2, '8888888888888888', '06', '09');
insert into CREDIT_CARD values(10, 1, '9999999999999999', '02', '07');

insert into BANK_ACCOUNT values(1, '1384930293849302', 'Fictional Bank', '837283948372839');
insert into BANK_ACCOUNT values(2, '5432168798132154', 'Fictional Bank', '837283948372839');
insert into BANK_ACCOUNT values(3, '9898997843135415', 'Fictional Bank', '837283948372839');
insert into BANK_ACCOUNT values(4, '1656893565498475', 'Fictional Bank', '837283948372839');

insert into CATEGORY values(1, 1, 'Antiques', '2004-07-03 08:30:00.0', null);
insert into CATEGORY values(2, 1, 'Art', '2004-07-03 08:30:00.0', null);
insert into CATEGORY values(3, 1, 'Books', '2004-07-03 08:30:00.0', null);
insert into CATEGORY values(4, 1, 'Fiction', '2004-07-03 08:30:00.0', 3);
insert into CATEGORY values(5, 1, 'Non-Fiction', '2004-07-03 08:30:00.0', 3);
insert into CATEGORY values(6, 1, 'Computers & Networking', '2004-07-03 08:30:00.0', null);
insert into CATEGORY values(7, 1, 'Desktop PC''s', '2004-07-03 08:30:00.0', 6);
insert into CATEGORY values(8, 1, 'Laptops', '2004-07-03 08:30:00.0', 6);
insert into CATEGORY values(9, 1, 'Servers', '2004-07-03 08:30:00.0', 6);
insert into CATEGORY values(10, 1, 'Accessories', '2004-07-03 08:30:00.0', 6);
insert into CATEGORY values(11, 1, 'Health & Beauty', '2004-07-03 08:30:00.0', null);
insert into CATEGORY values(12, 1, 'Home & Garden', '2004-07-03 08:30:00.0', null);
insert into CATEGORY values(13, 1, 'Baby', '2004-07-03 08:30:00.0', 12);
insert into CATEGORY values(14, 1, 'Home Decor', '2004-07-03 08:30:00.0', 12);
insert into CATEGORY values(15, 1, 'Kitchen', '2004-07-03 08:30:00.0', 12);
insert into CATEGORY values(16, 1, 'Lawn & Garden', '2004-07-03 08:30:00.0', 12);
insert into CATEGORY values(17, 1, 'Pet Supplies', '2004-07-03 08:30:00.0', 12);
insert into CATEGORY values(18, 1, 'Tools', '2004-07-03 08:30:00.0', 12);
insert into CATEGORY values(19, 1, 'Music', '2004-07-03 08:30:00.0', null);
insert into CATEGORY values(20, 1, 'Sporting Goods', '2004-07-03 08:30:00.0', null);
insert into CATEGORY values(21, 1, 'Stamps', '2004-07-03 08:30:00.0', null);
insert into CATEGORY values(22, 1, 'Toys & Hobbies', '2004-07-03 08:30:00.0', null);
insert into CATEGORY values(23, 1, 'Action Figures', '2004-07-03 08:30:00.0', 22);
insert into CATEGORY values(24, 1, 'Diecast', '2004-07-03 08:30:00.0', 22);
insert into CATEGORY values(25, 1, 'Video Games', '2004-07-03 08:30:00.0', null);

insert into USERS values(1, 1, 'Snow', 'White', 'iluvapples', 'sweetlips', 'swhite@fairytaleserver.grimm', 1, 0, '2004-07-03 08:30:00.0', null, '1234 Cottage Way', '55555', 'Woodsville');
insert into USERS values(2, 1, 'Robin', 'Hood', 'rhood', 'marian', 'tights@fairytaleserver.grimm', 1, 0, '2004-07-03 08:30:00.0', null, 'The Big Oak', '55555', 'Sherwood Forrest');
insert into USERS values(3, 1, 'Captain', 'Nemo', 'captain', 'dive!', 'captain@fairytaleserver.grimm', 1, 0, '2004-07-03 08:30:00.0', null, '4433 Dock #3', '55555', 'Oceanside');
insert into USERS values(4, 1, 'Don', 'Quixote', 'knight', 'valor_n_glory', 'quixote@fairytaleserver.grimm', 1, 0, '2004-07-03 08:30:00.0', null, '3324 Mill St', '55555', 'Countryside');

insert into BILLING_DETAILS values(1, 1, 1, 'Snow White', '2004-07-03 08:30:00.0', 1);
insert into BILLING_DETAILS values(2, 4, 2, 'Robin Hood', '2004-07-03 08:30:00.0', 1);
insert into BILLING_DETAILS values(3, 3, 3, 'Captain Nemo', '2004-07-03 08:30:00.0', 1);
insert into BILLING_DETAILS values(4, 5, 4, 'Don Quixote', '2004-07-03 08:30:00.0', 1);

insert into ITEM values(1, 1, 'Apple', 'Insomniac? Can''t get a decent nights sleep? Try this delicious apple! You''ll be amazed!', 35.00, 'USD', 50.00, 'USD', '2004-07-03 08:30:00.0', '2004-07-03 08:30:00.0', 'a', null, '2004-07-03 08:30:00.0', null, 1, null);
insert into ITEM values(2, 1, 'Silver Arrow', 'Need to impress someone? Imagine yourself showing off this amazing sterling silver arrow to your friends! You''ll be the hit of the party!', 9050.00, 'USD', 49999.99, 'USD', '2004-07-03 08:30:00.0', '2004-07-03 08:30:00.0', 'a', null, '2004-07-03 08:30:00.0', null, 2, null);
insert into ITEM values(3, 1, 'Quarterstaff', 'Slightly used, excellent condition', 0.00, 'USD', 45.00, 'USD', '2004-07-03 08:30:00.0', '2004-07-03 08:30:00.0', 'a', null, '2004-07-03 08:30:00.0', null, 2, null);
insert into ITEM values(4, 1, 'Calamari', 'Nemo Enterprises brings you the *best* in deep sea Calamari! Direct from the ocean, no preservatives, all natural!', 15.00, 'USD', 25.00, 'USD', '2004-07-03 08:30:00.0', '2004-07-03 08:30:00.0', 'a', null, '2004-07-03 08:30:00.0', null, 3, null);

insert into CATEGORIZED_ITEM values(11, 1, '2004-07-03 08:30:00.0', 'iluvapples');
insert into CATEGORIZED_ITEM values(20, 2, '2004-07-03 08:30:00.0', 'rhood');
insert into CATEGORIZED_ITEM values(22, 2, '2004-07-03 08:30:00.0', 'rhood');
insert into CATEGORIZED_ITEM values(14, 2, '2004-07-03 08:30:00.0', 'rhood');
insert into CATEGORIZED_ITEM values(1, 2, '2004-07-03 08:30:00.0', 'rhood');
insert into CATEGORIZED_ITEM values(22, 3, '2004-07-03 08:30:00.0', 'rhood');
insert into CATEGORIZED_ITEM values(20, 3, '2004-07-03 08:30:00.0', 'rhood');
insert into CATEGORIZED_ITEM values(16, 3, '2004-07-03 08:30:00.0', 'rhood');
insert into CATEGORIZED_ITEM values(15, 4, '2004-07-03 08:30:00.0', 'captain');