import java.io.InputStreamReader;
import java_cup.runtime.Symbol;

/*
  Created by: Fei Song
  Modified by: Alireza Sharif & Pedram Mirmiran
  File Name: Scanner.java
  * This class serves as a wrapper for the Lexer class, providing a simple
 * interface to retrieve tokens one at a time from the input stream.

*/

public class Scanner {
  private Lexer scanner = null;

  public Scanner( Lexer lexer ) {
    scanner = lexer; 
  }

  public Symbol getNextToken() throws java.io.IOException {
    return scanner.next_token();
  }

  public static void main(String argv[]) {
    try {
      Scanner scanner = new Scanner(new Lexer(new InputStreamReader(System.in)));
      Symbol tok = null;
      while( (tok=scanner.getNextToken()) != null )
        System.out.println(sym.terminalNames[tok.sym]);
    }
    catch (Exception e) {
      System.out.println("Unexpected exception:");
      e.printStackTrace();
    }
  }
}
