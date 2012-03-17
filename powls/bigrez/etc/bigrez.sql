
-- Oracle table-creation script
-- If you port this to other databases send it to us and we'll put it in for others to use!

drop sequence common_sequence;
drop table reservationrate;
drop table reservation;
drop table guestprofile;
drop table rate;
drop table inventory;
drop table roomtype;
drop table offer;
drop table property;

create sequence COMMON_SEQUENCE
      INCREMENT BY 100
      START WITH 100
      MAXVALUE 9223372036854775807;

create table RESERVATIONRATE (
  ID NUMBER(10) NOT NULL,
  VERSION NUMBER(4) NOT NULL,
  RESERVATION_ID NUMBER(10) NOT NULL,
  STARTDATE DATE NOT NULL,
  NUMNIGHTS NUMBER(10) NOT NULL,
  RATE NUMBER(12,5) NOT NULL,
  constraint RESERVATIONRATE_PK primary key (ID) );

create table RATE (
  ID NUMBER(10) NOT NULL,
    VERSION NUMBER(4) NOT NULL,
  ROOMTYPE_ID NUMBER(10) NOT NULL,
  STARTDATE DATE NOT NULL,
  ENDDATE DATE NOT NULL,
  RATE NUMBER(12,5) NOT NULL,
  constraint RATE_PK primary key (ID) );

create table RESERVATION (
  ID NUMBER(10) NOT NULL,
    VERSION NUMBER(4) NOT NULL,
  CONFIRMNUM VARCHAR2(10) NOT NULL,
  GUESTPROFILE_ID NUMBER(10) NOT NULL,
  ROOMTYPE_ID NUMBER(10) NOT NULL,
  ARRIVE DATE NOT NULL,
  DEPART DATE NOT NULL,
  CARDTYPE VARCHAR2(20) NOT NULL,
  CARDEXP VARCHAR2(10) NOT NULL,
  CARDNUM VARCHAR2(30) NOT NULL,
  constraint RESERVATION_PK primary key (ID) );

create table GUESTPROFILE (
  ID NUMBER(10) NOT NULL,
    VERSION NUMBER(4) NOT NULL,
  LOGON VARCHAR2(10) NOT NULL,
  PASSWORD VARCHAR2(10) NOT NULL,
  FIRSTNAME VARCHAR2(30) NULL,
  LASTNAME VARCHAR2(30) NULL,
  PHONE VARCHAR2(30) NULL,
  EMAIL VARCHAR2(60) NULL,
  CARDTYPE VARCHAR2(20) NULL,
  CARDEXP VARCHAR2(10) NULL,
  CARDNUM VARCHAR2(30) NULL,
  constraint GUESTPROFILE_PK primary key (ID) );

create table INVENTORY (
  ID NUMBER(10) NOT NULL,
    VERSION NUMBER(4) NOT NULL,
  ROOMTYPE_ID NUMBER(10) NOT NULL,
  "DAY" DATE NOT NULL,
  ROOMSAVAIL NUMBER(10) NOT NULL,
  constraint INVENTORY_PK primary key (ID) );

create table ROOMTYPE (
  ID NUMBER(10) NOT NULL,
    VERSION NUMBER(4) NOT NULL,
  PROPERTY_ID NUMBER(10) NOT NULL,
  DESCRIPTION VARCHAR2(60) NOT NULL,
  FEATURES VARCHAR2(2000) NOT NULL,
  MAXADULTS NUMBER(10) NOT NULL,
  SMOKINGFLAG NUMBER(10) NOT NULL,
  NUMROOMS NUMBER(10) NOT NULL,
  constraint ROOMTYPE_PK primary key (ID) );

create table PROPERTY (
  ID NUMBER(10) NOT NULL,
    VERSION NUMBER(4) NOT NULL,
  DESCRIPTION VARCHAR2(60) NOT NULL,
  FEATURES VARCHAR2(2000) NOT NULL,
  ADDRESS1 VARCHAR2(60) NOT NULL,
  ADDRESS2 VARCHAR2(60) null,
  CITY VARCHAR2(30) NOT NULL,
  STATECODE CHAR(2) NOT NULL,
  POSTALCODE VARCHAR2(10) NOT NULL,
  PHONE VARCHAR2(30) NOT NULL,
  IMAGEFILE VARCHAR2(100),
  constraint PROPERTY_PK primary key (ID) );

create table OFFER (
  ID NUMBER(10) NOT NULL,
    VERSION NUMBER(4) NOT NULL,
  PROPERTY_ID NUMBER(10) NOT NULL,
  IMAGEFILE VARCHAR2(60) NOT NULL,
  CAPTION VARCHAR2(60) NOT NULL,
  DESCRIPTION VARCHAR2(2000) NOT NULL,
  constraint OFFER_PK primary key (ID) );

-- Add foreign key constraints to table RESERVATIONRATE.
alter table RESERVATIONRATE
  add constraint REZ_REZRATE_FK1 foreign key (RESERVATION_ID)
   references RESERVATION (ID);

-- Add foreign key constraints to table RATE.
alter table RATE
  add constraint ROOMTYPE_RATE_FK1 foreign key (ROOMTYPE_ID)
   references ROOMTYPE (ID);

-- Add foreign key constraints to table RESERVATION.
alter table RESERVATION
  add constraint ROOMTYPE_RESERVATION_FK1 foreign key (ROOMTYPE_ID)
   references ROOMTYPE (ID);

alter table RESERVATION
  add constraint GUESTPROFILE_RESERVATION_FK1 foreign key (GUESTPROFILE_ID)
   references GUESTPROFILE (ID);

-- Add foreign key constraints to table INVENTORY.
alter table INVENTORY
  add constraint ROOMTYPE_INVENTORY_FK1 foreign key (ROOMTYPE_ID)
   references ROOMTYPE (ID);

-- Add foreign key constraints to table ROOMTYPE.
alter table ROOMTYPE
  add constraint PROPERTY_ROOMTYPE_FK1 foreign key (PROPERTY_ID)
   references PROPERTY (ID);

-- Add foreign key constraints to table OFFER.
alter table OFFER
  add constraint PROPERTY_OFFER_FK1 foreign key (PROPERTY_ID)
   references PROPERTY (ID);

-- Add uniqueness constraint to table GUESTPROFILE.
alter table GUESTPROFILE
  add constraint GUESTPROFILE_UNIQUE1 unique (LOGON);

commit;

insert into property values (51, 1, 'BigRez Inn', 'The BigRez Inn features:<ul><li>Heated pool with jacuzzi and sauna<li>In-room coffee makers<li>Cable television with full premium channels<li>Continental breakfast</ul>Come stay with us!', '1000 37th Ave', '', 'Minneapolis', 'MN', '55402', '612-555-1212', 'bigrezinn.gif');
insert into roomtype values (11, 1, 51, 'Standard Room', 'A Standard Room features a color TV and coffeemaker', 2, 0, 20);
insert into roomtype values (12, 1, 51, 'Deluxe Room', 'A Deluxe Room features a color TV, coffee maker, and view of the downtown skyline', 4, 0, 15);
insert into roomtype values (13, 1, 51, 'Presidential Suite', 'This is the very best room in the house. It features a whirlpool, sauna, oversized bed, and personal massage therapist.', 2, 1, 1);
insert into rate values (11, 1, 11,  to_date( '04/01/2002 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'),  to_date( '08/31/2003 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'), 115);
insert into rate values (12, 1, 11,  to_date( '09/01/2003 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'),  to_date( '12/31/2005 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'), 85);
insert into rate values (13, 1, 12,  to_date( '04/01/2002 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'),  to_date( '08/31/2003 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'), 135);
insert into rate values (14, 1, 12,  to_date( '09/01/2003 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'),  to_date( '12/31/2005 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'), 105);
insert into rate values (15, 1, 13,  to_date( '04/01/2002 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'),  to_date( '12/31/2005 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'), 300);

insert into inventory values (1, 1, 11,  to_date( '06/01/2003 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'), 0);
insert into inventory values (2, 1, 11,  to_date( '06/11/2003 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'), 10);

insert into property values (52, 1, 'BigRez Inn of Duluth', 'The BigRez Inn of Duluth features:<ul><li>Heated pool with jacuzzi and sauna<li>In-room coffee makers<li>Cable television with full premium channels<li>Continental breakfast</ul>Come stay with us!', '45 Main Street', '', 'Duluth', 'MN', '53020', '655-555-1212', 'bigrezduluth.gif');
insert into roomtype values (21, 1, 52, 'Standard Room', 'A Standard Room features a color TV and coffeemaker', 2, 0, 10);
insert into roomtype values (22, 1, 52, 'Deluxe Room', 'A Deluxe Room features a color TV, coffee maker, and view of the harbor', 4, 0, 5);
insert into rate values (21, 1, 21,  to_date( '04/01/2002 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'),  to_date( '12/31/2005 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'), 95);
insert into rate values (22, 1, 22,  to_date( '04/01/2002 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'),  to_date( '12/31/2005 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'), 105);


insert into property values (61, 1, 'DewDrop Inn Downtown', 'No features at all!', '75 5th Ave', '', 'Minneapolis', 'MN', '55401', '612-555-3232', 'dewdropdowntown.gif');
insert into roomtype values (31, 1, 61, 'Standard Room', 'A Standard Room features a bed. Thats it.', 2, 0, 10);
insert into roomtype values (32, 1, 61, 'Deluxe Room', 'A Deluxe Room features a bed and a lamp. Consider yourself lucky.', 4, 0, 5);
insert into rate values (31, 1, 31,  to_date( '04/01/2002 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'),  to_date( '12/31/2005 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'), 65);
insert into rate values (32, 1, 32,  to_date( '04/01/2002 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'),  to_date( '12/31/2005 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'), 75);

insert into offer values (1, 1, 51,'bigrezinn.gif','Free Breakfast!','Free continental breakfast at the BigRez Inn every morning! Come stay with us!');
insert into offer values (2, 1, 52,'bigrezduluth.gif','Free Harbor Trip!','BigRez Inn of Duluth features a free harbor boat trip for you and your guest!');
insert into offer values (3, 1, 61,'dewdropdowntown.gif','Small Cost, Large Value!','We have friendly and helpful staff and that''s about it');

insert into guestprofile values (1, 1, 'nyberg', 'x', 'Greg', 'Nyberg', '612-555-1313', 'nyberg@yahoo.com', 'American Express', 'May 2004', '370000000000002');

commit;
