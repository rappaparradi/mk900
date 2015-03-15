package entities;

public class ExpRaws {
	
	int id;
	int exp_id; 
	Double Tsec;
	int i;
	Double w_sr;
	Double q_sr;
	Double y_sr;
	Double v;
	Double R;
	Double K_p;
	int opt;
	
	public ExpRaws() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ExpRaws(int id, int exp_id, Double tsec, int i, Double w_sr,
			Double q_sr, Double y_sr, Double v, Double r, Double k_p, int opt) {
		super();
		this.id = id;
		this.exp_id = exp_id;
		Tsec = tsec;
		this.i = i;
		this.w_sr = w_sr;
		this.q_sr = q_sr;
		this.y_sr = y_sr;
		this.v = v;
		R = r;
		K_p = k_p;
		this.opt = opt;
	}
	public ExpRaws(int exp_id, Double tsec, int i, Double w_sr,
			Double q_sr, Double y_sr, Double v, Double r, Double k_p, int opt) {
		super();
		this.exp_id = exp_id;
		Tsec = tsec;
		this.i = i;
		this.w_sr = w_sr;
		this.q_sr = q_sr;
		this.y_sr = y_sr;
		this.v = v;
		R = r;
		K_p = k_p;
		this.opt = opt;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getExp_id() {
		return exp_id;
	}
	public void setExp_id(int exp_id) {
		this.exp_id = exp_id;
	}
	public Double getTsec() {
		return Tsec;
	}
	public void setTsec(Double tsec) {
		Tsec = tsec;
	}
	public int getI() {
		return i;
	}
	public void setI(int i) {
		this.i = i;
	}
	public Double getW_sr() {
		return w_sr;
	}
	public void setW_sr(Double w_sr) {
		this.w_sr = w_sr;
	}
	public Double getQ_sr() {
		return q_sr;
	}
	public void setQ_sr(Double q_sr) {
		this.q_sr = q_sr;
	}
	public Double getY_sr() {
		return y_sr;
	}
	public void setY_sr(Double y_sr) {
		this.y_sr = y_sr;
	}
	public Double getV() {
		return v;
	}
	public void setV(Double v) {
		this.v = v;
	}
	public Double getR() {
		return R;
	}
	public void setR(Double r) {
		R = r;
	}
	public Double getK_p() {
		return K_p;
	}
	public void setK_p(Double k_p) {
		K_p = k_p;
	}
	public int getOpt() {
		return opt;
	}
	public void setOpt(int opt) {
		this.opt = opt;
	}
	
	
}
