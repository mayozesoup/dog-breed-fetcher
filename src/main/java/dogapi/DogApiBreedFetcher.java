package dogapi;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONString;

import java.io.IOException;
import java.util.*;

/**
 * BreedFetcher implementation that relies on the dog.ceo API.
 * Note that all failures get reported as BreedNotFoundException
 * exceptions to align with the requirements of the BreedFetcher interface.
 */
public class DogApiBreedFetcher implements BreedFetcher {
    private final OkHttpClient client = new OkHttpClient();

    /**
     * Fetch the list of sub breeds for the given breed from the dog.ceo API.
     * @param breed the breed to fetch sub breeds for
     * @return list of sub breeds for the given breed
     * @throws BreedNotFoundException if the breed does not exist (or if the API call fails for any reason)
     */
    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {

        //Done
        Request request = new Request.Builder()
                .url("https://dog.ceo/api/breed/" + breed + "/list")
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful() || response.body() != null) {
                JSONObject responsebody = new JSONObject(response.body().string());
                ArrayList<String> subBreeds = new ArrayList<>();

                JSONArray list = responsebody.getJSONArray("message");
                for (int i = 0; i < list.length(); i++) {
                    subBreeds.add(list.getString(i));
                }
                return subBreeds;
            }
            else {
                throw new BreedNotFoundException("Failed to fetch breed list");
            }

        } catch (IOException e) {
            throw new BreedNotFoundException("Breed not found:" + breed);
        }
    }
}