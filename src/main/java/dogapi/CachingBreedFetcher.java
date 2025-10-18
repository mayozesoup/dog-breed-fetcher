package dogapi;

import java.util.*;

/**
 * This BreedFetcher caches fetch request results to improve performance and
 * lessen the load on the underlying data source. An implementation of BreedFetcher
 * must be provided. The number of calls to the underlying fetcher are recorded.
 *
 * If a call to getSubBreeds produces a BreedNotFoundException, then it is NOT cached
 * in this implementation. The provided tests check for this behaviour.
 *
 * The cache maps the name of a breed to its list of sub breed names.
 */
public class CachingBreedFetcher implements BreedFetcher {
    private int callsMade = 0;
    private HashMap<String,List<String>> cache = new HashMap<String,List<String>>();
    private final BreedFetcher fetcher;
    public CachingBreedFetcher(BreedFetcher fetcher) {
        this.fetcher = fetcher;
    }

    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        List<String> subreeds =  cache.get(breed);
        if (subreeds != null) {
            return subreeds;
        }
        else {
            try {
                subreeds = fetcher.getSubBreeds(breed);
                cache.put(breed, subreeds);
                callsMade++;
                return subreeds;
            }
            catch (BreedNotFoundException e) {
                callsMade++;
                throw new BreedNotFoundException("breed not found:" + breed);
            }
        }
    }

    public int getCallsMade() {
        return callsMade;
    }
}