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
  
-- Function: sku_before_del()

-- DROP FUNCTION sku_before_del();

CREATE OR REPLACE FUNCTION sku_before_del()
  RETURNS trigger AS
$BODY$
BEGIN 
  IF (OLD.default_product_id IS NOT NULL) THEN
    UPDATE product SET default_sku_id = NULL WHERE id = OLD.default_product_id;
  END IF;
  RETURN OLD;
END; 
 $BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION sku_before_del()
  OWNER TO postgres;
  
-- Trigger: before_del on sku

-- DROP TRIGGER before_del ON sku;

CREATE TRIGGER before_del
  BEFORE DELETE
  ON sku
  FOR EACH ROW
  EXECUTE PROCEDURE sku_before_del(); 
  
  
-- Function: check_attribute_product_according(character varying, character varying, uuid, uuid)

-- DROP FUNCTION check_attribute_product_according(character varying, character varying, uuid, uuid);

CREATE OR REPLACE FUNCTION check_attribute_product_according(attr_name character varying, attr_value character varying, prod_id uuid, def_sku_id uuid)
  RETURNS boolean AS
$BODY$
  DECLARE res boolean;
  DECLARE arrValues varchar[];
BEGIN
  res = False;
  arrValues = string_to_array(attr_value,',');
  SELECT True INTO res FROM sku s  
                 left join attribute_value av on (av.product_id=prod_id or av.sku_id=s.id)  
                 left join attribute_decimal_value adv on adv.id=av.id
                 left join attribute_int_value aiv on aiv.id=av.id
                 left join attribute_slist_value alv on alv.id=av.id
                 left join attribute_string_value asv on asv.id=av.id
                 left join attribute a on a.id=av.attribute_id  
                 left join lnk_sku_option_value lsov on lsov.sku_id=s.id
                 left join product_option_value pov on pov.id=lsov.product_option_value_id  
                 left join product_option po on po.id=pov.productoption_id
                where (s.product_id=prod_id or s.id=def_sku_id) and s.enabled=true and (((trim(a.name) = attr_name and
			(cast(round(cast(adv.attributevalue as numeric), 2) as varchar) = ANY(arrValues) or 
                        cast(aiv.attributevalue as varchar) = ANY(arrValues) or 
                        trim(alv.attributevalue) = ANY(arrValues) or 
                        trim(asv.attributevalue) = ANY(arrValues))                       
                        )) or (trim(po.label)=attr_name and trim(pov.option_value) = ANY(arrValues) ));
  RETURN res;
END
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION check_attribute_product_according(character varying, character varying, uuid, uuid)
  OWNER TO postgres;

  
