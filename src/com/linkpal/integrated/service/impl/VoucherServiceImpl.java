package com.linkpal.integrated.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.linkpal.integrated.entity.AccountProject;
import com.linkpal.integrated.entity.CashFlow;
import com.linkpal.integrated.entity.Voucher;
import com.linkpal.integrated.entity.VoucherBody;
import com.linkpal.integrated.entity.VoucherData;
import com.linkpal.integrated.service.VoucherService;
import com.linkpal.integrated.util.DBUtils;
import com.linkpal.integrated.util.HttpUtil;

/**
 * Description:凭证
 *
 * @author lichao
 * @date 2019年11月8日 下午5:41:17
    *     实现思路:先接收ESB传过来的数据存入中间表(t_ESB_Voucher),再去中间表读取数据
 *
 */
@WebService
@BindingType(javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class VoucherServiceImpl implements VoucherService {
	private Connection conn = null;
	private PreparedStatement pst = null;
	private ResultSet rs = null;
	private PreparedStatement pst2 = null;
	private ResultSet rs2 = null;
	
	// 共享凭证内码
	String gxVoucherId;
	// 组织编码
	String orgNumber;
	// 会计年度
	String year;
	// erp凭证编码
	String erpVoucherCode;
	// erp凭证内码
	String erpVoucherId;
	// 失败信息
	String generateMsg;
	// 生成状态
	boolean generateFlag;
	
	private static final Logger Logger = LoggerFactory.getLogger(VoucherServiceImpl.class);

	@Override
	public String getVoucherData(String voucherData) throws ParseException {
		Logger.info("接收到的凭证数据为:"+voucherData);
		JSONObject voucherDataObj = JSONObject.parseObject(voucherData);
		
		try {
			conn = DBUtils.getConnection();
			if(conn!=null) {
				// 总共47个字段，实际表中需要增加Id(自增)，CreateDate(当前时间)，IsRead(默认为0),erpVoucherId
				pst=conn.prepareStatement("insert into t_ESB_Voucher(ZWPZK_PZNM,ZWPZK_DWBH,ZWPZK_KJND,ZWPZK_KJQJ,ZWPZK_PZRQ,ZWPZK_PZBH,ZWPZK_LXBH,ZWPZK_FJZS,ZWPZK_ZDR,ZWPZK_TZDZS,ZWPZK_ZY,ZWPZK_JE,CREATEDTIME,ZWPZK_ZDRBH,ZWFZYS_ID,ZWPZFL_KMBH,ZWPZFL_ZY,"
						+ "ZWPZFL_JZFX,ZWFZYS_YSBH,ZWFZYS_BMBH,ZWFZYS_WLDWBH,ZWFZYS_ZGBH,ZWFZYS_CPBH,ZWFZYS_XMBH1,ZWFZYS_XMBH2,ZWFZYS_XMBH3,ZWFZYS_XMBH4,ZWFZYS_XMBH5,ZWFZYS_XMBH6,ZWFZYS_XMBH7,ZWFZYS_XMBH8,ZWFZYS_XMBH9,ZWFZYS_XMBH10,ZWFZYS_XMBH11,ZWFZYS_XMBH12,"
						+ "ZWFZYS_XMBH13,ZWFZYS_XMBH14,ZWFZYS_XMBH15,ZWFZYS_WBBH,ZWFZYS_SL,ZWFZYS_DJ,ZWFZYS_WB,ZWFZYS_HL,ZWFZYS_JE,ZWFZYS_YWRQ,ZWFZYS_PJH,ZWFZYS_YT) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
				pst.setString(1, voucherDataObj.getString("ZWPZK_PZNM"));
				pst.setString(2, voucherDataObj.getString("ZWPZK_DWBH"));
				pst.setString(3, voucherDataObj.getString("ZWPZK_KJND"));
				pst.setString(4, voucherDataObj.getString("ZWPZK_KJQJ"));
				pst.setString(5, voucherDataObj.getString("ZWPZK_PZRQ"));
				pst.setString(6, voucherDataObj.getString("ZWPZK_PZBH"));
				pst.setString(7, voucherDataObj.getString("ZWPZK_LXBH"));
				pst.setInt(8, voucherDataObj.getIntValue("ZWPZK_FJZS"));
				pst.setString(9, voucherDataObj.getString("ZWPZK_ZDR"));
				pst.setInt(10, voucherDataObj.getIntValue("ZWPZK_TZDZS"));
				pst.setString(11, voucherDataObj.getString("ZWPZK_ZY"));
				pst.setFloat(12, voucherDataObj.getFloatValue("ZWPZK_JE"));
				pst.setString(13, voucherDataObj.getString("CREATEDTIME"));
				pst.setString(14, voucherDataObj.getString("ZWPZK_ZDRBH"));
				JSONArray entries = voucherDataObj.getJSONArray("ZWPZFL");
				for (int i = 0; i < entries.size(); i++) {
					JSONObject entryData = entries.getJSONObject(i);
					pst.setString(15, entryData.getString("ZWFZYS_ID"));
					pst.setString(16, entryData.getString("ZWPZFL_KMBH"));
					pst.setString(17, entryData.getString("ZWPZFL_ZY"));
					pst.setString(18, entryData.getString("ZWPZFL_JZFX"));
					pst.setString(19, entryData.getString("ZWFZYS_YSBH"));
					pst.setString(20, entryData.getString("ZWFZYS_BMBH"));
					pst.setString(21, entryData.getString("ZWFZYS_WLDWBH"));
					pst.setString(22, entryData.getString("ZWFZYS_ZGBH"));
					pst.setString(23, entryData.getString("ZWFZYS_CPBH"));
					pst.setString(24, entryData.getString("ZWFZYS_XMBH1"));
					pst.setString(25, entryData.getString("ZWFZYS_XMBH2"));
					pst.setString(26, entryData.getString("ZWFZYS_XMBH3"));
					pst.setString(27, entryData.getString("ZWFZYS_XMBH4"));
					pst.setString(28, entryData.getString("ZWFZYS_XMBH5"));
					pst.setString(29, entryData.getString("ZWFZYS_XMBH6"));
					pst.setString(30, entryData.getString("ZWFZYS_XMBH7"));
					pst.setString(31, entryData.getString("ZWFZYS_XMBH8"));
					pst.setString(32, entryData.getString("ZWFZYS_XMBH9"));
					pst.setString(33, entryData.getString("ZWFZYS_XMBH10"));
					pst.setString(34, entryData.getString("ZWFZYS_XMBH11"));
					pst.setString(35, entryData.getString("ZWFZYS_XMBH12"));
					pst.setString(36, entryData.getString("ZWFZYS_XMBH13"));
					pst.setString(37, entryData.getString("ZWFZYS_XMBH14"));
					pst.setString(38, entryData.getString("ZWFZYS_XMBH15"));
					pst.setString(39, entryData.getString("ZWFZYS_WBBH"));
					pst.setString(40, entryData.getString("ZWFZYS_SL"));
					pst.setString(41, entryData.getString("ZWFZYS_DJ"));
					pst.setString(42, entryData.getString("ZWFZYS_WB"));
					pst.setString(43, entryData.getString("ZWFZYS_HL"));
					pst.setString(44, entryData.getString("ZWFZYS_JE"));
					pst.setString(45, entryData.getString("ZWFZYS_YWRQ"));
					pst.setString(46, entryData.getString("ZWFZYS_PJH"));
					pst.setString(47, entryData.getString("ZWFZYS_YT"));
					pst.execute();
				}
			}
		} catch (Exception e) {
			Logger.info(e.getMessage());
		}finally {
			DBUtils.closeConnection(conn, pst, null);
		}
		
		VoucherData vd = new VoucherData();
		try {
			conn = DBUtils.getConnection();
			if(conn!=null) {
				pst = conn.prepareStatement("select distinct top 1 ZWPZK_PZNM,ZWPZK_DWBH,ZWPZK_KJND,ZWPZK_KJQJ,ZWPZK_PZRQ,ZWPZK_PZBH,ZWPZK_LXBH,ZWPZK_FJZS,ZWPZK_ZDR,ZWPZK_TZDZS,ZWPZK_ZY,ZWPZK_JE,CREATEDTIME,ZWPZK_ZDRBH from t_ESB_Voucher where IsRead=0");
				rs = pst.executeQuery();
				while (rs.next()) {
					gxVoucherId = rs.getString(1);
					orgNumber = rs.getString(2);
					year = rs.getString(3);
					Voucher voucher = new Voucher();
					voucher.setAttachments(rs.getInt(8));
					voucher.setCashier("NONE");
					voucher.setDate(rs.getString(5));
					voucher.setExplanation(rs.getString(11));
					voucher.setGroup(orgNumber+rs.getString(7));
					voucher.setHandler("");
					voucher.setNumber(0);
					voucher.setPeriod(Integer.parseInt(rs.getString(4)));
					voucher.setPoster("NONE");
					voucher.setPreparer(rs.getString(9));
					voucher.setReference("");
					voucher.setSerialNum(0);
					voucher.setTransDate(new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("yyyyMMdd").parse(rs.getString(5))));
					voucher.setYear(rs.getInt(3));
					pst2 = conn.prepareStatement("select ZWFZYS_ID,ZWPZFL_KMBH,ZWPZFL_ZY,ZWPZFL_JZFX,ZWFZYS_YSBH,ZWFZYS_BMBH,ZWFZYS_WLDWBH,ZWFZYS_ZGBH,ZWFZYS_CPBH,ZWFZYS_XMBH1,ZWFZYS_XMBH2,ZWFZYS_XMBH3,ZWFZYS_XMBH4,ZWFZYS_XMBH5,ZWFZYS_XMBH6,ZWFZYS_XMBH7,ZWFZYS_XMBH8,ZWFZYS_XMBH9,ZWFZYS_XMBH10,ZWFZYS_XMBH11,ZWFZYS_XMBH12,ZWFZYS_XMBH13,ZWFZYS_XMBH14,ZWFZYS_XMBH15,ZWFZYS_WBBH,ZWFZYS_SL,ZWFZYS_DJ,ZWFZYS_WB,ZWFZYS_HL,ZWFZYS_JE,ZWFZYS_YWRQ,ZWFZYS_PJH,ZWFZYS_YT from t_ESB_Voucher where ZWPZK_PZNM=? and ZWPZK_DWBH=? and ZWPZK_KJND=?");
					pst2.setString(1, gxVoucherId);
					pst2.setString(2, orgNumber);
					pst2.setString(3, year);
					rs2 = pst2.executeQuery();
					List<VoucherBody> list = new ArrayList<VoucherBody>();
					List<CashFlow> cfList = new ArrayList<CashFlow>();
					while (rs2.next()) {
						VoucherBody body = new VoucherBody();
						body.setAccountName("");
						body.setAccountNumber(rs2.getString(2));
						body.setAmount(rs2.getFloat(30));
						body.setAmountFor(rs2.getFloat(30)*rs2.getFloat(29));
						// 如果外币编号为空，则币别默认为RMB
						if(rs2.getString(25)==null || rs2.getString(25).equals("")) {
							body.setCurrencyName("人民币");
							body.setCurrencyNumber("RMB");
						}else {
							body.setCurrencyName("");
							body.setCurrencyNumber(rs2.getString(25));
						}
						// 共享 1：借 2：贷		K3 1：借 0：贷
						if(rs.getString(4).equals("1")) {
							body.setDC(1);
						}else {
							body.setDC(0);
						}
						body.setEntryId(rs2.getRow());
						body.setExchangeRate(rs2.getFloat(29));
						body.setExplanation(rs2.getString(3));
						body.setMeasureUnit(null);
						body.setMeasureUnitUUID(null);
						body.setQuantity(rs2.getFloat(26));
						body.setSettleNo(null);
						body.setSettleTypeName("");
						body.setTransNo("");
						body.setUnitPrice(rs2.getFloat(27));
						
						List<AccountProject> apList = new ArrayList<AccountProject>();
						// 部门编号
						if(rs2.getString(6)!=null || !rs2.getString(6).equals("")) {
							AccountProject acctProject = new AccountProject();
							acctProject.setDetailName("");
							acctProject.setDetailNumber(rs2.getString(6));
							acctProject.setDetailUUID("{" + UUID.randomUUID().toString().toUpperCase() + "}");
							acctProject.setTypeName("");
							acctProject.setTypeNumber("002");
							apList.add(acctProject);
						}
						// 往来单位编号
						if(rs2.getString(7)!=null || !rs2.getString(7).equals("")) {
							AccountProject acctProject = new AccountProject();
							acctProject.setDetailName("");
							acctProject.setDetailNumber(rs2.getString(7));
							acctProject.setDetailUUID("{" + UUID.randomUUID().toString().toUpperCase() + "}");
							acctProject.setTypeName("");
							acctProject.setTypeNumber("");
							apList.add(acctProject);
						}
						// 职工编号
						if(rs2.getString(8)!=null || !rs2.getString(8).equals("")) {
							AccountProject acctProject = new AccountProject();
							acctProject.setDetailName("");
							acctProject.setDetailNumber(rs2.getString(8));
							acctProject.setDetailUUID("{" + UUID.randomUUID().toString().toUpperCase() + "}");
							acctProject.setTypeName("");
							acctProject.setTypeNumber("003");
							apList.add(acctProject);
						}
						// 产品编号
						if(rs2.getString(9)!=null || !rs2.getString(9).equals("")) {
							AccountProject acctProject = new AccountProject();
							acctProject.setDetailName("");
							acctProject.setDetailNumber(rs2.getString(9));
							acctProject.setDetailUUID("{" + UUID.randomUUID().toString().toUpperCase() + "}");
							acctProject.setTypeName("");
							acctProject.setTypeNumber("004");
							apList.add(acctProject);
						}
						
						body.setAcctList(apList);
						list.add(body);
						
						if(rs2.getString(10)!=null || !rs2.getString(10).equals("")) {
							CashFlow cashFlow = new CashFlow();
							cashFlow.setAccName("");
							cashFlow.setAccNumber(rs2.getString(2));
							cashFlow.setAmount(rs2.getFloat(30));
							cashFlow.setAmountFor(rs2.getFloat(30)*rs2.getFloat(29));
							cashFlow.setClassName("现金流量项目");
							cashFlow.setClassNumber("i009");
							// 如果外币编号为空，则币别默认为RMB
							if(rs2.getString(25)==null || rs2.getString(25).equals("")) {
								cashFlow.setCurrencyName("人民币");
								cashFlow.setCurrencyNumber("RMB");
							}else {
								cashFlow.setCurrencyName("");
								cashFlow.setCurrencyNumber(rs2.getString(25));
							}
							cashFlow.setEntryid(rs2.getRow());
							cashFlow.setEntryid2(Integer.parseInt(rs2.getString(11)));
							cashFlow.setItemName("");
							cashFlow.setItemNumber(rs2.getString(10));
							cashFlow.setSubClassName(null);
							cashFlow.setSubClassNumber(null);
							cashFlow.setSubItemName(null);
							cashFlow.setSubItemNumber(null);
							cfList.add(cashFlow);
						}
					}
					voucher.setCashFlow(cfList);
					voucher.setBodyList(list);
					
					vd.setReplace("false");
					vd.setVoucher(voucher);
				}
			}
		} catch (Exception e) {
			Logger.info(e.getMessage());
		}finally {
			DBUtils.closeConnection(conn, pst, rs);
			DBUtils.closeConnection(conn, pst2, rs2);
		}
		
		String param = JSON.toJSONString(vd, SerializerFeature.WriteMapNullValue);
		
		// 发送 GET 请求
		String authorityCode = "4f03ba08c7d87ece76858af449ad24e0f9a2ad3bafafe148";
		String token = HttpUtil.sendGet("http://172.16.7.153/K3API/Token/Create", "authorityCode=" + authorityCode);
				
		// 发送POST请求
		String response = HttpUtil.sendPost("http://172.16.7.153/K3API/VoucherData/UpdateVoucher?token="
						+ JSON.parseObject(JSON.parseObject(token).get("Data").toString()).get("Token"), param);
		JaxWsDynamicClientFactory clientFactory =JaxWsDynamicClientFactory.newInstance();
		Client client = clientFactory.createClient("http://172.16.2.139/cwbase/service/jzstandiface/VoucherGenerateResultService.asmx?wsdl");
		JSONObject resultObj = JSONObject.parseObject(response);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if(resultObj.getIntValue("StatusCode") == 200) {
			// 此处去数据库读取数据
			erpVoucherCode="";
			erpVoucherId = resultObj.getString("Data").substring(12);
			generateFlag = true;
		}else {
			generateMsg = resultObj.getString("Data");
			generateFlag = false;
		}
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		resultMap.put("voucherId", gxVoucherId);
		resultMap.put("erpVoucherCode", erpVoucherCode);
		resultMap.put("erpVoucherId", erpVoucherId);
		resultMap.put("generateMsg", generateMsg);
		resultMap.put("generateFlag", generateFlag);
		resultMap.put("generateTime", dateFormat.format(calendar.getTime()));
		JSONObject resultJson = new JSONObject(resultMap);
		try {
			Object[] result = client.invoke("SetVoucherGenerateResult",resultJson.toJSONString());
			for (Object json : result) {
				if (((JSONObject) JSON.toJSON(json)).getBooleanValue("resultState")) {
					conn = DBUtils.getConnection();
					if(conn!=null) {
						pst = conn.prepareStatement("update t_ESB_Voucher set IsRead = 1,erpVoucherId=? where ZWPZK_PZNM=? and ZWPZK_DWBH=? and ZWPZK_KJND=?");
						pst.setInt(1, Integer.parseInt(erpVoucherId));
						pst.setString(2, gxVoucherId);
						pst.setString(3, orgNumber);
						pst.setString(4, year);
						pst.execute();
					}
				}
				else {
					Logger.info(((JSONObject) JSON.toJSON(json)).getString("resultMessage"));
				}
			}
		} catch (Exception e) {
			Logger.info(e.getMessage());
		}finally {
			DBUtils.closeConnection(conn, pst, null);
		}
		return "";
	}
}
