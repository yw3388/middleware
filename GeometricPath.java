package Monte;

import java.util.ArrayList;

public class GeometricPath implements StockPath {
   
	double spotprice;
	double sigma;
	double r;
	ArrayList<Double> vectors;
	ArrayList<Double> prices;
   
   public GeometricPath(double spotprice, double sigma, double r, int t, ArrayList<Double>vectors){
	   this.spotprice = spotprice;
	   this.sigma = sigma;
	   this.r = r;
	   this.vectors = vectors;
   }


	@Override
	
	public ArrayList<Double> getPrices() {
		ArrayList<Double> prices = new ArrayList<Double>(vectors.size());
		prices.add(0, spotprice);
	    for(int i = 1; i < vectors.size(); i++){
	    	double newprice = prices.get(i-1) * Math.exp(r-0.5*sigma*sigma + sigma*vectors.get(i-1));
	    	prices.add(i, newprice);
	}
		return prices;
	   
	}
}
