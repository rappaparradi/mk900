package models;

import java.util.*;

import entities.*;
import mk900.*;

import java.sql.*;

public class ExpRawsModel {

	public List<ExpRaws> findAll(int exp_id) {

		try {

			List<ExpRaws> ent_list = new ArrayList<>();
			PreparedStatement ps = Main.c
					.prepareStatement("select * from exp_raws where exp_id=?");
			ps.setInt(1, exp_id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				ExpRaws ent_var = new ExpRaws(rs.getInt("id"),
						rs.getInt("exp_id"), rs.getDouble("tsec"), rs.getInt("i"),
						rs.getDouble("w_sr"), rs.getDouble("q_sr"),
						rs.getDouble("y_sr"), rs.getDouble("v"),
						rs.getDouble("r"), rs.getDouble("k_p"),
						rs.getInt("opt"));
				ent_list.add(ent_var);
			}

			return ent_list;

		} catch (Exception e) {
			System.out.println(e.getClass().getName() + ": " + e.getMessage());
			return null;
		}

	}

	public ExpRaws find(int id_par, int exp_id) {

		try {

			ExpRaws ent_var = null;
			PreparedStatement ps = Main.c
					.prepareStatement("select * from exp_raws where id=?");
			ps.setInt(1, id_par);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {

				ent_var = new ExpRaws();
				ent_var.setId(rs.getInt("id"));
				ent_var.setExp_id(rs.getInt("exp_id"));
				ent_var.setTsec(rs.getDouble("tsec"));
				ent_var.setI(rs.getInt("i"));
				ent_var.setW_sr(rs.getDouble("w_sr"));
				ent_var.setQ_sr(rs.getDouble("q_sr"));
				ent_var.setY_sr(rs.getDouble("y_sr"));
				ent_var.setV(rs.getDouble("v"));
				ent_var.setR(rs.getDouble("R"));
				ent_var.setK_p(rs.getDouble("k_p"));
				ent_var.setOpt(rs.getInt("opt"));

			}

			return ent_var;

		} catch (Exception e) {
			return null;
		}

	}

	public boolean create(ExpRaws exp_par) {

		try {
			PreparedStatement ps = Main.c
					.prepareStatement("insert into exp_raws(exp_id, tsec, i, w_sr, q_sr, y_sr, v, R, k_p, opt)"
							+ " values (?,?,?,?,?,?,?,?,?,?)");
			ps.setInt(1, exp_par.getExp_id());
			ps.setDouble(2, exp_par.getTsec());
			ps.setInt(3, exp_par.getI());
			ps.setDouble(4, exp_par.getW_sr());
			ps.setDouble(5, exp_par.getQ_sr());
			ps.setDouble(6, exp_par.getY_sr());
			ps.setDouble(7, exp_par.getV());
			ps.setDouble(8, exp_par.getR());
			ps.setDouble(9, exp_par.getK_p());
			ps.setInt(10, exp_par.getOpt());

			return ps.executeUpdate() > 0;

		} catch (Exception e) {
			return false;
		}

	}

	public boolean delete(ExpRaws expr_par) {

		try {
			PreparedStatement ps = Main.c
					.prepareStatement("delete from exp_raws where exp_id=?");
			ps.setInt(1, expr_par.getExp_id());

			return ps.executeUpdate() > 0;

		} catch (Exception e) {
			return false;
		}

	}
	
	public boolean deleteBiExpId(int expr_id) {

		try {
			PreparedStatement ps = Main.c
					.prepareStatement("delete from exp_raws where exp_id=?");
			ps.setInt(1, expr_id);

			return ps.executeUpdate() > 0;

		} catch (Exception e) {
			return false;
		}

	}

}
