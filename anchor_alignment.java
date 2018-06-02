import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.*;


public class anchor_alignment {
	private static int K = 0;
	private static int p1 = 0;
	private static int p2 = 0;
	private static int g = 0;
	private static int s = 0; 
	private static int minNum = -999999;
	public static void main(String[] args) {
		 File fileToRead = new File(args[0]);
		 try(Scanner fileScanner = new Scanner(fileToRead)){
			 String[] int5 = fileScanner.nextLine().split(" ");
			 String seq1 = fileScanner.nextLine();
			 String seq2 = fileScanner.nextLine();
		
			 K = Integer.parseInt(int5[0]);
			 p1 = Integer.parseInt(int5[1]);
			 p2 = Integer.parseInt(int5[2]);
			 g = Integer.parseInt(int5[3]);
			 s = Integer.parseInt(int5[4]);
			  
			 ArrayList<int[]> anchPairs = new ArrayList<int[]>();
			 if(K > 0) {
				 for(int i = 0; i < K; i ++) {
					 String[] temp = fileScanner.nextLine().split(" ");
					 int[] numArr = new int[temp.length];
					 for(int j = 0; j < temp.length; j ++){
						 numArr[j] = Integer.parseInt(temp[j]) -1;
					 }
					 anchPairs.add(numArr);
				 }
			 }
			 
			 ArrayList<String> subSeq1 = new ArrayList<String>();
			 ArrayList<String> subSeq2 = new ArrayList<String>();
			 ArrayList<Character> anchChars = new ArrayList<Character>();
			 ArrayList<Character> anchChars2 = new ArrayList<Character>();
			 
			 for(int i = 0; i < anchPairs.size(); i ++) {
				 int[] currPair = anchPairs.get(i);
				 int subSeq1Pos = currPair[0];
				 int subSeq2Pos = currPair[1];
				 
				 String currSubSeq1 = seq1.substring(0, subSeq1Pos);
				 String currSubSeq2 = seq2.substring(0, subSeq2Pos);
				 Character currAnchChar1 = seq1.charAt(subSeq1Pos);
				 Character currAnchChar2 = seq2.charAt(subSeq2Pos);
				 
				 subSeq1.add(currSubSeq1);
				 subSeq2.add(currSubSeq2);
				 anchChars.add(currAnchChar1);
				 anchChars2.add(currAnchChar2);
				 
				 seq1 = seq1.substring(subSeq1Pos + 1);
				 seq2 = seq2.substring(subSeq2Pos + 1); 
				 
				 for(int j = i + 1; j < anchPairs.size(); j++) {
					anchPairs.get(j)[0] -= (subSeq1Pos + 1);
					anchPairs.get(j)[1] -= (subSeq2Pos + 1);
				 }
			 }
			 
			 if(!seq1.equals("")) {
				 subSeq1.add(seq1);
			 }
			 if(!seq2.equals("")) {
				 subSeq2.add(seq2);
			 }
			 
			 ArrayList<String> newSubSeq1 = new ArrayList<String>();
			 ArrayList<String> newSubSeq2 = new ArrayList<String>();
			 for(int i = 0; i < subSeq1.size() && i < subSeq2.size(); i ++){
				 String[] newSubSeqs = seqAlign(subSeq1.get(i), subSeq2.get(i));
				 newSubSeq1.add(newSubSeqs[0]);
				 newSubSeq2.add(newSubSeqs[1]);
			 }
			
			 /**
			  * REPLACE THIS COMBINING STRING WITH NEW SUB SEQ
			  */
			 String a = "";
			 for(int i = 0; i < newSubSeq1.size(); i ++) {
				 a += newSubSeq1.get(i);
				 if (i < anchChars.size()) {
					 a += anchChars.get(i);
				 }
			 }

			 String b = "";
			 for(int i = 0; i < newSubSeq2.size(); i ++) {
				 b += newSubSeq2.get(i);
				 if (i < anchChars2.size()) {
					 b += anchChars2.get(i);
				 }
			 }
			 
			 System.out.println(a);
			 System.out.println(b);
			 
			 /*
			  * FOR TESTING PURPOSES
			 String testSeq1 = null;
			 String testSeq2 = null;
			 File fileToReadTest = new File(args[1]);
			 Scanner testFileScanner = new Scanner(fileToReadTest);
			 testSeq1 = testFileScanner.nextLine();
			 testSeq2 = testFileScanner.nextLine();
			 
			 System.out.println();
			 System.out.println(testSeq1);
			 System.out.println(testSeq2);
			 
			 System.out.println();
			 System.out.println(testSeq1.equals(a));
			 System.out.println(testSeq2.equals(b));
			 testFileScanner.close(); */ 
			 
		  } catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		  }
	}
	
	public static String[] seqAlign(String seq1, String seq2) {
		matrixBox[][] matrix = new matrixBox[seq1.length()+1][seq2.length()+1];
		
		for(int i = 0; i < matrix.length; i ++){
			for(int j = 0; j < matrix[i].length; j++){
				matrix[i][j] = new matrixBox(j, i);
			}
		}
			
		matrix[0][0].M.num = 0;
		// REMEMBER TO DO J THEN I TO AVOID CONFUSION
		for(int i = 0; i < 1; i ++) {
			for(int j = 0; j < matrix[i].length; j ++) {
				matrix[i][j].Iy.num = -g-s*j;
				if(j != 0) {
					matrix[i][j].Iy.parent = matrix[i][j-1].Iy;
				}
			}
		}
		
		for(int i = 0; i < matrix.length; i ++) {
			for(int j = 0; j < 1; j ++) {
				matrix[i][j].Ix.num = -g-s*i;
				if(i != 0) {
					matrix[i][j].Ix.parent = matrix[i-1][j].Ix;
				}
			}
		}
		
		for(int i = 1; i < matrix.length; i ++) {
			for(int j = 1; j < matrix[i].length; j ++) {
				int score = 0;
				if(seq1.charAt(i-1) == seq2.charAt(j-1)) {
					score = p1;
				} else {
					score = -p2;
				}
				
				int MM = matrix[i-1][j-1].M.num + score;
				int MIx = matrix[i-1][j-1].Ix.num + score;
				int MIy = matrix[i-1][j-1].Iy.num + score;
				
				matrix[i][j].M.num = Math.max(Math.max(MM, MIx), MIy);
				if(matrix[i][j].M.num != minNum) {
					if(matrix[i][j].M.num == MM) {
						matrix[i][j].M.parent = matrix[i-1][j-1].M;
					} else if (matrix[i][j].M.num == MIx) {
						matrix[i][j].M.parent = matrix[i-1][j-1].Ix;
					} else if (matrix[i][j].M.num == MIy) {
						matrix[i][j].M.parent = matrix[i-1][j-1].Iy;
					}
				}
				int IxM = minNum;
				int IxIx = minNum;
				if(matrix[i-1][j].M.num != minNum) {
					IxM = matrix[i-1][j].M.num - g - s;
				}
				if(matrix[i-1][j].M.num != minNum) {
					IxIx = matrix[i-1][j].Ix.num - s;
				}
				
				matrix[i][j].Ix.num = Math.max(IxM, IxIx);
				if(matrix[i][j].Ix.num != minNum) {
					if(matrix[i][j].Ix.num == IxM) {
						matrix[i][j].Ix.parent = matrix[i-1][j].M;
					} else if (matrix[i][j].Ix.num == IxIx) {
						matrix[i][j].Ix.parent = matrix[i-1][j].Ix;
					} 
				}
				
				int IyM = minNum;
				int IyIy = minNum;
				if(matrix[i][j-1].M.num != minNum) {
					IyM = matrix[i][j-1].M.num - g - s;
				}
				if(matrix[i][j-1].Iy.num != minNum) {
					IyIy = matrix[i][j-1].Iy.num - s;
				}
				
				matrix[i][j].Iy.num = Math.max(IyM, IyIy);
				if(matrix[i][j].Iy.num != minNum) {
					if(matrix[i][j].Iy.num == IyM) {
						matrix[i][j].Iy.parent = matrix[i][j-1].M;
					} else if (matrix[i][j].Iy.num == IyIy) {
						matrix[i][j].Iy.parent = matrix[i][j-1].Iy;
					} 
				}
			}
		}
		
		StringBuilder str1 = new StringBuilder();
		StringBuilder str2 = new StringBuilder();
		matrixBox lastBox = matrix[matrix.length -1][matrix[matrix.length-1].length -1];
		IntegerObj maxNumObj = lastBox.getMax();
		// Use indexes to indicate character?
		while(maxNumObj.parent != null) {
			int maxNumObjIndexSum = maxNumObj.indexI + maxNumObj.indexJ;
			int maxNumObjParentsIndexSum = maxNumObj.parent.indexI + maxNumObj.parent.indexJ;
			
			
			if(Math.abs(maxNumObjIndexSum - maxNumObjParentsIndexSum) == 2) {
				str1.append(seq1.charAt(maxNumObj.indexI - 1));
				str2.append(seq2.charAt(maxNumObj.indexJ - 1));
			} else if(Math.abs(maxNumObj.indexI - maxNumObj.parent.indexI) == 1 && Math.abs(maxNumObj.indexJ - maxNumObj.parent.indexJ) == 0) {
				str1.append(seq1.charAt(maxNumObj.indexI - 1));
				str2.append('_');
			} else if(Math.abs(maxNumObj.indexI - maxNumObj.parent.indexI) == 0 && Math.abs(maxNumObj.indexJ - maxNumObj.parent.indexJ) == 1) {
				str1.append('_');
				str2.append(seq2.charAt(maxNumObj.indexJ - 1));
			}
			
			maxNumObj = maxNumObj.parent;
		} 
		String[] returnSeq = new String[2];
		returnSeq[0] = str1.reverse().toString();
		returnSeq[1] = str2.reverse().toString();
		
		return returnSeq;
	}
	
	
}
	
