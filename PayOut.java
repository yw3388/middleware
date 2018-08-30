package Monte;

import java.util.ArrayList;

public interface PayOut {
	//function of payout
	//return a price for a simulation
	public double getPayout(StockPath path);
	

}
