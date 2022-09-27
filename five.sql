prompt PL/SQL Developer import file
prompt Created on 2022年9月27日 by 98569
set feedback off
set define off
prompt Creating FIVE...
create table FIVE
(
  cname VARCHAR2(50) not null,
  pwd   VARCHAR2(60)
)
;
alter table FIVE
  add primary key (CNAME);

prompt Loading FIVE...
insert into FIVE (cname, pwd)
values ('小陈', '1');
insert into FIVE (cname, pwd)
values ('小赵', '1');
insert into FIVE (cname, pwd)
values ('小王', '1');
insert into FIVE (cname, pwd)
values ('小张', '1');
insert into FIVE (cname, pwd)
values ('小李', '1');
commit;
prompt 5 records loaded
set feedback on
set define on
prompt Done.
