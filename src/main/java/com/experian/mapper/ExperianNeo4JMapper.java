package com.experian.mapper;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.util.StringUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.experian.dto.aiml.response.AimlFileFinalResponse;
import com.experian.dto.aiml.response.AimlFileResponse;
import com.experian.dto.neo4j.RequirementStatement;
import com.experian.dto.neo4j.RequirementSuggestions;
import com.experian.dto.neo4j.Suggestions;
import com.experian.dto.neo4j.brd.RequirementBasedOnElaborationResponse;
import com.experian.dto.neo4j.finalResponse.FinalResponse;
import com.experian.dto.neo4j.finalResponse.SavedDataResponse;
import com.experian.dto.neo4j.request.Neo4JFileRequest;
import com.experian.dto.neo4j.request.ShortCutDocument;
import com.experian.dto.neo4j.response.SuggestionResponse;
import com.experian.dto.neo4j.response.autocomplete.AutoCompleteResponse;
import com.experian.dto.neo4j.response.autocomplete.SearchAnyResponse;
import com.experian.dto.neo4j.suggestion.request.Neo4jSuggestionData;
import com.experian.dto.neo4j.suggestion.request.Neo4jSuggestionDataList;
import com.experian.dto.neo4j.suggestion.request.Neo4jSuggestionResponse;
import com.experian.dto.neo4j.suggestion.request.SuggestionBasedOnMultipleRequirement;
import com.experian.dto.neo4j.suggestion.request.SuggestionBasedOnMultipleRequirementRequest;

@Component
public class ExperianNeo4JMapper {

	
	/**
	 * This method will convert requirement list to format accepted by Neo4j for processing result.
	 * @param aimlFileFinalResponse.getResponse()
	 * @return
	 */
	public SuggestionBasedOnMultipleRequirementRequest convertRequirementSuggestionToSuggestionRequest(AimlFileFinalResponse aimlFileFinalResponse) {
		SuggestionBasedOnMultipleRequirementRequest request = new SuggestionBasedOnMultipleRequirementRequest();
		List<SuggestionBasedOnMultipleRequirement> suggestionRequestList = new ArrayList<>();
		for (AimlFileResponse aimlFileResponse : aimlFileFinalResponse.getResponse()) {
			SuggestionBasedOnMultipleRequirement suggestionBasedOnMultipleRequirement = new SuggestionBasedOnMultipleRequirement();
			suggestionBasedOnMultipleRequirement.setLevel1(aimlFileResponse.getTaxonomy_Level_1());
			suggestionBasedOnMultipleRequirement.setLevel2(aimlFileResponse.getTaxonomy_Level_2());
			suggestionBasedOnMultipleRequirement.setId(aimlFileResponse.getRequirementStatement().getID().toString());
			suggestionBasedOnMultipleRequirement.setStatement(aimlFileResponse.getRequirementStatement().getRequirementStatement());
			suggestionRequestList.add(suggestionBasedOnMultipleRequirement);
		}
		request.setMultipleSearch(suggestionRequestList);
		return request;
	}
	
	/**
	 * This method will create suggestion response from list of neo4j suggestion object.
	 * @param searchInput 
	 * @param neo4jSuggestionResponseList
	 * @return
	 */
	public SuggestionResponse convertSuggestionBasedResponse(String searchInput, List<Neo4jSuggestionResponse> neo4jSuggestionResponseList) {
		SuggestionResponse suggestionResponse = new SuggestionResponse();
		List<RequirementSuggestions> requirementSuggestionsList = new ArrayList<>();
		for (Neo4jSuggestionResponse neo4jSuggestionResponse : neo4jSuggestionResponseList) {
			requirementSuggestionsList.add(createRequirementSuggestions(searchInput, Integer.parseInt(neo4jSuggestionResponse.getId()), neo4jSuggestionResponse));
		}
		suggestionResponse.setSuggestions(requirementSuggestionsList);
		return suggestionResponse;
	}
	
	/**
	 * This method will create requirement suggestions for each id.
	 * @param searchInput 
	 * @param id
	 * @param neo4jSuggestionResponse
	 * @return
	 */
	private RequirementSuggestions createRequirementSuggestions(String searchInput, Integer id, Neo4jSuggestionResponse neo4jSuggestionResponse) {
		RequirementSuggestions requirementSuggestions = new RequirementSuggestions();
		RequirementStatement requirementStatement = new RequirementStatement();
		List<Suggestions> suggestionList = new ArrayList<>();
		requirementStatement.setID(id);
		List<Integer> suggestionId = new ArrayList<>();
		for (Neo4jSuggestionDataList neo4jSuggestionDataList : neo4jSuggestionResponse.getResult()) {
			if(neo4jSuggestionDataList != null) {
				if(!org.springframework.util.StringUtils.isEmpty(neo4jSuggestionDataList.getStatement())) {
					requirementStatement.setRequirementStatement(neo4jSuggestionDataList.getStatement());
				} else {
					requirementStatement.setRequirementStatement(searchInput);
				}
				for (Neo4jSuggestionData neo4jSuggestionData : neo4jSuggestionDataList.getSuggestion()) {
					if(!suggestionId.contains(neo4jSuggestionData.getId())) {
						suggestionId.add(neo4jSuggestionData.getId());
						Suggestions suggestions = createSuggestions(neo4jSuggestionData);
						suggestionList.add(suggestions);
					}
				}
				
			}
		}
		requirementSuggestions.setRequirements(requirementStatement);
		requirementSuggestions.setSuggestionResponse(suggestionList);
		return requirementSuggestions;
	}
	
	/**
	 * This method will create suggestions object based on neo4j suggestion data.
	 * @param neo4jSuggestionData
	 * @return
	 */
	private Suggestions createSuggestions(Neo4jSuggestionData neo4jSuggestionData) {
		Suggestions suggestions = new Suggestions();
		if(neo4jSuggestionData != null) {
			suggestions.setId(neo4jSuggestionData.getId());
			suggestions.setLevel1(neo4jSuggestionData.getLevel1());
			suggestions.setLevel2(neo4jSuggestionData.getLevel2());
			suggestions.setLevel3(neo4jSuggestionData.getLevel3());
			suggestions.setMatchPercentage(neo4jSuggestionData.getSearchScore());
			suggestions.setQualityScore(neo4jSuggestionData.getQualityScore());
			suggestions.setRequirementElaboration(neo4jSuggestionData.getRequirementElaboration());
			suggestions.setSuggestion(neo4jSuggestionData.getRequirementStatement());
		}
		return suggestions;	
	}
	
	/**
	 * This method will set requirement elaboration to neo4jFileRequest.
	 * @param neo4jFileRequestsList
	 * @param requirementElab
	 * @return
	 */
	public List<Neo4JFileRequest> createFinalMappingResponse(List<Neo4JFileRequest> neo4jFileRequestsList, String requirementElab) {
		List<Neo4JFileRequest> neo4jFileRequests = new ArrayList<>();
		ShortCutDocument shortCutDocument = new ShortCutDocument();
		shortCutDocument.setRequirementElaboration(requirementElab);
		for (Neo4JFileRequest neo4jFileRequest : neo4jFileRequestsList) {
			neo4jFileRequest.setShortCutDocument(shortCutDocument);
			neo4jFileRequests.add(neo4jFileRequest);
		}
		return neo4jFileRequests;
	}
	
	/**
	 * This method will convert list of final response to saved data response.
	 * @param finalResponseList
	 * @return
	 */
	public List<SavedDataResponse> convertFinalResponseToSavedData(List<FinalResponse> finalResponseList) {
		List<SavedDataResponse> savedDataResponses = new ArrayList<>();
		for (FinalResponse finalResponse : finalResponseList) {
			SavedDataResponse savedDataResponse = new SavedDataResponse();
			savedDataResponse.setDocumentElaboration(finalResponse.getRequirementElaboration());
			savedDataResponse.setDocumentPurpose(finalResponse.getDocument().getDocumentPurpose());
			savedDataResponse.setRequirementStatement(finalResponse.getRequirementStatement());
			savedDataResponse.setTaxonomyLevel1(finalResponse.getLevel1());
			savedDataResponse.setTaxonomyLevel2(finalResponse.getLevel2());
			savedDataResponse.setTaxonomyLevel3(finalResponse.getLevel3());
			savedDataResponse.setTaxonomyLevel4(finalResponse.getLevel4());
			savedDataResponses.add(savedDataResponse);
		}
		return savedDataResponses;
	}
	
	/**
	 * This method will convert list of requirement object based on requirement elaboration to list of saved 
	 * data response.
	 * @param requirementBasedOnElaborationResponses
	 * @return
	 */
	public List<SavedDataResponse> convertRequirementBasedOnElaborationToSavedDataResponseList(List<RequirementBasedOnElaborationResponse> requirementBasedOnElaborationResponses) {
		List<SavedDataResponse> savedDataResponses = new ArrayList<>();
		for (RequirementBasedOnElaborationResponse requirementBasedOnElaborationResponse : requirementBasedOnElaborationResponses) {
			SavedDataResponse savedDataResponse = new SavedDataResponse();
			savedDataResponse.setDocumentElaboration(requirementBasedOnElaborationResponse.getDocumentInfoNode().getRequirementElaboration());
			savedDataResponse.setDocumentPurpose(requirementBasedOnElaborationResponse.getDocumentInfoNode().getDocumentPurpose());
			savedDataResponse.setRequirementStatement(requirementBasedOnElaborationResponse.getRequirementStatementNode().getRequirementStatement());
			savedDataResponse.setTaxonomyLevel1(requirementBasedOnElaborationResponse.getRequirementStatementNode().getLevel1());
			savedDataResponse.setTaxonomyLevel2(requirementBasedOnElaborationResponse.getRequirementStatementNode().getLevel2());
			savedDataResponse.setTaxonomyLevel3(requirementBasedOnElaborationResponse.getRequirementStatementNode().getLevel3());
			savedDataResponse.setTaxonomyLevel4(requirementBasedOnElaborationResponse.getRequirementStatementNode().getLevel4());
			savedDataResponses.add(savedDataResponse);
			
		}
		return savedDataResponses;
	}
	
	/**
	 * This method will convert search any service response list to auto complete response list.
	 * @param searchAnyResponses
	 * @return
	 */
	public List<AutoCompleteResponse> convertSearchAnyResponseToAutoCompleteResponse(List<SearchAnyResponse> searchAnyResponses) {
		List<AutoCompleteResponse> autoCompleteResponses = new ArrayList<>();
		for (SearchAnyResponse searchAnyResponse : searchAnyResponses) {
			AutoCompleteResponse autoCompleteResponse = new AutoCompleteResponse();
			if(!StringUtils.isEmpty(searchAnyResponse.getRequirementStatement())) {
				autoCompleteResponse.setSuggestion(searchAnyResponse.getRequirementStatement());
				autoCompleteResponses.add(autoCompleteResponse);
			}
		}
		return autoCompleteResponses;
	}
}
