import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class TreeNode {
    static Random r = new Random();
    static int nActions = 5;
    public double epsilon = 1e-6;
    public double Constant = Math.sqrt(2);
    public World board = new World();
    public int color;
    public String Move ;

    TreeNode[] children;
    double nVisits, totValue;
    
    public void setRoot(String [][] root) {
    	board = new World();
    	this.board.setBoard(root);
    }
    public void setMove(String m) {
    	this.Move = m;
    }
    
    public void setColor(int c) {
    	this.color = c;
    	this.board.setMyColor(c, 0, 0);
    }

    public void selectAction() {
    	long start = System.currentTimeMillis();
    	long end = System.currentTimeMillis();
    	while(end - start < 3000) {
    		end = System.currentTimeMillis();
	        List<TreeNode> visited = new LinkedList<TreeNode>();
	        TreeNode cur = this;
	        visited.add(this);
	        while (!cur.isLeaf()) {
	            cur = cur.select();
	            visited.add(cur);
	        }
	        cur.expand();
	        TreeNode newNode = cur.select();
	        visited.add(newNode);
	        double value = rollOut(newNode);
	        for (TreeNode node : visited) {
	            // would need extra logic for n-player game
//	        	System.out.println(this.board.getBoard());
	            node.updateStats(value);
	        }
    	}
    }

    public void expand() {
    	ArrayList<String> moves = new ArrayList<String>();
    	if(color==0) {
			moves = this.board.whiteMoves(board.getBoard());
		}else {
			moves = this.board.blackMoves(board.getBoard());
		}	
        children = new TreeNode[moves.size()];
        String m = "" ;
        for (int i=0; i<moves.size(); i++) {
            children[i] = new TreeNode();
            m = moves.get(i);
            children[i].setRoot(board.makeMyMove(board.getBoard(), m));
            if(color == 0) {
            	children[i].setColor(1);
            }else {
            	children[i].setColor(0);
            }
            children[i].setMove(m);     			
        }
    }

    private TreeNode select() {
        TreeNode selected = children[0];
        double bestValue = Double.MIN_VALUE;
        for (TreeNode c : children) {
            double uctValue = c.totValue / (c.nVisits + epsilon) + 2 * Constant *
                       Math.sqrt(Math.log(nVisits+1) / (c.nVisits)) ;
            // small random number to break ties randomly in unexpanded nodes
            if (uctValue > bestValue) {
                selected = c;
                bestValue = uctValue;
            }
        }
        return selected;
    }

    public boolean isLeaf() {
        return children == null;
    }

    public double rollOut(TreeNode tn) {
        // ultimately a roll out will end in some value
        // assume for now that it ends in a win or a loss
        // and just return this at random
    	
        return tn == null ? 0 : tn.board.Simulate(color);
    }

    public void updateStats(double value) {
        nVisits++;
        totValue += value;
    }

    public int arity() {
        return children == null ? 0 : children.length;
    }
}