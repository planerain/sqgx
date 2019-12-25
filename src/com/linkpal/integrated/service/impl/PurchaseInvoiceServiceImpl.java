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
@WebService(serviceName = "PurchaseInvoiceService")
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
				pst = conn.prepareStatement("insert into t_ESB_PurchaseInvoice(RKJS_NM,RKJS_DJBH,RKJS_MIXNUMBER,RKJS_RQ,RKJS_DWBH,RKJS_BMBH,RKJS_ZDRYGBH,"
						+ "RKJS_CSR,RKJS_CSRQ,RKJS_JHR,RKJS_JHRQ,RKJS_FHR,RKJS_FHRQ,RKJS_ZY,RKJS_FJZS,RKJS_JSHJ,JSMX_NM,JSMX_GYSBH,JSMX_ZY,JSMX_FPHM,JSMX_SFID,"
						+ "JSMX_WLBH,JSMX_SL,JSMX_SLDW,JSMX_DJ,JSMX_BHSJE,JSMX_SLV,JSMX_SE,JSMX_JSHJ,JSMX_GGXH,JSMX_ZKJC,JSMX_KB,JSMX_BZ,JSMX_HLV,JSMX_RKRQ,JSMX_GLRQ,"
						+ "JSMX_JSKJ,JSMX_ZHKM,JSMX_RZZT,JSMX_TZDH,JSMX_CGDDH,JSMX_HH,JSMX_FKFF,JSMX_FPLB,JSMX_AREA,JSMX_FPLX,JSMX_FKBZ,JSMX_OU,JSMX_HLLX,JSMX_HLRQ,JSMX_FPHJE,JSMX_COMPANY,JSMX_BM,JSMX_CSRQ) "
						+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
				pst.setString(1, purchaseInvoiceDataObj.getString("RKJS_NM"));
				pst.setString(2, purchaseInvoiceDataObj.getString("RKJS_DJBH"));
				pst.setString(3, purchaseInvoiceDataObj.getString("RKJS_MIXNUMBER"));
				pst.setString(4, purchaseInvoiceDataObj.getString("RKJS_RQ"));
				pst.setString(5, purchaseInvoiceDataObj.getString("RKJS_DWBH"));
				pst.setString(6, purchaseInvoiceDataObj.getString("RKJS_BMBH"));
				pst.setString(7, purchaseInvoiceDataObj.getString("RKJS_ZDRYGBH"));
				pst.setString(8, purchaseInvoiceDataObj.getString("RKJS_CSR"));
				pst.setString(9, purchaseInvoiceDataObj.getString("RKJS_CSRQ")==null?"":purchaseInvoiceDataObj.getString("RKJS_CSRQ"));
				pst.setString(10, purchaseInvoiceDataObj.getString("RKJS_JHR"));
				pst.setString(11, purchaseInvoiceDataObj.getString("RKJS_JHRQ")==null?"":purchaseInvoiceDataObj.getString("RKJS_JHRQ"));
				pst.setString(12, purchaseInvoiceDataObj.getString("RKJS_FHR"));
				pst.setString(13, purchaseInvoiceDataObj.getString("RKJS_FHRQ")==null?"":purchaseInvoiceDataObj.getString("RKJS_FHRQ"));
				pst.setString(14, purchaseInvoiceDataObj.getString("RKJS_ZY"));
				pst.setInt(15, purchaseInvoiceDataObj.getIntValue("RKJS_FJZS"));
				pst.setFloat(16, purchaseInvoiceDataObj.getFloatValue("RKJS_JSHJ"));
				JSONArray entries = purchaseInvoiceDataObj.getJSONArray("JSMX");
				for (int i = 0; i < entries.size(); i++) {
					JSONObject entryData = entries.getJSONObject(i);
					pst.setString(17, entryData.getString("JSMX_NM"));
					pst.setString(18, entryData.getString("JSMX_GYSBH"));
					pst.setString(19, entryData.getString("JSMX_ZY"));
					pst.setString(20, entryData.getString("JSMX_FPHM"));
					pst.setString(21, entryData.getString("JSMX_SFID"));
					pst.setString(22, entryData.getString("JSMX_WLBH"));
					pst.setInt(23, entryData.getIntValue("JSMX_SL"));
					pst.setString(24, entryData.getString("JSMX_SLDW"));
					pst.setFloat(25, entryData.getFloatValue("JSMX_DJ"));
					pst.setFloat(26, entryData.getFloatValue("JSMX_BHSJE"));
					pst.setString(27, entryData.getString("JSMX_SLV"));
					pst.setFloat(28, entryData.getFloatValue("JSMX_SE"));
					pst.setFloat(29, entryData.getFloatValue("JSMX_JSHJ"));
					pst.setString(30, entryData.getString("JSMX_GGXH"));
					pst.setString(31, entryData.getString("JSMX_ZKJC"));
					pst.setString(32, entryData.getString("JSMX_KB"));
					pst.setString(33, entryData.getString("JSMX_BZ"));
					pst.setFloat(34, entryData.getFloatValue("JSMX_HLV"));
					pst.setString(35, entryData.getString("JSMX_RKRQ"));
					pst.setString(36, entryData.getString("JSMX_GLRQ")==null?"":entryData.getString("JSMX_GLRQ"));
					pst.setString(37, entryData.getString("JSMX_JSKJ"));
					pst.setString(38, entryData.getString("JSMX_ZHKM"));
					pst.setString(39, entryData.getString("JSMX_RZZT"));
					pst.setString(40, entryData.getString("JSMX_TZDH"));
					pst.setString(41, entryData.getString("JSMX_CGDDH"));
					pst.setString(42, entryData.getString("JSMX_HH"));
					pst.setString(43, entryData.getString("JSMX_FKFF"));
					pst.setString(44, entryData.getString("JSMX_FPLB"));
					pst.setString(45, entryData.getString("JSMX_AREA"));
					pst.setString(46, entryData.getString("JSMX_FPLX"));
					pst.setString(47, entryData.getString("JSMX_FKBZ"));
					pst.setString(48, entryData.getString("JSMX_OU"));
					pst.setString(49, entryData.getString("JSMX_HLLX"));
					pst.setString(50, entryData.getString("JSMX_HLRQ")==null?"":entryData.getString("JSMX_HLRQ"));
					pst.setFloat(51, entryData.getFloatValue("JSMX_FPHJE"));
					pst.setString(52, entryData.getString("JSMX_COMPANY"));
					pst.setString(53, entryData.getString("JSMX_BM"));
					pst.setString(54, entryData.getString("JSMX_CSRQ")==null?"":entryData.getString("JSMX_CSRQ"));
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
				pst = conn.prepareStatement("select distinct top 1 RKJS_MIXNUMBER,JSMX_GYSBH,JSMX_GLRQ,JSMX_BZ,JSMX_HLV,JSMX_ZHKM,RKJS_ZY,RKJS_JSHJ,RKJS_BMBH,RKJS_ZDRYGBH,JSMX_JSKJ from t_ESB_PurchaseInvoice where IsRead=0");
				rs = pst.executeQuery();
				while (rs.next()) {
					billNo = rs.getString(1);
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
					page1JsonObj.put("FFincDate", rs.getString(3));
					// 汇率类型
					JSONObject exchangeRateTypeObj = new JSONObject();
					exchangeRateTypeObj.put("FNumber","01");
					exchangeRateTypeObj.put("FName","");
					page1JsonObj.put("FExchangeRateType", exchangeRateTypeObj);
					// 事务类型
					page1JsonObj.put("FClassTypeID", 1000004);
					// 币别  ************
					JSONObject currencyObj = new JSONObject();
					currencyObj.put("FNumber","");
					currencyObj.put("FName",rs.getString(4));
					page1JsonObj.put("FCurrencyID", currencyObj);
					// 汇率
					page1JsonObj.put("FExchangeRate", rs.getFloat(5));
					// 年利率(％)
					page1JsonObj.put("FInterestRate", 0.0);
					// 往来科目  ************ rs.getString(6) 
					JSONObject acttObj = new JSONObject();
					acttObj.put("FNumber","1123.01");
					acttObj.put("FName","");
					page1JsonObj.put("FAcctID", acttObj);
					// 调整汇率
					page1JsonObj.put("FAdjustExchangeRate", rs.getFloat(5));
					// 采购方式
					JSONObject poStyleObj = new JSONObject();
					poStyleObj.put("FNumber","");
					poStyleObj.put("FName","");
					page1JsonObj.put("FPOStyle", poStyleObj);
					// 摘要
					page1JsonObj.put("FNote", rs.getString(7));
					// 备注（合同号）
					page1JsonObj.put("FCompactNo", "");
					// 总成本额
					page1JsonObj.put("FTotalCostFor", rs.getFloat(8));
					// 总成本额（本位币）
					page1JsonObj.put("FTotalCost", rs.getFloat(8)*rs.getFloat(5));
					// 源单类型
					page1JsonObj.put("FSourceBillType", "");
					// 源单编号
					page1JsonObj.put("FSourceBillNo1", "");
					// 部门
					JSONObject deptObj = new JSONObject();
					deptObj.put("FNumber",rs.getString(9));
					deptObj.put("FName","");
					page1JsonObj.put("FDeptID", deptObj);
					// 业务员  ************ rs.getString(10)
					JSONObject empObj = new JSONObject();
					empObj.put("FNumber","");
					empObj.put("FName","");
					page1JsonObj.put("FEmpID", empObj);
					// 制单人 ************ rs.getString(10)
					JSONObject billerObj = new JSONObject();
					billerObj.put("FNumber","Administrator");
					billerObj.put("FName","Administrator");
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
					// 审核 rs.getString(11)
					JSONObject checkerObj = new JSONObject();
					checkerObj.put("FNumber","Administrator");
					checkerObj.put("FName","Administrator");
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
					page1JsonObj.put("FBillStatus", 0);
					// 抵扣税额
					page1JsonObj.put("FOffsetTaxFor", 0);
					// 抵扣税额（本位币）
					page1JsonObj.put("FOffsetTax", 0);
					// 自由项1
					page1JsonObj.put("FFreeItem3", "");
					// 红蓝字标志
					page1JsonObj.put("FROB", 1);
					// 单据类型
					page1JsonObj.put("FTranType", 604);
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
					page1JsonObj.put("FRemainAmountForBill", rs.getFloat(8));
					// 单据本位币未核销金额
					page1JsonObj.put("FRemainAmountBill", rs.getFloat(8)*rs.getFloat(5));
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
					page1JsonObj.put("FJSBillNo", "");
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
					page2JsonObj.put("FAmountFor", rs.getFloat(8));
					// 原始单据内码
					page2JsonObj.put("FOrgID", 0);
					// 付款金额（本位币）
					page2JsonObj.put("FAmount2", rs.getFloat(8)*rs.getFloat(5));
					// 剩余金额(本位币)
					page2JsonObj.put("FRemainAmount", 0);
					// 剩余金额（原币）
					page2JsonObj.put("FRemainAmountFor", 0);
					// 系统类型
					page2JsonObj.put("FRP", 0);
					page2Array.add(page2JsonObj);
					
					pst2 = conn.prepareStatement("select JSMX_WLBH,JSMX_SLDW,JSMX_SL,JSMX_DJ,JSMX_SLV,JSMX_JSHJ,JSMX_ZKJC,JSMX_HH,JSMX_SE,JSMX_SFID from t_ESB_PurchaseInvoice where RKJS_MIXNUMBER=?");
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
						itemObj.put("FNumber", rs2.getString(1));
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
						// 单位  ************
						JSONObject unitObj = new JSONObject();
						unitObj.put("FNumber", "");
						unitObj.put("FName", rs2.getString(2));
						page3JsonObj.put("FUnitID", unitObj);
						// 基本单位
						page3JsonObj.put("FBaseUnit", "");
						// 数量
						page3JsonObj.put("FAuxQty", rs2.getFloat(3));
						// 付款申请关联数量
						page3JsonObj.put("FQuantityPayApply_Commit", 0);
						// 收付款关联数量
						page3JsonObj.put("FQuantityReceive_Commit", rs2.getFloat(3));
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
						page3JsonObj.put("FAuxPrice", rs2.getFloat(4));
						// 含税单价
						page3JsonObj.put("FAuxTaxPrice", rs2.getFloat(4)*(1+rs2.getFloat(5)));
						// 折扣率(％)
						page3JsonObj.put("FDiscountRate", rs2.getFloat(7)/rs2.getFloat(6)*100);
						// 实际含税单价
						if(rs2.getFloat(7)==0.0) {
							page3JsonObj.put("FPriceDiscount", rs2.getFloat(4)*(1+rs2.getFloat(5)));
						}else {
							page3JsonObj.put("FPriceDiscount", rs2.getFloat(4)*(1+rs2.getFloat(5))*(rs2.getFloat(7)/rs2.getFloat(6)));
						}
						// 源单内码
						page3JsonObj.put("FSourceInterId", 0);
						// 折扣额
						page3JsonObj.put("FAmtDiscount", rs2.getFloat(7));
						// 折扣额（本位币）
						page3JsonObj.put("FStdAmtDiscount", rs2.getFloat(7)*rs.getFloat(5));
						// 整单折扣分配额
						page3JsonObj.put("FEntryDisCount", 0);
						// 整单折扣分配额(本位币)
						page3JsonObj.put("FStdEntryDisCount", 0);
						// 整单折前金额
						page3JsonObj.put("FAmountExceptDisCount", rs.getFloat(8));
						// 整单折前金额(本位币)
						page3JsonObj.put("FStdAmountExceptDisCount", rs.getFloat(8)*rs.getFloat(5));
						// 金额
						page3JsonObj.put("FAmount3", rs2.getFloat(6)-rs2.getFloat(9));
						// 金额(本位币)
						page3JsonObj.put("FStdAmount", (rs2.getFloat(6)-rs2.getFloat(9))*rs.getFloat(5));
						// 基本单价
						page3JsonObj.put("FPrice", rs2.getFloat(4));
						// 基本单位数量
						page3JsonObj.put("FQty", rs2.getFloat(3));
						// 含税价
						page3JsonObj.put("FTaxPrice", rs2.getFloat(4)*(1+rs2.getFloat(5)));
						// 税率(％)
						page3JsonObj.put("FTaxRate", rs2.getFloat(5)*100);
						// 合同行号
						page3JsonObj.put("FContractEntryID", 0);
						// 源分录ID
						page3JsonObj.put("FEntryID_SRC", rs2.getInt(8));
						// 税额
						page3JsonObj.put("FTaxAmount", rs2.getFloat(9));
						// 付款申请付款金额
						page3JsonObj.put("FPayReqPayAmountFor", 0);
						// 订单类型
						page3JsonObj.put("FOrderType", 0);
						// 税额（本位币）
						page3JsonObj.put("FStdTaxAmount", rs2.getFloat(9)*rs.getFloat(5));
						// 订单行号
						page3JsonObj.put("FOrderEntryID", 0);
						// 订单内码
						page3JsonObj.put("FOrderInterID", 0);
						// 价税合计
						page3JsonObj.put("FAmountIncludeTax", rs2.getFloat(6));
						// 价税合计（本位币）
						page3JsonObj.put("FStdAmountIncludeTax", rs2.getFloat(6)*rs.getFloat(5));
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
						page3JsonObj.put("FSourceBillNo", rs2.getString(10));
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
						page3JsonObj.put("FRemainAmountForEntry", rs2.getFloat(6)-rs2.getFloat(9));
						// 未核销金额(本位币)
						page3JsonObj.put("FRemainAmountEntry", (rs2.getFloat(6)-rs2.getFloat(9))*rs.getFloat(5));
						// 累计核销数量
						page3JsonObj.put("FCheckQty", 0);
						// 未核销数量
						page3JsonObj.put("FRemainQty", rs2.getFloat(3));
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
						page3JsonObj.put("FSourceEntryID", rs2.getInt(8));
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
		String authorityCode = "cc7c2a733c0a29ece004ca4aaa31fa27a349134496f8c1d0";
		String token = HttpUtil.sendGet("http://172.16.7.191/K3API/Token/Create", "authorityCode=" + authorityCode);
		
		// 发送POST请求
		String response = HttpUtil.sendPost("http://172.16.7.191/K3API/Bill1000004/Save?token="
						+ JSON.parseObject(JSON.parseObject(token).get("Data").toString()).get("Token"), param);
		JSONObject resultObj = JSONObject.parseObject(response);
		if(resultObj.getIntValue("StatusCode") == 200) {
			try {
				conn = DBUtils.getConnection();
				if (conn!=null) {
					pst = conn.prepareStatement("update t_ESB_PurchaseInvoice set IsRead=1 where RKJS_MIXNUMBER=?");
					pst.setString(1, billNo);
					pst.execute();
				}
			} catch (Exception e) {
				Logger.info(e.getMessage());
			}finally {
				DBUtils.closeConnection(conn, pst, null);
			}
		}
		Logger.info("返回报文为："+resultObj.toJSONString());
		return resultObj.toJSONString();
	}

}
