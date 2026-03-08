package io.github.freehij.mcartifact

import org.gradle.api.Plugin
import org.gradle.api.Project
import groovy.json.JsonSlurper

class McArtifactExtension {
    String version
    String environment
}

class McArtifactPlugin implements Plugin<Project> {
    void apply(Project project) {
        def extension = project.extensions.create('mcartifact', McArtifactExtension)
        
        project.afterEvaluate {
            def key = "${extension.version}_${extension.environment}"
            def url = "https://raw.githubusercontent.com/freehij/resources/refs/heads/main/versions.json"
            def json = new JsonSlurper().parse(new URL(url))
            
            if (!json[key]) {
                throw new RuntimeException("Key $key not found in versions.json")
            }
            
            def downloadUrl = json[key]
            def fileName = downloadUrl.substring(downloadUrl.lastIndexOf('/') + 1)
            def artifactFile = new File(project.gradle.gradleUserHomeDir, ".gradle/mcartifacts/$key/$fileName")
            
            if (!artifactFile.exists()) {
                artifactFile.parentFile.mkdirs()
                artifactFile << new URL(downloadUrl).openStream()
            }
            
            project.dependencies.add('compileOnly', project.files(artifactFile))
            
            project.tasks.register('downloadMcArtifact') {
                doLast {
                    println "Downloaded: $artifactFile.absolutePath"
                }
            }
        }
    }
}