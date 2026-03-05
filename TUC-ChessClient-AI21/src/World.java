import java.util.ArrayList;
import java.util.Random;

public class World
{
	private String[][] board = null;
	private int rows = 7;
	private int columns = 5;
	private int myColor = 0;
	private ArrayList<String> availableMoves = null;
	private int rookBlocks = 3;		// rook can move towards <rookBlocks> blocks in any vertical or horizontal direction
	private int nTurns = 0;
	private long nBranches = 0;
	private int noPrize = 9;
	private int depth = 4;
	private int depthW = 2;
	private String lastMove = "";
	int scoreB=0;
	int scoreW=0;
	int check=0;

	
	public World()
	{
		board = new String[rows][columns];
		hashCode();
		/* represent the board
		
		BP|BR|BK|BR|BP
		BP|BP|BP|BP|BP
		--|--|--|--|--
		P |P |P |P |P 
		--|--|--|--|--
		WP|WP|WP|WP|WP
		WP|WR|WK|WR|WP
		*/
		
		// initialization of the board
		for(int i=0; i<rows; i++)
			for(int j=0; j<columns; j++)
				board[i][j] = " ";
		
		// setting the black player's chess parts
		
		// black pawns
		for(int j=0; j<columns; j++)
			board[1][j] = "BP";
		
		board[0][0] = "BP";
		board[0][columns-1] = "BP";
		
		// black rooks
		board[0][1] = "BR";
		board[0][columns-2] = "BR";
		
		// black king
		board[0][columns/2] = "BK";
		
		// setting the white player's chess parts
		
		// white pawns
		for(int j=0; j<columns; j++)
			board[rows-2][j] = "WP";
		
		board[rows-1][0] = "WP";
		board[rows-1][columns-1] = "WP";
		
		// white rooks
		board[rows-1][1] = "WR";
		board[rows-1][columns-2] = "WR";
		
		// white king
		board[rows-1][columns/2] = "WK";
		
		// setting the prizes
		for(int j=0; j<columns; j++)
			board[rows/2][j] = "P";
		
		availableMoves = new ArrayList<String>();
	}
	
	public void setMyColor(int myColor,int scoreB,int scoreW)
	{
		this.myColor = myColor;
		this.scoreB=scoreB;
		this.scoreW=scoreW;
	}
	
	public String selectAction()
	{	
		availableMoves = new ArrayList<String>();
		
		if(myColor == 0)
			// I am the white player
			this.whiteMoves1();
		else
			// I am the black player
			this.blackMoves1();

		nTurns++;
		nBranches += availableMoves.size();
		
//		TreeNode Root = new TreeNode();
		

//		Root.setRoot(board);
//		Root.setColor(1);
		
//		Root.selectAction();

		if(myColor == 0) {
			return this.selectRandomAction();
//			System.out.println(MiniMax3(board , depthW , Integer.MIN_VALUE , Integer.MAX_VALUE ,  true,myColor,availableMoves.get(0)));	
//			return lastMove;
		}else {	
			System.out.println(MiniMax(board , depth , Integer.MIN_VALUE , Integer.MAX_VALUE ,  true,myColor,availableMoves.get(0)));	
			return lastMove;
//			return bestChild(Root);
//			return this.selectRandomAction();
		}	
		
	}

	private double MiniMax(String [][] myBoard ,int MYdepth , double a ,double b , boolean maxPlayer,int color,String initialMove) {
		if(nTurns == 1) { //make  random first  move
			lastMove = this.selectRandomAction();
			return 0.0; 
		}		
		double p=eval(myBoard,MYdepth);
		
		if (MYdepth == 0 || check ==1) {
			check=0;
			return p;		
		}
		
		ArrayList<String> moves = new ArrayList<String>();
		
		if(color==0) {
			moves = whiteMoves(myBoard);
			color=1;
		}else {
			moves = blackMoves(myBoard);
			color=0;
		}	
		
		if(MYdepth != depth) {
			nTurns++;
			nBranches += moves.size(); // calculate branching factor
		}
		
		String m  = "";		
		if (maxPlayer) {			
			double maxVal = Integer.MIN_VALUE;
			for (int i = 0; i < moves.size(); i++) {				
				m = moves.get(i);
				//Save pieces before move
				int x1 = Character.getNumericValue(m.charAt(0));
				int y1 = Character.getNumericValue(m.charAt(1));
				int x2 = Character.getNumericValue(m.charAt(2));		
				int y2 = Character.getNumericValue(m.charAt(3));				
				String chesspart1 = myBoard[x1][y1];
				String chesspart2 = myBoard[x2][y2];
				//Make move on board
				myBoard=makeMyMove(myBoard,m);

				//MiniMax call
				double eval;
				if(MYdepth == depth) {
					eval = MiniMax(myBoard,MYdepth - 1, a, b, false,color,m);
				}else {
					eval = MiniMax(myBoard,MYdepth - 1, a, b, false,color,initialMove);
				}
					
				
				//Undo move on board
				myBoard[x1][y1] = chesspart1;
				myBoard[x2][y2] = chesspart2;
				//Evaluate position
							
				maxVal= Math.max(maxVal, eval);
				if(MYdepth == depth) {
					if(eval > a)lastMove = m;
				}else {
					if(eval > a)lastMove = initialMove;
				}
				a = Math.max(a, maxVal);
				if(a>=b) {
					break;
				}
			}
			return maxVal;
		}else {
			double minVal = Integer.MAX_VALUE;
			for (int i = 0; i < moves.size(); i++) {
				m = moves.get(i);
				//Save pieces before move
				int x1 = Character.getNumericValue(m.charAt(0));
				int y1 = Character.getNumericValue(m.charAt(1));
				int x2 = Character.getNumericValue(m.charAt(2));		
				int y2 = Character.getNumericValue(m.charAt(3));				
				String chesspart1 = myBoard[x1][y1];
				String chesspart2 = myBoard[x2][y2];
				//Make move on board
				myBoard=makeMyMove(myBoard,m);

				//MiniMax call
				double eval;
				if(MYdepth == depth) {
					eval = MiniMax(myBoard,MYdepth - 1, a, b, true,color,m);
				}else {
					eval = MiniMax(myBoard,MYdepth - 1, a, b, true,color,initialMove);
				}
				
				//Undo move on board
				myBoard[x1][y1] = chesspart1;
				myBoard[x2][y2] = chesspart2;
		
				minVal= Math.min(minVal, eval);
				b = Math.min(b, minVal);
				if (b<=a) {
					break;
				}
			}
			return minVal;
		}
	}
	
	private double MiniMax3(String [][] myBoard ,int MYdepth , double a ,double b , boolean maxPlayer,int color,String initialMove) {
		if(nTurns == 1) { //make  random first  move
			lastMove = this.selectRandomAction();
			return 0.0; 
		}
		double p=eval(myBoard,MYdepth);
		if (MYdepth == 0 || check ==1) {
			check=0;
			return p;		
		}
		
		
		ArrayList<String> moves = new ArrayList<String>();
		
		if(color==0) {
			moves = whiteMoves(myBoard);
			color=1;
		}else {
			moves = blackMoves(myBoard);
			color=0;
		}
		
		if(MYdepth != depth) {
			nTurns++;
			nBranches += moves.size(); // calculate branching factor
		}
		
		String m  = "";		
		if (maxPlayer) {			
			double maxVal = Integer.MIN_VALUE;
			for (int i = 0; i < moves.size(); i++) {				
				m = moves.get(i);
				//Save pieces before move
				int x1 = Character.getNumericValue(m.charAt(0));
				int y1 = Character.getNumericValue(m.charAt(1));
				int x2 = Character.getNumericValue(m.charAt(2));		
				int y2 = Character.getNumericValue(m.charAt(3));				
				String chesspart1 = myBoard[x1][y1];
				String chesspart2 = myBoard[x2][y2];
				//Make move on board
				myBoard=makeMyMove(myBoard,m);
				
				//MiniMax call
				double eval;
				if(MYdepth == depthW) {
					eval = MiniMax3(myBoard,MYdepth - 1, a, b, false,color,m);
				}else {
					eval = MiniMax3(myBoard,MYdepth - 1, a, b, false,color,initialMove);
				}
					
				
				//Undo move on board
				myBoard[x1][y1] = chesspart1;
				myBoard[x2][y2] = chesspart2;
				//Evaluate position
							
				maxVal= Math.max(maxVal, eval);
				if(MYdepth == depthW) {
					if(eval > a)lastMove = m;
				}else {
					if(eval > a)lastMove = initialMove;
				}
				a = Math.max(a, maxVal);
				if(a>=b) {
					break;
				}
			}
			return maxVal;
		}else {
			double minVal = Integer.MAX_VALUE;
			for (int i = 0; i < moves.size(); i++) {
				m = moves.get(i);
				//Save pieces before move
				int x1 = Character.getNumericValue(m.charAt(0));
				int y1 = Character.getNumericValue(m.charAt(1));
				int x2 = Character.getNumericValue(m.charAt(2));		
				int y2 = Character.getNumericValue(m.charAt(3));				
				String chesspart1 = myBoard[x1][y1];
				String chesspart2 = myBoard[x2][y2];
				//Make move on board
				myBoard=makeMyMove(myBoard,m);

				//MiniMax call
				double eval;
				if(MYdepth == depthW) {
					eval = MiniMax3(myBoard,MYdepth - 1, a, b, true,color,m);
				}else {
					eval = MiniMax3(myBoard,MYdepth - 1, a, b, true,color,initialMove);
				}
				
				//Undo move on board
				myBoard[x1][y1] = chesspart1;
				myBoard[x2][y2] = chesspart2;
		
				minVal= Math.min(minVal, eval);
				b = Math.min(b, minVal);
				if (b<=a) {
					break;
				}
			}
			return minVal;
		}
	}
	
	public ArrayList<String> whiteMoves(String[][] board)
	{
		String firstLetter = "";
		String secondLetter = "";
		String move = "";
		ArrayList<String> moves = new ArrayList<String>();	
		
		for(int i=0; i<rows; i++)
		{
			for(int j=0; j<columns; j++)
			{
				firstLetter = Character.toString(board[i][j].charAt(0));
				
				// if it there is not a white chess part in this position then keep on searching
				if(firstLetter.equals("B") || firstLetter.equals(" ") || firstLetter.equals("P"))
					continue;
				
				// check the kind of the white chess part
				secondLetter = Character.toString(board[i][j].charAt(1));
				
				if(secondLetter.equals("P"))	// it is a pawn
				{
					
					// check if it can move one vertical position ahead
					firstLetter = Character.toString(board[i-1][j].charAt(0));
					
					if(firstLetter.equals(" ") || firstLetter.equals("P"))
					{
						move = Integer.toString(i) + Integer.toString(j) + 
							   Integer.toString(i-1) + Integer.toString(j);
						
						moves.add(move);
					}
					
					// check if it can move crosswise to the left
					if(j!=0 && i!=0)
					{
						firstLetter = Character.toString(board[i-1][j-1].charAt(0));						
						if(!(firstLetter.equals("W") || firstLetter.equals(" ") || firstLetter.equals("P"))) {
							move = Integer.toString(i) + Integer.toString(j) + 
									   Integer.toString(i-1) + Integer.toString(j-1);
								
							moves.add(move);
						}											
					}
					
					// check if it can move crosswise to the right
					if(j!=columns-1 && i!=0)
					{
						firstLetter = Character.toString(board[i-1][j+1].charAt(0));
						if(!(firstLetter.equals("W") || firstLetter.equals(" ") || firstLetter.equals("P"))) {
							
							move = Integer.toString(i) + Integer.toString(j) + 
									   Integer.toString(i-1) + Integer.toString(j+1);							
							moves.add(move);
						}
					}
				}
				else if(secondLetter.equals("R"))	// it is a rook
				{
					// check if it can move upwards
					for(int k=0; k<rookBlocks; k++)
					{
						if((i-(k+1)) < 0)
							break;
						
						firstLetter = Character.toString(board[i-(k+1)][j].charAt(0));
						
						if(firstLetter.equals("W"))
							break;
						
						move = Integer.toString(i) + Integer.toString(j) + 
							   Integer.toString(i-(k+1)) + Integer.toString(j);
						
						moves.add(move);
						
						// prevent detouring a chesspart to attack the other
						if(firstLetter.equals("B") || firstLetter.equals("P"))
							break;
					}
					
					// check if it can move downwards
					for(int k=0; k<rookBlocks; k++)
					{
						if((i+(k+1)) == rows)
							break;
						
						firstLetter = Character.toString(board[i+(k+1)][j].charAt(0));
						
						if(firstLetter.equals("W"))
							break;
						
						move = Integer.toString(i) + Integer.toString(j) + 
							   Integer.toString(i+(k+1)) + Integer.toString(j);
						
						moves.add(move);
						
						// prevent detouring a chesspart to attack the other
						if(firstLetter.equals("B") || firstLetter.equals("P"))
							break;
					}
					
					// check if it can move on the left
					for(int k=0; k<rookBlocks; k++)
					{
						if((j-(k+1)) < 0)
							break;
						
						firstLetter = Character.toString(board[i][j-(k+1)].charAt(0));
						
						if(firstLetter.equals("W"))
							break;
						
						move = Integer.toString(i) + Integer.toString(j) + 
							   Integer.toString(i) + Integer.toString(j-(k+1));
						
						moves.add(move);
						
						// prevent detouring a chesspart to attack the other
						if(firstLetter.equals("B") || firstLetter.equals("P"))
							break;
					}
					
					// check of it can move on the right
					for(int k=0; k<rookBlocks; k++)
					{
						if((j+(k+1)) == columns)
							break;
						
						firstLetter = Character.toString(board[i][j+(k+1)].charAt(0));
						
						if(firstLetter.equals("W"))
							break;
						
						move = Integer.toString(i) + Integer.toString(j) + 
							   Integer.toString(i) + Integer.toString(j+(k+1));
						
						moves.add(move);
						
						// prevent detouring a chesspart to attack the other
						if(firstLetter.equals("B") || firstLetter.equals("P"))
							break;
					}
				}
				else // it is the king
				{
					// check if it can move upwards
					if((i-1) >= 0)
					{
						firstLetter = Character.toString(board[i-1][j].charAt(0));
						
						if(!firstLetter.equals("W"))
						{
							move = Integer.toString(i) + Integer.toString(j) + 
								   Integer.toString(i-1) + Integer.toString(j);
								
							moves.add(move);	
						}
					}
					
					// check if it can move downwards
					if((i+1) < rows)
					{
						firstLetter = Character.toString(board[i+1][j].charAt(0));
						
						if(!firstLetter.equals("W"))
						{
							move = Integer.toString(i) + Integer.toString(j) + 
								   Integer.toString(i+1) + Integer.toString(j);
								
							moves.add(move);	
						}
					}
					
					// check if it can move on the left
					if((j-1) >= 0)
					{
						firstLetter = Character.toString(board[i][j-1].charAt(0));
						
						if(!firstLetter.equals("W"))
						{
							move = Integer.toString(i) + Integer.toString(j) + 
								   Integer.toString(i) + Integer.toString(j-1);
								
							moves.add(move);	
						}
					}
					
					// check if it can move on the right
					if((j+1) < columns)
					{
						firstLetter = Character.toString(board[i][j+1].charAt(0));
						
						if(!firstLetter.equals("W"))
						{
							move = Integer.toString(i) + Integer.toString(j) + 
								   Integer.toString(i) + Integer.toString(j+1);
								
							moves.add(move);	
						}
					}
				}			
			}	
		}
		return moves;
	}
	
	
	public ArrayList<String> blackMoves(String[][] board)
	{
		String firstLetter = "";
		String secondLetter = "";
		String move = "";
		ArrayList<String> moves = new ArrayList<String>();
		
		
		for(int i=0; i<rows; i++)
		{
			for(int j=0; j<columns; j++)
			{
				firstLetter = Character.toString(board[i][j].charAt(0));
				
				// if it there is not a black chess part in this position then keep on searching
				if(firstLetter.equals("W") || firstLetter.equals(" ") || firstLetter.equals("P"))
					continue;
				
				// check the kind of the white chess part
				secondLetter = Character.toString(board[i][j].charAt(1));
				
				if(secondLetter.equals("P"))	// it is a pawn
				{
					
					// check if it can move one vertical position ahead
					firstLetter = Character.toString(board[i+1][j].charAt(0));
					
					if(firstLetter.equals(" ") || firstLetter.equals("P"))
					{
						move = Integer.toString(i) + Integer.toString(j) + 
							   Integer.toString(i+1) + Integer.toString(j);
						
						moves.add(move);
					}
					
					// check if it can move crosswise to the left
					if(j!=0 && i!=rows-1)
					{
						firstLetter = Character.toString(board[i+1][j-1].charAt(0));
						
						if(!(firstLetter.equals("B") || firstLetter.equals(" ") || firstLetter.equals("P"))) {
							move = Integer.toString(i) + Integer.toString(j) + 
									   Integer.toString(i+1) + Integer.toString(j-1);
								
							moves.add(move);
						}																	
					}
					
					// check if it can move crosswise to the right
					if(j!=columns-1 && i!=rows-1)
					{
						firstLetter = Character.toString(board[i+1][j+1].charAt(0));
						
						if(!(firstLetter.equals("B") || firstLetter.equals(" ") || firstLetter.equals("P"))) {
							move = Integer.toString(i) + Integer.toString(j) + 
									   Integer.toString(i+1) + Integer.toString(j+1);
								
							moves.add(move);
						}
							
						
						
					}
				}
				else if(secondLetter.equals("R"))	// it is a rook
				{
					// check if it can move upwards
					for(int k=0; k<rookBlocks; k++)
					{
						if((i-(k+1)) < 0)
							break;
						
						firstLetter = Character.toString(board[i-(k+1)][j].charAt(0));
						
						if(firstLetter.equals("B"))
							break;
						
						move = Integer.toString(i) + Integer.toString(j) + 
							   Integer.toString(i-(k+1)) + Integer.toString(j);
						
						moves.add(move);
						
						// prevent detouring a chesspart to attack the other
						if(firstLetter.equals("W") || firstLetter.equals("P"))
							break;
					}
					
					// check if it can move downwards
					for(int k=0; k<rookBlocks; k++)
					{
						if((i+(k+1)) == rows)
							break;
						
						firstLetter = Character.toString(board[i+(k+1)][j].charAt(0));
						
						if(firstLetter.equals("B"))
							break;
						
						move = Integer.toString(i) + Integer.toString(j) + 
							   Integer.toString(i+(k+1)) + Integer.toString(j);
						
						moves.add(move);
						
						// prevent detouring a chesspart to attack the other
						if(firstLetter.equals("W") || firstLetter.equals("P"))
							break;
					}
					
					// check if it can move on the left
					for(int k=0; k<rookBlocks; k++)
					{
						if((j-(k+1)) < 0)
							break;
						
						firstLetter = Character.toString(board[i][j-(k+1)].charAt(0));
						
						if(firstLetter.equals("B"))
							break;
						
						move = Integer.toString(i) + Integer.toString(j) + 
							   Integer.toString(i) + Integer.toString(j-(k+1));
						
						moves.add(move);
						
						// prevent detouring a chesspart to attack the other
						if(firstLetter.equals("W") || firstLetter.equals("P"))
							break;
					}
					
					// check of it can move on the right
					for(int k=0; k<rookBlocks; k++)
					{
						if((j+(k+1)) == columns)
							break;
						
						firstLetter = Character.toString(board[i][j+(k+1)].charAt(0));
						
						if(firstLetter.equals("B"))
							break;
						
						move = Integer.toString(i) + Integer.toString(j) + 
							   Integer.toString(i) + Integer.toString(j+(k+1));
						
						moves.add(move);
						
						// prevent detouring a chesspart to attack the other
						if(firstLetter.equals("W") || firstLetter.equals("P"))
							break;
					}
				}
				else // it is the king
				{
					// check if it can move upwards
					if((i-1) >= 0)
					{
						firstLetter = Character.toString(board[i-1][j].charAt(0));
						
						if(!firstLetter.equals("B"))
						{
							move = Integer.toString(i) + Integer.toString(j) + 
								   Integer.toString(i-1) + Integer.toString(j);
								
							moves.add(move);	
						}
					}
					
					// check if it can move downwards
					if((i+1) < rows)
					{
						firstLetter = Character.toString(board[i+1][j].charAt(0));
						
						if(!firstLetter.equals("B"))
						{
							move = Integer.toString(i) + Integer.toString(j) + 
								   Integer.toString(i+1) + Integer.toString(j);
								
							moves.add(move);	
						}
					}
					
					// check if it can move on the left
					if((j-1) >= 0)
					{
						firstLetter = Character.toString(board[i][j-1].charAt(0));
						
						if(!firstLetter.equals("B"))
						{
							move = Integer.toString(i) + Integer.toString(j) + 
								   Integer.toString(i) + Integer.toString(j-1);
								
							moves.add(move);	
						}
					}
					
					// check if it can move on the right
					if((j+1) < columns)
					{
						firstLetter = Character.toString(board[i][j+1].charAt(0));
						
						if(!firstLetter.equals("B"))
						{
							move = Integer.toString(i) + Integer.toString(j) + 
								   Integer.toString(i) + Integer.toString(j+1);
								
							moves.add(move);	
						}
					}
				}			
			}	
		}
		return moves;
	}
	
		
	public String [][] makeMyMove(String [][] state , String move){
		
		
		String boardStateTemp[][] = new String [7][5];
		
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < columns; col++) {
				boardStateTemp[row][col] = state[row][col];
			}
		}
				
		int x1 = Character.getNumericValue(move.charAt(0));
		int y1 = Character.getNumericValue(move.charAt(1));
		int x2 = Character.getNumericValue(move.charAt(2));		
		int y2 = Character.getNumericValue(move.charAt(3));
		String chesspart = Character.toString(boardStateTemp[x1][y1].charAt(1));
		String chesspart2 = Character.toString(boardStateTemp[x2][y2].charAt(0));
		
		
		
		boolean pawnLastRow = false;
		
		// check if it is a move that has made a move to the last line
		if(chesspart.equals("P"))
			if( (x1==rows-2 && x2==rows-1) || (x1==1 && x2==0) )
			{
				boardStateTemp[x2][y2] = " ";	// in a case an opponent's chess part has just been captured
				boardStateTemp[x1][y1] = " ";
				pawnLastRow = true;
			}
		
		
		if(!pawnLastRow)
		{
			boardStateTemp [x2][y2] = boardStateTemp [x1][y1];
			boardStateTemp [x1][y1] = " ";
		}
		
		
		return boardStateTemp;
	}
	

	
	private double eval(String [][] state , int MyDepth) {		
		
		double WhiteValue = 0 ,BlackValue = 0,k=0,n=0;
		
		String boardStateTemp[][] = new String [7][5];
		
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < columns; col++) {
				boardStateTemp[row][col] = state[row][col];
			}
		}
		
		for (int row = 0; row < rows; row++) { // traverse board;
            for (int col = 0; col < columns; col++) {
            	if (boardStateTemp[row][col].equals("WP")) {
            		WhiteValue ++;
            		if(row > 0) {
            			if(col > 0) {
            				if (boardStateTemp[row-1][col-1] == "WP")WhiteValue += 0.2; //Protected by pawn
            			}
            			if(col < 4) {
            				if (boardStateTemp[row-1][col+1] == "WP")WhiteValue += 0.2; //Protected by pawn
            			}              		
            		}
            		for(int x=row-1 ; x>=0 ; x--) {
            			if (boardStateTemp[x][col]=="WR")WhiteValue += 0.2; //Protected by Rook           			
            		}
            		for(int y=col-1 ; y>=0 ; y--) {
        				if (boardStateTemp[row][y]=="WR")WhiteValue += 0.2; //Protected by Rook
        			}
            		for(int x=row+1 ; x<rows ; x++) {
            			if (boardStateTemp[x][col]=="WR")WhiteValue += 0.2; //Protected by Rook           			
            		}
            		for(int y=col+1 ; y<columns ; y++) {
        				if (boardStateTemp[row][y]=="WR")WhiteValue += 0.2; //Protected by Rook
        			}
            	}else if (boardStateTemp[row][col].equals("WR")) {
            		WhiteValue += 3;
            		
            	}else if (boardStateTemp[row][col].equals("WK")) {
            		WhiteValue += 8;
            		k++;
            	}else if (boardStateTemp[row][col].equals("BP")) {
            		BlackValue ++;
            		if(row > 0) {
            			if(col > 0) {
            				if (boardStateTemp[row+1][col-1] == "BP")BlackValue += 0.2; //Protected by pawn
            			}
            			if(col < 4) {
            				if (boardStateTemp[row+1][col+1] == "BP")BlackValue += 0.2; //Protected by pawn
            			}              		
            		}
            		for(int x=row-1 ; x>=0 ; x--) {
            			if (boardStateTemp[x][col]=="BR")BlackValue += 0.2; //Protected by Rook           			
            		}
            		for(int y=col-1 ; y>=0 ; y--) {
        				if (boardStateTemp[row][y]=="BR")BlackValue += 0.2; //Protected by Rook
        			}
            		for(int x=row+1 ; x<rows ; x++) {
            			if (boardStateTemp[x][col]=="BR")BlackValue += 0.2; //Protected by Rook           			
            		}
            		for(int y=col+1 ; y<columns ; y++) {
        				if (boardStateTemp[row][y]=="BR")BlackValue += 0.2; //Protected by Rook
        			}
            	}else if (boardStateTemp[row][col].equals("BR")) {
            		BlackValue += 3;
            	}else if (boardStateTemp[row][col].equals("BK")) {
            		BlackValue += 8;
            		n++;
            	}            	
            }
		}	
		if(k!=1 || n!=1)check=1;
		 

		if (myColor == 0) {
			if(k!=1) return -999;
			if(n!=1) {
				if(scoreW + 8 > scoreB) {
					return 999 ;
				}else return - 900 ;
			}
			
			return WhiteValue - BlackValue;
		}else {
			if(n!=1) return -999;
			if(k!=1) {
				if(scoreB + 8 > scoreW) {
					return 999 ;
				}else return - 900 ;
			}
			return BlackValue - WhiteValue;
		}
	}
	
	private String selectRandomAction()
	{		
		Random ran = new Random();
		int x = ran.nextInt(availableMoves.size());
		
		return availableMoves.get(x);
	}
	private void whiteMoves1()
	{
		String firstLetter = "";
		String secondLetter = "";
		String move = "";
				
		for(int i=0; i<rows; i++)
		{
			for(int j=0; j<columns; j++)
			{
				firstLetter = Character.toString(board[i][j].charAt(0));
				
				// if it there is not a white chess part in this position then keep on searching
				if(firstLetter.equals("B") || firstLetter.equals(" ") || firstLetter.equals("P"))
					continue;
				
				// check the kind of the white chess part
				secondLetter = Character.toString(board[i][j].charAt(1));
				
				if(secondLetter.equals("P"))	// it is a pawn
				{
					
					// check if it can move one vertical position ahead
					firstLetter = Character.toString(board[i-1][j].charAt(0));
					
					if(firstLetter.equals(" ") || firstLetter.equals("P"))
					{
						move = Integer.toString(i) + Integer.toString(j) + 
							   Integer.toString(i-1) + Integer.toString(j);
						
						availableMoves.add(move);
					}
					
					// check if it can move crosswise to the left
					if(j!=0 && i!=0)
					{
						firstLetter = Character.toString(board[i-1][j-1].charAt(0));						
						if(!(firstLetter.equals("W") || firstLetter.equals(" ") || firstLetter.equals("P"))) {
							move = Integer.toString(i) + Integer.toString(j) + 
									   Integer.toString(i-1) + Integer.toString(j-1);
								
							availableMoves.add(move);
						}											
					}
					
					// check if it can move crosswise to the right
					if(j!=columns-1 && i!=0)
					{
						firstLetter = Character.toString(board[i-1][j+1].charAt(0));
						if(!(firstLetter.equals("W") || firstLetter.equals(" ") || firstLetter.equals("P"))) {
							
							move = Integer.toString(i) + Integer.toString(j) + 
									   Integer.toString(i-1) + Integer.toString(j+1);							
							availableMoves.add(move);
						}
					}
				}
				else if(secondLetter.equals("R"))	// it is a rook
				{
					// check if it can move upwards
					for(int k=0; k<rookBlocks; k++)
					{
						if((i-(k+1)) < 0)
							break;
						
						firstLetter = Character.toString(board[i-(k+1)][j].charAt(0));
						
						if(firstLetter.equals("W"))
							break;
						
						move = Integer.toString(i) + Integer.toString(j) + 
							   Integer.toString(i-(k+1)) + Integer.toString(j);
						
						availableMoves.add(move);
						
						// prevent detouring a chesspart to attack the other
						if(firstLetter.equals("B") || firstLetter.equals("P"))
							break;
					}
					
					// check if it can move downwards
					for(int k=0; k<rookBlocks; k++)
					{
						if((i+(k+1)) == rows)
							break;
						
						firstLetter = Character.toString(board[i+(k+1)][j].charAt(0));
						
						if(firstLetter.equals("W"))
							break;
						
						move = Integer.toString(i) + Integer.toString(j) + 
							   Integer.toString(i+(k+1)) + Integer.toString(j);
						
						availableMoves.add(move);
						
						// prevent detouring a chesspart to attack the other
						if(firstLetter.equals("B") || firstLetter.equals("P"))
							break;
					}
					
					// check if it can move on the left
					for(int k=0; k<rookBlocks; k++)
					{
						if((j-(k+1)) < 0)
							break;
						
						firstLetter = Character.toString(board[i][j-(k+1)].charAt(0));
						
						if(firstLetter.equals("W"))
							break;
						
						move = Integer.toString(i) + Integer.toString(j) + 
							   Integer.toString(i) + Integer.toString(j-(k+1));
						
						availableMoves.add(move);
						
						// prevent detouring a chesspart to attack the other
						if(firstLetter.equals("B") || firstLetter.equals("P"))
							break;
					}
					
					// check of it can move on the right
					for(int k=0; k<rookBlocks; k++)
					{
						if((j+(k+1)) == columns)
							break;
						
						firstLetter = Character.toString(board[i][j+(k+1)].charAt(0));
						
						if(firstLetter.equals("W"))
							break;
						
						move = Integer.toString(i) + Integer.toString(j) + 
							   Integer.toString(i) + Integer.toString(j+(k+1));
						
						availableMoves.add(move);
						
						// prevent detouring a chesspart to attack the other
						if(firstLetter.equals("B") || firstLetter.equals("P"))
							break;
					}
				}
				else // it is the king
				{
					// check if it can move upwards
					if((i-1) >= 0)
					{
						firstLetter = Character.toString(board[i-1][j].charAt(0));
						
						if(!firstLetter.equals("W"))
						{
							move = Integer.toString(i) + Integer.toString(j) + 
								   Integer.toString(i-1) + Integer.toString(j);
								
							availableMoves.add(move);	
						}
					}
					
					// check if it can move downwards
					if((i+1) < rows)
					{
						firstLetter = Character.toString(board[i+1][j].charAt(0));
						
						if(!firstLetter.equals("W"))
						{
							move = Integer.toString(i) + Integer.toString(j) + 
								   Integer.toString(i+1) + Integer.toString(j);
								
							availableMoves.add(move);	
						}
					}
					
					// check if it can move on the left
					if((j-1) >= 0)
					{
						firstLetter = Character.toString(board[i][j-1].charAt(0));
						
						if(!firstLetter.equals("W"))
						{
							move = Integer.toString(i) + Integer.toString(j) + 
								   Integer.toString(i) + Integer.toString(j-1);
								
							availableMoves.add(move);	
						}
					}
					
					// check if it can move on the right
					if((j+1) < columns)
					{
						firstLetter = Character.toString(board[i][j+1].charAt(0));
						
						if(!firstLetter.equals("W"))
						{
							move = Integer.toString(i) + Integer.toString(j) + 
								   Integer.toString(i) + Integer.toString(j+1);
								
							availableMoves.add(move);	
						}
					}
				}			
			}	
		}
	}
	private void blackMoves1()
	{
		String firstLetter = "";
		String secondLetter = "";
		String move = "";
				
		for(int i=0; i<rows; i++)
		{
			for(int j=0; j<columns; j++)
			{
				firstLetter = Character.toString(board[i][j].charAt(0));
				
				// if it there is not a black chess part in this position then keep on searching
				if(firstLetter.equals("W") || firstLetter.equals(" ") || firstLetter.equals("P"))
					continue;
				
				// check the kind of the white chess part
				secondLetter = Character.toString(board[i][j].charAt(1));
				
				if(secondLetter.equals("P"))	// it is a pawn
				{
					
					// check if it can move one vertical position ahead
					firstLetter = Character.toString(board[i+1][j].charAt(0));
					
					if(firstLetter.equals(" ") || firstLetter.equals("P"))
					{
						move = Integer.toString(i) + Integer.toString(j) + 
							   Integer.toString(i+1) + Integer.toString(j);
						
						availableMoves.add(move);
					}
					
					// check if it can move crosswise to the left
					if(j!=0 && i!=rows-1)
					{
						firstLetter = Character.toString(board[i+1][j-1].charAt(0));
						
						if(!(firstLetter.equals("B") || firstLetter.equals(" ") || firstLetter.equals("P"))) {
							move = Integer.toString(i) + Integer.toString(j) + 
									   Integer.toString(i+1) + Integer.toString(j-1);
								
							availableMoves.add(move);
						}																	
					}
					
					// check if it can move crosswise to the right
					if(j!=columns-1 && i!=rows-1)
					{
						firstLetter = Character.toString(board[i+1][j+1].charAt(0));
						
						if(!(firstLetter.equals("B") || firstLetter.equals(" ") || firstLetter.equals("P"))) {
							move = Integer.toString(i) + Integer.toString(j) + 
									   Integer.toString(i+1) + Integer.toString(j+1);
								
							availableMoves.add(move);
						}
							
						
						
					}
				}
				else if(secondLetter.equals("R"))	// it is a rook
				{
					// check if it can move upwards
					for(int k=0; k<rookBlocks; k++)
					{
						if((i-(k+1)) < 0)
							break;
						
						firstLetter = Character.toString(board[i-(k+1)][j].charAt(0));
						
						if(firstLetter.equals("B"))
							break;
						
						move = Integer.toString(i) + Integer.toString(j) + 
							   Integer.toString(i-(k+1)) + Integer.toString(j);
						
						availableMoves.add(move);
						
						// prevent detouring a chesspart to attack the other
						if(firstLetter.equals("W") || firstLetter.equals("P"))
							break;
					}
					
					// check if it can move downwards
					for(int k=0; k<rookBlocks; k++)
					{
						if((i+(k+1)) == rows)
							break;
						
						firstLetter = Character.toString(board[i+(k+1)][j].charAt(0));
						
						if(firstLetter.equals("B"))
							break;
						
						move = Integer.toString(i) + Integer.toString(j) + 
							   Integer.toString(i+(k+1)) + Integer.toString(j);
						
						availableMoves.add(move);
						
						// prevent detouring a chesspart to attack the other
						if(firstLetter.equals("W") || firstLetter.equals("P"))
							break;
					}
					
					// check if it can move on the left
					for(int k=0; k<rookBlocks; k++)
					{
						if((j-(k+1)) < 0)
							break;
						
						firstLetter = Character.toString(board[i][j-(k+1)].charAt(0));
						
						if(firstLetter.equals("B"))
							break;
						
						move = Integer.toString(i) + Integer.toString(j) + 
							   Integer.toString(i) + Integer.toString(j-(k+1));
						
						availableMoves.add(move);
						
						// prevent detouring a chesspart to attack the other
						if(firstLetter.equals("W") || firstLetter.equals("P"))
							break;
					}
					
					// check of it can move on the right
					for(int k=0; k<rookBlocks; k++)
					{
						if((j+(k+1)) == columns)
							break;
						
						firstLetter = Character.toString(board[i][j+(k+1)].charAt(0));
						
						if(firstLetter.equals("B"))
							break;
						
						move = Integer.toString(i) + Integer.toString(j) + 
							   Integer.toString(i) + Integer.toString(j+(k+1));
						
						availableMoves.add(move);
						
						// prevent detouring a chesspart to attack the other
						if(firstLetter.equals("W") || firstLetter.equals("P"))
							break;
					}
				}
				else // it is the king
				{
					// check if it can move upwards
					if((i-1) >= 0)
					{
						firstLetter = Character.toString(board[i-1][j].charAt(0));
						
						if(!firstLetter.equals("B"))
						{
							move = Integer.toString(i) + Integer.toString(j) + 
								   Integer.toString(i-1) + Integer.toString(j);
								
							availableMoves.add(move);	
						}
					}
					
					// check if it can move downwards
					if((i+1) < rows)
					{
						firstLetter = Character.toString(board[i+1][j].charAt(0));
						
						if(!firstLetter.equals("B"))
						{
							move = Integer.toString(i) + Integer.toString(j) + 
								   Integer.toString(i+1) + Integer.toString(j);
								
							availableMoves.add(move);	
						}
					}
					
					// check if it can move on the left
					if((j-1) >= 0)
					{
						firstLetter = Character.toString(board[i][j-1].charAt(0));
						
						if(!firstLetter.equals("B"))
						{
							move = Integer.toString(i) + Integer.toString(j) + 
								   Integer.toString(i) + Integer.toString(j-1);
								
							availableMoves.add(move);	
						}
					}
					
					// check if it can move on the right
					if((j+1) < columns)
					{
						firstLetter = Character.toString(board[i][j+1].charAt(0));
						
						if(!firstLetter.equals("B"))
						{
							move = Integer.toString(i) + Integer.toString(j) + 
								   Integer.toString(i) + Integer.toString(j+1);
								
							availableMoves.add(move);	
						}
					}
				}			
			}	
		}
	}
	
	public double getAvgBFactor()
	{
		return (long) nBranches /  nTurns;
	}
	
	public void makeMove(int x1, int y1, int x2, int y2, int prizeX, int prizeY)
	{
		String chesspart = Character.toString(board[x1][y1].charAt(1));
		
		boolean pawnLastRow = false;
		
		// check if it is a move that has made a move to the last line
		if(chesspart.equals("P"))
			if( (x1==rows-2 && x2==rows-1) || (x1==1 && x2==0) )
			{
				board[x2][y2] = " ";	// in a case an opponent's chess part has just been captured
				board[x1][y1] = " ";
				pawnLastRow = true;
			}
		
		// otherwise
		if(!pawnLastRow)
		{
			board[x2][y2] = board[x1][y1];
			board[x1][y1] = " ";
		}
		
		// check if a prize has been added in the game
		if(prizeX != noPrize)
			board[prizeX][prizeY] = "P";
	}
	
	public void setBoard(String [][] myBoard) {
		this.board = myBoard;
	}
	
	public String [][] getBoard(){
		return this.board;
	}

	public double Simulate(int color) {
		// TODO Auto-generated method stub
		String boardStateTemp[][] = new String [7][5];
		
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < columns; col++) {
				boardStateTemp[row][col] = board[row][col]; //save board
			}
		}
		
		int side = color;
		
		String m = "";
		int k=0,n=0;
		
		for(int i = 0 ; i < 20 ; i++) {
			availableMoves = new ArrayList<String>();
			if(side == 0) {
				// I am the white player
				this.whiteMoves1();
				side = 1;
			}else {
				// I am the black player
				this.blackMoves1();
				side = 0;
			}
			if(!availableMoves.isEmpty()) {
				m = this.selectRandomAction();
			}else {
				break;
			}				
			int x1 = Character.getNumericValue(m.charAt(0));
			int y1 = Character.getNumericValue(m.charAt(1));
			int x2 = Character.getNumericValue(m.charAt(2));		
			int y2 = Character.getNumericValue(m.charAt(3));
			makeMove(x1,y1,x2,y2,9,0);	
			
			for (int row = 0; row < rows; row++) { // traverse board;
	            for (int col = 0; col < columns; col++) {
	            	if (board[row][col] == "WK") {   // check if black has won
	            		k++;
	            	} 
	            	if(board[row][col] == "BK") {
	            		n++;
	            	}
	            }
			}
			if(n == 0 || k == 0)break; // game ended
		}
		
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < columns; col++) {
				board[row][col] = boardStateTemp[row][col]; // reset board
			}
		}
		if (k==0) {
			return 1;
		}else {
			return 0;
		}
	}
	
	public String bestChild(TreeNode TN) {
		String move = "";
		double max = Integer.MIN_VALUE;
		double temp;
		for(TreeNode c : TN.children) {
			temp = c.totValue / (c.nVisits + TN.epsilon) + 2 * TN.Constant *
                    Math.sqrt(Math.log(TN.nVisits+1) / (c.nVisits)) ;
			System.out.println(temp);
			if(temp > max)move = c.Move;
		}
		return move;
	}
	
}
