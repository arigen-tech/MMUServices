package com.mmu.services.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mmu.services.service.WardService;

@RestController
@CrossOrigin
@RequestMapping("/ward")
public class WardController {
	
	@Autowired
	WardService wardService;
	@RequestMapping(value="/getWardDepartment", method=RequestMethod.POST, consumes="application/json", produces="application/json")
	public ResponseEntity<Map<String,Object>> getWardDepartment(@RequestBody Map<String,Object> inputJson){
		Map<String,Object> map = wardService.getWardDepartment(inputJson);
		if(map.isEmpty()) {
			return new ResponseEntity<Map<String,Object>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Map<String,Object>>(map, HttpStatus.OK);
	}
	}
