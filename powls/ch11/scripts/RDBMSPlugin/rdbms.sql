drop table CH11_RDBMS_PLUGIN;

create table CH11_RDBMS_PLUGIN (

	USERNAME VARCHAR2(20),
	PASSWORD VARCHAR2(20),
	GROUPS   VARCHAR2(100)  

);

INSERT INTO CH11_RDBMS_PLUGIN VALUES ('someuser','password','CH11ProtectedGroup');
INSERT INTO CH11_RDBMS_PLUGIN VALUES ('someuserother','password','CH11ProtectedGroup:CH11SpecialGroup');

COMMIT;

