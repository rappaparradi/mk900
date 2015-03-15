package entities;

public class Experiment {
	
	int id;
	long date;
	public Experiment() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Experiment(int id, long date) {
		super();
		this.id = id;
		this.date = date;
	}
	
	public Experiment(long date) {
		super();
		this.date = date;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
	}
	

}
