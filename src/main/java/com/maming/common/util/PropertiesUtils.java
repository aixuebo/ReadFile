package com.maming.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtils {

	private String driverClassName;

	private String commonUrl;
	private String commonUser;
	private String commonPass;
	
	private static PropertiesUtils INSTANCE = new PropertiesUtils();

	public static PropertiesUtils getInstance() {
		return INSTANCE;
	}

	private PropertiesUtils() {
		init();
	}

	private void init() {
		Properties properties = new Properties();
		InputStream is = null;
		try {
			is = PropertiesUtils.class.getClassLoader().getResourceAsStream("application.properties");
			properties.load(is);

			driverClassName = properties.getProperty("jdbc.driverClassName");

			commonUrl = properties.getProperty("jdbc.common.url");
			commonUser = properties.getProperty("jdbc.common.userName");
			commonPass = properties.getProperty("jdbc.common.password");
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public String getDriverClassName() {
		return driverClassName;
	}

	public String getCommonUrl() {
		return commonUrl;
	}

	public void setCommonUrl(String commonUrl) {
		this.commonUrl = commonUrl;
	}

	public String getCommonUser() {
		return commonUser;
	}

	public void setCommonUser(String commonUser) {
		this.commonUser = commonUser;
	}

	public String getCommonPass() {
		return commonPass;
	}

	public void setCommonPass(String commonPass) {
		this.commonPass = commonPass;
	}
	
	public static void main(String[] args) {
		System.out.println(PropertiesUtils.getInstance().getCommonUrl());
	}
}
