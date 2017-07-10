//
// ============================================================================
// (C) Copyright Schalk W. Cronje 2017
//
// This software is licensed under the Apache License 2.0
// See http://www.apache.org/licenses/LICENSE-2.0 for license details
//
// Unless required by applicable law or agreed to in writing, software distributed under the License is
// distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and limitations under the License.
//
// ============================================================================
//

package org.ysb33r.gradle.nodejs.impl.npm

import groovy.transform.CompileStatic
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.file.FileTree
import org.gradle.process.ExecResult
import org.gradle.process.ExecSpec
import org.ysb33r.gradle.nodejs.NodeJSExtension
import org.ysb33r.gradle.nodejs.NpmDependencyGroup
import org.ysb33r.gradle.nodejs.NpmExecSpec
import org.ysb33r.gradle.nodejs.NpmExtension
import org.ysb33r.gradle.nodejs.NpmPackageDescriptor
import org.ysb33r.gradle.olifant.exec.ResolvedExecutable

/** Utility methods that aids in running NPM
 *
 * @since 0.1
 */
@CompileStatic
class NpmExecutor {

    /** Configures an NpmExecSpec from a NodeJSExtensions and a NpmExtension.
     *
     * <p> Will set {@code npm_config_userconfig} and {@code npm_config_globalconfig}
     *   environmental variables. If the working directory was not set previsouly, it
     *   will set it to the {@code homeDirectory} as deifned by the {@link NpmExtension}.
     *
     * @param execSpec NPM execution spec that needs configuration.
     * @param nodeJS An {@link NodeJSExtension}
     * @param npm An {@link NpmExtension}.
     * @return
     */
    static NpmExecSpec configureSpecFromExtensions(NpmExecSpec execSpec,NodeJSExtension nodeJS, NpmExtension npm) {

        ResolvedExecutable resolver = execSpec.getResolvedExecutable()
        if(resolver == null) {
            resolver = npm.getResolvedNpmCliJs()
            execSpec.setExecutable(  resolver )
        }

        if(execSpec.workingDir == null) {
            execSpec.workingDir npm.homeDirectory
        }

        execSpec.environment  npm_config_userconfig : npm.localConfig.absolutePath,
            npm_config_globalconfig : npm.globalConfig.absolutePath

        execSpec.nodeExecutable(nodeJS.getResolvedNodeExecutable())

        return execSpec
    }

    /** Runs NPM given a fully-configured execution specification.
     *
     * @param project The project in which context this execution will be performed.
     * @param execSpec
     * @return Execution result
     * @throw May throw depending on whether execution specification was mal-configured or whether
     *   execution itself failed.
     */
    static ExecResult runNpm(Project project, NpmExecSpec execSpec) {
        Closure runner = { NpmExecSpec fromSpec, ExecSpec toSpec ->
            fromSpec.copyToExecSpec(toSpec)
        }

        project.exec runner.curry(execSpec)
    }

    /** Installs an NPM package by running {@code npm install} i a controlled environment.
     *
     * <p> This uses the global {@code nodejs} and {@code npm} project extension to find defaults.
     *
     * @param project Gradle {@link org.gradle.api.Project} that this installation is associated with.
     * @param npmPackageDescriptor Description of NPM package
     * @param installGroup Production, development of optional installation.
     * @param additionalArgs Any additional arguments that might be deemed necessary to customise the installation.
     *   This is here to provide flexibility as the auithro cannot foresee all common use cases.
     *   Should rarely be used. Should you find using this method regular occurance it might be prudent to
     *   to raise an issue to ask to a feature update and explaining the context in which this is needed.
     * @return List of files that were installed
     *
     * @sa {@link https://docs.npmjs.com/cli/install}
     */
    static Set<File> installNpmPackage(
        final Project project,
        final NpmPackageDescriptor npmPackageDescriptor,
        final NpmDependencyGroup installGroup,
        Iterable<String> additionalArgs
    ) {
        installNpmPackage(
            project,
            (NodeJSExtension)(project.extensions.getByName(NodeJSExtension.NAME)),
            (NpmExtension)(project.extensions.getByName(NpmExtension.NAME)),
            npmPackageDescriptor,
            installGroup,
            additionalArgs
        )
    }

    /** Installs an NPM package by running {@code npm install} i a controlled environment.
     *
     * @param project Gradle {@link org.gradle.api.Project} that this installation is associated with.
     * @param nodeJSExtension A NodeJS project or task extension.
     * @param npmExtension A NPM project or task extension.
     * @param npmPackageDescriptor Description of NPM package.
     * @param installGroup Production, development of optional installation.
     * @param additionalArgs Any additional arguments that might be deemed necessary to customise the installation.
     *   This is here to provide flexibility as the auithro cannot foresee all common use cases.
     *   Should rarely be used. Should you find using this method regular occurance it might be prudent to
     *   to raise an issue to ask to a feature update and explaining the context in which this is needed.
     * @return List of files that were installed
     *
     * @sa {@link https://docs.npmjs.com/cli/install}
     */
    static Set<File> installNpmPackage(
        final Project project,
        final NodeJSExtension nodeJSExtension,
        final NpmExtension npmExtension,
        final NpmPackageDescriptor npmPackageDescriptor,
        final NpmDependencyGroup installGroup,
        Iterable<String> additionalArgs
    ) {
        NpmExecSpec execSpec = NpmExecSpecInstantiator.INSTANCE.create(project)
        execSpec.command 'install'
        execSpec.cmdArgs npmPackageDescriptor.toString()
        execSpec.cmdArgs "--save-${installGroup.getDependencyGroup()}"
        execSpec.cmdArgs additionalArgs
        configureSpecFromExtensions(execSpec, nodeJSExtension, npmExtension)
        runNpm(project,execSpec).assertNormalExitValue()

        String scope = npmPackageDescriptor.scope ? "@${npmPackageDescriptor.scope}/" : ''
        FileTree fileTree = project.fileTree("${execSpec.workingDir}/node_modules/${scope}${npmPackageDescriptor.packageName}")
        fileTree.files
    }

    /** Inatalls packages from a {@code package.json} description.
     *
     * @param project Project the installation is associated with.
     * @param packageJson Path to a {@code package.json} file.
     * @param additionalArgs Additional arguments to be passed to NPM install command.
     * @return {@link org.gradle.api.FileTree} with lists of files that were installed. Will also include files from
     *   packages that were already there, but which would have been installed otherwise.
     * @throw GradleException if {@code packageJson} does not exist or is not in the {@code project.npm.homeDirectory}.
     */
    static FileTree installPackagesFromDescription(
        final Project project,
        final File packageJson,
        Iterable<String> additionalArgs
    ) {
        installPackagesFromDescription(
            project,
            (NodeJSExtension)(project.extensions.getByName(NodeJSExtension.NAME)),
            (NpmExtension)(project.extensions.getByName(NpmExtension.NAME)),
            packageJson,
            additionalArgs
        )
    }

    /** Inatalls packages from a {@code package.json} description.
     *
     * @param project Project the installation is associated with.
     * @param nodeJSExtension A NodeJS project or task extension.
     * @param npmExtension A NPM project or task extension.
     * @param packageJson Path to a {@code package.json} file.
     * @param additionalArgs Additional arguments to be passed to NPM install command.
     * @return {@link org.gradle.api.FileTree} with lists of files that were installed. Will also include files from
     *   packages that were already there, but which would have been installed otherwise.
     * @throw GradleException if {@code packageJson} does not exist or is not in the {@code npmExtension.homeDirectory}.
     */
    static FileTree installPackagesFromDescription(
        final Project project,
        final NodeJSExtension nodeJSExtension,
        final NpmExtension npmExtension,
        final File packageJson,
        Iterable<String> additionalArgs
    ) {
        if(packageJson.name != 'package.json' || !packageJson.exists() ) {
            throw new GradleException("${packageJson} does not exist or is not a valid description file")
        }

        if(packageJson.parentFile != npmExtension.homeDirectory) {
            throw new GradleException("${packageJson} is not a child of ${npmExtension.homeDirectory}")
        }

        NpmExecSpec execSpec = NpmExecSpecInstantiator.INSTANCE.create(project)
        execSpec.command 'install'
        execSpec.cmdArgs packageJson.parentFile.absolutePath
        execSpec.cmdArgs additionalArgs
        configureSpecFromExtensions(execSpec, nodeJSExtension, npmExtension)
        runNpm(project,execSpec).assertNormalExitValue()
        calculateInstallableFiles(project,npmExtension,packageJson)
    }

    /** Works out where the installaton folder will be for a package.
     *
     * @param project Gradle {@link org.gradle.api.Project} that this installation is associated with.
     * @param nodeJSExtension A NodeJS project or task extension
     * @param npmExtension A NPM project or task extension
     * @param npmPackageDescriptor Description of NPM package
     * @return The path where the package will be installe dto
     */
    static File getPackageInstallationFolder(
        final Project project,
        final NodeJSExtension nodeJSExtension,
        final NpmExtension npmExtension,
        final NpmPackageDescriptor npmPackageDescriptor
    ) {
        NpmExecSpec execSpec = NpmExecSpecInstantiator.INSTANCE.create(project)
        configureSpecFromExtensions(execSpec, nodeJSExtension, npmExtension)
        String scope = npmPackageDescriptor.scope ? "@${npmPackageDescriptor.scope}/" : ''
        new File("${execSpec.workingDir}/node_modules/${scope}${npmPackageDescriptor.packageName}")
    }

    /** Creates a template {@code package.json} file.
     *
     * @param project Project for which the {@package.json} file needs to be created.
     * @param nodeJSExtension Configured {@Link NodeJSExtension}.
     * @param npmExtension Configured {@link NpmExtension}
     * @return Location of the generated file.
     */
    static File initPkgJson(
        final Project project,
        final NodeJSExtension nodeJSExtension,
        final NpmExtension npmExtension
    ) {
        final String name = '"name": "' + project.name + '",'
        final String version = '"tag": "' + project.version + '",'

        NpmExecSpec execSpec = NpmExecSpecInstantiator.INSTANCE.create(project)
        execSpec.command 'init'
        execSpec.cmdArgs '-f', '-q'
        configureSpecFromExtensions(execSpec, nodeJSExtension, npmExtension)
        runNpm(project,execSpec).assertNormalExitValue()

        File packageJson = new File(execSpec.workingDir,'package.json')

        if( !packageJson.exists() ) {
            throw new GradleException("${packageJson.absolutePath} was not created as expected")
        }

        packageJson.text = packageJson.text.
            replaceAll(~/"name":\s+".+?",/,name).
            replaceAll(~/"tag":\s+".+?",/,version)

        return packageJson
    }

    /** Returns a live set of installable files.
     *
     * <p> This is an approximation. It parses the {@code package.json} file to discover dependencies, then
     *   recursively parses all other {@code package.json} files it find in those dependencies.
     *
     * @param project Gradle project this instalalton is associated with
     * @param npmExtension Extension that defines the NPM context.
     * @param rootPackageJson Initial package.json file to start traversal.
     * @return Live file collecton, meaning it is possible to add more package directories.
     *   Returns null if no dependencies, optional dependencies or dev dependencies were found.
     * @throw GradleException if {@code packageJson} does not exist or is not in the {@code npmExtension.homeDirectory}.
     */
    static FileTree calculateInstallableFiles( Project project, NpmExtension npmExtension, File rootPackageJson ) {

        if(rootPackageJson.name != 'package.json' || !rootPackageJson.exists() ) {
            throw new GradleException("${rootPackageJson} does not exist or is not a valid description file")
        }

        if(rootPackageJson.parentFile != npmExtension.homeDirectory) {
            throw new GradleException("${rootPackageJson} is not a child of ${npmExtension.homeDirectory}")
        }


        PackageJson descriptor = PackageJson.parsePackageJson(rootPackageJson)
        Set<String> pkgNames = []
        pkgNames.addAll(descriptor.dependencies.keySet())
        pkgNames.addAll(descriptor.devDependencies.keySet())
        pkgNames.addAll(descriptor.optionalDependencies.keySet())
        if(pkgNames.empty) {
            return null
        }

        String root = npmExtension.homeDirectory.absolutePath
        Set<String> pkgDirectories = pkgNames.collect { String name ->
            "${root}/${name}"
        }

        FileTree tree = project.fileTree( project.files(pkgDirectories) )
        for (String dir : pkgDirectories) {
            File nextPackageJson = new File(root,'package.json')
            if(nextPackageJson.exists()) {
                FileTree nextCollection = calculateInstallableFiles(project,npmExtension,nextPackageJson)
                if(nextCollection != null) {
                    tree.add(nextCollection)
                }
            }
        }

        return tree
    }
}
