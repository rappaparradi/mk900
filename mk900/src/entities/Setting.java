package entities;

public class Setting {
	
	String name;
	String value;
	String desc;
	int sort_ind;
	int exp_id;
	
	public int getExp_id() {
		return exp_id;
	}
	public void setExp_id(int exp_id) {
		this.exp_id = exp_id;
	}
	public int getSort_ind() {
		return sort_ind;
	}
	public void setSort_ind(int sort_ind) {
		this.sort_ind = sort_ind;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public Setting(String name, String value, String desc) {
		super();
		this.name = name;
		this.value = value;
		this.desc = desc;
		this.desc = desc;
	}
	public Setting(String name, String value, String desc, int sort_ind) {
		super();
		this.name = name;
		this.value = value;
		this.desc = desc;
		this.sort_ind = sort_ind;
		this.exp_id = 0;
	}
	public Setting(String name, String value, String desc, int sort_ind, int exp_id) {
		super();
		this.name = name;
		this.value = value;
		this.desc = desc;
		this.sort_ind = sort_ind;
		this.exp_id = exp_id;
	}
	public Setting() {
		super();
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}

}
