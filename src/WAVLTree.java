/**
 *
 * WAVLTree
 *
 * An implementation of a WAVL Tree with
 * distinct integer keys and info
 *
 */

public class WAVLTree {

	private WAVLNode root = null;
	
	/**
   	* public boolean empty()
   	*
   	* returns true if and only if the tree is empty
   	*
   	*/
	public boolean empty() {
		return (root==null);
	}

 /**
   * public String search(int k)
   *
   * returns the info of an item with key k if it exists in the tree
   * otherwise, returns null
   */
	public String search(int k)
	  {
		return searchBranch(k, root);  // to be replaced by student code
	  }
	  
	public String searchBranch(int k, WAVLNode current){ 
		//While there's still how to go down the tree (and key not found) - search through the correct path.
		if (current.key == k) {
			return current.value;
		  
		} else if((current.key > k) && (current.getRightSon() instanceof AbsWAVLNode)) {
			return searchBranch(k, (WAVLNode) current.getRightSon());
			   
		} else if((current.key < k) && (current.getLeftSon() instanceof AbsWAVLNode)) {
			return searchBranch(k, (WAVLNode) current.getRightSon());
			  
		} else 
			return null;
			  
	  }
  /**
   * public int insert(int k, String i)
   *
   * inserts an item with key k and info i to the WAVL tree.
   * the tree must remain valid (keep its invariants).
   * returns the number of rebalancing operations, or 0 if no rebalancing operations were necessary.
   * returns -1 if an item with key k already exists in the tree.
   */
   public int insert(int k, String i) {
	  return recursiveInsert(root, k, i);	// to be replaced by student code
   }
   
   private int recursiveInsert (WAVLNode root, int k, String i){
	   int count = -1;
	   if (root ==null){
		   root = new WAVLNode(k,i);
	   }
	   else{
		   if (k < root.getKey()){
			   if (root.getLeftSon() instanceof WAVLExternalNode){
				   root.setLeftSon(new WAVLNode(k,i));
				   root.getLeftSon().setParent(root);
				   count = promote((WAVLNode) root.getLeftSon(), 0); //brings the count to zero, from now we can count rebalancing steps.
			   }
			   else{
				   count = recursiveInsert((WAVLNode) root.getLeftSon(), k, i);
			   }
			   
		   } //end of case: k<root.key
		   else{
			   if(k > root.getKey()){
				   if(root.getRightSon() instanceof WAVLExternalNode){
					   root.setRightSon(new WAVLNode(k,i));
					   root.getRightSon().setParent(root);
					   //TODO balance
					   count = promote((WAVLNode)root.getRightSon(), 0);
				   }
				   else{
					   recursiveInsert((WAVLNode) root.getRightSon(), k,i );
				   }
			   }//end of case: k<root.key
		   }
	   }//and of case: root not null
	   
	   return count;
   }
   
   private int promote(WAVLNode node, int count){
	   //increase rank by 1
	   node.setRank((Math.max(node.getLeftSon().getRank(), node.getRightSon().getRank())) + 1);
	   count ++;
	   //if this causes a problem, rotate:
	   int ranksDiff = node.getLeftSon().getRank()-node.getRightSon().getRank();
	   //the size of the left is bigger then the right, the left is the place to balance:
	   if (ranksDiff == 2){
		   //TODO-rotation
	   }
	   else{
		   //The rank of the right subtree is bigger, adjust it:
		   if(ranksDiff == -2){
			   //TODO-rotation
		   }
	   }
	   //TODO: after rotation need to update, from which node up?
	   //if we fix the problem by rotating, didn't finish promoting all nodes:
	   if (node.getParent() != null){
		   node.setRank(node.getRank() + 1);
		   count = count + promote(node.getParent(), count);   
	   }
	   
	   return count;
   }
   
   private int rebalanceLeftSide(WAVLNode source){
	   int count = 1;
	   //This casting should be ok because we are in the case where the left side has a high rank:
	   WAVLNode child = (WAVLNode) source.getLeftSon();
	   //If we are in the left-right case we would like to move it to a left-left situation::
	   if (child.getLeftSon().getRank() < child.getRightSon().getRank()){
		   count ++;
		   WAVLNode grandchild = (WAVLNode) child.getRightSon();
		   AbsWAVLNode temp = grandchild.getLeftSon();
		   //move the grandchild under root:
		   source.setLeftSon(grandchild);
		   grandchild.setParent(source);
		   
		   //move the child under the grandchild:
		   grandchild.setLeftSon(child);
		   child.setParent(grandchild);
		   //Restore lost leaves:
		   child.setRightSon(temp);
		   temp.setParent(child);
	   }
	   //from now handle left-left situation:
	   
	   //if this is not the root  of the tree:
	   if (source.getParent() != null){
		   WAVLNode grandfather = (WAVLNode)source.getParent();
		   child = (WAVLNode)source.getLeftSon();
		   grandfather.setLeftSon(child);
		   child.setParent(grandfather);
		   
		   AbsWAVLNode temp= child.getRightSon();
		   child.setRightSon(source);
		   source.setParent(child);
		   
		   source.setLeftSon(temp);
		   temp.setParent(source);
	   }
	   //this is the root, need a different approach:
	   else{
		   //source is root, we are not losing information:
		   this.root = (WAVLNode) source.getLeftSon();
		   this.root.setParent(null);
		   AbsWAVLNode temp = root.getRightSon();
		   root.setRightSon(source);
		   source.setParent(root);
		   source.setLeftSon(temp);
		   temp.setParent(source);
	   }
	   return count;
   }

  /**
   * public int delete(int k)
   *
   * deletes an item with key k from the binary tree, if it is there;
   * the tree must remain valid (keep its invariants).
   * returns the number of rebalancing operations, or 0 if no rebalancing operations were needed.
   * returns -1 if an item with key k was not found in the tree.
   */
   public int delete(int k)
   {
	   return 42;	// to be replaced by student code
   }

   /**
    * public String min()
    *
    * Returns the iîfo of the item with the smallest key in the tree,
    * or null if the tree is empty
    */
   public String min()
   {
	   //If the tree is empty there is no minimum value - return null
	   if (empty()){
		   return null;
	   }
	   
	   WAVLNode current = root;
	   //While we can go left - there is a smaller key.. we will return the value of the leftmost node
	   while (!(current.getLeftSon() instanceof WAVLExternalNode)){
		   current = (WAVLNode) current.getLeftSon();
	   }
	   return current.getValue(); // to be replaced by student code
   }

   /**
    * public String max()
    *
    * Returns the info of the item with the largest key in the tree,
    * or null if the tree is empty
    */
   public String max()
   {
	   if (empty()){
		   return null;
	   }
	   WAVLNode current = root;
	   while (!(current.getRightSon() instanceof WAVLExternalNode)){
		   current = (WAVLNode) current.getRightSon();
	   }
	   return current.getValue();
   }

  /**
   * public int[] keysToArray()
   *
   * Returns a sorted array which contains all keys in the tree,
   * or an empty array if the tree is empty.
   */
  public int[] keysToArray()
  {
	  if (empty()){
		  return null;
	  }
	  int size=size();
	  int[] arr = new int[size];
      
	  return keysToArray(root, arr, 0);              // to be replaced by student code
  }
  
  private int[] keysToArray(WAVLNode root, int[] arr, int low){
	  int place = low+root.getLeftSon().getSize();
	  arr[place] = root.getKey();
	  if (!(root.getLeftSon() instanceof WAVLExternalNode)){
		  arr = keysToArray((WAVLNode)root.getLeftSon(), arr, low);
	  }
	  if (!(root.getRightSon() instanceof WAVLExternalNode)){
		arr = keysToArray((WAVLNode)root.getRightSon(), arr, place+1);  
	  }
	  return arr;
	  
  }
  
  /*
  private int[] keysToArray(WAVLNode root, int[] arr, int low, int high){
	  int middle = (low+high)/2;
	  if (!(root.getLeftSon() instanceof WAVLExternalNode)){
		  arr = keysToArray((WAVLNode) root.getLeftSon(),arr, low, middle);
	  }
	  if (!(root.getRightSon() instanceof WAVLExternalNode)){
		  arr = keysToArray((WAVLNode)root.getRightSon(), arr, middle, high);
	  }
	  return arr;
  }
*/
  /**
   * public String[] infoToArray()
   *
   * Returns an array which contains all info in the tree,
   * sorted by their respective keys,
   * or an empty array if the tree is empty.
   */
  public String[] infoToArray()
  {
        String[] arr = new String[42]; // to be replaced by student code
        return arr;                    // to be replaced by student code
  }

   /**
    * public int size()
    *
    * Returns the number of nodes in the tree.
    *
    * precondition: none
    * postcondition: none
    */
   public int size()
   {
	   return root.getSize();
   }

  /**
   * public class WAVLNode
   *
   * If you wish to implement classes other than WAVLTree
   * (for example WAVLNode), do it in this file, not in 
   * another file.
   * This is an example which can be deleted if no such classes are necessary.
   */
   public static abstract class AbsWAVLNode {
	   	protected int rank;
	   	protected int size;
	   	protected WAVLNode parent;
	   	
	   	public WAVLNode getParent() {
			return parent;
		}

		public void setParent(WAVLNode parent) {
			this.parent = parent;
		}

		public int getRank() {
			return rank;
		}

	   	public void setRank(int rank) {
			this.rank = rank;
		}
	   	public int getSize() {
			return size;
		}

		public void setSize(int size) {
			this.size = size;
		}
		

	}
   
   
  public static class WAVLNode extends AbsWAVLNode{
	  private int key;
	  private String value;
	  private AbsWAVLNode leftSon = null;
	  private AbsWAVLNode rightSon = null;
	  
	  public WAVLNode (int key, String value){
		  this.setKey(key);
		  this.setValue(value);
		  setSize(0);
		  setRank(0);
		  setParent(null);
		  this.leftSon = new WAVLExternalNode();
		  this.leftSon.setParent(this);
		  this.rightSon = new WAVLExternalNode();
		  this.rightSon.setParent(this);
	  }

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public AbsWAVLNode getLeftSon() {
		return leftSon;
	}

	public void setLeftSon(AbsWAVLNode temp) {
		this.leftSon = temp;
	}

	public AbsWAVLNode getRightSon() {
		return rightSon;
	}

	public void setRightSon(AbsWAVLNode temp) {
		this.rightSon = temp;
	}


  
  }
  
  public static class WAVLExternalNode extends AbsWAVLNode{
	  public WAVLExternalNode (){
		  this.setRank(-1);
		  this.setSize(0);
	  }
  }

}
  

