package no.hild1.bank.test;

import static org.junit.Assert.*;

import no.hild1.bank.MOD10;
import no.hild1.bank.MOD11;

import org.junit.Test;

/**
 * @author hildenae
 *
 */
public class MODTest {
	@Test
	public void calculateCD() {
		assertEquals("2", MOD10.calculateCD("12345678"));
		assertEquals("6", MOD10.calculateCD("234567"));
		assertEquals("8", MOD10.calculateCD("0411781"));
		assertEquals("8", MOD10.calculateCD("411781"));
		assertEquals("4", MOD10.calculateCD("457931"));
		assertEquals("7", MOD10.calculateCD("45793"));
		assertEquals("8", MOD10.calculateCD("0449900015333"));
		assertEquals("2", MOD10.calculateCD("002258290"));
		assertEquals("4", MOD10.calculateCD("002356548"));
		
		assertEquals("5", MOD11.calculateCD("12345678"));
		assertEquals("6", MOD11.calculateCD("234567"));
		assertEquals("-", MOD11.calculateCD("23456711"));
		
	}
	
	@Test
	public void checkCD() {
		assertTrue(MOD10.checkCD("123456782"));
		assertTrue(MOD10.checkCD("2345676"));
		assertTrue(MOD10.checkCD("04117818"));
		assertTrue(MOD10.checkCD("4117818"));
		assertTrue(MOD10.checkCD("4579314"));
		assertTrue(MOD10.checkCD("457937"));
		assertTrue(MOD10.checkCD("04499000153338"));
		assertTrue(MOD10.checkCD("0022582902"));
		assertTrue(MOD10.checkCD("0023565484"));
		
		assertTrue(MOD11.checkCD("123456785"));
		assertTrue(MOD11.checkCD("2345676"));
		assertTrue(MOD11.checkCD("23456711-"));
		
		assertFalse(MOD10.checkCD("0023565480"));
		assertFalse(MOD10.checkCD("0023565481"));
		assertFalse(MOD10.checkCD("0023565482"));
		assertFalse(MOD10.checkCD("0023565483"));
		assertFalse(MOD10.checkCD("0023565485"));
		assertFalse(MOD10.checkCD("0023565486"));
		assertFalse(MOD10.checkCD("0023565487"));
		assertFalse(MOD10.checkCD("0023565488"));
		assertFalse(MOD10.checkCD("0023565489"));
		
	}
}
