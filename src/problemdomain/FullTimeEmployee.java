package problemdomain;

public class FullTimeEmployee extends Employee {
	private double baseSalary;
	private double bonus;

	public FullTimeEmployee(int id, String name, String department, String email, String phone, String address,
			double baseSalary, double bonus) {
		super(id, name, department, email, phone, address);
		this.baseSalary = baseSalary;
		this.bonus = bonus;
	}

	public double getBaseSalary() {
		return baseSalary;
	}

	public double getBonus() {
		return bonus;
	}

	public void setBaseSalary(double baseSalary) {
		this.baseSalary = baseSalary;
	}

	public void setBonus(double bonus) {
		this.bonus = bonus;
	}

	@Override
	public String toString() {
		return "FullTimeEmployee : " + super.toString() + ","+" baseSalary=" + baseSalary + ", bonus=" + bonus + "]";

	}

	@Override
	public double calculateSalary() {
		return baseSalary + bonus;
	}

}
