package com.linkpal.integrated.client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.linkpal.integrated.util.DBUtils;
import com.linkpal.integrated.util.HttpUtil;

/**
 * Description:
 *
 * @author lichao
 * @date 2019年11月13日 上午11:15:15
 *
 */

@Component("voucherClient")
public class VoucherClient {
	private Connection conn = null;
	private PreparedStatement pst1 = null;
	private PreparedStatement pst2 = null;
	private PreparedStatement pst3 = null;
	private ResultSet rs1 = null;
	private ResultSet rs2 = null;
	private static final Logger Logger = LoggerFactory.getLogger(VoucherClient.class);
	
	// 共享凭证内码
	String gxVoucherId;
	// 组织编码
	String orgNumber;
	// 单据编号
	String billNo;

	// 每月最后一天晚上22:00触发执行
	@Scheduled(cron = "0 00 22 28-31 * ?")
	public void transVoucher() {
		final Calendar c = Calendar.getInstance();
		if (c.get(Calendar.DATE) == c.getActualMaximum(Calendar.DATE)) {
			try {
				conn = DBUtils.getConnection();
				if (conn != null) {
					pst1 = conn.prepareStatement(
							"select top 1 VoucherId from EIS_Voucher where IsRead=0 and Period=? and Year=?");
					Calendar cal = Calendar.getInstance();
					pst1.setInt(1, cal.get(Calendar.MONTH) + 1);
					pst1.setInt(2, cal.get(Calendar.YEAR));
					rs1 = pst1.executeQuery();
					if (rs1.next()) {
						int VoucherId = rs1.getInt(1);
						pst2 = conn.prepareStatement("select distinct ZWPZK_PZNM,ZWPZK_DWBH,ZWPZK_ZDRBH from t_ESB_Voucher where erpVoucherId=?");
						pst2.setInt(1, VoucherId);
						rs2 = pst2.executeQuery();
						while (rs2.next()) {
							gxVoucherId = rs2.getString(1);
							orgNumber = rs2.getString(2);
							billNo = rs2.getString(3);
						}
						// 发送 GET 请求
						String authorityCode = "4f03ba08c7d87ece76858af449ad24e0f9a2ad3bafafe148";
						String token = HttpUtil.sendGet("http://172.16.7.153/K3API/Token/Create",
								"authorityCode=" + authorityCode);
						// 发送POST请求
						String param = "{\"Filter\": \"FVoucherId=" + VoucherId + "\"}";
						String response = HttpUtil.sendPost(
								"http://172.16.7.153/K3API/VoucherData/QueryVoucher?token="
										+ JSON.parseObject(JSON.parseObject(token).get("Data").toString()).get("Token"),
								param);
						JSONObject resObj = JSONObject.parseObject(response);
						JSONObject sendJson = resObj.getJSONArray("Data").getJSONObject(1);
						sendJson.put("FNumber",sendJson.getString("FGroup")+sendJson.getIntValue("FNumber"));
						sendJson.put("ZWPZK_PZNM", gxVoucherId);
						sendJson.put("ZWPZK_DWBH", orgNumber);
						sendJson.put("ZWPZK_DJBH", billNo);
						
						JaxWsDynamicClientFactory clientFactory =JaxWsDynamicClientFactory.newInstance();
						Client client = clientFactory.createClient("http://10.1.100.1:807/InterfaceWebService.asmx?wsdl");
						try {
							Object[] result = client.invoke("GetERPDataByCus",sendJson.toJSONString());
							for (Object json : result) {
								if (((JSONObject) JSON.toJSON(json)).getIntValue("StatusCode") == 200) {
									pst3 = conn.prepareStatement("update EIS_Voucher set IsRead = 1 where VoucherId=?");
									pst3.setInt(1, VoucherId);
									pst3.execute();
								}
								else {
									Logger.info(((JSONObject) JSON.toJSON(json)).getString("Message"));
								}
							}
						} catch (Exception e) {
							Logger.info(e.getMessage());
						}
					} else {
						Logger.info("没有可传输的凭证信息");
					}
				}
			} catch (Exception e) {
				Logger.info(e.getMessage());
			} finally {
				DBUtils.closeConnection(conn, pst1, rs1);
				DBUtils.closeConnection(conn, pst2, rs2);
				DBUtils.closeConnection(conn, pst3, null);
			}
		}
	}
}
