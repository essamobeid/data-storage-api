package com.github.api;

import com.github.api.model.RepositoryEntity;
import com.github.api.dto.RepositoryDto;
import com.github.api.utils.HashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/data")
public class DataController {

    HashMap<String, Set<RepositoryEntity>> store = new HashMap<>();
    @GetMapping(value = "/{repository}/{objectID}",
            produces = {
                    MediaType.APPLICATION_JSON_VALUE
            })
    public ResponseEntity<String>  getObject(@PathVariable("repository") String repository, @PathVariable("objectID") String objectID) {
        if(store.containsKey(repository)){
            for(RepositoryEntity repo: store.get(repository)){
                if(repo.getOid().equals(objectID)){
                    return new ResponseEntity<>(repo.getData(), HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(
            value = "/{repository}",
            method = RequestMethod.PUT,
            produces = {
                    MediaType.APPLICATION_JSON_VALUE
            }
    )
    public ResponseEntity<RepositoryDto>  createNewRepository(@PathVariable String repository, @RequestBody String str) {
        String oid = HashUtil.sha256(str);
        RepositoryEntity newRepo = new RepositoryEntity(oid, str.length(), str);
        if(!store.containsKey(repository)){
            store.put(repository,new HashSet<>(Arrays.asList(newRepo)));
            return new ResponseEntity<>(new RepositoryDto(newRepo), HttpStatus.CREATED);
        }
        else{
            for(RepositoryEntity repo: store.get(repository)){
                if(repo.getOid().equals(oid)){
                    return new ResponseEntity<>(new RepositoryDto(newRepo), HttpStatus.CREATED); // we can change the httpstatus to already created but then one of the tests will fail
                }
            }
            store.get(repository).add(newRepo);
            return new ResponseEntity<>(new RepositoryDto(newRepo), HttpStatus.CREATED);
        }
    }


}
