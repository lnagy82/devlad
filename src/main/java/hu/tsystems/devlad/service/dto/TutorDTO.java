package hu.tsystems.devlad.service.dto;


import java.io.Serializable;
import java.math.BigInteger;

/**
 * A DTO for the Developer entity.
 */
public class TutorDTO implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -2941784584439908350L;
	
	public TutorDTO(Object[] queryResult) {
		super();
		this.id = (BigInteger) queryResult[0];
		this.name = (String) queryResult[1];
		this.developers = (String) queryResult[2];
	}
	
	private BigInteger id;

	private String name;

    private String developers;

    public BigInteger getId() {
		return id;
	}
    
    public void setId(BigInteger id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDevelopers(String developers) {
		this.developers = developers;
	}
	
	public String getDevelopers() {
		return developers;
	}
}
