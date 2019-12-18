package com.linkpal.integrated.test;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description:测试凭证接收接口
 *
 * @author lichao
 * @date 2019年12月4日 下午4:09:29
 *
 */
public class TestVoucherService {
	private static final Logger LOGGER = LoggerFactory.getLogger(TestVoucherService.class);
	
	public void transVoucher() {
		String jsonData = "{\r\n" + 
				"	\"VoucherID\": \"GStest201912091617\",\r\n" + 
				"	\"ZWPZK_DWBH\": \"105\",\r\n" + 
				"	\"ZWPZK_KJND\": \"2019\",\r\n" + 
				"	\"ZWPZK_KJQJ\": \"11\",\r\n" + 
				"	\"ZWPZK_PZRQ\": \"20191130\",\r\n" + 
				"	\"ZWPZK_PZBH\": \"\",\r\n" + 
				"	\"ZWPZK_LXBH\": \"GL现支\",\r\n" + 
				"	\"ZWPZK_FJZS\":  0,\r\n" + 
				"	\"ZWPZK_CSR\": \"\",\r\n" + 
				"	\"ZWPZK_CSRMC\": \"张三\",\r\n" + 
				"	\"ZWPZK_FSR\": \"\",\r\n" + 
				"	\"ZWPZK_FSRMC\": \"李四\",\r\n" + 
				"	\"ZWPZK_TZDZS\": 0,\r\n" + 
				"	\"ZWPZK_ZY\": \"付史秀琴应付挂账款11\",\r\n" + 
				"	\"ZWPZK_JE\": 2,\r\n" + 
				"	\"ZWPZK_DJBH\": \"\",\r\n" + 
				"	\"ZWFZYS_WBBH\": \"\",\r\n" + 
				"	\"ZWFZYS_HL\": 1,\r\n" + 
				"	\"CREATEDTIME\": \"2019-11-30\",\r\n" + 
				"	\"ZWPZK_ZHID\": \"\",\r\n" + 
				"	\"ZWPZFL\": [{\r\n" + 
				"		\"ZWFZYS_ID\": \"\",\r\n" + 
				"		\"ZWPZFL_KMBH\": \"2241.98.10\",\r\n" + 
				"		\"ZWPZFL_ZY\": \"付史秀琴应付挂账款\",\r\n" + 
				"		\"ZWPZFL_JZFX\": \"1\",\r\n" + 
				"		\"ZWFZYS_YSBH\": \"\",\r\n" + 
				"		\"ZWFZYS_BMBH\": \"00\",\r\n" + 
				"		\"ZWFZYS_WLDWBH\": \"00\",\r\n" + 
				"		\"ZWFZYS_ZGBH\": \"\",\r\n" + 
				"		\"ZWFZYS_CPBH\": \"\",\r\n" + 
				"		\"ZWFZYS_XMBH1\": \"CI1.02.04.01\",\r\n" + 
				"		\"ZWFZYS_XMBH2\": \"222\",\r\n" + 
				"		\"ZWFZYS_XMBH3\": \"\",\r\n" + 
				"		\"ZWFZYS_XMBH4\": \"\",\r\n" + 
				"		\"ZWFZYS_XMBH5\": \"\",\r\n" + 
				"		\"ZWFZYS_XMBH6\": \"\",\r\n" + 
				"		\"ZWFZYS_XMBH7\": \"\",\r\n" + 
				"		\"ZWFZYS_XMBH8\": \"\",\r\n" + 
				"		\"ZWFZYS_XMBH9\": \"\",\r\n" + 
				"		\"ZWFZYS_XMBH10\": \"\",\r\n" + 
				"		\"ZWFZYS_XMBH11\": \"\",\r\n" + 
				"		\"ZWFZYS_XMBH12\": \"\",\r\n" + 
				"		\"ZWFZYS_XMBH13\": \"\",\r\n" + 
				"		\"ZWFZYS_XMBH14\": \"\",\r\n" + 
				"		\"ZWFZYS_XMBH15\": \"\",\r\n" + 
				"		\"ZWFZYS_SL\": \"\",\r\n" + 
				"		\"ZWFZYS_DJ\": \"\",\r\n" + 
				"		\"ZWFZYS_WB\": \"\",\r\n" + 
				"		\"ZWFZYS_JE\": \"\",\r\n" + 
				"		\"ZWFZYS_YWRQ\": \"\",\r\n" + 
				"		\"ZWFZYS_PJH\": \"\",\r\n" + 
				"		\"ZWFZYS_YT\": \"\",\r\n" + 
				"		\"ZWFZYS_FSGLF\": \"\"\r\n" + 
				"	},{\r\n" + 
				"\"ZWFZYS_ID\": \"\",\r\n" + 
				"		\"ZWPZFL_KMBH\": \"2241.98.10\",\r\n" + 
				"		\"ZWPZFL_ZY\": \"付史秀琴应付挂账款\",\r\n" + 
				"		\"ZWPZFL_JZFX\": \"1\",\r\n" + 
				"		\"ZWFZYS_YSBH\": \"\",\r\n" + 
				"		\"ZWFZYS_BMBH\": \"00\",\r\n" + 
				"		\"ZWFZYS_WLDWBH\": \"00\",\r\n" + 
				"		\"ZWFZYS_ZGBH\": \"\",\r\n" + 
				"		\"ZWFZYS_CPBH\": \"\",\r\n" + 
				"		\"ZWFZYS_XMBH1\": \"CI1.02.04.01\",\r\n" + 
				"		\"ZWFZYS_XMBH2\": \"222\",\r\n" + 
				"		\"ZWFZYS_XMBH3\": \"\",\r\n" + 
				"		\"ZWFZYS_XMBH4\": \"\",\r\n" + 
				"		\"ZWFZYS_XMBH5\": \"\",\r\n" + 
				"		\"ZWFZYS_XMBH6\": \"\",\r\n" + 
				"		\"ZWFZYS_XMBH7\": \"\",\r\n" + 
				"		\"ZWFZYS_XMBH8\": \"\",\r\n" + 
				"		\"ZWFZYS_XMBH9\": \"\",\r\n" + 
				"		\"ZWFZYS_XMBH10\": \"\",\r\n" + 
				"		\"ZWFZYS_XMBH11\": \"\",\r\n" + 
				"		\"ZWFZYS_XMBH12\": \"\",\r\n" + 
				"		\"ZWFZYS_XMBH13\": \"\",\r\n" + 
				"		\"ZWFZYS_XMBH14\": \"\",\r\n" + 
				"		\"ZWFZYS_XMBH15\": \"\",\r\n" + 
				"		\"ZWFZYS_SL\": \"\",\r\n" + 
				"		\"ZWFZYS_DJ\": \"\",\r\n" + 
				"		\"ZWFZYS_WB\": \"\",\r\n" + 
				"		\"ZWFZYS_JE\": \"\",\r\n" + 
				"		\"ZWFZYS_YWRQ\": \"\",\r\n" + 
				"		\"ZWFZYS_PJH\": \"\",\r\n" + 
				"		\"ZWFZYS_YT\": \"\",\r\n" + 
				"		\"ZWFZYS_FSGLF\": \"\"\r\n" + 
				"}]\r\n" + 
				"}\r\n" + 
				"";
		JaxWsDynamicClientFactory clientFactory =JaxWsDynamicClientFactory.newInstance();
		Client client = clientFactory.createClient("http://127.0.0.1:8080/sqgx/ws/voucher?wsdl");
		if(client!=null) {
			LOGGER.info("链接成功");
			try {
				Object[] result = client.invoke("getVoucherData", jsonData);
				LOGGER.info(result[0].toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Test
	public void test() {
		TestVoucherService tvs = new TestVoucherService();
		tvs.transVoucher();
	}
}
