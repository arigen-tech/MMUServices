package com.mmu.services.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mmu.services.service.LabService;

@RequestMapping("/lab")
@RestController
@CrossOrigin
public class LabController {

	@Autowired
	LabService labService; 
	
	/************************************************************* Sample Collection *************************************************/
	/**
	 * @Description: Method:getPendingSampleCollection(), pending sample collection waiting List
	 * @param payload
	 * @param request
	 * @param response
	 * @return
	 */
	
	//getPendingSampleCollectionWaitingListGrid()
	@RequestMapping(value="/getPendingSampleCollectionWaitingListGrid", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
	public String getPendingSampleCollectionWaitingListGrid(@RequestBody Map<String, Object> payload, HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject(payload);
		return labService.getPendingSampleCollectionWaitingListGrid(jsonObject,request,response);
	}
	
	@RequestMapping(value="/getPendingSampleCollection", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
	public String getPendingSampleCollection(@RequestBody Map<String, Object> payload, HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject(payload);
		return labService.getPendingSampleCollection(jsonObject,request,response);
	}
	
	@RequestMapping(value="/getPendingSampleCollectionDetails", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
	public String getPendingSampleCollectionDetails(@RequestBody Map<String, Object> payload, HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject(payload);
		return labService.getPendingSampleCollectionDetails(jsonObject,request,response);
	}
	
	@RequestMapping(value="/submitSampleCollectionDetails", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
	public String submitSampleCollectionDetails(@RequestBody Map<String, Object> payload, HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject(payload);
		return labService.submitSampleCollectionDetails(jsonObject,request,response);
	}
	
	/********************************************************** Sample Validate ********************************************************/
	/**
	 * @Description: Method:pendingSampleValidate(), waiting List of Pending Sample Validate
	 * @param payload
	 * @param request
	 * @param response
	 * @return
	 */
	//getPendingSampleValidateListGrid
	@RequestMapping(value="/getPendingSampleValidateListGrid", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
	public String getPendingSampleValidateListGrid(@RequestBody Map<String, Object> payload, HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject(payload);
		return labService.getPendingSampleValidateListGrid(jsonObject,request,response);
	}
	
	@RequestMapping(value="/getPendingSampleValidateList", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
	public String pendingSampleValidate(@RequestBody Map<String, Object> payload, HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject(payload);
		return labService.getPendingSampleValidateList(jsonObject,request,response);
	}
	
	@RequestMapping(value="/getPendingSampleValidateDetails", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
	public String PendingSampleValidateDetails(@RequestBody Map<String, Object> payload, HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject(payload);
		return labService.getPendingSampleValidateDetails(jsonObject,request,response);
	}
	
	@RequestMapping(value = "/submitSampleValidationDetails", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
	public String submitSampleValidationDetails(@RequestBody Map<String, Object> payload, HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject(payload);
		return labService.submitSampleValidationDetails(jsonObject,request,response);
	}
	
	/**************************************************** Result Entry **************************************************/
	/**
	 * @Description: Method: getPendingResultEntryList(), waiting List of Result Entry
	 * @param payload
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/getResultEntryWaitingList", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
	public String getResultEntryWaitingList(@RequestBody Map<String, Object> payload, HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject(payload);
		return labService.getResultEntryWaitingList(jsonObject,request,response);
	}

	@RequestMapping(value="/getResultEntryWaitingListGrid", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
	public String getResultEntryWaitingListGrid(@RequestBody Map<String, Object> payload, HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject(payload);
		return labService.getResultEntryWaitingListGrid(jsonObject,request,response);
	}
	
	@RequestMapping(value="/getResultEntryDetails", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
	public String getResultEntryDetails(@RequestBody Map<String, Object> payload, HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject(payload);
		return labService.getResultEntryDetails(jsonObject,request,response);
	}
	
	
	@RequestMapping(value="/submitResultEntryDetails", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
	public String submitResultEntryDetails(@RequestBody Map<String, Object> payload, HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject(payload);
		return labService.submitResultEntryDetails(jsonObject,request,response);
	}
	
	/********************************************************** RESULT VALIDATION *********************************************************/
	/**
	 * 
	 * @param payload
	 * @param request
	 * @param response
	 * @return
	 */
	
	
	@RequestMapping(value="/getResultValidationWaitingListGrid", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
	public String getResultValidationWaitingListGrid(@RequestBody Map<String, Object> payload, HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject(payload);
		return labService.getResultValidationWaitingListGrid(jsonObject,request,response);
	}
	
	@RequestMapping(value="/getResultValidationWaitingList", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
	public String getResultValidationWaitingList(@RequestBody Map<String, Object> payload, HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject(payload);
		return labService.getResultValidationWaitingList(jsonObject,request,response);
	}
	
	@RequestMapping(value="/getResultValidationDetails", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
	public String getResultValidationDetails(@RequestBody Map<String, Object> payload, HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject(payload);
		return labService.getResultValidationDetails(jsonObject,request,response);
	}
	
	@RequestMapping(value="/submitResultValidationDetails", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
	public String submitResultValidationDetails(@RequestBody Map<String, Object> payload, HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject(payload);
		return labService.submitResultValidationDetails(jsonObject,request,response);
	}
	
	/*******************************  LabHistory ******************************************************/
	
	@RequestMapping(value="/getPatientList", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
	public String getPatientList(@RequestBody Map<String, Object> payload, HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject(payload);
		return labService.getPatientList(jsonObject,request,response);
	}
	
	@RequestMapping(value="/getLabHistory", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
	public String getLabHistory(@RequestBody Map<String, Object> payload, HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject(payload);
		return labService.getLabHistory(jsonObject,request,response);
	}
	
	/*******************************  Update Result Entry ******************************************************/
	@RequestMapping(value="/getResultUpdateWaitingList", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
	public String getResultUpdateWaitingList(@RequestBody Map<String, Object> payload, HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject(payload);
		return labService.getResultUpdateWaitingList(jsonObject,request,response);
	}
	
	@RequestMapping(value="/getResultEntryUpdateDetails", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
	public String getResultEntryUpdateDetails(@RequestBody Map<String, Object> payload, HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject(payload);
		return labService.getResultEntryUpdateDetails(jsonObject,request,response);
	}
	
	@RequestMapping(value="/updateResultEntryDetails", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
	public String updateResultEntryDetails(@RequestBody Map<String, Object> payload, HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject(payload);
		return labService.updateResultEntryDetails(jsonObject,request,response);
	}

	/*******************************  OutSide Patient ******************************************************/
	
		@RequestMapping(value="/getOutSidePatientWaitingList", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
		public String getOutSidePatientWaitingList(@RequestBody Map<String, Object> payload, HttpServletRequest request, HttpServletResponse response) {
			JSONObject jsonObject = new JSONObject(payload);
			return labService.getOutSidePatientWaitingList(jsonObject,request,response);
		}
		
		@RequestMapping(value="/getOutSidePatientDetails", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
		public String getOutSidePatientDetails(@RequestBody Map<String, Object> payload, HttpServletRequest request, HttpServletResponse response) {
			JSONObject jsonObject = new JSONObject(payload);
			return labService.getOutSidePatientDetails(jsonObject,request,response);
		}
		
		@RequestMapping(value="/getSampleRejectedWaitingList", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
		public String getSampleRejectedWaitingList(@RequestBody Map<String, Object> payload, HttpServletRequest request, HttpServletResponse response) {
			JSONObject jsonObject = new JSONObject(payload);
			return labService.getSampleRejectedWaitingList(jsonObject,request,response);
		}	
		
		@RequestMapping(value="/getSampleRejectedDetails", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
		public String getSampleRejectedDetails(@RequestBody Map<String, Object> payload, HttpServletRequest request, HttpServletResponse response) {
			JSONObject jsonObject = new JSONObject(payload);
			return labService.getSampleRejectedDetails(jsonObject,request,response);
		}
		
		
		@RequestMapping(value = "/submitSampleRejectedDetails", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
		public String submitSampleRejectedDetails(@RequestBody Map<String, Object> payload, HttpServletRequest request, HttpServletResponse response) {
			JSONObject jsonObject = new JSONObject(payload);
			return labService.submitSampleRejectedDetails(jsonObject,request,response);
		}
		
		@RequestMapping(value = "/getLabHistoryDetails", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
		public String authenticateUser(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
				HttpServletResponse response) {
			return labService.getLabHistoryDetails(jsondata, request, response);
		}
		
		@RequestMapping(value = "/checkAuthenticateUser", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
		public String checkAuthenticateUser(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
				HttpServletResponse response) {
			return labService.checkAuthenticateUser(jsondata, request, response);
		}
		
		@RequestMapping(value = "/checkAuthenticateServiceNo", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
		public String checkAuthenticateServiceNo(@RequestBody HashMap<String, Object> jsondata, HttpServletRequest request,
				HttpServletResponse response) {
			return labService.checkAuthenticateServiceNo(jsondata, request, response);
		}
		
				
		@RequestMapping(value = "/getInvestigationList", method = RequestMethod.POST)
		public ResponseEntity<Map<String, Object>> getInvestigationList(@RequestBody Map<String, Object> requestData) {
			Map<String, Object> invList = new HashMap<String, Object>();
			invList = labService.getInvestigationList(requestData);
			if (invList.isEmpty()) {
				return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<Map<String, Object>>(invList, HttpStatus.OK);
		}
}
