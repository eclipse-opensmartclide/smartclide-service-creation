package org.eclipse.opensmartclide.servicecreation.functionality.mainFlow;


import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.IOException;
import java.util.HashMap;

public class LicenseFlow {

    public static HashMap<String,String> licensesMap;

    public static void addLicense(String gitlabToken, String gitRepoURL, String projectName, String license) {
        //if given license exists start flow
        if (licensesMap.get(license) != null) {
            LicenseProject licenseProject = new LicenseProject(gitlabToken, gitRepoURL, projectName, license);
            try {
                licenseProject.cloneRepository();
                licenseProject.createLicenseFile();
            } catch (GitAPIException | IOException e) {
                throw new RuntimeException(e);
            }
            licenseProject.deleteFolder();
        }
    }

}
