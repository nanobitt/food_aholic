package com.foodaholic.items;

public class ItemAbout {

 	private String app_name, app_logo, app_desc, app_version, author, contact, email, website, facebook_link, privacy, terms_and_conditions, developedby;

	public ItemAbout(String app_name, String app_logo, String app_desc, String app_version, String author, String contact, String email, String website, String privacy, String terms_and_conditions, String developedby, String facebook_link) {
		this.app_name = app_name;
		this.app_logo = app_logo;
		this.app_desc = app_desc;
		this.app_version = app_version;
		this.author = author;
		this.contact = contact;
		this.email = email;
		this.website = website;
		this.privacy = privacy;
		this.terms_and_conditions = terms_and_conditions;
		this.developedby = developedby;
		this.facebook_link = facebook_link;
	}

	public String getAppName() {
		return app_name;
	}
	 
	public String getAppLogo() {
		return app_logo;
	}

	public String getAppDesc() {
		return app_desc;
	}

	public String getAppVersion() {
		return app_version;
	}

	public String getAuthor() {
		return author;
	}

	public String getContact() {
		return contact;
	}

	public String getEmail() {
		return email;
	}

	public String getWebsite() {
		return website;
	}

	public String getPrivacy() {
		return privacy;
	}

	public String getDevelopedby() {
		return developedby;
	}

	public String getTerms_and_conditions() {
		return terms_and_conditions;
	}

	public String getFacebook_link() {
		return facebook_link;
	}
}
