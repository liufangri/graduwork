package com.sxy.graduwork.searchconfig;

import java.util.List;

import com.sxy.graduwork.po.SearchField;

public class ACMSearchConfig extends BasicSearchConfig {

	public ACMSearchConfig() {

	}

	public ACMSearchConfig(BasicSearchConfig basicSearchConfig) {
		super(basicSearchConfig);
	}

	/**
	 * Get the special query string from this configure
	 * 
	 * @return
	 */
	public String toACMQueryString() {
		StringBuffer sb = new StringBuffer();
		sb.append("");
		if (this.isAnyFieldNotEnmpty()) {
			List<SearchField> fields = this.getAnyField();
			sb.append('(');
			// Loop query conditions to create query string
			appendQuery(sb, fields);
			sb.append(") AND ");
		}

		// author
		if (this.isAuthorNotEnmpty()) {
			sb.append("persons.authors.personName:(");
			List<SearchField> fields = this.getAuthor();
			appendQuery(sb, fields);
			sb.append(") AND ");
		}

		// title
		if (this.isTitleNotEnmpty()) {
			sb.append("acmdlTitle:(");
			List<SearchField> fields = this.getTitle();
			appendQuery(sb, fields);
			sb.append(") AND ");
		}

		// abstract
		if (this.isDigestNotEnmpty()) {
			sb.append("recordAbstract:(");
			List<SearchField> fields = this.getDigest();
			appendQuery(sb, fields);
			sb.append(") AND ");
		}

		String result = sb.toString();
		return result.endsWith(" AND ") ? result.substring(0, result.length() - 5) : result;
	}

	private void appendQuery(StringBuffer sb, List<SearchField> fields) {
		for (SearchField field : fields) {

			String match = field.getMatch();
			String values[] = field.getValue().split(" ");
			if (values.length > 1) {
				for (int i = 0; i < values.length; i++) {
					sb.append(match);
					sb.append(values[i] + " ");
				}
			} else {
				sb.append(match + values[0] + " ");
			}
		}
		if (sb.charAt(sb.length() - 1) == ' ') {
			sb.deleteCharAt(sb.length() - 1);
		}
	}

}
