package problemdomain;

public abstract class Employee {
	private int id;
	private String name;
	private String department;
	private String email;
	private String phone;
	private String address;

	public Employee(int id, String name, String department, String email, String phone, String address) {
		this.id = id;
		this.name = name;
		this.department = department;
		this.email = email;
		this.phone = phone;
		this.address = address;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDepartment() {
		return department;
	}

	public String getEmail() {
		return email;
	}

	public String getPhone() {
		return phone;
	}

	public String getAddress() {
		return address;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "Employee [id=" + id + ", name=" + name + ", department=" + department + ", email=" + email + ", phone="
				+ phone + ", address=" + address;
	}

	public abstract double calculateSalary();

}
