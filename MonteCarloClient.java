package Middleware;

import java.util.ArrayList;

import javax.jms.*;

import Monte.*;


import org.apache.activemq.ActiveMQConnectionFactory;


public class MonteCarloClient {
	 private static String brokerURL = "tcp://localhost:61616";
	   private static ConnectionFactory factory;
	   private Connection connection;
	   private Session session;
	   private String optionid;
	   protected MessageProducer producer;
	   
	   public MonteCarloClient(String optionid) throws Exception {
		      factory = new ActiveMQConnectionFactory(brokerURL);
		      connection = factory.createConnection();
		      session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		      producer = session.createProducer(null);
		      connection.start();
		    
		      this.optionid = optionid;
		   }
	   public static void main(String[] args) throws Exception {
		      MonteCarloClient client = new MonteCarloClient("1");
		      client.run();
		   }
	private void run() throws JMSException {
		Destination destination = session.createQueue(optionid + "one request");
	      MessageConsumer messageConsumer =session.createConsumer(destination);
	      messageConsumer.setMessageListener(new MessageListener() {
	         public void onMessage(Message message) {
	            if (message instanceof TextMessage){
	               try{
	            	   Pricing p = convertinformation( (TextMessage) message);
	                   String payout = Simulation(p);
	                   sendMessage(payout);
	                   message.acknowledge();
	               } catch (JMSException e ){
	                  e.printStackTrace();
	               }
	            }
	         }
	      });
	      }
	         
	         //message:"strike， +  sigma， + time， +  spotprice， +  interestrate， +  type"
			public Pricing convertinformation(TextMessage message) throws JMSException {
				String[] infor = message.getText().split("\\s");
				double strike = Double.parseDouble(infor[0]);
				double sigma = Double.parseDouble(infor[1]);
				int t = Integer.parseInt(infor[2]);
				double spotprice = Double.parseDouble(infor[3]);
				double interestrate = Double.parseDouble(infor[4]);
				String type = infor[5];
				Pricing p = new Pricing(strike,  sigma,  t,  spotprice, interestrate, type); 
				return p;     
				
			}
	    
	     
			   
		public String Simulation(Pricing p){
			  int t = p.getMaturitydate();
			  double spotprice = p.getSpotprice();
			  double sigma = p.getSigma();
			  double interestrate = p.getInterestrate();
			  double strikeprice = p.getStrikeprice();
			  String type = p.getType();
			 NormalRandomVectorGenerator g = new NormalRandomVectorGenerator(t);
			  Antithetic anti = new Antithetic(g, t);
			   ArrayList<Double> vectors = anti.getVector();
			GeometricPath path = new GeometricPath(spotprice, sigma, interestrate, t, vectors);
			String result = "";
			switch(type) {
			case "AsianCall":
				PayOut PayOut1 = new Asianpayout(strikeprice);
				result = String.valueOf(PayOut1.getPayout(path) * Math.exp(-1 * p.getInterestrate() * p.getMaturitydate()));							
				break;
			case "EuropeanCall":
				PayOut PayOut2 = new Callpayout(strikeprice);
				result = String.valueOf(PayOut2.getPayout(path) * Math.exp(-1 * p.getInterestrate() * p.getMaturitydate()));
				break;
			default:
				// When the option's type is bad, close the connection
				System.out.println("No type found");
				break;		
			}
			return result;
		}
		
		   private void sendMessage(String result) throws JMSException{
			   Destination destination = session.createQueue(optionid + "result");
			   TextMessage message = session.createTextMessage(result);
			   producer.send(destination, message);
			  
		
		}
		   public Session getSession() {
			     return session;
			  }
		  
		   public void close() throws JMSException {
		       if (connection != null) {
		           connection.close();
		       }
		   }
   
   
}

	

