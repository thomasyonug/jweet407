package source.assertion;

import static jsweet.util.Lang.$export;

import def.js.Error;

public class FailingAssertions {

	public static void main(String[] args) {
		int i1 = 1;
		int i2 = 1;

		try {
			assert i1 + i2 == 3;
			$export("assertion1", true);
		} catch (Error e) {
		}

		try {
			assert i1 + i2 == 3;
		} catch (Error e) {
			$export("assertion2", false);
		}
	}

}
