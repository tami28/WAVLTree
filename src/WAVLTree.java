import java.util.LinkedList;
import java.util.Queue;

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
	private WAVLNode min_node = null;
	private WAVLNode max_node = null;

	
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
		  
		} else if((current.key > k) && (current.getRightSon() instanceof WAVLNode)) {
			return searchBranch(k, (WAVLNode) current.getRightSon());
			   
		} else if((current.key < k) && (current.getLeftSon() instanceof WAVLNode)) {
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
   * returns -1 if an ite	m with key k already exists in the tree.
   */
   public int insert(int k, String i) {
	   if (this.root ==null){
		   this.root = new WAVLNode(k,i);
		   this.max_node=root;
		   this.min_node=root;
		   return 0;
	   }
	  //return recursiveInsert(root, k, i, -1);	// to be replaced by student code
	   return anotherRecursiveInsert(k, i);
   }
   
   /**
    * recursive method. this uses a bottom upu search (like exercise 3) which means we get a better amortized time!
    * @param k - key to insert
    * @param i value of the key
    * @return number of rebalancing steps done
    */
   private int anotherRecursiveInsert(int k, String i){
	   //first we need to now where to put the new node:
	   WAVLNode closest = findClosestNode(k);
	   //if we've found the node itself - we don't want to insert..
	   if (closest.getKey() == k){
		   return -1;
	   }
	   //insert the node to the left\right according to it's size. From the way we found the closest we know that
	   //the right pllace is an external node so no need to check here again:
	   if(closest.getKey() > k){
		   closest.setLeftSon(new WAVLNode(k, i));
		   closest.getLeftSon().setParent(closest);
		   //can't forget to update min!
		   if (k < min_node.getKey()){
			   min_node = (WAVLNode) closest.getLeftSon();
		   }
	   }
	   else{
		   closest.setRightSon(new WAVLNode(k,i));
		   closest.getRightSon().setParent(closest);
		   //can't forget to update max!
		   if (k > max_node.getKey()){
			   max_node = (WAVLNode) closest.getRightSon();
		   }
	   }
	   return rebalance(closest, 0);
   }
   
   /*
   private int recursiveInsert (WAVLNode source, int k, String i, int count){
	      if (k < source.getKey()){
			   if (source.getLeftSon() instanceof WAVLExternalNode){
				   source.setLeftSon(new WAVLNode(k,i));
				   source.getLeftSon().setParent(source);
				   if (k < min_node.getKey()){
					   min_node = (WAVLNode) source.getLeftSon();
				   }
				   count = rebalance((WAVLNode) source, 0); //brings the count to zero, from now we can count rebalancing steps.
				   
			   }
			   else{
				   count = recursiveInsert((WAVLNode) source.getLeftSon(), k, i, count);
			   }
			   
		   } //end of case: k<root.key
		   else{
			   if(k > source.getKey()){
				   if(source.getRightSon() instanceof WAVLExternalNode){
					   source.setRightSon(new WAVLNode(k,i));
					   source.getRightSon().setParent(source);
					   if (k > max_node.getKey()){
						   max_node = (WAVLNode) source.getRightSon();
					   }
					   count = rebalance((WAVLNode)source, 0);
					   
				   }
				   else{
					   count = recursiveInsert((WAVLNode) source.getRightSon(), k,i,count );
				   }
			   }//end of case: k<root.key
		   }
	   return count;
   }
   */
   
   /**
    * recursive method to rebalance the tree bottom up:
    * @param node current node to update and balance
    * @param count number of balancing steps so far
    * @return number of balancing steps done
    */
   private int rebalance(WAVLNode node, int count){
	   //if the rank hasn't changed, we can stop rebalancing:
	   if (node.updateRank()){
		   count ++;
		   //if we need  to do rotations:
		   if (!(node.isValidRankDiff())){
			   //if this causes a problem, rotate:
			   int ranksDiff = node.getRankDiff();
			   //the size of the left is bigger then the right, the left is the place to balance:
			   if (ranksDiff == 2){
				   count+=rebalanceLeftSide(node);
			   }
			   else{
				   //The rank of the right subtree is bigger, adjust it:
				   if(ranksDiff == -2){
					  count+=rebalanceRightSide(node);
				   }
			   }
			   
		   }
		   //continue up until rott/ a node that doesn't get effected by the update:
		   if (node.getParent() != null){
			   count +=rebalance(node.getParent(), 0);   
		   }
		}
	   return count;
	   
   }
   
   /**
    * 
    * @param k key to search for
    * @return the node with the closest key
    */
   private WAVLNode findClosestNode(int k){
	   WAVLNode current = min_node;
	   //First we go up to a common ancestor of min and k:
	   while(current.getKey()< k){
		   if (current == root){
			   break;
		   }
		   current = current.getParent();
	   }
	   //now go back down till we've reached k or a node where is should be:
	   while(current.getKey()!= k){
		   //the whole loop is to decide which side to go or if we've reached the end of our journey:
		   if (k<current.getKey()){
			   if (current.getLeftSon() instanceof WAVLExternalNode){
				 break;  
			   } else{
				   current=(WAVLNode)current.getLeftSon();
			   }
		   } else{
			   
			   if(current.getRightSon() instanceof WAVLExternalNode){
				   break;
			   } else{
				   current = (WAVLNode)current.getRightSon();
			   }
		   }
	   }
	   //current is now either the node with k or the closest node to it:
	   return current;
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
		   child.updateRank();
	   }
	   //from now handle left-left situation:
	   
	   //if this is not the root  of the tree:
	   child = (WAVLNode)source.getLeftSon();
       int sourceKey = source.getKey();
	   String sourceValue = source.getValue();
	   int maxKey = max_node.getKey();
       source.setKey(child.getKey());
	   source.setValue(child.getValue());
	   source.setLeftSon(child.getLeftSon());
	   source.getLeftSon().setParent(source);

	   //don't want to lose any data..:
	   AbsWAVLNode temp= child.getRightSon();
	   AbsWAVLNode temp2=source.getRightSon();
	   
	   //add the original node in place:
	   source.setRightSon(new WAVLNode(sourceKey, sourceValue));
	   source.getRightSon().setParent(source);
	   //keep max updated:
	   if(maxKey == sourceKey){
		   max_node = (WAVLNode) source.getRightSon();
	   }
	   //continue working on the node with original data:
	   source = (WAVLNode) source.getRightSon();
	   source.setLeftSon(temp);
	   source.setRightSon(temp2);
	   temp.setParent(source);
	   temp2.setParent(source);
	   source.updateRank();
	   source.getParent().updateRank();
	   return count;
   }
   
   
   private int rebalanceRightSide(WAVLNode source){
	   int count = 1;
	   //This casting should be ok because we are in the case where the left side has a high rank:
	   WAVLNode child = (WAVLNode) source.getRightSon();
	   //If we are in the left-right case we would like to move it to a left-left situation::
	   if (child.getRightSon().getRank() < child.getLeftSon().getRank()){
		   count ++;
		   WAVLNode grandchild = (WAVLNode) child.getLeftSon();
		   AbsWAVLNode temp = grandchild.getRightSon();
		   //move the grandchild under root:
		   source.setRightSon(grandchild);
		   grandchild.setParent(source);
		   
		   //move the child under the grandchild:
		   grandchild.setRightSon(child);
		   child.setParent(grandchild);

		   //Restore lost leaves:
		   child.setLeftSon(temp);
		   temp.setParent(child);
		   child.updateRank();
	   }
	   //from now handle rigght-right situation:
	   child = (WAVLNode)source.getRightSon();
	   int sourceKey = source.getKey();
	   String sourceValue = source.getValue();
	   int minKey = min_node.getKey();
	   
	   //ling grandparent to child:
	   source.setKey(child.getKey());
	   source.setValue(child.getValue());
	   source.setRightSon(child.getRightSon());
	   source.getRightSon().setParent(source);
	   //don't want to lose the data..
	   AbsWAVLNode temp= child.getLeftSon();
	   AbsWAVLNode temp2=source.getLeftSon();
	   
	   //add the original node in place:
	   source.setLeftSon(new WAVLNode(sourceKey, sourceValue));
	   source.getLeftSon().setParent(source);
	   //keep min updated:
	   if(minKey == sourceKey){
		   min_node = (WAVLNode) source.getLeftSon();
	   }
	   //continue working on the node with original data:
	   source = (WAVLNode) source.getLeftSon();
	   source.setRightSon(temp);
	   source.setLeftSon(temp2);
       temp.setParent(source);
	   temp2.setParent(source);
	   source.updateRank();
	   source.getParent().updateRank();
	   return count;
   }   
   
   //TODO delete this!!!
   
   public String toString() {
	   StringBuffer print = new StringBuffer();
	   Queue<AbsWAVLNode> queue = new LinkedList<AbsWAVLNode>() ;
       if (root == null)
           return null;
       queue.clear();
       queue.add(root);
       while(!queue.isEmpty()){
    	   WAVLNode node = (WAVLNode)queue.remove();
           print.append(node.toString() + " ");
           if(!(node.getLeftSon() instanceof WAVLExternalNode)) queue.add(node.getLeftSon());
           if(!(node.getRightSon() instanceof WAVLExternalNode)) queue.add(node.getRightSon());
       }
       return print.toString();
   }
   
   /**
    * 
    * @return true if this is a valid WAVL tree..
    */
   public boolean isValidTree(){
	   return isValidRank(root);
   }
   
   //used to make sure all our nodes are cool and follow WAVL standards:
   private boolean isValidRank(WAVLNode node){
		boolean valid = node.isValidRankDiff();
		if(node.getRightSon() instanceof WAVLTree.WAVLNode){
			valid = isValidRank((WAVLTree.WAVLNode) node.getRightSon()) && valid;
		}
		if (node.getLeftSon() instanceof WAVLTree.WAVLNode){
			valid =  isValidRank((WAVLTree.WAVLNode)node.getLeftSon()) && valid;
		}
		return valid;
	}
   //TODO delete until here

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
	   if (search(k)!=null)
		   return recursiveDelete(root, k, null);
	   return -1;
   }
   
   public int recursiveDelete(WAVLNode current, int k, WAVLNode parent) {
	   
	   //if this is the correct node to delete, delete the node correctly
	   if (current.key == k){
		   //delete node in a scenario of node being a leaf
		   if (isLeaf(current)) {
			   if (parent != null) {
				   if (parent.key > k)
					   return deleteLeaf(current, current.parent, true);
				   else
					   return deleteLeaf(current, current.parent, false);
			   } else {
				   root = null;
				   this.min_node = null;
				   this.max_node = null;
			   }
			   return 0;
		  //delete node in the scenario of node being an unary node
		   } else if (isLeftUnary(current)) {
			   return deleteUnaryNode(current, current.parent, true);
			   
		   } else if (isRightUnary(current)) {
			   return deleteUnaryNode(current, current.parent, false);
		   //deleting a none-unary node
		   } else {
			   WAVLNode temp = findSuccessor(current, current.key);
			   current.setKey(temp.key);
			   current.setValue(temp.value);
			   delete(temp.key);
			   //TODO: balance tree without this node
			   return 0;
			   
		   }

	   //searching for the correct node to delete
	   } else if (current.key > k) {
		   return recursiveDelete((WAVLNode) current.getRightSon(), k, current);
	   } else {
		   return recursiveDelete((WAVLNode) current.getLeftSon(), k, current);
	   }
   }
   
   public int deleteLeaf(WAVLNode current, WAVLNode parent, boolean isLeft){
	   //delete the relevant leaf
	   if(this.min_node.key == current.key){
		   this.min_node = parent;
	   }
	   if(this.max_node.key == current.key){
		   this.max_node = parent;
	   }

	   if (isLeft)
		   parent.leftSon = new WAVLExternalNode();
	   else
		   parent.rightSon = new WAVLExternalNode();
	   
	   //update parent's rank accordingly
	   parent.updateRank();
	   
	   //handle the new different structures of the tree
	   if (parent.getRank() == 1){
		   return 0;
	   } else if (parent.getRank() == 0){
		   if (parent.getParent() == null)
			   return 0;
		   else {
			   if (parent.parent.getKey() > parent.getKey())
				   return rebalanceRightSide(parent);
			   else
				   return rebalanceLeftSide(parent);
		   }
	   } else {
		   if (isLeft)
			   return rebalanceRightSide(parent);
		   else
			   return rebalanceLeftSide(parent);
	   }
   }
   
   public int deleteUnaryNode(WAVLNode current, WAVLNode parent, boolean hasLeftLeaf){
	   //replacing current node with it's only son
	   boolean min_flag=false, max_flag=false;
	   if(this.min_node.key == current.key){
		   min_flag = true;
	   }
	   if(this.max_node.key == current.key){
		   max_flag = true;
	   }

	   if (hasLeftLeaf){
		   current = (WAVLNode) current.getLeftSon();
		   this.max_node = (max_flag ? current : this.max_node);
	   }
	   else {
		   current = (WAVLNode) current.getRightSon();
		   this.min_node = (min_flag ? current : this.min_node);
	   }

	   if (parent == null)
		   return 0;
	   
	   if (parent.getRank() - current.getRank() <= 2)
		   return 0;
	   else {
		   if (parent.getKey() > current.getKey())
			   return rebalanceRightSide(parent);
		   else
			   return rebalanceLeftSide(parent);
	   }
   }

   public boolean isLeaf(WAVLNode current){
	   if (current.getLeftSon() instanceof WAVLExternalNode && current.getRightSon() instanceof WAVLExternalNode)
		   return true;
	   return false;
   }

   public boolean isLeftUnary(WAVLNode current){
	   if (!(current.getLeftSon() instanceof WAVLExternalNode) && current.getRightSon() instanceof WAVLExternalNode)
		   return true;
	   return false;
   }

   public boolean isRightUnary(WAVLNode current){
	   if ((current.getLeftSon() instanceof WAVLExternalNode) && !(current.getRightSon() instanceof WAVLExternalNode))
		   return true;
	   return false;
   }

   public WAVLNode findSuccessor(WAVLNode current, int k){
	   if (isLeaf(current)){
		   return current;
	   }
	   if (isLeftUnary(current)){
		   WAVLNode lefty = (WAVLNode) findSuccessor((WAVLNode) current.getLeftSon(), k);
		   if (k-lefty.key<k-current.key)
			   return lefty;
	   } else if (isRightUnary(current)){
		   WAVLNode righty = (WAVLNode) findSuccessor((WAVLNode) current.getRightSon(), k);
		   if (k-righty.key<k-current.key)
			   return righty;
	   }
	   return current;
   }

   /**
    * public String min()
    *
    * Returns the info of the item with the smallest key in the tree,
    * or null if the tree is empty
    */
   public String min()
   {
	   //If the tree is empty there is no minimum value - return null
	   if (empty()){
		   return null;
	   }
	   return min_node.value;
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
	   return max_node.value;
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
  
  /**
   * helper recurssive method:
   * @param root - root of the subtree
   * @param arr - the array where we put the keys
   * @param low - index of array to start from. Basically is how many keys before my subtree.
   * @return sorted array of keys
   */
  private int[] keysToArray(WAVLNode root, int[] arr, int low){
	  //Where to put this key:
	  int place = low+root.getLeftSon().getSize();
	  arr[place] = root.getKey();
	  //fill up the left side of the array:
	  if (!(root.getLeftSon() instanceof WAVLExternalNode)){
		  arr = keysToArray((WAVLNode)root.getLeftSon(), arr, low);
	  }
	  //fill up the right side of the array:
	  if (!(root.getRightSon() instanceof WAVLExternalNode)){
		arr = keysToArray((WAVLNode)root.getRightSon(), arr, place+1);  
	  }
	  return arr;
	  
  }
  
  /**
   * public String[] infoToArray()
   *
   * Returns an array which contains all info in the tree,
   * sorted by their respective keys,
   * or an empty array if the tree is empty.
   */
  public String[] infoToArray()
  {
	  if (empty()){
		  return null;
	  }
	  int size = size();
	  String[] arr = new String[size];
      return infoToArray(root, arr, 0);                    // to be replaced by student code
  }
  
  /**
   * recursive method to create array of values orderd by the key.
   * @param root the subtree we are working on
   * @param arr sorted array of values
   * @param low index from which to insert this subtree
   * @return array of values sorted by the key
   */
  private String[] infoToArray(WAVLNode root, String[] arr, int low){
	  //where to put the current value:
	  int place = low+root.getLeftSon().getSize();
	  arr[place] = root.getValue();
	  //fill up the left side of the array:
	  if (!(root.getLeftSon() instanceof WAVLExternalNode)){
		  arr = infoToArray((WAVLNode)root.getLeftSon(), arr, low);
	  }
	  //fill up the right side of the array:
	  if (!(root.getRightSon() instanceof WAVLExternalNode)){
		  arr =infoToArray((WAVLNode)root.getRightSon(), arr, place+1);
	  }
	  return arr;
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

   public WAVLNode getRoot() {
		return root;
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
	   	public abstract int getSize();
	   	public abstract String toString();
	

	}
   
   
  public static class WAVLNode extends AbsWAVLNode{
	  private int key;
	  private String value;
	  private AbsWAVLNode leftSon = null;
	  private AbsWAVLNode rightSon = null;
	  
	  public WAVLNode (int key, String value){
		  this.setKey(key);
		  this.setValue(value);
		  setRank(0);
		  setParent(null);
		  this.leftSon = new WAVLExternalNode();
		  this.leftSon.setParent(this);
		  this.rightSon = new WAVLExternalNode();
		  this.rightSon.setParent(this);
	  }
	  
	  /**
	   * @return This will be positive if the left tree size is bigger thhen the right, zero if equal, negative otherwise
	   */
	  public int getRankDiff(){
		  return leftSon.getRank() - rightSon.getRank();
	  }
	  
	  /**
	   * updates the rank based on its kids.
	   * @return true if the rank was changed, false if not.
	   */
	  public boolean updateRank(){
		  int temp = rank;
		  setRank((Math.max(getLeftSon().getRank(), getRightSon().getRank())) + 1);
		  return rank != temp;
		 }
	  
	  /**
	   * 
	   * @return is the diff between the node and it's sons are allowed for an WAVL tree
	   */
	  public boolean isValidRankDiff(){
		  if(rightSon instanceof WAVLExternalNode && leftSon instanceof WAVLExternalNode){
			  return(rank ==0);
		  }
		  return (Math.min(getRank() - rightSon.getRank(), getRank()- leftSon.getRank()) >= 1 && 
				  Math.max(getRank()-rightSon.getRank(), getRank()-leftSon.getRank()) <= 2);
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

	@Override
	public int getSize() {
		return (rightSon.getSize() + leftSon.getSize() +1);
	}

	@Override
	public String toString() {
		return key+":"+value;
	}


  
  }
  
  public static class WAVLExternalNode extends AbsWAVLNode{
	  public WAVLExternalNode (){
		  this.setRank(-1);
	  }

	@Override
	public int getSize() {
		return 0;
	}

	@Override
	public String toString() {
		return null;
	}
  }

  
}
  

