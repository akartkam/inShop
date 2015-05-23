package com.akartkam.inShop.service.product;

import java.util.List;
import java.util.UUID;

import com.akartkam.inShop.domain.product.option.ProductOption;

public interface ProductService {
	ProductOption createPO(ProductOption po);
	List<ProductOption> getAllPO();
	ProductOption getPOById(UUID id);
	ProductOption clonePOById(UUID id) throws CloneNotSupportedException;
	ProductOption loadPOById(UUID id, Boolean lock);
	void softDeletePOById(UUID id);
	void deletePO(ProductOption po);
	void deletePOById(UUID id);
	void updatePO(ProductOption po);
	void mergeWithExistingPOAndUpdateOrCreate(final ProductOption po);
}
