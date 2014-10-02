
public class Archer extends Warrior{

	public Archer(String name) {
		super(name);
		
	}
	public Archer(String name, float strength){
		super(name,strength);
	}
	@Override
	public void fight() {
		System.out.println("TWANG!  " + getName() + " says: Take that in the name of my lord, " + getEmployer());
	}


}
