package com.linkpal.integrated.entity;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Description:
 *
 * @author lichao
 * @date 2019年11月7日 下午2:05:18
 *
 */
public class VoucherBody {
	// 科目名称
	@JSONField(name="FAccountName")
	private String accountName;
	// 科目编码
	@JSONField(name="FAccountNumber")
	private String accountNumber;
	// 本位币金额
	@JSONField(name="FAmount")
	private float amount;
	// 原币金额
	@JSONField(name="FAmountFor")
	private float amountFor;
	// 币别名称
	@JSONField(name="FCurrencyName")
	private String currencyName;
	// 币别编码
	@JSONField(name="FCurrencyNumber")
	private String CurrencyNumber;
	// 借贷方向 1:借方 0:贷方
	@JSONField(name="FDC")
	private int DC;
	// 分录ID
	@JSONField(name="FEntryID")
	private int entryId;
	// 汇率
	@JSONField(name="FExchangeRate")
	private float exchangeRate;
	// 摘要
	@JSONField(name="FExplanation")
	private String explanation;
	// 单位
	@JSONField(name="FMeasureUnit")
	private String measureUnit;
	// 单位UUID
	@JSONField(name="FMeasureUnitUUID")
	private String measureUnitUUID;
	// 数量
	@JSONField(name="FQuantity")
	private float quantity;
	// 结算号
	@JSONField(name="FSettleNo")
	private String settleNo;
	// 结算类型
	@JSONField(name="FSettleTypeName")
	private String settleTypeName;
	// 业务号
	@JSONField(name="FTransNo")
	private String transNo;
	// 单价
	@JSONField(name="FUnitPrice")
	private float unitPrice;
	// 凭证分录数据
	@JSONField(name="DetailEntries")
	private List<AccountProject> acctList;
	
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	public float getAmountFor() {
		return amountFor;
	}
	public void setAmountFor(float amountFor) {
		this.amountFor = amountFor;
	}
	public String getCurrencyName() {
		return currencyName;
	}
	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}
	public String getCurrencyNumber() {
		return CurrencyNumber;
	}
	public void setCurrencyNumber(String currencyNumber) {
		CurrencyNumber = currencyNumber;
	}
	public int getDC() {
		return DC;
	}
	public void setDC(int dC) {
		DC = dC;
	}
	public int getEntryId() {
		return entryId;
	}
	public void setEntryId(int entryId) {
		this.entryId = entryId;
	}
	public float getExchangeRate() {
		return exchangeRate;
	}
	public void setExchangeRate(float exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
	public String getExplanation() {
		return explanation;
	}
	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}
	public String getMeasureUnit() {
		return measureUnit;
	}
	public void setMeasureUnit(String measureUnit) {
		this.measureUnit = measureUnit;
	}
	public String getMeasureUnitUUID() {
		return measureUnitUUID;
	}
	public void setMeasureUnitUUID(String measureUnitUUID) {
		this.measureUnitUUID = measureUnitUUID;
	}
	public float getQuantity() {
		return quantity;
	}
	public void setQuantity(float quantity) {
		this.quantity = quantity;
	}
	public String getSettleNo() {
		return settleNo;
	}
	public void setSettleNo(String settleNo) {
		this.settleNo = settleNo;
	}
	public String getSettleTypeName() {
		return settleTypeName;
	}
	public void setSettleTypeName(String settleTypeName) {
		this.settleTypeName = settleTypeName;
	}
	public String getTransNo() {
		return transNo;
	}
	public void setTransNo(String transNo) {
		this.transNo = transNo;
	}
	public float getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(float unitPrice) {
		this.unitPrice = unitPrice;
	}
	public List<AccountProject> getAcctList() {
		return acctList;
	}
	public void setAcctList(List<AccountProject> acctList) {
		this.acctList = acctList;
	}
}
