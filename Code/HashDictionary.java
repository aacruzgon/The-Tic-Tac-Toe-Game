import java.util.LinkedList;

public class HashDictionary implements DictionaryADT {

    // Declaring instance variable dictionary
    private LinkedList<Data>[] dictionary;

    // Represents the size of the hashTable to compress hash value
    private int modulo;

    // The base 37 produces the least ammount of colissions in combination with the MAD compression function.
    // 37 is one the 4 values where experimental studies show are good choices for a base when working with character strings
    private int base = 37;

    // Prime number of mad compression function
    private int p = 1000000007;

    // declaring and initializing variables a and b for the MAD method. After brief experimentation producing random generated numbers between [0-p] where a > 0
    // for a = 225549583 and b = 77636339 produce the least number of collisions (1557 based on the TestDict) for the compression function.
    // MAD COMPRESSION FUNCTION [(ai+b) mod p] mod N
    private int a = 225549583;
    private int b = 77636339;

    // Declaring instance variable that holds the number of records in the dictionary
    private int numRecords = 0;

    //Constructor
    @SuppressWarnings("unchecked")
    public HashDictionary(int size) {
        this.modulo = size;
        this.dictionary = new LinkedList[size];
        for (int i = 0; i < size; i++) {
            dictionary[i] = new LinkedList<>();
        }
    }

    // method that adds a record to the hash dictionary, produces hash using polynomial function and MAD method to compress it
    // colission are handle via a linked list where which hash entry has a bucket where it will stored the nodes
    public int put(Data record) throws DictionaryException {
        try {
            int hashValue = madHash(record.getConfiguration());
            for (Data data : dictionary[hashValue]) {
                if (data.getConfiguration().equals(record.getConfiguration())) {
                    throw new DictionaryException();
                }
            }
            boolean collision = !dictionary[hashValue].isEmpty();
            dictionary[hashValue].add(record);
            numRecords = numRecords + 1;
            return collision ? 1 : 0;
        } catch (Exception e) {
            throw new DictionaryException();
        }
    }

    // method that removes record from the hash dictionary, produces hash using polynomial functoin and MAD method to compress it
    public void remove(String config) throws DictionaryException {
        try {
            int hashValue = madHash(config);
            for (Data data : dictionary[hashValue]) {
                if (data.getConfiguration().equals(config)) {
                    dictionary[hashValue].remove(data);
                    numRecords = numRecords - 1;
                    return;
                }
            }
            throw new DictionaryException();
        } catch (Exception e) {
            throw new DictionaryException();
        }
    }

    // method that returns the score by checking if configuration is in the hash, produces hash using polynomial functoin and MAD method to compress it
    public int get(String config) {
        int hashValue = madHash(config);
        for (Data data : dictionary[hashValue]) {
            if (data.getConfiguration().equals(config)) {
                return data.getScore();
            }
        }
        return -1;
    }

    // method that returns the number of records in the current dictionary
    public int numRecords() {
        return numRecords;
    }

    // Helper method to create the initial hash using polynomialHash method
    private int polynomialHash(String config) {
        int hashValue = (int) config.charAt(config.length() - 1);

        for (int i = config.length() - 2; i >= 0; i--) {
            hashValue = (hashValue * base + (int) config.charAt(i));
        }

        return hashValue;
    }

    // Helper Compression method to get the final hash value that will eliminate repeated patterns in a set of integer keys using the MAD method
    private int madHash(String config) {
        int hashValue = polynomialHash(config);
        int finalHash = ((a * hashValue + b) % p) % modulo;
        return (finalHash + modulo) % modulo;
    }
}
