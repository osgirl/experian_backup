/**
 * 
 */
package com.experian.dto.aiml.request;

import java.util.List;

import com.experian.dto.neo4j.RequirementStatement;
import com.experian.dto.neo4j.response.wordCategory.WordCategory;

/**
 * @author manchanda.a
 *
 */
public class AimlQualityScoreRequest {
	
	private List<RequirementStatement> requirementStatements;
	private List<WordCategory> wordCategory;

	/**
	 * @return the requirementStatements
	 */
	public List<RequirementStatement> getRequirementStatements() {
		return requirementStatements;
	}

	/**
	 * @param requirementStatements the requirementStatements to set
	 */
	public void setRequirementStatements(List<RequirementStatement> requirementStatements) {
		this.requirementStatements = requirementStatements;
	}

	/**
	 * @return the wordCategory
	 */
	public List<WordCategory> getWordCategory() {
		return wordCategory;
	}

	/**
	 * @param wordCategory the wordCategory to set
	 */
	public void setWordCategory(List<WordCategory> wordCategory) {
		this.wordCategory = wordCategory;
	}
	
	

}
