package org.eclipse.opensmartclide.servicecreation.functionality.mainFlow;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

public class LicenseProject {

    private String gitlabToken;
    private String gitRepoURL;
    private String projectName;
    private String license;
    private String folderName;
    private Git gitRepo;

    public LicenseProject(String gitlabToken, String gitRepoURL, String projectName, String license) {
        this.gitlabToken = gitlabToken;
        this.gitRepoURL = gitRepoURL;
        this.projectName = projectName;
        this.license = license;

        folderName = "/" + projectName +"."+ System.currentTimeMillis();
    }

    /**
     * Creates the licence, writes the content, and commits
     * @throws IOException
     * @throws GitAPIException
     */
    public void createLicenseFile() throws IOException, GitAPIException {
        File fileLicense = new File(folderName + "/LICENSE");
        if (fileLicense.createNewFile()) {
            FileWriter myWriter = new FileWriter(fileLicense);
            myWriter.write(LicenseFlow.licensesMap.get(license));
            myWriter.close();

            commitAndPush();
            System.out.println("License added");
        }
    }

    /**
     * Commits and pushes
     * @throws IOException
     * @throws GitAPIException
     */
    public void commitAndPush() throws IOException, GitAPIException {
        gitRepo.add().addFilepattern(".").call();
        gitRepo.commit().setNoVerify(true).setMessage("Add LICENSE").call();
        gitRepo.push().setForce(true)
                .setCredentialsProvider(new UsernamePasswordCredentialsProvider("gitlab-ci-token", gitlabToken))
                .call();
    }

    /**
     * Clones repo locally
     * @throws GitAPIException
     */
    public void cloneRepository() throws GitAPIException {
        gitRepo = Git.cloneRepository()
                    .setURI(gitRepoURL)
                    .setDirectory(new File(folderName))
                    .setCredentialsProvider(new UsernamePasswordCredentialsProvider("gitlab-ci-token", gitlabToken))
                    .call();
    }

    /**
     * Deletes cloned folder
     */
    public void deleteFolder() {
        FileSystemUtils.deleteRecursively(new File(folderName));
    }
}
