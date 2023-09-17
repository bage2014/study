package com.bage.maven.plugins;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
 
import java.util.List;
 
@Mojo(name = "dependency-counter", defaultPhase = LifecyclePhase.COMPILE)
public class DependencyCounterMojo extends AbstractMojo {
 
    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    MavenProject project;
 
    @Parameter(property = "scope")
    String scope;
 
    @Override
    public void execute() {
        List<Dependency> dependencies = project.getDependencies();
        long numDependencies = dependencies.stream()
                .filter(d -> (StringUtils.isEmpty(scope)) || scope.equals(d.getScope()))
                .count();
        if (StringUtils.isEmpty(scope)) {
            getLog().info("bage Number of dependencies: " + numDependencies);
        } else {
            getLog().info("bage Number of '" + scope + "' dependencies: " + numDependencies);
        }
    }
}