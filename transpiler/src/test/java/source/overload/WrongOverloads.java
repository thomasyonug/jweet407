/* 
 * JSweet - http://www.jsweet.org
 * Copyright (C) 2015 CINCHEO SAS <renaud.pawlak@cincheo.fr>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package source.overload;

import static jsweet.util.Lang.$export;
import static jsweet.util.Lang.array;

class SuperClass {

	public SuperClass() {
		array(WrongOverloads.trace).push("1");
	}

	public SuperClass(int i) {
		array(WrongOverloads.trace).push("2");
	}

}

public class WrongOverloads extends SuperClass {

	public static String[] trace = {};

	public static void main(String[] args) {

		new WrongOverloads();
		new WrongOverloads("test");
		new WrongOverloads("test5", 0);
		new WrongOverloads(true);
		$export("trace", trace);
	}

	private void draw() {
		System.out.println("tutu");
	}

	private void draw(String s) {
	}

	public void draw(String s, Object o) {
	}

	public void draw(String s, Integer i) {
	}
	
	int i;

	String getter() {
		return "";
	}

	void m() {
		// other statements are not allowed in overloading
		i = 0;
		this.m("");
	}

	void m(String s) {
		// calling a method is wrong for overloading
		this.m(getter(), 1);
	}

	// this method cannot overload the one with string
	void m(int i) {
	}

	void m(String s, int i) {
		s = "";
	}

	public WrongOverloads() {
		array(WrongOverloads.trace).push("5");
		draw();
		draw("");
	}

	public WrongOverloads(String s) {
		super(1);
		array(WrongOverloads.trace).push("3");
		System.out.println(s);
	}

	public WrongOverloads(String s5, int i2) {
		super(0);
		String s = "tutu";
		array(WrongOverloads.trace).push("4");
		array(WrongOverloads.trace).push(s5);
		array(WrongOverloads.trace).push(s);
		System.out.println(s);
	}

	public WrongOverloads(boolean b) {
		this("1", 0);
		array(WrongOverloads.trace).push("6");
	}

	public WrongOverloads(String s, int i, String s2) {
		array(WrongOverloads.trace).push("10");
		System.out.println(s);
	}

	int test(int x, int y) {
		return x + y;
	}

	int test(Data d) {
		return test(d.x, d.y);
	}

}

class Data {
	int x;
	int y;
}

class CharSequenceAdapter {
	private char[] charArray;
	private int start;
	private int end;

	public CharSequenceAdapter(char[] charArray) {
		this(charArray, 0, charArray.length);
	}

	public CharSequenceAdapter(char[] charArray, int start, int end) {
		this.charArray = charArray;
		this.start = start;
		this.end = end;
	}

	public char charAt(int index) {
		return charArray[index + start];
	}

	public int length() {
		return end - start;
	}

	public CharSequenceAdapter subSequence(int start, int end) {
		return new CharSequenceAdapter(charArray, this.start + start, this.start + end);
	}
}
