package com.bage.ai.pipeline.agent.tool;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
public class GitTool implements AgentTool {

    @Override
    public String toolName() { return "git"; }

    @Value("${agent.project-base-path:}")
    private String projectBasePath;

    private volatile String activePath;

    public void setActivePath(String path) {
        this.activePath = path;
    }

    @Tool("Initialize a git repository in the project directory")
    public String init() {
        try (Git git = Git.init().setDirectory(resolveBase().toFile()).call()) {
            return "OK: git repo initialized";
        } catch (GitAPIException e) {
            return "ERROR: " + e.getMessage();
        }
    }

    @Tool("Add all files to staging")
    public String add() {
        try (Git git = Git.open(resolveBase().toFile())) {
            git.add().addFilepattern(".").call();
            return "OK: added all files";
        } catch (GitAPIException | IOException e) {
            return "ERROR: " + e.getMessage();
        }
    }

    @Tool("Commit all staged changes")
    public String commit(@P("commit message") String message) {
        try (Git git = Git.open(resolveBase().toFile())) {
            git.commit().setMessage(message).call();
            return "OK: committed";
        } catch (GitAPIException | IOException e) {
            return "ERROR: " + e.getMessage();
        }
    }

    @Tool("Checkout a new branch")
    public String checkoutBranch(@P("branch name") String branchName) {
        try (Git git = Git.open(resolveBase().toFile())) {
            git.checkout().setCreateBranch(true).setName(branchName).call();
            return "OK: checked out branch " + branchName;
        } catch (GitAPIException | IOException e) {
            return "ERROR: " + e.getMessage();
        }
    }

    @Tool("Get the current branch name")
    public String getCurrentBranch() {
        try (Git git = Git.open(resolveBase().toFile())) {
            return git.getRepository().getBranch();
        } catch (IOException e) {
            return "ERROR: " + e.getMessage();
        }
    }

    @Tool("Push the current branch to remote")
    public String push(@P("remote name") String remote, @P("branch name") String branch) {
        try (Git git = Git.open(resolveBase().toFile())) {
            git.push().setRemote(remote).setRefSpecs(new RefSpec("refs/heads/" + branch)).call();
            return "OK: pushed to " + remote + "/" + branch;
        } catch (GitAPIException | IOException e) {
            return "ERROR: " + e.getMessage();
        }
    }

    @Tool("Get git status")
    public String status() {
        try (Git git = Git.open(resolveBase().toFile())) {
            List<String> status = new ArrayList<>(git.status().call().getUncommittedChanges());
            return status.isEmpty() ? "OK: clean" : "Uncommitted: " + String.join(", ", status);
        } catch (GitAPIException | IOException e) {
            return "ERROR: " + e.getMessage();
        }
    }

    private Path resolveBase() {
        String base = activePath != null ? activePath : projectBasePath;
        return Paths.get(base).toAbsolutePath().normalize();
    }
}