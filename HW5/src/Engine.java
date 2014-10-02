
public class Engine{
	public Engine() {
		}
	public Engine(String brand){
		this.brand = brand;
	}
	public Engine(int horsepower){
		this.horsepower = horsepower;
	}
	public Engine(String brand, int horsepower) {
		this.brand = brand;
		this.horsepower = horsepower;
	}
	
	private int horsepower = 100;
	private String brand = "Poly";
	public String toString(){
		return 	 
				"\tbrand:" + "\t\t" + brand + "\n\t" +
				"horsepower:" + "\t" + horsepower + "\n";
	}
	public int getHorsepower(){
		return horsepower;
	}
	public boolean equals(Engine otherEngine){
		if (this.brand.equals(otherEngine.brand))
			if (this.horsepower == otherEngine.horsepower)
				return true;
			else
				return false;
		else
			return false;
	}

}
