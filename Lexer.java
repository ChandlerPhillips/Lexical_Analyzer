//Chandler Phillips
//Donovan Cadena De La Rosa
//CSC 415 Assignment 4 - Lexer

import java.io.*;

public class Lexer {
	
	private BufferedReader reader; //read input files
	private char curr; // current character being scanned
	private static final char EOF = (char) (-1); // end of file character

	public Lexer(String file) {
		try {
			reader = new BufferedReader(new FileReader(file));
		} catch (Exception e) {
			e.printStackTrace();
		}
		//starts here
		curr = read();
	}

	private char read() {
		try {
			return (char) (reader.read());
		} catch (IOException e) {
			e.printStackTrace();
			return EOF;
		}
	}

	//checks character for a digit value
	private boolean isNum(char c) {
		if (c >= '0' && c <= '9')
			return true;
		return false;
	}

	//checks  character for a letter (uppercase and lowercase)
	public boolean isLetter(char c){
		if(c>='a' && c<='z' )
			return true;
		if(c>='A' && c<='Z' )
			return true;	
		return false;
	}

	public Token lex() {
		int state = 1; //initial state
		int numValues = 0; //buffer for number literals
		String stringValues = ""; //buffer for string literals
		int decValues=0;
		boolean skipped = false;
		while (true) {
			if (curr == EOF && !skipped) {
				skipped = true;       
			}else if (skipped) {        
				try {  
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}             
				return null;
			}	
			switch (state) {
				// Controller
				case 1:
				switch (curr) {
					case ' ': //checks for whitespaces
					case '\n':
					case '\b':
					case '\f':
					case '\r':
					case '\t':	
					curr = read();
					continue;
					
					case '+': //checks for +
					curr = read();
					return new Token("+", "add/concatenate");
					
					case '-': //checks for -
					curr = read();
					return new Token("-", "negation");
					
					case '{': //checks for {
						curr = read();
						state = 9;
						stringValues="";
						continue; //if true, continue to state 9
					
						case'(': //checks for (
						curr=read();	
						return new Token("(","open parenthesis");
					
						case')': //checks for )
						curr=read();	
						return new Token(")","closed parenthesis");
					
						case':': //checks for :
						curr=read();	
						return new Token(":","colon");
					
						case'>': //checks for >
						curr=read();	
						return new Token(">","greater than");
					
						case'<': //checks for <
						curr=read();	
						return new Token("<","less than");
					
						case'=': //checks for =
						curr=read();	
						return new Token("=", "assignment");
					
						case'!': //checks for !
						curr=read();
						return new Token("!", "not equals to");
					
						case'?': //checks for ?
						curr=read();
						return new Token("?", "equals to");
						 
						case '"': //checks for string literals. if true, continue to state 8
						curr=read();
						state=8;
						stringValues="";
						continue;
                             
						default:
						state = 2; //checks the next possibility
						continue;
					}

					//integer - start
					case 2:
					if (isNum(curr)) {
						numValues = 0; //reset the buffer.
						numValues += (curr - '0');
						state = 3;            
						curr = read();             
					} else {
						state=5; //does not start with number or symbol go to case 5
					}
					continue;

					//integer - body
					case 3:
					if (isNum(curr)) {
						numValues *= 10;
						numValues += (curr - '0');
						curr = read();                
					}else if(curr=='.'){
						curr = read();	        
						state=4; //has decimal point go to case 4               
					} else {
						return new Token("" + numValues,"number literal");
					}            
					continue;
				
					//decimal-start	
					case 4:
					if (isNum(curr)) {
						decValues = 0;
						decValues += (curr - '0');
						state=7;					
						curr = read();	                
					}else  {
						return new Token(numValues+".", "ERROR");
					}
					continue;

					//identifier-start
					case 5:
					if(isLetter(curr)|| curr=='_'){
						stringValues = "";					
						stringValues+=curr;
						state=6;
						curr = read();
					}else {
						stringValues = "";					
						stringValues+=curr;
						curr=read();
						return new Token(stringValues,"ERROR");
					}
					continue;	
				
					//keyword-start
					case 6:	
					if ((isLetter(curr) || isNum(curr) || curr=='_')) {
						stringValues += curr;
						curr = read();
					} else {           
						if( stringValues.equals("text")||
							stringValues.equals("if")||
								stringValues.equals("loop")   
						){
							return new Token("" + stringValues, "keyword");         
						}     
						return new Token("" + stringValues, "identifier");
					}
					continue;
				
					//decimal body
					case 7:
					if (isNum(curr)) {
						decValues *= 10;
						decValues += (curr - '0');
						curr = read();
					} else {
						return new Token("" + numValues+"."+decValues, "number literal");
					}
					continue;	
                
					//checks for string literals        
					case 8:
					if(curr=='"'){
						curr=read();
						return new Token("\""+String.format(stringValues,"\n") +"\"","string literal");
					}
					else{
						stringValues += curr;
						curr = read();
					}
					continue;
                         
					//checks for comments		 
					case 9:
					if(curr=='}'){
						curr=read();
						return new Token("{"+String.format(stringValues,"\n")+"}","comment");
					}
					else{
						stringValues += curr;
						curr = read();
					}
					continue;
				}
			}
		}
	}
	
	/*	References:
	Concepts of Programming Languages Book 11th edition
	https://stackoverflow.com/questions/17848207/making-a-lexical-analyzer
	https://codereview.stackexchange.com/questions/138725/yet-another-lexical-analyser
	http://voidexception.weebly.com/lexical-analyzer.html
	https://dzone.com/articles/java-string-format-examples
	*/
		
		