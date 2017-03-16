package hu.tsystems.devlad.service.dto;


import java.io.Serializable;
import java.math.BigInteger;

/**
 * A DTO for the Developer entity.
 */
public class LearnedSkillDTO implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -2947784584439908350L;
	
	public LearnedSkillDTO(Object[] queryResult) {
		super();
		this.id = (BigInteger) queryResult[0];
		this.name = (String) queryResult[1];
		this.levels = (String) queryResult[2];
		this.xp = (BigInteger) queryResult[3];
	}
	
	private BigInteger id;

	private String name;

    private String levels;

    private BigInteger xp;
    
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

	public String getLevels() {
		return levels;
	}

	public void setLevels(String levels) {
		this.levels = levels;
	}

	public BigInteger getXp() {
		return xp;
	}

	public void setXp(BigInteger xp) {
		this.xp = xp;
	}
    
    
    
}
