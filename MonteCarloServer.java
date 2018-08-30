package Middleware;



import javax.jms.*;
import javax.jms.Message;
import org.apache.activemq.*;
import Monte.*;


public class MonteCarloServer {
	   protected static String brokerURL = "tcp://localhost:61616";
	   protected static ConnectionFactory factory;
	   protected javax.jms.Connection connection;
	   protected Session session;
	   protected MessageProducer producer;
	   protected static int count = 100;
	   protected String optionid;
	   protected Stats stats;
	   
	   public MonteCarloServer(String optionid, int count) throws JMSException {
		      factory = new ActiveMQConnectionFactory(brokerURL);
		      connection = factory.createConnection();
		      connection.start();
		      session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		      producer = session.createProducer(null);
		      this.optionid = optionid;
		      stats = new Stats();     
	   }
	   
	   public static void main(String[] args) throws Exception {
		      MonteCarloServer publisher = new MonteCarloServer("1", 100);
		      publisher.run();

		   }
	 
	public void sendMessage(String strike, String sigma, String time, String spotprice, String interestrate, String type) throws JMSException {
		Destination destination = session.createQueue(optionid + "one request");
		TextMessage message = session.createTextMessage(strike + sigma + time + spotprice + interestrate + type);
		producer.send(destination, message);
		
		
	}
	   private void run() throws JMSException {
		//listener listen to interested message
		Destination destination = session.createQueue(optionid + "result");
	     MessageConsumer consumer = session.createConsumer(destination);
			while(notFinish()) {
	    	 for (int i = 0; i < count; i++){
	    		 sendMessage("165 ", "0.01 ", "252 ", "152.35 ", "0.0001 ", "EuropeanCall");
	    	 }  
	         consumer.setMessageListener(new MessageListener() {
	         public void onMessage(Message message) {
	            if (message instanceof TextMessage){
	               try{
	            	   double newPayout = Double.parseDouble( ((TextMessage) message).getText());
	            	   stats.newstats(newPayout);
	                   message.acknowledge();
	               } catch (JMSException e ){
	                  e.printStackTrace();
	               }
	               
	            }
	         }	
	         
	});  
	         System.out.println("Number of iterations is: " + stats.getTimes() + "  Estimated price is: " + stats.getExpectation());
			}
	     
			System.out.println("The price of the option is: "+ stats.getExpectation()* Math.exp(-0.0001 * 252));
	}
	   
	
	
	
	//check for accuracy
	private boolean notFinish(){
		double p = 0.04;
        double t = Math.sqrt(Math.log(1/(p*p)));
        double c0 = 2.515517;
		double c1 = 0.802853;
		double c2 = 0.010328;
		double d1 = 1.432788;
		double d2 = 0.189269;
	    double d3 = 0.001308;
	    double x = t - (c0 + c1 * t + c2 * t * t)/(1 + d1 * t+ d2 * t * t + d3 * t* t * t);
	    if ( x * stats.getSigma() / Math.sqrt(stats.getTimes()) <= 0.1 && stats.getSigma() > 0 && stats.getTimes()>100){
			return false;
		}
	    return true;
}

	public void close() throws Exception{
		if(connection != null){
			connection.close();
		}
	}
	
}
