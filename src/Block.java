import java.io.IOException;
import java.sql.Timestamp;
import java.util.Random;

public class Block {

	private int index;							//index of the block in the list
	private Timestamp timestamp; 		//time at which transaction has been processed 
	private Transaction transaction; 			//the Transaction Object
	private String nonce; 						//random string (for proof of work)
	private String previousHash;				// previous Hash(set to "" in first block)
	private String hash; 					  // hash of the block (hash of the string obtained from previous variables via toString() method)
	
	public Block(int indexTxt, long timestampTxt, Transaction tText, String nonceTxt, String expHashTxt) {
		
		index = indexTxt;
		timestamp = new java.sql.Timestamp(timestampTxt);
		transaction = tText;
		nonce = nonceTxt;
		hash = expHashTxt;
		
	}

	public Block(int indexNew, Transaction transNew, String prevHashNew) {
		
		//setting variables with passed info
		
		timestamp = new Timestamp(System.currentTimeMillis());
		
		index = indexNew;
		transaction = transNew; 
		previousHash = prevHashNew;
		
	}
	// hash to validate the existing hases from the txt file
	public void createHashFromNonce() throws IOException {
		
		hash = Sha1.hash(this.toString());
		
	}
	
	

	public void createHash() throws IOException {

		nonce = "Blank";
		String symbols = ".&%^@#!?0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Random sym = new Random();
		
		createHashFromNonce();
		int counter=0;
		while(!hash.startsWith("00000")) {
			nonce = "";
			for (int i = 0; i < 10; ++i) {
				int numIndex = sym.nextInt(symbols.length());
				nonce = nonce.concat(Character.toString(symbols.charAt(numIndex)));
			counter++;	
			}
		
		createHashFromNonce();
		
		}
		System.out.println("Trise to figure out nounce was: "+counter);	
	}
	
	
	//Getter and setter for Index
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	//Getter and setter for Previous Hash
	public String getPreviousHash() {
		return previousHash;
	}

	public void setPreviousHash(String previousHash) {
		this.previousHash = previousHash;
	}
	//Getter and setter for Hash
	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}
	
	
	//------- to string method
	
	public String getNonce() {
		return nonce;
	}

	public void setNonce(String nonce) {
		this.nonce = nonce;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}
	
	

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public String toString () {
		
		return timestamp.toString() + ":" + transaction.toString() + "." + nonce + previousHash;
	}

	
	
}
