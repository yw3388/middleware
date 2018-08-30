package Monte;


import java.util.ArrayList;

public interface StockPath {
	//interface for creating StockPath. The returned list should be ordered by date
	//return an arraylist of vectors(special case:  normal vectors)
	public ArrayList getPrices();
}
