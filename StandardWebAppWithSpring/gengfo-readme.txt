1. task aim
	1.1 to setup the standard project to deploy basic servlet to weblogic

2. create maven project as:
	mvn archetype:generate -DarchetypeGroupId=org.apache.maven.archetypes -DgroupId=gengfo.test -DartifactId=StandardWebAppByServlet

3. eclipse workspace created in  
	C:\GengFo\GitHubWs\WeblogicPrj_StandardWebAppByServlet

4. update build in build dir



---create oracle tables ---
-- Create table
create table EMPDEMO
(
  EMPID VARCHAR2(20),
  NAME  VARCHAR2(20),
  DEPT  VARCHAR2(20)
)
tablespace AR_DATA
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    minextents 1
    maxextents unlimited
  );
  
