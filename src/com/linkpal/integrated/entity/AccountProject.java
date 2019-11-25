package com.linkpal.integrated.entity;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Description:
 *
 * @author lichao
 * @date 2019年11月7日 下午2:23:39
 *
 */
public class AccountProject {
	// 核算项目名称
	@JSONField(name = "FDetailName")
	private String detailName;
	// 核算项目编码
	@JSONField(name = "FDetailNumber")
	private String detailNumber;
	@JSONField(name = "FDetailUUID")
	private String detailUUID;
	// 核算项目类型名称
	@JSONField(name = "FTypeName")
	private String typeName;
	// 核算项目类型编码
	@JSONField(name = "FTypeNumber")
	private String typeNumber;
	
	public String getDetailName() {
		return detailName;
	}
	public void setDetailName(String detailName) {
		this.detailName = detailName;
	}
	public String getDetailNumber() {
		return detailNumber;
	}
	public void setDetailNumber(String detailNumber) {
		this.detailNumber = detailNumber;
	}
	public String getDetailUUID() {
		return detailUUID;
	}
	public void setDetailUUID(String detailUUID) {
		this.detailUUID = detailUUID;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getTypeNumber() {
		return typeNumber;
	}
	public void setTypeNumber(String typeNumber) {
		this.typeNumber = typeNumber;
	}
}
