
 -- Creating Sequence for Admin Data
 
 create sequence admin_sequence
 start with 1002
 increment by 3
 minvalue 1002
 maxvalue 1032
 nocycle;
 
 
 -- Creating Sequence for Librarian Data
 
 create sequence librarian_sequence
 start with 2018
 increment by 1
 minvalue 2018
 maxvalue 3000
 nocycle;
 
 
 -- Creating Sequence for Member Table
 
 create sequence member_sequence
 start with 6600 
 increment by 3
 minvalue 6600
 maxvalue 20000
 nocycle;
 