package com.akartkam.inShop.service.common.media;

import java.util.List;

import org.hibernate.envers.Audited;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.akartkam.inShop.dao.common.MediaDAO;
import com.akartkam.inShop.domain.common.media.Media;

@Service("MediaService")
@Transactional(readOnly = true)
public class MediaServiceImpl implements MediaService {

	@Autowired
	private MediaDAO mediaDAO;  
	
	@Override
	public List<Media> getAllMedia() {
		return mediaDAO.list();
	}

}
