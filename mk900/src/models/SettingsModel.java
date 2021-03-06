package models;

import java.util.*;

import entities.*;
import mk900.*;

import java.lang.reflect.Field;
import java.sql.*;

public class SettingsModel {
	
	public List<Setting> findAll(){
		
		try { 
			
			List<Setting> settings_list = new ArrayList<>();	
			PreparedStatement ps = Main.c.prepareStatement("select * from settings where exp_id = 0 or exp_id is NULL order by sort_ind");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {				
				Setting setting_var = new Setting(rs.getString("name"), rs.getString("value"), rs.getString("desc"));
				settings_list.add(setting_var);
			}
			
			return settings_list;
		
			
		} catch (Exception e) {
			System.out.println( e.getClass().getName() + ": " + e.getMessage() );
			return null;
		}
		
	}
	
public List<Setting> findAll(int exp_id){
		
		try { 
			
			List<Setting> settings_list = new ArrayList<>();	
			PreparedStatement ps = Main.c.prepareStatement("select * from settings where exp_id=?");
			ps.setInt(1, exp_id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {				
				Setting setting_var = new Setting(rs.getString("name"), rs.getString("value"), rs.getString("desc"));
				settings_list.add(setting_var);
			}
			
			return settings_list;
		
			
		} catch (Exception e) {
			System.out.println( e.getClass().getName() + ": " + e.getMessage() );
			return null;
		}
		
	}
	
public SettingsModel() {
		super();
		// TODO Auto-generated constructor stub
	}

public Setting find(String name_par){
		
		try { 
			
			Setting setting_var = null;		
			PreparedStatement ps = Main.c.prepareStatement("select * from settings where name=?");
			ps.setString(1, name_par);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				
				setting_var = new Setting();
				setting_var.setName(rs.getString("name"));
				setting_var.setValue(rs.getString("value"));
				setting_var.setDesc(rs.getString("desc")); 
				setting_var.setExp_id(rs.getInt("exp_id"));
				
			}
			
			return setting_var;
		
			
		} catch (Exception e) {
			return null;
		}
		
	}

public Setting find(String name_par, int exp_id){
	
	try { 
		
		Setting setting_var = null;		
		PreparedStatement ps = Main.c.prepareStatement("select * from settings where name=? and exp_id=?");
		ps.setString(1, name_par);
		ps.setInt(2, exp_id);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			
			setting_var = new Setting();
			setting_var.setName(rs.getString("name"));
			setting_var.setValue(rs.getString("value"));
			setting_var.setDesc(rs.getString("desc")); 
			setting_var.setExp_id(rs.getInt("exp_id"));
			setting_var.setSort_ind(rs.getInt("sort_ind"));
			
		}
		
		return setting_var;
	
		
	} catch (Exception e) {
		return null;
	}
	
}

public boolean create(Setting setting_par){
	
	try {
		PreparedStatement ps = Main.c.prepareStatement("insert into settings(name, value, desc, exp_id) values (?,?,?,?)");
		ps.setString(1, setting_par.getName());
		ps.setString(2, setting_par.getValue());
		ps.setString(3, setting_par.getDesc());
		ps.setInt(4, setting_par.getExp_id());
		
		return ps.executeUpdate() > 0;
		
	} catch (Exception e) {
		return false;
	}
	
}

public boolean edit(Setting setting_par){
	
	try {
		PreparedStatement ps = Main.c.prepareStatement("update settings set value=?, desc=? where name=? and exp_id=?");
		ps.setString(1, setting_par.getValue());
		ps.setString(2, setting_par.getDesc());
		ps.setString(3, setting_par.getName());
		ps.setInt(4, setting_par.getExp_id());
		
		return ps.executeUpdate() > 0;
		
	} catch (Exception e) {
		return false;
	}
	
}

public boolean editColumn(Setting setting_par, String col_name){
	
	try {
		PreparedStatement ps = Main.c.prepareStatement("update settings set "+col_name+"=? where name=?");
		Field field = setting_par.getClass().getDeclaredField(col_name);

		  field.setAccessible(true);

		  Object value = field.get(setting_par);
		ps.setString(1, value.toString());
		ps.setString(2, setting_par.getName());
		
		return ps.executeUpdate() > 0;
		
	} catch (Exception e) {
		return false;
	}
	
}

public boolean delete(Setting setting_par){
	
	try {
		PreparedStatement ps = Main.c.prepareStatement("delete from settings where name=?");
		ps.setString(1, setting_par.getName());
		
		return ps.executeUpdate() > 0;
		
	} catch (Exception e) {
		return false;
	}
	
}

}
