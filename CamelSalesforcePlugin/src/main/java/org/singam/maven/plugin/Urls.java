package org.singam.maven.plugin;


public class Urls
{
private String layout;

private String layouts;

private String compactLayouts;

private String rowTemplate;
private String approvalLayouts;
private String uiDetailTemplate;
private String uiEditTemplate;
private String listviews;
private String describe;
private String uiNewRecord;

private String quickActions;

public String getQuickActions() {
	return quickActions;
}

public void setQuickActions(String quickActions) {
	this.quickActions = quickActions;
}

public String getLayouts() {
	return layouts;
}

public void setLayouts(String layouts) {
	this.layouts = layouts;
}

public String getRowTemplate() {
	return rowTemplate;
}

public void setRowTemplate(String rowTemplate) {
	this.rowTemplate = rowTemplate;
}

public String getApprovalLayouts() {
	return approvalLayouts;
}

public void setApprovalLayouts(String approvalLayouts) {
	this.approvalLayouts = approvalLayouts;
}

public String getUiDetailTemplate() {
	return uiDetailTemplate;
}

public void setUiDetailTemplate(String uiDetailTemplate) {
	this.uiDetailTemplate = uiDetailTemplate;
}

public String getUiEditTemplate() {
	return uiEditTemplate;
}

public void setUiEditTemplate(String uiEditTemplate) {
	this.uiEditTemplate = uiEditTemplate;
}

public String getListviews() {
	return listviews;
}

public void setListviews(String listviews) {
	this.listviews = listviews;
}

public String getDescribe() {
	return describe;
}

public void setDescribe(String describe) {
	this.describe = describe;
}

public String getUiNewRecord() {
	return uiNewRecord;
}

public void setUiNewRecord(String uiNewRecord) {
	this.uiNewRecord = uiNewRecord;
}

public String getSobject() {
	return sobject;
}

public void setSobject(String sobject) {
	this.sobject = sobject;
}

private String sobject;



public String getCompactLayouts() {
	return compactLayouts;
}

public void setCompactLayouts(String compactLayouts) {
	this.compactLayouts = compactLayouts;
}

public String getLayout ()
{
return layout;
}

public void setLayout (String layout)
{
this.layout = layout;
}

@Override
public String toString()
{
return "ClassPojo [layout = "+layout+"]";
}
}
