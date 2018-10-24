import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class BlockChain {
	// private variable
	private ArrayList<Block> masterChain;

	// constructor that takes an array list
	public BlockChain(ArrayList<Block> in) {

		masterChain = in;

	}

	public static void main(String[] args) throws IOException {

		// Read from file
		Scanner readerTxt = new Scanner(System.in);
		System.out.println("Enter the name of the text file you want to use");
		String fileName = readerTxt.nextLine();

		BlockChain blockObj;
		blockObj = BlockChain.fromFile(fileName);

		// step 2:VALIDATE BLOCKCHAIN

		boolean valid = blockObj.validateBlockchain();
		if (!valid) {
			readerTxt.close();
			throw new IOException("The blockcahin is Invalid");

		}

		// step 3: PROMPT TO USE NEW TRANSACTION AND VERIFY IT
		 else {
			// the transaction is valid then add to block chain
			// pass transaction the sender, receiver, amount then pass that to block

			
			// uncomenting the line below so that the while is false in order to 
			 //validate other friends chains without making transactions
			// boolean makeTransaction = false;
			
			while(true) {
				String senderIn;
				System.out.println("Enter new Transaction. Sender: ");
				senderIn = readerTxt.nextLine();

				String receiverIn;
				System.out.println("Enter the Receiver: ");
				receiverIn = readerTxt.nextLine();

				int amountIn;
				System.out.println("Enter the amount: ");
				String amountStr = readerTxt.nextLine();
				amountIn = Integer.parseInt(amountStr);

				if (blockObj.getBalance(senderIn) < amountIn) {
					System.out.println("Not enough funds, please try again later!");
				}
				
				Transaction transactionTemp = new Transaction(senderIn, receiverIn, amountIn);
				Block blockTempNew = new Block( blockObj.getMasterChain().size(), transactionTemp, blockObj.getMasterChain().get(blockObj.getMasterChain().size()-1).getHash());

				// step 4:ADDING TRANSACTION TO BLOCKCHAIN
				blockTempNew.createHash();
				blockObj.add(blockTempNew);
				
				if(!blockObj.validateBlockchain()){
					readerTxt.close();
					throw new IOException();
					
				}

				// step 5: ASK MORE TRANSACTIONS AND THEN BACK TO step 3

				System.out.println("Would you like to make another transaction? (YES/NO) ");
				String anotherT = readerTxt.nextLine();
				if (anotherT.toUpperCase().equals("NO")) {
					break;
				}

			}

		}

		// step 6: SAVING BLOCKCHAIN TO A FILE WITH SPECIFIC FILENAME

		readerTxt.close();
		blockObj.toFile(fileName);

	}
	// step 6: SAVING BLOCKCHAIN TO A FILE WITH SPECIFIC FILENAME

	public void toFile(String nameFileOut) throws IOException {

		String txtFileOut = nameFileOut + "_amold022.txt";
		OutputStream outStream = new FileOutputStream(txtFileOut);
		for (Integer i = 0; i < masterChain.size(); i++) {

			Block blockPrint = masterChain.get(i);
			
			outStream.write(i.toString().getBytes());
			outStream.write("\n".getBytes());
			outStream.write(String.valueOf(blockPrint.getTimestamp().getTime()).getBytes());
			outStream.write("\n".getBytes());
			outStream.write(blockPrint.getTransaction().getSender().getBytes());
			outStream.write("\n".getBytes());
			outStream.write(blockPrint.getTransaction().getReceiver().getBytes());
			outStream.write("\n".getBytes());
			outStream.write(String.valueOf(blockPrint.getTransaction().getAmount()).getBytes());
			outStream.write("\n".getBytes());
			outStream.write(blockPrint.getNonce().getBytes());
			outStream.write("\n".getBytes());
			outStream.write(blockPrint.getHash().getBytes());
			outStream.write("\n".getBytes());

		}

		outStream.close();

	}

	// accessing the masterChain
	public ArrayList<Block> getMasterChain() {
		return masterChain;
	}

// from File method that 
	public static BlockChain fromFile(String fileName) throws IOException {

		ArrayList<Block> mainChain = new ArrayList<Block>();

		// -----------Reads from File--------------------------
		File path = new File(fileName);
		// VALIDATION TO MAKE SURE THE FILE EXISTS
		System.out.println("we got file called: " + path);
		System.out.println("does it exist? : " + path.exists());
		System.out.println("is this a directory? : " + path.isDirectory());

		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)));

		// --------------------------------------------------------
		String line = reader.readLine();

		int counter = 0;
		int indexTemp = 0;
		long timestampTemp = 0;
		String senderTemp = null;
		String receiverTemp = line;
		int amountTemp = 0;
		String nonceTemp = null;
		String expHashTemp = null;

		// these while loops go through 7 sets of lines in the text file to get all of
		// the information and
		// set it in the block class to make blocks

		while (line != null) {
			while (line != null && counter <= 6) {

				switch (counter) {

				case 0:
					indexTemp = Integer.parseInt(line);
					break;
				case 1:
					timestampTemp = Long.parseLong(line);
					break;
				case 2:
					senderTemp = line;
					break;
				case 3:
					receiverTemp = line;
					break;
				case 4:
					amountTemp = Integer.parseInt(line);
					break;
				case 5:
					nonceTemp = line;
					break;
				case 6:
					expHashTemp = line;
					break;
				}

				line = reader.readLine();
				counter++;
			}
			// we call transaction class and set the transaction information and we call
			// Block class to set up
			// a block
			Transaction transTemp = new Transaction(senderTemp, receiverTemp, amountTemp);
			Block blockTemp = new Block(indexTemp, timestampTemp, transTemp, nonceTemp, expHashTemp);

			// this for loop makes sure to set previous hash filed of the first block as
			// 000000
			// and it checks the following blocks to set the previous hash filed of the has
			// from the
			// previous block i-1

			for (int i = 0; i <= mainChain.size(); i++) {
				if (i == 0) {
					blockTemp.setPreviousHash("00000");
				} else {
					String phash = mainChain.get(i - 1).getHash();
					blockTemp.setPreviousHash(phash);
				}
			}
			// as soon as we are done setting the info of the the block then we ask the
			// system
			// to add the block to the arrayList of blocks and then we set the counter to
			// zero
			// then we activate the loop again to read the next 7 lines in the text file and
			// create the next block
			mainChain.add(blockTemp);
			counter = 0;
		}

		reader.close();

		BlockChain bChain = new BlockChain(mainChain);

		return bChain;
	}

	public boolean validateBlockchain() throws IOException {

		for (int i = 1; i < masterChain.size(); i++) {
			Block previous = masterChain.get(i - 1);
			Block current = masterChain.get(i);

			if (!current.getPreviousHash().equals(previous.getHash())) {
				return false;
			}
			if (current.getIndex() != i) {
				return false;
			}

			String currentHash = current.getHash();
			current.createHashFromNonce();
			if (!current.getHash().equals(currentHash)) {
				return false;
			}

		}

		Block current = masterChain.get(0);

		if (!current.getPreviousHash().equals("00000")) {
			return false;
		}
		if (current.getIndex() != 0) {
			return false;
		}

		return true;
	}

	public int getBalance(String userName) {

		int amount = 0;

		for (int i = 0; i < masterChain.size(); i++) {

			if (masterChain.get(i).getTransaction().getSender().equals(userName)) {
				amount = amount - masterChain.get(i).getTransaction().getAmount();
			}
			if (masterChain.get(i).getTransaction().getReceiver().equals(userName)) {
				amount = amount + masterChain.get(i).getTransaction().getAmount();
			}
		}

		return amount;
	}

	public void add(Block block) {
		masterChain.add(block);
	}

}
