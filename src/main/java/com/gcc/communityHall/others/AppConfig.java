package com.gcc.communityHall.others;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
public class AppConfig {

    private String title;
    private String baseUrl;
    private String getOtpUrl;
    private String erpUrl;
    private String assetURL;
    private String manualRefundCode;
    private String commErpUrl;

    private String fullUrl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getGetOtpUrl() {
        return getOtpUrl;
    }

    public void setGetOtpUrl(String getOtpUrl) {
        this.getOtpUrl = getOtpUrl;
    }

    public String getErpUrl() {
        return erpUrl;
    }

    public void setErpUrl(String erpUrl) {
        this.erpUrl = erpUrl;
    }

	public String getAssetURL() {
		return assetURL;
	}

	public void setAssetURL(String assetURL) {
		this.assetURL = assetURL;
	}

	public String getManualRefundCode() {
		return manualRefundCode;
	}

	public void setManualRefundCode(String manualRefundCode) {
		this.manualRefundCode = manualRefundCode;
	}

    public String getFullUrl() {
        return fullUrl;
    }

    public void setFullUrl(String fullUrl) {
        this.fullUrl = fullUrl;
    }

    public String getCommErpUrl() {
        return commErpUrl;
    }

    public void setCommErpUrl(String commErpUrl) {
        this.commErpUrl = commErpUrl;
    }
}
