/*******************************************************************************
 * Copyright (C) 2021-2022 University of Macedonia
 * 
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * SPDX-License-Identifier: EPL-2.0
 ******************************************************************************/
package org.eclipse.opensmartclide.servicecreation.controller;


import org.eclipse.opensmartclide.servicecreation.functionality.mainFlow.LicenseFlow;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import org.eclipse.opensmartclide.servicecreation.functionality.mainFlow.MainFlow;
import org.eclipse.opensmartclide.servicecreation.functionality.utils.ResultObject;

import java.util.concurrent.TimeUnit;

//@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class EndpointController {

	@GetMapping("/getRepoURL")
	public String getRepoURL() {
		return "Hello, placeholder for Git repo URL";
	}

	//@CrossOrigin(origins = "*")
	@PostMapping("/createStuctureJenkins")
	public ResultObject createStuctureJenkins(@RequestHeader String projectName, @RequestHeader String projVisibility,
			@RequestHeader String projDescription, @RequestHeader String gitLabServerURL,
			@RequestHeader String gitlabToken, @RequestHeader String jenkinsServerUrl,
			@RequestHeader String jenkinsUsername, @RequestHeader String jenkinsToken) {

		if (isEmptyOrNull(projectName) || isEmptyOrNull(gitLabServerURL) || isEmptyOrNull(gitlabToken)
				|| isEmptyOrNull(jenkinsServerUrl) || isEmptyOrNull(jenkinsUsername) || isEmptyOrNull(jenkinsToken)
				|| isEmptyOrNull(projVisibility) || isEmptyOrNull(projDescription)) {
			return new ResultObject(1, "One or more fields are empty or blank");
		}

		projectName = projectName.replaceAll("( )+","-");
		ResultObject ret = new ResultObject(1, "Creation Failed");
		ret = MainFlow.createStructureJenkins(projectName, projVisibility, projDescription, gitLabServerURL,
				gitlabToken, jenkinsServerUrl, jenkinsUsername, jenkinsToken);

		return ret;
	}

	//@CrossOrigin(origins = "*")
	@PostMapping("/createStructure")
	public ResultObject createStructure(@RequestHeader String projectName, @RequestHeader String projVisibility,
			@RequestHeader String projDescription, @RequestHeader String gitLabServerURL,
			@RequestHeader String gitlabToken, @RequestHeader(required = false) String license) {

		if (isEmptyOrNull(projectName) || isEmptyOrNull(gitLabServerURL) || isEmptyOrNull(gitlabToken)
				|| isEmptyOrNull(projVisibility) || isEmptyOrNull(projDescription)) {
			return new ResultObject(1, "One or more fields are empty or null");
		}

		projectName = projectName.replaceAll("( )+","-");
		ResultObject ret = new ResultObject(1, "Creation Failed");
		ret = MainFlow.createStructure(projectName, projVisibility, projDescription, gitLabServerURL, gitlabToken);

		if(!isEmptyOrNull(license) && ret.getStatus()==0){
			System.out.println("Project created");
			LicenseFlow.addLicense(gitlabToken, ret.getMessage(), projectName, license);
		}

		return ret;

	}

	private boolean isEmptyOrNull(String str) {
		return (str == null || str.isEmpty());
	}
}
