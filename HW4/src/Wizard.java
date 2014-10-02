
public class Wizard extends Fighter{

	public Wizard(String name) {
		super(name);
	}
	public Wizard(String name, float strength){
		super(name,strength);
	}
	@Override
	public void fight() {
		System.out.println("POOF");
	}

}