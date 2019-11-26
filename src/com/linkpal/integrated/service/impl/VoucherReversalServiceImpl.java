package com.linkpal.integrated.service.impl;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.linkpal.integrated.service.VoucherReversalService;
import com.linkpal.integrated.util.HttpUtil;

/**
 * Description:
 *
 * @author lichao
 * @date 2019年11月26日 下午1:58:40
 *
 */
@WebService
@BindingType(javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class VoucherReversalServiceImpl implements VoucherReversalService {
	private static final Logger Logger = LoggerFactory.getLogger(VoucherReversalServiceImpl.class);
	
	// 如果是技术冲销，先生成红冲凭证，再生成正确的凭证
	// 如果是业务冲销，先调用接口生成红冲凭证，再调用接口生成新的凭证
	@Override
	public String getVoucherReversalData(String voucherReversalData) {
		Logger.info("接收到的凭证冲销数据为:"+voucherReversalData);
		JSONObject jsonObject = JSONObject.parseObject(voucherReversalData);
		int erpVoucherId = Integer.parseInt(jsonObject.getString("erpVoucherId"));
		String reverseType = jsonObject.getString("reverseType");
		// 业务冲销
		if(reverseType.equals("0")) {
			// 发送 GET 请求
			String authorityCode = "4f03ba08c7d87ece76858af449ad24e0f9a2ad3bafafe148";
			String token = HttpUtil.sendGet("http://172.16.7.153/K3API/Token/Create",
					"authorityCode=" + authorityCode);
			// 发送POST请求
			String param = "{\"Filter\": \"FVoucherId=" + erpVoucherId + "\"}";
			String response = HttpUtil.sendPost(
					"http://172.16.7.153/K3API/VoucherData/QueryVoucher?token="
							+ JSON.parseObject(JSON.parseObject(token).get("Data").toString()).get("Token"),
					param);
			JSONObject resObj = JSONObject.parseObject(response);
			
		}
		// 技术冲销
		if(reverseType.equals("1")) {
			
		}
		return null;
	}

}
