ALTER TABLE account ADD COLUMN password CHARACTER VARYING(255) NOT NULL;

INSERT INTO account (id, username, first_name, last_name, middle_name, password, email, enabled, createby, createddate, version) 
SELECT '0b90130d-2c19-4001-b61a-ef100d7b950e', 'akartkam','Artur','Akchurin', 'Kamilevich',
'$2a$10$0D.1i9QByybz6LOSjFysgubhX0fIMsFL86iyDYTaS63n.Fpn7Gu.W', 'akartkam@gmail.com', true,
'0b90130d-2c19-4001-b61a-ef100d7b950e', Now(), 0
WHERE NOT EXISTS (SELECT id FROM account a WHERE a.id='0b90130d-2c19-4001-b61a-ef100d7b950e');

INSERT INTO role (id, name, role, enabled, createby, createddate, version) 
SELECT '53a5c8ce-4f56-4c79-acb3-e47d76e580df', 'Admin','ADMIN',true, '0b90130d-2c19-4001-b61a-ef100d7b950e', Now(), 0
WHERE NOT EXISTS (SELECT id FROM role a WHERE a.id='53a5c8ce-4f56-4c79-acb3-e47d76e580df');

INSERT INTO role (id, name, role, enabled, createby, createddate, version) 
SELECT 'c147c790-454a-4b43-b592-73367939c6cf', 'User','USER',true, '0b90130d-2c19-4001-b61a-ef100d7b950e', Now(), 0
WHERE NOT EXISTS (SELECT id FROM role a WHERE a.id='c147c790-454a-4b43-b592-73367939c6cf');

INSERT INTO role (id, name, role, enabled, createby, createddate, version) 
SELECT 'a45ef259-3880-45de-a052-40070f48d4da', 'Manager','MANAGER',true, '0b90130d-2c19-4001-b61a-ef100d7b950e', Now(), 0
WHERE NOT EXISTS (SELECT id FROM role a WHERE a.id='a45ef259-3880-45de-a052-40070f48d4da');

INSERT INTO account_role (account_id, role_id) SELECT '0b90130d-2c19-4001-b61a-ef100d7b950e', '53a5c8ce-4f56-4c79-acb3-e47d76e580df'
WHERE NOT EXISTS(SELECT account_id, role_id FROM account_role ar WHERE ar.account_id = '0b90130d-2c19-4001-b61a-ef100d7b950e' AND
role_id = '53a5c8ce-4f56-4c79-acb3-e47d76e580df');


CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
DELETE FROM category WHERE name LIKE 'Test_%';
DO $$
	DECLARE uuid_root1 uuid;
	DECLARE uuid_root2 uuid;
	DECLARE uuid_root3 uuid;
BEGIN
	uuid_root1 := uuid_generate_v4();
	uuid_root2 := uuid_generate_v4();
	uuid_root3 := uuid_generate_v4();
	
	INSERT INTO category(
		    id, createddate, enabled, updateddate, version, ordering, description, 
		    long_description, name, createby, updatedby, parent_id)
	    VALUES (uuid_root1, current_timestamp, true, null, 0, 1, 'Test description 1', 
		    'Test long description 1', 'Test_Root_Category1', '0b90130d-2c19-4001-b61a-ef100d7b950e', null, null);
	INSERT INTO category(
		    id, createddate, enabled, updateddate, version, ordering, description, 
		    long_description, name, createby, updatedby, parent_id)
	    VALUES (uuid_root2, current_timestamp, true, null, 0, 2, 'Test description 2', 
		    'Test long description 2', 'Test_Root_Category2', '0b90130d-2c19-4001-b61a-ef100d7b950e', null, null);
	INSERT INTO category(
		    id, createddate, enabled, updateddate, version, ordering, description, 
		    long_description, name, createby, updatedby, parent_id)
	    VALUES (uuid_root3, current_timestamp, true, null, 0, 3, 'Test description 3', 
		    'Test long description 3', 'Test_Root_Category3', '0b90130d-2c19-4001-b61a-ef100d7b950e', null, uuid_root1);
	INSERT INTO category(
		    id, createddate, enabled, updateddate, version, ordering, description, 
		    long_description, name, createby, updatedby, parent_id)
	    VALUES (uuid_generate_v4(), current_timestamp, true, null, 0, 4, 'Test description 4', 
		    'Test long description 4', 'Test_Category4', '0b90130d-2c19-4001-b61a-ef100d7b950e', null, uuid_root3);
	INSERT INTO category(
		    id, createddate, enabled, updateddate, version, ordering, description, 
		    long_description, name, createby, updatedby, parent_id)
	    VALUES (uuid_generate_v4(), current_timestamp, true, null, 0, 5, 'Test description 5', 
		    'Test long description 5', 'Test_Category5', '0b90130d-2c19-4001-b61a-ef100d7b950e', null, uuid_root2);
		    
END $$;