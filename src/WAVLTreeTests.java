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
		assertTrue(tree.isValidTree());
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
		assertEquals(tree.height(), tree.getRoot().getRank());
		assertEquals(-1, tree.insert(-5, "-5"));
		assertTrue(tree.isValidTree());
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
		assertTrue(tree.isValidTree());
		
	}
	

	@Test
	public void rankEqHeightAfterInsertionsOnly(){
		WAVLTree tree = new WAVLTree();
		for (int i=0; i<100000; i++){
			int rand = ThreadLocalRandom.current().nextInt(-100000000, 100000000 + 1);
			tree.insert(rand, Integer.toString(rand));
		}
		assertEquals(tree.height(), tree.getRoot().getRank());
		assertTrue(tree.isValidTree());
	}
	


	@Test
	public void insertDeleteActions(){
		ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i=0; i<10000; i++) {
            list.add(new Integer(i));
        }
        Collections.shuffle(list);
        
        WAVLTree tree = new WAVLTree();
        int maxActions = 100;
        int counter = 0;
        ArrayList<Integer> added = new ArrayList<Integer>();
        //insert k items:
        while (maxActions >0){
        	int k = ThreadLocalRandom.current().nextInt(0, maxActions + 1);
        	maxActions = maxActions -k;
        	//insertions
        	System.out.println("inserting " + k + "items:");
        	for (int i=0; i<k; i++){
        		counter += tree.insert(list.get(i), Integer.toString(list.get(i))); //We don't have double cases here, different test
        		added.add(list.get(i));
        	}
        	//deletions
        	k = ThreadLocalRandom.current().nextInt(0,Math.min(added.size(), maxActions)+ 1);
        	System.out.println("deleting " + k + "items:");
        	maxActions = maxActions -k;
        	Collections.shuffle(added);
        	for (int i=0; i<k; i++){
        		counter += tree.delete(added.get(i));
        		added.remove(i);
        		
        	}
        	
        	
        }
        System.out.println(counter);
        assertTrue(tree.isValidTree());
	}
}
