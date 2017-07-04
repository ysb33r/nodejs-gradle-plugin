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
import org.gradle.api.Plugin
import org.gradle.api.Project

/** Provide the basic capabilites for dealing with Node.js & NPM tasks. Allow for downlaoding & caching of Node.js distributions
 * on a variery of the most common development platforms.
 *
 * @since 0.1
 */
@CompileStatic
class NodeJSBasePlugin implements Plugin<Project> {
    void apply(Project project) {
        project.extensions.create(NodeJSExtension.NAME,NodeJSExtension,project)
        project.extensions.create(NpmExtension.NAME,NpmExtension,project)
    }
}
