package com.linkpal.integrated.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

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
import com.linkpal.integrated.util.CommonUtil;
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
	private PreparedStatement pst3 = null;
	private ResultSet rs3 = null;
	private PreparedStatement pst4 = null;
	private ResultSet rs4 = null;
	
	// 凭证类型
	String voucherType;
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
	//执行结果码
	String resultCode;
	private static final Logger Logger = LoggerFactory.getLogger(VoucherServiceImpl.class);

	@Override
	public String getVoucherData(String voucherData) throws ParseException {
		Logger.info("接收到的凭证数据为:"+voucherData);
		JSONObject voucherDataObj = JSONObject.parseObject(voucherData);
		
		try {
			conn = DBUtils.getConnection();
			if(conn!=null) {
				// 总共52个字段，实际表中需要增加Id(自增)，CreateDate(当前时间)，IsRead(默认为0),erpVoucherId
				pst=conn.prepareStatement("insert into t_ESB_Voucher(VoucherID,ZWPZK_DWBH,ZWPZK_KJND,ZWPZK_KJQJ,ZWPZK_PZRQ,ZWPZK_PZBH,ZWPZK_LXBH,ZWPZK_FJZS,ZWPZK_CSR,ZWPZK_CSRMC,ZWPZK_FSR,ZWPZK_FSRMC,ZWPZK_TZDZS,ZWPZK_ZY,ZWPZK_JE,ZWPZK_DJBH,ZWFZYS_WBBH,ZWFZYS_HL,CREATEDTIME,ZWPZK_ZHID,ZWFZYS_ID,ZWPZFL_KMBH,ZWPZFL_ZY,"
						+ "ZWPZFL_JZFX,ZWFZYS_YSBH,ZWFZYS_BMBH,ZWFZYS_WLDWBH,ZWFZYS_ZGBH,ZWFZYS_CPBH,ZWFZYS_XMBH1,ZWFZYS_XMBH2,ZWFZYS_XMBH3,ZWFZYS_XMBH4,ZWFZYS_XMBH5,ZWFZYS_XMBH6,ZWFZYS_XMBH7,ZWFZYS_XMBH8,ZWFZYS_XMBH9,ZWFZYS_XMBH10,ZWFZYS_XMBH11,ZWFZYS_XMBH12,"
						+ "ZWFZYS_XMBH13,ZWFZYS_XMBH14,ZWFZYS_XMBH15,ZWFZYS_SL,ZWFZYS_DJ,ZWFZYS_WB,ZWFZYS_JE,ZWFZYS_YWRQ,ZWFZYS_PJH,ZWFZYS_YT,ZWFZYS_FSGLF) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
				pst.setString(1, voucherDataObj.getString("VoucherID"));
				pst.setString(2, voucherDataObj.getString("ZWPZK_DWBH"));
				pst.setString(3, voucherDataObj.getString("ZWPZK_KJND"));
				pst.setString(4, voucherDataObj.getString("ZWPZK_KJQJ"));
				pst.setString(5, voucherDataObj.getString("ZWPZK_PZRQ"));
				pst.setString(6, voucherDataObj.getString("ZWPZK_PZBH"));
				pst.setString(7, voucherDataObj.getString("ZWPZK_LXBH"));
				pst.setInt(8, voucherDataObj.getIntValue("ZWPZK_FJZS"));
				pst.setString(9, voucherDataObj.getString("ZWPZK_CSR"));
				pst.setString(10, voucherDataObj.getString("ZWPZK_CSRMC"));
				pst.setString(11, voucherDataObj.getString("ZWPZK_FSR"));
				pst.setString(12, voucherDataObj.getString("ZWPZK_FSRMC"));
				pst.setInt(13, voucherDataObj.getIntValue("ZWPZK_TZDZS"));
				pst.setString(14, voucherDataObj.getString("ZWPZK_ZY"));
				pst.setFloat(15, voucherDataObj.getFloatValue("ZWPZK_JE"));
				pst.setString(16, voucherDataObj.getString("ZWPZK_DJBH"));
				pst.setString(17, voucherDataObj.getString("ZWFZYS_WBBH"));
				pst.setFloat(18, voucherDataObj.getFloatValue("ZWFZYS_HL"));
				pst.setString(19, voucherDataObj.getString("CREATEDTIME"));
				pst.setString(20, voucherDataObj.getString("ZWPZK_ZHID"));
				JSONArray entries = voucherDataObj.getJSONArray("ZWPZFL");
				for (int i = 0; i < entries.size(); i++) {
					JSONObject entryData = entries.getJSONObject(i);
					pst.setString(21, entryData.getString("ZWFZYS_ID"));
					pst.setString(22, entryData.getString("ZWPZFL_KMBH"));
					pst.setString(23, entryData.getString("ZWPZFL_ZY"));
					pst.setString(24, entryData.getString("ZWPZFL_JZFX"));
					pst.setString(25, entryData.getString("ZWFZYS_YSBH"));
					pst.setString(26, entryData.getString("ZWFZYS_BMBH"));
					pst.setString(27, entryData.getString("ZWFZYS_WLDWBH"));
					pst.setString(28, entryData.getString("ZWFZYS_ZGBH"));
					pst.setString(29, entryData.getString("ZWFZYS_CPBH"));
					pst.setString(30, entryData.getString("ZWFZYS_XMBH1"));
					pst.setString(31, entryData.getString("ZWFZYS_XMBH2"));
					pst.setString(32, entryData.getString("ZWFZYS_XMBH3"));
					pst.setString(33, entryData.getString("ZWFZYS_XMBH4"));
					pst.setString(34, entryData.getString("ZWFZYS_XMBH5"));
					pst.setString(35, entryData.getString("ZWFZYS_XMBH6"));
					pst.setString(36, entryData.getString("ZWFZYS_XMBH7"));
					pst.setString(37, entryData.getString("ZWFZYS_XMBH8"));
					pst.setString(38, entryData.getString("ZWFZYS_XMBH9"));
					pst.setString(39, entryData.getString("ZWFZYS_XMBH10"));
					pst.setString(40, entryData.getString("ZWFZYS_XMBH11"));
					pst.setString(41, entryData.getString("ZWFZYS_XMBH12"));
					pst.setString(42, entryData.getString("ZWFZYS_XMBH13"));
					pst.setString(43, entryData.getString("ZWFZYS_XMBH14"));
					pst.setString(44, entryData.getString("ZWFZYS_XMBH15"));
					pst.setFloat(45, entryData.getFloatValue("ZWFZYS_SL"));
					pst.setFloat(46, entryData.getFloatValue("ZWFZYS_DJ"));
					pst.setFloat(47, entryData.getFloatValue("ZWFZYS_WB"));
					pst.setFloat(48, entryData.getFloatValue("ZWFZYS_JE"));
					pst.setString(49, entryData.getString("ZWFZYS_YWRQ"));
					pst.setString(50, entryData.getString("ZWFZYS_PJH"));
					pst.setString(51, entryData.getString("ZWFZYS_YT"));
					pst.setString(52, entryData.getString("ZWFZYS_FSGLF"));
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
				pst = conn.prepareStatement("select distinct top 1 	VoucherID,ZWPZK_DWBH,ZWPZK_KJND,ZWPZK_KJQJ,ZWPZK_PZRQ,ZWPZK_PZBH,ZWPZK_LXBH,ZWPZK_FJZS,ZWPZK_CSR,ZWPZK_CSRMC,ZWPZK_FSR,ZWPZK_FSRMC,ZWPZK_TZDZS,ZWPZK_ZY,ZWPZK_JE,ZWPZK_DJBH,ZWFZYS_WBBH,ZWFZYS_HL,CREATEDTIME,ZWPZK_ZHID from t_ESB_Voucher where IsRead=0");
				rs = pst.executeQuery();
				while (rs.next()) {
					gxVoucherId = rs.getString(1);
					orgNumber = rs.getString(2);
					year = rs.getString(3);
					voucherType = rs.getString(7);
					Voucher voucher = new Voucher();
					voucher.setAttachments(rs.getInt(8));
					voucher.setCashier("NONE");
					voucher.setDate(new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("yyyyMMdd").parse(rs.getString(5))));
					voucher.setExplanation(rs.getString(14));
					voucher.setGroup(orgNumber+rs.getString(7));
					voucher.setHandler("");
					voucher.setNumber(CommonUtil.getNo(orgNumber, voucherType));
				    voucher.setPeriod(Integer.parseInt(rs.getString(4))); 
					voucher.setPoster("NONE");
					//voucher.setPreparer(rs.getString(9));
					voucher.setPreparer("");
					voucher.setReference("");
					voucher.setSerialNum(0);
					voucher.setTransDate(new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("yyyyMMdd").parse(rs.getString(5))));
					voucher.setVoucherID(0);
					voucher.setYear(Integer.parseInt(rs.getString(3))); 
					//voucher.setZWPZK_CSRMC(rs.getString(10));
					//voucher.setZWPZK_FSRMC(rs.getString(12));
					pst2 = conn.prepareStatement("select ZWFZYS_ID,ZWPZFL_KMBH,ZWPZFL_ZY,ZWPZFL_JZFX,ZWFZYS_YSBH,ZWFZYS_BMBH,ZWFZYS_WLDWBH,ZWFZYS_ZGBH,ZWFZYS_CPBH,ZWFZYS_XMBH1,ZWFZYS_XMBH2,ZWFZYS_XMBH3,ZWFZYS_XMBH4,ZWFZYS_XMBH5,ZWFZYS_XMBH6,ZWFZYS_XMBH7,ZWFZYS_XMBH8,ZWFZYS_XMBH9,ZWFZYS_XMBH10,ZWFZYS_XMBH11,ZWFZYS_XMBH12,ZWFZYS_XMBH13,ZWFZYS_XMBH14,ZWFZYS_XMBH15,ZWFZYS_SL,ZWFZYS_DJ,ZWFZYS_WB,ZWFZYS_JE,ZWFZYS_YWRQ,ZWFZYS_PJH,ZWFZYS_YT,ZWFZYS_FSGLF,ZWFZYS_WBBH,ZWFZYS_HL from t_ESB_Voucher where  VoucherID=? and ZWPZK_DWBH=? and ZWPZK_KJND=?");
					pst2.setString(1, gxVoucherId);
					pst2.setString(2, orgNumber);
					pst2.setString(3, year);
					rs2 = pst2.executeQuery();
					List<VoucherBody> list = new ArrayList<VoucherBody>();
					List<CashFlow> cfList = new ArrayList<CashFlow>();
					while (rs2.next()) {
						VoucherBody body = new VoucherBody();
						body.setAccountName("");
						body.setAccountNumber(rs2.getString(2).substring(0,4)+"."+rs2.getString(2).substring(4, 6)+"."+rs2.getString(2).substring(6, 8)+"."+rs2.getString(2).substring(8, 10));
						body.setAmount(rs2.getFloat(28)*rs2.getFloat(34));
						body.setAmountFor(rs2.getFloat(28));
						// 如果外币编号为空，则币别默认为RMB
						if(rs2.getString(33)==null || rs2.getString(33).equals("")) {
							body.setCurrencyName("人民币");
							body.setCurrencyNumber("RMB");
						}else {
							body.setCurrencyName("");
							body.setCurrencyNumber(rs2.getString(33));
						}
						// 共享 1：借 2：贷		K3 1：借 0：贷
						if(rs2.getString(4).equals("1")) {
							body.setDC(1);
						}else {		
							body.setDC(0);
						}
						body.setEntryId(rs2.getRow()-1);
						body.setExchangeRate(rs2.getFloat(34));
						body.setExplanation(rs2.getString(3));
						body.setMeasureUnit(null);
						body.setMeasureUnitUUID(null);
						body.setQuantity(rs2.getFloat(25));
						body.setSettleNo(null);
						body.setSettleTypeName("");
						body.setTransNo("");
						body.setUnitPrice(rs2.getFloat(26));
						
						List<AccountProject> apList = new ArrayList<AccountProject>();
						// 部门编号
						if(rs2.getString(6)!=null && !rs2.getString(6).equals("")) {
							AccountProject acctProject = new AccountProject();
							acctProject.setDetailName("");
							acctProject.setDetailNumber(rs2.getString(6));
							acctProject.setDetailUUID("{" + UUID.randomUUID().toString().toUpperCase() + "}");
							acctProject.setTypeName("部门");
							acctProject.setTypeNumber("002");
							apList.add(acctProject);
						}
						// 往来单位编号
						if(rs2.getString(7)!=null && !rs2.getString(7).equals("")) {
							AccountProject acctProject = new AccountProject();
							acctProject.setDetailName("");
							acctProject.setDetailNumber(rs2.getString(7));
							acctProject.setDetailUUID("{" + UUID.randomUUID().toString().toUpperCase() + "}");
							pst3 = conn.prepareStatement("select FItemClassID from t_Item where FNumber=? and FItemClassID in (1,8)");
							pst3.setString(1, rs2.getString(7));
							rs3 = pst3.executeQuery();
							while (rs3.next()) {
								if(rs3.getInt(1) == 1) {
									acctProject.setTypeName("客户");
									acctProject.setTypeNumber("001");
								}else {
									acctProject.setTypeName("供应商");
									acctProject.setTypeNumber("008");
								}
							}
							apList.add(acctProject);
						}
						// 职工编号
						if(rs2.getString(8)!=null && !rs2.getString(8).equals("")) {
							AccountProject acctProject = new AccountProject();
							acctProject.setDetailName("");
							acctProject.setDetailNumber(rs2.getString(8));
							acctProject.setDetailUUID("{" + UUID.randomUUID().toString().toUpperCase() + "}");
							acctProject.setTypeName("职员");
							acctProject.setTypeNumber("003");
							apList.add(acctProject);
						}
						// 产品编号
						if(rs2.getString(17)!=null && !rs2.getString(17).equals("")) {
							AccountProject acctProject = new AccountProject();
							acctProject.setDetailName("");
							acctProject.setDetailNumber(rs2.getString(9));
							acctProject.setDetailUUID("{" + UUID.randomUUID().toString().toUpperCase() + "}");
							acctProject.setTypeName("物料");
							acctProject.setTypeNumber("004");
							apList.add(acctProject);
						}
						// 项目号(工程项目)
						if(rs2.getString(11)!=null && !rs2.getString(11).equals("")) {
							AccountProject acctProject = new AccountProject();
							acctProject.setDetailName("");
							acctProject.setDetailNumber(rs2.getString(11));
							acctProject.setDetailUUID("{" + UUID.randomUUID().toString().toUpperCase() + "}");
							acctProject.setTypeName("工程项目");
							acctProject.setTypeNumber("2039");
							apList.add(acctProject);
						}
						// 增减变动
						if(rs2.getString(12)!=null && !rs2.getString(12).equals("")) {
							AccountProject acctProject = new AccountProject();
							acctProject.setDetailName("");
							acctProject.setDetailNumber(rs2.getString(12));
							acctProject.setDetailUUID("{" + UUID.randomUUID().toString().toUpperCase() + "}");
							acctProject.setTypeName("增减变动");
							acctProject.setTypeNumber("14");
							apList.add(acctProject);
						}
						// 借款性质
						if(rs2.getString(13)!=null && !rs2.getString(13).equals("")) {
							AccountProject acctProject = new AccountProject();
							acctProject.setDetailName("");
							acctProject.setDetailNumber(rs2.getString(13));
							acctProject.setDetailUUID("{" + UUID.randomUUID().toString().toUpperCase() + "}");
							acctProject.setTypeName("借款性质");
							acctProject.setTypeNumber("");
							apList.add(acctProject);
						}
						// 银行账号
						if(rs2.getString(14)!=null && !rs2.getString(14).equals("")) {
							AccountProject acctProject = new AccountProject();
							acctProject.setDetailName("");
							acctProject.setDetailNumber(rs2.getString(14));
							acctProject.setDetailUUID("{" + UUID.randomUUID().toString().toUpperCase() + "}");
							acctProject.setTypeName("银行账号");
							acctProject.setTypeNumber("2024");
							apList.add(acctProject);
						}
						// 预算号/技措号/技改号(要素项目)
						if(rs2.getString(15)!=null && !rs2.getString(15).equals("")) {
							AccountProject acctProject = new AccountProject();
							acctProject.setDetailName("");
							acctProject.setDetailNumber(rs2.getString(15));
							acctProject.setDetailUUID("{" + UUID.randomUUID().toString().toUpperCase() + "}");
							acctProject.setTypeName("要素项目");
							acctProject.setTypeNumber("2035");
							apList.add(acctProject);
						}
						// 内外销(参考段)
						if(rs2.getString(16)!=null && !rs2.getString(16).equals("")) {
							AccountProject acctProject = new AccountProject();
							acctProject.setDetailName("");
							acctProject.setDetailNumber(rs2.getString(16));
							acctProject.setDetailUUID("{" + UUID.randomUUID().toString().toUpperCase() + "}");
							acctProject.setTypeName("参考段");
							acctProject.setTypeNumber("999");
							apList.add(acctProject);
						}
						
						body.setAcctList(apList);
						list.add(body);
						
						if(rs2.getString(10)!=null && !rs2.getString(10).equals("")) {
							CashFlow cashFlow = new CashFlow();
							cashFlow.setAccName("");
							cashFlow.setAccNumber(rs2.getString(2).substring(0,4)+"."+rs2.getString(2).substring(4, 6)+"."+rs2.getString(2).substring(6, 8)+"."+rs2.getString(2).substring(8, 10));
							cashFlow.setAmount(rs2.getFloat(28)*rs2.getFloat(34));
							cashFlow.setAmountFor(rs2.getFloat(28));
							cashFlow.setClassName("现金流量项目");
							cashFlow.setClassNumber("i009");
							// 如果外币编号为空，则币别默认为RMB
							if(rs2.getString(33)==null || rs2.getString(33).equals("")) {
								cashFlow.setCurrencyName("人民币");
								cashFlow.setCurrencyNumber("RMB");
							}else {
								cashFlow.setCurrencyName("");
								cashFlow.setCurrencyNumber(rs2.getString(33));
							}
							cashFlow.setEntryid(rs2.getRow()-1);
							cashFlow.setEntryid2(0);
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
			DBUtils.closeConnection(conn, pst3, rs3);
			DBUtils.closeConnection(conn, pst4, rs4);
		}
		
		String param = JSON.toJSONString(vd, SerializerFeature.WriteMapNullValue);
		Logger.info("向接口发送的数据为:"+param);
		// 发送 GET 请求
		String authorityCode = "cc7c2a733c0a29ece004ca4aaa31fa27a349134496f8c1d0";
		String token = HttpUtil.sendGet("http://172.16.7.191/K3API/Token/Create", "authorityCode=" + authorityCode);
				
		// 发送POST请求
		String response = HttpUtil.sendPost("http://172.16.7.191/K3API/VoucherData/UpdateVoucher?token="
						+ JSON.parseObject(JSON.parseObject(token).get("Data").toString()).get("Token"), param);
		//JaxWsDynamicClientFactory clientFactory =JaxWsDynamicClientFactory.newInstance();
		//Client client = clientFactory.createClient("http://172.16.2.139/cwbase/service/jzstandiface/VoucherGenerateResultService.asmx?wsdl");
		JSONObject resultObj = JSONObject.parseObject(response);
		Logger.info("生成状态:"+resultObj.getString("Data"));
		//Map<String, Object> resultMap = new HashMap<String, Object>();
		if(resultObj.getIntValue("StatusCode") == 200) {
			// 此处去数据库读取数据
			erpVoucherId = resultObj.getString("Data").substring(12);
			try {
				conn = DBUtils.getConnection();
				if(conn!=null) {
					pst = conn.prepareStatement("select t2.FName+convert(varchar(10),t1.FNumber) FErpVoucherCode from t_Voucher t1 left join t_VoucherGroup t2 on t1.FGroupID=t2.FGroupID where t1.FVoucherID=?");
					pst.setInt(1, Integer.parseInt(erpVoucherId));
					rs = pst.executeQuery();
					while (rs.next()) {
						erpVoucherCode=rs.getString(1);
					}
				}
			} catch (Exception e) {
				Logger.info(e.getMessage());
			}finally {
				DBUtils.closeConnection(conn, pst, rs);
			}
			generateFlag = true;
			resultCode="01";
		}else {
			generateMsg = resultObj.getString("Data");
			generateFlag = false;
			resultCode="00";
		}
		//Calendar calendar = Calendar.getInstance();
		//SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		//resultMap.put("voucherId", gxVoucherId);
		//resultMap.put("erpVoucherCode", erpVoucherCode);
		//resultMap.put("erpVoucherId", erpVoucherId);
		//resultMap.put("generateMsg", generateMsg);
		//resultMap.put("generateFlag", generateFlag);
		//resultMap.put("generateTime", dateFormat.format(calendar.getTime()));
		//JSONObject resultJson = new JSONObject(resultMap);
		JSONObject responsemessage= new JSONObject();
		responsemessage.put("resultState",generateFlag);
		responsemessage.put("resultCode", resultCode);
		responsemessage.put("resultMessage",generateMsg);
		try {
			//Object[] result = client.invoke("SetVoucherGenerateResult",resultJson.toJSONString());
			//String json = result[0].toString();//resultState
//			if (JSONObject.parseObject(json).getBooleanValue("resultState")) {
//				conn = DBUtils.getConnection();
//				if(conn!=null) {
//					pst = conn.prepareStatement("update t_ESB_Voucher set IsRead = 1,erpVoucherId=? where VoucherId=? and ZWPZK_DWBH=? and ZWPZK_KJND=?");
//					pst.setInt(1, Integer.parseInt(erpVoucherId));
//					pst.setString(2, gxVoucherId);
//					pst.setString(3, orgNumber);
//					pst.setString(4, year);
//					pst.execute();
//				}
//			}
//			else {
//				Logger.info(JSONObject.parseObject(json).getString("resultMessage"));
//			}
			if(resultCode.equalsIgnoreCase("01")) {
				conn = DBUtils.getConnection();
				if(conn!=null) {
					pst = conn.prepareStatement("update t_ESB_Voucher set IsRead = 1,erpVoucherId=? where VoucherId=? and ZWPZK_DWBH=? and ZWPZK_KJND=?");
					pst.setInt(1, Integer.parseInt(erpVoucherId));
					pst.setString(2, gxVoucherId);
					pst.setString(3, orgNumber);
					pst.setString(4, year);
					pst.execute();
				}
			}
		} catch (Exception e) {
			Logger.info(e.getMessage());
		}finally {
			DBUtils.closeConnection(conn, pst, null);
		}
		return responsemessage.toString();
	}
}
