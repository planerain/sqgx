package com.linkpal.integrated.entity;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Description:
 *
 * @author lichao
 * @date 2019年11月7日 下午2:23:19
 *
 */
public class CashFlow {
	// 科目名称
	@JSONField(name = "FAccName")
	private String accName;
	// 科目编码
	@JSONField(name = "FAccNumber")
	private String accNumber;
	// 本位币金额
	@JSONField(name = "FAmount")
	private float amount;
	// 原币金额
	@JSONField(name = "FAmountFor")
	private float amountFor;
	// 主表核算项目类型名称
	@JSONField(name = "FClassName")
	private String className;
	// 主表核算项目类型编码
	@JSONField(name = "FClassNumber")
	private String classNumber;
	// 币别名称
	@JSONField(name = "FCurrencyName")
	private String currencyName;
	// 币别编码 
	@JSONField(name = "FCurrencyNumber")
	private String currencyNumber;
	// 分录ID
	@JSONField(name = "FEntryid")
	private int entryid;
	// 对方科目分录ID
	@JSONField(name = "FEntryid2")
	private int entryid2;
	// 主表核算项目名称
	@JSONField(name = "FItemName")
	private String itemName;
	// 主表核算项目编码 
	@JSONField(name = "FItemNumber")
	private String itemNumber;
	// 附表核算项目类型名称
	@JSONField(name = "FSubClassName")
	private String subClassName;
	// 附表核算项目类型编码
	@JSONField(name = "FSubClassNumber")
	private String subClassNumber;
	// 附表核算项目名称
	@JSONField(name = "FSubItemName")
	private String subItemName;
	// 附表核算项目编码
	@JSONField(name = "FSubItemNumber")
	private String subItemNumber;
	
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	public String getAccNumber() {
		return accNumber;
	}
	public void setAccNumber(String accNumber) {
		this.accNumber = accNumber;
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
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getClassNumber() {
		return classNumber;
	}
	public void setClassNumber(String classNumber) {
		this.classNumber = classNumber;
	}
	public String getCurrencyName() {
		return currencyName;
	}
	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}
	public String getCurrencyNumber() {
		return currencyNumber;
	}
	public void setCurrencyNumber(String currencyNumber) {
		this.currencyNumber = currencyNumber;
	}
	public int getEntryid() {
		return entryid;
	}
	public void setEntryid(int entryid) {
		this.entryid = entryid;
	}
	public int getEntryid2() {
		return entryid2;
	}
	public void setEntryid2(int entryid2) {
		this.entryid2 = entryid2;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getItemNumber() {
		return itemNumber;
	}
	public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
	}
	public String getSubClassName() {
		return subClassName;
	}
	public void setSubClassName(String subClassName) {
		this.subClassName = subClassName;
	}
	public String getSubClassNumber() {
		return subClassNumber;
	}
	public void setSubClassNumber(String subClassNumber) {
		this.subClassNumber = subClassNumber;
	}
	public String getSubItemName() {
		return subItemName;
	}
	public void setSubItemName(String subItemName) {
		this.subItemName = subItemName;
	}
	public String getSubItemNumber() {
		return subItemNumber;
	}
	public void setSubItemNumber(String subItemNumber) {
		this.subItemNumber = subItemNumber;
	}
}
