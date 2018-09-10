
 -- Inserting Admin related Data
 
 insert into admin_info (id, first_name, last_name, email_id, mobile_no) values (admin_sequence.nextval, 'NNIKHIL', 'AGARWAL', 'nikhil123@gmail.com', '9920656540');
 insert into admin_info (id, first_name, last_name, email_id, mobile_no) values (admin_sequence.nextval, 'YASH', 'ATISHAY', 'yash22@hotmail.com', '5656412897');
 
 -- Inserting Librarian related Data
 
 insert into librarian_details (id, first_name, last_name) values (librarian_sequence.nextval, 'WARREN', 'DSOUZA');
 insert into librarian_bgc_details (id, city, address, email_id, mobile_no) values (librarian_sequence.currval, 'MUMBAI', '2nd FLOOR, EDWINA HAVEN, GOVANDI', 'warren_souza@hotmail.com', '5558889991');
 insert into librarian_login (id, password) values (librarian_sequence.currval, '');
 
 insert into librarian_details (id, first_name, last_name) values (librarian_sequence.nextval, 'JIHAN', 'S. PUNIA');
 insert into librarian_bgc_details (id, city, address, email_id, mobile_no) values (librarian_sequence.currval, 'THANE', 'SOMEWHERE IN THAT VILLAGE', 'shnakee_chutiya@brazzers.com', '2228887791');
 insert into librarian_login (id, password) values (librarian_sequence.currval, 'iamgay');
 
 insert into librarian_details (id, first_name, last_name) values (librarian_sequence.nextval, 'DISHANT', 'DUBEY');
 insert into librarian_bgc_details (id, city, address, email_id, mobile_no) values (librarian_sequence.currval, 'JUPITER PLANET', 'NO DESCRIPTION REQUIRED - ALIEN CONFIRMED', 'distatnt_alien@alienporn.com', '0101010101');
 insert into librarian_login (id, password) values (librarian_sequence.currval, 'proudalien');
