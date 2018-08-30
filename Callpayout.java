package Monte;

import java.util.ArrayList;

public class Callpayout implements PayOut {
    double strikeprice;
 //random variables given to path
    
    
       public Callpayout(double strikeprice){
    	this.strikeprice = strikeprice;
    }
	
	@Override
	public double getPayout(StockPath path) {
		   double payout;
		    ArrayList<Double> prices = path.getPrices();
		    payout= Math.max(0.0, prices.get(prices.size()-1)- strikeprice);
			return payout;
		}
	
}


