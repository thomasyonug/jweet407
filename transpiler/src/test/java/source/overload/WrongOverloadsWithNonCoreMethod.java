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

import def.js.Array;

public interface WrongOverloadsWithNonCoreMethod {

	static Array<String> trace = new Array<String>();

	public static void main(String[] args) {
		WrongOverloadsWithNonCoreMethod i = new SubClass2();
		i.draw();
		i.m(new double[0]);
		i.m(new float[0]);
		$export("trace", trace.join(","));
	}

	void draw();
	
	void m(double[] a);

	void m(float[] a);
	
}

class SubClass2 implements WrongOverloadsWithNonCoreMethod {

	public void draw() {
		trace.push("draw0");
		draw("draw1");
	}

	public void draw(String s) {
		trace.push(s);
	}

	@Override
	public void m(double[] a) {
		trace.push("double1");
	}

	@Override
	public void m(float[] a) {
		trace.push("float1");
	}
	
}

class AbstractClass1 implements WrongOverloadsWithNonCoreMethod {

	public void draw() {
		trace.push("draw0");
	}

	@Override
	public void m(double[] a) {
		trace.push("double2");
	}

	@Override
	public void m(float[] a) {
		trace.push("float2");
	}
	
}

class SubClass4 extends AbstractClass1 {

	public void draw(String s) {
		trace.push(s);
	}

}

