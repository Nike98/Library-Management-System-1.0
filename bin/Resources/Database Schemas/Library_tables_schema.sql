 
/* -- Create Table Admin_Info for all the Administrator details
 
 create table admin_info
 (
	id number(4,0) constraints admin_id_pk primary key,
	first_name varchar2(15) not null,
	last_name varchar2(15) not null,
	email_id varchar2(40) not null unique,
	mobile_no number(10,0) not null unique
 );
 
 
 -- Create Table Admin_Login for Admin login details
 
 create table admin_login
 (
	id number(4,0) constraints admin_login_id_fk references admin_info(id),
	password varchar2(16) not null
 );
 
 
 -- Create Table Librarian_Details
 
 create table librarian_details
 (
	id number(4,0) constraints librarian_details_id_pk primary key,
	first_name varchar2(15) not null,
	last_name varchar2(15) not null
 );
 
 
 -- Create Table Librarian_BGC_Details
 
 create table librarian_bgc_details
 (
	id number(4,0) constraints librarian_bgc_details_id_fk references librarian_details(id),
	city varchar2(20) not null,
	address varchar2(60) not null,
	email_id varchar2(40) not null unique,
	mobile_no number(10,0) not null unique
 );
 
 
 -- Create Table Librarian_Login
 
 create table librarian_login
 (
	id number(4,0) constraints librarian_login_fk references librarian_details(id),
	password varchar2(16) default 'admin@123'
 );
 
 
 -- Create Table Member having all the member details
 
 create table member
 (
	id number(6,0) constraints member_id_pk primary key,
	fname varchar2(15) not null,
	lname varchar2(15) not null,
	city varchar2(20) not null,
	address varchar2(100) not null,
	mobile_no number(10,0) not null unique,
	email_id varchar2(50) not null unique
 );
 
 
 -- Create Table Book having all the book details
 
 create table book
 (
	isbn varchar2(17) constraints book_isbn_pk primary key,
	title varchar2(50) not null,
	author varchar2(40) not null,
	edition_number varchar2(15) not null,
	publisher varchar2(40) not null,
	price number(5,0) not null,
	available char(1) default 'Y' check (available in ('Y','N'))
 );
 */

 /* Create Table Book */
create table book
(
	isbn varchar(17),
	title varchar(200) not null constraint book_title,
	author varchar(60) not null constraint book_author,
	edition_number varchar(60) not null constraint book_edition_number,
	publisher varchar(200) not null constraint book_publisher,
	price decimal(5,2) not null constraint book_price,
	available boolean default true constraint book_available,
	constraint book_isbn_pk primary key (isbn)
);

/* Create Table Member */
create table member
(
	id integer generated always as identity (start with 100, increment by 1),
	name varchar(200) not null constraint member_name,
	city varchar(100) not null constraint member_city,
	address varchar(200) not null constraint member_address,
	mobile_no integer not null unique constraint member_mobile_no,
	email_id varchar(100) not null unique constraint member_email_id,
	constraint member_id_pk primary key (id)
);

/* Create Table Issue */
create table issue
(
	isbn varchar(17),
	member_id integer,
	issue_time timestamp default current_timestamp constraint issue_table_issue_time,
	day_count integer default 0 constraint issue_day_count,
	constraint issue_isbn_fk foreign key (isbn) references book(isbn),
	constraint issue_member_id foreign key (member_id) references member(id)
);