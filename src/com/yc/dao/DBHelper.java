package com.yc.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBHelper {

	/*
	 * static { try { System.out.println("加载驱动");
	 * Class.forName(DbProperties.getInstance().getProperty("driverClassName"));
	 * 
	 * } catch (ClassNotFoundException e) { // TODO: handle exception
	 * e.printStackTrace(); } }
	 */

	public Connection getConnection() {
		DbProperties p = DbProperties.getInstance();
		Connection con = null;
		try {
			con = DriverManager.getConnection(p.getProperty("url"), p.getProperty("username"),
					p.getProperty("password"));
			// 另一个重载方法，参数不同．如用下面的方法的话，则db.properties中要修改u.s.enname ->userl l
			// con=DriverManager.getConnection( p.getProperty( "url")，p );
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;

	}

	public int doUpdete(String sql, Object... values) {
		int result = 0;
		try (Connection con = getConnection(); 
			PreparedStatement pstm = con.prepareStatement(sql);
				) {
			setParams(pstm, values);
			result = pstm.executeUpdate();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return result;
	}

	private void setParams(PreparedStatement pstmt, Object... values) throws SQLException {
		// 循环参数列表values，给pstm太中的sgl中的―?设置参数值
		if (values != null && values.length > 0) {
			for (int i = 0; i < values.length; i++) {
				// 所有的参数都当成object处理．
				pstmt.setObject(i + 1, values[i]);
			}
		}
	}

	public double selectAggreation(String sql, Object... values) {       //聚合查询 查个数
		double result = 0;
		try (Connection con = getConnection(); PreparedStatement pstmt = con.prepareStatement(sql); // 预预编译
		) {
			// 设置参数
			setParams(pstmt, values);// 查询
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				result = rs.getDouble(1);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	public List<Map<String, Object>> select(String sql, Object... values) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try (Connection con = getConnection(); PreparedStatement pstmt = con.prepareStatement(sql); // 预预编译
		) {
			// 设置参数
			setParams(pstmt, values);
			// 查询
			ResultSet rs = pstmt.executeQuery();
			// 根据太s获取结果集的元数据，取所有的字段名,用于Map的键
			ResultSetMetaData rsmd = rs.getMetaData();
			int cc = rsmd.getColumnCount(); // 结果集中总的列数//存好列名
			List<String> columnName = new ArrayList<String>();
			for (int i = 0; i < cc; i++) {
				columnName.add(rsmd.getColumnName(i + 1));
			}
			// 循环结果集,取出每一行while (rs.next()) i
			// 循环所有的列，一个列一个列的取值，存到Map中
			while (rs.next()) {
			Map<String, Object> map = new HashMap<String, Object>();// 循环所有的列.
			for (int i = 0; i < columnName.size(); i++) {
				String cn = columnName.get(i); // 取列名
				Object value = rs.getObject(cn); // 根据列名取值//存到map中
				map.put(cn, value);
			}
			list.add(map);
		  }
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return list;
	}

	public Map<String, String> batchInsert(String path) {
		Map<String, String> map = new HashMap<String, String>();


		List<String> list = new ArrayList<String>();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path))))) {
			String s;
			while ((s = reader.readLine()) != null) {
				list.add(s);
			}
		} catch (Exception ex) {
			// TODO: handle exceptionprivate
			ex.printStackTrace();
		}

		map.put("待导入的数量", list.size() + "");

		try {
			Class.forName("oracle.jdbc.OracleDriver");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


		String sql = "insert into students values(?,?,?)";// seq_students.nextval
		Connection con = null;
		PreparedStatement pstmt = null;
		String s = null;
		String[] ss = null;
		int total = 0;
		try {// 资源自动关闭
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "scott", "a");
			con.setAutoCommit(false);
			pstmt = con.prepareStatement(sql);

			for (int i = 0; i < list.size(); i++) {
				s = list.get(i);
				ss = s.split("\t");

				pstmt.setString(1, ss[0]);
				pstmt.setString(2, ss[1]);
				pstmt.setString(3, ss[2]);
				// 批处理
				pstmt.addBatch();
				if ((i + 1) % 1000 == 0) {
					int[] result = pstmt.executeBatch();
					total += sum(result);
					con.commit();
					pstmt.clearBatch();
				}
			}
			int[] result = pstmt.executeBatch();
			total += sum(result);
			con.commit();
			pstmt.clearBatch();

			map.put("成功插入条数，", total + "");
		} catch (Exception ex) {
			// TODO: handle exception
			ex.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} finally {
			if (con != null) {
				try {
					con.setAutoCommit(true);
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return map;
	}

	private int sum(int[] result) {
		int total = 0;
		if (result == null || result.length <= 0) {
			return 0;
		}
		for (int i = 0; i < result.length; i++) {
			total += 1;
		}
		return total;
	}

}
