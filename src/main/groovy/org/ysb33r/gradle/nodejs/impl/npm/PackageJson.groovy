package org.ysb33r.gradle.nodejs.impl.npm

import groovy.json.JsonParserType
import groovy.json.JsonSlurper
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic

/** Parses a {@code package.json} file.
 *
 * <p> This is not a complete representation. It is only meant to provide enought information for the plugin
 * to make reasonable decisions about packages.
 *
 * @since 0.1
 */
@CompileStatic
class PackageJson {

    static Object parsePackageJsonFileDynamic(final File packageJson) {
        JsonSlurper parser = new JsonSlurper( type : (packageJson.size() > 4194304 ? JsonParserType.CHARACTER_SOURCE : JsonParserType.INDEX_OVERLAY) )
        parser.parse(packageJson)
    }

    @CompileDynamic
    static PackageJson parsePackageJson(final File packageJson) {
        Object json = parsePackageJsonFileDynamic(packageJson)
        PackageJson descriptor = new PackageJson()

        descriptor.with {
            name = json.name
            version = json.version
            description = json.description
            license = json.license

            if(json.dependencies) {
                dependencies.putAll(json.dependencies)
            }

            if(json.devDependencies) {
                devDependencies.putAll(json.devDependencies)
            }

            if(json.optionalDependencies) {
                optionalDependencies.putAll(json.optionalDependencies)
            }

            if(json.peerDependencies) {
                peerDependencies.putAll(json.peerDependencies)
            }

            if(json.bundledDependencies) {
                bundledDependencies.addAll(json.bundledDependencies)
            }
        }
    }

    String getName() {
        this.name
    }

    String getVersion() {
        this.version
    }

    String getDescription() {
        this.description
    }

    String getLicense() {
        this.license
    }

    Map<String,String> getDependencies() {
        this.dependencies
    }

    Map<String,String> getDevDependencies() {
        this.devDependencies
    }

    Map<String,String> getOptionalDependencies() {
        this.optionalDependencies
    }

    Map<String,String> getPeerDependencies() {
        this.peerDependencies
    }

    Iterable<String> getBundledDependencies() {
        this.bundledDependencies
    }

    private PackageJson() {

    }

    private String name
    private String version
    private String description
    private String license
    private Map<String,String> dependencies = [:]
    private Map<String,String> devDependencies = [:]
    private Map<String,String> optionalDependencies = [:]
    private Map<String,String> peerDependencies = [:]
    private List<String> bundledDependencies = []
}
