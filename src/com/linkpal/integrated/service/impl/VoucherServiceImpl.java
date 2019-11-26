package com.linkpal.integrated.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
import com.linkpal.integrated.entity.Result;
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
	private static final Logger Logger = LoggerFactory.getLogger(VoucherServiceImpl.class);

	@Override
	public String getVoucherData(String voucherData) throws ParseException {
		Logger.info("接收到的凭证数据为:"+voucherData);
		JSONObject voucherDataObj = JSONObject.parseObject(voucherData);
		
		try {
			conn = DBUtils.getConnection();
			if(conn!=null) {
				// 总共47个字段，实际表中需要增加Id(自增)，CreateDate(当前时间)，IsRead(默认为0)
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
				pst.setString(12, voucherDataObj.getString("ZWPZK_JE"));
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
		Voucher voucher = new Voucher();

		List<VoucherBody> list = new ArrayList<VoucherBody>();
		JSONArray Entries = voucherDataObj.getJSONArray("ZWPZFL");
		for (int i = 0; i < Entries.size(); i++) {
			JSONObject entryData = Entries.getJSONObject(i);
			VoucherBody body = new VoucherBody();
			body.setAccountName("");
			body.setAccountNumber(entryData.getString("ZWPZFL_KMBH"));
			String currencyNumber = entryData.getString("ZWFZYS_WBBH");
			if (currencyNumber == "RMB") {
				body.setCurrencyName("人民币");
				body.setCurrencyNumber("RMB");
				body.setAmount(entryData.getFloatValue("ZWFZYS_JE"));
				body.setAmountFor(entryData.getFloatValue("ZWFZYS_JE"));
			} else {
				body.setCurrencyName("");
				body.setCurrencyNumber(currencyNumber);
				body.setAmount(entryData.getFloatValue("ZWFZYS_JE"));
				body.setAmountFor(entryData.getFloatValue("ZWFZYS_JE"));
			}
			String dc = entryData.getString("ZWPZFL_JZFX");
			if (dc.equals("1")) {
				body.setDC(1);
			} else {
				body.setDC(0);
			}
			body.setEntryId(i);
			body.setExchangeRate(entryData.getFloatValue("ZWFZYS_HL"));
			// 冲2014/2/28记字第44号凭证 外购入库_滚珠
			body.setExplanation(entryData.getString("ZWPZFL_ZY"));
			body.setMeasureUnit(null);
			body.setMeasureUnitUUID(null);
			body.setQuantity(entryData.getFloatValue("ZWFZYS_SL"));
			body.setSettleNo(null);
			body.setSettleTypeName("");
			body.setTransNo("");
			body.setUnitPrice(entryData.getFloatValue("ZWFZYS_DJ"));

			List<AccountProject> apList = new ArrayList<AccountProject>();
			AccountProject acctProject = new AccountProject();
			acctProject.setDetailNumber(entryData.getString("ZWFZYS_XMBH1"));
			acctProject.setDetailName("");
			acctProject.setDetailUUID("{" + UUID.randomUUID().toString().toUpperCase() + "}");
			acctProject.setTypeNumber(entryData.getString("ZWFZYS_XMBH2"));
			acctProject.setTypeName("");
			apList.add(acctProject);
			body.setAcctList(apList);
			list.add(body);
		}

		List<CashFlow> cfList = new ArrayList<CashFlow>();
		for (int k = 0; k < Entries.size(); k++) {
			JSONObject entryData = Entries.getJSONObject(k);
			CashFlow cashFlow = new CashFlow();
			cashFlow.setAccName(entryData.getString("ZWFZYS_XMBH3"));
			cashFlow.setAccNumber(entryData.getString("ZWFZYS_XMBH4"));
			cashFlow.setAmount(entryData.getFloatValue("ZWFZYS_XMBH5"));
			cashFlow.setAmountFor(entryData.getFloatValue("ZWFZYS_XMBH6"));
			cashFlow.setClassName(entryData.getString("ZWFZYS_XMBH7"));
			cashFlow.setClassNumber(entryData.getString("ZWFZYS_XMBH8"));
			cashFlow.setCurrencyName(entryData.getString("ZWFZYS_XMBH9"));
			cashFlow.setCurrencyNumber(entryData.getString("ZWFZYS_XMBH10"));
			cashFlow.setEntryid(k);
			cashFlow.setEntryid2(entryData.getIntValue("ZWFZYS_XMBH11"));
			cashFlow.setItemName(entryData.getString("ZWFZYS_XMBH12"));
			cashFlow.setItemNumber(entryData.getString("ZWFZYS_XMBH13"));
			cashFlow.setSubClassName(null);
			cashFlow.setSubClassNumber(null);
			cashFlow.setSubItemName(null);
			cashFlow.setSubItemNumber(null);
			cfList.add(cashFlow);
		}

		voucher.setAttachments(voucherDataObj.getIntValue("ZWPZK_FJZS"));
		voucher.setCashier("NONE");
		voucher.setDate(new SimpleDateFormat("yyyy-MM-dd")
				.format(new SimpleDateFormat("yyyyMMdd").parse(voucherDataObj.getString("ZWPZK_PZRQ"))));
		// 冲2014/2/28记字第44号凭证 外购入库_滚珠
		voucher.setExplanation(voucherDataObj.getString("ZWPZK_ZY"));
		voucher.setGroup(voucherDataObj.getString("ZWPZK_LXBH"));
		voucher.setHandler("");
		voucher.setNumber(0);
		voucher.setPeriod(Integer.parseInt(voucherDataObj.getString("ZWPZK_KJQJ")));
		voucher.setPoster("NONE");
		voucher.setPreparer(voucherDataObj.getString("ZWPZK_ZDR"));
		voucher.setReference("");
		voucher.setSerialNum(0);

		voucher.setTransDate(new SimpleDateFormat("yyyy-MM-dd")
				.format(new SimpleDateFormat("yyyyMMdd").parse(voucherDataObj.getString("ZWPZK_PZRQ"))));
		voucher.setVoucherID(0);
		voucher.setYear(Integer.parseInt(voucherDataObj.getString("ZWPZK_KJND")));
		voucher.setCashFlow(cfList);
		voucher.setBodyList(list);

		vd.setReplace("false");
		vd.setVoucher(voucher);

		String param = JSON.toJSONString(vd, SerializerFeature.WriteMapNullValue);
		
		// 发送 GET 请求
		String authorityCode = "4f03ba08c7d87ece76858af449ad24e0f9a2ad3bafafe148";
		String token = HttpUtil.sendGet("http://172.16.7.153/K3API/Token/Create", "authorityCode=" + authorityCode);
				
		// 发送POST请求
		String response = HttpUtil.sendPost("http://172.16.7.153/K3API/VoucherData/UpdateVoucher?token="
						+ JSON.parseObject(JSON.parseObject(token).get("Data").toString()).get("Token"), param);
		
		JSONObject resultObj = JSONObject.parseObject(response);
		Result result = new Result();
		result.setUnitCode(voucherDataObj.getString("ZWPZK_DWBH"));
		result.setVoucherId(voucherDataObj.getString("ZWPZK_PZNM"));
		result.setFiscalYear(voucherDataObj.getString("ZWPZK_KJND"));
		result.setErpVoucherCode("");
		result.setErpVoucherId(resultObj.getString("Message").substring(12));
		result.setGenerateFlag("");
		result.setGenerateMsg(resultObj.getString("Message")+"--"+resultObj.getString("Data"));
		result.setGenerateTime("");
		//result.setVoucherId(voucherDataObj.getString("ZWPZK_PZNM"));
		//result.setOrgNumber(voucherDataObj.getString("ZWPZK_DWBH"));
		//result.setYear(voucherDataObj.getString("ZWPZK_KJND"));
		//result.setCode(resultObj.getString("StatusCode"));
		//result.setMessage(resultObj.getString("Message")+"--"+resultObj.getString("Data"));
		Logger.info("返回信息："+JSON.toJSONString(result,SerializerFeature.WriteMapNullValue));
		if(resultObj.getIntValue("StatusCode") == 200) {
			int erpVoucherId = Integer.parseInt(resultObj.getString("Message").substring(12));
			try {
				conn = DBUtils.getConnection();
				if(conn!=null) {
					pst = conn.prepareStatement("update t_voucher set FVoucherId_GX=? where FVoucherID=? and FYear=?");
					pst.setString(1, voucherDataObj.getString("ZWPZK_PZNM"));
					pst.setInt(2, erpVoucherId);
					pst.setInt(3, Integer.parseInt(voucherDataObj.getString("ZWPZK_KJQJ")));
					pst.execute();
				}
			} catch (Exception e) {
				Logger.info(e.getMessage());
			}finally {
				DBUtils.closeConnection(conn, pst, null);
			}
		}
		return JSON.toJSONString(result,SerializerFeature.WriteMapNullValue);
	}
}
