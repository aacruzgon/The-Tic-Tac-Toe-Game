public class Data {

    // declared private instance variable
    private String config;
    private int score;

    // Constructor that initializes a new Data object with a specific configuratoin and score
    public Data(String config, int score) {
        this.config = config;
        this.score = score;
    }

    // public method that returns the configuration stored in the data object
    public String getConfiguration() {
        return this.config;
    }

    // public method that returns the score stored in the data object
    public int getScore() {
        return this.score;
    }
}
