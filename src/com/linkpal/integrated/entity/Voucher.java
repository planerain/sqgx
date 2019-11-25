package com.linkpal.integrated.entity;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Description:
 *
 * @author lichao
 * @date 2019年11月7日 下午2:01:26
 *
 */
public class Voucher {
	// 附件数
	@JSONField(name = "FAttachments")
	private int attachments;
	// 出纳员
	@JSONField(name = "FCashier")
	private String cashier;
	// 凭证日期
	@JSONField(name = "FDate")
	private String date;
	// 摘要
	@JSONField(name = "FExplanation")
	private String explanation;
	// 凭证字
	@JSONField(name = "FGroup")
	private String group;
	// 会计主管
	@JSONField(name = "FHandler")
	private String handler;
	// 凭证号
	@JSONField(name = "FNumber")
	private int number;
	// 会计期间
	@JSONField(name = "FPeriod")
	private int period;
	// 记账人
	@JSONField(name = "FPoster")
	private String poster;
	// 制单人
	@JSONField(name = "FPreparer")
	private String preparer;
	// 参考信息
	@JSONField(name = "FReference")
	private String reference;
	// 凭证序号
	@JSONField(name = "FSerialNum")
	private int serialNum;
	// 附件数
	@JSONField(name = "FTransDate")
	private String transDate;
	// 凭证内码
	@JSONField(name = "FVoucherID")
	private int voucherID;
	// 会计年度
	@JSONField(name = "FYear")
	private int year;
	// 凭证分录数据
	@JSONField(name = "Entries")
	private List<VoucherBody> bodyList;
	// 现金流量
	@JSONField(name = "CashFlow")
	private List<CashFlow> cashFlow;
	
	public int getAttachments() {
		return attachments;
	}
	public void setAttachments(int attachments) {
		this.attachments = attachments;
	}
	public String getCashier() {
		return cashier;
	}
	public void setCashier(String cashier) {
		this.cashier = cashier;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getExplanation() {
		return explanation;
	}
	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getHandler() {
		return handler;
	}
	public void setHandler(String handler) {
		this.handler = handler;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public int getPeriod() {
		return period;
	}
	public void setPeriod(int period) {
		this.period = period;
	}
	public String getPoster() {
		return poster;
	}
	public void setPoster(String poster) {
		this.poster = poster;
	}
	public String getPreparer() {
		return preparer;
	}
	public void setPreparer(String preparer) {
		this.preparer = preparer;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public int getSerialNum() {
		return serialNum;
	}
	public void setSerialNum(int serialNum) {
		this.serialNum = serialNum;
	}
	public String getTransDate() {
		return transDate;
	}
	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}
	public int getVoucherID() {
		return voucherID;
	}
	public void setVoucherID(int voucherID) {
		this.voucherID = voucherID;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public List<VoucherBody> getBodyList() {
		return bodyList;
	}
	public void setBodyList(List<VoucherBody> bodyList) {
		this.bodyList = bodyList;
	}
	public List<CashFlow> getCashFlow() {
		return cashFlow;
	}
	public void setCashFlow(List<CashFlow> cashFlow) {
		this.cashFlow = cashFlow;
	}
}
