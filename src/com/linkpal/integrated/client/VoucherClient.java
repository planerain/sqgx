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
	private ResultSet rs = null;
	private static final Logger Logger = LoggerFactory.getLogger(VoucherClient.class);

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
					rs = pst1.executeQuery();
					if (rs.next()) {
						int VoucherId = rs.getInt(1);
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
						
						JaxWsDynamicClientFactory clientFactory =JaxWsDynamicClientFactory.newInstance();
						Client client = clientFactory.createClient("http://10.1.100.1:807/InterfaceWebService.asmx?wsdl");
						try {
							Object[] result = client.invoke("GetERPDataByCus",resObj.getString(""));
							for (Object json : result) {
								if (((JSONObject) JSON.toJSON(json)).getIntValue("StatusCode") == 200) {
									pst2 = conn.prepareStatement("update EIS_Voucher set IsRead = 1 where VoucherId=?");
									pst2.setInt(1, VoucherId);
									pst2.execute();
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
				DBUtils.closeConnection(conn, pst1, rs);
				DBUtils.closeConnection(conn, pst2, null);
			}
		}
	}
}
