package problemdomain;

public class Contractor extends Employee {
	private double hourlyRate;
	private int hoursWorked;

	public Contractor(int id, String name, String department, String email, String phone, String address,
			double hourlyRate, int hoursWorked) {
		super(id, name, department, email, phone, address);
		this.hourlyRate = hourlyRate;
		this.hoursWorked = hoursWorked;
	}

	public double getHourlyRate() {
		return hourlyRate;
	}

	public int getHoursWorked() {
		return hoursWorked;
	}

	public void setHourlyRate(double hourlyRate) {
		this.hourlyRate = hourlyRate;
	}

	public void setHoursWorked(int hoursWorked) {
		this.hoursWorked = hoursWorked;
	}

	@Override
	public String toString() {
		return "Contractor: " + super.toString() +","+ " hourlyRate=" + hourlyRate + ", hoursWorked=" + hoursWorked + "]";
	}

	@Override
	public double calculateSalary() {
		return hourlyRate * hoursWorked;
	}

}
