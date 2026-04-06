package problemdomain;

public class TaxCalculator implements Taxable {
	private double taxRate;
	
	public TaxCaculator(double taxRate) {
		this.taxRate = taxRate;
	}

	public double getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(double taxRate) {
		this.taxRate = taxRate;
	}

	
	@Override
	public double applyTax(double salary) {
		return salary*taxRate;
	}

}
