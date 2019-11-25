package com.linkpal.integrated.entity;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Description:
 *
 * @author lichao
 * @date 2019年11月12日 上午11:17:39
 *
 */
public class Result {
	// 单位编号
	@JSONField(name = "unitCode")
	private String unitCode;
	// GS凭证内码
	@JSONField(name = "voucherId")
	private String voucherId;
	// GS会计年度
	@JSONField(name = "fiscalYear")
	private String fiscalYear;
	// ERP系统凭证编号
	@JSONField(name = "erpVoucherCode")
	private String erpVoucherCode;
	// ERP系统凭证内码
	@JSONField(name = "erpVoucherId")
	private String erpVoucherId;
	// 生成状态
	@JSONField(name = "generateFlag")
	private String generateFlag;
	// 生成信息
	@JSONField(name = "generateMsg")
	private String generateMsg;
	// 生成时间
	@JSONField(name = "generateTime")
	private String generateTime;
	// 单据编号
	@JSONField(name = "billNo")
	private String billNo;
	
	public String getUnitCode() {
		return unitCode;
	}
	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}
	public String getVoucherId() {
		return voucherId;
	}
	public void setVoucherId(String voucherId) {
		this.voucherId = voucherId;
	}
	public String getFiscalYear() {
		return fiscalYear;
	}
	public void setFiscalYear(String fiscalYear) {
		this.fiscalYear = fiscalYear;
	}
	public String getErpVoucherCode() {
		return erpVoucherCode;
	}
	public void setErpVoucherCode(String erpVoucherCode) {
		this.erpVoucherCode = erpVoucherCode;
	}
	public String getErpVoucherId() {
		return erpVoucherId;
	}
	public void setErpVoucherId(String erpVoucherId) {
		this.erpVoucherId = erpVoucherId;
	}
	public String getGenerateFlag() {
		return generateFlag;
	}
	public void setGenerateFlag(String generateFlag) {
		this.generateFlag = generateFlag;
	}
	public String getGenerateMsg() {
		return generateMsg;
	}
	public void setGenerateMsg(String generateMsg) {
		this.generateMsg = generateMsg;
	}
	public String getGenerateTime() {
		return generateTime;
	}
	public void setGenerateTime(String generateTime) {
		this.generateTime = generateTime;
	}
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
}
