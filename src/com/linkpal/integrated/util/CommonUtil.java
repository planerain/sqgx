package com.linkpal.integrated.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description:
 *
 * @author lichao
 * @date 2019年11月29日 下午1:44:54
 *
 */
public class CommonUtil {
	private static Connection conn = null;
	private static PreparedStatement pst = null;
	private static PreparedStatement pst2 = null;
	private static PreparedStatement pst3 = null;
	private static ResultSet rs = null;
	private final static Logger LOGGER = LoggerFactory.getLogger(CommonUtil.class);

	public static int getNo(String orgNumber, String voucherType) {
		int num = 0;
		int nowYear = 0;
		int nowMonth = 0;
		String voucherNo = "";
		try {
			conn = DBUtils.getConnection();
			if (conn != null) {
				pst = conn.prepareStatement("select Year,Month,SerialNum from t_VoucherNo where OrgNo=? and Type=?");
				pst.setString(1, orgNumber);
				pst.setString(2, voucherType);
				rs = pst.executeQuery();
				nowYear = Integer
						.parseInt(new SimpleDateFormat("yy", Locale.CHINESE).format(Calendar.getInstance().getTime()));
				nowMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
				while (rs.next()) {
					if (rs.getInt(1) == nowYear && rs.getInt(2) == nowMonth) {
						num = rs.getInt(3);
						pst2 = conn.prepareStatement(
								"update t_VoucherNo set SerialNum=SerialNum+1 where OrgNo=? and Type=?");
						pst2.setString(1, orgNumber);
						pst2.setString(2, voucherType);
						pst2.execute();
					} else {
						num = 1;
						pst3 = conn.prepareStatement(
								"update t_VoucherNo set SerialNum=2,Year=?,Month=? where OrgNo=? and Type=?");
						pst3.setInt(1, nowYear);
						pst3.setInt(2, nowMonth);
						pst3.setString(3, orgNumber);
						pst3.setString(4, voucherType);
						pst3.execute();
					}
				}
			}
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
		} finally {
			DBUtils.closeConnection(conn, pst, rs);
			DBUtils.closeConnection(conn, pst2, null);
			DBUtils.closeConnection(conn, pst3, null);
		}
		voucherNo = nowYear + new DecimalFormat("00").format(nowMonth)
				+ new DecimalFormat("00000").format(num);
		return Integer.parseInt(voucherNo);
	}
}
