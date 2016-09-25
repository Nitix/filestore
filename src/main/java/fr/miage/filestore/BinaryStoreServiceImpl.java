package fr.miage.filestore;

import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by nitix on 24/09/2016.
 */
public class BinaryStoreServiceImpl implements BinaryStoreService {

    private HashMap<String, InputStream> data = new HashMap<>();

    private int k = 0;

    @Override
    public boolean exists(String key) throws BinaryStoreServiceException {
        return data.containsKey(key);
    }

    @Override
    public String put(InputStream is) throws BinaryStoreServiceException {
        String key = ""+k;
        data.put(key, is);
        k++;
        return key;
    }

    @Override
    public InputStream get(String key) throws BinaryStoreServiceException, BinaryStreamNotFoundException {
        if(!exists(key)){
            throw new BinaryStreamNotFoundException();
        }
        return data.get(key);
    }
}
