package arrays.introduction;

public class DVD {
    public String name;
    public int releaseYear;
    public String director;

    public DVD(String name, int releaseYear, String director) {
        this.name = name;
        this.director = director;
        this.releaseYear = releaseYear;
    }

    public String toString() {
        return this.name + ", directed by " + this.director + ", released in "+ this.releaseYear;
    }
}
