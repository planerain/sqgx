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
	
	private static final Logger Logger = LoggerFactory.getLogger(PurchaseInvoiceServiceImpl.class);

	@Override
	public String getSaleInvoiceData(String saleInvoiceData) {
		Logger.info("接收到的应收单数据为:"+saleInvoiceData);
		JSONObject saleInvoiceDataDataObj = JSONObject.parseObject(saleInvoiceData);
		try {
			conn = DBUtils.getConnection();
			if(conn!=null) {
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
				pst.setFloat(6, saleInvoiceDataDataObj.getFloatValue("RKJS_JSHJ"));
				pst.setString(7, saleInvoiceDataDataObj.getString("RKJS_ZY"));
				JSONArray entries = saleInvoiceDataDataObj.getJSONArray("JSMX");
				for (int i = 0; i < entries.size(); i++) {
					JSONObject entryData = entries.getJSONObject(i);
					pst.setString(8, entryData.getString("JSMX_NM"));
					pst.setString(9, entryData.getString("JSMX_GYSBH"));
					pst.setString(10, entryData.getString("JSMX_FPHM"));
					pst.setString(11, entryData.getString("JSMX_SFID"));
					pst.setString(12, entryData.getString("JSMX_WLBH"));
					pst.setInt(13, entryData.getIntValue("JSMX_SL"));
					pst.setString(14, entryData.getString("JSMX_SLDW"));
					pst.setFloat(15, entryData.getFloatValue("JSMX_DJ"));
					pst.setString(16, entryData.getString("JSMX_SLV"));
					pst.setFloat(17, entryData.getFloatValue("JSMX_SE"));
					pst.setFloat(18, entryData.getFloatValue("JSMX_JSHJ"));
					pst.setString(19, entryData.getString("JSMX_BZ"));
					pst.setFloat(20, entryData.getFloatValue("JSMX_HLV"));
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
					pst.setFloat(34, entryData.getFloatValue("JSMX_SZ2"));
					pst.setString(35, entryData.getString("JSMX_GBZ3"));
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
				pst = conn.prepareStatement("select distinct top 1 JSMX_MIXNUMBER,JSMX_GYSBH,JSMX_GLRQ,JSMX_HLV,JSMX_BZ,JSMX_ZHKM,RKJS_ZY,RKJS_ZDRYGBH,RKJS_JSHJ from t_ESB_SaleInvoice where IsRead=0");
				rs = pst.executeQuery();
				while (rs.next()) {
					billNo = rs.getString(1);
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
					page1JsonObj.put("FAdjustExchangeRate",rs.getFloat(4));
					// 币别  ************
					JSONObject  FCurrencyObj = new JSONObject();
					FCurrencyObj.put("FNumber","");
					FCurrencyObj.put("FName",rs.getString(5));
					page1JsonObj.put("FCurrencyID", FCurrencyObj);
					// 汇率
					page1JsonObj.put("FExchangeRate",rs.getFloat(4));
					// 年利率(％)
					page1JsonObj.put("FInterestRate",0.0);
					// 税务登记号
					page1JsonObj.put("FTaxNum","");
					// 销售方式
					JSONObject  FSaleStyleObj = new JSONObject();
					FSaleStyleObj.put("FNumber","FXF02");
					FSaleStyleObj.put("FName","");
					page1JsonObj.put("FSaleStyle", FSaleStyleObj);
					// 往来科目  rs.getString(6)
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
					FCheckerObj.put("FNumber","Administrator");
					FCheckerObj.put("FName","Administrator");
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
					page1JsonObj.put("FArApStatus",0);
					// 累计调汇金额
					page1JsonObj.put("FAdjustAmount",0);
					// 年份
					page1JsonObj.put("FYear",Integer.parseInt(new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("yyyyMMdd").parse(rs.getString(3))).split("-")[0]));
					// 期间
					page1JsonObj.put("FPeriod",Integer.parseInt(new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("yyyyMMdd").parse(rs.getString(3))).split("-")[1]));
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
					page1JsonObj.put("FRemainAmountForBill",rs.getFloat(9));
					// 钩稽期间
					page1JsonObj.put("FYearPeriod",null);
					// 单据本位币未核销金额
					page1JsonObj.put("FRemainAmountBill", rs.getFloat(9)*rs.getFloat(4));
					// 钩稽人
					JSONObject FHookerObj = new JSONObject();
					FHookerObj .put("FNumber","");
					FHookerObj .put("FName","");
					page1JsonObj.put("FHookerID",FHookerObj );
					// 最后核销日期
					page1JsonObj.put("FCheckDate","");
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
					page2JsonObj.put("FAmountFor",rs.getFloat(9));
					// 原始单据内码
					page2JsonObj.put("FOrgID",0);
					// 收款金额(本位币)
					page2JsonObj.put("FAmount2",rs.getFloat(9)*rs.getFloat(4));
					// 剩余金额(本位币)
					page2JsonObj.put("FRemainAmount",0.0);
					// 剩余金额（原币）
					page2JsonObj.put("FRemainAmountFor",0.0);
					// 系统类型
					page2JsonObj.put("FRP",1);
					page2Array.add(page2JsonObj);
					
					pst2 = conn.prepareStatement("select JSMX_WLBH,JSMX_SL,JSMX_SLDW,JSMX_JSHJ,JSMX_DJ,JSMX_SLV,JSMX_ZKJC,JSMX_HH,JSMX_SE,JSMX_GNM7,JSMX_GNM2,JSMX_SFID,JSMX_GBZ3,JSMX_GXM9 from t_ESB_SaleInvoice where JSMX_MIXNUMBER=?");
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
						page3JsonObj.put("FHasInvoiceQty", rs2.getFloat(2));
						// 单位 ************
						JSONObject  FUnitObj = new JSONObject();
						FUnitObj.put("FNumber","");
						FUnitObj.put("FName",rs2.getString(3));
						page3JsonObj.put("FUnitID",FUnitObj);
						// 数量
						page3JsonObj.put("FAuxQty",rs2.getFloat(2));
						// 已开发票金额
						page3JsonObj.put("FHasInvoiceAmount",rs2.getFloat(4));
						// 付款申请关联数量
						page3JsonObj.put("FQuantityPayApply_Commit",0.0);
						// 收付款关联数量
						page3JsonObj.put("FQuantityReceive_Commit",rs2.getFloat(2));
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
						page3JsonObj.put("FAuxPrice",rs2.getFloat(5));
						// 含税单价
						page3JsonObj.put("FAuxTaxPrice",rs2.getFloat(5)*(1+rs2.getFloat(6)));
						// 折扣率(％)
						page3JsonObj.put("FDiscountRate",rs2.getFloat(7)/rs2.getFloat(4)*100);
						// 源单内码
						page3JsonObj.put("FSourceInterId",0);
						// 实际含税单价
						if(rs2.getFloat(7)==0.0) {
							page3JsonObj.put("FPriceDiscount",rs2.getFloat(5)*(1+rs2.getFloat(6)));
						}else {
							page3JsonObj.put("FPriceDiscount",rs2.getFloat(5)*(1+rs2.getFloat(6))*(rs2.getFloat(7)/rs2.getFloat(4)));
						}
						// 折扣额
					    page3JsonObj.put("FAmtDiscount",rs2.getFloat(7));
						// 折扣额（本位币）
					    page3JsonObj.put("FStdAmtDiscount",rs2.getFloat(7)*rs.getFloat(4));
						// 金额
					    page3JsonObj.put("FAmount3",rs2.getFloat(4)-rs2.getFloat(7));
						// 基本单价
					    page3JsonObj.put("FPrice",rs2.getFloat(5));
						// 含税价
					    page3JsonObj.put("FTaxPrice",rs2.getFloat(5)*(1+rs2.getFloat(6)));
						// 金额（本位币）
					    page3JsonObj.put("FStdAmount",(rs2.getFloat(4)-rs2.getFloat(7))*rs.getFloat(4));
						// 合同行号
					    page3JsonObj.put("FContractEntryID",0);
						// 源分录ID
					    page3JsonObj.put("FEntryID_SRC",rs2.getInt(8));
						// 税率(％)
					    page3JsonObj.put("FTaxRate",rs2.getFloat(6)*100);
						// 订单行号
					    page3JsonObj.put("FOrderEntryID",0);
						// 订单内码
					    page3JsonObj.put("FOrderInterID",0);
						// 税额
					    page3JsonObj.put("FTaxAmount",rs2.getFloat(9));
						// 自由项1
					    page3JsonObj.put("FFreeItem3_3","");
						// 自由项2
					    page3JsonObj.put("FFreeItem4_3","");
						// 税额（本位币）
					    page3JsonObj.put("FStdTaxAmount",rs2.getFloat(9)*rs.getFloat(4));
						// 价税合计
					    page3JsonObj.put("FAmountIncludeTax", rs2.getFloat(4));
						// 价税合计（本位币）
					    page3JsonObj.put("FStdAmountIncludeTax",rs2.getFloat(4)*rs.getFloat(4));
						// 备注
					    page3JsonObj.put("FNote_3","");
						// 批号
					    page3JsonObj.put("FBatchNo",rs2.getString(10));
						// 基本单位
					    page3JsonObj.put("FBaseUnit","");
						// 基本单位数量
					    page3JsonObj.put("FQty", rs2.getFloat(2));
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
						page3JsonObj.put("FRemainAmountForEntry",rs2.getFloat(4));
						// 累计已核销已关联数量
						page3JsonObj.put("FLinkCheckQty", 0.00);
						// 未核销金额(本位币)
						page3JsonObj.put("FRemainAmountEntry",rs2.getFloat(4)*rs.getFloat(4));
						// 累计核销数量
						page3JsonObj.put("FCheckQty", 0.00);
						// 未核销数量
						page3JsonObj.put("FRemainQty", rs2.getFloat(2));
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
		String authorityCode = "cc7c2a733c0a29ece004ca4aaa31fa27a349134496f8c1d0";
		String token = HttpUtil.sendGet("http://172.16.7.191/K3API/Token/Create", "authorityCode=" + authorityCode);
		
		// 发送POST请求
		String response = HttpUtil.sendPost("http://172.16.7.191/K3API/Bill1000002/Save?token="
						+ JSON.parseObject(JSON.parseObject(token).get("Data").toString()).get("Token"), param);
		JSONObject resultObj = JSONObject.parseObject(response);
		if(resultObj.getIntValue("StatusCode") == 200) {
			try {
				conn = DBUtils.getConnection();
				if (conn!=null) {
					pst = conn.prepareStatement("update t_ESB_SaleInvoice set IsRead=1 where JSMX_MIXNUMBER=?");
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
