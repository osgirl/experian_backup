/**
 * 
 */
package com.experian.dto.aiml.response;

/**
 * @author manchanda.a
 *
 */
public class AimlTaxation {
	
	private String taxonomy_Level_1;
	private Integer ID;
	private String taxonomy_Level_2;
	private String taxonomy_Level_3;
	private String taxonomy_Level_4;
	
	
	
	/**
	 * @return the iD
	 */
	public Integer getID() {
		return ID;
	}
	/**
	 * @param iD the iD to set
	 */
	public void setID(Integer iD) {
		ID = iD;
	}
	/**
	 * @return the taxonomy_Level_1
	 */
	public String getTaxonomy_Level_1() {
		return taxonomy_Level_1;
	}
	/**
	 * @param taxonomy_Level_1 the taxonomy_Level_1 to set
	 */
	public void setTaxonomy_Level_1(String taxonomy_Level_1) {
		this.taxonomy_Level_1 = taxonomy_Level_1;
	}
	/**
	 * @return the taxonomy_Level_2
	 */
	public String getTaxonomy_Level_2() {
		return taxonomy_Level_2;
	}
	/**
	 * @param taxonomy_Level_2 the taxonomy_Level_2 to set
	 */
	public void setTaxonomy_Level_2(String taxonomy_Level_2) {
		this.taxonomy_Level_2 = taxonomy_Level_2;
	}
	/**
	 * @return the taxonomy_Level_3
	 */
	public String getTaxonomy_Level_3() {
		return taxonomy_Level_3;
	}
	/**
	 * @param taxonomy_Level_3 the taxonomy_Level_3 to set
	 */
	public void setTaxonomy_Level_3(String taxonomy_Level_3) {
		this.taxonomy_Level_3 = taxonomy_Level_3;
	}
	/**
	 * @return the taxonomy_Level_4
	 */
	public String getTaxonomy_Level_4() {
		return taxonomy_Level_4;
	}
	/**
	 * @param taxonomy_Level_4 the taxonomy_Level_4 to set
	 */
	public void setTaxonomy_Level_4(String taxonomy_Level_4) {
		this.taxonomy_Level_4 = taxonomy_Level_4;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AimlTaxationResponse [id=" + ID + ", taxonomy_Level_1=" + taxonomy_Level_1 + ", taxonomy_Level_2="
				+ taxonomy_Level_2 + ", taxonomy_Level_3=" + taxonomy_Level_3 + ", taxonomy_Level_4=" + taxonomy_Level_4
				+ "]";
	}
	
	
}
