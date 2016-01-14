package movies;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * A class for downloading movie data from the internet.
 * Code adapted from Google.
 * <p>
 * YOUR TASK: Add comments explaining how this code works!
 *
 * @author Joel Ross & Kyungmin Lee
 */
public class MovieDownloader {

    public static String[] downloadMovieData(String movie) {

        //construct the url for the omdbapi API
        String urlString = "http://www.omdbapi.com/?s=" + movie + "&type=movie";

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String movies[] = null;


        try {
            //Opens a HTTP connection with a GET request to the given website
            URL url = new URL(urlString);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //Checks to see if the user input is valid
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }

            //Reads the response from the inputStream and sends it to the reader to make it more efficent to read
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            //Goes through the entire response and adds a new line delimiter between each line
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }

            //Changes the reponse to a JSON friendly format for the reader to use
            String results = buffer.toString();
            results = results.replace("{\"Search\":[", "");
            results = results.replace("]}", "");
            results = results.replace("},", "},\n");

            //Changes the results into an array
            movies = results.split("\n");
        } catch (IOException e) {
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                }
            }
        }

        return movies;
    }


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter a movie name to search for: ");
        String searchTerm = sc.nextLine().trim();
        String[] movies = downloadMovieData(searchTerm);
        for (String movie : movies) {
            System.out.println(movie);
        }

        sc.close();
    }
}
