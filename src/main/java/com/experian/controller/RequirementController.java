/**
 * 
 */
package com.experian.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.experian.dto.ExperianFileRefreshRequest;
import com.experian.dto.ExperianMatchedRequirementsRequest;
import com.experian.dto.ExperianSearchRequest;
import com.experian.dto.FileUploadResponse;
import com.experian.dto.FileUploadResponseList;
import com.experian.dto.chatbot.response.ChatBotScoreResponse;
import com.experian.dto.neo4j.request.FinalNeo4JRequest;
import com.experian.dto.neo4j.response.SuggestionResponse;
import com.experian.dto.neo4j.response.TaxationResponse;
import com.experian.dto.neo4j.response.taxation.Taxation;
import com.experian.service.ExternalService;
import com.experian.validator.Validator;

/**
 * @author manchanda.a
 *
 */
@RestController
@RequestMapping("/api")
public class RequirementController {

	private static final Logger logger = LoggerFactory.getLogger(RequirementController.class);

	@Autowired
	private ExternalService service;

	@Autowired
	private Validator validate;


	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	private boolean submitRequirementToNeo4j(@RequestBody FinalNeo4JRequest request) {
		return service.processFinalResponse(request);
	}

	/**
	 * This controller will be called when user click on search for resources.
	 * 
	 * @return
	 */
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	private SuggestionResponse searchResource(@RequestBody ExperianSearchRequest request) {
		validate.validateSearchText(request.getRequirement());
		SuggestionResponse response = service.searchRequirementToGetSuggestions(request.getRequirement());
		return response;
	}

	/**
	 * This controller will be called when user update level 3 and 4 , it will
	 * call AI/ML service to get updated score.
	 * 
	 * @return
	 */
	@RequestMapping(value = "/refresh", method = RequestMethod.POST)
	private void getScore(@RequestBody ExperianFileRefreshRequest request) {

	}

	/**
	 * This controller will be called when there is search and match conditions occurs.
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/add/match", method = RequestMethod.POST)
	private FileUploadResponseList addNewResourceMatched(@RequestBody ExperianMatchedRequirementsRequest request) {
		return service.addMatchedRequirement(request.getMatchedRequirements());
	}

	/**
	 * This controller will be called when no match found for requirement.
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/add/no/match", method = RequestMethod.POST)
	private FileUploadResponse addNoMatchFoundForResource(@RequestBody ExperianSearchRequest request) {
		return service.addNoMatchedRequirement(request.getRequirement());
	}

	/**
	 * This controller will be called when new requirement is added.
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/add/new", method = RequestMethod.POST)
	private FileUploadResponse addNewResource(@RequestBody ExperianSearchRequest request) {
		return service.addNewRequirement(request.getRequirement());
	}

	/**
	 * This controller will be called to retrieve taxation mapping.
	 */
	@RequestMapping(value = "/getTaxation", method = RequestMethod.GET)
	private List<Taxation> getTaxation() {
		return service.getTaxation();
	}
	
	/**
	 * This controller will be called when chatbot send request to get quality score.
	 * @param request
	 */
	@RequestMapping(value="/chatbot/quality/score", method = RequestMethod.POST)
	private ChatBotScoreResponse getChatbotQualityScore(@RequestBody ExperianSearchRequest request) {
		return service.calculateChatBotScore(request.getRequirement());
	}
	
}
