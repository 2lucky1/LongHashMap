package de.comparus.opensource.longmap;

import java.util.Arrays;
import java.util.HashSet;

public class Main {

	public static void main(String[] args) {
//		int i = Integer.MAX_VALUE;
//int b = i - 10000;
//int c = b*2;
//int d = i*2;
//System.out.println(b + " - b");
//System.out.println(c + " - c");
//System.out.println(d + " - d");
//System.out.println(Integer.MAX_VALUE + " = MaxValue");
//System.out.println(10<<8);
		
		LongMapImpl<String> map = new LongMapImpl<>(16,true);
		HashSet<Long> set = new HashSet<>();
		for(int i = 0; i < 250; i++) {
			map.put(i, "s");
		}
		
		for(int i = 0; i < 10; i++) {
			map.put(i, "f");
		}
		
		for(long e:map.keys()) {
			set.add(e);
		}
		
		
		System.out.println(map.size());
		System.out.println("Keys: " + Arrays.toString(map.keys()));
		System.out.println("Values: " + Arrays.toString(map.values()));
		System.out.println("set size: " + set.size());
		
		set.clear();
		for(int i = 0; i < 10; i++) {
			map.remove(i);
		}
		
		for(long e:map.keys()) {
			set.add(e);
		}
		
		System.out.println(map.size());
		System.out.println("Keys: " + Arrays.toString(map.keys()));
		System.out.println("Values: " + Arrays.toString(map.values()));
		System.out.println("set size: " + set.size());
	}

}
