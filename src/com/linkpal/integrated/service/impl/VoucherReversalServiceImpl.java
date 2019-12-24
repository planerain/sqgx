package com.linkpal.integrated.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.linkpal.integrated.service.VoucherReversalService;
import com.linkpal.integrated.util.CommonUtil;
import com.linkpal.integrated.util.HttpUtil;

/**
 * Description:凭证冲销
 *
 * @author lichao
 * @date 2019年11月26日 下午1:58:40
 *
 */
@WebService(serviceName = "VoucherReversalService")
@BindingType(javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class VoucherReversalServiceImpl implements VoucherReversalService {
	private static final Logger Logger = LoggerFactory.getLogger(VoucherReversalServiceImpl.class);

	// 日期
	String date;
	// 凭证类型
	String voucherType;
	// 凭证号
	int voucherNumber;
	// 摘要
	String explanation;

	// 如果是技术冲销，先生成红冲凭证，再生成正确的凭证
	// 如果是业务冲销，先调用接口生成红冲凭证，再调用接口生成新的凭证
	// 冲2014/2/28记字第44号凭证 外购入库_滚珠
	@Override
	public String getVoucherReversalData(String voucherReversalData) {
		Logger.info("接收到的凭证冲销数据为:" + voucherReversalData);
		JSONObject jsonObject = JSONObject.parseObject(voucherReversalData);
		int erpVoucherId = Integer.parseInt(jsonObject.getString("erpVoucherId"));
		String reverseType = jsonObject.getString("reverseType");
		// 业务冲销
		if (reverseType.equals("0")) {
			// 获取Token值
			String authorityCode = "4f03ba08c7d87ece76858af449ad24e0f9a2ad3bafafe148";
			String token = HttpUtil.sendGet("http://172.16.7.153/K3API/Token/Create", "authorityCode=" + authorityCode);
			// 根据凭证ID查询凭证信息
			String param = "{\"Filter\": \"FVoucherId=" + erpVoucherId + "\"}";
			String response = HttpUtil.sendPost("http://172.16.7.153/K3API/VoucherData/QueryVoucher?token="
					+ JSON.parseObject(JSON.parseObject(token).get("Data").toString()).get("Token"), param);
			Logger.info("查询结果："+response);
			JSONObject resObj = JSONObject.parseObject(response).getJSONArray("Data").getJSONObject(0);
			Logger.info("凭证内码为"+erpVoucherId+"的凭证信息为:"+resObj.toJSONString());
			date = resObj.getString("FDate").replace('-', '/');
			voucherType = resObj.getString("FGroup");
			voucherNumber = resObj.getIntValue("FNumber");
			explanation = resObj.getString("FExplanation");
			resObj.put("FExplanation", "冲"+date+voucherType+"字第"+voucherNumber+"号凭证 "+explanation);
			resObj.put("FNumber", CommonUtil.getNo(voucherType.substring(0, 3), voucherType.substring(3)));
			resObj.put("FSerialNum", 0);
			resObj.put("FVoucherID",0);
			JSONArray apArray = resObj.getJSONArray("Entries");
			for (int i = 0; i < apArray.size(); i++) {
				JSONObject apObj = apArray.getJSONObject(i);
				explanation = apObj.getString("FExplanation")==null?"":apObj.getString("FExplanation");
				apObj.put("FExplanation", "冲"+date+voucherType+"字第"+voucherNumber+"号凭证 "+explanation);
				apObj.put("FAmount", apObj.getFloatValue("FAmount")*-1);
				apObj.put("FAmountFor", apObj.getFloatValue("FAmountFor")*-1);
			}
			JSONArray cfArray = resObj.getJSONArray("CashFlow");
			for (int j = 0; j < cfArray.size(); j++) {
				JSONObject cfObj = cfArray.getJSONObject(j);
				cfObj.put("FAmount", cfObj.getFloatValue("FAmount")*-1);
				cfObj.put("FAmountFor", cfObj.getFloatValue("FAmountFor")*-1);
			}
			Map<String, Object> sendMap = new HashMap<String, Object>();
			sendMap.put("Replace", "false");
			sendMap.put("VoucherData", resObj);
			JSONObject sendJson = new JSONObject(sendMap);
			Logger.info("业务冲销凭证生成数据为:"+StringEscapeUtils.unescapeJava(JSON.toJSONString(sendJson, SerializerFeature.WriteMapNullValue)));
			// 生成冲销凭证
			HttpUtil.sendPost(
					"http://172.16.7.153/K3API/VoucherData/UpdateVoucher?token="
							+ JSON.parseObject(JSON.parseObject(token).get("Data").toString()).get("Token"),
							StringEscapeUtils.unescapeJava(JSON.toJSONString(sendJson, SerializerFeature.WriteMapNullValue)));
		}
		// 技术冲销
		if (reverseType.equals("1")) {
			// 获取Token值
		    String authorityCode = "4f03ba08c7d87ece76858af449ad24e0f9a2ad3bafafe148";
		    String token = HttpUtil.sendGet("http://172.16.7.153/K3API/Token/Create", "authorityCode=" + authorityCode);
		    // 根据凭证ID查询凭证信息
		    String param = "{\"Filter\": \"FVoucherId=" + erpVoucherId + "\"}";
		    String response = HttpUtil.sendPost("http://172.16.7.153/K3API/VoucherData/QueryVoucher?token="
		    		+ JSON.parseObject(JSON.parseObject(token).get("Data").toString()).get("Token"), param);
		    JSONObject resObj = JSONObject.parseObject(response).getJSONArray("Data").getJSONObject(0);
		    date = resObj.getString("FDate").replace('-', '/');
		    voucherType = resObj.getString("FGroup");
		    voucherNumber = resObj.getIntValue("FNumber");
		    explanation = resObj.getString("FExplanation");
		    resObj.put("FExplanation", "冲"+date+voucherType+"字第"+voucherNumber+"号凭证 "+explanation);
		    resObj.put("FNumber", CommonUtil.getNo(voucherType.substring(0, 3), voucherType.substring(3)));
		    resObj.put("FSerialNum", 0);
		    resObj.put("FVoucherID",0);
		    JSONArray apArray = resObj.getJSONArray("Entries");
		    for (int i = 0; i < apArray.size(); i++) {
		    	JSONObject apObj = apArray.getJSONObject(i);
		    	apObj.put("FExplanation", "冲"+date+voucherType+"字第"+voucherNumber+"号凭证 "+apObj.getString("FExplanation")==null?"":apObj.getString("FExplanation"));
		    	apObj.put("FAmount", apObj.getFloatValue("FAmount")*-1);
		    	apObj.put("FAmountFor", apObj.getFloatValue("FAmountFor")*-1);
		    }
		    JSONArray cfArray = resObj.getJSONArray("CashFlow");
		    for (int j = 0; j < cfArray.size(); j++) {
		    	JSONObject cfObj = cfArray.getJSONObject(j);
		    	cfObj.put("FAmount", cfObj.getFloatValue("FAmount")*-1);
		    	cfObj.put("FAmountFor", cfObj.getFloatValue("FAmountFor")*-1);
		    }
		    Map<String, Object> sendMap = new HashMap<String, Object>();
		    sendMap.put("Replace", "false");
		    sendMap.put("VoucherData", resObj.toJSONString());
		    JSONObject sendJson = new JSONObject(sendMap);
		    // 生成冲销凭证
		    HttpUtil.sendPost(
		    		"http://172.16.7.153/K3API/VoucherData/UpdateVoucher?token="
		    				+ JSON.parseObject(JSON.parseObject(token).get("Data").toString()).get("Token"),
		    		JSON.toJSONString(sendJson, SerializerFeature.WriteMapNullValue));
		    // 生成新的凭证
		    }
		return "success";
	}

}
