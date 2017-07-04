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
import org.gradle.process.ProcessForkOptions
import org.ysb33r.gradle.olifant.exec.AbstractCommandExecSpec

/**
 *
 * @since 0.1
 */
@CompileStatic
class NpmExecSpec extends AbstractCommandExecSpec {

    /** Construct class and attach it to specific project.
     *
     * @param project Project this exec spec is attached.
     */
    NpmExecSpec(Project project) {
        super(project)
    }

    /** Do not use this version of the method for NPM.
     *
     * @throw GradleException.
     */
    @Override
    ProcessForkOptions executable(Object o) {
        throw new GradleException( '''Use the Map version to set the executable''' )
    }

    /** Do not use this version of the method for NPM.
     *
     * @throw GradleException.
     */
    @Override
    void setExecutable(Object o) {
        throw new GradleException( '''Use the Map version to set the executable''' )
    }

    /** Do not use this version of the method for NPM.
     *
     * @throw GradleException.
     */
    @Override
    void setExecutable(String s) {
        throw new GradleException( '''Use the Map version to set the executable''' )
    }

//    /** Do not use this version of the method for NPM.
//     *
//     * @throw GradleException.
//     */
//    @Override
//    ProcessForkOptions executable(String s) {
//        throw new GradleException( '''Use the Map version to set the executable''' )
//    }

    @Override
    void setExecutable(Map<String,Object> exe) {

    }

    ProcessForkOptions executable(Map<String,Object> exe) {
        setExecutable(exe)
        return (ProcessForkOptions)this
    }

//    private
}
