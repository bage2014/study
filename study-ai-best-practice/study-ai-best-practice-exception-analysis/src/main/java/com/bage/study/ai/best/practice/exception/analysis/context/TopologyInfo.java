package com.bage.study.ai.best.practice.exception.analysis.context;

import java.time.LocalDateTime;
import java.util.List;

public class TopologyInfo {

    private String appId;
    private String appName;
    private String appType;
    private List<ModuleDependency> modules;
    private List<ApplicationDependency> downstreamApps;
    private List<ApplicationDependency> upstreamApps;

    public TopologyInfo() {
    }

    public TopologyInfo(String appId, String appName, String appType,
                       List<ModuleDependency> modules,
                       List<ApplicationDependency> downstreamApps,
                       List<ApplicationDependency> upstreamApps) {
        this.appId = appId;
        this.appName = appName;
        this.appType = appType;
        this.modules = modules;
        this.downstreamApps = downstreamApps;
        this.upstreamApps = upstreamApps;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public List<ModuleDependency> getModules() {
        return modules;
    }

    public void setModules(List<ModuleDependency> modules) {
        this.modules = modules;
    }

    public List<ApplicationDependency> getDownstreamApps() {
        return downstreamApps;
    }

    public void setDownstreamApps(List<ApplicationDependency> downstreamApps) {
        this.downstreamApps = downstreamApps;
    }

    public List<ApplicationDependency> getUpstreamApps() {
        return upstreamApps;
    }

    public void setUpstreamApps(List<ApplicationDependency> upstreamApps) {
        this.upstreamApps = upstreamApps;
    }

    public static class ModuleDependency {
        private String moduleName;
        private String moduleType;
        private List<DependencyItem> mqDependencies;
        private List<DependencyItem> jobDependencies;
        private List<DependencyItem> dbDependencies;
        private List<DependencyItem> externalServiceDependencies;

        public ModuleDependency() {
        }

        public ModuleDependency(String moduleName, String moduleType) {
            this.moduleName = moduleName;
            this.moduleType = moduleType;
        }

        public String getModuleName() {
            return moduleName;
        }

        public void setModuleName(String moduleName) {
            this.moduleName = moduleName;
        }

        public String getModuleType() {
            return moduleType;
        }

        public void setModuleType(String moduleType) {
            this.moduleType = moduleType;
        }

        public List<DependencyItem> getMqDependencies() {
            return mqDependencies;
        }

        public void setMqDependencies(List<DependencyItem> mqDependencies) {
            this.mqDependencies = mqDependencies;
        }

        public List<DependencyItem> getJobDependencies() {
            return jobDependencies;
        }

        public void setJobDependencies(List<DependencyItem> jobDependencies) {
            this.jobDependencies = jobDependencies;
        }

        public List<DependencyItem> getDbDependencies() {
            return dbDependencies;
        }

        public void setDbDependencies(List<DependencyItem> dbDependencies) {
            this.dbDependencies = dbDependencies;
        }

        public List<DependencyItem> getExternalServiceDependencies() {
            return externalServiceDependencies;
        }

        public void setExternalServiceDependencies(List<DependencyItem> externalServiceDependencies) {
            this.externalServiceDependencies = externalServiceDependencies;
        }
    }

    public static class ApplicationDependency {
        private String appId;
        private String appName;
        private String relationType;
        private String description;

        public ApplicationDependency() {
        }

        public ApplicationDependency(String appId, String appName, String relationType, String description) {
            this.appId = appId;
            this.appName = appName;
            this.relationType = relationType;
            this.description = description;
        }

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public String getRelationType() {
            return relationType;
        }

        public void setRelationType(String relationType) {
            this.relationType = relationType;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public static class DependencyItem {
        private String name;
        private String type;
        private String status;
        private String metrics;
        private boolean hasIssue;

        public DependencyItem() {
        }

        public DependencyItem(String name, String type, String status, String metrics) {
            this.name = name;
            this.type = type;
            this.status = status;
            this.metrics = metrics;
            this.hasIssue = false;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMetrics() {
            return metrics;
        }

        public void setMetrics(String metrics) {
            this.metrics = metrics;
        }

        public boolean isHasIssue() {
            return hasIssue;
        }

        public void setHasIssue(boolean hasIssue) {
            this.hasIssue = hasIssue;
        }
    }
}