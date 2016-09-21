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
/*Categorys*/	
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
INSERT INTO category(
        id, createby, createddate, enabled, updatedby, updateddate, version, 
        ordering, description, long_description, name, url, parent_id)
	VALUES('1f0f64ac-8b38-4e6c-888c-60f7e20c9e83','akartkam',current_timestamp,TRUE,null,null,0,0,null,null,'Аудио','/audio',null);
INSERT INTO category(
        id, createby, createddate, enabled, updatedby, updateddate, version, 
        ordering, description, long_description, name, url, parent_id)
	VALUES('b2b0d75c-9dfb-45ee-ada2-a61d7f890780','akartkam',current_timestamp,TRUE,null,null,0,0,null,null,'Наушники','/headphones','1f0f64ac-8b38-4e6c-888c-60f7e20c9e83');	
/*Brands*/
INSERT INTO brand(
            id, createby, createddate, enabled, updatedby, updateddate, version, 
            description, logo_url, name, url)
    VALUES ('46d7e8ae-fba7-4ace-b1f9-7c1de7a6e9e6','akartkam',current_timestamp,TRUE,null,null,0,null,'/images/sony.png','Sony','/sony');
INSERT INTO brand(
            id, createby, createddate, enabled, updatedby, updateddate, version, 
            description, logo_url, name, url)
    VALUES ('4e5399f4-3f4a-46d4-8b45-0b2740538bad','akartkam',current_timestamp,TRUE,null,null,0,null,'/images/panasonic.png','Panasonic','/panasonic');
INSERT INTO brand(
            id, createby, createddate, enabled, updatedby, updateddate, version, 
            description, logo_url, name, url)
    VALUES ('5f5df595-4ed2-420d-a7f6-072f963d2bed','akartkam',current_timestamp,TRUE,'akartkam',current_timestamp,1,null,'/images/samsung.png','Samsung','/samsung');
INSERT INTO brand(
            id, createby, createddate, enabled, updatedby, updateddate, version, 
            description, logo_url, name, url)
    VALUES ('81f351c2-e59f-4d2d-b9a3-60127b3a6c2a','akartkam',current_timestamp,TRUE,null,null,0,null,'/images/Beats.jpg','Beats','/beats');
/*Skus*/
INSERT INTO sku(
            id, createby, createddate, enabled, updatedby, updateddate, version, 
            ordering, active_end_date, active_start_date, code, cost_price, 
            description, inventory_type, long_description, name, retail_price, 
            sale_price, product_id, quantity_avable)
    VALUES ('4ea061cf-dbaf-485b-873d-c92924e3c4f2','akartkam',current_timestamp,TRUE,'akartkam',current_timestamp,3,0,null,null,null,28300.00000,'Используются для микширования во всех крупных студиях.','ALWAYS_AVAILABLE','<ul><li>Чистые верхи и глубокие низы</li><li>Вращающиеся чаши</li><li>Надежный корпус и мягкое оголовье для длительной работы в студии</li><li>Соединитель DaisyChain™ с двумя аудиовыходами для совместного прослушивания</li><li>Принимайте звонки и управляйте музыкой с кабелем RemoteTalk™*</li><li>(*Совместим с iOS устройствами. Набор поддерживаемых функций варьируется в зависимости от устройства.)</li></ul>','Beats Pro',32500.00000,null,null,null);
INSERT INTO sku(
            id, createby, createddate, enabled, updatedby, updateddate, version, 
            ordering, active_end_date, active_start_date, code, cost_price, 
            description, inventory_type, long_description, name, retail_price, 
            sale_price, product_id, quantity_avable)
    VALUES ('52f402e0-9963-4df1-a163-bb4950ce232d','akartkam',current_timestamp,TRUE,'akartkam',current_timestamp,5,0,null,null,null,320.00000,'Наушники-клипсы',null,'<ul ><li>Сверхтонкий корпус (9,9 мм).</li><li>30-мм динамическая головка для мощного звука.</li><li>Эргономичная ушная дужка для надежной и удобной фиксации.</li><li>Красивая глянцевая отделка Deluxe.</li><li>Частотный диапазон 20 Гц – 20 кГц.</li></ul>','Panasonic RP-HS46',450.00000,null,null,null);
INSERT INTO sku(
            id, createby, createddate, enabled, updatedby, updateddate, version, 
            ordering, active_end_date, active_start_date, code, cost_price, 
            description, inventory_type, long_description, name, retail_price, 
            sale_price, product_id, quantity_avable)
    VALUES ('8311a588-5df7-435a-9dc9-0b4b538a7eb0','akartkam',current_timestamp,TRUE,'akartkam',current_timestamp,6,0,null,null,null,1000.00000,'Оснащенные мембраной Balanced Armature наушники-вкладыши весом 6 г с функцией цифрового шумоподавления',null,'<span>Арматурные наушники премиум-класса – истинное наслаждение для каждого фаната хорошего звука. Тем более, Sony XBA-NC85D прекрасно подавляет внешние шумы – 97,5 процента посторонних звуков просто-напросто не дойдут до ваших ушей. За систему цифрового звукоподавления отвечают процессоры, встроенные прямо в деку наушников. За чистоту звука отвечает и качественный медный провод, передающий музыку без искажений. Характеристики закрытых наушников также не подкачали – диапазон частот 5-20000 Гц, сопротивление 820 Ом и чувствительность 106 дБ обеспечивают отличную акустическую картину. Для подключения используется 1,5-метровый кабель со штекером 3,5 мм.</span>','Sony XBA-NC85D',1200.00000,null,null,null);
INSERT INTO sku(
            id, createby, createddate, enabled, updatedby, updateddate, version, 
            ordering, active_end_date, active_start_date, code, cost_price, 
            description, inventory_type, long_description, name, retail_price, 
            sale_price, product_id, quantity_avable)
    VALUES ('baa4e1a2-a539-4296-9da6-6a66feac173c','akartkam',current_timestamp,TRUE,'akartkam',current_timestamp,1,0,null,null,null,21500.00000,'Полностью обновленный взгляд на легенду',null,'<ul ><li>Двойное адаптивное шумоподавление, чтобы исключить все лишнее</li><li>Легендарное звучание Beats</li><li>Надежные и складные</li><li>Аккумулятор, рассчитанный на 20 часов работы, и индикатор уровня заряда</li><li>Принимайте звонки и управляйте музыкой с кабелем RemoteTalk™*</li><li>(*Совместим с iOS устройствами. Набор поддерживаемых функций варьируется в зависимости от устройства.)</li></ul>','Beats Studio',26800.00000,null,null,null);
INSERT INTO sku(
            id, createby, createddate, enabled, updatedby, updateddate, version, 
            ordering, active_end_date, active_start_date, code, cost_price, 
            description, inventory_type, long_description, name, retail_price, 
            sale_price, product_id, quantity_avable)
    VALUES ('de203b2a-8f93-4094-a103-033b2186e32d','akartkam',current_timestamp,TRUE,'akartkam',current_timestamp,6,0,null,null,'189076-99',1690.00000,'Отличное предложение для любителей хорошего звука',null,'<p>Мониторные наушники Panasonic RP-HX550 – это идеальное звучание и невероятно стильный дизайн. Модель выполнена в трех вариантах расцветок: помимо классического черного, это элегантный белый и изысканный золотой. Обратите внимание: провод сделан в той же цветовой гамме, что и наушники, что придает Panasonic RP-HX550 еще более привлекательный вид.</p><p>В конструкции использованы вставки из настоящего метала, поэтому наушники RP-HX550 – не только красивые, но и прочные. Мягкое оголовье легко регулируется по размеру головы, провод оптимальной длины в 1,2 метра крепится только к одной, левой чашечке, что создает дополнительные удобства при прослушивании музыки. Частотный диапазон 8-26(Гц – кГц) и размер динамиков 40 мм – залог чистого и мощного звука.</p>','Panasonic RP-HX550',4190.00000,3190.00000,null,null);
    
    
/*Products*/
INSERT INTO product(
            id, createby, createddate, enabled, updatedby, updateddate, version, 
            ordering, can_sell_without_options, model, url, brand_id, category_id, 
            default_sku_id)
    VALUES ('159c5c82-3b8f-4473-a965-046604f8e56d','akartkam',current_timestamp,TRUE,null,null,0,1,TRUE,'RP-HX550','/panasonic-rp-hx550','4e5399f4-3f4a-46d4-8b45-0b2740538bad','b2b0d75c-9dfb-45ee-ada2-a61d7f890780','de203b2a-8f93-4094-a103-033b2186e32d');
INSERT INTO product(
            id, createby, createddate, enabled, updatedby, updateddate, version, 
            ordering, can_sell_without_options, model, url, brand_id, category_id, 
            default_sku_id)
    VALUES ('75881a47-1847-454a-9bf0-71b597d0db75','akartkam',current_timestamp,TRUE,null,null,0,3,FALSE,null,'/beats-pro','81f351c2-e59f-4d2d-b9a3-60127b3a6c2a','b2b0d75c-9dfb-45ee-ada2-a61d7f890780','4ea061cf-dbaf-485b-873d-c92924e3c4f2');
INSERT INTO product(
            id, createby, createddate, enabled, updatedby, updateddate, version, 
            ordering, can_sell_without_options, model, url, brand_id, category_id, 
            default_sku_id)
    VALUES ('cf1749e9-daf3-4df1-8b96-c6675cb5cbd8','akartkam',current_timestamp,TRUE,'akartkam',current_timestamp,1,4,FALSE,null,'/beats-studio','81f351c2-e59f-4d2d-b9a3-60127b3a6c2a','b2b0d75c-9dfb-45ee-ada2-a61d7f890780','baa4e1a2-a539-4296-9da6-6a66feac173c');
INSERT INTO product(
            id, createby, createddate, enabled, updatedby, updateddate, version, 
            ordering, can_sell_without_options, model, url, brand_id, category_id, 
            default_sku_id)
    VALUES ('e55c788a-f9a5-467e-a975-c0b3a2736634','akartkam',current_timestamp,TRUE,'akartkam',current_timestamp,2,0,TRUE,'XBA-NC85D','/sony-xba-nc85d','46d7e8ae-fba7-4ace-b1f9-7c1de7a6e9e6','b2b0d75c-9dfb-45ee-ada2-a61d7f890780','8311a588-5df7-435a-9dc9-0b4b538a7eb0');
INSERT INTO product(
            id, createby, createddate, enabled, updatedby, updateddate, version, 
            ordering, can_sell_without_options, model, url, brand_id, category_id, 
            default_sku_id)
    VALUES ('ee4c9e31-1b06-44cf-b08c-b2dc548a9338','akartkam',current_timestamp,TRUE,'akartkam',current_timestamp,2,2,FALSE,'RP-HS46','/panasonic-rp-hs46','4e5399f4-3f4a-46d4-8b45-0b2740538bad','b2b0d75c-9dfb-45ee-ada2-a61d7f890780','52f402e0-9963-4df1-a163-bb4950ce232d');

/*rest skus*/    
INSERT INTO sku(
            id, createby, createddate, enabled, updatedby, updateddate, version, 
            ordering, active_end_date, active_start_date, code, cost_price, 
            description, inventory_type, long_description, name, retail_price, 
            sale_price, product_id, quantity_avable)
    VALUES ('045244d2-aa82-4204-a1b4-4397377c0108','akartkam',current_timestamp,TRUE,'akartkam',current_timestamp,1,1,null,null,null,null,null,null,null,'Panasonic RP-HS46',450.00000,null,'ee4c9e31-1b06-44cf-b08c-b2dc548a9338',null);
INSERT INTO sku(
            id, createby, createddate, enabled, updatedby, updateddate, version, 
            ordering, active_end_date, active_start_date, code, cost_price, 
            description, inventory_type, long_description, name, retail_price, 
            sale_price, product_id, quantity_avable)
    VALUES ('0fa89e22-46e1-4b68-acb9-d5df82f37823','akartkam',current_timestamp,TRUE,'akartkam',current_timestamp,3,0,null,null,'189078-99',null,null,null,null,'Panasonic RP-HX550',4190.00000,3190.00000,'159c5c82-3b8f-4473-a965-046604f8e56d',1);
INSERT INTO sku(
            id, createby, createddate, enabled, updatedby, updateddate, version, 
            ordering, active_end_date, active_start_date, code, cost_price, 
            description, inventory_type, long_description, name, retail_price, 
            sale_price, product_id, quantity_avable)
    VALUES ('1a0b5c8f-5b71-4b67-a261-0de3f4d9de7f','akartkam',current_timestamp,TRUE,'akartkam',current_timestamp,2,2,null,null,null,null,null,null,null,'Panasonic RP-HX550',4190.00000,3190.00000,'159c5c82-3b8f-4473-a965-046604f8e56d',1);
INSERT INTO sku(
            id, createby, createddate, enabled, updatedby, updateddate, version, 
            ordering, active_end_date, active_start_date, code, cost_price, 
            description, inventory_type, long_description, name, retail_price, 
            sale_price, product_id, quantity_avable)
    VALUES ('4855a98a-e9d0-4da8-aa8d-6543abb8ecdf','akartkam',current_timestamp,TRUE,null,null,0,0,null,null,null,null,null,null,null,'Beats Pro',32500.00000,null,'75881a47-1847-454a-9bf0-71b597d0db75',null);
INSERT INTO sku(
            id, createby, createddate, enabled, updatedby, updateddate, version, 
            ordering, active_end_date, active_start_date, code, cost_price, 
            description, inventory_type, long_description, name, retail_price, 
            sale_price, product_id, quantity_avable)
    VALUES ('4c080065-250d-4386-876c-fc0bf6c71e52','akartkam',current_timestamp,TRUE,'akartkam',current_timestamp,4,1,null,null,null,null,null,null,null,'Beats Pro',32500.00000,31500.00000,'75881a47-1847-454a-9bf0-71b597d0db75',null);
INSERT INTO sku(
            id, createby, createddate, enabled, updatedby, updateddate, version, 
            ordering, active_end_date, active_start_date, code, cost_price, 
            description, inventory_type, long_description, name, retail_price, 
            sale_price, product_id, quantity_avable)
    VALUES ('73a847e9-baa8-4946-b1a4-2741c19178f7','akartkam',current_timestamp,TRUE,'akartkam',current_timestamp,1,2,null,null,null,null,null,null,null,'Beats Studio',26800.00000,null,'cf1749e9-daf3-4df1-8b96-c6675cb5cbd8',null);
INSERT INTO sku(
            id, createby, createddate, enabled, updatedby, updateddate, version, 
            ordering, active_end_date, active_start_date, code, cost_price, 
            description, inventory_type, long_description, name, retail_price, 
            sale_price, product_id, quantity_avable)
    VALUES ('952e14b6-e0d8-4606-897d-a310b677b20f','akartkam',current_timestamp,TRUE,'akartkam',current_timestamp,1,0,null,null,null,null,null,null,null,'Beats Studio',26800.00000,null,'cf1749e9-daf3-4df1-8b96-c6675cb5cbd8',null);
INSERT INTO sku(
            id, createby, createddate, enabled, updatedby, updateddate, version, 
            ordering, active_end_date, active_start_date, code, cost_price, 
            description, inventory_type, long_description, name, retail_price, 
            sale_price, product_id, quantity_avable)
    VALUES ('c640d72a-f94d-46cb-8cf8-9242e04e6813','akartkam',current_timestamp,TRUE,'akartkam',current_timestamp,2,1,null,null,null,null,null,null,null,'Beats Studio',26800.00000,null,'cf1749e9-daf3-4df1-8b96-c6675cb5cbd8',null);
INSERT INTO sku(
            id, createby, createddate, enabled, updatedby, updateddate, version, 
            ordering, active_end_date, active_start_date, code, cost_price, 
            description, inventory_type, long_description, name, retail_price, 
            sale_price, product_id, quantity_avable)
    VALUES ('d6120190-1591-4207-868c-853aea97efa1','akartkam',current_timestamp,TRUE,'akartkam',current_timestamp,4,1,null,null,'189079-99',null,null,null,null,'Panasonic RP-HX550',4190.00000,3190.00000,'159c5c82-3b8f-4473-a965-046604f8e56d',1);
   
