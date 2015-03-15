package models;

import java.util.*;

import entities.*;
import mk900.*;


import java.sql.*;

public class ExperimentModel {
	
	public ExperimentModel() {
		super();
		// TODO Auto-generated constructor stub
	}
	
public List<Experiment> findAll(){
		
		try { 
			
			List<Experiment> ent_list = new ArrayList<>();	
			PreparedStatement ps = Main.c.prepareStatement("select * from experiments");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {				
				Experiment ent_var = new Experiment(rs.getInt("id"), rs.getLong("date"));
				ent_list.add(ent_var);
			}
			
			return ent_list;
		
			
		} catch (Exception e) {
			System.out.println( e.getClass().getName() + ": " + e.getMessage() );
			return null;
		}
		
}

public Experiment find(int id_par){
	
	try { 
		
		Experiment ent_var = null;		
		PreparedStatement ps = Main.c.prepareStatement("select * from experiments where id=?");
		ps.setInt(1, id_par);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			
			ent_var = new Experiment();
			ent_var.setId(rs.getInt("id"));
			ent_var.setDate(rs.getLong("date"));
				
		}
		
		return ent_var;
	
		
	} catch (Exception e) {
		return null;
	}
	
}

public Experiment findByDate(long date_par){
	
	try { 
		
		Experiment ent_var = null;		
		PreparedStatement ps = Main.c.prepareStatement("select * from experiments where date=?");
		ps.setLong(1, date_par);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			
			ent_var = new Experiment();
			ent_var.setId(rs.getInt("id"));
			ent_var.setDate(rs.getLong("date"));
				
		}
		
		return ent_var;
	
		
	} catch (Exception e) {
		return null;
	}
	
}

public boolean create(Experiment exp_par){

try {
	PreparedStatement ps = Main.c.prepareStatement("insert into experiments(date) values (?)");
	ps.setLong(1, exp_par.getDate());
	
	return ps.executeUpdate() > 0;
	
} catch (Exception e) {
	return false;
}

}

public boolean edit(Experiment exp_par){

try {
	PreparedStatement ps = Main.c.prepareStatement("update experiments set date=? where id=?");
	ps.setLong(1, exp_par.getDate());
	ps.setInt(2, exp_par.getId());

	
	return ps.executeUpdate() > 0;
	
} catch (Exception e) {
	return false;
}

}



public boolean delete(Experiment expr_par){

try {
	PreparedStatement ps = Main.c.prepareStatement("delete from experiments where id=?");
	ps.setInt(1, expr_par.getId());
	
	return ps.executeUpdate() > 0;
	
} catch (Exception e) {
	return false;
}

}

}
