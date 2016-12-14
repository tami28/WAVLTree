
public class Tests {

	public static void main(String[] args){
		testInsert();
	}
	
	
	public static void testInsert(){
		WAVLTree tree = new WAVLTree();
		//case left:lefts
		System.out.println(tree.insert(10, "10"));
		System.out.println(tree.insert(5,"5"));
		System.out.println(tree.insert(1, "1"));
		tree.breadthPrint();
		//Should be: 5,10,1
		
		//case right:left
		System.out.println(tree.insert(4, "4"));
		tree.breadthPrint();
		System.out.println(tree.insert(3, "3"));
		tree.breadthPrint();
		//Should be: 5,3,10,1,4
		
	}
}
