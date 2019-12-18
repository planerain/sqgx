package com.linkpal.integrated.test;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestVoucherReversalService {
	private static final Logger LOGGER = LoggerFactory.getLogger(TestVoucherReversalService.class);
	
	public static void main(String[] args) {
		String jsonData = "{\r\n" + 
				"	\"erpVoucherId\": \"4064\",\r\n" + 
				"	\"reverseType\": \"1\",\r\n" + 
				"	\"reverseDate\": \"2019-12-10\"\r\n" + 
				"}";
		JaxWsDynamicClientFactory clientFactory =JaxWsDynamicClientFactory.newInstance();
		Client client = clientFactory.createClient("http://172.16.7.153:8080/sqgx/ws/voucherReversal?wsdl");
		if(client!=null) {
			LOGGER.info("链接成功");
			System.out.println("111111111");
			try {
				Object[] result = client.invoke("getVoucherReversalData", jsonData);
				LOGGER.info(result[0].toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
