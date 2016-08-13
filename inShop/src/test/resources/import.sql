CREATE SEQUENCE order_number_generator;
ALTER SEQUENCE order_number_generator RESTART WITH 1001;

ALTER TABLE account ADD COLUMN password CHARACTER VARYING(255);

INSERT INTO account (id, username, first_name, last_name, middle_name, password, email, enabled, createby, createddate, version) 
    VALUES ('0b90130d-2c19-4001-b61a-ef100d7b950e', 'akartkam','Artur','Akchurin', 'Kamilevich',
			'$2a$10$0D.1i9QByybz6LOSjFysgubhX0fIMsFL86iyDYTaS63n.Fpn7Gu.W', 'akartkam@gmail.com', true,
			'0b90130d-2c19-4001-b61a-ef100d7b950e', current_timestamp, 0)

INSERT INTO role (id, name, role, enabled, createby, createddate, version) 
    VALUES ('53a5c8ce-4f56-4c79-acb3-e47d76e580df', 'Admin','ADMIN',true, '0b90130d-2c19-4001-b61a-ef100d7b950e', current_timestamp, 0);

INSERT INTO account_role (account_id, role_id) 
    VALUES('0b90130d-2c19-4001-b61a-ef100d7b950e', '53a5c8ce-4f56-4c79-acb3-e47d76e580df');
	
INSERT INTO category(
	    id, createddate, enabled, updateddate, version, ordering, description, 
	    long_description, name, createby, updatedby, parent_id)
    VALUES('786a9240-aa8a-4ff0-9684-ed55963d1a89', current_timestamp, true, null, 0, 1, 'Test description 1', 
	    'Test long description 1', 'Test_Root_Category1', '0b90130d-2c19-4001-b61a-ef100d7b950e', null, null);
INSERT INTO category(
	    id, createddate, enabled, updateddate, version, ordering, description, 
	    long_description, name, createby, updatedby, parent_id)
    VALUES('425c4eb6-f2bb-4b0d-a5a6-c4bc46c523cf', current_timestamp, true, null, 0, 2, 'Test description 2', 
	    'Test long description 2', 'Test_Root_Category2', '0b90130d-2c19-4001-b61a-ef100d7b950e', null, null);
INSERT INTO category(
	    id, createddate, enabled, updateddate, version, ordering, description, 
	    long_description, name, createby, updatedby, parent_id)
    VALUES('5d5d723c-8202-4c69-b67c-b6b8587e7228', current_timestamp, true, null, 0, 3, 'Test description 3', 
	    'Test long description 3', 'Test_Category3', '0b90130d-2c19-4001-b61a-ef100d7b950e', null, '786a9240-aa8a-4ff0-9684-ed55963d1a89');
