package hu.tsystems.devlad.web.rest.util;

import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {

	public static <T extends Object> ResponseEntity<T> wrapOrNotFound(Optional<T> ofNullable) {
		return wrapOrNotFound(ofNullable, null);		    
	}
	
	 public static <X> ResponseEntity<X> wrapOrNotFound(Optional<X> maybeResponse, HttpHeaders header) {
	        return maybeResponse.map(response -> ResponseEntity.ok().headers(header).body(response))
	            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	 }

}
