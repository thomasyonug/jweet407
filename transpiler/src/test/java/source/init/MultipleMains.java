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
package source.init;

import static jsweet.util.Lang.$export;

import jsweet.lang.Disabled;

public class MultipleMains {
	public static int counter = 0;

	public static void main(String[] args) {
		$export("a", MultipleMains.counter++);
	}
}

class B {
	@Disabled
	public static void main(String[] args) {
		$export("b", MultipleMains.counter++);
	}
}

class C {
	public static void main(String[] args) {
		$export("c", MultipleMains.counter++);
	}
}
