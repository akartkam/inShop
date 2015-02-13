-- Function: del_attribute(character varying)

--DROP FUNCTION del_attribute(character varying);

CREATE OR REPLACE FUNCTION del_attribute(attribute_name character varying)
  RETURNS void AS
$BODY$
DECLARE attribute_rec RECORD;
DECLARE attribute_value_rec RECORD;
BEGIN
  FOR attribute_rec IN SELECT * FROM attribute WHERE name ~* attribute_name LOOP
	FOR attribute_value_rec IN SELECT * FROM attribute_value WHERE attribute_id = attribute_rec.id LOOP
		delete from attribute_decimal_value where id = attribute_value_rec.id;
		delete from attribute_string_value where id = attribute_value_rec.id;
		delete from attribute_decimal_value where id = attribute_value_rec.id;
	END LOOP;
	delete from attribute_value WHERE attribute_id = attribute_rec.id;
  END LOOP;
  delete from attribute WHERE name ~* attribute_name;
END
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION del_attribute(character varying)
  OWNER TO postgres;

  
-- Function: del_product(character varying)

-- DROP FUNCTION del_product(character varying);

CREATE OR REPLACE FUNCTION del_product(product_name character varying)
  RETURNS void AS
$BODY$
DECLARE product_rec RECORD;
DECLARE attribute_value_rec RECORD;
BEGIN
  FOR product_rec IN SELECT * FROM product WHERE name ~* product_name LOOP
	FOR attribute_value_rec IN SELECT * FROM attribute_value WHERE product_id = product_rec.id LOOP
		delete from attribute_decimal_value where id = attribute_value_rec.id;
		delete from attribute_string_value where id = attribute_value_rec.id;
		delete from attribute_decimal_value where id = attribute_value_rec.id;
	END LOOP;
	delete from attribute_value WHERE product_id = product_rec.id;
  END LOOP;
  delete from product WHERE name ~* product_name;
END
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION del_product(character varying)
  OWNER TO postgres;
