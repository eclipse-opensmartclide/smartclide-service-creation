package org.eclipse.opensmartclide.servicecreation.functionality.mainFlow;


import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.IOException;
import java.util.HashMap;

public class LicenseFlow {

    public static HashMap<String,String> licensesMap;

    public static void addLicense(String gitlabToken, String gitRepoURL, String projectName, String license) {
        LicenseProject licenseProject =new LicenseProject(gitlabToken,gitRepoURL,projectName,license);
        try {
            licenseProject.cloneRepository();
            if(licensesMap.get(license) != null) {
                licenseProject.createLicenseFile();
            }
        } catch (GitAPIException | IOException e) {
            throw new RuntimeException(e);
        }
    }

}
