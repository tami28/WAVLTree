import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.Before;
import org.junit.Test;

public class WAVLTreeTests {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testBasicInsertRotations() {
		WAVLTree tree = new WAVLTree();
		//case left:left
		assertEquals(0,tree.insert(10, "10"));
		assertEquals(1, tree.insert(7,"7"));
		assertEquals(3, tree.insert(1, "1"));
		assertEquals(tree.toString(), "7:7 1:1 10:10 ");

		
		//case right:left
		assertEquals(2, tree.insert(4, "4"));
		assertEquals(4, tree.insert(3, "3"));
		assertEquals("7:7 3:3 10:10 1:1 4:4 ",tree.toString());
		
		//case right:right
		assertEquals(1, tree.insert(11, "11"));
		assertEquals(3, tree.insert(12, "12"));
		assertEquals("7:7 3:3 11:11 1:1 4:4 10:10 12:12 ", tree.toString());
		
		//case left:right
		assertEquals(3, tree.insert(14, "14"));
		assertEquals(4, tree.insert(13, "13"));
		assertEquals("7:7 3:3 11:11 1:1 4:4 10:10 13:13 12:12 14:14 ",tree.toString());
		
		//another order creating a 2-2 node in insert:
		tree = new WAVLTree();
		assertEquals(0,tree.insert(10, "10"));
		assertEquals(1, tree.insert(5, "5"));
		assertEquals(0,tree.insert(15, "15"));
		assertEquals(2,tree.insert(0, "0"));
		assertEquals(1, tree.insert(20, "20"));
		assertEquals("10:10 5:5 15:15 0:0 20:20 ",tree.toString());
		assertEquals(3, tree.insert(-5, "-5"));
		assertEquals("10:10 0:0 15:15 -5:-5 5:5 20:20 ",tree.toString());
	}
	
	@Test
	public void testGetKeyArray(){
		WAVLTree tree = new WAVLTree();
		int[] arr = new int[300000];
		int count = 0;
		int j =0;
		for (int i=0; i<100000; i++){
			int rand = ThreadLocalRandom.current().nextInt(-100000000, 100000000 + 1);
			int num = tree.insert(rand, "test");
			if (num != -1){
				count +=num;
				arr[j] = rand;
				j++;
			}
			
		}
		int[] keyArr = tree.keysToArray();
		int[] improvedArr = Arrays.copyOf(arr, j);
		Arrays.sort(improvedArr);
		for(int i=0; i<keyArr.length; i++){
			assertEquals(improvedArr[i],keyArr[i]);
		}
		System.out.println(count);
	}
	
	@Test
	public void testGetInfoArray(){
		WAVLTree tree = new WAVLTree();
		String[] arr = new String[100000];
		int count = 0;
		int j =0;
		for (int i=0; i<100000; i++){
			int rand = ThreadLocalRandom.current().nextInt(-100000000, 100000000 + 1);
			int num = tree.insert(rand, Integer.toString(rand));
			if (num != -1){
				count +=num;
				arr[j] = Integer.toString(rand);
				j++;
			}
			
		}
		String[] infoArr = tree.infoToArray();
		String[] improvedArr = Arrays.copyOf(arr, j);
		Arrays.sort(improvedArr, new Comparator<String>(){
			@Override
			public int compare(String s1, String s2) {
				return Integer.compare(Integer.parseInt((String) s1), Integer.parseInt((String) s2));
			}
		});
		for(int i=0; i<infoArr.length; i++){
			assertEquals(improvedArr[i],infoArr[i]);
		}
		System.out.println(count);
	}

	@Test
	public void testSize(){
		ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i=0; i<10000; i++) {
            list.add(new Integer(i));
        }
        Collections.shuffle(list);
        
        WAVLTree tree = new WAVLTree();
        for (int i=0; i<10000; i++){
        	tree.insert(list.get(i), "test");
        }
        assertEquals(10000, tree.size());
        
	}

	@Test
	public void testMinMaxAfterActions(){
		WAVLTree tree = new WAVLTree();
		String[] arr = new String[100000];
		int j =0;
		for (int i=0; i<10000; i++){
			int rand = ThreadLocalRandom.current().nextInt(-100000, 100000 + 1);
			@SuppressWarnings("unused")
			int num = tree.insert(rand, Integer.toString(rand));
			if (num != -1){
				arr[j] = Integer.toString(rand);
				j++;
			}
			
		}
		arr = Arrays.copyOf(arr, j);
		Arrays.sort(arr, new Comparator<String>(){
			@Override
			public int compare(String s1, String s2) {
				return Integer.compare(Integer.parseInt((String) s1), Integer.parseInt((String) s2));
			}
		});
		assertEquals(arr[0], tree.min());
		assertEquals(arr[arr.length-1], tree.max());
	}

	@Test
	public void testRanksValidityAfterInsesrtions(){
		WAVLTree tree = new WAVLTree();
		for (int i=0; i<100000; i++){
			int rand = ThreadLocalRandom.current().nextInt(-100000000, 100000000 + 1);
			tree.insert(rand, Integer.toString(rand));
		}
		assertTrue(isValidRank(tree.getRoot()));
		
	}
	
	private boolean isValidRank(WAVLTree.WAVLNode node){
		boolean valid = node.isValidRankDiff();
		if(node.getRightSon() instanceof WAVLTree.WAVLNode){
			valid = isValidRank((WAVLTree.WAVLNode) node.getRightSon()) && valid;
		}
		if (node.getLeftSon() instanceof WAVLTree.WAVLNode){
			valid =  isValidRank((WAVLTree.WAVLNode)node.getLeftSon()) && valid;
		}
		return valid;
	}
}
