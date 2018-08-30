package Monte;

import java.util.ArrayList;

public class Stats {
    
	ArrayList<Double>_initial;//initial arraylist of payouts zero
	int curr_iter;		// current iteration idx
	double mu;
	double sigma;
	double secondMom;
	
	public Stats(){
		this._initial= new ArrayList<Double>();
		mu = 0;
		sigma = 0;
		secondMom = 0;
		curr_iter = 0;
	}
		
	public void setExpectation(ArrayList<Double> initial){
		this._initial = initial;
		double sum = 0;
		for(int n = 0; n < initial.size(); n++){
			sum += initial.get(n);	
		}
		this.mu = sum/initial.size();
	}
	
	public void setSigma(){
		
		this.sigma = Math.sqrt(secondMom-mu*mu);
	}
	
	public double getExpectation(){
		return this.mu;
	}
	
	public double getSigma(){
		return this.sigma;
	}
	public double getSecondMoments(){
		return this.secondMom;
	}
	
	public int getTimes(){
		return this.curr_iter;
	}
	
	public void newstats(double newPayout){
		_initial.add(newPayout);
		curr_iter++;
		if(curr_iter == 1){
			mu = newPayout;
			sigma = 0;
			return;
		}
		this.mu =( mu * (curr_iter-1) + newPayout)/(curr_iter);
		double r = 0;
		for(int i =0; i < curr_iter; i++){
			r = r + (_initial.get(i) - mu) * (_initial.get(i) - mu);
		}
		sigma = Math.sqrt(r/curr_iter);
	}
}
