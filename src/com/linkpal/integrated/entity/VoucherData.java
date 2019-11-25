package com.linkpal.integrated.entity;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Description:
 *
 * @author lichao
 * @date 2019年11月9日 下午4:52:24
 *
 */
public class VoucherData {
	@JSONField(name = "Replace")
	private String replace;
	@JSONField(name = "VoucherData")
	private Voucher voucher;
	
	public String getReplace() {
		return replace;
	}
	public void setReplace(String replace) {
		this.replace = replace;
	}
	public Voucher getVoucher() {
		return voucher;
	}
	public void setVoucher(Voucher voucher) {
		this.voucher = voucher;
	}
}
