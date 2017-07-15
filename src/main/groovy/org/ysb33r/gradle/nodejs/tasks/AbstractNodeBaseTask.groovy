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

package org.ysb33r.gradle.nodejs.tasks

import groovy.transform.CompileStatic
import org.gradle.api.DefaultTask
import org.ysb33r.gradle.nodejs.NodeJSExtension
import org.ysb33r.gradle.nodejs.NpmExtension

/** A base class that will provide NodeJS and NPM task extensions.
 *
 * @since 0.1
 */
@CompileStatic
class AbstractNodeBaseTask extends DefaultTask {

    protected AbstractNodeBaseTask() {
        super()
        npmExtension = (NpmExtension)(extensions.create(NpmExtension.NAME,NpmExtension,this))
        nodeExtension = (NodeJSExtension)(extensions.create(NodeJSExtension.NAME,NodeJSExtension,this))
    }

    /** Internal access to attached NPM extension.
     *
      * @return NPM extension.
     */
    protected NpmExtension getNpmExtension() {
        this.npmExtension
    }

    /** Internal access to attached NodeJS extension.
     *
     * @return NodeJS extension.
     */
    protected NodeJSExtension getNodeExtension() {
        this.nodeExtension
    }

    private final NpmExtension npmExtension
    private final NodeJSExtension nodeExtension

}
