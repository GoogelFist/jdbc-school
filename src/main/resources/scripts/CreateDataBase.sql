CREATE DATABASE school;
CREATE USER school_admin WITH PASSWORD '1234';

GRANT ALL ON DATABASE school TO school_admin WITH GRANT OPTION;