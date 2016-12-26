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
	private int size = 0;
	
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
		if (!(root instanceof WAVLNode)){
			return null;
		}
		return searchBranch(k, root);
	  }
	  
	public String searchBranch(int k, WAVLNode current) { 
		//While there's still how to go down the tree (and key not found) - search through the correct path.
		if (current.key == k) {
			return current.value;
		} else if((current.key < k) && (current.getRightSon() instanceof WAVLNode)) {
			return searchBranch(k, (WAVLNode) current.getRightSon());
			   
		} else if ((current.key > k) && (current.getLeftSon() instanceof WAVLNode)) {
			return searchBranch(k, (WAVLNode) current.getLeftSon());
			  
		} else {
			return null;
		}
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
	   if (this.root ==null){
		   this.root = new WAVLNode(k,i);
		   this.max_node=root;
		   this.min_node=root;
		   this.size = 1;
		   return 0;
	   }
	   return recursiveInsert(k, i);
   }
   
   /**
    * This uses a bottom upu search (like exercise 3) which means we get a better amortized time!
    * @param k - key to insert
    * @param i value of the key
    * @return number of rebalancing steps done
    */
   private int recursiveInsert(int k, String i){
	   //first we need to know where to put the new node:
	   WAVLNode closest = findClosestNode(k);
	   //if we've found the node itself - we don't want to insert..
	   if (closest.getKey() == k){
		   return -1;
	   }
	   //insert the node to the left\right according to it's size. From the way we found the closest we know that
	   //the right pllace is an external node so no need to check here again:
	   size ++;
	   if(closest.getKey() > k){
		   closest.setLeftSon(new WAVLNode(k, i));
		   //closest.getLeftSon().setParent(closest);
		   //can't forget to update min!
		   if (k < min_node.getKey()){
			   min_node = (WAVLNode) closest.getLeftSon();
		   }
	   }
	   else {
		   closest.setRightSon(new WAVLNode(k,i));
		   //closest.getRightSon().setParent(closest);
		   //can't forget to update max!
		   if (k > max_node.getKey()){
			   max_node = (WAVLNode) closest.getRightSon();
		   }
	   }
	   return rebalance(closest, 0);
   }
   
  /**
    * recursive method to rebalance the tree bottom up:
    * @param node current node to update and balance
    * @param count number of balancing steps so far
    * @return number of balancing steps done
    */
   private int rebalance(WAVLNode node, int count){
	   //if the rank hasn't changed, we can stop rebalancing:
	   if (node.updateRank()){
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
			   
		   } else{
			   //We just updated a rank - add to the count..
			   count++;
		   }
		   //continue up until root/ a node that doesn't get effected by the update:
		   if (node.getParent() != null){
			   count +=rebalance(node.getParent(), 0);   
		   }
		} else if (!node.isValidRankDiff()) {
			int ranksDiff = node.getRankDiff();
			   //the size of the left is bigger then the right, the left is the place to balance:
			if (ranksDiff == 2){
				   count+=rebalanceLeftSide(node);
			}
			else if (ranksDiff == -2 ){
				//The rank of the right subtree is bigger, adjust it:
				count+=rebalanceRightSide(node);
			}
			if (!(node.parent == null))
				count+= rebalance(node.getParent(), count);
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
		   rotateLeftRightToLeftLeft(source, child);
		   count++;
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
	   //source.getLeftSon().setParent(source);

	   //don't want to lose any data..:
	   AbsWAVLNode temp= child.getRightSon();
	   AbsWAVLNode temp2=source.getRightSon();
	   
	   //add the original node in place:
	   source.setRightSon(new WAVLNode(sourceKey, sourceValue));
	   //source.getRightSon().setParent(source);
	   //keep max updated:
	   if(maxKey == sourceKey){
		   max_node = (WAVLNode) source.getRightSon();
	   }
	   //continue working on the node with original data:
	   source = (WAVLNode) source.getRightSon();
	   source.setLeftSon(temp);
	   source.setRightSon(temp2);
	   //temp.setParent(source);
	   //temp2.setParent(source);
	   source.updateRank();
	   source.getParent().updateRank();
	   return count;
   }

	private void rotateLeftRightToLeftLeft(WAVLNode source,WAVLNode child) {
	   WAVLNode grandchild = (WAVLNode) child.getRightSon();
	   AbsWAVLNode temp = grandchild.getLeftSon();
	   //move the grandchild under root:
	   source.setLeftSon(grandchild);
	   //grandchild.setParent(source);
	   
	   //move the child under the grandchild:
	   grandchild.setLeftSon(child);
	   //child.setParent(grandchild);
	   //Restore lost leaves:
	   child.setRightSon(temp);
	   //temp.setParent(child);
	   child.updateRank();

	}
   
   
   private int rebalanceRightSide(WAVLNode source){
	   int count = 1;
	   //This casting should be ok because we are in the case where the left side has a high rank:
	   WAVLNode child = (WAVLNode) source.getRightSon();
	   //If we are in the left-right case we would like to move it to a left-left situation::
	   if (child.getRightSon().getRank() < child.getLeftSon().getRank()){
		   count ++;
		   rotateRightLeftToRightRight(source, child);
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
	   //source.getRightSon().setParent(source);
	   //don't want to lose the data..
	   AbsWAVLNode temp= child.getLeftSon();
	   AbsWAVLNode temp2=source.getLeftSon();
	   
	   //add the original node in place:
	   source.setLeftSon(new WAVLNode(sourceKey, sourceValue));
	   //source.getLeftSon().setParent(source);
	   //keep min updated:
	   if(minKey == sourceKey){
		   min_node = (WAVLNode) source.getLeftSon();
	   }
	   //continue working on the node with original data:
	   source = (WAVLNode) source.getLeftSon();
	   source.setRightSon(temp);
	   source.setLeftSon(temp2);
       //temp.setParent(source);
	   //temp2.setParent(source);
	   source.updateRank();
	   source.getParent().updateRank();
	   return count;
   }

private void rotateRightLeftToRightRight(WAVLNode source, WAVLNode child) {
	WAVLNode grandchild = (WAVLNode) child.getLeftSon();
	   AbsWAVLNode temp = grandchild.getRightSon();
	   //move the grandchild under root:
	   source.setRightSon(grandchild);
	 //  grandchild.setParent(source);
	   
	   //move the child under the grandchild:
	   grandchild.setRightSon(child);
	   //child.setParent(grandchild);

	   //Restore lost leaves:
	   child.setLeftSon(temp);
	   //temp.setParent(child);
	   child.updateRank();
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
			valid = valid && isValidRank((WAVLTree.WAVLNode) node.getRightSon());
		}
		if (node.getLeftSon() instanceof WAVLTree.WAVLNode){
			valid =  valid && isValidRank((WAVLTree.WAVLNode)node.getLeftSon());
		}
		return valid;
	}
   
  /**
   * public int delete(int k)
   *
   * deletes an item with key k from the binary tree, if it is there; Uses deleteLeaf to find the
   * relevant node and delete, considering different scenarios.
   * the tree must remain valid (keep its invariants).
   * returns the number of rebalancing operations, or 0 if no rebalancing operations were needed.
   * returns -1 if an item with key k was not found in the tree.
   */
   public int delete(int k)
   {
	   if (search(k)!=null)
		   return recursiveDelete(root, k, null, true);
	   return -1;
   }
   
   /**
    * public int recursiveDelete(WAVLNode current, int k, WAVLNode parent, boolean toRebalance)
    *
    * searches and deletes an item differently for each one of three different types of node;
    *  uses the following methods - isLeaf, isLeftUnary and isRightUnary to identify the types
    *  of node, and the methods deleteLeaf and deleteUnaryNode to delete them. Also uses
    *  the method findSuccessor and rebalance for adjusting the tree.
    */
   public int recursiveDelete(WAVLNode current, int k, WAVLNode parent, boolean toRebalance) {
	   //if this is the correct node to delete, delete the node correctly
	   if (current.key == k){
		   this.size--;	
		   //delete node in a scenario of node being a leaf
		   if (current.isLeaf()) {//if the node is not the tree's root it handle left and right leafs
			   if (parent != null) {
				   if (parent.key > k) {
					   return deleteLeaf(current, current.parent, true, toRebalance);
				   } else {
					   return deleteLeaf(current, current.parent, false, toRebalance);
				   }
			   } else {//if the node is the root, the tree is initialized
				   root = null;
				   this.min_node = null;
				   this.max_node = null;
				   return 0;
			   }
		  //delete node in the scenario of node being a left unary node
		   } else if (isLeftUnary(current)) {
			   if (current.parent == null) {
				   root = (WAVLNode) current.getLeftSon();
				   this.max_node = root;
				   return 0;
			   }
			   return deleteUnaryNode(current, current.parent, true, toRebalance);

		  //delete node in the scenario of node being a left unary node
		   } else if (isRightUnary(current)) {
			   if (current.parent == null) {
				   root = (WAVLNode) current.getRightSon();
				   this.min_node = root;
				   return 0;
			   }
			   return deleteUnaryNode(current, current.parent, false, toRebalance);

		  //deleting a none-unary node
		   } else {
			   //find a successor node to be replaced with
			   WAVLNode temp = findSuccessor(current);
			   int new_key = temp.key;
			   String new_value = temp.value;
			   WAVLNode temp_parent = temp.getParent(); 
			   
			   //deleting the successor node without rebalancing
			   int counter = recursiveDelete(current, new_key, parent, false);
			   //setting the old node to be the new node
			   current.setKey(new_key);
			   current.setValue(new_value);
			   //rebalancing the tree after the replacement has been done
			   counter += rebalance(temp_parent, 0);
			   return counter;
		   }

	   //if current is not the node to be deleted, searching for the correct node to delete
	   } else if (current.key > k) {
		   return recursiveDelete((WAVLNode) current.getLeftSon(), k, current, toRebalance);
	   } else {
		   return recursiveDelete((WAVLNode) current.getRightSon(), k, current, toRebalance);
	   }
   }
   
   /**
    * public int deleteLeaf(WAVLNode current, WAVLNode parent, boolean isLeft, boolean toRebalance)
    *
    * deletes a leaf node with key k from the binary tree
    * updates the min\max node if necessary, assigns  WAVLExternalNode as a deletion method
    * rebalances the tree only if it's not a part of a binary deletion 
    */
   public int deleteLeaf(WAVLNode current, WAVLNode parent, boolean isLeft, boolean toRebalance) {
	   //replace the min\max node before the deletion, if relevant
	   if(this.min_node.key == current.key){
		   this.min_node = parent;
	   }
	   if(this.max_node.key == current.key){
		   this.max_node = parent;
	   }
	   //delete the relevant leaf
	   if (isLeft) {
		   parent.setLeftSon(new WAVLExternalNode());
	   } else {
		   parent.setRightSon(new WAVLExternalNode());
	   }
	   //balances the tree if necessary
	   int count = 0;
	   if (toRebalance)
		   count = rebalance(parent, 0);
	   return count;
	   
   }
   
   /**
    * public int deleteUnaryNode(WAVLNode current, WAVLNode parent, boolean hasLeftLeaf, boolean toRebalance)
    *
    * deletes an unary node with key k from the binary tree
    * updates the min\max node if necessary, assigns  WAVLExternalNode as a deletion method
    * rebalances the tree only if it's not a part of a binary deletion 
    */
   public int deleteUnaryNode(WAVLNode current, WAVLNode parent, boolean hasLeftLeaf, boolean toRebalance){
	   //check if the node is a min or max node
	   boolean min_flag=false, max_flag=false;
	   if(this.min_node.key == current.key){
		   min_flag = true;
	   }
	   if(this.max_node.key == current.key){
		   max_flag = true;
	   }
	   
	   //updates the node's parent to point at the node's son
	   if (hasLeftLeaf){
		   if (current.parent.getLeftSon().equals(current))
			   parent.setLeftSon(current.getLeftSon());
		   else
			   parent.setRightSon(current.getLeftSon());
		   this.max_node = (max_flag ? current : this.max_node);
	   }
	   else {
		   if (current.parent.getLeftSon().equals(current))
			   parent.setLeftSon(current.getRightSon());
		   else
			   parent.setRightSon(current.getRightSon());
		   this.min_node = (min_flag ? current : this.min_node);
	   }
	   
	   //rebalances the tree if it's unbalanced and the deletion is not
	   //a part of a binary deletion.
		   int count = 0;
		   if (toRebalance)
			   count += rebalance(parent, count);
		   return count;
   }

   /**
    * public boolean isLeaf(WAVLNode current)
    * 
    * checks if the node doesn't point to any leaf
    */
   public boolean isLeaf(WAVLNode current){
	   return (current.getLeftSon() instanceof WAVLExternalNode
			   && current.getRightSon() instanceof WAVLExternalNode);
   }

   /**
    * public boolean isLeftUnary(WAVLNode current)
    * 
    * checks if the node has a left leaf only
    */
   public boolean isLeftUnary(WAVLNode current){
	   return (!(current.getLeftSon() instanceof WAVLExternalNode)
			   && current.getRightSon() instanceof WAVLExternalNode);
   }

   /**
    * public boolean isRightUnary(WAVLNode current)
    * 
    * checks if the node has a right leaf only
    */
   public boolean isRightUnary(WAVLNode current){
	   return ((current.getLeftSon() instanceof WAVLExternalNode)
			   && !(current.getRightSon() instanceof WAVLExternalNode));
   }

   /**
    * public WAVLNode findSuccessor(WAVLNode current)
    * 
    * a method that searches a node's closest successor for replacement.
    * compares the right and left sub-nodes using the recursiveSuccessor method.
    */
   public WAVLNode findSuccessor(WAVLNode current){
	   //search for the best successor of each side
	   WAVLNode lefty = (WAVLNode) recursiveSuccessor((WAVLNode) current.getLeftSon(), current.key);
	   WAVLNode righty = (WAVLNode) recursiveSuccessor((WAVLNode) current.getRightSon(), current.key);
	   //compare the successors and returns the closest.
	   if (Math.abs(current.key - lefty.key) < Math.abs(righty.key - current.key)) {
		   return (WAVLNode) lefty;
	   } else {
		   return (WAVLNode) righty;
	   }
   }
   
   /**
    * public WAVLNode recursiveSuccessor(WAVLNode current, int k)
    * 
    * a recursive method that checks for every node which of
    * it's sub-node is the closest to k. The function returns
    * the closest successor to k.
    */
   public WAVLNode recursiveSuccessor(WAVLNode current, int k) {
	   //if the node is a leaf - returns the leaf itself 
	   if (isLeaf(current))
		   return current;
	   //if the node is an unary - returns the closest of each side
	   if (isLeftUnary(current)){
		   WAVLNode lefty = (WAVLNode) current.getLeftSon();
		   if (Math.abs(k-lefty.key) < Math.abs(k-current.key))
			   return lefty;
		   return current;
	   } else if (isRightUnary(current)){
		   WAVLNode righty = (WAVLNode) current.getRightSon();
		   if (Math.abs(righty.key-k) < Math.abs(k-current.key))
			   return righty;
		   return current;
	   //if the node is a binary - compares and returns the best returns of each side  
	   } else {
		   WAVLNode lefty = (WAVLNode) recursiveSuccessor((WAVLNode) current.getLeftSon(), k);
		   WAVLNode righty = (WAVLNode) recursiveSuccessor((WAVLNode) current.getRightSon(), k);
		   if (Math.abs(k - current.key) < Math.abs(righty.key - k) &&
				   Math.abs(k - current.key) < Math.abs(lefty.key - k))
			   return current;
		   if (Math.abs(k - lefty.key) < Math.abs(righty.key - k))
			   return (WAVLNode) lefty;
		   else
			   return (WAVLNode) righty;
	   }
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
	  int[] arr = new int[size];
      
	  keysToArray(root, arr, 0);
	  
	  return arr;// to be replaced by student code
  }
  
  
  /**
   * 
   * Helper recursive method for keysToArray
   * @param source - the source of the current subtree
   * @param arr - the array to fill
   * @param place - where to start the current subtree
   * @return the place for the current subtree
   */
  private int keysToArray(AbsWAVLNode source, int[] arr, int place){
	  //if this is an external node we don't want to addqchange the placement
	  if(source instanceof WAVLExternalNode){
		  return place;
	  }
	  //put all the left childs in the array:
	  place = keysToArray(((WAVLNode)source).getLeftSon(), arr, place);
	  //now put this one in:
	  arr[place] = ((WAVLNode) source).getKey();
	  //now put the right subtree in
	  place = keysToArray(((WAVLNode)source).getRightSon(), arr, ++place);
	  return place;
	  
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
	  String[] arr = new String[size];
      infoToArray(root, arr, 0);
      return arr;
  }
  

  private int infoToArray(AbsWAVLNode source, String[] arr, int place){
	  //if this is an external node we don't want to addqchange the placement
	  if(source instanceof WAVLExternalNode){
		  return place;
	  }
	  //put all the left childs in the array:
	  place = infoToArray(((WAVLNode)source).getLeftSon(), arr, place);
	  //now put this one in:
	  arr[place] = ((WAVLNode) source).getValue();
	  //now put the right subtree in
	  place = infoToArray(((WAVLNode)source).getRightSon(), arr, ++place);
	  return place;
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
	   return size;
   }

   /**
    * public WAVLNode getRoot()
    *
    * Returns the root of the tree.
    *
    * precondition: none
    */
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
		  if(isLeaf()){
			  return(rank ==0);
		  }
		  return (isRankDiffSmallEnough() && isRankDiffBigEnough());
	  }
	  
	  public boolean isRankDiffBigEnough() {
		  return Math.min(getRank() - rightSon.getRank(), getRank()- leftSon.getRank()) >= 1;
	  }
	  
	  public boolean isRankDiffSmallEnough() {
		  return Math.max(getRank()-rightSon.getRank(), getRank()-leftSon.getRank()) <= 2;
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
		this.leftSon.setParent(this);
	}

	public AbsWAVLNode getRightSon() {
		return rightSon;
	}

	public void setRightSon(AbsWAVLNode temp) {
		this.rightSon = temp;
		this.rightSon.setParent(this);
	}

		@Override
	public String toString() {
		return key+":"+value;
	}

		public boolean isLeaf(){
			   if (getLeftSon() instanceof WAVLExternalNode && getRightSon() instanceof WAVLExternalNode)
				   return true;
			   return false;
		   }

		public boolean isLeftUnary(){
			   if (!(getLeftSon() instanceof WAVLExternalNode) && getRightSon() instanceof WAVLExternalNode)
				   return true;
			   return false;
		   }

		public boolean isRightUnary(){
			   if ((getLeftSon() instanceof WAVLExternalNode) && !(getRightSon() instanceof WAVLExternalNode))
				   return true;
			   return false;
		   }


  
  }
  
  public static class WAVLExternalNode extends AbsWAVLNode{
	  public WAVLExternalNode (){
		  this.setRank(-1);
	  }

		@Override
	public String toString() {
		return null;
	}
  }

  
}
  

