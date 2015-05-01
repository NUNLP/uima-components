package org.northshore.cbri.mapper;

import static org.junit.Assert.*

import org.junit.Test

import com.wcohen.ss.JaroWinklerTFIDF
import com.wcohen.ss.Levenstein

class StringMatchTest {

	@Test
	public void test() {
		JaroWinklerTFIDF.main("New York", "New Yrk")
		Levenstein.main("york", "YORK")
	}

}
