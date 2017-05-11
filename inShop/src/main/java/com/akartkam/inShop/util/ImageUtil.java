package com.akartkam.inShop.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;

import com.akartkam.inShop.exception.ImageUploadException;


public class ImageUtil {
	
	private static final Log LOG = LogFactory.getLog(ImageUtil.class);
	
	private String imagesMaxUploadSize;
	
	public String getImagesMaxUploadSize() {
		return imagesMaxUploadSize;
	}

	public void setImagesMaxUploadSize(String imagesMaxUploadSize) {
		this.imagesMaxUploadSize = imagesMaxUploadSize;
	}

	public void validateImage(MultipartFile image, String fieldName, Errors errors) {
		String allowedFileType = "image/jpeg,image/png,image/gif";
		if (image == null || image.isEmpty() || (allowedFileType.indexOf(image.getContentType()) < 0)) {
			errors.rejectValue(fieldName, "error.image.contentType");
	    } else {
		long size = Long.parseLong(imagesMaxUploadSize);
		if (image.getSize() > size) {
			errors.rejectValue(fieldName, "error.image.size", new String[] {imagesMaxUploadSize}, null);		
		}
	    }
	}
	
	public void saveImage(String filePath, MultipartFile image)
			throws ImageUploadException {
		try {
			Path fPath = Paths.get(filePath);
			Files.write(fPath, image.getBytes());

/*			String newFName = new String(filePath.getBytes(), "ISO-8859-2");
			File file = new File(newFName);
			FileUtils.writeByteArrayToFile(file, );
			file.setReadable(true, false);
*/
		} catch (Exception e) {
			LOG.error("Error during put image "+filePath, e);
		}
	}	   


}
