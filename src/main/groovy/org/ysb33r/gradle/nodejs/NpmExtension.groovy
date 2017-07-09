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

package org.ysb33r.gradle.nodejs

import groovy.transform.CompileStatic
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.Task
import org.ysb33r.gradle.nodejs.impl.npm.NpmDistributionResolver
import org.ysb33r.gradle.nodejs.tasks.NpmTask
import org.ysb33r.gradle.olifant.exec.ResolvedExecutable

/** Set up global config or task-based config for NPM.
 *
 * @since 0.1
 */
@CompileStatic
class NpmExtension {

    static final String NAME = 'npm'

    /** Adds the extension to the project.
     *
     * @param project Project to link to.
     */
    NpmExtension(Project project) {
        this.project = project
        this.npmResolver = new NpmDistributionResolver(project)
        this.npmResolver.executable(this.defaultNodejs())
        this.localConfig = new File(project.rootProject.projectDir,'npmrc')
        this.globalConfig = new File(project.gradle.gradleUserHomeDir,'npmrc')
        this.homeDirectory = project.projectDir
    }

    /** Adds the extension to a {@link org.ysb33r.gradle.nodejs.tasks.NpmTask} task.
     *
     * <p> Links the executable resolving to the global instance, but allows
     * it to be overriden on a per-task basis.
     *
     * @param task Task to be extended.
     */
    NpmExtension(NpmTask task) {
        this.task = task
    }

    /** Sets npm executable.
     *
     * It can be passed by a single map option.
     *
     * <code>
     *   // By tag (Gradle will download and cache the correct distribution).
     *   executable tag : '7.10.0'
     *
     *   // By a physical path (
     *   executable path : '/path/to/npm'
     *
     *   // By using searchPath (will attempt to locate in search path).
     *   executable searchPath()
     *
     *   // By using the the {@code npm} that is bundled by the
     *   executable defaultNodejs()
     * </code>
     *
     * If nothing is set the default will be {@code defaultNodejs ( )} for a project extension. In the case of
     * a task extension it will default to the project extension's settings.
     *
     * @param opts Map taking one of the keys or methods mentioned above.
     */
    void executable(final Map<String, Object> opts) {
        if(npmResolver == null) {
            this.npmResolver = new NpmDistributionResolver(project)
        }
        npmResolver.executable(opts)
    }

    /** Resolves a path to a {@code node} executable.
     *
     * <p> If the extension is linked to a task and not the location not configured,
     * a lookup will be performed on the project extension of the same name,
     *
     * @return Returns the path to the located {@code node} executable.
     * @throw {@code GradleException} if location was not configured.
     */
    ResolvedExecutable getResolvedNpmCliJs() {
        if(task) {
            npmResolver ? npmResolver.getResolvedExecutable() : ((NpmExtension)getProject().extensions.getByName(NAME)).getResolvedNpmCliJs()
        } else {
            if(npmResolver == null) {
                throw new GradleException('Method for finding npm-cli.js is not configured')
            }
            npmResolver.getResolvedExecutable()
        }
    }

    /** Sets NPM to be resolved from the default node.js distribution associated with this project.
     *
     * @return Something that is suitable to be passed to @{@link #executable}
     */
    Map<String, Object> defaultNodejs() {
        ['default': ((NodeJSExtension) (getProject().extensions.getByName(NodeJSExtension.NAME)))] as Map<String, Object>
    }

    /** Use this to configure a system path search for {@code npm}
     *
     * @return Returns a special option to be used in {@link #executable}
     */
    Map<String, Object> searchPath() {
        NpmDistributionResolver.SEARCH_PATH
    }

    /** Location & name of global NPM config file.
     *
     * When this extension is attached to a project, the default location is set to
     * {@code "${project.gradle.gradleUserHomeDir}/npmrc"}
     *
     * @return {@link java.io.File} object pointing to global NPM config
     */
    File getGlobalConfig() {
        if(task) {
            this.globalConfig ? project.file(this.globalConfig) : ((NpmExtension)getProject().extensions.getByName(NAME)).getGlobalConfig()
        } else {
            project.file(this.globalConfig)
        }
    }

    /** Set global config file.
     *
     * @param path Anything that can be converted using {@code project.file}.
     */
    void setGlobalConfig(Object path) {
        this.globalConfig = path
    }

    /** Set global config file.
     *
     * @param path Anything that can be converted using {@code project.file}.
     */
    void globalConfig(Object path) {
        setGlobalConfig(path)
    }

    /** Location & name of local NPM config file.
     *
     * When this extension is attached to a project, the default location is set to
     * {@code "${project.rootProject.projectDir}/npmrc"}
     *
     * @return {@link java.io.File} oject pointing to local NPM config
     */
    File getLocalConfig() {
        if(task) {
            this.localConfig ? project.file(this.localConfig) : ((NpmExtension)getProject().extensions.getByName(NAME)).getLocalConfig()
        } else {
            project.file(this.localConfig)
        }
    }

    /** Set local config file.
     *
     * @param path Anything that can be converted using {@code project.file}.
     */
    void setLocalConfig(Object path) {
        this.localConfig = path
    }

    /** Set local config file.
     *
     * @param path Anything that can be converted using {@code project.file}.
     */
    void localConfig(Object path) {
        setLocalConfig(path)
    }

    /** The NPM home directory - the parent directory of {@code node_modules},
     *
     * @return Parent directory of {@code node_modules}. Never null if the extension is tied to a project,
     * in which case it defaults to {@code proect.projectDir}.
     *
     */
    File getHomeDirectory() {
        if(task) {
            this.homeDirectory ? project.file(this.homeDirectory) : ((NpmExtension)getProject().extensions.getByName(NAME)).getHomeDirectory()
        } else {
            project.file(this.homeDirectory)
        }
    }

    /** Sets the home directory.
     *
     * @param homeDir A directory in which {@code node_modules} will be created as a child folder.
     *   Anything that can be resovled with {@code project.file} is acceptable
     */
    void setHomeDirectory(Object homeDir) {
        this.homeDirectory = homeDir
    }

    /** Sets the home directory.
     *
     * @param homeDir A directory in which {@code node_modules} will be created as a child folder.
     *   Anything that can be resovled with {@code project.file} is acceptable
     */
    void homeDirectory(Object homeDir) {
        setHomeDirectory(homeDir)
    }

    /** Obtains the project this is directly or indirectly attached to.
     *
     * @return Project instance. If this extension was attached to a task, the associated project for that task will be returned.
     */
    private Project getProject() {
        this.project ?: task.project
    }

    private Project project
    private Task task
    private NpmDistributionResolver npmResolver
    private Object localConfig
    private Object globalConfig
    private Object homeDirectory
}