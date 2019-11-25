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
 *
 */
@WebService
@BindingType(javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class VoucherServiceImpl implements VoucherService {
	private Connection conn = null;
	private PreparedStatement pst = null;
	private ResultSet rs = null;
	private String explanation="";
	private String number="";
	private String date="";
	private String group="";
	private static final Logger Logger = LoggerFactory.getLogger(VoucherServiceImpl.class);

	@Override
	public String getVoucherData(String voucherData) throws ParseException {
		Logger.info("接收到的凭证数据为:"+voucherData);
		
		JSONObject voucherDataObj = JSONObject.parseObject(voucherData);
		boolean isReversal = voucherDataObj.containsKey("BANPZKNM");
		String voucherId="";
		int year = 0;
		if(isReversal) {
			voucherId = voucherDataObj.getString("BANPZKNM");
			year = Integer.parseInt(voucherDataObj.getString("ZWPZK_KJND"));
			try {
				conn = DBUtils.getConnection();
				if(conn!=null) {
					pst = conn.prepareStatement("select t1.FExplanation,t1.FNumber,t1.FDate,t2.FName from t_voucher t1 left join t_vouchergroup t2 on t1.fgroupid=t2.fgroupid where t1.FVoucherId_GX=? and t1.FYear=?");
					pst.setString(1, voucherId);
					pst.setInt(2, year);
					rs = pst.executeQuery();
					while (rs.next()) {
						explanation = rs.getString(1);
						number = rs.getString(2);
						date = rs.getString(3);
						group = rs.getString(4);
					}
				}
			} catch (Exception e) {
				Logger.info(e.getMessage());
			}finally {
				DBUtils.closeConnection(conn, pst, rs);
			}
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
			if(isReversal) {
				body.setExplanation("冲"+date+group+"字第"+number+"号凭证 "+explanation);
			}else {
				body.setExplanation(entryData.getString("ZWPZFL_ZY"));
			}
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
		if(isReversal) {
			voucher.setExplanation("冲"+date+group+"字第"+number+"号凭证 "+explanation);
		}else {
			voucher.setExplanation(voucherDataObj.getString("ZWPZK_ZY"));
		}
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
		result.setErpVoucherId("");
		result.setGenerateFlag("");
		result.setGenerateMsg("");
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
