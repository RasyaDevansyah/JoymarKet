package model;

public class Registrant {
	int id;
	String name;
	int age;
	String address;
	double ipk;

	public Registrant(int id, String name, int age, String address, double ipk) {
		super();
		this.id = id;
		this.name = name;
		this.age = age;
		this.address = address;
		this.ipk = ipk;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double getIpk() {
		return ipk;
	}

	public void setIpk(double ipk) {
		this.ipk = ipk;
	}

}
