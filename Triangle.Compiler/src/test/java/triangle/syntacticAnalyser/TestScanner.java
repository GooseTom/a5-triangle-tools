package triangle.syntacticAnalyser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.function.ThrowingRunnable;

import triangle.ErrorReporter;
import triangle.syntacticAnalyzer.Parser;
import triangle.syntacticAnalyzer.Scanner;
import triangle.syntacticAnalyzer.SourceFile;

public class TestScanner {
	
	/* some individual unit tests for helper methods in Scanner */

	@Test
	public void testIsDigit() {
		assertTrue(Scanner.isDigit('0'));
		assertTrue(Scanner.isDigit('1'));
		assertTrue(Scanner.isDigit('5'));
		assertTrue(Scanner.isDigit('8'));
		assertTrue(Scanner.isDigit('9'));
		assertFalse(Scanner.isDigit('a'));
		assertFalse(Scanner.isDigit('Z'));
		assertFalse(Scanner.isDigit('&'));
		assertFalse(Scanner.isDigit(';'));
		assertFalse(Scanner.isDigit('\n'));
	}
	
	@Test
	public void testIsOperator() {
	    
		// test for valid operators
	    assertTrue(Scanner.isOperator('+'));
	    assertTrue(Scanner.isOperator('-'));
	    assertTrue(Scanner.isOperator('*'));
	    assertTrue(Scanner.isOperator('/'));
	    assertTrue(Scanner.isOperator('?'));
	    assertTrue(Scanner.isOperator('='));
	    assertTrue(Scanner.isOperator('%'));
	    assertTrue(Scanner.isOperator('^'));
	    assertTrue(Scanner.isOperator('&'));
	    assertTrue(Scanner.isOperator('<'));
	    assertTrue(Scanner.isOperator('>'));

	    // test for invalid operators
	    assertFalse(Scanner.isOperator('!'));
	    assertFalse(Scanner.isOperator('#'));
	    assertFalse(Scanner.isOperator(' '));
	    assertFalse(Scanner.isOperator('1'));
	    assertFalse(Scanner.isOperator('\n'));
	    assertFalse(Scanner.isOperator('z'));
	}

	@Test
	public void testIsLetter() {
	    
		// test edge cases for letters
	    assertTrue(Scanner.isLetter('a'));
	    assertTrue(Scanner.isLetter('z'));
	    assertTrue(Scanner.isLetter('A'));
	    assertTrue(Scanner.isLetter('Z'));

	    // test for non-letters
	    assertFalse(Scanner.isLetter('1'));
	    assertFalse(Scanner.isLetter('&'));
	    assertFalse(Scanner.isLetter('#'));
	    assertFalse(Scanner.isLetter(' '));
	    assertFalse(Scanner.isLetter('\n'));
	}
	
	
	/* these tests all try to compile example programs... */
	
	@Test
	public void testHi() {
		compileExpectSuccess("/hi.tri");
	}
	

	@Test
	public void testHiNewComment() {
		compileExpectFailure("/hi-newcomment.tri");
	}
	

	@Test
	public void testHiNewComment2() {
		compileExpectFailure("/hi-newcomment2.tri");
	}
	

	@Test
	public void testBarDemo() {
		compileExpectFailure("/bardemo.tri");
	}
	

	@Test
	public void testRepeatUntil() {
		compileExpectFailure("/repeatuntil.tri");
	}
	
	
	
	private void compileExpectSuccess(String filename) {
		// build.gradle has a line sourceSets.test.resources.srcDir file("$rootDir/programs")
		// which adds the programs directory to the list of places Java can easily find files
		// getResource() below searches for a file, which is in /programs 
		//SourceFile source = SourceFile.ofPath(this.getClass().getResource(filename).getFile().toString());
		SourceFile source = SourceFile.fromResource(filename);
		
		Scanner scanner = new Scanner(source);
		ErrorReporter reporter = new ErrorReporter(true);
		Parser parser = new Parser(scanner, reporter);
		
		parser.parseProgram();
		
		// we should get to here with no exceptions
		
		assertEquals("Problem compiling " + filename, 0, reporter.getNumErrors());
	}
	
	private void compileExpectFailure(String filename) {
		//SourceFile source = SourceFile.ofPath(this.getClass().getResource(filename).getFile().toString());
		SourceFile source = SourceFile.fromResource(filename);
		Scanner scanner = new Scanner(source);
		ErrorReporter reporter = new ErrorReporter(true);
		Parser parser = new Parser(scanner, reporter);

		// we expect an exception here as the program has invalid syntax
		assertThrows(RuntimeException.class, new ThrowingRunnable() {
			public void run(){
				parser.parseProgram();
			}
		});
		
		// currently this program will fail
		assertNotEquals("Problem compiling " + filename, 0, reporter.getNumErrors());
	}

}
