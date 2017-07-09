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

/** Create Gulp plugin that applies some conventions.
 *
 * @since 0.1
 */
@CompileStatic
class GulpPlugin implements Plugin<Project> {

    static final String GULP_TASK_NAME = 'gulp'
    static final String GULP_INTERNAL_CONFIGURATION = '$$$$gulp$$$$'

    void apply(Project project) {
        project.apply plugin : 'org.ysb33r.nodejs.gulp.base'
        project.configurations.create(GULP_INTERNAL_CONFIGURATION).visible = false
//        project.tasks.create(GULP_TASK_NAME,GulpTask)
    }
}
