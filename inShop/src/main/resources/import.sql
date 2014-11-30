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
role_id = '53a5c8ce-4f56-4c79-acb3-e47d76e580df')