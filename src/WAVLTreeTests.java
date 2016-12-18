import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
		//Should be: 7,3,10,1,4
		
		//case right:right
		assertEquals(1, tree.insert(11, "11"));
		assertEquals(3, tree.insert(12, "12"));
		assertEquals("7:7 3:3 11:11 1:1 4:4 10:10 12:12 ", tree.toString());
		//Should be: 7,3,11,1,4,10,12
		
		//case left:right
		assertEquals(3, tree.insert(14, "14"));//should be 0
		assertEquals(6, tree.insert(13, "13"));//should be 2
		assertEquals("7:7 3:3 11:11 1:1 4:4 10:10 13:13 12:12 14:14 ",tree.toString());
		//Should be 7, 3, 11, 1, 4, 10, 13, 12, 14
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
}
