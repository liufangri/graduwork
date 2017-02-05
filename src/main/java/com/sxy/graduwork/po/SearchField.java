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
	private char match;
	/**
	 * Field value
	 */
	private String value;

	public char getMatch() {
		return match;
	}

	public void setMatch(char match) {
		this.match = match;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
