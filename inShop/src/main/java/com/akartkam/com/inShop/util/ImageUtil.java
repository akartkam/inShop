package com.akartkam.com.inShop.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;

import com.akartkam.inShop.exception.ImageUploadException;

public class ImageUtil {
	
	static public void validateImage(MultipartFile image, String fieldName, Errors errors) {
		String allowedFileType = "image/jpeg,image/png,image/gif";
		if (image == null || image.isEmpty() || (allowedFileType.indexOf(image.getContentType()) < 0)) {
			errors.rejectValue(fieldName, "error.image.contentType");
		}
	}
	
	static public void saveImage(String filePath, MultipartFile image)
			throws ImageUploadException {
		try {
			
			File file = new File(filePath);
			FileUtils.writeByteArrayToFile(file, image.getBytes());
		} catch (IOException e) {
			throw new ImageUploadException("Unable to save image", e);
		}
	}	   


}
