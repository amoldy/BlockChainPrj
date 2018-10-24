

public class Transaction {

	private String sender;
	private String receiver;
	private int amount;
	
	//constructor that sets the variables
	public Transaction(String sender, String receiver, int amount) {
		
		this.sender = sender;
		this.receiver = receiver;
		this.amount = amount;
		
	}
		
	
	// SET & GET SENDER--------------------------------
	public String getSender() {
		return sender;
	}



	public void setSender(String sender) {
		this.sender = sender;
	}


	// SET & GET RECEIVER
	public String getReceiver() {
		return receiver;
	}



	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}


	// SET & GET AMOUNT
	public int getAmount() {
		return amount;
	}



	public void setAmount(int amount) {
		this.amount = amount;
	}
	//------------end setters and getters ----------------


	//Returns info about transaction
	public String toString() {
		
		
		return sender + ":" + receiver + "=" + amount;
	}
	
}
