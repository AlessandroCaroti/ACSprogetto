/**
    This file is part of ACSprogetto.

    ACSprogetto is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    ACSprogetto is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with ACSprogetto.  If not, see <http://www.gnu.org/licenses/>.

**/
package utility.cryptography;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StringHashed {

    private final String algorithm;
    private final byte[] stringHashed;

    public StringHashed(String plainText) throws NoSuchAlgorithmException {

        algorithm = "SHA-256";
        stringHashed = hashFunctions.stringHash(plainText);
    }

    public StringHashed(String plainText, String hash_algorithm) throws NoSuchAlgorithmException {
        algorithm = hash_algorithm;
        stringHashed = hashFunctions.stringHash(plainText, algorithm);
    }

    public byte[] getStringHashed() {
        return stringHashed;
    }

    public boolean isEqual(String plainText)
    {
        byte[] toCompere = null;
        try {
            toCompere = hashFunctions.stringHash(plainText, algorithm);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return MessageDigest.isEqual(stringHashed, toCompere);
    }
}
