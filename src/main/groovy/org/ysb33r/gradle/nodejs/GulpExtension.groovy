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
import org.gradle.api.Project
import org.ysb33r.gradle.nodejs.impl.gulp.GulpResolver
import org.ysb33r.gradle.nodejs.pkgwrapper.AbstractPackageWrappingExtension
import org.ysb33r.gradle.nodejs.tasks.GulpTask

/** Extension that allows for setting of Gulp configuration at a project or task level.
 *
 * If no executable is set the default will be to install {@link #GulpExtension.GULP_DEFAULT} for a project extension. In the case of
 * a task extension it will default to the project extension's settings.
 *
 * @since 0.1
 */
@CompileStatic
class GulpExtension extends AbstractPackageWrappingExtension<GulpResolver> {
    final static String NAME = 'gulp'

    /**
     *  The default version of Gulp that will be used if nothing is configured.
     */
    final static String GULP_DEFAULT = '3.9.1'

    /** Adds the extension to the project.
     *
     * <p> Sets the default Gulp tag to {@code GULP_DEFAULT}.
     *
     * @param project Project to link to.
     */
    GulpExtension(Project project) {
        super(project)
        setResolver(createResolver())
        setInstallGroup(NpmDependencyGroup.DEVELOPMENT)
        executable( [version : GULP_DEFAULT] as Map<String,Object> )
        this.gulpFile = "${project.projectDir}/gulpfile.js"
        this.requires = []
    }

    /** Adds the extension to a {@link GulpTask} task.
     *
     * <p> Links the executable resolving to the global instance, but allows
     * it to be overriden on a per-task basis.
     *
     * @param task Task to be extended.
     */
    GulpExtension(GulpTask task) {
        super(task)
    }

    /** Appends more require specifications.
     *
     * @param reqs one of more require specifications
     */
    void requires(String... reqs) {
        requires(reqs as List)
    }

    /** Appends more require specifications.
     *
     * @param reqs Iteratable list of package requirements
     */
    void requires(final Iterable<String> reqs) {
        if(this.requires == null) {
            setRequires(reqs)
        } else {
            this.requires.addAll(reqs)
        }
    }

    /** Replace any existing require specifications with a new one.
     *
     * @param reqs Iteratable list of package requirements.
     */
    void setRequires(final Iterable<String> reqs) {
        if(this.requires==null) {
            this.requires= []
        } else {
            this.requires.clear()
        }
        this.requires.addAll(reqs)
    }

    /** Get set of requires that will be passed to Gulp.
     *
     * <p> This is the same as the {@code --requires} command-line.
     *
     * @return An iterable list of require specifications. If the extension is attached to a task and the list is null, the
     *   project extension will be queried. To override the list from the project extension with an empty list call
     *   {@link #setRequires} with an empty list
     */
    Iterable<String> getRequires() {
        if(task) {
            this.requires != null ? this.requires : projectExtension.requires
        } else {
            this.requires
        }
    }

    /** Sets the location of {@code gulpfile.js}
     *
     * @param gulpFileLocation
     */
    void gulpFile(Object gulpFileLocation) {
        setGulpFile(gulpFileLocation)
    }

    /** Sets the location of {@code gulpfile.js}
     *
     * @param gulpFileLocation
     */
    void setGulpFile(Object gulpFileLocation) {
        this.gulpFile  = gulpFileLocation
    }

    /** The location of {@code gulpfile.js}.
     *
     * @return Returns location. If this is a project extension and the location was not
     *  set {@code project.file("${project.projectDir}/gulpfile.js"} will be returned.
     */
    // tag::example-of-task-overriding1[]
    File getGulpFile() {
        if(task) { // <1>
            this.gulpFile != null ? getProject().file(this.gulpFile) : projectExtension.getGulpFile()
        } else {
            getProject().file(this.gulpFile)
        }
    }
    // end::example-of-task-overriding1[]

    /** Returns the name by which the extension is known.
     *
     * @return Extension name. Never null.
     */
    @Override
    protected String getExtensionName() {
        NAME
    }

    /** The entrypoint path relative to the installed package folder
     *
     * @return {@code bin/gulp.js}
     */
    @Override
    protected String getEntryPoint() {
        'bin/gulp.js'
    }

    /** Creates a resolver for this specific extension
     *
     * @return Resolver
     */
    @Override
    protected GulpResolver createResolver() {
        new GulpResolver(getProject(),getNodeJSExtension(),getNpmExtension())
    }


    /** Returns the version of the extension that is attached to a project.
     *
     * @return Project extension.
     */
    // tag::example-of-task-overriding2[]
    private GulpExtension getProjectExtension() {
        task ? (GulpExtension)getProject().extensions.getByName(NAME) : this  // <2>
    }
    // end::example-of-task-overriding2[]

    private Object gulpFile
    private Set<String> requires

}
