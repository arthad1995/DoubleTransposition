package doubleTransposition;

import java.util.ArrayList;
import java.util.HashSet;

public class DoubleTransposition {

	public static void main(String[] args) {
		String message = "Be at the third pillar from the left outside the lyceum theatre tonight at seven If you are distrustful bring two friends";
		System.out.println("Message: "+ message + '\n');
		System.out.println("First Cipher: \n");
		String  firstCipher = encrypt("Be at the third pillar from the left outside the lyceum theatre tonight at seven If you are distrustful bring two friends",
				"cryptographic");
		System.out.println("Second Cipher: \n");
		String secondCipher = encrypt(firstCipher, "network security");
		
		String cipherToDecrypt = "gMBhU F=9c2 Xzzlz pswIb ZZc32 mbSIW ac=kv S0mXG 2WdFR FmE4h Nd9cE Sj5jh uisvb Z=ccl v1xoR RV0lH S0Yhy =0sZR YZoyF dAoxF 4W2=6 3o2gl YLdbT GUIcZ LUbL0 vXmD3 GHXB5 hllm3 IMbbc aM0tW W=HVH kFRxN Rgk=R 1BhYQ G23pt  vZks9 abaZy  3IbZL  Ibcg5  HDCS2  GmNJ5  B=T2U  bZbXZ  Ryvuz  WX1G3 wMIRQ";
		String firstdec = decrypt(cipherToDecrypt, "NETWORKSCU");
		System.out.println(firstdec);
		String second = decrypt(firstdec, "CRYPTOGAHI");
		System.out.println(second);
	}
	


	private static String encrypt(String message, String keyword) {
		//comment
		
		message = message.replaceAll(" ", "").toLowerCase();
		keyword = keyword.toLowerCase();
		//check dup character
		HashSet<Character> dupSet = new HashSet<>();
		ArrayList<Turple> header = new ArrayList<>();
		int ii = 0;
		//store the header so we can know the read order and array index
		for (int i = 0; i < keyword.length(); i++) {
			if (dupSet.add(keyword.charAt(i))){
				if(keyword.charAt(i) == ' '){
					header.add(new Turple(ii, 0, keyword.charAt(i)));
				}
				else{
					header.add(new Turple(ii, keyword.charAt(i) - 96, keyword.charAt(i)));
				}
				ii++;
			}
		}
		//create array of ArrayList the message
		ArrayList<Character>[] listArray = new ArrayList[header.size()];
		//init Arraylists
		for (int i = 0; i < listArray.length; i++) {
			listArray[i] = new ArrayList<Character>();
		}
		for (int i = 0; i < message.length(); i++) {
			int position = i % header.size();
			listArray[position].add(message.charAt(i));
		}
		printTurpleList(header);
		printlistArray(listArray);
		HashSet<Integer> checked = new HashSet<>();	
		String outputCipher = outputCipher(listArray,  "",  "",  smallestIndex(header, checked), header, checked );
		System.out.println(outputCipher);
		System.out.println();
		return outputCipher;
	}
	
	
	private static String decrypt(String cipher, String keyword){
		HashSet<Character> dupSet = new HashSet<>();
		ArrayList<Turple> header = new ArrayList<>();
		keyword = keyword.toLowerCase();
		int ii = 0;
		for (int i = 0; i < keyword.length(); i++) {
			if (dupSet.add(keyword.charAt(i))){
				if(keyword.charAt(i) == ' '){
					header.add(new Turple(ii, 0, keyword.charAt(i)));
				}
				else{
					header.add(new Turple(ii, keyword.charAt(i) - 96, keyword.charAt(i)));
				}
				ii++;
			}
		}
		ArrayList<Character>[] listArray = new ArrayList[header.size()];
		//init Arraylists
		for (int i = 0; i < listArray.length; i++) {
			listArray[i] = new ArrayList<Character>();
		}
		cipher = cipher.replaceAll(" ", "");
		int totalRows= cipher.length()/header.size();
		int extra = (cipher.length()%header.size())-1;
		HashSet<Integer> checked = new HashSet<>();	
		int minIndex =smallestIndex(header, checked);
		int cipherIndex = 0;
		int j = 0;
		while(minIndex != -1){
			for(int i = 0 ; i< (header.get(minIndex).arrayIndex <= extra ? totalRows+1 : totalRows); i++ ){
				listArray[minIndex].add(cipher.charAt(cipherIndex));
				cipherIndex++;
			}
			j++;
			minIndex =smallestIndex(header, checked);
		}
		
		printTurpleList(header);
		printlistArray(listArray);
		String result = "";
		int max = listArray[0].size() + 1;
		int jj = 0;
		try {
			while (jj < max) {
				for (int i = 0; i < listArray.length; i++) {
					result += listArray[i].get(jj);
				}
				jj++;
			}

		} catch (Exception e) {
		
		}
		
		return result;
	}
	
	
	private static String outputCipher(ArrayList<Character>[] listArray, String cipherBlock, String cipher, int minIndex,ArrayList<Turple> header, HashSet<Integer> checked ){
		//when everything checked
		if( minIndex == -1 ){
			return cipher;
		}
		ArrayList<Character> charList = listArray[minIndex];
		int i = 0;
		//loop until the remaining character on that column is less than 5
		while((charList.size()-i)/5 > 0){
			while(cipherBlock.length()<5){
				cipherBlock += charList.get(i);
				i++;
			}
			cipher += cipherBlock + " ";
			cipherBlock = "";
		}

		//get the remaining characters
		for( ;i <charList.size() ; i++){
			cipherBlock += charList.get(i);
		}

		if(cipherBlock.length() == 5){
			cipherBlock = "";
			cipher += cipherBlock + " ";
		}
		//carry the unfinished cipher block to next round
		return outputCipher(listArray, cipherBlock, cipher,smallestIndex(header, checked),header,checked);
	}

	//get current min index.
	private static int smallestIndex(ArrayList<Turple> header, HashSet<Integer> checked) {
		int minValue = 100;
		int minIndex = -1;
		for (Turple t : header) {
			if(t.alphIndex<minValue && !checked.contains(t.arrayIndex)){
				minValue = t.alphIndex;
				minIndex = t.arrayIndex;

			}
		}
		checked.add(minIndex);
		return minIndex;
	}

	private static void printlistArray(ArrayList<Character>[] listArray) {
		int max = listArray[0].size() + 1;
		int j = 0;
		String line = "";
		try {
			while (j < max) {
				for (int i = 0; i < listArray.length; i++) {
					line += listArray[i].get(j) + "  ";
				}
				j++;
				System.out.println(line);
				line = "";
			}

		} catch (Exception e) {
			System.out.println(line);
		}
	}

	private static void printTurpleList(ArrayList<Turple> turpleLis) {
		String characters = "";
		String alphIndex = "";
		String arrayIndex = "";
		for (Turple t : turpleLis) {
			characters += t.getCharacter() + "  ";
			alphIndex += String.format("%02d", t.getAlphIndex()) + " ";
			arrayIndex += String.format("%02d", t.getArrayIndex()) + " ";
		}
		System.out.println(characters);
		System.out.println(alphIndex);
		System.out.println(arrayIndex);
	}

	private static class Turple {
		private int arrayIndex;
		private int alphIndex;
		private char character;

		public Turple(int arrayIndex, int alphIndex, char character) {
			this.arrayIndex = arrayIndex;
			this.alphIndex = alphIndex;
			this.character = character;
		}

		public int getArrayIndex() {
			return arrayIndex;
		}

		public int getAlphIndex() {
			return alphIndex;
		}

		public char getCharacter() {
			return character;
		}

	}

}
