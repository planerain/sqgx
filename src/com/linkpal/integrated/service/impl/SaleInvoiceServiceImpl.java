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
import com.linkpal.integrated.service.SaleInvoiceService;
import com.linkpal.integrated.util.DBUtils;
import com.linkpal.integrated.util.HttpUtil;

/**
 * Description:应收单接口实现类
 *
 * @author lichao
 * @date 2019年12月24日 上午9:40:12
 *
 */
@WebService(serviceName = "SaleInvoiceService")
@BindingType(javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class SaleInvoiceServiceImpl implements SaleInvoiceService {
	private Connection conn = null;
	private PreparedStatement pst = null;
	private ResultSet rs = null;
	private PreparedStatement pst2 = null;
	private ResultSet rs2 = null;
	
	// 共享组合号(K3发票号)
	String billNo;
	
	private static final Logger Logger = LoggerFactory.getLogger(SaleInvoiceServiceImpl.class);

	@Override
	public String getSaleInvoiceData(String saleInvoiceData) {
		Logger.info("接收到的应收单数据为:"+saleInvoiceData);
		JSONObject result = new JSONObject();
		JSONObject saleInvoiceDataDataObj = JSONObject.parseObject(saleInvoiceData);
		for (int i = 0; i < 1; i++) {
			String djbh = saleInvoiceDataDataObj.getString("RKJS_DJBH");
			String glDate = saleInvoiceDataDataObj.getJSONArray("JSMX").getJSONObject(i).getString("JSMX_GLRQ");
			String jskj = saleInvoiceDataDataObj.getJSONArray("JSMX").getJSONObject(i).getString("JSMX_JSKJ");
			if(glDate==null || glDate.equals("")) {
				result.put("Message", "");
				result.put("Data", "GL日期不能为空！");
				result.put("StatusCode", "");
			}else if (jskj==null || jskj.equals("")) {
				result.put("Message", "");
				result.put("Data", "经手会计不能为空！");
				result.put("StatusCode", "");
			}else {
				try {
					conn = DBUtils.getConnection();
					if(conn!=null) {
						pst2 = conn.prepareStatement("delete from t_ESB_SaleInvoice where RKJS_DJBH=?");
						pst2.setString(1, djbh);
						pst2.execute();
						pst = conn.prepareStatement("insert into t_ESB_SaleInvoice(RKJS_NM,RKJS_DJBH,RKJS_RQ,RKJS_DWBH,"
								+ "RKJS_ZDRYGBH,RKJS_JSHJ,RKJS_ZY,JSMX_NM,JSMX_GYSBH,JSMX_FPHM,JSMX_SFID,JSMX_WLBH,JSMX_SL,"
								+ "JSMX_SLDW,JSMX_DJ,JSMX_SLV,JSMX_SE,JSMX_JSHJ,JSMX_BZ,JSMX_HLV,JSMX_GLRQ,JSMX_JSKJ,JSMX_ZHKM,"
								+ "JSMX_HH,JSMX_HLLX,JSMX_CSRQ,JSMX_ZKJC,JSMX_MIXNUMBER,JSMX_GXM1,JSMX_GXM9,JSMX_GNM1,JSMX_GNM2,"
								+ "JSMX_GNM7,JSMX_SZ2,JSMX_GBZ3) "
								+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
						pst.setString(1, saleInvoiceDataDataObj.getString("RKJS_NM"));
						pst.setString(2, saleInvoiceDataDataObj.getString("RKJS_DJBH"));
						pst.setString(3, saleInvoiceDataDataObj.getString("RKJS_RQ"));
						pst.setString(4, saleInvoiceDataDataObj.getString("RKJS_DWBH"));
						pst.setString(5, saleInvoiceDataDataObj.getString("RKJS_ZDRYGBH"));
						pst.setDouble(6, saleInvoiceDataDataObj.getDoubleValue("RKJS_JSHJ"));
						pst.setString(7, saleInvoiceDataDataObj.getString("RKJS_ZY"));
						JSONArray entries = saleInvoiceDataDataObj.getJSONArray("JSMX");
						for (int j = 0; j < entries.size(); j++) {
							JSONObject entryData = entries.getJSONObject(j);
							pst.setString(8, entryData.getString("JSMX_NM"));
							pst.setString(9, entryData.getString("JSMX_GYSBH"));
							pst.setString(10, entryData.getString("JSMX_FPHM"));
							pst.setString(11, entryData.getString("JSMX_SFID"));
							pst.setString(12, entryData.getString("JSMX_WLBH"));
							pst.setInt(13, entryData.getIntValue("JSMX_SL"));
							pst.setString(14, entryData.getString("JSMX_SLDW"));
							pst.setDouble(15, entryData.getDoubleValue("JSMX_DJ"));
							pst.setString(16, entryData.getString("JSMX_SLV"));
							pst.setDouble(17, entryData.getDoubleValue("JSMX_SE"));
							pst.setDouble(18, entryData.getDoubleValue("JSMX_JSHJ"));
							pst.setString(19, entryData.getString("JSMX_BZ"));
							//pst.setDouble(20, entryData.getDoubleValue("JSMX_HLV"));
							pst.setFloat(20, 1.0f);
							pst.setString(21, entryData.getString("JSMX_GLRQ"));
							pst.setString(22, entryData.getString("JSMX_JSKJ"));
							pst.setString(23, entryData.getString("JSMX_ZHKM"));
							pst.setInt(24, entryData.getIntValue("JSMX_HH"));
							pst.setString(25, entryData.getString("JSMX_HLLX"));
							pst.setString(26, entryData.getString("JSMX_CSRQ"));
							pst.setString(27, entryData.getString("JSMX_ZKJC"));
							pst.setString(28, entryData.getString("JSMX_MIXNUMBER"));
							pst.setString(29, entryData.getString("JSMX_GXM1"));
							pst.setString(30, entryData.getString("JSMX_GXM9"));
							pst.setString(31, entryData.getString("JSMX_GNM1"));
							pst.setString(32, entryData.getString("JSMX_GNM2"));
							pst.setString(33, entryData.getString("JSMX_GNM7"));
							pst.setDouble(34, entryData.getDoubleValue("JSMX_SZ2"));
							pst.setString(35, entryData.getString("JSMX_GBZ3"));
							pst.execute();
						}
					}
				} catch (Exception e) {
					Logger.info(e.getMessage());
				}finally {
					DBUtils.closeConnection(conn, pst, null);
					DBUtils.closeConnection(conn, pst2, null);
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
						pst = conn.prepareStatement("select distinct top 1 JSMX_MIXNUMBER,JSMX_GYSBH,JSMX_GLRQ,JSMX_HLV,JSMX_BZ,JSMX_ZHKM,RKJS_ZY,RKJS_ZDRYGBH,RKJS_JSHJ,RKJS_DJBH from t_ESB_SaleInvoice where IsRead=0 and id = (select MAX(id) from t_ESB_SaleInvoice)");
						rs = pst.executeQuery();
						while (rs.next()) {
							billNo = rs.getString(10);
							page1JsonObj = new JSONObject();
							// 合同小号
							page1JsonObj.put("FHeadSelfI0457","");
							// 计生号
							page1JsonObj.put("FHeadSelfI0458","");
							// 稅控日期
							page1JsonObj.put("FJSDate",null);
							// 税控发票号
							page1JsonObj.put("FJSBillNo","");
							// 税控引出
							page1JsonObj.put("FJSExported",null);
							// 发票号码
							page1JsonObj.put("FBillNo",billNo);
							// 核算项目类别
							JSONObject FItemClassObj = new JSONObject();
							FItemClassObj .put("FNumber","001");
							FItemClassObj .put("FName","");
							page1JsonObj.put("FItemClassID", FItemClassObj);
							// 核算项目
							JSONObject FCustObj = new JSONObject();
							FCustObj .put("FNumber",rs.getString(2));
							FCustObj.put("FName","");
							page1JsonObj.put("FCustID", FCustObj);
							// 事务类型
							page1JsonObj.put("FClassTypeID",1000002);
							// 财务日期
							page1JsonObj.put("FFincDate",rs.getString(3));
							// 开票日期
							page1JsonObj.put("FDate",rs.getString(3));
							// 汇率类型
							JSONObject  FExchangeRateObj = new JSONObject();
							FExchangeRateObj.put("FNumber","01");
							FExchangeRateObj.put("FName","");
							page1JsonObj.put("FExchangeRateType",FExchangeRateObj);
							// 调整汇率
							page1JsonObj.put("FAdjustExchangeRate",rs.getDouble(4));
							// 币别  ************
							JSONObject  FCurrencyObj = new JSONObject();
							FCurrencyObj.put("FNumber",rs.getString(5));
							FCurrencyObj.put("FName","");
							page1JsonObj.put("FCurrencyID", FCurrencyObj);
							// 汇率 ************ rs.getDouble(4)
							page1JsonObj.put("FExchangeRate",rs.getDouble(4));
							// 年利率(％)
							page1JsonObj.put("FInterestRate",0.0);
							// 税务登记号
							page1JsonObj.put("FTaxNum","");
							// 销售方式
							JSONObject  FSaleStyleObj = new JSONObject();
							FSaleStyleObj.put("FNumber","FXF02");
							FSaleStyleObj.put("FName","");
							page1JsonObj.put("FSaleStyle", FSaleStyleObj);
							// 往来科目 ************ rs.getString(6)
							JSONObject FAcctObj = new JSONObject();
							FAcctObj.put("FNumber","1122.01.01");
							FAcctObj.put("FName","");
							page1JsonObj.put("FAcctID",FAcctObj);
							// 摘要
							page1JsonObj.put("FNote",rs.getString(7));
							// 结算方式代码
							page1JsonObj.put("FSettleNumber",0);
							// 结算方式
							JSONObject 	FSettleObj = new JSONObject();
							FSettleObj.put("FNumber","");
							FSettleObj.put("FName","");
							page1JsonObj.put("FSettleID",FSettleObj);
							// 备注（合同号）
							page1JsonObj.put("FCompactNo","");
							// 源单类型
							page1JsonObj.put("FSourceBillType","");
							// 源单编号
							page1JsonObj.put("FSourceBillNo1","");
							// 业务员  rs.getString(8)
							JSONObject FEmpObj = new JSONObject();
							FEmpObj.put("FNumber","");
							FEmpObj.put("FName","");
							page1JsonObj.put("FEmpID",FEmpObj);
							// 部门  根据合同号带出
							JSONObject 	FDeptObj = new JSONObject();
							FDeptObj.put("FNumber","");
							FDeptObj.put("FName","");
							page1JsonObj.put("FDeptID",FDeptObj);
							// 制单
							JSONObject 	FBillerObj = new JSONObject();
							FBillerObj.put("FNumber","Administrator");
							FBillerObj.put("FName","Administrator");
							page1JsonObj.put("FBillerID",FBillerObj);
							// 项目任务
							JSONObject 	FTaskObj = new JSONObject();
							FTaskObj.put("FNumber","");
							FTaskObj.put("FName","");
							page1JsonObj.put("FTaskID",FTaskObj);
							// 审核
							JSONObject FCheckerObj = new JSONObject();
							FCheckerObj.put("FNumber","李佳乐");
							FCheckerObj.put("FName","李佳乐");
							page1JsonObj.put("FCheckerID",FCheckerObj);
							// 项目资源
							JSONObject FResourceObj = new JSONObject();
							FResourceObj.put("FNumber","");
							FResourceObj.put("FName","");
							page1JsonObj.put("FResourceID",FResourceObj);
							// 记账
							page1JsonObj.put("FPosterID",0);
							// 概算金额
							page1JsonObj.put("FBudgetAmountFor", 0.0);
							// 项目订单
							JSONObject FOrderObj = new JSONObject();
							FOrderObj.put("FNumber","");
							FOrderObj.put("FName","");
							page1JsonObj.put("FOrderID",FOrderObj);
							// 项目订单金额
							page1JsonObj.put("FOrderAmountFor",0);
							// 地址
							page1JsonObj.put("FAddress","");
							// 凭证字号
							page1JsonObj.put("FVchInterID",0);
							// 核销状态
							page1JsonObj.put("FBillStatus",0);
							// 银行账号
							page1JsonObj.put("FAccount","");
							// 单据类型
							page1JsonObj.put("FTranType",602);
							// 开户银行
							page1JsonObj.put("FBank","");
							// 核销
							page1JsonObj.put("FArApStatus",1);
							// 累计调汇金额
							page1JsonObj.put("FAdjustAmount",0);
							// 年份
							page1JsonObj.put("FYear",Integer.parseInt(new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("yyyy-MM-dd").parse(rs.getString(3))).split("-")[0]));
							// 期间
							page1JsonObj.put("FPeriod",Integer.parseInt(new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("yyyy-MM-dd").parse(rs.getString(3))).split("-")[1]));
							// 子系统
							page1JsonObj.put("FSubSystemID",1);
							// 自由项1
							page1JsonObj.put("FFreeItem3","");
							// 红蓝字标志
							page1JsonObj.put("FROB",1);
							// 自由项2
							page1JsonObj.put("FFreeItem4","");
							// 地区
							page1JsonObj.put("FRegion",null);
							// 行业
							page1JsonObj.put("FTrade",null);
							// 单据累计核销金额
							page1JsonObj.put("FCheckAmountForBill",0);
							// 打印次数
							page1JsonObj.put("FPrintCount",0);
							// 单据本位币累计核销金额
							page1JsonObj.put("FCheckAmountBill",0);
							// 单据未核销金额
							page1JsonObj.put("FRemainAmountForBill",rs.getDouble(9));
							// 钩稽期间
							page1JsonObj.put("FYearPeriod",null);
							// 单据本位币未核销金额
							page1JsonObj.put("FRemainAmountBill", rs.getDouble(9)*rs.getDouble(4));
							// 钩稽人
							JSONObject FHookerObj = new JSONObject();
							FHookerObj .put("FNumber","");
							FHookerObj .put("FName","");
							page1JsonObj.put("FHookerID",FHookerObj );
							// 最后核销日期
							page1JsonObj.put("FCheckDate",rs.getString(3));
							// 状态
							page1JsonObj.put("FStatus",0);
							// 整单折扣参数
							page1JsonObj.put("FSysStatus",2);
							// 汇率获取方式
							page1JsonObj.put("FObtainRateWay",0);
							// 对账确认意见
							page1JsonObj.put("FConfirmAdvice","");
							// 对账确认人
							JSONObject FConfirmorObj = new JSONObject();
							FConfirmorObj  .put("FNumber","");
							FConfirmorObj .put("FName","");
							page1JsonObj.put("FConfirmor",FConfirmorObj );
							// 对账确认日期
							page1JsonObj.put("FConfirmDate",null);
							// 开票状态
							page1JsonObj.put("FInvoiceStatus","未开票");
							// 打印状态
							page1JsonObj.put("FPrintStatus","未打印");
							// 对账确认标志
							page1JsonObj.put("FConfirmFlag",0);
							// 稅控日期
							page1JsonObj.put("FJSDate",null);
							// 收款条件
							JSONObject FPayConditionObj = new JSONObject();
							FPayConditionObj.put("FNumber","");
							FPayConditionObj.put("FName","");
							page1JsonObj.put("FPayCondition", FPayConditionObj);
							page1Array.add(page1JsonObj);
							
							page2JsonObj = new JSONObject();
							// 行号
							page2JsonObj.put("FEntryID2",1);
							// 应收日期
							page2JsonObj.put("fdate_2",rs.getString(3));
							// 收款金额
							page2JsonObj.put("FAmountFor",rs.getDouble(9));
							// 原始单据内码
							page2JsonObj.put("FOrgID",0);
							// 收款金额(本位币)
							page2JsonObj.put("FAmount2",rs.getDouble(9)*rs.getDouble(4));
							// 剩余金额(本位币)
							page2JsonObj.put("FRemainAmount",0.0);
							// 剩余金额（原币）
							page2JsonObj.put("FRemainAmountFor",0.0);
							// 系统类型
							page2JsonObj.put("FRP",1);
							page2Array.add(page2JsonObj);
							
							pst2 = conn.prepareStatement("select JSMX_WLBH,JSMX_SL,JSMX_SLDW,JSMX_JSHJ,JSMX_DJ,JSMX_SLV,JSMX_ZKJC,JSMX_HH,JSMX_SE,JSMX_GNM7,JSMX_GNM2,JSMX_SFID,JSMX_GBZ3,JSMX_GXM9 from t_ESB_SaleInvoice where RKJS_DJBH=?");
							pst2.setString(1, billNo);
							rs2 = pst2.executeQuery();
							while (rs2.next()) {
								page3JsonObj = new JSONObject();
								// 车辆配置
								page3JsonObj.put("FEntrySelfI0470","");
								// 产品代码
								JSONObject  FItemObj = new JSONObject();
								FItemObj.put("FNumber",rs2.getString(1));
								FItemObj.put("FName","");
								page3JsonObj.put("FItemID",FItemObj );
								// 产品名称
								page3JsonObj.put("FItemID36422","");
								// 规格型号
								page3JsonObj.put("FItemID36436","");
								// 行号
								page3JsonObj.put("FEntryID3",rs2.getRow());
								// 辅助属性
								JSONObject  FAuxPropObj = new JSONObject();
								FAuxPropObj.put("FNumber","");
								FAuxPropObj.put("FName","");
								page3JsonObj.put("FAuxPropID",FAuxPropObj);
								// 已开发票数量
								page3JsonObj.put("FHasInvoiceQty", rs2.getDouble(2));
								// 单位 ************
								JSONObject  FUnitObj = new JSONObject();
								FUnitObj.put("FNumber","");
								FUnitObj.put("FName",rs2.getString(3));
								page3JsonObj.put("FUnitID",FUnitObj);
								// 数量
								page3JsonObj.put("FAuxQty",rs2.getDouble(2));
								// 已开发票金额
								page3JsonObj.put("FHasInvoiceAmount",0);
								// 付款申请关联数量
								page3JsonObj.put("FQuantityPayApply_Commit",0.0);
								// 收付款关联数量
								page3JsonObj.put("FQuantityReceive_Commit",0);
								// 辅助单位
								JSONObject  FSecUnitObj = new JSONObject();
								FSecUnitObj.put("FNumber","");
								FSecUnitObj.put("FName","");
								page3JsonObj.put("FSecUnitID",FSecUnitObj);
								// 换算率
								page3JsonObj.put("FsecCoefficient",0.0);
								// 辅助单位数量
								page3JsonObj.put("FSecQty",0.0);
								// 单价
								page3JsonObj.put("FAuxPrice",rs2.getDouble(5)/(1+rs2.getDouble(6)));
								// 含税单价
								page3JsonObj.put("FAuxTaxPrice",rs2.getDouble(5));
								// 折扣率(％)
								page3JsonObj.put("FDiscountRate",rs2.getDouble(7)/rs2.getDouble(4)*100);
								// 源单内码
								page3JsonObj.put("FSourceInterId",0);
								// 实际含税单价
								if(rs2.getDouble(7)==0.0) {
									page3JsonObj.put("FPriceDiscount",rs2.getDouble(5));
								}else {
									page3JsonObj.put("FPriceDiscount",rs2.getDouble(5)*(rs2.getDouble(7)/rs2.getDouble(4)));
								}
								// 折扣额
							    page3JsonObj.put("FAmtDiscount",rs2.getDouble(7));
								// 折扣额（本位币）
							    page3JsonObj.put("FStdAmtDiscount",rs2.getDouble(7)*rs.getDouble(4));
								// 金额
							    page3JsonObj.put("FAmount3",rs2.getDouble(4)-rs2.getDouble(7));
								// 基本单价
							    page3JsonObj.put("FPrice",rs2.getDouble(5)/(1+rs2.getDouble(6)));
								// 含税价
							    page3JsonObj.put("FTaxPrice",rs2.getDouble(5));
								// 金额（本位币）
							    page3JsonObj.put("FStdAmount",(rs2.getDouble(4)-rs2.getDouble(7))*rs.getDouble(4));
								// 合同行号
							    page3JsonObj.put("FContractEntryID",0);
								// 源分录ID
							    page3JsonObj.put("FEntryID_SRC",rs2.getInt(8));
								// 税率(％)
							    page3JsonObj.put("FTaxRate",rs2.getDouble(6)*100);
								// 订单行号
							    page3JsonObj.put("FOrderEntryID",0);
								// 订单内码
							    page3JsonObj.put("FOrderInterID",0);
								// 税额
							    page3JsonObj.put("FTaxAmount",rs2.getDouble(9));
								// 自由项1
							    page3JsonObj.put("FFreeItem3_3","");
								// 自由项2
							    page3JsonObj.put("FFreeItem4_3","");
								// 税额（本位币）
							    page3JsonObj.put("FStdTaxAmount",rs2.getDouble(9)*rs.getDouble(4));
								// 价税合计
							    page3JsonObj.put("FAmountIncludeTax", rs2.getDouble(4));
								// 价税合计（本位币）
							    page3JsonObj.put("FStdAmountIncludeTax",rs2.getDouble(4)*rs.getDouble(4));
								// 备注
							    page3JsonObj.put("FNote_3","");
								// 批号
							    page3JsonObj.put("FBatchNo",rs2.getString(10));
								// 基本单位
							    page3JsonObj.put("FBaseUnit","");
								// 基本单位数量
							    page3JsonObj.put("FQty", rs2.getDouble(2));
								// 订单单价
							    page3JsonObj.put("FAuxOrderPrice", 0.00);
								// 基本计量单位订单单价
							    page3JsonObj.put("FOrderPrice", 0.00);
								// 源单类型
								JSONObject  FClass_SRCObj = new JSONObject();
								FClass_SRCObj.put("FNumber","");
								FClass_SRCObj.put("FName","");
								page3JsonObj.put("FClassID_SRC", FClass_SRCObj);
								// 源单单号
								page3JsonObj.put("FSourceBillNo",rs2.getString(11));
								// 合同号
								page3JsonObj.put("FContractBillNo","");
								// 销售订单号
								page3JsonObj.put("FOrderBillNo","");
								// 累计已核销已关联金额
								page3JsonObj.put("FLinkCheckAmountFor",0.00);
								// 累计已核销已关联金额(本位币)
								page3JsonObj.put("FLinkCheckAmount",0.00);
								// 累计核销金额
								page3JsonObj.put("FCheckAmountFor",0.00);
								// 累计核销金额(本位币)
								page3JsonObj.put("FCheckAmount",0.00);
								// 未核销金额
								page3JsonObj.put("FRemainAmountForEntry",rs2.getDouble(4));
								// 累计已核销已关联数量
								page3JsonObj.put("FLinkCheckQty", 0.00);
								// 未核销金额(本位币)
								page3JsonObj.put("FRemainAmountEntry",rs2.getDouble(4)*rs.getDouble(4));
								// 累计核销数量
								page3JsonObj.put("FCheckQty", 0.00);
								// 未核销数量
								page3JsonObj.put("FRemainQty", rs2.getDouble(2));
								// 收款关联金额
								page3JsonObj.put("FAmountFor_Commit",0.00);
								// 收款关联金额(本位币)
								page3JsonObj.put("FAmount_Commit",0.00);
								// 红字发票已关联已核销金额(本位币)
								page3JsonObj.put("FInvLinkCheckAmount",0.00);
								// 红字发票关联金额(本位币)
								page3JsonObj.put("FInvoiceAmount",0.00);
								// 红字发票关联金额(原币)
								page3JsonObj.put("FInvoiceAmountFor",0.00);
								// 红字发票已关联已核销金额(原币)
								page3JsonObj.put("FInvLinkCheckAmountFor",0.00);
								// 红字发票已关联已核销数量
								page3JsonObj.put("FInvLinkCheckQty",0.00);
								// 红字发票关联数量
								page3JsonObj.put("FInvoiceQty",0.00);
								// 源单类型
								page3JsonObj.put("FSourceTranType",0);
								// 对账确认意见(表体)
								page3JsonObj.put("FConfirmAdviceEntry","");
								// 源单分录
								page3JsonObj.put("FSourceEntryID",rs2.getInt(8));
								// 网店订单号
								page3JsonObj.put("FOnLineOrderNo","");
								// 合同小号/合同号
								page3JsonObj.put("FEntrySelfI0473",rs2.getString(12));
								// 任务号
								page3JsonObj.put("FEntrySelfI0474",rs2.getString(13));
								// 计生号JSH
								page3JsonObj.put("FEntrySelfI0475",rs2.getString(14));
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
				String authorityCode = "71dcfd6f3cd7e764f5b26917d8566bb0abf051c14717c7ea";
				String token = HttpUtil.sendGet("http://172.16.7.191/K3API/Token/Create", "authorityCode=" + authorityCode);
				
				// 发送POST请求
				String response = HttpUtil.sendPost("http://172.16.7.191/K3API/Bill1000002/Save?token="
								+ JSON.parseObject(JSON.parseObject(token).get("Data").toString()).get("Token"), param);
				result = JSONObject.parseObject(response);
				if(result.getIntValue("StatusCode") == 200) {
					try {
						conn = DBUtils.getConnection();
						if (conn!=null) {
							pst = conn.prepareStatement("update t_ESB_SaleInvoice set IsRead=1 where RKJS_DJBH=?");
							pst.setString(1, billNo);
							pst.execute();
							pst2 = conn.prepareStatement("update ICSale set FCheckerID=16394 where FBillNo=?");
							pst2.setString(1, billNo);
							pst2.execute();
						}
					} catch (Exception e) {
						Logger.info(e.getMessage());
					}finally {
						DBUtils.closeConnection(conn, pst, null);
						DBUtils.closeConnection(conn, pst2, null);
					}
				}
				Logger.info("返回报文为："+result.toJSONString());
			}
		}
		return result.toJSONString();
	}
}
