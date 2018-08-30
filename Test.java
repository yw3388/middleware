package Middleware;

import java.util.ArrayList;

import javax.jms.Destination;
import javax.jms.Session;
import javax.jms.TextMessage;


import Monte.Antithetic;
import Monte.Asianpayout;
import Monte.Callpayout;
import Monte.GeometricPath;
import Monte.NormalRandomVectorGenerator;
import Monte.PayOut;
import Monte.Pricing;
import junit.framework.TestCase;

public class Test extends TestCase {

		public void testconvertinformation() throws Exception{
			String t = "7 0.02 100 10 0.01 European";
			
			String text[] = t.split(", ");
			double strikeprice = Double.parseDouble(text[0]);
			double sigma = Double.parseDouble(text[1]);
			int time = Integer.parseInt(text[2]);
			double spotprice = Double.parseDouble(text[3]);
			double interestrate = Double.parseDouble(text[4]);
			String type = text[5];
			Pricing p = new Pricing(strikeprice,  sigma,  time, spotprice, interestrate, type); 
			System.out.println(p.getType());
			System.out.println(p.getInterestrate());
	}
		
		public void test_simulation() throws Exception {
			int t = 100;
			double spotprice = 10;
			  double sigma = 0.02;
			  double interestrate = 0.01;
			  double strikeprice = 7;
			  String type = "EuropeanCall";
			 NormalRandomVectorGenerator g = new NormalRandomVectorGenerator(t);
			  Antithetic anti = new Antithetic(g, t);
			   ArrayList<Double> vectors = anti.getVector();
			GeometricPath path = new GeometricPath(spotprice, sigma, interestrate, t, vectors);
			String result = "";
			switch(type) {
			case "AsianCall":
				PayOut PayOut1 = new Asianpayout(strikeprice);
				result = String.valueOf(PayOut1.getPayout(path) * Math.exp(-1 * interestrate * t));							
				break;
			case "EuropeanCall":
				PayOut PayOut2 = new Callpayout(strikeprice);
				result = String.valueOf(PayOut2.getPayout(path) * Math.exp(-1 * interestrate * t));
				break;
			default:
				// When the option's type is bad, close the connection
				System.out.println("No type found");
				break;		
			}
			System.out.println(type);
			System.out.println(result);
			MonteCarloClient m = new MonteCarloClient("1");
			Session session = m.getSession();
			TextMessage message = session.createTextMessage(result);
		    System.out.println(message.getText());
		    
		}
			
			

}
