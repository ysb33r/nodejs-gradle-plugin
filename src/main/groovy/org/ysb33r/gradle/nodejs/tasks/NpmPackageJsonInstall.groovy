package org.ysb33r.gradle.nodejs.tasks

import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction
import org.ysb33r.gradle.nodejs.impl.npm.NpmExecutor

/** Installs the packages as described by a {@code package.json} file
 *
 * @since 0.1
 */
class NpmPackageJsonInstall extends AbstractNodeBaseTask {

    NpmPackageJsonInstall() {
        super()
    }

    /** The package.json file that this task will query.
     *
     * @return File object.
     */
    @InputFile
    File getPackageJsonFile() {
        project.file("${npmExtension.homeDirectory}/package.json" )
    }

    /** Replace list of arguments with a new list.
     *
     * @param args new arguments to use.
     *
     * @sa https://docs.npmjs.com/cli/install
     */
    void setAdditionalInstallArgs(Iterable<String> args) {
        this.additionalInstallArgs.clear()
        this.additionalInstallArgs.addAll(args)
    }

    /** Adds more installation arguments
     *
     * @param args One or more arguments
     *
     * @sa https://docs.npmjs.com/cli/install
     */
    void additionalInstallArgs(String... args) {
        additionalInstallArgs(args as List)
    }

    /** Adds more installation arguments
     *
     * @param args Iterable list of arguments
     *
     * @sa https://docs.npmjs.com/cli/install
     */
    void additionalInstallArgs(Iterable<String> args) {
        this.additionalInstallArgs.addAll(args)
    }

    /** Customise installation via additional argument that are passed to {@code npm install}.
     *
     * @return List of additional arguments.
     *
     * @sa https://docs.npmjs.com/cli/install
     */
    @Input
    Iterable<String> getAdditionalInstallArgs() {
        this.additionalInstallArgs
    }

    @TaskAction
    void exec() {
        NpmExecutor.installPackagesFromDescription(project,packageJsonFile,this.additionalInstallArgs)
    }

    private List<String> additionalInstallArgs = []
}
