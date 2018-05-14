package com.williamhaw.compression;

import com.williamhaw.compression.execution.GZIPCompressionExecutor;
import com.williamhaw.compression.execution.GZIPDecompressionExecutor;
import com.williamhaw.compression.utils.Validation;

/**
 * Entry point for application
 * @author williamhaw
 *
 */
public class Main {

	public static void main(String[] args) {
		
		if(args.length == 3) {//compression
			if(Validation.isValidPath(args[0]) == false || Validation.isValidPath(args[1]) == false) {
				System.err.println("Invalid path specified");
				System.exit(1);
			}else if(Validation.isInteger(args[2]) == false) {
				System.err.println("Size in MB is not an integer");
				System.exit(1);
			}else {
				try {
					GZIPCompressionExecutor exec = new GZIPCompressionExecutor();
					exec.executeCompression(args[0], args[1], Integer.valueOf(args[2]));
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		}else if(args.length == 2) {//decompression
			if(Validation.isValidPath(args[0]) == false || Validation.isValidPath(args[1]) == false) {
				System.err.println("Invalid path specified");
				System.exit(1);
			}else {
				try {
					GZIPDecompressionExecutor exec = new GZIPDecompressionExecutor();
					exec.executeDecompression(args[0], args[1]);
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		}else {
			System.err.println("Wrong number of parameters given. Refer to usage in README.md");
			System.exit(1);
		}

	}

}
