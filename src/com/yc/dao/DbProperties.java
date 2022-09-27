package com.yc.dao;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class  DbProperties extends Properties {
	private static DbProperties dbProperties = new DbProperties(); //¶öººµ¥Àý

	private DbProperties() {
		try (InputStream fis = DbProperties.class.getClassLoader().getResourceAsStream("db.properties");
				) {
			super.load(fis);
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}	
	}
	
	
	
	public static DbProperties getInstance() {
		// TODO Auto-generated method stub
		if(dbProperties==null) {
			dbProperties=new DbProperties();
		}
	return dbProperties;
	}
	
}