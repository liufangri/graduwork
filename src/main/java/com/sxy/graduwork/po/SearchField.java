package com.sxy.graduwork.po;

/**
 * Contains some information about query field
 * 
 * @author Y400
 *
 */
public class SearchField {
	/**
	 * How to match. AND(+) NOT(-)
	 */
	private String match;
	/**
	 * Field value
	 */
	private String value;

	public String getMatch() {
		return match;
	}

	public void setMatch(String match) {
		this.match = match;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
