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


--CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
--DELETE FROM category WHERE name LIKE 'Test_%';
--DO $$
--	DECLARE uuid_root1 uuid;
--	DECLARE uuid_root2 uuid;
--	DECLARE uuid_root3 uuid;
--BEGIN
--	uuid_root1 := uuid_generate_v4();
--	uuid_root2 := uuid_generate_v4();
--	uuid_root3 := uuid_generate_v4();
	
	INSERT INTO category(
		    id, createddate, enabled, updateddate, version, ordering, description, 
		    long_description, name, createby, updatedby, parent_id)
	    SELECT '786a9240-aa8a-4ff0-9684-ed55963d1a89', current_timestamp, true, null, 0, 1, 'Test description 1', 
		    'Test long description 1', 'Test_Root_Category1', '0b90130d-2c19-4001-b61a-ef100d7b950e', null, null
		WHERE NOT EXISTS (SELECT id FROM category WHERE id = '786a9240-aa8a-4ff0-9684-ed55963d1a89');		    
	INSERT INTO category(
		    id, createddate, enabled, updateddate, version, ordering, description, 
		    long_description, name, createby, updatedby, parent_id)
	    SELECT '425c4eb6-f2bb-4b0d-a5a6-c4bc46c523cf', current_timestamp, true, null, 0, 2, 'Test description 2', 
		    'Test long description 2', 'Test_Root_Category2', '0b90130d-2c19-4001-b61a-ef100d7b950e', null, null
		WHERE NOT EXISTS (SELECT id FROM category WHERE id = '425c4eb6-f2bb-4b0d-a5a6-c4bc46c523cf');		    
	INSERT INTO category(
		    id, createddate, enabled, updateddate, version, ordering, description, 
		    long_description, name, createby, updatedby, parent_id)
	    SELECT '5d5d723c-8202-4c69-b67c-b6b8587e7228', current_timestamp, true, null, 0, 3, 'Test description 3', 
		    'Test long description 3', 'Test_Root_Category3', '0b90130d-2c19-4001-b61a-ef100d7b950e', null, '786a9240-aa8a-4ff0-9684-ed55963d1a89'
		WHERE NOT EXISTS (SELECT id FROM category WHERE id = '5d5d723c-8202-4c69-b67c-b6b8587e7228');		    
	INSERT INTO category(
		    id, createddate, enabled, updateddate, version, ordering, description, 
		    long_description, name, createby, updatedby, parent_id)
	    SELECT 'a6d26eb4-5f63-4482-8293-cb1de9ab4d7a', current_timestamp, true, null, 0, 4, 'Test description 4', 
		    'Test long description 4', 'Test_Category4', '0b90130d-2c19-4001-b61a-ef100d7b950e', null, '5d5d723c-8202-4c69-b67c-b6b8587e7228'
		WHERE NOT EXISTS (SELECT id FROM category WHERE id = 'a6d26eb4-5f63-4482-8293-cb1de9ab4d7a');		    
	INSERT INTO category(
		    id, createddate, enabled, updateddate, version, ordering, description, 
		    long_description, name, createby, updatedby, parent_id)
	    SELECT 'ed752562-3c39-4a74-9cf6-679c8689a5e9', current_timestamp, true, null, 0, 5, 'Test description 5', 
		    'Test long description 5', 'Test_Category5', '0b90130d-2c19-4001-b61a-ef100d7b950e', null, '425c4eb6-f2bb-4b0d-a5a6-c4bc46c523cf'
		WHERE NOT EXISTS (SELECT id FROM category WHERE id = 'ed752562-3c39-4a74-9cf6-679c8689a5e9');		    
		    
--END $$;
		
		
INSERT INTO product(
            id, createddate, enabled, updateddate, version, ordering, manuf, 
            model, name, createby, updatedby, category_id)
    SELECT '72763e92-6bb0-41ad-a8c6-d276fed557a1', current_timestamp, true, null, 0, 0, 'Test_manuf1', 
            'Test_model1', 'Test_product1', '0b90130d-2c19-4001-b61a-ef100d7b950e',
            null, 'a6d26eb4-5f63-4482-8293-cb1de9ab4d7a'
    WHERE NOT EXISTS (SELECT id FROM product WHERE id = '72763e92-6bb0-41ad-a8c6-d276fed557a1');
INSERT INTO product(
            id, createddate, enabled, updateddate, version, ordering, manuf, 
            model, name, createby, updatedby, category_id)
    SELECT 'ea1a985e-31d2-45cc-8cd4-6ab2926e87d3', current_timestamp, true, null, 0, 0, 'Test_manuf2', 
            'Test_model2', 'Test_product2', '0b90130d-2c19-4001-b61a-ef100d7b950e',
            null, 'a6d26eb4-5f63-4482-8293-cb1de9ab4d7a'
    WHERE NOT EXISTS (SELECT id FROM product WHERE id = 'ea1a985e-31d2-45cc-8cd4-6ab2926e87d3');
INSERT INTO product(
            id, createddate, enabled, updateddate, version, ordering, manuf, 
            model, name, createby, updatedby, category_id)
    SELECT 'd8521941-8c83-430b-8a97-4bd89249d67a', current_timestamp, true, null, 0, 0, 'Test_manuf3', 
            'Test_model3', 'Test_product3', '0b90130d-2c19-4001-b61a-ef100d7b950e',
            null, 'ed752562-3c39-4a74-9cf6-679c8689a5e9'
    WHERE NOT EXISTS (SELECT id FROM product WHERE id = 'd8521941-8c83-430b-8a97-4bd89249d67a');
INSERT INTO product(
            id, createddate, enabled, updateddate, version, ordering, manuf, 
            model, name, createby, updatedby, category_id)
    SELECT '2520f69e-6679-480b-b145-a1e9701ccdec', current_timestamp, true, null, 0, 0, 'Test_manuf4', 
            'Test_model4', 'Test_product4', '0b90130d-2c19-4001-b61a-ef100d7b950e',
            null, 'ed752562-3c39-4a74-9cf6-679c8689a5e9'
    WHERE NOT EXISTS (SELECT id FROM product WHERE id = '2520f69e-6679-480b-b145-a1e9701ccdec'); 		