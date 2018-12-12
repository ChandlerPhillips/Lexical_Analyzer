//Chandler Phillips
//Donovan Cadena De La Rosa
//CSC 415 Assignment 4 - Lexer

import java.util.*;
import java.io.*;

public class Token {
	private String token; // type of of token
	private String lexeme; // the lexeme

	public Token(String token, String lexeme) {
		this.token = token;
		this.lexeme = lexeme;
	}

	//returns the type of the token
	public String getToken() {
		return token;
	}

	//returns the lexeme of the token
	public String getLexeme() {
		return lexeme;
	}

	//returns a string representation of the token
	public String toString() {
		 return String.format("%-23s %-15s",token,lexeme);
	}
	
	public static void main(String[] args) {
		System.out.println("\n----------------------------------------------");
		System.out.println("Lexemes: \t\tToken description:");
		System.out.println("----------------------------------------------");
		String inFile;
		if (args.length > 0) {
			inFile = args[0];
			Lexer lexer = new Lexer(inFile);
			Token t;   
			while ((t = lexer.lex()) != null) {
				System.out.println(t.toString());
			}
		}
		System.out.println("\nCOMPLETE");
	}
}