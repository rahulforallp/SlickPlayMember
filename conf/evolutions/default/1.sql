CREATE TABLE member (username VARCHAR(200) PRIMARY KEY,password VARCHAR(200),usertype VARCHAR(200) DEFAULT 'user');
CREATE TABLE award(id INT PRIMARY KEY,username VARCHAR(200),serlno VARCHAR(25),name VARCHAR(200),description VARCHAR(200),year VARCHAR(5));
