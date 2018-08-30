package Monte;

import java.util.ArrayList;

public class Asianpayout implements PayOut{
        ArrayList<Double>vectors; //random variables given to path
        double strikeprice;
        
        public Asianpayout(double strikeprice){
        	this.strikeprice = strikeprice;
        }
        
	@Override
	public double getPayout(StockPath path) {
		// TODO Auto-generated method stub
	    double Payout = 0;
	    ArrayList<Double> prices = path.getPrices();
	    for(int i = 0; i < prices.size(); i++){
	    Payout += (double) prices.get(i);
	    }
	    Payout /= (double) prices.size();
	    Payout = Math.max(0.0, Payout-strikeprice);
	    return Payout;
	    
	}
	

		
		
		
	}

