package com.linkpal.integrated.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.linkpal.integrated.service.PurchaseInvoiceService;
import com.linkpal.integrated.util.DBUtils;
import com.linkpal.integrated.util.HttpUtil;

/**
 * Description:应付单接口实现类
 *
 * @author lichao
 * @date 2019年12月19日 上午10:18:58
 * 
 * 接收过来的数据先存入中间表()，然后从中间表读取数据
 */
@WebService
@BindingType(javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class PurchaseInvoiceServiceImpl implements PurchaseInvoiceService {
	private Connection conn = null;
	private PreparedStatement pst = null;
	private ResultSet rs = null;
	private PreparedStatement pst2 = null;
	private ResultSet rs2 = null;
	
	// 共享组合号(K3发票号)
	String billNo;
	
	private static final Logger Logger = LoggerFactory.getLogger(PurchaseInvoiceServiceImpl.class);

	@Override
	public String getPurchaseInvoiceData(String purchaseInvoiceData) {
		Logger.info("接收到的应付单数据为:"+purchaseInvoiceData);
		JSONObject purchaseInvoiceDataObj = JSONObject.parseObject(purchaseInvoiceData);
		try {
			conn = DBUtils.getConnection();
			if(conn!=null) {
				pst = conn.prepareStatement("insert into t_ESB_PurchaseInvoice(JSMX_FPHM,JSMX_GYSBH,FPB_KPRQ,JSMX_GLRQ,JSMX_FKBZ,JSMX_HLV,JSMX_ZHKM,JSMX_SM,RKJS_BMBH,RKJS_USERCODE,JSMX_JSKJ,JSMX_HLLX,"
						+ "RKJS_DWBH,JSMX_MIXNUMBER,RKJS_JSHJFPJE,JSMX_DJ,JSMX_HH,JSMX_JSHJ,JSMX_SFID,JSMX_SL,JSMX_SLDW,JSMX_SLV,JSMX_WLBH,JSMX_ZKJC) "
						+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
				pst.setString(1, purchaseInvoiceDataObj.getString("JSMX_FPHM"));
				pst.setString(2, purchaseInvoiceDataObj.getString("JSMX_GYSBH"));
				pst.setString(3, purchaseInvoiceDataObj.getString("FPB_KPRQ"));
				pst.setString(4, purchaseInvoiceDataObj.getString("JSMX_GLRQ"));
				pst.setString(5, purchaseInvoiceDataObj.getString("JSMX_FKBZ"));
				pst.setFloat(6, purchaseInvoiceDataObj.getFloatValue("JSMX_HLV"));
				pst.setString(7, purchaseInvoiceDataObj.getString("JSMX_ZHKM"));
				pst.setString(8, purchaseInvoiceDataObj.getString("JSMX_SM"));
				pst.setString(9, purchaseInvoiceDataObj.getString("RKJS_BMBH"));
				pst.setString(10, purchaseInvoiceDataObj.getString("RKJS_USERCODE"));
				pst.setString(11, purchaseInvoiceDataObj.getString("JSMX_JSKJ"));
				pst.setString(12, purchaseInvoiceDataObj.getString("JSMX_HLLX"));
				pst.setString(13, purchaseInvoiceDataObj.getString("RKJS_DWBH"));
				pst.setString(14, purchaseInvoiceDataObj.getString("JSMX_MIXNUMBER"));
				pst.setFloat(15, purchaseInvoiceDataObj.getFloatValue("RKJS_JSHJFPJE"));
				JSONArray entries = purchaseInvoiceDataObj.getJSONArray("");
				for (int i = 0; i < entries.size(); i++) {
					JSONObject entryData = entries.getJSONObject(i);
					pst.setFloat(16, entryData.getFloatValue("JSMX_DJ"));
					pst.setString(17, entryData.getString("JSMX_HH"));
					pst.setFloat(18, entryData.getFloatValue("JSMX_JSHJ"));
					pst.setString(19, entryData.getString("JSMX_SFID"));
					pst.setString(20, entryData.getString("JSMX_SL"));
					pst.setString(21, entryData.getString("JSMX_SLDW"));
					pst.setString(22, entryData.getString("JSMX_SLV"));
					pst.setString(23, entryData.getString("JSMX_WLBH"));
					pst.setString(24, entryData.getString("JSMX_ZKJC"));
					pst.execute();
				}
			}
		} catch (Exception e) {
			Logger.info(e.getMessage());
		}finally {
			DBUtils.closeConnection(conn, pst, null);
		}
		
		JSONObject sendJsonObject = new JSONObject();
		
		JSONObject dataJsonObj = new JSONObject();
		
		JSONObject page1JsonObj = null;
		JSONObject page2JsonObj = null;
		JSONObject page3JsonObj = null;
		
		JSONArray page1Array = new JSONArray();
		JSONArray page2Array = new JSONArray();
		JSONArray page3Array = new JSONArray();
		try {
			conn = DBUtils.getConnection();
			if(conn!=null) {
				pst = conn.prepareStatement("select distinct top 1 JSMX_FPHM,JSMX_GYSBH,FPB_KPRQ,JSMX_GLRQ,JSMX_FKBZ,JSMX_HLV,JSMX_ZHKM,JSMX_SM,RKJS_BMBH,RKJS_USERCODE,JSMX_JSKJ,JSMX_HLLX,RKJS_DWBH,JSMX_MIXNUMBER,RKJS_JSHJFPJE from t_ESB_PurchaseInvoice where IsRead=0");
				rs = pst.executeQuery();
				while (rs.next()) {
					billNo = rs.getString(14);
					page1JsonObj = new JSONObject();
					// 发票号码
					page1JsonObj.put("FBillNo", billNo);
					// 核算项目类别
					JSONObject itemClassObj= new JSONObject();
					itemClassObj.put("FNumber","008");
					itemClassObj.put("FName","");
					page1JsonObj.put("FItemClassID",itemClassObj);
					// 核算项目
					JSONObject supplyObj = new JSONObject();
					supplyObj.put("FNumber",rs.getString(2));
					supplyObj.put("FName","");
					page1JsonObj.put("FSupplyID", supplyObj);
					// 开票日期
					page1JsonObj.put("FDate", rs.getString(3));
					// 财务日期
					page1JsonObj.put("FFincDate", rs.getString(4));
					// 汇率类型
					JSONObject exchangeRateTypeObj = new JSONObject();
					exchangeRateTypeObj.put("FNumber","01");
					exchangeRateTypeObj.put("FName","");
					page1JsonObj.put("FExchangeRateType", exchangeRateTypeObj);
					// 事务类型
					page1JsonObj.put("FClassTypeID", 1000004);
					// 币别
					JSONObject currencyObj = new JSONObject();
					currencyObj.put("FNumber",rs.getString(5));
					currencyObj.put("FName","");
					page1JsonObj.put("FCurrencyID", currencyObj);
					// 汇率
					page1JsonObj.put("FExchangeRate", rs.getFloat(6));
					// 年利率(％)
					page1JsonObj.put("FInterestRate", 0.0);
					// 往来科目
					JSONObject acttObj = new JSONObject();
					acttObj.put("FNumber",rs.getString(7));
					acttObj.put("FName","");
					page1JsonObj.put("FAcctID", acttObj);
					// 调整汇率
					page1JsonObj.put("FAdjustExchangeRate", rs.getFloat(6));
					// 采购方式
					JSONObject poStyleObj = new JSONObject();
					poStyleObj.put("FNumber","");
					poStyleObj.put("FName","");
					page1JsonObj.put("FPOStyle", poStyleObj);
					// 摘要
					page1JsonObj.put("FNote", rs.getString(8));
					// 备注（合同号）
					page1JsonObj.put("FCompactNo", "");
					// 总成本额
					page1JsonObj.put("FTotalCostFor", rs.getFloat(15));
					// 总成本额（本位币）
					page1JsonObj.put("FTotalCost", rs.getFloat(15)*rs.getFloat(6));
					// 源单类型
					page1JsonObj.put("FSourceBillType", "");
					// 源单编号
					page1JsonObj.put("FSourceBillNo1", "");
					// 部门
					JSONObject deptObj = new JSONObject();
					deptObj.put("FNumber",rs.getString(9));
					deptObj.put("FName","");
					page1JsonObj.put("FDeptID", deptObj);
					// 业务员
					JSONObject empObj = new JSONObject();
					empObj.put("FNumber",rs.getString(10));
					empObj.put("FName","");
					page1JsonObj.put("FEmpID", empObj);
					// 制单人
					JSONObject billerObj = new JSONObject();
					billerObj.put("FNumber",rs.getString(10));
					billerObj.put("FName","");
					page1JsonObj.put("FBillerID", billerObj);
					// 项目资源
					JSONObject resourceObj = new JSONObject();
					resourceObj.put("FNumber","");
					resourceObj.put("FName","");
					page1JsonObj.put("FResourceID", resourceObj);
					// 开票人
					page1JsonObj.put("FBillerName", "");
					// 项目任务
					JSONObject taskObj = new JSONObject();
					taskObj.put("FNumber","");
					taskObj.put("FName","");
					page1JsonObj.put("FTaskID", taskObj);
					// 审核
					JSONObject checkerObj = new JSONObject();
					checkerObj.put("FNumber",rs.getString(11));
					checkerObj.put("FName","");
					page1JsonObj.put("FCheckerID", checkerObj);
					// 概算金额
					page1JsonObj.put("FBudgetAmountFor", 0.0);
					// 记账
					page1JsonObj.put("FPosterID", 0);
					// 项目订单
					JSONObject orderObj = new JSONObject();
					orderObj.put("FNumber","");
					orderObj.put("FName","");
					page1JsonObj.put("FOrderID", orderObj);
					// 开户银行
					page1JsonObj.put("FBank", "");
					// 项目订单金额
					page1JsonObj.put("FOrderAmountFor", "");
					// 地址
					page1JsonObj.put("FAddress", "");
					// 凭证字号
					page1JsonObj.put("FVchInterID", 0);
					// 纳税登记号
					page1JsonObj.put("FTaxNum", "");
					// 银行账号
					page1JsonObj.put("FAccount", "");
					// 费用总额
					page1JsonObj.put("FTotalExpenseFor", 0);
					// 费用总额（本位币）
					page1JsonObj.put("FTotalExpense", 0);
					// 核销状态
					page1JsonObj.put("FBillStatus", "");
					// 抵扣税额
					page1JsonObj.put("FOffsetTaxFor", 0);
					// 抵扣税额（本位币）
					page1JsonObj.put("FOffsetTax", 0);
					// 自由项1
					page1JsonObj.put("FFreeItem3", "");
					// 红蓝字标志
					page1JsonObj.put("FROB", "");
					// 单据类型
					page1JsonObj.put("FTranType", "");
					// 自由项2
					page1JsonObj.put("FFreeItem4", "");
					// 资产凭证字号
					page1JsonObj.put("FVoucherInterID", "");
					// 核销
					page1JsonObj.put("FArApStatus", 7);
					// 累计调汇金额
					page1JsonObj.put("FAdjustAmount", 0);
					// 年份
					page1JsonObj.put("FYear", Integer.parseInt(new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("yyyyMMdd").parse(rs.getString(3))).split("-")[0]));
					// 期间
					page1JsonObj.put("FPeriod", Integer.parseInt(new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("yyyyMMdd").parse(rs.getString(3))).split("-")[1]));
					// 打印次数
					page1JsonObj.put("FPrintCount", 0);
					// 子系统
					page1JsonObj.put("FSubSystemID", "1");
					// 钩稽期间
					page1JsonObj.put("FYearPeriod", "");
					// 钩稽人
					JSONObject hookerObj = new JSONObject();
					hookerObj.put("FNumber","");
					hookerObj.put("FName","");
					page1JsonObj.put("FHookerID", hookerObj);
					// 地区
					page1JsonObj.put("FRegion", null);
					// 行业
					page1JsonObj.put("FTrade", null);
					// 单据累计核销金额
					page1JsonObj.put("FCheckAmountForBill", 0);
					// 单据本位币累计核销金额
					page1JsonObj.put("FCheckAmountBill", 0);
					// 单据未核销金额
					page1JsonObj.put("FRemainAmountForBill", 0);
					// 单据本位币未核销金额
					page1JsonObj.put("FRemainAmountBill", 0);
					// 最后核销日期
					page1JsonObj.put("FCheckDate", "");
					// 状态
					page1JsonObj.put("FStatus", 0);
					// 整单折扣参数
					page1JsonObj.put("FSysStatus", 2);
					// 汇率获取方式
					page1JsonObj.put("FObtainRateWay", 0);
					// 冻结标志
					page1JsonObj.put("FLockBill", 0);
					// 基础资料
					JSONObject baseObj = new JSONObject();
					baseObj.put("FNumber","");
					baseObj.put("FName","");
					page1JsonObj.put("FBase", baseObj);
					// 基础资料属性
					page1JsonObj.put("FBaseProperty", "");
					// 税控发票号
					page1JsonObj.put("FJSBillNo", rs.getString(1));
					// 付款条件
					JSONObject payConditionObj = new JSONObject();
					payConditionObj.put("FNumber","");
					payConditionObj.put("FName","");
					page1JsonObj.put("FPayCondition", payConditionObj);
					// 税控日期
					page1JsonObj.put("FJSDate", rs.getString(3));
					// 整单折扣额
					page1JsonObj.put("FBillDiscount", 0);
					// 整单折扣率(%)
					page1JsonObj.put("FBillDiscountRate", 0);
					page1Array.add(page1JsonObj);
					
					page2JsonObj = new JSONObject();
					// 行号
					page2JsonObj.put("FEntryID2", 1);
					// 应付日期
					page2JsonObj.put("fdate_2", rs.getString(3));
					// 付款金额
					page2JsonObj.put("FAmountFor", rs.getFloat(15));
					// 原始单据内码
					page2JsonObj.put("FOrgID", 131);
					// 付款金额（本位币）
					page2JsonObj.put("FAmount2", rs.getFloat(15)*rs.getFloat(6));
					// 剩余金额(本位币)
					page2JsonObj.put("FRemainAmount", 0);
					// 剩余金额（原币）
					page2JsonObj.put("FRemainAmountFor", 0);
					// 系统类型
					page2JsonObj.put("FRP", 0);
					page2Array.add(page2JsonObj);
					
					pst2 = conn.prepareStatement("select JSMX_DJ,JSMX_HH,JSMX_JSHJ,JSMX_SFID,JSMX_SL,JSMX_SLDW,JSMX_SLV,JSMX_WLBH,JSMX_ZKJC from t_ESB_PurchaseInvoice where JSMX_MIXNUMBER=?");
					pst2.setString(1, billNo);
					rs2 = pst2.executeQuery();
					while (rs2.next()) {
						page3JsonObj = new JSONObject();
						// 自由项1
						page3JsonObj.put("FFreeItem3_3", "");
						// 自由项2
						page3JsonObj.put("FFreeItem4_3", "");
						// 累计已核销已关联数量
						page3JsonObj.put("FLinkCheckQty", 0);
						// 累计已核销已关联金额
						page3JsonObj.put("FLinkCheckAmountFor", 0);
						// 累计已核销已关联金额(本位币)
						page3JsonObj.put("FLinkCheckAmount", "");
						// 行号
						page3JsonObj.put("FEntryID3", rs2.getRow());
						// 产品代码
						JSONObject itemObj = new JSONObject();
						itemObj.put("FNumber", rs2.getString(8));
						itemObj.put("FName", "");
						page3JsonObj.put("FItemID", itemObj);
						// 产品名称
						page3JsonObj.put("FItemID36422", "");
						// 规格型号
						page3JsonObj.put("FItemID36436", "");
						// 辅助属性
						JSONObject auxPropObj = new JSONObject();
						auxPropObj.put("FNumber", "");
						auxPropObj.put("FName", "");
						page3JsonObj.put("FAuxPropID", auxPropObj);
						// 单位
						JSONObject unitObj = new JSONObject();
						unitObj.put("FNumber", rs2.getString(6));
						unitObj.put("FName", "");
						page3JsonObj.put("FUnitID", unitObj);
						// 基本单位
						page3JsonObj.put("FBaseUnit", "");
						// 数量
						page3JsonObj.put("FAuxQty", rs2.getFloat(5));
						// 付款申请关联数量
						page3JsonObj.put("FQuantityPayApply_Commit", 0);
						// 收付款关联数量
						page3JsonObj.put("FQuantityReceive_Commit", rs2.getFloat(5));
						// 辅助单位
						JSONObject secUnitObj = new JSONObject();
						secUnitObj.put("FNumber", "");
						secUnitObj.put("FName", "");
						page3JsonObj.put("FSecUnitID", secUnitObj);
						// 换算率
						page3JsonObj.put("FsecCoefficient", 0);
						// 辅助单位数量
						page3JsonObj.put("FSecQty", 0);
						// 单价
						page3JsonObj.put("FAuxPrice", rs2.getFloat(1));
						// 含税单价
						page3JsonObj.put("FAuxTaxPrice", rs2.getFloat(1)*(1+0.01*rs2.getFloat(7)));
						// 折扣率(％)
						page3JsonObj.put("FDiscountRate", rs2.getFloat(9)/rs2.getFloat(3)*100);
						// 实际含税单价
						if(rs2.getFloat(9)==0.0) {
							page3JsonObj.put("FPriceDiscount", rs2.getFloat(1)*(1+0.01*rs2.getFloat(7)));
						}else {
							page3JsonObj.put("FPriceDiscount", rs2.getFloat(1)*(1+0.01*rs2.getFloat(7))*(rs2.getFloat(9)/rs2.getFloat(3)));
						}
						// 源单内码
						page3JsonObj.put("FSourceInterId", 0);
						// 折扣额
						page3JsonObj.put("FAmtDiscount", rs2.getFloat(9));
						// 折扣额（本位币）
						page3JsonObj.put("FStdAmtDiscount", rs2.getFloat(9)*rs.getFloat(6));
						// 整单折扣分配额
						page3JsonObj.put("FEntryDisCount", 0);
						// 整单折扣分配额(本位币)
						page3JsonObj.put("FStdEntryDisCount", 0);
						// 整单折前金额
						page3JsonObj.put("FAmountExceptDisCount", rs.getFloat(15));
						// 整单折前金额(本位币)
						page3JsonObj.put("FStdAmountExceptDisCount", rs.getFloat(15)*rs.getFloat(6));
						// 金额
						page3JsonObj.put("FAmount3", rs2.getFloat(3));
						// 金额(本位币)
						page3JsonObj.put("FStdAmount", rs2.getFloat(3)*rs.getFloat(6));
						// 基本单价
						page3JsonObj.put("FPrice", rs2.getFloat(1));
						// 基本单位数量
						page3JsonObj.put("FQty", rs2.getFloat(5));
						// 含税价
						page3JsonObj.put("FTaxPrice", rs2.getFloat(1)*(1+0.01*rs2.getFloat(7)));
						// 税率(％)
						page3JsonObj.put("FTaxRate", rs2.getFloat(7));
						// 合同行号
						page3JsonObj.put("FContractEntryID", 0);
						// 源分录ID
						page3JsonObj.put("FEntryID_SRC", rs2.getInt(2));
						// 税额
						page3JsonObj.put("FTaxAmount", rs2.getFloat(3)*rs2.getFloat(7));
						// 付款申请付款金额
						page3JsonObj.put("FPayReqPayAmountFor", 0);
						// 订单类型
						page3JsonObj.put("FOrderType", 0);
						// 税额（本位币）
						page3JsonObj.put("FStdTaxAmount", rs2.getFloat(3)*rs2.getFloat(7)*rs.getFloat(6));
						// 订单行号
						page3JsonObj.put("FOrderEntryID", 0);
						// 订单内码
						page3JsonObj.put("FOrderInterID", 0);
						// 价税合计
						page3JsonObj.put("FAmountIncludeTax", rs2.getFloat(3));
						// 价税合计（本位币）
						page3JsonObj.put("FStdAmountIncludeTax", rs2.getFloat(3)*rs.getFloat(6));
						// 整单折前价税合计
						page3JsonObj.put("FAllAmountExceptDisCount", 0);
						// 整单折前价税合计(本位币)
						page3JsonObj.put("FStdAllAmountExceptDisCount", 0);
						// 应计费用（本位币）
						page3JsonObj.put("FAmountMust", 0);
						// 运费税金（本位币）
						page3JsonObj.put("FDeductTax", 0);
						// 订单单价
						page3JsonObj.put("FAuxOrderPrice", 0);
						// 基本计量单位订单单价
						page3JsonObj.put("FOrderPrice", 0);
						// 备注
						page3JsonObj.put("FNote_3", "");
						// 批号
						page3JsonObj.put("FBatchNo", "");
						// 源单类型
						JSONObject classIdSrcObj = new JSONObject();
						classIdSrcObj.put("FNumber", "");
						classIdSrcObj.put("FName", "");
						page3JsonObj.put("FClassID_SRC", classIdSrcObj);
						// 源单单号
						page3JsonObj.put("FSourceBillNo", rs2.getString(4));
						// 付款申请关联金额(原币)
						page3JsonObj.put("FPayApplyAmountFor", 0);
						// 合同号
						page3JsonObj.put("FContractBillNo", "");
						// 付款申请关联金额(本币)
						page3JsonObj.put("FPayApplyAmount", 0);
						// 采购订单号
						page3JsonObj.put("FOrderBillNo", "");
						// 累计核销金额
						page3JsonObj.put("FCheckAmountFor", 0);
						// 累计核销金额(本位币)
						page3JsonObj.put("FCheckAmount", 0);
						// 未核销金额
						page3JsonObj.put("FRemainAmountForEntry", rs2.getFloat(3));
						// 未核销金额(本位币)
						page3JsonObj.put("FRemainAmountEntry", rs2.getFloat(3)*rs.getFloat(6));
						// 累计核销数量
						page3JsonObj.put("FCheckQty", 0);
						// 未核销数量
						page3JsonObj.put("FRemainQty", rs2.getFloat(5));
						// 红字发票已关联已核销金额(本位币)
						page3JsonObj.put("FInvLinkCheckAmount", 0);
						// 红字发票关联金额(本位币)
						page3JsonObj.put("FInvoiceAmount", "");
						// 红字发票关联金额(原币)
						page3JsonObj.put("FInvoiceAmountFor", 0);
						// 红字发票已关联已核销金额(原币)
						page3JsonObj.put("FInvLinkCheckAmountFor", 0);
						// 红字发票已关联已核销数量
						page3JsonObj.put("FInvLinkCheckQty", 0);
						// 红字发票关联数量
						page3JsonObj.put("FInvoiceQty", 0);
						// 源单类型
						page3JsonObj.put("FSourceTranType", "");
						// 付款关联金额
						page3JsonObj.put("FAmountFor_Commit", 0);
						// 付款关联金额(本位币)
						page3JsonObj.put("FAmount_Commit", 0);
						// 源单行号
						page3JsonObj.put("FSourceEntryID", rs2.getInt(2));
						page3Array.add(page3JsonObj);
					}
					
				}
			}
		} catch (Exception e) {
			Logger.info(e.getMessage());
		}finally {
			DBUtils.closeConnection(conn, pst, rs);
			DBUtils.closeConnection(conn, pst2, rs2);
		}
		
		dataJsonObj.put("Page1", page1Array);
		dataJsonObj.put("Page2", page2Array);
		dataJsonObj.put("Page3", page3Array);
		
		sendJsonObject.put("Data", dataJsonObj);
		
		String param = JSON.toJSONString(sendJsonObject, SerializerFeature.WriteMapNullValue);
		Logger.info("向接口发送的数据为:"+param);
		// 发送 GET 请求
		String authorityCode = "4f03ba08c7d87ece76858af449ad24e0f9a2ad3bafafe148";
		String token = HttpUtil.sendGet("http://172.16.7.153/K3API/Token/Create", "authorityCode=" + authorityCode);
		
		// 发送POST请求
		String response = HttpUtil.sendPost("http://172.16.7.153/K3API/Bill1000004/Save?token="
						+ JSON.parseObject(JSON.parseObject(token).get("Data").toString()).get("Token"), param);
		JSONObject resultObj = JSONObject.parseObject(response);
		if(resultObj.getIntValue("StatusCode") == 200) {
			try {
				conn = DBUtils.getConnection();
				if (conn!=null) {
					pst = conn.prepareStatement("update t_ESB_PurchaseInvoice set IsRead=1 where JSMX_MIXNUMBER=?");
					pst.setString(1, billNo);
					pst.execute();
				}
			} catch (Exception e) {
				Logger.info(e.getMessage());
			}finally {
				DBUtils.closeConnection(conn, pst, null);
			}
		}
		Logger.info("返回报文为：");
		return null;
	}

}
