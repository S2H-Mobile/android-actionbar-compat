/*
 * Copyright (C) 2012 - 2013, S2H Mobile
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

package de.s2hmobile.tools;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StringHelper {
	
	private StringHelper() {}
		
	public static String formatDate(String template, long time){
		SimpleDateFormat sdf = new SimpleDateFormat(template, Locale.getDefault());
		return sdf.format(new Date(time));
	}
	
	/**
	 * Checks if a string is null, empty or blank.
	 * @param str the string to test
	 * @return true if str is null, empty, or blank
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.trim().length() == 0;
		// str.isEmpty() can be used with Java 6 and Android API Level 9
	}
}
