package com.oddsocks.gopig;

import java.io.IOException;

/**
 * Main entry point for executable JAR file
 * 
 */
public class GoPig {
	public static void main(String[] args) {
		try {
			new GopigoCommanderTest();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			System.out.print(e);
		}
	}
}
