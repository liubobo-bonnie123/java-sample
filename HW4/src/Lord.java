import java.util.ArrayList;
public class Lord extends Noble{
	private ArrayList<Fighter> employee = new ArrayList();
	public Lord(String name) {
		// TODO Auto-generated constructor stub
		super(name);
	}
	
	public String toString(){
		return getName() + " has an army of " + employee.size() + (employee.isEmpty()?"":employee.toString()).replace("[", "\n\t ").replace(",", "\n\t").replace("]","");
	}
	
	public void hire (Fighter aFighter){
		if (isDead())
			System.out.println(getName() + " could not hire " + aFighter.getName());
		else
			if(aFighter.setEmployer(this))
				employee.add(aFighter);
			else
				System.out.println(aFighter.getName() + " is dead, cannot hire him");
	}
	public void fire (Fighter aFighter){
		if (employee.remove(aFighter))
			aFighter.runsaway();
	}
	public void kill(){
		super.kill();
		for(Fighter aFighter:employee)
			aFighter.kill();
	}
	
	
	@Override
	public float getStrength(){
		int totalStrength = 0;
		for(Fighter aFighter:employee)
			totalStrength += aFighter.getStrength();
		return totalStrength;
	}
	@Override
	public void setStrength(float strengthRatio){
		for(Fighter aFighter:employee)
			aFighter.setStrength(strengthRatio);
	}
	@Override
	public void fight() {
		for(Fighter aFighter:employee)
			aFighter.fight();
	}

}
