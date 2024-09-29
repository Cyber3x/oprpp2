connect 'jdbc:derby://localhost:1527/blogBaza;user=sa;password=sapwd22;create=true';
CALL SYSCS_UTIL.SYSCS_CREATE_USER('blogDBAdmin', 'blogDBPassword');
CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY('derby.database.fullAccessUsers', 'sa,blogDBAdmin');
disconnect;

