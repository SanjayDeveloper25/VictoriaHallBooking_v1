package com.gcc.communityHall.configuration;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.gcc.communityHall.component.UserSessionInterceptor;
import com.gcc.communityHall.entity.ReasonMaster;
import com.gcc.communityHall.repository.ReasonMasterRepository;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	private final String uploadDirectory;
	private final String hallImageUploadDirectory;
	private final String ddImageUploadDirectory;
	private final String settlementUploadDirectory;

	private final String counterRefundUploadDirectory;

	@Autowired
	private ReasonMasterRepository reasonMasterRepository;

	public ResourceHandlerRegistry registryHandler;

	@Autowired
	public WebConfig(@Value("${file.upload.directory}") String uploadDirectory,
			@Value("${file.hall.upload.directory}") String hallImageUploadDirectory,
			@Value("${file.dd.upload.directory}") String ddImageUploadDirectory,
					 @Value("${file.counterrefund.upload.directory}")String counterRefundUploadDirectory,
					 @Value("${file.settlement.upload.directory}")String settlementUploadDirectory) {
		this.hallImageUploadDirectory = hallImageUploadDirectory;
		this.uploadDirectory = uploadDirectory;
		this.ddImageUploadDirectory = ddImageUploadDirectory;
		this.settlementUploadDirectory = settlementUploadDirectory;
		this.counterRefundUploadDirectory=counterRefundUploadDirectory;
	}

	@Autowired
	private UserSessionInterceptor userSessionInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(userSessionInterceptor);
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		//System.out.println(uploadDirectory);
		File f = new File(uploadDirectory);
		File imgDir = new File(hallImageUploadDirectory);
		File ddDir = new File(ddImageUploadDirectory);
		File crDir = new File(counterRefundUploadDirectory);
		File smDir = new File(settlementUploadDirectory);
		List<ReasonMaster> reasonMasters = reasonMasterRepository.findAll();
		registryHandler = registry;
		if (!f.exists()) {
			f.mkdirs();
			if (!imgDir.exists()) {
				imgDir.mkdirs();
			}
			if (!ddDir.exists()) {
	            ddDir.mkdirs();
	        }
			if (!crDir.exists()) {
				crDir.mkdirs();
			}
			if (!smDir.exists()) {
				smDir.mkdirs();
			}
			

		} else if (!imgDir.exists()) {
			imgDir.mkdirs();
		}
		else if (!ddDir.exists()) {
            ddDir.mkdirs();
        }
		else if (!crDir.exists()) {
			crDir.mkdirs();
		}
		else if (!smDir.exists()) {
			smDir.mkdirs();
		}
		File dam = new File(uploadDirectory + "/damage");
		if (!dam.exists()) {
			dam.mkdirs();
		}
		File noc = new File(uploadDirectory + "/NOC");
		if (!noc.exists()) {
			noc.mkdirs();
		}
		File eb = new File(uploadDirectory + "/eb");
		if (!eb.exists()) {
			eb.mkdirs();
		}
		File bd = new File(uploadDirectory + "/gccblockingdocs");
		if (!bd.exists()) {
			bd.mkdirs();
		}

		File profile = new File(uploadDirectory + "/userPhoto");
		if (!profile.exists()) {
			profile.mkdirs();
		}

		for (ReasonMaster reason : reasonMasters) {
			File sf = new File(uploadDirectory + "/" + reason.getReason());
			if (!sf.exists()) {
				sf.mkdirs();
			}
			registry.addResourceHandler("/images/" + reason.getReason() + "/**").addResourceLocations("file:" + uploadDirectory + "/" + reason.getReason() + "/");
		}
//		registry.addResourceHandler("/images/hall/**").addResourceLocations("file:" + hallImageUploadDirectory + "/");
//		// Add resource handler for dd images
//        registry.addResourceHandler("/images/dd/**").addResourceLocations("file:" + ddImageUploadDirectory + "/");
//        registry.addResourceHandler("/images/cr/**").addResourceLocations("file:" + counterRefundUploadDirectory + "/");
//
//		registry.addResourceHandler("/images/damage/**").addResourceLocations("file:" + uploadDirectory + "/damage/");
//		registry.addResourceHandler("/images/userPhoto/**").addResourceLocations("file:" + uploadDirectory + "/userPhoto/");
//		registry.addResourceHandler("/images/challonDetails/**").addResourceLocations("file:" + settlementUploadDirectory + "/");
		
		registry.addResourceHandler("/images/hallImages/**").addResourceLocations("file:" + hallImageUploadDirectory + "/");
		// Add resource handler for dd images
        registry.addResourceHandler("/images/ddImages/**").addResourceLocations("file:" + ddImageUploadDirectory + "/");
        registry.addResourceHandler("/images/counterrefundImages/**").addResourceLocations("file:" + counterRefundUploadDirectory + "/");
        registry.addResourceHandler("/images/NOC/**").addResourceLocations("file:" + uploadDirectory + "/NOC/");
		registry.addResourceHandler("/images/damage/**").addResourceLocations("file:" + uploadDirectory + "/damage/");
		registry.addResourceHandler("/images/eb/**").addResourceLocations("file:" + uploadDirectory + "/eb/");
		registry.addResourceHandler("/images/gccblockingdocs/**").addResourceLocations("file:" + uploadDirectory + "/gccblockingdocs/");
		registry.addResourceHandler("/images/userPhoto/**").addResourceLocations("file:" + uploadDirectory + "/userPhoto/");
		registry.addResourceHandler("/images/challonDetails/**").addResourceLocations("file:" + settlementUploadDirectory + "/");
		
	}

}