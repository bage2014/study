package com.bage.study.cryption.pbkdf;
	import java.security.SecureRandom;
	import javax.crypto.spec.PBEKeySpec;
	import javax.crypto.SecretKeyFactory;
	import java.math.BigInteger;
	import java.security.NoSuchAlgorithmException;
	import java.security.spec.InvalidKeySpecException;
	/**
	 * PBKDF2 salted password hashing.
	 * Author: havoc AT defuse.ca
	 * www: http://crackstation.net/hashing-security.htm
	 */
	public class Main
	{
	    public static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1";


	    // The following constants may be changed without breaking existing hashes.
	    public static final int SALT_BYTE_SIZE = 24;
	    public static final int HASH_BYTE_SIZE = 24;
	    public static final int PBKDF2_ITERATIONS = 1000;


	    public static final int ITERATION_INDEX = 0;
	    public static final int SALT_INDEX = 1;
	    public static final int PBKDF2_INDEX = 2;


	    /**
	     * Returns a salted PBKDF2 hash of the password.
	     *
	     * @param   password    the password to hash
	     * @return              a salted PBKDF2 hash of the password
	     */
	    public static String createHash(String password)
	        throws NoSuchAlgorithmException, InvalidKeySpecException
	    {
	        return createHash(password.toCharArray());
	    }


	    /**
	     * Returns a salted PBKDF2 hash of the password.
	     *
	     * @param   password    the password to hash
	     * @return              a salted PBKDF2 hash of the password
	     */
	    public static String createHash(char[] password)
	        throws NoSuchAlgorithmException, InvalidKeySpecException
	    {
	        // Generate a random salt
	        SecureRandom random = new SecureRandom();
	        byte[] salt = new byte[SALT_BYTE_SIZE];
	        random.nextBytes(salt);


	        // Hash the password
	        byte[] hash = pbkdf2(password, salt, PBKDF2_ITERATIONS, HASH_BYTE_SIZE);
	        // format iterations:salt:hash
	        return PBKDF2_ITERATIONS + ":" + toHex(salt) + ":" +  toHex(hash);
	    }


	    /**
	     * Validates a password using a hash.
	     *
	     * @param   password        the password to check
	     * @param   correctHash     the hash of the valid password
	     * @return                  true if the password is correct, false if not
	     */
	    public static boolean validatePassword(String password, String correctHash)
	        throws NoSuchAlgorithmException, InvalidKeySpecException
	    {
	        return validatePassword(password.toCharArray(), correctHash);
	    }


	    /**
	     * Validates a password using a hash.
	     *
	     * @param   password        the password to check
	     * @param   correctHash     the hash of the valid password
	     * @return                  true if the password is correct, false if not
	     */
	    public static boolean validatePassword(char[] password, String correctHash)
	        throws NoSuchAlgorithmException, InvalidKeySpecException
	    {
	        // Decode the hash into its parameters
	        String[] params = correctHash.split(":");
	        int iterations = Integer.parseInt(params[ITERATION_INDEX]);
	        byte[] salt = fromHex(params[SALT_INDEX]);
	        byte[] hash = fromHex(params[PBKDF2_INDEX]);
	        // Compute the hash of the provided password, using the same salt, 
	        // iteration count, and hash length
	        byte[] testHash = pbkdf2(password, salt, iterations, hash.length);
	        // Compare the hashes in constant time. The password is correct if
	        // both hashes match.
	        return slowEquals(hash, testHash);
	    }


	    /**
	     * Compares two byte arrays in length-constant time. This comparison method
	     * is used so that password hashes cannot be extracted from an on-line 
	     * system using a timing attack and then attacked off-line.
	     * 
	     * @param   a       the first byte array
	     * @param   b       the second byte array 
	     * @return          true if both byte arrays are the same, false if not
	     */
	    private static boolean slowEquals(byte[] a, byte[] b)
	    {
	        int diff = a.length ^ b.length;
	        for(int i = 0; i < a.length && i < b.length; i++)
	            diff |= a[i] ^ b[i];
	        return diff == 0;
	    }


	    /**
	     *  Computes the PBKDF2 hash of a password.
	     *
	     * @param   password    the password to hash.
	     * @param   salt        the salt
	     * @param   iterations  the iteration count (slowness factor)
	     * @param   bytes       the length of the hash to compute in bytes
	     * @return              the PBDKF2 hash of the password
	     */
	    private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int 


	bytes)
	        throws NoSuchAlgorithmException, InvalidKeySpecException
	    {
	        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, bytes * 8);
	        SecretKeyFactory skf = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
	        return skf.generateSecret(spec).getEncoded();
	    }


	    /**
	     * Converts a string of hexadecimal characters into a byte array.
	     *
	     * @param   hex         the hex string
	     * @return              the hex string decoded into a byte array
	     */
	    private static byte[] fromHex(String hex)
	    {
	        byte[] binary = new byte[hex.length() / 2];
	        for(int i = 0; i < binary.length; i++)
	        {
	            binary[i] = (byte)Integer.parseInt(hex.substring(2*i, 2*i+2), 16);
	        }
	        return binary;
	    }


	    /**
	     * Converts a byte array into a hexadecimal string.
	     *
	     * @param   array       the byte array to convert
	     * @return              a length*2 character string encoding the byte array
	     */
	    private static String toHex(byte[] array)
	    {
	        BigInteger bi = new BigInteger(1, array);
	        String hex = bi.toString(16);
	        int paddingLength = (array.length * 2) - hex.length();
	        if(paddingLength > 0)
	            return String.format("%0" + paddingLength + "d", 0) + hex;
	        else
	            return hex;
	    }


	    /**
	     * Tests the basic functionality of the PasswordHash class
	     *
	     * @param   args        ignored
	     */
	    public static void main(String[] args)
	    {
	        try
	        {
	        	System.out.println("09c428048a3781d5ae07d3d53dcfdbc27bcf2afb8a67f3cf57df79a0ac0e70adf36980defa46feb09ff0d497dcb81fbc654e4fd4ec637b1c698e5aa919e2c0937bf4930d17320de55df0d9e4dca076294d52e609c1dacc564143ec05ff1dd124fd497badbb4f7fc6e23b0aff8629e11e3ac6bb2f8014d1dac6404cc8818dba9def781a022fafba68bdc83579f3dd391cfc0c373b39d370a92b92a37411efa7a93781199c9d7958e6bf16cb1f28042732b6ea641eb3f2c67aaad264b9c619e615be32a224b6865fd29580001bab94fee1fdeb22fa68d9223f0afeadd08e2e73a458cb6439a0f6ece35ce71c85c6a263c6ea30645ac6e8cae938a1f750b80cb959".length());
	        	System.out.println(toHex(pbkdf2("1234".toCharArray(),"abcde".getBytes(),1000,24)));
//	            // Print out 10 hashes
//	            for(int i = 0; i < 10; i++)
//	                System.out.println(Test.createHash("p\r\nassw0Rd!"));
//
//
//	            // Test password validation
//	            boolean failure = false;
//	            System.out.println("Running tests...");
//	            for(int i = 0; i < 100; i++)
//	            {
//	                String password = ""+i;
//	                String hash = createHash(password);
//	                String secondHash = createHash(password);
//	                if(hash.equals(secondHash)) {
//	                    System.out.println("FAILURE: TWO HASHES ARE EQUAL!");
//	                    failure = true;
//	                }
//	                String wrongPassword = ""+(i+1);
//	                if(validatePassword(wrongPassword, hash)) {
//	                    System.out.println("FAILURE: WRONG PASSWORD ACCEPTED!");
//	                    failure = true;
//	                }
//	                if(!validatePassword(password, hash)) {
//	                    System.out.println("FAILURE: GOOD PASSWORD NOT ACCEPTED!");
//	                    failure = true;
//	                }
//	            }
//	            if(failure)
//	                System.out.println("TESTS FAILED!");
//	            else
//	                System.out.println("TESTS PASSED!");
	        }
	        catch(Exception ex)
	        {
	            System.out.println("ERROR: " + ex);
	        }
	    }


	}

