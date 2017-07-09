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

package org.ysb33r.gradle.nodejs.plugins

import groovy.transform.CompileStatic
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.ysb33r.gradle.nodejs.GulpExtension

/** Provides {@code gulpConfig} extension.
 *
 * @since 0.1
 */
@CompileStatic
class GulpBasePlugin implements Plugin<Project> {

    void apply(Project project) {
        project.apply plugin : 'org.ysb33r.nodejs.npm'

        project.extensions.create(GulpExtension.NAME,GulpExtension,project)
    }
}
