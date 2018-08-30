package Monte;

import java.util.ArrayList;
//not very generic, tailor to the case of normal distribution
public class Antithetic implements RandomVectorGenerator{
	int t;
	public RandomVectorGenerator generator;
	public boolean anti = false;
	public ArrayList<Double> tempVec;
	
	//TODO: Constructor
	public Antithetic(RandomVectorGenerator generator, int t){
		//System.out.println(generator.getVector());
		this.generator = generator;
		//System.out.println(generator.getVector());
		this.t = t;
		
	}

	@Override
	public ArrayList<Double> getVector() {
		// TODO Auto-generated method stub
		if(anti){
		this.anti = false;
		return negate(tempVec);
		}
		tempVec = generator.getVector();
		this.anti = true;
		return tempVec;
	}
		

	private ArrayList<Double> negate(ArrayList<Double> tempVec) {
		// TODO Auto-generated method stub
		ArrayList<Double>tempVec2 = new ArrayList<Double>(); 
		for (double vec: tempVec){
			vec = vec * -1;
			tempVec2.add(vec);
		}
		return tempVec2;
		
	}



}
