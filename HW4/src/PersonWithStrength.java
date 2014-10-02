
public class PersonWithStrength extends Noble{
	private float strength;
	public PersonWithStrength(String name) {
		super(name);
	}
	public PersonWithStrength(String name,float strengh) {
		super(name);
		this.strength = strengh;
	}
	public String toString(){
		return getName() + ": " + strength;
	}
	public void hire(Warrior aWarrior){
		System.out.println(getName() + " could not hire " + aWarrior.getName());
	}
	@Override
	public void setStrength(float strengthRatio){
		strength = strength*(1-strengthRatio);
	}
	@Override
	public float getStrength() {
		return strength;
	}
	@Override
	public void fight() {};
	
}
