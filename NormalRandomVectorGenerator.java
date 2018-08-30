package Monte;

import java.util.ArrayList;
import java.util.Random;

public class NormalRandomVectorGenerator implements RandomVectorGenerator{
	   static int t;
	   static ArrayList<Double>list;
	
	   //setter & getter
	   
	public NormalRandomVectorGenerator(int t){
		NormalRandomVectorGenerator.t = t;
		
		
	}
	public void generateNormalVector(){
		Random r = new Random();
		list = new ArrayList<Double>(t);
		for(int i = 0; i < t; i++){
		list.add(r.nextGaussian());
	}
	}

	@Override
	public ArrayList<Double> getVector() {
		// TODO Auto-generated method stub
		this.generateNormalVector();
		return list;
	}

}
