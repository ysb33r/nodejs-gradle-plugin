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

package org.ysb33r.gradle.nodejs.impl

import groovy.transform.CompileStatic
import org.gradle.api.Project
import org.ysb33r.gradle.nodejs.NodeJSExecSpec
import org.ysb33r.gradle.olifant.exec.ExecSpecInstantiator

/** Creates instances for {@link NodeJSExecSpec}
 *
 * @since 0.1
 */
@CompileStatic
class NodeJSExecSpecInstantiator implements ExecSpecInstantiator<NodeJSExecSpec> {

    /** Instantiates a NPM execution specification.
     *
     * @param project Project that this execution specification will be associated with.
     * @return New NPM execution specification.
     */
    @Override
    NodeJSExecSpec create(Project project) {
        new NodeJSExecSpec(project)
    }

    /** An existing instance that can be used without concurrency issues.
     *
     */
    static final NodeJSExecSpecInstantiator INSTANCE = new NodeJSExecSpecInstantiator()
}
