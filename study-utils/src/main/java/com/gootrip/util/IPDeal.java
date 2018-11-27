/**
 * IP������
 */
package com.gootrip.util;

/**
 * @author advance
 *
 */
import com.gootrip.database.*;
import java.sql.*;

public class IPDeal {
	/**
	 * ͨ��ip��ַ��ѯ��������
	 * @param strip
	 * @return ip���ڵ�������
	 */
	public static String getArea(String strip){
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql;
		String strRtn = null;
		try{
			MyJdbc myjdbc = new MyJdbc();
			conn = myjdbc.getConn();
			sql = "select * from fullip where startip<='" + strip + "' and endip>='" + strip + "'";
			pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()){
				strRtn = rs.getString("country");
			}else{
				strRtn = "δȷ��";
			}
			rs.close();
			rs = null;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if (pstmt != null)
				try{
					pstmt.close();
					pstmt = null;
				}catch(Exception e){}
			if (conn != null)
				try{
					conn.close();
					conn = null;
				}catch(Exception e){}
		}
		return strRtn;
	}
	/**
	 * ��ip��ַ��ʽ��Ϊ��000.000.000.000
	 * @param ip
	 * @return ���ع��ip
	 */
	public static String strfullip(String ip){
		StringBuffer buff = new StringBuffer();
		buff.append("");
		String strzero = "000";
		int ilen = 0;
		if(ip != null){
			String[] arrip = ip.split("\\.");
			if(arrip.length == 4){
				for(int i = 0; i < 4; i++){
					if (i==0){
						ilen = arrip[i].length();
						if(ilen < 3){
							buff.append(strzero.substring(0,3-ilen)).append(arrip[i]);
						}else{
							buff.append(arrip[i]);
						}
					}else{
						ilen = arrip[i].length();
						if(ilen < 3){
							buff.append(".").append(strzero.substring(0,3-ilen)).append(arrip[i]);
						}else{
							buff.append(".").append(arrip[i]);
						}
					}
				}
			}
		}
		return buff.toString();
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String strip = "202.108.33.32";
		System.out.println(IPDeal.strfullip(strip));
		System.out.println(System.currentTimeMillis());
		System.out.println("ip" + strip + "���ڵ�����" + IPDeal.getArea(IPDeal.strfullip(strip)));
		System.out.println(System.currentTimeMillis());
	}

}
