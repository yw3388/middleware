package Monte;

import java.util.ArrayList;

public class Pricing {
	double strikeprice;
	static double spotprice;
	static double sigma;
	static double interestrate;
	PayOut payout;
	RandomVectorGenerator rvg;
    public Stats stats;
	StockPath path;
	static int maturitydate;
	String type;
	    static double p = 0.04;
        static double t = Math.sqrt(Math.log(1/(p*p)));
        static double c0 = 2.515517;
		static double c1 = 0.802853;
		static double c2 = 0.010328;
		static double d1 = 1.432788;
		static double d2 = 0.189269;
	    static double d3 = 0.001308;
	    static double x = t - (c0 + c1 * t + c2 * t * t)/(1 + d1 * t+ d2 * t * t + d3 * t* t * t);

	
	public Pricing(double strikeprice, double sigma, int t, double spotprice, double interestrate, String type){
		this.strikeprice = strikeprice;
		this.sigma = sigma;
		this.interestrate = interestrate;
		this.spotprice = spotprice;
		this.maturitydate = t;
		this.type = type;
	}
	public double getStrikeprice(){
		return strikeprice;
	}
	public double getSigma(){
		return sigma;
	}
	public double getInterestrate(){
		return interestrate;
	}
	public double getSpotprice(){
		return spotprice;
	}
	public int getMaturitydate(){
		return maturitydate;
	}
	public String getType(){
		return type;
	}
	
	public double simulation(PayOut payout, Stats stats, double x){
		boolean flag = true;
		while(flag) {
		 //Generate random variable
		   NormalRandomVectorGenerator g = new NormalRandomVectorGenerator(maturitydate);
		   //Get price
		   Antithetic anti = new Antithetic(g, maturitydate);
		   ArrayList<Double> vectors = anti.getVector();
		   GeometricPath path = new GeometricPath(spotprice, sigma, interestrate, maturitydate, vectors);
		   double currentPrice = payout.getPayout(path) * Math.exp(-1 * interestrate * maturitydate);
		   stats.newstats(currentPrice);
		   if ( x * stats.getSigma() / Math.sqrt(stats.getTimes()) <= 0.1 && stats.getSigma() > 0){
				flag = false;
			}
		}
		return stats.getExpectation();	
	}
	
public static void main( String[] args ) {
	double initialPrice = 152.35;
	double sigma = 0.01;
	double interestRate = 0.0001;
	int period = 252;
	double strikePrice = 165;
	String type = "Europeancall";
	Stats stats = new Stats();
	PayOut payOut = new Callpayout(strikePrice);
	Pricing p = new Pricing(strikePrice, sigma, period, initialPrice, interestRate, type);
	double price = p.simulation( payOut, stats, x) * Math.exp(-maturitydate*0.0001);
	System.out.println("The first option's price should be " + price);	
	
  String type2 = "AsianCall";
  double strikePrice2 = 164;
  Stats statsCollector2 = new Stats();
  PayOut payOut2 = new Asianpayout(strikePrice2);
  Pricing p2 = new Pricing(strikePrice2, sigma, period, initialPrice, interestRate, type2);
  double price2 = p2.simulation( payOut2, statsCollector2, x)*Math.exp(-maturitydate*0.0001);
  System.out.println("The second option's price should be " + price2);
}	
}
	

	
	

