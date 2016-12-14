
public class Tests {

	public static void main(String[] args){
		testInsert();
		testKeysToArray();
		testAnotherTreeInsertion();
	}
	
	
	public static void testInsert(){
		WAVLTree tree = new WAVLTree();
		//case left:left
		System.out.println(tree.insert(10, "10"));//should be 0
		System.out.println(tree.insert(7,"7"));//should be 0
		System.out.println(tree.insert(1, "1")); //should be 1
		System.out.println(tree.toString());
		//Should be: 7,10,1
		
		//case right:left
		System.out.println(tree.insert(4, "4"));//should be 0
		System.out.println(tree.insert(3, "3"));//should be 2
		System.out.println(tree.toString());
		//Should be: 7,3,10,1,4
		
		//case right:right
		System.out.println(tree.insert(11, "11"));//should be 0
		System.out.println(tree.insert(12, "12"));//should be 1
		System.out.println(tree.toString());
		//Should be: 7,3,11,1,4,10,12
		
		//case left:tight
		System.out.println(tree.insert(14, "14"));//should be 0
		System.out.println(tree.insert(13, "13"));//should be 2
		System.out.println(tree.toString());
		//Should be 7, 3, 11, 1, 4, 10, 13, 12, 14
		
	}
	
	public static void testKeysToArray(){
		WAVLTree tree = new WAVLTree();
		tree.insert(10, "10");
		tree.insert(7, "7");
		tree.insert(1, "1");
		tree.insert(4, "14");
		tree.insert(3, "3");
		tree.insert(11, "11");
		tree.insert(12, "12");
		tree.insert(14, "14");
		tree.insert(13, "13");
		int[] arr = tree.keysToArray();
		StringBuffer arrStr = new StringBuffer();
		for(int i=0; i<arr.length; i++){
			arrStr.append(arr[i] + " ");
		}
		System.out.println(arrStr);
	}

	public static void testAnotherTreeInsertion(){
		WAVLTree tree = new WAVLTree();
		tree.insert(10, "10");
		tree.insert(5, "5");
		tree.insert(15, "15");
		tree.insert(0, "0");
		tree.insert(20, "20");
		System.out.println(tree.toString());
		tree.insert(-5, "-5");
		System.out.println(tree.toString());
	}
}
