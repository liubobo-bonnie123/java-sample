
public class Swordsman extends Warrior{

	public Swordsman(String name) {
		super(name);
		
	}
	public Swordsman(String name, float strength){
		super(name,strength);
	}
	@Override
	public void fight() {
		System.out.println("CLANG!  " + getName() + " says: Take that in the name of my lord, " + getEmployer());
	}


}
