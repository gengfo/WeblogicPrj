package com.bigrez.web;

import java.util.ArrayList;
import java.util.List;

public class LabelValue
{
	// Attributes

	private String label;
	private String value;
		
	// Public Methods

	public LabelValue(String pBoth) {
		setLabel(pBoth);
		setValue(pBoth);
	}

	public LabelValue(String pLabel, String pValue) {
		setLabel(pLabel);
		setValue(pValue);
	}

	public static List<LabelValue> makeLabelValues(List<String> lvlist) {
		return makeLabelValues(lvlist,lvlist);
	}

	public static List<LabelValue> makeLabelValues(List<String> labels, List<String> values) {
		List<LabelValue> results = new ArrayList<LabelValue>(labels.size());
		for (int jj=0; jj<labels.size(); jj++) {
			LabelValue lv = new LabelValue((String)labels.get(jj),(String)values.get(jj));
			results.add(lv);
		}
		return results;
	}

	public String toString() {
		// return string representation, either "label" or "label:value" if different
		return ( getLabel().equals(getValue()) ? getLabel() : getLabel()+":"+getValue() );
	}

	// Accessors

	public String getLabel() {
		return label;
	}

	public void setLabel(String pValue) {
		label = pValue;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String pValue) {
		value = pValue;
	}

	// End Accessors
}
