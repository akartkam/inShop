ALTER TABLE account ADD COLUMN password CHARACTER VARYING(255) NOT NULL;

INSERT INTO account (id, username, first_name, last_name, middle_name, password, email, enabled, createby, createddate, version) 
SELECT '0b90130d-2c19-4001-b61a-ef100d7b950e', 'akartkam','Artur','Akchurin', 'Kamilevich',
'$2a$10$0D.1i9QByybz6LOSjFysgubhX0fIMsFL86iyDYTaS63n.Fpn7Gu.W', 'akartkam@gmail.com', true,
'0b90130d-2c19-4001-b61a-ef100d7b950e', Now(), 0
WHERE NOT EXISTS (SELECT id FROM account a WHERE a.id='0b90130d-2c19-4001-b61a-ef100d7b950e');