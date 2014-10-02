
public abstract class Fighter extends Person{
	private float strength;
	private Lord employer;
	public Fighter(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	public Fighter(String name, float strength){
		super(name);
		this.strength = strength;
	}
	
	public String toString(){
		return getName() + ": " + strength;
	}

	public float getStrength(){
		return strength;
	}
	public void setStrength(float strengthRatio){
		strength = strength*(1-strengthRatio);
	}
	public boolean setEmployer(Lord aLord){
		if (isDead()==false){
			employer = aLord;
			return true;
		}
		else
			return false;
	}
	public void runsaway(){
		if(employer!=null && isDead()==false){
			System.out.println("So long " + employer.getName() + ". I'm out'a here! -- " + getName());
			Lord exEmployer = employer;
			employer = null;
			exEmployer.fire(this);
		}
			
	}
	public void kill(){
		super.kill();
		strength = 0;
	}
	public String getEmployer(){
		return employer.getName();
	}
	public abstract void fight();

}
